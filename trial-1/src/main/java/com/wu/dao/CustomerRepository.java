package com.wu.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wu.model.Customer;


@Repository
public interface CustomerRepository  extends JpaRepository<Customer, Integer>{

	@Query("select c from Customer c where c.email=:email")
	public Customer getCustomerByCustomerName(@Param("email") String email);
}
