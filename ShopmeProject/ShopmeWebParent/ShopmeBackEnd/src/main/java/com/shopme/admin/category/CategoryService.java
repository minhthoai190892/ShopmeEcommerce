package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;

import jakarta.transaction.Transactional;

@Service
@Transactional
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

	public List<Category> listAll(String sortDir) {
		Sort sort = Sort.by("name");
		if (sortDir == null || sortDir.isEmpty()) {
			sort=sort.ascending();
		} else if (sortDir.equals("asc")) {
			sort = sort.ascending();
		} else if (sortDir.equals("desc")) {
			sort = sort.descending();
		}
		List<Category> rootCategories = categoryRepository.findRootCategories(sort);
		// gọi lại hàm
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
			Set<Category> children = sortSubCategories(rootCategory.getChildren());
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
		Set<Category> children = sortSubCategories(parent.getChildren());
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
		Iterable<Category> categoriesInDB = categoryRepository.findRootCategories(Sort.by("name").ascending());
		for (Category category : categoriesInDB) {// => chạy danh sách cha
			if (category.getParent() == null) {
				// categoriesUsedInForm.add(new Category(category.getName()));
				categoriesUsedInForm.add(Category.copyIdAndName(category));

				Set<Category> children = sortSubCategories(category.getChildren());// =>list danh sách con
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
//		Set<Category> children = parent.getChildren();
		Set<Category> children = sortSubCategories(parent.getChildren());
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

	/**
	 * hàm lưu thông tin category
	 * 
	 * @param category nhận vào một đối tượng
	 * @return trả
	 */
	public Category save(Category category) {
		return categoryRepository.save(category);
	}

	/**
	 * Hàm thông tin bằng id
	 * 
	 * @param id nhận vào một id
	 * @return trả về một đối tượng
	 * @throws CategoryNotFoundException
	 */
	public Category get(Integer id) throws CategoryNotFoundException {
		try {
			return categoryRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new CategoryNotFoundException("Could not find any category with ID " + id);
		}
	}

	/**
	 * Hàm kiểm tra name và alias có trùng không
	 * 
	 * @param id    nhận vào một id
	 * @param name
	 * @param alias
	 * @return trả về một chuổi Ok hoặc DuplicateName
	 */
	public String checkUnique(Integer id, String name, String alias) {
		boolean isCreatingNew = (id == null || id == 0);// =>kiểm tra có tạo mới chưa
		Category categoryByName = categoryRepository.findByName(name);
		if (isCreatingNew) {
			if (categoryByName != null) {// => kiểm tra đã tồn tại chưa
				return "DuplicateName";
			} else {
				Category categoryByAlias = categoryRepository.findByAlias(alias);
				if (categoryByAlias != null) {
					return "DuplicateAlias";
				}
			}
		} else {
			if (categoryByName != null && categoryByName.getId() != id) {
				return "DuplicateName";
			}
			Category categoryByAlias = categoryRepository.findByAlias(alias);
			if (categoryByAlias != null && categoryByAlias.getId() != id) {
				return "DuplicateAlias";
			}
		}
		return "Ok";

	}
	private SortedSet<Category> sortSubCategories(Set<Category> children) {
		return sortSubCategories(children,"asc");
	}
	

	private SortedSet<Category> sortSubCategories(Set<Category> children,String sortDir) {
		SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {

			@Override
			public int compare(Category cat1, Category cat2) {
				if (sortDir.equals("asc")) {
					return cat1.getName().compareTo(cat2.getName());
				} else {
					return cat2.getName().compareTo(cat1.getName());
				}
				
			}
		});
		sortedChildren.addAll(children);
		return sortedChildren;
	}
}
