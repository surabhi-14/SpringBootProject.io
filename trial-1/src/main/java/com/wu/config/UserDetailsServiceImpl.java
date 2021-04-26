package com.wu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.wu.dao.CustomerRepository;
import com.wu.model.Customer;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private CustomerRepository customerRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		//fetching customer from db
		Customer customer = customerRepository.getCustomerByCustomerName(username);
		
		if(customer==null) {
			throw new UsernameNotFoundException("could not found customer!!");
		}
		
		CustomUserDetails customCustomerDetails = new CustomUserDetails(customer);
		
		return customCustomerDetails;
	}

}
