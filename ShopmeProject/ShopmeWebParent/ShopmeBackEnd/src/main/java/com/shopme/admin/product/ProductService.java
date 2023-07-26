package com.shopme.admin.product;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Product;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {
	public static final int PRODUCTS_PER_PAGE=5;
	@Autowired
	private ProductRepository productRepository;

	public List<Product> listAll() {
		return productRepository.findAll();
	}
	public Page<Product> listByPage(int pageNum,String sortField,String sortDir,String keyword,Integer categoryId) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc")?sort.ascending():sort.descending();
		Pageable pageable = PageRequest.of(pageNum-1, PRODUCTS_PER_PAGE,sort);
		//kiểm tra keyword có được nhập không
		if (keyword!=null&&!keyword.isEmpty()) {
			
			if (categoryId!=null && categoryId>0) {
				String categoryIdMatch = "-"+String.valueOf(categoryId)+"-";
				return productRepository.searchInCategory(categoryId, categoryIdMatch, keyword, pageable);
			}
			//search by keyword
			return productRepository.findAll(keyword, pageable);

		}
		//--search by dropdown category
		if (categoryId!=null && categoryId>0) {
			String categoryIdMatch = "-"+String.valueOf(categoryId)+"-";
			return productRepository.findAllInCategory(categoryId, categoryIdMatch, pageable);
		}
		return productRepository.findAll(pageable);
	}
	
	/**
	 * hàm save product
	 * 
	 * @param product nhận một đối tượng product
	 * @return trả về một product mới
	 */
	public Product save(Product product) {

		if (product.getId() == null) {
			// product mới tạo thì set ngày tạo hiện tại
			product.setCreatedTime(new Date());
		}
		if (product.getAlias() == null || product.getAlias().isEmpty()) {
			String defaultAlias = product.getName().replaceAll(" ", "-");
			product.setAlias(defaultAlias);
		} else {
			product.setAlias(product.getAlias().replaceAll(" ", "-"));
		}
		product.setUpdateTime(new Date());
		return productRepository.save(product);
	}

	/**
	 * Hàm kiểm tra trùng tên
	 * @param id nhận id 
	 * @param name tên kiểm tra
	 * @return trả về Duplicate hoặc Ok
	 */
	public String checkUnique(Integer id, String name) {

		boolean isCreatingNew = (id == null || id == 0);
		Product productByName = productRepository.findByName(name);
		if (isCreatingNew) {
			if (productByName != null) {
				return "Duplicate";
			}
		} else {
			if (productByName != null && productByName.getId() != id) {
				return "Duplicate";
			}
		}
		return "Ok";
	}
	
	/**
	 * Hàm cập nhật status
	 * @param id nhận id
	 * @param enabled nhận trạng thái của product true hoặc false
	 */
	public void updateProductEnabledStatus(Integer id,boolean enabled) {
		productRepository.updateEnabledStatus(id, enabled);
	}
	/**
	 * Hàm xóa product
	 * @param id nhận id product
	 * @throws ProductNotFoundException
	 */
	public void delete(Integer id) throws ProductNotFoundException {
		Long countById = productRepository.countById(id);
		if (countById==null || countById==0) {
			throw new ProductNotFoundException("Could not find any product with ID "+id);
		}
		productRepository.deleteById(id);
	}

	public Product get(Integer id) throws ProductNotFoundException {
		try {
			return productRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			// TODO: handle exception
			throw new ProductNotFoundException("Could not find any product with ID: "+id);
		}
	}
}
