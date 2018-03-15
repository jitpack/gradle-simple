FROM gradle

#RUN gradlew build
RUN gradle build
ENV GRADLE_USER_HOME=${{CF_VOLUME_PATH}}/.gradle

#WORKDIR /src
#ENV DEBUG=*


#CMD ["gradle", "build"]
