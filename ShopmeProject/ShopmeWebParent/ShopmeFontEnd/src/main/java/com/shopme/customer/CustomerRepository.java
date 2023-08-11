package com.shopme.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	@Query("select c from Customer c Where c.email=?1")
	public Customer findByEmail(String email);
	@Query("select c from Customer c where c.verificationCode=?1")
	public Customer findByVerificationCode(String code);
	@Query("update Customer c set c.enabled = true, c.verificationCode=null where c.id=?1")
	@Modifying
	public void enabled(Integer id);
	
}
