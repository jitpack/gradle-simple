FROM gradle

RUN ./gradlew build
#RUN gradle build

#WORKDIR /src
#ENV DEBUG=*


#CMD ["gradle", "build"]
