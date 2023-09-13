package com.shopme.shoppingcart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.Utility;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ShoppingCartController {
	@Autowired
	private ShoppingCartService cartService;
	@Autowired
	private CustomerService customerService;

	@GetMapping("/cart")
	public String viewCart(Model model, HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);
		List<CartItem> cartItems = cartService.listCartItems(customer);
		float estimatedTotal = 0.0F;
		for (CartItem item : cartItems) {
			estimatedTotal+=item.getSubtotal();
		}
		model.addAttribute("pageTitle", "Shopping Cart");
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("estimatedTotal", estimatedTotal);
		return "cart/shopping_cart";
	}

	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		System.err.println("ShoppingCartRestController>>getAuthenticatedCustomer");
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		System.out.println(email);

		return customerService.getCustomerByEmail(email);

	}
}