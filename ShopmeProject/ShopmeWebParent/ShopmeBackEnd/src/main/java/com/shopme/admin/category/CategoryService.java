package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.poi.hpsf.Array;
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
	 * Hàm update enabled
	 * 
	 * @param id      nhận id của category
	 * @param enabled giá tị của enabled
	 */
	public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
		categoryRepository.updateEnabledStatus(id, enabled);
	}

	public List<Category> listCategoriesUsedInForm() {
		List<Category> categoriesUsedInForm = new ArrayList<>();
		Iterable<Category> categoriesInDB = categoryRepository.findAll();
		for (Category category : categoriesInDB) {
			if (category.getParent() == null) {
				categoriesUsedInForm.add(new Category(category.getName()));

				Set<Category> children = category.getChildren();
				for (Category subCategory : children) {
					String name = "--"+subCategory.getName();
					categoriesUsedInForm.add(new Category(name));
					listChildren(categoriesUsedInForm,subCategory, 1);
				}
			}
		}
		return categoriesUsedInForm;
	}

	private void listChildren(List<Category> categoriesUsedInForm,Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();
		for (Category subCategory : children) {
			String name ="";
			for (int i = 0; i < newSubLevel; i++) {
				name+= "--";
			}
			name+=subCategory.getName();
			categoriesUsedInForm.add(new Category(name));
			listChildren(categoriesUsedInForm,subCategory, newSubLevel);
		}
	}
}
