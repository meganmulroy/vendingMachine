package com.techelevator;

public class Gum extends Product {

	private String sound = "Chew Chew, Yum!";
	
	public Gum(String productName, Double productPrice, String productCategory) {
		super(productName, productPrice, productCategory);
	}

	@Override
	public String getSound() {
		return sound;
	}

}
