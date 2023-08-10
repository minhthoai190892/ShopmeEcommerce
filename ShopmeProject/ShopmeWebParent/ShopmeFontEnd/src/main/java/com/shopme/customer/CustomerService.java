package com.shopme.customer;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.setting.CountryRepository;

@Service
public class CustomerService {
	@Autowired
	private CountryRepository countryRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	public List<Country> listAllCountries() {
		return countryRepository.findAllByOrderByNameAsc();
	}
	public boolean isEmailUnique(String email) {
	Customer customer=	customerRepository.findByEmail(email);
	return customer == null;
	}
	public void registerCustomer(Customer customer) {
		encodePassword(customer);
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());
		String randomCode = randomString(64);
		customer.setVerificationCode(randomCode);
		customerRepository.save(customer);
		System.err.println(randomCode);
	}
	private String randomString(int n) {
		 // choose a Character random from this String
		  String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
		         + "0123456789"
		         + "abcdefghijklmnopqrstuvxyz";
		 
		  // create StringBuffer size of AlphaNumericString
		  StringBuilder sb = new StringBuilder(n);
		 
		  for (int i = 0; i < n; i++) {
		 
		   // generate a random number between
		   // 0 to AlphaNumericString variable length
		   int index
		    = (int)(AlphaNumericString.length()
		      * Math.random());
		 
		   // add Character one by one in end of sb
		   sb.append(AlphaNumericString
		      .charAt(index));
		  }
		 
		  return sb.toString();
	}
	private void encodePassword(Customer customer) {
		// TODO Auto-generated method stub
		String encodePassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodePassword);
		
	}
}
