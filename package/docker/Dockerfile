FROM amazoncorretto:11.0.18
VOLUME /tmp
COPY build/libs/* app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"] 
