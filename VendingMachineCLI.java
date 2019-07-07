package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.techelevator.view.Menu;

public class VendingMachineCLI {

	Scanner userInput = new Scanner(System.in);

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String MAIN_MENU_OPTION_SALES_REPORT = "Sales Report**";

	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, 
														MAIN_MENU_OPTION_PURCHASE,
														MAIN_MENU_OPTION_EXIT,
														MAIN_MENU_OPTION_SALES_REPORT};
	
	private static final String PURCHASE_MENU_OPTION_FEED_MONEY = "Insert money";
	private static final String PURCHASE_MENU_OPTION_SELECT_PRODUCT = "Select Item";
	private static final String PURCHASE_MENU_OPTION_FINISH_TRANSACTION = "Finish transaction";
	private static final String[] PURCHASE_MENU_OPTIONS = { PURCHASE_MENU_OPTION_FEED_MONEY,
															PURCHASE_MENU_OPTION_SELECT_PRODUCT, 
															PURCHASE_MENU_OPTION_FINISH_TRANSACTION };

	private Menu menu;

	VendingMachine ourVendingMachine = new VendingMachine();

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public void run() {
		boolean finished = false;
		boolean mainMenu = true;

		while (finished == false) {

			if (mainMenu) {
				String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

				if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {

					System.out.println();
					System.out.format("%-8s%-25s%-11s%-10s\n", "SLOT", "ITEM", "PRICE", "AVAILABLE");
					System.out.println("-------------------------------------------------------");

					System.out.println(ourVendingMachine.getDisplay());
					
					System.out.println("-------------------------------------------------------");

				} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
					mainMenu = false;

				} else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
					ourVendingMachine.outputAuditReport();
					finished = true;
					System.out.println("Thank you for your business!");
				} else if (choice.equals(MAIN_MENU_OPTION_SALES_REPORT)) {
					ourVendingMachine.outputSalesReport();
					System.out.println("Amount sold and total sales saved to Sales Report.");
				}
			} else {
				String purchaseChoice = (String) menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);

				if (purchaseChoice.equals(PURCHASE_MENU_OPTION_FEED_MONEY)) {
					try {
						System.out.println("How much money would you like to insert? ");
						String moneyInserted = userInput.nextLine();
						int inserted = Integer.parseInt(moneyInserted);
						ourVendingMachine.feedMoney(inserted);
						System.out.print("Your current balance is: $");
						System.out.format("%.2f\n", ourVendingMachine.getCustomerBalance());
					} catch (NumberFormatException e) {
						System.out.println("Please insert a whole dollar amount. Thanks!");
					}
				} else if (purchaseChoice.equals(PURCHASE_MENU_OPTION_SELECT_PRODUCT)) {
					System.out.println("Which slot would you like a product from? ");
					String slotSelected = userInput.nextLine();
					
					if(ourVendingMachine.getCustomerBalance() <= 0) {
						System.out.print("Your balance is $");
						System.out.format("%.2f", ourVendingMachine.getCustomerBalance());
						System.out.print(". Please insert money!");
					}
					else {
					if(ourVendingMachine.slotExists(slotSelected) == false) {
						System.out.println("That is not a valid slot in our vending machine.");
					}
					else {
						Product currentProduct = ourVendingMachine.getCurrentProduct(slotSelected);
						Double productPrice = currentProduct.getProductPrice();
						if(productPrice > ourVendingMachine.getCustomerBalance()) {
							System.out.print("This item costs $");
							System.out.format("%.2f", productPrice);
							System.out.print(". Your balance is only $");
							System.out.format("%.2f", ourVendingMachine.getCustomerBalance());
							System.out.print(". Please insert money or pick a different item.");
						} 
						else {
							if (ourVendingMachine.purchaseProduct(slotSelected)) {
							System.out.print(currentProduct.getSound() + " Thank you for your purchase of " + currentProduct.getProductName() + " for $");
							System.out.format("%.2f", productPrice);
							System.out.print("! Your new balance is $");
							System.out.format("%.2f", ourVendingMachine.getCustomerBalance());
							System.out.format("%s\n", ".");
							} 
							else {
								System.out.println(slotSelected + " is empty!!");
							}
						}
					}
					}
				}			

				 else if (purchaseChoice.equals(PURCHASE_MENU_OPTION_FINISH_TRANSACTION)) {
					System.out.println("Your change is: " 
										+ ourVendingMachine.getChange(ourVendingMachine.getCustomerBalance()));
					mainMenu = true;
				}
			}
		}
	}

	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
	}
}
