package com.retialerApi.entity;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Retailer {
	@OneToMany(mappedBy = "retailer", cascade = CascadeType.ALL)
	private List<Product> products = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
private String name;

    public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}

	private String username;
    private String password;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Retailer(Long id, String username, String password) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
	}
	public Retailer() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Retailer [id=" + id + ", username=" + username + ", password=" + password + "]";
	}

    
}
