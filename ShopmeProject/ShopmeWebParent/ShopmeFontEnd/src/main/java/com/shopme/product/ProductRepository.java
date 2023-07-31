package com.shopme.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Product;

public interface ProductRepository  extends JpaRepository<Product, Integer>{
	
	@Query("select p from Product p where p.enabled = true and p.category.id=?1 or p.category.allParentIDs like %?2% order by p.name asc")
	public Page<Product> listByCategory(Integer Id,String categoryIDMatch,Pageable pageable) ;
	/**
	 * hàm tìm kiếm product bằng alias
	 * @param alias tên alias của product
	 * @return một product
	 * */
	public Product findByAlias(String alias);
	
	
	
}
