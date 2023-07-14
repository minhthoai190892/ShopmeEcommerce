package com.shopme.admin.product;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public List<Product> listAll() {
		return productRepository.findAll();
	}

	/**
	 * hàm save product
	 * 
	 * @param product nhận một đối tượng product
	 * @return trả về một product mới
	 */
	public Product saveProduct(Product product) {

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

	public String checkUnique(Integer id, String name) {

		boolean isCreatingNew = (id == null || id == 0);
		Product productByName = productRepository.findByName(name);
		if (isCreatingNew) {
			if (productByName != null) {
				return "Duplicate";
			}
		} else {
			if (productByName != null && productByName.getId() != null) {
				return "Duplicate";
			}
		}
		return "Ok";
	}
	
	public void updateProductEnabledStatus(Integer id,boolean enabled) {
		productRepository.updateEnabledStatus(id, enabled);
	}
}
