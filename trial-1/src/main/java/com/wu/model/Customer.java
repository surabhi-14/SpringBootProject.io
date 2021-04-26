package com.wu.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name="CUSTOMER")
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int  id;
	@NotBlank(message="Name should not be blank")
	@Size(min=2,max=20,message="min2 and max 20 characters required")
	private String customerName;
    private String email;
    private String password;
    private String phoneNumber;
    private String amount;
    private String role;
    
    @OneToMany(mappedBy="customer",cascade = CascadeType.ALL,orphanRemoval=true)
	private List<MyOrder> myOrder = new ArrayList<>();
	
    
    public List<MyOrder> getMyOrder() {
		return myOrder;
	}
	public void setMyOrder(List<MyOrder> myOrder) {
		this.myOrder = myOrder;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Customer [id=" + id + ", customerName=" + customerName + ", email=" + email + ", password=" + password
				+ ", phoneNumber=" + phoneNumber + ", amount=" + amount + "]";
	}
    

}
