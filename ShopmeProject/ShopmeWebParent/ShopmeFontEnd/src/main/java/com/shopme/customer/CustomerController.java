package com.shopme.customer;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.Utility;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.security.CustomerUserDetails;
import com.shopme.security.oauth.CustomerOAuth2User;
import com.shopme.setting.CountryRepository;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.SettingService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private SettingService settingService;

	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		// lấy danh sách Country
		List<Country> listCountries = customerService.listAllCountries();
		for (Country country : listCountries) {
			System.err.println(country.getName());
		}
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("pageTitle", "Customer Registration");
		model.addAttribute("customer", new Customer());
		return "register/register_form";
	}

	@PostMapping("/create_customer")
	public String createCustomer(Customer customer, Model model, HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException {
		customerService.registerCustomer(customer);
		sendVerificationEmail(request, customer);
		model.addAttribute("pageTitle", "Registration Success");
		return "/register/register_success";
	}

	/**
	 * Hàm send Verification Email khi tạo mới một customer
	 * 
	 * @param request
	 * @param customer đối tượng vừa tạo
	 * 
	 */
	private void sendVerificationEmail(HttpServletRequest request, Customer customer)
			throws UnsupportedEncodingException, MessagingException {
		//
		EmailSettingBag emailSettingBag = settingService.getEmailSettings();

		JavaMailSenderImpl mailSenderImpl = Utility.prepareMailSender(emailSettingBag);
		String toAddress = customer.getEmail();
		String subject = emailSettingBag.getCustomerVerifySubject();
		String content = emailSettingBag.getCustomerVerifyContent();

		MimeMessage message = mailSenderImpl.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom(emailSettingBag.getFromAddress(), emailSettingBag.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		content = content.replace("[[name]]", customer.getFullName());
		String verifyURL = Utility.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();
		content = content.replace("[[URL]]", verifyURL);
		helper.setText(content, true);
		mailSenderImpl.send(message);
		System.err.println("to Address: " + toAddress);
		System.err.println("verify URL: " + verifyURL);
	}

	@GetMapping("/verify")
	public String verifyAccount(@Param("code") String code, Model model) {
		boolean verified = customerService.verify(code);
		return "register/" + (verified ? "verify_success" : "verify_fail");
	}

	@GetMapping("/account_details")
	public String viewAccountDetails(Model model, HttpServletRequest request) {
		String email = getEmailOfAuthenticatedCustomer(request);
		System.err.println("account_details>>>>>>>"+email);
		Customer customer = customerService.getCustomerByEmail(email);

		List<Country> listCountries = customerService.listAllCountries();
		model.addAttribute("customer", customer);
		model.addAttribute("listCountries", listCountries);
		return "customer/account_form";
	}

	private String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
		Object principal = request.getUserPrincipal();
		String customerEmail = null;
		if (principal instanceof UsernamePasswordAuthenticationToken
				|| principal instanceof RememberMeAuthenticationToken) {
			customerEmail = request.getUserPrincipal().getName();
			System.err.println("customerEmail>>>>>>" + customerEmail);
		} else if (principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oauth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
			CustomerOAuth2User oAuth2User = (CustomerOAuth2User) oauth2AuthenticationToken.getPrincipal();
			customerEmail = oAuth2User.getEmail();
			System.err.println("customerEmail......" + customerEmail);
		}
		return customerEmail;
	}

	@PostMapping("/update_account_details")
	public String updateAccountDetails(Model model, Customer customer,RedirectAttributes redirectAttributes,HttpServletRequest request) {
		
		customerService.update(customer);
		redirectAttributes.addFlashAttribute("message","Your account details have been updated.");
		updateNameForAuthenticatedCustomer(customer,request);
		return "redirect:/account_details";
	}

	private void updateNameForAuthenticatedCustomer(Customer customer,HttpServletRequest request) {
		Object principal = request.getUserPrincipal();
		
		if (principal instanceof UsernamePasswordAuthenticationToken
				|| principal instanceof RememberMeAuthenticationToken) {
			CustomerUserDetails userDetails = getCustomerUserDetailsObject(principal);
			System.err.println("userDetails=============="+userDetails);
			Customer authenticatedCustomer = userDetails.getCustomer();
			authenticatedCustomer.setFirstName(customer.getFirstName());
			authenticatedCustomer.setLastName(customer.getLastName());
		} else if (principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oauth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
			CustomerOAuth2User oAuth2User = (CustomerOAuth2User) oauth2AuthenticationToken.getPrincipal();
			String fullName = customer.getFirstName()+ " "+customer.getLastName();
			oAuth2User.setFullName(fullName);
		}
	}
	private CustomerUserDetails getCustomerUserDetailsObject(Object principal) {
		CustomerUserDetails  userDetails = null;
		if (principal instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
		userDetails=	(CustomerUserDetails) token.getPrincipal();
		}else if (principal instanceof RememberMeAuthenticationToken) {
			RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;
			userDetails= (CustomerUserDetails) token.getPrincipal();
		}
		return userDetails;
	}
}
