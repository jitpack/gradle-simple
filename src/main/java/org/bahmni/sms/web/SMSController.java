package org.bahmni.sms.web;

import lombok.AllArgsConstructor;
import org.bahmni.sms.SMSSender;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;

@RestController
@RequestMapping(value = "/sms")
@AllArgsConstructor
public class SMSController {
    private final SMSSender smsSender;

    @RequestMapping(method = RequestMethod.POST, value = "/send")
    public @ResponseBody
    String sendSMS(@RequestParam String phoneNumber,@RequestParam String message)  {
        System.out.println("PHONE" + phoneNumber);
        return smsSender.send(phoneNumber, message);
    }
}
