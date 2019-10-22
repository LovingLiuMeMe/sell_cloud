FROM hub.c.163.com/library/java:latest
ADD target/*.jar app.jar
EXPOSE 4321
ENTRYPOINT ["java", "-jar", "/app.jar"]