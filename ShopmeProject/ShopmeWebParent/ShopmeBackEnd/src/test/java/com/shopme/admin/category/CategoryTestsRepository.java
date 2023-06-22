package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryTestsRepository {
	@Autowired
	private CategoryRepository categoryRepository;
	@Test
	public void testCreateRootCategory() {
		Category category = new Category("Electronics");
		Category saveCategory = categoryRepository.save(category);
		assertThat(saveCategory.getId()).isGreaterThan(0);
	}
	@Test
	public void testCreateSubCategory() {
		//lấy id của category cha
		Category paretnCategory = new Category(4);
		System.out.println("==========>"+paretnCategory.getId());
		Category subCategory = new Category("Gaming Laptops", paretnCategory);
		categoryRepository.save(subCategory);
//		assertThat(saveCategory.getId()).isGreaterThan(0);
	}
	@Test
	public void testGetCategory() {
		Category category = categoryRepository.findById(1).get();
		System.out.println(category.getName());
		//lấy danh sách
		Set<Category> children = category.getChildren();
		for (Category subCategory : children) {
			System.out.println(subCategory.getName());
		}
		
	}
	@Test
	public void testPrintHierarchicalCategories() {
		Iterable<Category> categories = categoryRepository.findAll();
		for (Category category : categories) {
			if (category.getParent()==null) {
				System.out.println(category.getName());
				
				Set<Category> children = category.getChildren();
				for (Category subCategory: children) {
					System.out.println("--"+subCategory.getName());
					printChildren(subCategory, 1);
				}
			}
		}
	}
	private void printChildren(Category parent,int subLevel) {
		int newSubLevel = subLevel+1;
		Set<Category> children = parent.getChildren();
		for (Category subCategory: children) {
			for (int i = 0; i < newSubLevel; i++) {
				System.out.print("--");
			}
			System.out.println(subCategory.getName());
			printChildren(subCategory, newSubLevel);
		}
	}
	@Test
	public void testListRootCategories(){
		List<Category> rootCategories = categoryRepository.findRootCategories();
		rootCategories.forEach(cat->System.out.println(cat.getName()));
	}
}
