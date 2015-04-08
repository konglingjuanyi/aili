package org.hbhk.aili.webflow.share.model;

import java.io.Serializable;

public class Product implements Serializable {

	private static final long serialVersionUID = -2027021562412577102L;
	private int id;
	private String description;

	private String name;

	private int price;

	public Product() {
	}

	public Product(int id, String description, int price) {
		this.id = id;
		this.description = description;
		this.price = price;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
