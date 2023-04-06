package org.bahmni.sms;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sms")
@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Builder
public class SMSProperties {
    private String originator;
    private String token;
    private String openmrsHost;
    private String openmrsPort;
    private String openmrsRootURL;

    public String getOpenmrsRootURL() {
        return "http://" + openmrsHost + ":" + openmrsPort + "/openmrs/ws/rest/v1";
    }
}
