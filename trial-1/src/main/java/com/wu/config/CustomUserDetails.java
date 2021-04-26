package com.wu.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.wu.model.Customer;

public class CustomUserDetails implements UserDetails{

	private Customer customer; 
	
	
	public CustomUserDetails(Customer customer) {
		super();
		this.customer = customer;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		SimpleGrantedAuthority simpleGrantedAuthority= new SimpleGrantedAuthority(customer.getRole());
		
		return List.of(simpleGrantedAuthority);
	}

	@Override
	public String getPassword() {
		return customer.getPassword();
	}

	@Override
	public String getUsername() {
		return customer.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
