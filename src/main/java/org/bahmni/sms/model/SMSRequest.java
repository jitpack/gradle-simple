package org.bahmni.sms.model;

import java.util.List;

public class SMSRequest {
    public List<Message> messages;

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
