package com.shopme.customer;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.setting.CountryRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
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
	/**
	 * Hàm kiểm tra email có trùng không
	 * @param email của customer 
	 * @return trả về null hoặc not null
	 * */
	public boolean isEmailUnique(String email) {
	Customer customer=	customerRepository.findByEmail(email);
	return customer == null;
	}
	/**
	 * Hàm tạo mới customer
	 * @param customer đối tượng customer từ form
	 * */
	public void registerCustomer(Customer customer) {
		encodePassword(customer);
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());
		String randomCode = randomString(64);
		customer.setVerificationCode(randomCode);
		customerRepository.save(customer);
		System.err.println(randomCode);
	}
	/**
	 * Hàm tạo Verification Code
	 * @param n là số ký tự của mã Verification Code
	 * @return trả về một mã code
	 * */
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
	/**
	 * Hàm mã hóa password
	 * @param customer đối tượng customer cần mã hóa password
	 * */
	private void encodePassword(Customer customer) {
		// TODO Auto-generated method stub
		String encodePassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodePassword);
		
	}
	/**
	 * Hàm tìm kiếm và xác minh customer bằng  verification Code
	 * @param verificationCode verificationCode của customer
	 * @return false / true
	 * */
	public boolean verify(String verificationCode) {

		Customer customer =customerRepository.findByVerificationCode(verificationCode);

		if (customer==null || customer.isEnabled()) {
			return false;
		}else {
			//update customer
			customerRepository.enabled(customer.getId());
			return true;
		}
		
	}
}
