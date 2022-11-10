package org.bahmni.sms;

public interface SMSSender {
    String send(String phoneNumber, String message);
}
