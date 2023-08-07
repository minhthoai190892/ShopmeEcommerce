package com.shopme.admin.state;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.admin.setting.state.StateRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@Rollback(false)
public class TestStateRepository {
	@Autowired
	private StateRepository stateRepository;
	@Autowired
	private TestEntityManager entityManager;
//	private CountryRepository countryRepository;
	@Test
	public void testGetCountry() {
		Integer id =3;
		Country country =entityManager.find(Country.class, id);
//		Country country = countryRepository.findById(id).get();
		System.err.println(country);

	}
	@Test
	public void testCreateStatesInUSA() {
		Integer id =8;
		Country country = entityManager.find(Country.class, id);
		State state = stateRepository.save(new State("Texas", country));
		System.err.println("state >>>"+state);
		assertThat(state).isNotNull();
		assertThat(state.getId()).isGreaterThan(0);
	}
}
