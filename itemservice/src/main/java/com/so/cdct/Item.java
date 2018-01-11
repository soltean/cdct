package com.so.cdct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

	private String code;
	private int reservePrice;

	public static final Item EMPTY = new Item() {

		@Override
		public void setCode(String code) {
		}

		@Override
		public void setReservePrice(int reservePrice) {
		}
	};
}

