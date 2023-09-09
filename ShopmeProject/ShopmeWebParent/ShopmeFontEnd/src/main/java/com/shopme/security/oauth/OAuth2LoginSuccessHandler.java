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
		CustomerOAuth2User oauth2User = (CustomerOAuth2User) authentication.getPrincipal();

		String name = oauth2User.getName();
		String email = oauth2User.getEmail();
		//lấy code của Country khi đăng nhập bằng GOOGLE
		String countryCode = request.getLocale().getCountry();
		String clientName = oauth2User.getClientName();
		System.err.println("Client Name: "+clientName);
		Customer customer = customerService.getCustomerByEmail(email);
		AuthenticationType authenticationType = getAuthenticationType(clientName);
		if (customer == null) {
			customerService.addNewCustomerUponOAuthLogin(name, email, countryCode,authenticationType);;
		} else {
			oauth2User.setFullName(customer.getFullName());
			customerService.updateAuthenticationType(customer, authenticationType);
		}
		System.err.println("onAuthenticationSuccess Handler: " + name + " " + email+" "+countryCode);

		super.onAuthenticationSuccess(request, response, authentication);
	}
	/**
	 * Hàm so sánh và lấy theo loại đăng nhập
	 * @param clientName tên loại đăng nhập
	 * 
	 * */
	private AuthenticationType getAuthenticationType(String clientName) {
		if (clientName.equals("61157404128-177hlpi5f435v2fj26f2io3lhe5q9ujp.apps.googleusercontent.com")) {
			return AuthenticationType.GOOGLE;
		}else if (clientName.equals("298301506215098")) {
			return AuthenticationType.FACEBOOK;
		} else {
			return AuthenticationType.DATABASE;
		}
	}

}
