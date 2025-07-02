package com.retialerApi.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.*;

@Entity
public class orders {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "retailer_username")
    private String retailerUsername;
    public String getRetailerUsername() {
        return retailerUsername;
    }

    public void setRetailerUsername(String retailerUsername) {
        this.retailerUsername = retailerUsername;
    }

    private String customerName;
    private LocalDateTime orderDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	public orders(Long id, String customerName, LocalDateTime orderDate, List<OrderItem> items) {
		super();
		this.id = id;
		this.customerName = customerName;
		this.orderDate = orderDate;
		this.items = items;
	}

	public orders() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", customerName=" + customerName + ", orderDate=" + orderDate + "]";
	}

    
}

