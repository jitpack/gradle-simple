package org.bahmni.sms.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.bahmni.sms.SMSProperties;
import org.bahmni.sms.SMSSender;
import org.bahmni.sms.model.Message;
import org.bahmni.sms.model.SMSRequest;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static org.bahmni.sms.Constants.AUTHORIZATION;
import static org.bahmni.sms.Constants.SEND;

@Repository
public class DefaultSmsSender implements SMSSender {

    private final SMSProperties smsProperties;

    public DefaultSmsSender(SMSProperties smsProperties) {
        this.smsProperties = smsProperties;
    }

    @Override
    public String send(String phoneNumber, String messageText) {
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
            return response.getStatusLine().toString();
        } catch (Exception e) {
            return "Error " + e;
        }
    }
}
