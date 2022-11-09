package org.bahmni.sms;

public interface SMSSender {
    String send(String sender, String phoneNumber, String message);
}
