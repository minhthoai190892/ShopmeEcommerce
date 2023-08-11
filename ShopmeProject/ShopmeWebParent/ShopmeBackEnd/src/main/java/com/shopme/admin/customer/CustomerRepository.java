package com.shopme.admin.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	/**
	 * Hàm phân trang và tìm kiếm theo email, firstName,lastName,addressLine1,addressLine2,city,state,postalCode,country
	 * @param keyword từ khóa cần tìm kiếm
	 * @param pageable 
	 * */
	@Query("select c from Customer c where concat(c.email,' ',c.firstName,' ',c.lastName,' ',c.addressLine1,' ',c.addressLine2,' ',c.city,' ',c.state,' ',c.postalCode,' ',c.countryName)like %?1%")
	public Page<Customer> findAll(String keyword,Pageable pageable);
	/**
	 * Hàm update enabled
	 * @param id id của customer
	 * @param enabled trạng thái enabled (true/false)
	 * */
	@Query("Update Customer c set c.enabled=?2 where c.id=?1")
	@Modifying
	public void updateEnabledStatus(Integer id,boolean enabled);
	/**
	 * Hàm tìm kiếm customer bằng Email
	 * @param email email của customer cần tìm
	 * */
	@Query("select c from Customer c where c.email=?1")
	public Customer findByEmail(String email);
	
	public long countId(Integer id);
}
