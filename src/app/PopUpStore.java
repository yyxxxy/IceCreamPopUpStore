package app;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.*;
import domain.*;

public class PopUpStore {

	public static void main(String[] args) throws IOException {
		menu();
	}

	public static void menu() throws IOException {
		int choice;

		while (true) {
			System.out.println("--------------------------------------");
			System.out.println("Welcome to the Ice Cream Pop Up Store!");
			System.out.println("--------------------------------------");
			System.out.println();
			System.out.println("[1] Login as Customer");
			System.out.println("[2] Login as Staff");
			System.out.println("[3] Login as Admin");
			System.out.println("[0] Exit");
			System.out.print("Enter choice [0-3]: ");

			String input = scanner.nextLine();
			System.out.println();
			try {
				choice = Integer.parseInt(input);
				if (choice < 0 || choice > 3) {
					System.out.println("Invalid choice! Please enter a number between 0 and 3.");
					continue;
				}
			} catch (NumberFormatException e) {
				System.out.println("Not an integer! Please enter a number between 0 and 3");
				System.out.println();
				continue;
			}
			switch (choice) {
			case 0:
				exit();
				break;
			case 1:
				customerInterface();
				break;
			case 2:
				loginAsStaff();
				break;
			case 3:
				loginAsAdmin();
				break;
			default:
				System.out.println("Invalid choice! Please enter a number between 0 and 4.");
				System.out.println();
				continue;
			}
		}

	}

	

	

	public static void createOrder() throws IOException {
		Customer customer;
		Order order = new Order();
		OrderedIceCream orderedIceCream = new OrderedIceCream();

		ArrayList<IceCream> iceCreamList = new ArrayList<IceCream>();
		ArrayList<OrderedIceCream> orderedIceCreamList = new ArrayList<OrderedIceCream>();

		int quantity;
		String id, name, email, phoneNum;
		LocalDate orderDate;

		while (true) {
			try {
				id = order.generateOrderId();
				System.out.println("Order ID: " + id); // generate orderId
				System.out.print("Enter Date [YYYY-MM-DD]: ");
				orderDate = LocalDate.parse(scanner.nextLine()); // user input date manually
				// input customer info
				System.out.print("Enter Name: ");
				name = scanner.nextLine();
				System.out.print("Enter Email Address: ");
				email = scanner.nextLine();
				while (true) {
					System.out.print("Enter Phone Number (60123456789): ");
					phoneNum = scanner.nextLine();
					if (phoneNum.length() == 11) {
						break;
					} else {
						System.out.println("Input Lenngth Error, Try Again!");
						continue;
					}
				}
				customer = new Customer(name, email, phoneNum);

				while (true) {
					// read from file then store it in list & display ice cream menu
					iceCreamList = getIceCreamMenu(iceCreamList);
					displayIceCreamMenu(iceCreamList);

					// enter ice cream option
					System.out.print("Enter ice cream flavour ('0' to Stop): ");
					String flavor = scanner.nextLine();

					// enter '0' to stop looping after ice cream selection
					if (flavor.equals("0")) {
						break;
					}

					// perform a loop if ice cream option not found
					int flavorIndex = Integer.parseInt(flavor) - 1;
					if (flavorIndex < 0 || flavorIndex >= iceCreamList.size()) {
						System.out.println("Invalid flavor index. Please try again!");
						continue;
					}
					// store selected iceCream option in 'selectedIceCream' if ice cream option
					// found
					IceCream selectedIceCream = iceCreamList.get(flavorIndex);

					// enter quantity needed for that flavor
					System.out.print("Enter quantity: ");
					quantity = scanner.nextInt();
					scanner.nextLine();

					// if enter negative value or>10, perform loop
					// dk if we need this
					if (quantity <= 0 || quantity > 10) {
						System.out.println("Invalid quantity. Please enter a number between 1 and 10!");
						continue;
					}

					orderedIceCream = new OrderedIceCream(selectedIceCream, quantity);
					// add all selected ice cream to a list
					orderedIceCreamList.add(orderedIceCream);
				}
				break;
			} catch (DateTimeParseException e) {// catch false input for date
				System.out.println("Invalid date format. Please enter date in 'YYYY-MM-DD' format. ");
				System.out.println();
				continue;
			} catch (NumberFormatException e) {// catch false input for int values
				System.out.println("Not an integer! Please enter a number between 0 and 6");
				System.out.println();
				continue;
			}

		}

		// calculate price of all selected ice cream in an order
		double orderedPrice = calculateTotalPrice(orderedIceCreamList);

		order = new Order(id, customer, orderedIceCreamList, orderDate, orderedPrice);

		// write one order into orderFile.txt
		try (FileWriter writer = new FileWriter("orderFile.txt", true)) {
			// Append the order to the end of the file
			writer.write(order.getOrderId() + "," + order.getOrderDate() + "," + customer.getCustName() + ","
					+ customer.getCustEmail() + "," + customer.getCustPhoneNum() + ",");
			for (OrderedIceCream o : orderedIceCreamList) {
				writer.write(o.getIceCream().getFlavor() + "," + o.getQuantity() + ",");
			}
			writer.write(order.getOrderPrice() + "\n");
		} catch (IOException e) {
			System.out.println("Failed to write order to file.");
			return;
		}

		// display created order/ 'receipt'
		viewOrder();
	}

	// display order positioned lowest/bottom in orderFile.txt
	//only customer can have access to this method, cuz staff can viewAll
	public static void viewOrder() throws IOException {

		try (Scanner input = new Scanner(new File("orderFile.txt"))) {

			String lastLine = null;

			// get last line of order in orderFile
			while (input.hasNextLine()) {
				lastLine = input.nextLine();
			}
			// check if the orderFile is empty
			if (lastLine == null) {
				System.out.println("No orders found.");
				menu();
				return;
			}
			// display order
			String[] field = lastLine.split(",");
			String orderId = field[0];
			LocalDate orderDate = LocalDate.parse(field[1]);
			String custName = field[2];
			String custEmail = field[3];
			String custPhoneNum = field[4];
			System.out.println("=============================");
			System.out.println("     Billing Statement");
			System.out.println("=============================");
			String orderIdInFive = orderId.substring(orderId.length() - 5);
			System.out.println("Order ID O: " + orderIdInFive);
			System.out.println("Order date: " + orderDate);
			System.out.println("Customer Name: " + custName);
			System.out.println("Customer Email Address: " + custEmail);
			System.out.println("Customer Phone Number: " + custPhoneNum);
			System.out.println("-----------------------------");
			System.out.printf("%-20s %8s\n", "Flavor", "Quantity");
			// get all flavor and quantity
			for (int i = 5; i < field.length - 1; i += 2) {
				String flavor = field[i];
				int quantity = Integer.parseInt(field[i + 1]);
				System.out.printf("%-20s %8s\n", flavor, quantity);
			}
			System.out.println("=============================");
			double price = Double.parseDouble(field[field.length - 1]);
			System.out.println("Total Price: " + price);
			System.out.println("=============================");
			System.out.println();
		} catch (FileNotFoundException e) {
			System.out.println("No orders found.");
		} catch (IOException e) {
			System.out.println("Error reading from file: " + e.getMessage());
		}
		menu();
	}

	// the update is more like delete then add except the orderId stays the same
	public static void updateOrder() throws IOException {
		ArrayList<IceCream> iceCreamList = new ArrayList<IceCream>();// define an empty ice cream list
		// read ice cream flavor & price
		iceCreamList = getIceCreamMenu(iceCreamList);// basically fill the empty ice cream list with flavors and price
														// from iceCreamFile

		// Read the existing orders from the file into a list
		List<Order> orderList = new ArrayList<>();// create an empty list for orders
		orderList = readAllOrderToList(orderList);// fill all orders from orderFIle into the order list

		Order orderSearched = searchOrder(orderList);
		String orderId = orderSearched.getOrderId();
		orderList.remove(orderSearched);

		// almost similar to createOrder() just that the orderID remain unchanged
		ArrayList<OrderedIceCream> orderedIceCreamList = new ArrayList<OrderedIceCream>();
		LocalDate orderDate;
		String name, email, phoneNum;
		Customer customer;
		// this line can delete after all done
		System.out.println("Order ID in Update Function = " + orderId);
		while (true) {
			// just add this line then will not stop prompting
			if (orderId != null) {
				try {
					System.out.println("Order ID: " + orderId); // get previous orderId
					System.out.println("Enter Date [YYYY-MM-DD]: ");
					orderDate = LocalDate.parse(scanner.nextLine()); // user input date manually

					// input customer info
					System.out.println("Enter Name: ");
					name = scanner.nextLine();
					System.out.println("Enter Email Address: ");
					email = scanner.nextLine();
					while (true) {
						System.out.println("Enter Phone Number (60123456789: ");
						phoneNum = scanner.nextLine();
						if (phoneNum.length() == 11) {
							break;
						} else
							continue;
					}
					customer = new Customer(name, email, phoneNum);
					while (true) {//till here
						// display ice cream flavor & price
						displayIceCreamMenu(iceCreamList);
						System.out.print("Enter ice cream flavour ('0' to Stop): ");
						String flavor = scanner.nextLine();
						// stop loop
						if (flavor.equals("0")) {
							break;
						}
						int flavorIndex = Integer.parseInt(flavor) - 1;
						if (flavorIndex < 0 || flavorIndex >= iceCreamList.size()) {
							System.out.println("Invalid flavor index. Please try again!");
							continue;
						}
						IceCream selectedIceCream = iceCreamList.get(flavorIndex);
						System.out.print("Enter quantity: ");
						int quantity = scanner.nextInt();
						scanner.nextLine();
						if (quantity <= 0 || quantity > 6) {
							System.out.println("Invalid quantity. Please enter a number between 1 and 6!");
							continue;
						}
						OrderedIceCream orderedIceCream = new OrderedIceCream(selectedIceCream, quantity);

						orderedIceCreamList.add(orderedIceCream);
					}
					break;
				} catch (DateTimeParseException e) {
					System.out.println("Invalid date format. Please enter date in 'YYYY-MM-DD' format. ");
					System.out.println();
					continue;
				} catch (NumberFormatException e) {
					System.out.println("Not an integer! Please enter a number between 0 and 6");
					System.out.println();
					continue;
				}

			}
		}
		// calculate price of all selected ice cream in an order
		double orderedPrice = calculateTotalPrice(orderedIceCreamList);

		// add updated order at the end of the list
		orderSearched = new Order(orderSearched.getOrderId(), customer, orderedIceCreamList, orderDate, orderedPrice);
		orderList.add(orderSearched);

		// Write the updated order list back to the file
		writeOrderListToOrderFile(orderList);

		staffInterface();
	}

	// reminder: this method can only work if searchOrder() is completed so please
	// be patient ;)
	public static void cancelOrder() throws IOException {
		// Read the existing orders from the file into a list
		List<Order> orderList = new ArrayList<>();
		orderList = readAllOrderToList(orderList);

		Order orderSearched = searchOrder(orderList);
		String orderId = orderSearched.getOrderId();

		// Find the order to cancel and remove it from the list
		Order orderToCancel = null;
		for (Order order : orderList) {
			if (order.getOrderId().equals(orderId)) {
				orderToCancel = order;
				break;
			}
		}
		if (orderToCancel == null) {
			System.out.println("Order with ID " + orderId + " not found.");
			menu();
		}
		orderList.remove(orderToCancel);

		// Write the updated order list back to the file
		writeOrderListToOrderFile(orderList);

		System.out.println("Order with ID " + orderId + " canceled successfully.");
		staffInterface();
	}

	// search order
	public static Order searchOrder(List<Order> orderedList) throws IOException {
		String input;
		System.out.print("\nPlease enter the order id to perform : ");
		input = scanner.nextLine();
		Order selectedorder = new Order();
		if ((orderedList) != null) {
			for (Order o : orderedList) {
				String word = o.getOrderId();
				if (word.equals(input)) {
					selectedorder = o;
				} else {
					System.out.println("Order Id not found!");
					updateOrder();
				}
			}
		}
		return selectedorder;
	}

	// create employee profile
	public static void createUserProfile() throws IOException {
		Employee em = new Employee();
		String name, id;
		int age;
		double experience;
		System.out.println("\nEnter employee ic (12 digits): ");
		id = scanner.next();                                                                                                                                                                                                                                      
		if (id.length() != 12) {
			System.out.println("Please enter a valid ic number !");
			createUserProfile();
		}
		System.out.println("Enter employee name: ");
		name = scanner.next();
		try {
			System.out.println("Enter employee experience (in years): ");
			experience = scanner.nextDouble();
			em.setExperience(experience);
			System.out.println("Enter employee age: ");
			age = scanner.nextInt();
			em.setAge(age);
			scanner.nextLine();
		}catch (InputMismatchException e) {
			System.out.println("Please enter a numeric value.");
			createUserProfile();
		}
		
		em = new Employee(name, em.getAge(), id, em.getExperience());
		// write input into the file
		try (FileWriter writer = new FileWriter("userFile", true)) {
			writer.write(em.getId() + "," + em.getName() + "," + em.getExperience() + "," + em.getAge() + "\n");
		} catch (IOException e) {
			System.out.println("Failed to write employee details to file.");
			createUserProfile();
		}
		adminMenu();
	}

	// view all employees' profile
	public static void viewUserProfile() throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("userFile"));
			String line = reader.readLine();
			while (line != null) {
				String[] field = line.split(",");
				String employeeIc = field[0];
				String employeeName = field[1];
				double experience = Double.parseDouble(field[2]);
				int age = Integer.parseInt(field[3]);

				System.out.println("=======================================");
				System.out.println("            Employee Profile           ");
				System.out.println("=======================================");
				System.out.println("Employee IC : " + employeeIc);
				System.out.println("---------------------------------------");
				System.out.println("Employee Name : " + employeeName);
				System.out.println("---------------------------------------");
				System.out.println("Employee Experience : " + experience + " years");
				System.out.println("---------------------------------------");
				System.out.println("Employee age : " + age + " years old");
				System.out.println("=======================================");

				// read next line
				line = reader.readLine();

			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		adminMenu();
	}

	
	public static void viewAllOrder() {
		List<Order> order = new ArrayList<>();
		order = readAllOrderToList(order);

		int count = 0;

		System.out.println("========================================================");
		System.out.println("                       ORDERS");
		System.out.println("========================================================");

		for (Order o : order) {
			System.out.println("Order ID: " + o.getOrderId());
			System.out.println("Order date: " + o.getOrderDate());
			System.out.println("Customer Name: " + o.getCustomer().getCustName());
			System.out.println("Customer Email Address: " + o.getCustomer().getCustEmail());
			System.out.println("Customer Phone Number: " + o.getCustomer().getCustPhoneNum());
			System.out.println("Ordered Ice Cream:");
			for (OrderedIceCream i : o.getOrderedIceCreamList()) {
				count++;
				System.out.printf("%d. %-20s x %d\n", count, i.getIceCream().getFlavor(), i.getQuantity());
			}
			System.out.println("----------------------------------------------------");
			System.out.println("Total Price: " + o.getOrderPrice());
			System.out.println("----------------------------------------------------");
		}
	}

	// ===========================================================================================================
	/*
	 * ALL METHODS BELOW ARE NOT FUNCTIONALITY!!
	 */
	// ===========================================================================================================

	// for all input
	private static Scanner scanner = new Scanner(System.in);
	
	// admin login function
		public static void loginAsAdmin() throws IOException {
			System.out.print("Please enter admin password :");
			String password = "123admin";
			String input = scanner.next();
			if (input.equalsIgnoreCase(password)) {
				adminMenu();
			}
		}

	// display main menu of the system
	public static void displayStaffInterface() {
		System.out.println("================");
		System.out.println("STAFF INTERFACE");
		System.out.println("================");
		System.out.println();
		System.out.println("Please select an option:");
		System.out.println("[1] Update Order");
		System.out.println("[2] Cancel Order");
		System.out.println("[3] View all Order");
		System.out.println("[0] Exit to Main Menu");
		System.out.print("Enter your choice: ");
	}

	// read flavor&price from iceCReamFile and return an array of ice cream
	public static ArrayList<IceCream> getIceCreamMenu(ArrayList<IceCream> iceCreamList) {
		iceCreamList = new ArrayList<IceCream>();
		double price;
		// read ice cream flavor & price from iceCreamFile.txt
		try (Scanner input = new Scanner(new File("iceCreamFile.txt"))) {
			while (input.hasNextLine()) {
				String line = input.nextLine();
				String[] fields = line.split(",");
				if (fields.length < 2) {
					System.out.println("Invalid line format: " + line);
					continue;
				}
				String flavor = fields[0];
				price = Double.parseDouble(fields[1]);
				iceCreamList.add(new IceCream(flavor, price));
			}
		} catch (IOException e) {
			System.out.println("Failed to read ice cream list from file.");
			System.exit(0);
		}

		return iceCreamList;
	}

	// display ice cream menu
	public static void displayIceCreamMenu(ArrayList<IceCream> iceCreamList) {
		System.out.println("Ice Cream Menu: ");
		for (int i = 0; i < iceCreamList.size(); i++) {
			System.out.println(
					"[" + (i + 1) + "]" + iceCreamList.get(i).getFlavor() + " RM" + iceCreamList.get(i).getPrice());
		}
	}

	// calculate total price of ordered ice cream
	public static double calculateTotalPrice(ArrayList<OrderedIceCream> orderedIceCreamList) {
		double totalPrice = 0;
		for (OrderedIceCream o : orderedIceCreamList) {
			totalPrice += o.getQuantity() * o.getIceCream().getPrice();
		}
		return totalPrice;
	}

	// read all order from orderFile and store in list
	public static List<Order> readAllOrderToList(List<Order> orderList) {
		try (Scanner input = new Scanner(new File("orderFile.txt"))) {
			while (input.hasNextLine()) {
				String line = input.nextLine();
				String[] field = line.split(",");
				String currentOrderId = field[0];
				LocalDate orderDate = LocalDate.parse(field[1]);
				String custName = field[2];
				String custEmail = field[3];
				String custPhoneNum = field[4];
				Customer customer = new Customer(custName, custEmail, custPhoneNum);
				ArrayList<OrderedIceCream> orderedIceCreamList = new ArrayList<>();
				for (int i = 5; i < field.length - 1; i += 2) {
					String flavor = field[i];
					int quantity = Integer.parseInt(field[i + 1]);
					orderedIceCreamList.add(new OrderedIceCream(new IceCream(flavor, 0), quantity));
				}
				double orderPrice = Double.parseDouble(field[field.length - 1]);
				orderList.add(new Order(currentOrderId, customer, orderedIceCreamList, orderDate, orderPrice));
			}
		} catch (IOException e) {
			System.out.println("Failed to read order list from file.");
			System.exit(0);
		}
		return orderList;
	}

	// write orderList into orderFile.txt
	public static void writeOrderListToOrderFile(List<Order> orderList) {
		try (PrintWriter output = new PrintWriter(new File("orderFile.txt"))) {
			for (Order order : orderList) {
				output.print(order.getOrderId() + "," + order.getOrderDate() + "," + order.getCustomer().getCustName()
						+ "," + order.getCustomer().getCustEmail() + "," + order.getCustomer().getCustPhoneNum() + ",");
				for (OrderedIceCream o : order.getOrderedIceCreamList()) {
					output.print(o.getIceCream().getFlavor() + "," + o.getQuantity() + "\n");
				}
			}
		} catch (IOException e) {
			System.out.println("Failed to write updated order list to file.");
			return;
		}
	}

	// display admin menu
	public static void adminMenu() throws IOException {
		System.out.println("=========================================");
		System.out.println("               ADMIN MENU                ");
		System.out.println("=========================================");
		System.out.println("Please choose your action: ");
		System.out.println("1.Create employee profile");
		System.out.println("2.View all employees profile");
		System.out.println("3.Exit to Main Menu");
		System.out.print("Enter choice[1-3]: ");
		
		int adminAction = scanner.nextInt();
		if (adminAction == 1) {
			createUserProfile();
		} else if (adminAction == 2) {
			viewUserProfile();
		} else if (adminAction == 3) {
			menu();
		} else {
			throw new IOException("Please enter a number between 1 and 3");
		}
	}
	
	public static void customerInterface() throws IOException {
		System.out.println("===================");
		System.out.println("CUSTOMER INTERFACE");
		System.out.println("===================");
		createOrder();
	}
	
	public static void loginAsStaff() throws IOException {
		String staffPass = "123staff";
		String inputPass;
		System.out.print("Enter Staff Password: ");
		inputPass = scanner.nextLine();
		if (inputPass.equalsIgnoreCase(staffPass)) {
			staffInterface();
		}else {
			System.out.println("Erorr!! Wrong Password input!");
			menu();
		}
	}
	public static void staffInterface() throws IOException {
		
			int choice;
			while (true) {

				displayStaffInterface();

				String input = scanner.nextLine();
				System.out.println();
				try {
					choice = Integer.parseInt(input);
					if (choice < 0 || choice > 3) {
						System.out.println("Invalid choice! Please enter a number between 0 and 3.");
						continue;
					}
				} catch (NumberFormatException e) {
					System.out.println("Not an integer! Please enter a number between 0 and 3");
					System.out.println();
					continue;
				}
				switch (choice) {
				case 0:
					menu();
					break;
				case 1:
					updateOrder();
					break;
				case 2:
					cancelOrder();
					break;
				case 3:
					viewAllOrder();
					break;
				default:
					System.out.println("Invalid choice! Please enter a number between 0 and 3.");
					System.out.println();
					continue;

				}
			}
		} 

	public static void exit() {
		// The 'Thank You' will look neat on the console. just. TRUST.
		System.out.println("=========================================================");
		System.out.println("  _____ _                 _     __   __          ");
		System.out.println(" |_   _| |__   __ _ _ __ | | __ \\ \\ / /__  _   _ ");
		System.out.println("   | | | '_ \\ / _` | '_ \\| |/ /  \\ V / _ \\| | | |");
		System.out.println("   | | | | | | (_| | | | |   <    | | (_) | |_| |");
		System.out.println("   |_| |_| |_|\\__,_|_| |_|_|\\_\\   |_|\\___/ \\__,_|");
		System.out.println("=========================================================");
		System.exit(0);
	}
}