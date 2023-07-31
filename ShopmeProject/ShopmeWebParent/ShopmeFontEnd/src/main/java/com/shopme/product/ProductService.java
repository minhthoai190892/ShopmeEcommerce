package com.shopme.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Product;
import com.shopme.common.exception.ProductNotFoundException;

@Service
public class ProductService {
	public static final int PRODUCTS_PER_PAGE=3;
	public static final int SEARCH_RESULTS_PER_PAGE=3;
	@Autowired
	private ProductRepository productRepository;
	/**
	 * Hàm dùng để phân trang
	 * @param pageNum là số trang
	 * @categoryId là id của category
	 * @return trả về một danh sách category
	 * */
	public Page<Product> listByCategory(int pageNum,Integer categoryId) {
		String categoryIdMatch = "-"+String.valueOf(categoryId)+"-";
		System.err.println("categoryIdMatch>>>>>>>"+categoryIdMatch);
		Pageable pageable = PageRequest.of(pageNum-1, PRODUCTS_PER_PAGE);
		return productRepository.listByCategory(categoryId, categoryIdMatch, pageable);
	}
	/**
	 * Hàm lấy product bằng alias
	 * @param alias tên alias của product
	 * @return trả về một product
	 * */
	public Product getProduct(String alias) throws ProductNotFoundException {
		Product product = productRepository.findByAlias(alias);
		if (product==null) {
			throw new ProductNotFoundException("Could not find any product with alias "+alias);
		}
		return product;
	}
	public Page<Product> search(String keyword,int pageNum) {
		Pageable pageable = PageRequest.of(pageNum -1, SEARCH_RESULTS_PER_PAGE);
		System.err.println("pageable>>>>"+pageable);
		return productRepository.search(keyword, pageable);
	}
}
