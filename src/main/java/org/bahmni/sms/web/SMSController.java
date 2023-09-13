package org.bahmni.sms.web;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bahmni.sms.SMSSender;
import org.bahmni.sms.model.SMSContract;
import org.bahmni.sms.web.security.OpenMRSAuthenticator;
import org.bahmni.sms.web.security.TokenValidator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/notification/sms")
@AllArgsConstructor
public class SMSController {
    private final SMSSender smsSender;
    private OpenMRSAuthenticator authenticator;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity sendSMS(@RequestBody SMSContract smsContract, @CookieValue(value = "reporting_session", required = false) String cookie, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) throws Exception {
        boolean isValidToken = false;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            isValidToken = TokenValidator.validateToken(token);
        }

        if (isValidToken) {
            return smsSender.send(smsContract.getPhoneNumber(), smsContract.getMessage());
        } else {
            ResponseEntity authenticationResponse = authenticator.authenticate(cookie);
            if (authenticationResponse.getStatusCode().is2xxSuccessful()) {
                return smsSender.send(smsContract.getPhoneNumber(), smsContract.getMessage());
            } else {
                return authenticationResponse;
            }
        }
    }
}
