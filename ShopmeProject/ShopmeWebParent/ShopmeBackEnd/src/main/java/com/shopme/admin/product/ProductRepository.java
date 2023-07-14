package com.shopme.admin.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{

	Product findByName(String name);
	@Query("update Product p set p.enabled =?2 where p.id =?1")
	@Modifying
	public void updateEnabledStatus(Integer id,boolean enabled);
	
}
