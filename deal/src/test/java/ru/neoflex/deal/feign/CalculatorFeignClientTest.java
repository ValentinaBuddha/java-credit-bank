package ru.neoflex.deal.feign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.dto.ScoringDataDto;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@AutoConfigureWireMock(port = 8081)
@ActiveProfiles("test")
class CalculatorFeignClientTest {

    @Autowired
    private CalculatorFeignClient calculatorFeignClient;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void calculateLoanOffers() throws JsonProcessingException {
        stubFor(post(urlEqualTo("/calculator/offers"))
                .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        assertDoesNotThrow(() -> calculatorFeignClient.calculateLoanOffers(new LoanStatementRequestDto()));

        verify(1, postRequestedFor(urlEqualTo("/calculator/offers")));
        verify(postRequestedFor(urlEqualTo("/calculator/offers"))
                .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
                .withRequestBody(equalToJson(mapper.writeValueAsString(new LoanStatementRequestDto()))));
    }

    @Test
    void calculateCredit() throws JsonProcessingException {
        stubFor(post(urlEqualTo("/calculator/calc"))
                .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        assertDoesNotThrow(() -> calculatorFeignClient.calculateCredit(new ScoringDataDto()));

        verify(1, postRequestedFor(urlEqualTo("/calculator/calc")));
        verify(postRequestedFor(urlEqualTo("/calculator/calc"))
                .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
                .withRequestBody(equalToJson(mapper.writeValueAsString(new ScoringDataDto()))));
    }
}