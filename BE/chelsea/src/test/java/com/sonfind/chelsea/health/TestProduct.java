package com.sonfind.chelsea.health;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "products")
@Getter
@NoArgsConstructor
public class TestProduct {
	@Id
	private String id;

	@Field("product_name")
	private String productName;

	private double price;

	private List<String> tags;

	public TestProduct(String productName, double price, List<String> tags) {
		this.productName = productName;
		this.price = price;
		this.tags = tags;
	}
}
