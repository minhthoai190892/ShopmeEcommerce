package com.shopme.admin.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatComparable;

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
		Brand brand = entityManager.find(Brand.class, 7);
		Category category = entityManager.find(Category.class, 6);
		Product product = new Product();
		product.setName("Samsung Galaxy A412");
		product.setAlias("Samsung Galaxy A412");
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
	@Test
	public void testSaveProductWithImages() {
//		testListALlProducts();
//		// TODO Auto-generated method stub
		Integer id =4;
		Product product = productRepository.findById(id).get();
		product.setMainImage("main image.jpg");
		product.addExtraImage("extra image 1.png");
		product.addExtraImage("extra_image 2.png");
		product.addExtraImage("extra-image 3.png");
		Product saveProduct = productRepository.save(product);
		System.err.println(saveProduct);
		assertThat(saveProduct.getImages().size()).isEqualTo(3);
	}
	@Test
	public void testSaveProductWithDetails() {
		Integer id =4;
		Product product = productRepository.findById(id).get();
		product.addDetail("Device Memory", "128 GB");
		product.addDetail("CPU Model", "MediaTek");
		product.addDetail("OS", "Android 10");
		Product saveProduct= productRepository.save(product);
		System.err.println(saveProduct.getDetails());
		assertThat(saveProduct.getDetails()).isNotEmpty();
	}
}
