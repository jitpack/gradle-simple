FROM gradle

#RUN gradlew build
RUN gradle build
ENV GRADLE_USER_HOME=/codefresh/volume/.gradle

#WORKDIR /src
#ENV DEBUG=*


#CMD ["gradle", "build"]
