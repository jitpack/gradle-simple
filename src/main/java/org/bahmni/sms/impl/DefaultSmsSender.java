package org.bahmni.sms.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bahmni.sms.SMSProperties;
import org.bahmni.sms.SMSSender;
import org.bahmni.sms.model.Message;
import org.bahmni.sms.model.SMSRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

import static org.bahmni.sms.Constants.SEND;

@Repository
public class DefaultSmsSender implements SMSSender {

    private final SMSProperties smsProperties;
    private static final Logger logger = LogManager.getLogger(DefaultSmsSender.class);

    public DefaultSmsSender(SMSProperties smsProperties) {
        this.smsProperties = smsProperties;
    }

    @Override
    public ResponseEntity send(String phoneNumber, String messageText) {
        logger.info("Sending SMS for ********" + phoneNumber.substring(phoneNumber.length() - 2));
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(SEND);
            Message message = new Message();
            message.setChannel("sms");
            message.setMsg_type("text");
            message.setRecipients(new ArrayList<String>() {{
                add(phoneNumber);
            }});
            message.setOriginator(smsProperties.getOriginator());
            message.setContent(messageText);
            SMSRequest smsRequest = new SMSRequest();
            smsRequest.setMessages(new ArrayList<Message>() {{
                add(message);
            }});
            ObjectMapper Obj = new ObjectMapper();
            String jsonObject = Obj.writeValueAsString(smsRequest);
            StringEntity params = new StringEntity(jsonObject);
            request.addHeader("content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + smsProperties.getToken());
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            return new ResponseEntity<>(response.getStatusLine().getReasonPhrase(), HttpStatus.valueOf(response.getStatusLine().getStatusCode()));
        } catch (Exception e) {
            logger.warn("Error in sending sms", e);
            return new ResponseEntity<>("Error in sending sms" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
