FROM gradle

RUN gradle build

WORKDIR /src
#ENV DEBUG=*


#CMD ["npm", "start"]
