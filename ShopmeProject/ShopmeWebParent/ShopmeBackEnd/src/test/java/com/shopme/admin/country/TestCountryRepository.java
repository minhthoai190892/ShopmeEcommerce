package com.shopme.admin.country;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@Rollback(false)
public class TestCountryRepository {
	@Autowired
	private CountryRepository countryRepository;

	@Test
	public void testCreateCountry() {
		Country country = countryRepository.save(new Country("USA", "USA"));
	
		System.err.println("country >>>>>>>>> " + country);
		assertThat(country).isNotNull();
		assertThat(country.getId()).isGreaterThan(0);
	}

	@Test
	public void testUpdateCounter() {
		Integer id = 1;
		String name = "Republic of India";
		Country country = countryRepository.findById(id).get();
		country.setName(name);
		Country updateCountry = countryRepository.save(country);
		System.err.println("updateCountry >>>>> " + updateCountry);
		assertThat(updateCountry.getName()).isEqualTo(name);

	}

	@Test
	public void testGetCountry() {
		Integer id = 8;
		Country country = countryRepository.findById(id).get();
		System.err.println(country);
	}

	@Test
	public void testDeleteCountry() {
		Integer id = 1;
		countryRepository.deleteById(id);
	}

	@Test
	public void testListAllCountry() {
		List<Country> findAll = countryRepository.findAll();
		findAll.forEach(System.out::println);
	}

}
