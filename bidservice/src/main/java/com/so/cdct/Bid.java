package com.so.cdct;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Bid {

	private String itemCode;
	private int amount;

}
