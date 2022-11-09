package org.bahmni.sms.web;

import org.bahmni.sms.SMSSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/rest/v1/send")
public class SMSController {
    private final SMSSender smsSender;

    @Autowired
    public SMSController(SMSSender smsSender){
        this.smsSender = smsSender;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/sms")
    public @ResponseBody
    ResponseEntity.BodyBuilder sendSMS(@Valid @RequestParam String phoneNumber, String message)  {
        smsSender.send("+15135400794", phoneNumber, message);
        return ResponseEntity.ok();
    }
}
