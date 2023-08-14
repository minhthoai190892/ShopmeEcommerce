package com.shopme.admin.customer;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerService {
	public static final int CUSTOMERS_PER_PAGE=10;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private CountryRepository countryRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;	
	/**
	 * Hàm dùng để phân trang và tìm kiếm
	 * @param pageNum là số index của trang (1,2,3...)
	 * @param sortField là sắp xếp theo firstName hoặc lastName ....
	 * @param sortDir là sắp xếp theo "asc" hoặc "desc"
	 * @param keyword là từ khóa tìm kiếm
	 * @return trả về Page<Customer> có từ khóa tìm kiếm hay không
	 * */
	public Page<Customer> listByPage(int pageNum,String sortField,String sortDir,String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc")?sort.ascending():sort.descending();
		Pageable pageable = PageRequest.of(pageNum-1, CUSTOMERS_PER_PAGE, sort);
		
		if (keyword!=null) {
			return customerRepository.findAll(keyword,pageable);
		}
		return customerRepository.findAll(pageable);
	}
	/**
	 * Hàm update enabled của customer
	 * */
	public void updateCustomerEnabledStatus(Integer id,boolean enabled) {
		customerRepository.updateEnabledStatus(id, enabled);
	}
	/**
	 * Hàm lấy Customer bằng ID
	 * @param id id của người dùng cần tìm
	 * */
	public Customer get(Integer id) throws CustomerNotFoundException {
		try {
			return customerRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new CustomerNotFoundException("Could not find any customers with ID: "+id);
		}
	}

	public List<Country> listAllCountries() {
		return countryRepository.findAllByOrderByNameAsc();
	}
	/**
	 * Hàm kiểm tra email có trùng với database
	 * @param id là id của người dùng vừa tạo
	 * @param email là email của người dùng vừa tạo
	 * @return true/false
	 * */
	public boolean isEmailUnique(Integer id,String email) {
		Customer existCustomer =customerRepository.findByEmail(email);
		if (existCustomer!=null && existCustomer.getId()!=id) {
			return false;
		}
		return true;
	}
	/**
	 * Hàm lưu thông tin người dùng
	 * @param customerInform là lấy thông tin người dùng nhập trên form đăng ký
	 * 
	 * */
	public void save(Customer customerInform) {
		//kiểm tra xem ô password có được nhập không 
		if (!customerInform.getPassword().isEmpty()) {
			//=> có nhập
			String encondePassword = passwordEncoder.encode(customerInform.getPassword());
			customerInform.setPassword(encondePassword);
		}else {
			//=> không có nhập
			//tìm customer từ database
			Customer customerDB = customerRepository.findById(customerInform.getId()).get();
			//set lại password customer
			customerInform.setPassword(customerDB.getPassword());
		}
		customerRepository.save(customerInform);
	}
	/**
	 * Hàm xóa customer
	 * @param id là id customer cần xóa
	 * */
	public void delete(Integer id) throws CustomerNotFoundException {
		Long count = customerRepository.countById(id);
		System.err.println("CustomerService > delete >>>>>>: "+count);
		if (count==null || count==0) {
			throw new CustomerNotFoundException("Could not find any customers with ID: "+id);
		}
		customerRepository.deleteById(id);
	}

}
