package com.techelevator;

public class Drink extends Product {

	private String sound = "Glug Glug, Yum!";

	
	public Drink(String productName, Double productPrice, String productCategory) {
		super(productName, productPrice, productCategory);
	}

	@Override
	public String getSound() {
		return sound;
	}

}
