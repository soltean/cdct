package com.so.cdct;

import org.apache.avro.file.DataFileStream;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class BidController {

    @Autowired
    private RestTemplate restTemplate;

    private List<Bid> bids = new ArrayList<>();

    private final ItemMessage EMPTY = ItemMessage.newBuilder().setCode("").setReservePrice(0).build();

    @PostMapping(value = "/bids")
    public ResponseEntity sendBidForProduct(@RequestBody Bid bid) throws IOException {
        ItemMessage response = findItem(bid.getItemCode());
        if (response.equals(EMPTY)) {
            return new ResponseEntity("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        bids.add(bid);
        return new ResponseEntity("Bid confirmed", HttpStatus.OK);
    }

    private ItemMessage findItem(String code) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<ByteArrayResource> response = restTemplate.exchange("http://localhost:8082/items/" + code, HttpMethod.GET, entity, ByteArrayResource.class);

        if (response.getStatusCode().equals(HttpStatus.OK)) {
            ByteArrayResource byteResource = response.getBody();
            DatumReader<ItemMessage> datumReader = new SpecificDatumReader<>(ItemMessage.class);
            DataFileStream<ItemMessage> streamReader = new DataFileStream<>(byteResource.getInputStream(), datumReader);
            ItemMessage item = new ItemMessage();
            if (streamReader.hasNext()) {
                return streamReader.next(item);
            }
        }
        return EMPTY;
    }
}
