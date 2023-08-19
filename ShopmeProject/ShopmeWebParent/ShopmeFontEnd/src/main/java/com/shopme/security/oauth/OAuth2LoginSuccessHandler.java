package com.shopme.security.oauth;

import java.io.IOException;

import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	@Autowired
	CustomerService customerService;
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		// TODO Auto-generated method stub
		CustomerOAuth2User auth2User = (CustomerOAuth2User) authentication.getPrincipal();

		String name = auth2User.getName();
		String email = auth2User.getEmail();
		//lấy code của Country khi đăng nhập bằng GOOGLE
		String countryCode = request.getLocale().getCountry();
		Customer customer = customerService.getCustomerByEmail(email);
		if (customer == null) {
			customerService.addNewCustomerUponOAuthLogin(name, email, countryCode);;
		} else {
			customerService.updateAuthenticationType(customer, AuthenticationType.GOOGLE);
		}
		System.err.println("onAuthenticationSuccess Handler: " + name + " " + email+" "+countryCode);

		super.onAuthenticationSuccess(request, response, authentication);
	}

}
