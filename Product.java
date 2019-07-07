package com.techelevator;

public abstract class Product {
	
	private String productName;
	private Double productPrice;
	private int productInventory;
	private String productCategory;
	public static final int STARTING_INVENTORY = 5;

	public Product(String productName, Double productPrice, String productCategory) {
		this.productName = productName;
		this.productPrice = productPrice;
		this.productCategory = productCategory;
		productInventory = STARTING_INVENTORY;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public String getProductName() {
		return productName;
	}

	public Double getProductPrice() {
		return productPrice;
	}
	
	public int getProductInventory() {
		return productInventory;
	}
	
	public void setProductInventory(int productInventory) {
		this.productInventory = productInventory;
	}
	
	public int getStartingInventory() {
		return STARTING_INVENTORY;
	}
	
	public int getInventorySold() {
		return STARTING_INVENTORY - productInventory;
	}
	
	//Product sound is declared in each item class
	abstract String getSound();
	
	@Override
	public String toString() {
		return (productName + " $" + String.format("$%.2f", productPrice));
	}
	

}
