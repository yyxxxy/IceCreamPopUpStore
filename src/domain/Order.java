package domain;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {
	 	private String orderId;
	 	private Customer customer;
	    private ArrayList<OrderedIceCream> orderedIceCreamList;
	    private LocalDate orderDate;
	    private double orderPrice;
	    
	    public Order() {};

	    public Order(String orderId, Customer customer, ArrayList<OrderedIceCream> orderedIceCreamList, LocalDate orderDate,double orderPrice) {
	        this.orderId = orderId;
	        this.customer = customer;
	        this.orderedIceCreamList = orderedIceCreamList;
	        this.orderDate = orderDate;
	        this.orderPrice = orderPrice;
	    }
	    
	    public String generateOrderId() { 
	        int maxId = 0;
	        try {
	            File file = new File("orderFile.txt");
	            List<String> existingIds = Files.readAllLines(file.toPath()); //read all lines to a list
	            if (!existingIds.isEmpty()) { // check if list is empty
	                for (String id: existingIds) { //iterate list
	                    String[] fields = id.split(",");//separate all data by','
	                    int orderId = Integer.parseInt(fields[0]);
	                    if(orderId > maxId) {
	                        maxId = orderId;//get the max id
	                    }
	                }
	            }
	            else maxId = 0;
	        } catch(IOException e) {
	            System.out.println("Error reading order IDs file: " + e.getMessage());
	        }
	        String newId = String.format("%05d", maxId + 1);// max+1 will always get unique id
	        return newId;
	    }
	    

	    public String getOrderId() {
	        return orderId;
	    }
	    
	    public Customer getCustomer() {
	    	return customer;
	    }

	    public ArrayList<OrderedIceCream> getOrderedIceCreamList() {
	        return orderedIceCreamList;
	    }
	    
	    public void setIceCreamList(ArrayList<OrderedIceCream> orderedIceCreamList) {
	    	 this.orderedIceCreamList = orderedIceCreamList;
	    }

	    public LocalDate getOrderDate() {
	        return orderDate;
	    }
	    
	    public void setOrderDate(LocalDate orderDate) {
	    	this.orderDate = orderDate;
	    }

	    public double getOrderPrice() {
	        return orderPrice;
	    }
	    
}
