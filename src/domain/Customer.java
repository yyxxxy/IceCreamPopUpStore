package domain;

public class Customer {
	private String custName, custEmail, custPhoneNum;
	
	public Customer (String custName, String custEmail, String custPhoneNum) {
		this.custName = custName;
		this.custEmail = custEmail;
		this.custPhoneNum = custPhoneNum;
	}
	
	public String getCustName() {
		return custName;
	}
	public String getCustEmail() {
		return custEmail;
	}
	public String getCustPhoneNum() {
		return custPhoneNum;
	}
}
