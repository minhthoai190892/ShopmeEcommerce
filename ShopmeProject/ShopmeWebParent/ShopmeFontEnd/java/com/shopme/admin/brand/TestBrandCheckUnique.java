package com.shopme.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shopme.common.entity.Brand;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class TestBrandCheckUnique {
	@MockBean
	private BrandRepository brandRepository;
	@InjectMocks
	private BrandService brandService;

	@Test
	public void testCheckUniqueInNewModeReturnDuplicate() {
		Integer id = null;
		String name ="Asereere";
		Brand brand = new Brand(name);
		Mockito.when(brandRepository.findByName(name)).thenReturn(brand);
		String result = brandService.checkUnique(id, name);
		System.err.println(result);
		assertThat(result).isEqualTo("Duplicate");
	}
	@Test
	public void testCheckUniqueInNewModeReturnOk() {
		Integer id = null;
		String name="AMD";
		Mockito.when(brandRepository.findByName(name)).thenReturn(null);
		String result = brandService.checkUnique(id, name);
		System.err.println(result);
		assertThat(result).isEqualTo("Ok");
	}
	@Test
	public void testCheckUniqueInEditModeReturnDuplicate() {
		Integer id =1;
		String name = "Canod";
		Brand brand = new Brand(id, name);
		Mockito.when(brandRepository.findByName(name)).thenReturn(brand);
		String result = brandService.checkUnique(2, "Canon");
		System.err.println(result);
		assertThat(result).isEqualTo("Duplicate");
	}
	@Test
	public void testCheckUniqueInEditModeReturnOk() {
		Integer id =1;
		String name = "Canon";
		Brand brand = new Brand(id, name);
		Mockito.when(brandRepository.findByName(name)).thenReturn(brand);
		String result = brandService.checkUnique(2, "Canon");
		System.err.println(result);
		assertThat(result).isEqualTo("Ok");
	}
}
