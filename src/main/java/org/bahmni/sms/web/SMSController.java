package org.bahmni.sms.web;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bahmni.sms.SMSSender;
import org.bahmni.sms.impl.DefaultSmsSender;
import org.bahmni.sms.model.SMSContract;
import org.bahmni.sms.web.security.TokenValidator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/notification/sms")
@AllArgsConstructor
public class SMSController {
    private final SMSSender smsSender;
    private static final Logger logger = LogManager.getLogger(DefaultSmsSender.class);

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity sendSMS(@RequestBody SMSContract smsContract, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) throws Exception {
        boolean isValidToken = false;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            isValidToken = TokenValidator.validateToken(token);
        }

        if (isValidToken) {
            return smsSender.send(smsContract.getPhoneNumber(), smsContract.getMessage());
        } else {
            logger.error("Sms service could not authenticate with openmrs");
            return new ResponseEntity<>("Authentication Failure", HttpStatus.FORBIDDEN);
        }
    }
}
