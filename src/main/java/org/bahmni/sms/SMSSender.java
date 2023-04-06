package org.bahmni.sms;

import org.springframework.http.ResponseEntity;

public interface SMSSender {
    ResponseEntity send(String phoneNumber, String message);
}
