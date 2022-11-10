package org.bahmni.sms.web;

import org.bahmni.sms.SMSSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;


import java.net.URLDecoder;
import java.net.URLEncoder;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class SMSControllerTest {
    @MockBean
    private SMSSender smsSender;

    @Autowired
    private WebTestClient webClient;


    @Test
    public void shouldAcceptTheSMSRequest() {
        webClient.post()
                .uri("/sms/send" + "?phoneNumber=+919865689295&message=hello")
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

    }

    @Test
    public void shouldThrowBadRequest() {
        webClient.post()
                .uri("/sms/send" + "?phoneNumber=+919865689295")
                .exchange()
                .expectStatus()
                .isBadRequest();

    }

    @Test
    public void shouldCallSend() {
        webClient.post()
                .uri("/sms/send" + "?message=hello&phoneNumber=919865689295")
                .exchange();

        Mockito.verify(smsSender, times(1)).send("919865689295", "hello");
    }

}