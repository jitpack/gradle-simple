FROM amazoncorretto:11.0.18
RUN yum install openssl -y
COPY package/docker/generate_token.sh /home/
COPY package/docker/sms-startup.sh /
RUN chmod +x /home/generate_token.sh  /sms-startup.sh
COPY build/libs/* app.jar
EXPOSE 8080
VOLUME /tmp
CMD ["./sms-startup.sh"]