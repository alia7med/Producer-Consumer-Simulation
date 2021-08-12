package backend;

public class product implements Cloneable {
	private int number;
	private String color;

	public product(int number,String color) {
		this.number = number;
		this.color = color;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}