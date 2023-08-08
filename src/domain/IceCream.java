package domain;

public class IceCream {

	private String flavor;
    private double price;
    
    public IceCream() {};

    public IceCream(String flavor, double price) {
        this.flavor = flavor;
        this.price = price;
    }

    public String getFlavor() {
        return flavor;
    }

    public double getPrice() {
        return price;
    }
     
    
}
