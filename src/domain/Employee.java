package domain;

import java.io.*;

public class Employee {
	private String name, id;
	private double experience;
	private int age;

	public Employee() {}
	
	public Employee(String name, int age, String id, double experience) {
		this.name = name;
		this.age = age;
		this.id = id;
		this.experience = experience;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getExperience() {
		return experience;
	}

	public void setExperience(double experience) {
		this.experience = experience;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	
}