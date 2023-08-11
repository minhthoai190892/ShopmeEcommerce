package com.shopme.customer;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.Utility;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
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
		//lấy danh sách Country
		List<Country> listCountries = customerService.listAllCountries();
		model.addAttribute("listCountries",listCountries);
		model.addAttribute("pageTitle","Customer Registration");
		model.addAttribute("customer",new Customer());
		return "register/register_form";
	}
	@PostMapping("/create_customer")
	public String createCustomer(Customer customer,Model model,HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		customerService.registerCustomer(customer);
		sendVerificationEmail(request,customer);
		model.addAttribute("pageTitle","Registration Success");
		return "/register/register_success";
	}
	/**
	 * Hàm send Verification Email khi tạo mới một customer
	 * @param request
	 * @param customer đối tượng vừa tạo
	 * 
	 * */
	private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws UnsupportedEncodingException, MessagingException {
		//
		EmailSettingBag emailSettingBag = settingService.getEmailSettings();
		
		JavaMailSenderImpl mailSenderImpl = Utility.prepareMailSender(emailSettingBag);
		String toAddress = customer.getEmail();
		String subject = emailSettingBag.getCustomerVerifySubject();
		String content = emailSettingBag.getCustomerVerifyContent();
		
		MimeMessage message = mailSenderImpl.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom(emailSettingBag.getFromAddress(),emailSettingBag.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		content = content.replace("[[name]]", customer.getFullName());
		String verifyURL = Utility.getSiteURL(request)+"/verify?code="+customer.getVerificationCode();
		content = content.replace("[[URL]]", verifyURL);
		helper.setText(content,true);
		mailSenderImpl.send(message);
		System.err.println("to Address: "+toAddress);
		System.err.println("verify URL: "+verifyURL);
	}
	@GetMapping("/verify")
	public String verifyAccount(@Param("code")String code,Model model) {
		boolean verified = customerService.verify(code);
		return "register/"+(verified?"verify_success":"verify_fail");
	}
}
