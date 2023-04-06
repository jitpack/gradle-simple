package org.bahmni.sms.web;

import lombok.AllArgsConstructor;
import org.bahmni.sms.SMSSender;

import org.bahmni.sms.model.SMSContract;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(value = "/notification/sms")
@AllArgsConstructor
public class SMSController {
    private final SMSSender smsSender;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String sendSMS(@RequestBody SMSContract smsContract) throws Exception {
        return smsSender.send(smsContract.getPhoneNumber(), smsContract.getMessage());
    }
}
