package org.bahmni.sms.web;

import org.bahmni.sms.SMSSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/sms")
public class SMSController {
    private final SMSSender smsSender;

    @Autowired
    public SMSController(SMSSender smsSender){
        this.smsSender = smsSender;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/send")
    public @ResponseBody
    void sendSMS(@Valid @RequestParam String phoneNumber, String message)  {
        smsSender.send("+15135400794", phoneNumber, message);
    }
}
