FROM codenvy/jdk7:latest
COPY build/libs/*.jar .
ENTRYPOINT ["java", "-jar", "gradle-simple-2.0-sources.jar"]