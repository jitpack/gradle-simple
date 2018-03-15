#FROM gradle
FROM openjdk:9-jre-slim as builder
ARG GRADLE_USER_HOME
#RUN gradlew build
RUN gradle build

#WORKDIR /src
#ENV DEBUG=*


#CMD ["gradle", "build"]
