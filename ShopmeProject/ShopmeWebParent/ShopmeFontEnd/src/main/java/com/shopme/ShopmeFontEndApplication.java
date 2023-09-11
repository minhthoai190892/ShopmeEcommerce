package com.shopme;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;
import com.shopme.customer.CustomerRepository;
import com.shopme.product.ProductRepository;
import com.shopme.shoppingcart.CartItemRepository;

@SpringBootApplication
public class ShopmeFontEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopmeFontEndApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(CustomerRepository customerRepository,
			ProductRepository productRepository, CartItemRepository cartItemRepository) {
		return runner -> {
//			testSaveItem(customerRepository, productRepository, cartItemRepository);
//			testFindByCustomer(cartItemRepository,customerRepository);
//			testFindCustomerAndProduct(customerRepository, productRepository, cartItemRepository);
//			testUpdateQuantity(customerRepository, productRepository, cartItemRepository);
//			testDeleteByCustomerAndProduct(customerRepository, productRepository, cartItemRepository);
		};
	}

	private void testDeleteByCustomerAndProduct(CustomerRepository customerRepository,
			ProductRepository productRepository, CartItemRepository cartItemRepository) {
		// TODO Auto-generated method stub
		System.err.println("testDeleteByCustomerAndProduct");
		Integer customerId = 19;
		Integer productId = 35;
		cartItemRepository.deleteByCustomerAndProduct(customerId, productId);
		
		
	}

	private void testUpdateQuantity(CustomerRepository customerRepository, ProductRepository productRepository,
			CartItemRepository cartItemRepository) {
		// TODO Auto-generated method stub
		System.err.println("testUpdateQuantity");
		Integer customerId = 19;
		Integer productId = 35;
		Customer customer = customerRepository.findById(customerId).get();
		System.out.println(customer);
		Product product = productRepository.findById(productId).get();
		System.out.println(product);
		cartItemRepository.updateQuantity(4, customerId, productId);
		CartItem cartItem = cartItemRepository.findByCustomerAndProduct(customer, product);
		System.out.println(cartItem);
		
	}

	private void testFindCustomerAndProduct(CustomerRepository customerRepository, ProductRepository productRepository,
			CartItemRepository cartItemRepository) {
		System.err.println("testFindCustomerAndProduct");
		// TODO Auto-generated method stub
		Integer customerId = 19;
		Integer productId = 35;
		Customer customer = customerRepository.findById(customerId).get();
		System.out.println(customer);
		Product product = productRepository.findById(productId).get();
		System.out.println(product);
		CartItem cartItem = cartItemRepository.findByCustomerAndProduct(customer, product);
		System.out.println(cartItem);
	}

	private void testFindByCustomer(CartItemRepository cartItemRepository,CustomerRepository customerRepository) {
		System.err.println("testFindByCustomer");
		// TODO Auto-generated method stub
		Integer customerID = 19;
		Customer customer = customerRepository.findById(customerID).get();
		List<CartItem> listCartItems = cartItemRepository.findByCustomer(customer);
		listCartItems.forEach(System.out::println);
		
	}

	private void testSaveItem(CustomerRepository customerRepository, ProductRepository productRepository,
			CartItemRepository cartItemRepository) {
		// TODO Auto-generated method stub
		System.err.println("testSaveItem");
		Integer customerId = 19;
		Integer productId = 50;
		Customer customer = customerRepository.findById(customerId).get();
		System.out.println(customer);
		Product product = productRepository.findById(productId).get();
		System.out.println(product);
		CartItem cartItem = new CartItem();
		cartItem.setCustomer(customer);
		cartItem.setProduct(product);

		CartItem saveCartItem = cartItemRepository.save(cartItem);
		System.out.println(saveCartItem);
	}

}
