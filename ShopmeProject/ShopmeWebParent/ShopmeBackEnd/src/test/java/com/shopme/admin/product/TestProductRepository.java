package com.shopme.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class TestProductRepository {
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateProduct() {
		Brand brand = entityManager.find(Brand.class, 6);
		Category category = entityManager.find(Category.class, 6);
		Product product = new Product();
		product.setName("Samsung Galaxy A41");
		product.setAlias("Samsung Galaxy A41");
		product.setShortDescription("A good smartphone from samsung");
		product.setFullDescription("Full description");
		product.setEnabled(true);
		product.setInStock(true);;
		product.setBrand(brand);
		product.setCategory(category);
		product.setPrice(456);
		product.setCreatedTime(new Date());
		product.setUpdateTime(new Date());
		
		Product saveProduct = productRepository.save(product);
		assertThat(saveProduct).isNotNull();
		assertThat(saveProduct.getId()).isGreaterThan(0);
	}
	@Test
	public void testListALlProducts() {
		List<Product> listAll = productRepository.findAll();
		listAll.forEach(System.out::println);
	}
	@Test
	public void testGetProduct() {
		
		Product product = productRepository.findById(1).get();
		System.out.println(product);
	}
	@Test
	public void testUpdateProduct() {
		Product product = productRepository.findById(1).get();
		product.setName("asdfadsf");
		productRepository.save(product);
		System.err.println(product);
	}
	@Test
	public void testDeleteProduct() {
		productRepository.deleteById(1);
	}
}
