package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.Console;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Category;

@DataJpaTest
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
		Category paretnCategory = new Category(5);
		System.out.println("==========>"+paretnCategory.getId());
		Category subCategory = new Category("Memory", paretnCategory);
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
}
