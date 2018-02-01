package com.so.cdct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = { "com.so:item-service:1.0-SNAPSHOT:+:stubs:8082" }, workOffline = true)
@DirtiesContext
public abstract class BidServiceTest {

	@Autowired
	private RestTemplate restTemplate;

	@Test
	public void testBidShouldBeAdded() {
		ResponseEntity<String> addBid = restTemplate.postForEntity("/bids", "", String.class);

	}

}
