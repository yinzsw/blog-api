FROM openjdk:11-jre-slim

MAINTAINER yinzsw@qq.com

ADD .env.production /.env.production
ADD target/app.jar /app.jar

ENTRYPOINT ["java","-Dserver.port=4000","-jar","app.jar"]

EXPOSE 4000 4000
