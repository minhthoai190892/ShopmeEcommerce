package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;

	/**
	 * hàm hiển thị tất cả thông tin từ DB
	 * 
	 * @return trả về danh sách
	 */
	// public List<Category> listAll() {
	// return categoryRepository.findAll();
	// }

	public List<Category> listAll() {
		List<Category> rootCategories = categoryRepository.findRootCategories();
		//gọi lại hàm
		return listHierarchicalCategories(rootCategories);
		// return rootCategories;
	}

	/**
	 * hàm liệt kê danh mục phân cấp cho Category
	 * 
	 * @param rootCategories nhận vào một danh sách
	 * @return trả về danh sách danh mục
	 */
	private List<Category> listHierarchicalCategories(List<Category> rootCategories) {
		List<Category> hierarchicalCategories = new ArrayList<>();
		for (Category rootCategory : rootCategories) {
			// thêm đối tượng copy vào danh sách
			hierarchicalCategories.add(Category.copyFull(rootCategory));
			// lấy danh sách các tập hợp con
			Set<Category> children = rootCategory.getChildren();
			for (Category subCategory : children) {
				String name = "--" + subCategory.getName();
				// thêm vào danh sách với tất cả thông tin và thay đổi tên
				hierarchicalCategories.add(Category.copyFull(subCategory, name));
				listSubHierarchicalCategories(subCategory, 1, hierarchicalCategories);
			}
		}
		return hierarchicalCategories;
	}

	private void listSubHierarchicalCategories(Category parent, int subLevel, List<Category> hierarchicalCategories) {
		Set<Category> children = parent.getChildren();
		int newSubLevel = subLevel + 1;
		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			// thêm vào danh sách với tất cả thông tin và thay đổi tên
			hierarchicalCategories.add(Category.copyFull(subCategory, name));
			listSubHierarchicalCategories(subCategory, newSubLevel, hierarchicalCategories);
		}
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
		// lấy dữ liệu từ DB
		Iterable<Category> categoriesInDB = categoryRepository.findAll();
		for (Category category : categoriesInDB) {// => chạy danh sách cha
			if (category.getParent() == null) {
				// categoriesUsedInForm.add(new Category(category.getName()));
				categoriesUsedInForm.add(Category.copyIdAndName(category));

				Set<Category> children = category.getChildren();// =>list danh sách con
				for (Category subCategory : children) {// => chạy danh sách con
					String name = "--" + subCategory.getName();
					categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
					listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
				}
			}
		}
		return categoriesUsedInForm;
	}

	private void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();
		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
			listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, newSubLevel);
		}
	}

	/** hàm lưu thông tin category
	 * @param category nhận vào một đối tượng 
	 * @return trả
	 */
	public Category save(Category category) {
		return categoryRepository.save(category);
	}
}
