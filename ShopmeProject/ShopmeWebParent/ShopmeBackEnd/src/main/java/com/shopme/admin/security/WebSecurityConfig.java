package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
	@Bean
	// lớp mã hóa mật khẩu
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// cho phép vào trang web trực tiếp không cần phải đăng nhập
		http.authorizeHttpRequests().anyRequest().authenticated().and().formLogin().loginPage("/login").permitAll();
		;
		return http.build();

	}
	/**
	 * Hàm thêm các thư thư viện
	 * @return
	 */
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web->web.ignoring()
				.requestMatchers("/css/**","/images/**","/js/**")
				
				;
	}

}
