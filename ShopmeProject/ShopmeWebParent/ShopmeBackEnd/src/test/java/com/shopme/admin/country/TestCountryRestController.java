package com.shopme.admin.country;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;

import jakarta.persistence.EntityManager;

@SpringBootTest
@AutoConfigureMockMvc

public class TestCountryRestController {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	private CountryRepository countryRepository;
	@Test
	@WithMockUser(username = "Lethidoan@gmail.com",password = "Lethidoan@gmail.com",roles = "Admin")//library security-test
	public void testListCountries() throws Exception {
		String url = "/countries/list";
	MvcResult result=	mockMvc.perform(get(url)).andExpect(status().isOk()).andDo(print()).andReturn();
		String jsonResponse = result.getResponse().getContentAsString();
		System.out.println(jsonResponse);
		
		Country[] countries = objectMapper.readValue(jsonResponse, Country[].class);
		for (Country country : countries) {
			System.err.println(country);
		}
		
	}
	@Test
	@WithMockUser(username = "Lethidoan@gmail.com",password = "Lethidoan@gmail.com",roles = "Admin")//library security-test
	public void testCreateCountry() throws JsonProcessingException, Exception {
		String url = "/countries/save";
		String countryName = "France";
		String countryCode = "FR";
		Country country = new Country(countryName,countryCode);
		MvcResult result=	(MvcResult) mockMvc.perform(post(url).contentType("application/json").content(objectMapper.writeValueAsString(country)).with(csrf())).andDo(print()).andExpect(status().isOk()).andReturn();
	
		String reponse = result.getResponse().getContentAsString();
		Integer countryId = Integer.parseInt(reponse);
		Country saveCountry = countryRepository.findById(countryId).get();
		assertThat(saveCountry.getName()).isEqualTo(countryName);
		System.out.println("Country ID "+reponse);
	
	}
	@Test
	@WithMockUser(username = "Lethidoan@gmail.com",password = "Lethidoan@gmail.com",roles = "Admin")//library security-test
	public void testUpdateCountry() throws JsonProcessingException, Exception {
		String url = "/countries/save";
		Integer countryId =9;
		String countryName = "Spain";
		String countryCode = "ES";
		Country country = new Country(countryId, countryName, countryCode, null);
		System.err.println("country ++++ "+country);
		 mockMvc.perform(post(url).contentType("application/json")
				 .content(objectMapper.writeValueAsString(country))
				 .with(csrf())).andDo(print())
		 .andExpect(status().isOk()).andExpect((ResultMatcher) content().string(String.valueOf(countryId)));
	
		

		Country saveCountry = countryRepository.findById(countryId).get();
//		assertThat(saveCountry.getName()).isEqualTo(countryName);

	
	}
	@Test
	@WithMockUser(username = "Lethidoan@gmail.com",password = "Lethidoan@gmail.com",roles = "Admin")//library security-test
	public void delete() throws Exception {
	
		Integer countryId =9;
		String url = "/countries/delete/"+countryId;
		mockMvc.perform(get(url)).andExpect(status().isOk());
	Optional<Country>	findById = countryRepository.findById(countryId);
		assertThat(findById).isNotPresent();
	}
}
