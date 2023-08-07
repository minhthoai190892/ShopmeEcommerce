package com.shopme.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration

public class WebSecurityConfig {
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(configurer -> configurer.anyRequest().permitAll()// không cần xét đăng nhập
//                        .authenticated()//phải đăng nhập
		).formLogin(login -> login// hiển thị form login
				.permitAll());
		return http.build();

	}

	/**
	 * Hàm thêm các thư thư viện
	 * 
	 * @return
	 */
	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().requestMatchers("/css/**", "/images/**", "/js/**", "/fontawesome/**")

		;
	}

}
