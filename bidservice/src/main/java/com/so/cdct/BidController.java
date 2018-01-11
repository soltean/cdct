package com.so.cdct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BidController {

	@Autowired
	private RestTemplate restTemplate;

	private List<Bid> bids = new ArrayList<>();

	@PostMapping(value = "/bids")
	public ResponseEntity sendBidForProduct(String itemCode, long amount) {
		ResponseEntity<ItemResponse> response = restTemplate.getForEntity("localhost:8082/items/" + itemCode, ItemResponse.class);
		if (response.getStatusCode().equals(HttpStatus.FOUND)) {
			bids.add(new Bid(itemCode, 500));
		} else {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity(HttpStatus.OK);
	}

}
