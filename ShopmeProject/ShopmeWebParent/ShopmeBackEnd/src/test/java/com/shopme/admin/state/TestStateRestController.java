package com.shopme.admin.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
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
import org.thymeleaf.spring6.expression.Mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.admin.setting.state.StateRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;


@SpringBootTest
@AutoConfigureMockMvc
public class TestStateRestController {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private StateRepository stateRepository;
	@Autowired
	private CountryRepository countryRepository;
	
	@Test
	@WithMockUser(username = "Lethidoan@gmail.com",password = "Lethidoan@gmail.com",roles = "Admin")//library security-test
	public void testListByCountries() throws Exception {
		System.err.println("testListByCountries");
		Integer countryId =3;
		String url = "/states/list_by_country/"+countryId;
		MvcResult result = mockMvc.perform(get(url))
							.andExpect(status().isOk())
							.andDo(print())
							.andReturn();
		String jsonResponse = result.getResponse().getContentAsString();
		System.out.println("jsonResponse: " + jsonResponse);
		State[] states = objectMapper.readValue(jsonResponse, State[].class);
		for (State state : states) {
			System.out.println("state: " + state.toString());
		}
		assertThat(states).hasSizeGreaterThanOrEqualTo(0);
	}
	@Test
	@WithMockUser(username = "Lethidoan@gmail.com",password = "Lethidoan@gmail.com",roles = "Admin")//library security-test
	public void testCreateState() throws JsonProcessingException, Exception {
		String url = "/states/save";
		Integer countryId = 3;
		Country country = countryRepository.findById(countryId).get();
		State state = new State("Thuong Hai", country);
		MvcResult mvcResult = mockMvc.perform(post(url).contentType("application/json")
															.content(objectMapper.writeValueAsString(state))
															.with(csrf()))
														.andDo(print())
														.andExpect(status().isOk())
														.andReturn();
		String response = mvcResult.getRequest().getContentAsString();
		System.err.println("response "+response);
		Integer stateId = Integer.parseInt(response);
		Optional<State> findById = stateRepository.findById(stateId);
		System.err.println("{findById "+findById);
		assertThat(findById.isPresent());
	}
	@Test
	@WithMockUser(username = "Lethidoan@gmail.com",password = "Lethidoan@gmail.com",roles = "Admin")//library security-test
	public void testUpdateState() throws JsonProcessingException, Exception {
		String url = "/states/save";
		Integer stateId = 7;
		String stateName = "Da Nang";
		State state = stateRepository.findById(stateId).get();
		state.setName(stateName);
		
	 mockMvc.perform(post(url).contentType("application/json")
															.content(objectMapper.writeValueAsString(state))
															.with(csrf()))
														.andDo(print())
														.andExpect(status().isOk())
														.andExpect((ResultMatcher) content().string(String.valueOf(stateId)));
		Optional<State> findById = stateRepository.findById(stateId);
		assertThat(findById.isPresent());
		
		State updateState = findById.get();
		assertThat(updateState.getName()).isEqualTo(stateName);
	}
	

	@Test
	@WithMockUser(username = "Lethidoan@gmail.com",password = "Lethidoan@gmail.com",roles = "Admin")//library security-test
	public void testDeleteState() throws Exception {
		Integer stateId = 2;
		String url = "/states/delete/"+stateId;
		mockMvc.perform(get(url)).andExpect(status().isOk());
		Optional<State> findById = stateRepository.findById(stateId);
		assertThat(findById).isNotPresent();
	}
}
