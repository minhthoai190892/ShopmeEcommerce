package com.shopme.admin.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;
	public List<Category> listAll() {
		return categoryRepository.findAll();
	}
	/**
	 *  Hàm update enabled
	 * @param id nhận id của category
	 * @param enabled giá tị của enabled
	 */
	public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
		categoryRepository.updateEnabledStatus(id, enabled);
	}
}
