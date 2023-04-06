package org.bahmni.sms.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicStatusLine;
import org.bahmni.sms.SMSProperties;
import org.bahmni.sms.SMSSender;
import org.bahmni.sms.model.Message;
import org.bahmni.sms.model.SMSRequest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

import static org.bahmni.sms.Constants.SEND;

@Repository
public class DefaultSmsSender implements SMSSender {

    private final SMSProperties smsProperties;

    public DefaultSmsSender(SMSProperties smsProperties) {
        this.smsProperties = smsProperties;
    }

    @Override
    public String send(String phoneNumber, String messageText) {
        HttpResponseFactory factory = new DefaultHttpResponseFactory();
        HttpResponse response = null;
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
            response = httpClient.execute(request);
        } catch (Exception e) {
            System.out.println("Error in sending sms" + e);
            response = factory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_INTERNAL_SERVER_ERROR, "Error in sending sms"), null);
        }
        return response.getStatusLine().toString();
    }
}
