package domain;

public class OrderedIceCream {
	private IceCream iceCream;
	private int quantity;
	
	public OrderedIceCream() {};
	
	public OrderedIceCream(IceCream iceCream, int quantity) {
		this.iceCream = iceCream;
		this.quantity = quantity;
	}
	
	public IceCream getIceCream() {
		return iceCream;
	}
	
	public void setIceCream(IceCream iceCream) {
		this.iceCream = iceCream;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int num) {
		this.quantity = num;
	}
}
