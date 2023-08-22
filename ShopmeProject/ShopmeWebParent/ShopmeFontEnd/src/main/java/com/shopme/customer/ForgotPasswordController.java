package com.shopme.customer;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.SettingService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ForgotPasswordController {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private SettingService settingService;
	
	@GetMapping("/forgot_password")
	public String showRequestForm() {

		return "customer/forgot_password_form";
	}

	@PostMapping("/forgot_password")
	public String processRequestForm(HttpServletRequest request,Model model){
		System.err.println("/forgot_password");
		String email = request.getParameter("email");

		try {
			String token = customerService.updateResetPasswordToken(email);
			System.out.println("/forgot_password >> token " + token);
			System.out.println("/forgot_password >> email " + email);
			//gọi method resetpassword
			String link = Utility.getSiteURL(request)+"/reset_password?token="+token;
			System.out.println("/forgot_password >> link "+link);
			sendEmail(link, email);
			
			model.addAttribute("message","We have sent a reset password link to your email. Please check.");
		} catch (CustomerNotFoundException e) {
			model.addAttribute("error",e.getMessage());
		}catch (UnsupportedEncodingException|MessagingException e) {
			// TODO: handle exception
			model.addAttribute("error","Could not send emaill");
		}

		return "customer/forgot_password_form";
	}
	private void sendEmail(String link,String email) throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettingBag = settingService.getEmailSettings();
		JavaMailSenderImpl mailSenderImpl = Utility.prepareMailSender(emailSettingBag);
		String toAddress = email;
		String subject = "Here's the link to reset your password";
		String content = "<p>Hello,</p>"
						 +"<p>You have requested to reset your password.</p>"
						 +"<p>Click the link below to change your password:</p>"
						 +"<p><a href=\""+link+"\">Change my password</a></p>"
						 +"<br>"
						 +"<p>Ignore this email if you do remember your password, or you have not made the request.</p>";
		MimeMessage message = mailSenderImpl.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom(emailSettingBag.getFromAddress(), emailSettingBag.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSenderImpl.send(message);
		System.out.println("sendEmail > mailSenderImpl "+mailSenderImpl);
		
	}
	@GetMapping("/reset_password")
	public String showResetPasswordForm(@Param("token")String token,Model model) {
		System.err.println("/reset_password");
		Customer customer = customerService.getByResetPasswordToken(token);
		System.out.println("/reset_password > customer: "+customer);
		if (customer!=null) {
			model.addAttribute("token",token);
			model.addAttribute("pageTitle","reset password");
		}else {
			model.addAttribute("pageTitle","Invalid Token");
			model.addAttribute("message","Invalid Token");
			return "message";
		}
		return "customer/reset_password_form";
	}
	@PostMapping("/reset_password")
	public String processResetForm(HttpServletRequest request,Model model) {
		String token = request.getParameter("token");
		String password = request.getParameter("password");
		try {
			customerService.updatePassword(token, password);
			model.addAttribute("message","You have successfully changed your your password");
			model.addAttribute("title","Reset your password");
			return "message";
		} catch (CustomerNotFoundException e) {
			model.addAttribute("pageTitle","Invalid Token");
			model.addAttribute("message",e.getMessage());
			return "message";
		}
		
	}
}
