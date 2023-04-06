package org.bahmni.sms.web.security;

import org.apache.http.HttpResponseFactory;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bahmni.sms.SMSProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class OpenMRSAuthenticator {

    private static final Logger logger = LogManager.getLogger(OpenMRSAuthenticator.class);
    private static final String WHOAMI_URL = "/bahmnicore/whoami";
    public static final String OPENMRS_SESSION_ID_COOKIE_NAME = "JSESSIONID";

    @Autowired
    private SMSProperties properties;

    public ResponseEntity authenticate(String sessionId) {
        ResponseEntity<Privileges> response = callOpenMRS(sessionId);
        HttpStatus status = response.getStatusCode();
        HttpResponseFactory factory = new DefaultHttpResponseFactory();

        if (status.series() == HttpStatus.Series.SUCCESSFUL) {
            return response.getBody().hasSendSMSPrivilege() ?
                    new ResponseEntity<>("Authentication Success", HttpStatus.OK)
                    : new ResponseEntity<>("SMS sending privilege missing", HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>("Authentication Failure", status);
    }

    public ResponseEntity<Privileges> callOpenMRS(String sessionId) {
        logger.info("Authenticating with OpenMRS");
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", OPENMRS_SESSION_ID_COOKIE_NAME + "=" + sessionId);
        try {
            return new RestTemplate()
                    .exchange(properties.getOpenmrsRootURL() + WHOAMI_URL,
                            HttpMethod.GET,
                            new HttpEntity<>(null, requestHeaders),
                            Privileges.class
                    );
        } catch (HttpClientErrorException exception) {
            logger.warn("Could not authenticate with OpenMRS", exception);
            return new ResponseEntity<>(exception.getStatusCode());
        }
    }
}
