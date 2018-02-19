package com.so.cdct;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
public class ItemController {

    private List<Item> availableItems;
    private Schema schema;
    private GenericRecord EMPTY;

    {
        availableItems = new ArrayList<>();
        availableItems.add(new Item("A", 100));
        availableItems.add(new Item("B", 200));
        availableItems.add(new Item("C", 300));
        try {
            schema = new Schema.Parser().parse(this.getClass().getResourceAsStream("/avro/item.avsc"));
            EMPTY = new GenericData.Record(schema);
            EMPTY.put("code", "");
            EMPTY.put("reservePrice", 0);
        } catch (IOException e) {
            //log something useful
        }
    }

    //TODO: a avro message with a list
    @GetMapping(value = "/items")
    public ResponseEntity<List<GenericRecord>> getAvailableItems() {
        List<GenericRecord> mappedList = availableItems.stream().map(new ItemMessageMapper()).collect(Collectors.toList());
        return new ResponseEntity(mappedList, HttpStatus.FOUND);
    }

    @GetMapping(value = "/items/{code}")
    public ResponseEntity<byte[]> getItem(@PathVariable String code) throws IOException {

        GenericRecord item = availableItems.stream().filter(it -> it.getCode().equals(code))
                .findFirst().map(new ItemMessageMapper()).orElseGet(() -> EMPTY);

        DatumWriter<GenericRecord> userDatumWriter = new GenericDatumWriter<>();
        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<>(userDatumWriter);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        dataFileWriter.create(item.getSchema(), outputStream);
        dataFileWriter.append(item);
        dataFileWriter.close();

        HttpStatus status = item.equals(EMPTY) ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return new ResponseEntity(outputStream.toByteArray(), status);
    }


    private class ItemMessageMapper implements Function<Item, GenericRecord> {

        @Override
        public GenericRecord apply(Item item) {
            GenericRecord itemMessage = new GenericData.Record(schema);
            itemMessage.put("code", item.getCode());
            itemMessage.put("reservePrice", item.getReservePrice());
            return itemMessage;
        }
    }
}
