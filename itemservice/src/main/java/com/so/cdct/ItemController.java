package com.so.cdct;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ItemController {

	private List<Item> availableItems;

	{
		availableItems = new ArrayList<>();
		availableItems.add(new Item("A", 100));
		availableItems.add(new Item("B", 200));
		availableItems.add(new Item("C", 300));
	}

	@PostMapping(value = "/items")
	public ResponseEntity<List<Item>> getAvailableItems() {
		return new ResponseEntity(availableItems, HttpStatus.OK);
	}

	@GetMapping(value = "/items/{code}")
	public ResponseEntity<Item> getItem(@PathVariable String code) {
		Item item = availableItems.stream().filter(it -> it.getCode().equals(code)).findFirst().orElse(Item.EMPTY);
		HttpStatus status = item.canEqual(Item.EMPTY) ? HttpStatus.NOT_FOUND : HttpStatus.OK;
		return new ResponseEntity(item, status);
	}
}
