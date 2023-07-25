package com.shopme.admin.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	Product findByName(String name);

	@Query("update Product p set p.enabled =?2 where p.id =?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);

	public Long countById(Integer id);

	@Query("select p from Product p where p.name like %?1%" 
	+ "or p.shortDescription like %?1%" 
	+ "or p.fullDescription like %?1%"
	+ "or p.brand.name like %?1%" 
	+ "or p.category.name like %?1%")
	public Page<Product> findAll(String keyword, Pageable pageable);
}
