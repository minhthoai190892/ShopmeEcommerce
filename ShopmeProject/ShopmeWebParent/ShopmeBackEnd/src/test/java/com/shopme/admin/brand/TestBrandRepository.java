package com.shopme.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)

public class TestBrandRepository {
	@Autowired
	private BrandRepository brandRepository;
	@Test
	public void testCreateBrand1() {
		Category computers = new Category(8);
		Brand acer = new Brand("Acer");
		acer.getCategories().add(computers);
		Brand saveBrand = brandRepository.save(acer);
		System.out.println(">>>>>>>>>"+saveBrand.toString());
		assertThat(saveBrand).isNotNull();
		assertThat(saveBrand.getId()).isGreaterThan(0);
	}
	@Test
	public void testCreateBrand2() {
		Category category1 = new Category(6);
		Category category2 = new Category(9);
		Brand brand = new Brand("SamSung");
		brand.getCategories().add(category2);
		brand.getCategories().add(category1);
		
		Brand saveBrand = brandRepository.save(brand);
		System.out.println(">>>>>>>>>"+saveBrand.toString());
		assertThat(saveBrand).isNotNull();
		assertThat(saveBrand.getId()).isGreaterThan(0);
	}
	@Test
	public void testCreateBrand3() {
		Brand brand = new Brand("Test1");
		brand.getCategories().add(new Category(3));
		Brand saveBrand = brandRepository.save(brand);
		System.out.println(">>>>>>>>>"+saveBrand.toString());
		assertThat(saveBrand).isNotNull();
		assertThat(saveBrand.getId()).isGreaterThan(0);
	}
	@Test
	public void testListBrand() {
		List<Brand> brands = brandRepository.findAll();
		brands.forEach(System.out::println);
		assertThat(brands).isNotEmpty();
	}
	@Test
	public void testGetBrand() {
		Brand brand = brandRepository.findById(4).get();
		System.out.println(">>>>>>>"+brand.toString());
		assertThat(brand.getName()).isEqualTo("Test1");
		
	}
	@Test
	public void testUpdateBrand() {
		String newBrand = "PHone";
		Brand phone = brandRepository.findById(4).get();
		phone.setName(newBrand);
		Brand saveBrand = brandRepository.save(phone);
		System.out.println(">>>>>>>>>"+saveBrand.toString());
		assertThat(saveBrand.getName()).isEqualTo(newBrand);
	}
	@Test
	public void testDelete() {
		Integer id =4;
		brandRepository.deleteById(id);
		Optional<Brand> result = brandRepository.findById(id);
		assertThat(result.isEmpty());
	}
}
