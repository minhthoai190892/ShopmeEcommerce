package com.shopme;

import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.shopme.setting.EmailSettingBag;

import jakarta.servlet.http.HttpServletRequest;

public class Utility {
	public static String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		System.out.println("Utility > siteURL: "+siteURL);
		return siteURL.replace(request.getServletPath(), "");
	}
	public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings) {
		JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
		mailSenderImpl.setHost(settings.getHost());
		mailSenderImpl.setPort(settings.getPort());
		mailSenderImpl.setUsername(settings.getUserame());
		mailSenderImpl.setPassword(settings.getPassword());
		Properties mailProperties= new Properties();
		mailProperties.setProperty("mail.smtp.auth", settings.getSmtpAuth());
		mailProperties.setProperty("mail.smtp.starttls.enable", settings.getSmtpSecured());
		mailSenderImpl.setJavaMailProperties(mailProperties);
		return mailSenderImpl;
	}
}
