package com.shopme.admin.customer;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.product.ProductRepository;
import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class TestCustomer {
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private ProductRepository productRepository;
	@Test
	public void list() {
		List<Product> customers = productRepository.findAll();
		for (Product product : customers) {
			System.out.println(product);
		}
	}
	@Test
	public void testUpdateAuthenticationType() {
		Integer id =8;
		customerRepository.updateAuthenticationType(id, AuthenticationType.FACEBOOK);
		Customer customer = customerRepository.findById(id).get();
		System.err.println(customer.getAuthenticationType());
	}
}
