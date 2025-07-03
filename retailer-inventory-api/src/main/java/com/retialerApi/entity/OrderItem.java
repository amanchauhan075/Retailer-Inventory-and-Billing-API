package com.retialerApi.entity;

import jakarta.persistence.*;

@Entity
public class OrderItem {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    
    
private String customerName;
    public String getCustomerName() {
	return customerName;
}

public void setCustomerName(String customerName) {
	this.customerName = customerName;
}

public void setTotalPrice(Double totalPrice) {
	this.totalPrice = totalPrice;
}


	private String productName;
    private int quantity;
    private double price;
    @Column
    private Double totalPrice;

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }


    @ManyToOne
    @JoinColumn(name = "order_id")
    private orders order;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public orders getOrder() {
		return order;
	}

	public void setOrder(orders order) {
		this.order = order;
	}

	public OrderItem(Long id, String productName, int quantity, double price, orders order) {
		super();
		this.id = id;
		this.productName = productName;
		this.quantity = quantity;
		this.price = price;
		this.order = order;
	}

	public OrderItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", productName=" + productName + ", quantity=" + quantity + ", price=" + price
				+ ", order=" + order + "]";
	}

    
}
