package com.so.cdct;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileStream;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
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
    private GenericRecord EMPTY = null;
    private Schema schema;

    {
        try {
            schema = new Schema.Parser().parse(this.getClass().getResourceAsStream("/avro/item.avsc"));
            EMPTY = new GenericData.Record(schema);
            EMPTY.put("code", "");
            EMPTY.put("reservePrice", 0);
        } catch (IOException e) {
            e.printStackTrace();
            //log something useful
        }
    }

    @PostMapping(value = "/bids")
    public ResponseEntity sendBidForProduct(@RequestBody Bid bid) throws IOException {
        GenericRecord response = findItem(bid.getItemCode());
        if (response.equals(EMPTY)) {
            return new ResponseEntity("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        bids.add(bid);
        return new ResponseEntity("Bid confirmed", HttpStatus.OK);
    }

    private GenericRecord findItem(String code) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<ByteArrayResource> response = restTemplate.exchange("http://localhost:8082/items/" + code, HttpMethod.POST, entity, ByteArrayResource.class);

        if (response.getStatusCode().equals(HttpStatus.OK)) {
            ByteArrayResource byteResource = response.getBody();
            DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
            DataFileStream<GenericRecord> streamReader = new DataFileStream<>(byteResource.getInputStream(), datumReader);
            GenericRecord item = null;
            if (streamReader.hasNext()) {
                return streamReader.next(item);
            }
        }
        return EMPTY;
    }
}
