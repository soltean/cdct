package com.so.cdct;

import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    {
        availableItems = new ArrayList<>();
        availableItems.add(new Item("A", 100));
        availableItems.add(new Item("B", 200));
        availableItems.add(new Item("C", 300));
    }

    //TODO: a avro message with a list
    @GetMapping(value = "/items")
    public ResponseEntity<List<ItemMessage>> getAvailableItems() {
        List<ItemMessage> mappedList = availableItems.stream().map(new ItemMessageMapper()).collect(Collectors.toList());
        return new ResponseEntity(mappedList, HttpStatus.FOUND);
    }

    @GetMapping(value = "/items/{code}")
    public ResponseEntity<byte[]> getItem(@PathVariable String code) throws IOException {
        ItemMessage EMPTY = ItemMessage.newBuilder().setCode("").setReservePrice(0).build();
        ItemMessage item = availableItems.stream().filter(it -> it.getCode().equals(code))
                .findFirst().map(new ItemMessageMapper()).orElseGet(() -> EMPTY);

        DatumWriter<ItemMessage> userDatumWriter = new SpecificDatumWriter<>(ItemMessage.class);
        DataFileWriter<ItemMessage> dataFileWriter = new DataFileWriter<>(userDatumWriter);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        dataFileWriter.create(item.getSchema(), outputStream);
        dataFileWriter.append(item);
        dataFileWriter.close();

        HttpStatus status = item.equals(EMPTY) ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return new ResponseEntity(outputStream.toByteArray(), status);
    }

    private class ItemMessageMapper implements Function<Item, ItemMessage> {

        @Override
        public ItemMessage apply(Item item) {
            return ItemMessage.newBuilder().setCode(item.getCode()).setReservePrice(item.getReservePrice()).build();
        }
    }
}
