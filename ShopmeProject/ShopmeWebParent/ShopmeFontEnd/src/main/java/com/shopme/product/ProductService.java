package com.shopme.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Product;

@Service
public class ProductService {
	public static final int PRODUCTS_PER_PAGE=3;
	@Autowired
	private ProductRepository productRepository;
	
	public Page<Product> listByCategory(int pageNum,Integer categoryId) {
		String categoryIdMatch = "-"+String.valueOf(categoryId)+"-";
		System.err.println("categoryIdMatch>>>>>>>"+categoryIdMatch);
		Pageable pageable = PageRequest.of(pageNum-1, PRODUCTS_PER_PAGE);
		return productRepository.listByCategory(categoryId, categoryIdMatch, pageable);
	}


}
