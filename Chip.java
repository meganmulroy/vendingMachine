package com.techelevator;

public class Chip extends Product {
	

	private String sound = "Crunch Crunch, Yum!";

	
	public Chip(String productName, Double productPrice, String productCategory) {
		super(productName, productPrice, productCategory);
	}

	@Override
	public String getSound() {
		return sound;
	}
	
}
