package com.techelevator;

public class Candy extends Product {

	private String sound = "Munch Munch, Yum!";
	
	public Candy(String productName, Double productPrice, String productCategory) {
		super(productName, productPrice, productCategory);
	}

	@Override
	public String getSound() {
		return sound;
	}
	
}