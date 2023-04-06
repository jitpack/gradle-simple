package org.bahmni.sms.web;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bahmni.sms.SMSSender;
import org.bahmni.sms.model.SMSContract;
import org.bahmni.sms.web.security.OpenMRSAuthenticator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/notification/sms")
@AllArgsConstructor
public class SMSController {
    private final SMSSender smsSender;
    private OpenMRSAuthenticator authenticator;
    private static final Logger logger = LogManager.getLogger(SMSController.class);

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity sendSMS(@RequestBody SMSContract smsContract, @CookieValue("reporting_session") String cookie) throws Exception {
        //TODO: Remove authentication when send SMS invocation is moved to backend [BAH-2962]
        ResponseEntity authenticationResponse = authenticator.authenticate(cookie);
        if (authenticationResponse.getStatusCode().is2xxSuccessful())
            return smsSender.send(smsContract.getPhoneNumber(), smsContract.getMessage());
        return authenticationResponse;
    }
}
