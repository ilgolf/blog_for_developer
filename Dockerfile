## Dockerfile
FROM openjdk:17-slim
EXPOSE 8089
ENV JAR_FILE=/build/libs/blog.jar
VOLUME ["/logs"]
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar", "-Duser.timezone=Asia/Seoul", "-Dspring.profiles.active=local","/app.jar"]