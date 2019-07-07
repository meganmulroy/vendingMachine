package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class VendingMachine {
	
	private final static String CATEGORY_CHIP = "Chip";
	private final static String CATEGORY_CANDY = "Candy";
	private final static String CATEGORY_GUM = "Gum";
	private final static String CATEGORY_DRINK = "Drink";
	
	//Map slots and instances of Product objects
	private Map<String, Product> allTheProducts = new LinkedHashMap<String, Product>();

	private List<String> auditFile = new ArrayList<String>();	
	private List<String> salesFile = new ArrayList<String>();
		
	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

	private Double customerBalance = 0.00;
	private Double vendMachBalance = 0.00;

	public VendingMachine() {
		setupVendingMachine();
	}
	
	public Double getCustomerBalance() {
		return customerBalance;
	}
	
	public void setCustomerBalance(Double customerBalance) {
		this.customerBalance = customerBalance;
	}
	
	public Double getVendMachBalance() {
		return vendMachBalance;
	}
	
	public Product getCurrentProduct(String slotSelected) {
		return allTheProducts.get(slotSelected);
	}
	
	
	//Method to read in vending machine slots and product assigned to each slot
	//Creates an instance of each Product based on product type
	public Map<String, Product> setupVendingMachine() {
		File vendMachFile = new File("VendingMachine.txt");

		try (Scanner menuScanner = new Scanner(vendMachFile)) {
			while (menuScanner.hasNextLine()) {
				String itemDetails = menuScanner.nextLine();
				String[] itemDetailsSplit = itemDetails.split("\\|");
				String slot = itemDetailsSplit[0];
				String productName = itemDetailsSplit[1];
				Double productPrice = Double.parseDouble(itemDetailsSplit[2]);
				String productCategory = itemDetailsSplit[3];
				if(productCategory.contentEquals(CATEGORY_CHIP)) {
					Chip selectedItem = new Chip(productName, productPrice, productCategory);
					allTheProducts.put(slot, selectedItem);
				}
				else if(productCategory.contentEquals(CATEGORY_CANDY)) {
					Candy selectedItem = new Candy(productName, productPrice, productCategory);
					allTheProducts.put(slot, selectedItem);

				}
				else if(productCategory.contentEquals(CATEGORY_GUM)) {
					Gum selectedItem = new Gum(productName, productPrice, productCategory);
					allTheProducts.put(slot, selectedItem);

				}				
				else if(productCategory.contentEquals(CATEGORY_DRINK)) {
					Drink selectedItem = new Drink(productName, productPrice, productCategory);
					allTheProducts.put(slot, selectedItem);

				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Your Vending Machine file does not exist.");
		}
		return allTheProducts;
	}
	
	//Method to printout Vending Machine options 
	//when 1) Display Vending Machine Items is selected on menu
	public String getDisplay(){
		
		String vendingLine = "";
		
		for(String key : allTheProducts.keySet()) {
			Product currentProduct = getCurrentProduct(key);
			if(currentProduct.getProductInventory() > 0) {
			vendingLine += String.format("%-8s", key) + String.format("%-25s", currentProduct.getProductName()) 
				+ String.format("$%-10.2f", currentProduct.getProductPrice()) 
				+ String.format("%-10s\n", currentProduct.getProductInventory());
			} else {
				vendingLine += String.format("%-8s", key) + String.format("%-25s\n", "SOLD OUT!!");
			}
		}
		return vendingLine;
	}
	
	//Adds money to customer balance as it is inserted.
	//Adds String to auditFile list to eventually be printed in Audit File when exiting vending machine.
	public void feedMoney(int moneyInserted) {
		customerBalance += moneyInserted;
		String auditLineDetails = currentFormattedTime() + " FEED MONEY: $" + moneyInserted + ".00 $" 
									+ String.format("$%.2f",getCustomerBalance());
		auditFile.add(auditLineDetails);
	}

	//Method to ensure slot trying to purchase from exists in the Vending Machine
	public boolean slotExists(String slotSelected) {
		boolean slotExists = false;
		
		if (allTheProducts.containsKey(slotSelected)) {
			slotExists=true;
		}
		return slotExists;
	}
	
	//Completes purchase of product: updates inventory, customer balance, vendMachBalance
	//Adds String to auditFile list to eventually be printed in Audit File when exiting vending machine.
	public boolean purchaseProduct(String slotSelected) {
		boolean purchasedAnItem = false;
		Product currentProduct = allTheProducts.get(slotSelected);
		int inventory = currentProduct.getProductInventory();
		if (inventory > 0) {
			inventory -= 1;
			currentProduct.setProductInventory(inventory);
			allTheProducts.put(slotSelected, currentProduct);
			customerBalance -= currentProduct.getProductPrice();
			vendMachBalance += currentProduct.getProductPrice();
			String auditLineDetails = currentFormattedTime() + " " + slotSelected + " " + currentProduct + " $"
					+ String.format("$%.2f", getCustomerBalance());
			auditFile.add(auditLineDetails);
			purchasedAnItem = true;
		}
		return purchasedAnItem;
	}
	
	//Returns a String for change based on customer balance when completing a transaction
	//Adds String to auditFile list to eventually be printed in Audit File when exiting vending machine.
	public String getChange(Double currentBalance) {
		String change = "";
		
		int dollarCounter = 0;
		int quarterCounter = 0;
		int dimeCounter = 0;
		int nickelCounter = 0;
		
		int pennies = (int) (currentBalance * 100);
		
		dollarCounter = pennies /100;
		change += dollarCounter + " dollars, ";
		pennies = pennies % 100;		
		quarterCounter = pennies / 25;
		change += quarterCounter + " quarters, ";
		pennies = pennies % 25;
		dimeCounter = pennies / 10;
		change += dimeCounter + " dimes, ";
		pennies = pennies % 10;
		nickelCounter = pennies / 5;
		change += nickelCounter + " nickels.";
		
		String auditLineDetails = currentFormattedTime() + " GIVE CHANGE: " + String.format("$%.2f", getCustomerBalance()) + " $0.00";
		addAuditLine(auditLineDetails);	
		setCustomerBalance(0.00);

		return change;
	}

	//calls current time for naming files
	public String currentTime() {
		String currentTime = "";
		LocalDateTime now = LocalDateTime.now();
		currentTime += now;
		return currentTime;
	}
	
	//calls current time in a formatted String for inclusion in Audit File
	public String currentFormattedTime() {
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}
	
	//Method used in various other methods to add lines to Audit File list, for eventually printing
	public void addAuditLine(String auditLine) {
		auditFile.add(auditLine);
	}
	
	//Creates a file and writes each item from the Audit File list as a a line
	public void outputAuditReport() {
		String fileName = currentTime() + " AuditReport.txt";
		File destFile = new File(fileName);
		try (PrintWriter writer = new PrintWriter(destFile)) {
			if (destFile.createNewFile() || destFile.exists()) {
				for (int i = 0; i < auditFile.size(); i++) {
					writer.println(auditFile.get(i));
				}
			} else {
				System.out.println("Audit file to print to not found!");
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
		} catch (IOException e) {
			System.out.println("Can't create new file.");
		}
	}
	
	//Creates a file and writes each item from the Sales File list as a a line
	public void outputSalesReport() {
		for (String key : allTheProducts.keySet()) {
			Product currentProduct = allTheProducts.get(key);
			
			String salesReportLine = String.format("%-22s", currentProduct.getProductName()) 
									+ String.format("|%-3s|", (currentProduct.getStartingInventory() - currentProduct.getProductInventory()));			
			salesFile.add(salesReportLine);
		}
		
		String fileName = currentTime() + " SalesReport.txt";
		File destFile = new File(fileName);
		try (PrintWriter writer = new PrintWriter(destFile)) {
			if (destFile.createNewFile() || destFile.exists()) {
				for (int i = 0; i < salesFile.size(); i++) {
					writer.println(salesFile.get(i));
				}
				writer.println();
				writer.print("***TOTAL SALES*** $");
				writer.format("%.2f", vendMachBalance);
				
			} else {
				System.out.println("Sales file to print to not found!");
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
		} catch (IOException e) {
			System.out.println("Can't create new file.");
		}
	}
	
}

	

