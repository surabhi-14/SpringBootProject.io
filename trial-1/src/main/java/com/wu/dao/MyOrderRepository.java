package com.wu.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.wu.model.MyOrder;

@Repository
public interface MyOrderRepository extends JpaRepository<MyOrder,String> {
	
	@Query("from MyOrder as c where c.customer.id =:customer_id")
	public List<MyOrder> findHistoryByCustomer(@Param("customer_id")int customer_id);
}
