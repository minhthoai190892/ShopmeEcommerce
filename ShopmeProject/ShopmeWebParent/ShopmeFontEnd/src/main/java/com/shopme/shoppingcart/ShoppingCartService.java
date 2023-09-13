package com.shopme.shoppingcart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;
import com.shopme.product.ProductRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ShoppingCartService {
	@Autowired 
	private CartItemRepository cartItemRepository;
	@Autowired 
	private ProductRepository productRepository;
	public Integer addProduct(Integer productId,Integer quantity,Customer customer) throws ShoppingCartException {
		Integer updateQuantity = quantity;
		Product product = new Product(productId);
		CartItem cartItem = cartItemRepository.findByCustomerAndProduct(customer, product);
		if (cartItem!=null) {
			updateQuantity = cartItem.getQuantity()+quantity;
			if (updateQuantity>5) {
				throw new ShoppingCartException("Could not add "+quantity+" item(s) because there's already "+cartItem.getQuantity()+" item(s) in your shopping cart. Maximum allowed quantity is 5.");
			}
		}else {
			cartItem = new CartItem();
			cartItem.setCustomer(customer);
			cartItem.setProduct(product);
		}
		cartItem.setQuantity(updateQuantity);
		cartItemRepository.save(cartItem);
		return updateQuantity;
	}
	public List<CartItem> listCartItems(Customer customer) {
		return cartItemRepository.findByCustomer(customer);
	}
	public float updateQuantity(Integer productId,Integer quantity,Customer customer) {
		cartItemRepository.updateQuantity(quantity, customer.getId(), productId);
		Product product = productRepository.findById(productId).get();
		float subtotal = product.getDiscountPrice()*quantity;
		return subtotal;
	}
	public void removeProduct(Integer productId,Customer customer) {
		cartItemRepository.deleteByCustomerAndProduct(customer.getId(), productId);
	}
}