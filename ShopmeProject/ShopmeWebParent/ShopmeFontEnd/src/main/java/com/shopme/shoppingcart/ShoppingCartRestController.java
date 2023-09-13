package com.shopme.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ShoppingCartRestController {
	@Autowired
	private ShoppingCartService shoppingCartService;
	@Autowired
	private CustomerService customerService;

	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable(name = "productId") Integer productId,
			@PathVariable(name = "quantity") Integer quantiry, HttpServletRequest request) {
		try {
			Customer customer = getAuthenticatedCustomer(request);
			Integer updateQuantity = shoppingCartService.addProduct(productId, quantiry, customer);

			return updateQuantity + " item(s) of this product were added to your shopping cart";
		} catch (CustomerNotFoundException e) {
			// TODO: handle exception
			return "You must login to add this product to cart.";
		} catch (ShoppingCartException e) {
			// TODO: handle exception
			return e.getMessage();
		}

	}

	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		System.err.println("ShoppingCartRestController>>getAuthenticatedCustomer");
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		System.out.println(email);
		if (email == null) {
			throw new CustomerNotFoundException("No authenticated customer");
		}
		return customerService.getCustomerByEmail(email);

	}

	@PostMapping("/cart/update/{productId}/{quantity}")
	public String updateQuantity(@PathVariable(name = "productId") Integer productId,
			@PathVariable(name = "quantity") Integer quantiry, HttpServletRequest request) {
		try {
			Customer customer = getAuthenticatedCustomer(request);

			float subtotal = shoppingCartService.updateQuantity(productId, quantiry, customer);

			return String.valueOf(subtotal);
		} catch (CustomerNotFoundException e) {
			// TODO: handle exception
			return "You must login to change of product.";
		}
	}

	@DeleteMapping("/cart/remove/{productId}")
	public String updateQuantity(@PathVariable(name = "productId") Integer productId, HttpServletRequest request) {
		try {
			Customer customer = getAuthenticatedCustomer(request);
			shoppingCartService.removeProduct(productId, customer);
			return "The product has been removed from your shopping cart";
		} catch (CustomerNotFoundException e) {
			// TODO Auto-generated catch block
			return "You must login to remove product.";
		}

	
	}
}
