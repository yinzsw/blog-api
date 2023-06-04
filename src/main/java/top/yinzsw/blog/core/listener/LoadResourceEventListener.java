package top.yinzsw.blog.core.listener;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;
import top.yinzsw.blog.config.BlogConfig;
import top.yinzsw.blog.manager.ResourceManager;
import top.yinzsw.blog.model.po.ResourcePO;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 监听SpringBoot启动
 *
 * @author yinzsW
 * @since 23/01/16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoadResourceEventListener implements ApplicationListener<ApplicationStartedEvent> {
    private final ResourceManager resourceManager;
    private final BlogConfig blogConfig;

    @Override
    public void onApplicationEvent(@NonNull ApplicationStartedEvent event) {
        if (blogConfig.isInitResources()) {
            log.info("系统资源初始化开始...");
            List<ResourcePO> resources = getResources();
            resourceManager.saveInitialResources(resources);
            log.info("系统资源初始化完毕");
        }
    }

    @SneakyThrows
    private List<ResourcePO> getResources() {
        String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath("top.yinzsw.blog.controller") + "/**/*.class";
        List<String> anonymousResourceNames = List.of(
                "getArticle", "listTopArticles", "pagePreviewArticles",
                "pagePreviewArticlesByCategoryId", "pagePreviewArticlesByTagId", "pageArchivesArticles",
                "searchArticles", "updateArticleIsLiked",
                "authorize", "sendEmailCode", "register",
                "pageCategories", "listHotCategories",
                "listHotComment", "pageTopicComments", "pageReplyComments", "updateUserPasswordByEmailCode",
                "pageBackgroundFriendLinks",
                "report", "getBlogHomeInfo", "getAbout",
                "pageTags", "listHotTags"
        );

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory factory = new CachingMetadataReaderFactory(resolver);

        List<ResourcePO> resourcePOList = new ArrayList<>();
        for (Resource resource : resolver.getResources(pattern)) {
            MetadataReader metadataReader = factory.getMetadataReader(resource);
            String className = metadataReader.getClassMetadata().getClassName();
            Class<?> clazz = Class.forName(className);
            if (!clazz.isAnnotationPresent(Tag.class)) {
                continue;
            }

            String module = String.join("", clazz.getAnnotation(RequestMapping.class).value()).replaceAll("/", "").toLowerCase();
            String moduleName = clazz.getAnnotation(Tag.class).name();

            for (Method declaredMethod : clazz.getDeclaredMethods()) {
                RequestMapping requestMapping = AnnotatedElementUtils.getMergedAnnotation(declaredMethod, RequestMapping.class);
                if (Objects.isNull(requestMapping)) {
                    continue;
                }

                String name = declaredMethod.getName();
                String description = declaredMethod.getAnnotation(Operation.class).summary();

                String requestUri = UriComponentsBuilder
                        .fromPath("/".concat(module).concat("/"))
                        .path(String.join("", requestMapping.value()))
                        .build()
                        .toUriString();
                String uri = StringUtils.trimTrailingCharacter(requestUri, '/');
                String method = Arrays.stream(requestMapping.method()).map(Enum::name).findFirst().orElse("");
                boolean isAnonymous = anonymousResourceNames.contains(name);

                ResourcePO resourcePO = new ResourcePO()
                        .setModule(module)
                        .setModuleName(moduleName)
                        .setName(name)
                        .setDescription(description)
                        .setUri(uri)
                        .setMethod(method)
                        .setIsAnonymous(isAnonymous);
                resourcePOList.add(resourcePO);
            }
        }

        return resourcePOList;
    }
}
