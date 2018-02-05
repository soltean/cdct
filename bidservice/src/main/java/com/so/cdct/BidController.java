package com.so.cdct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity sendBidForProduct(@RequestBody Bid bid) {
        ResponseEntity<ItemResponse> response = findItem(bid.getItemCode());
        if (response.getStatusCode().equals(HttpStatus.FOUND)) {
            bids.add(new Bid(bid.getItemCode(), bid.getAmount()));
        } else {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity("Bid confirmed", HttpStatus.OK);
    }

    private ResponseEntity<ItemResponse> findItem(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange("http://localhost:8082/items/" + code, HttpMethod.GET, entity, ItemResponse.class);
    }

}
