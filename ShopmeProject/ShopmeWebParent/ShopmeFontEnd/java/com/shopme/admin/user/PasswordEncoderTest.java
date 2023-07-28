package com.shopme.admin.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
	@Test
	public void testEncodePassword() {
		//khai báo đối tượng BCryptPasswordEncoder để thực hiện mã hóa mật khẩu
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		//tạo mật khẩu
		String rawPassword = "thoai2021";
		//thực hiện mã hóa mật khẩu
		String encodePassword = bCryptPasswordEncoder.encode(rawPassword);
		System.out.println(encodePassword);
		//matches: xác định chuổi này có giống với với regular expression đã cho
		System.out.println(bCryptPasswordEncoder.matches(rawPassword, encodePassword));
		
	}

}
