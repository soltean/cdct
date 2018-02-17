package com.so.cdct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@AutoConfigureStubRunner(ids = {"com.so:item-service:+:stubs:8082"}, workOffline = true)
public  class BidServiceTest {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testBidShouldBeAdded() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("{\"itemCode\":\"A\",\"amount\":\"500\"}", headers);
        ResponseEntity<String> addBid = restTemplate.postForEntity("http://localhost:8081/bids", entity, String.class);
        assertTrue(addBid.getStatusCodeValue() == 200);
        assertTrue(addBid.getBody().equals("Bid confirmed"));
    }
}
