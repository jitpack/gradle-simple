package org.bahmni.sms.web;

import org.bahmni.sms.SMSSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.Mockito.times;

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
        Object requestBody = "{" +
                "\"phoneNumber\":\"+919999999999\"," +
                "\"message\":\"hello\"" +
                "}";
        webClient.post()
                .uri("/notification/sms")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }

    @Test
    public void shouldThrowBadRequest() {
        Object requestBody = "{" +
                "'phoneNumber':'+919999999999'," +
                "'message':'hello'" +
                "}";
        webClient.post()
                .uri("/notification/sms")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    public void shouldCallSend() {
        Object requestBody = "{" +
                "\"message\":\"hello\"," +
                "\"phoneNumber\":\"919999999999\"" +
                "}";
        webClient.post()
                .uri("/notification/sms")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange();

        Mockito.verify(smsSender, times(1)).send("919999999999", "hello");
    }

}