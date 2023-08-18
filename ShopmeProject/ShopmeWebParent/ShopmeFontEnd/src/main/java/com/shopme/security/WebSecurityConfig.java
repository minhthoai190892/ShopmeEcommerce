package com.shopme.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration

public class WebSecurityConfig {
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		/*
		http.authorizeHttpRequests(configurer -> configurer.anyRequest().permitAll()// không cần xét đăng nhập
//                        .authenticated()//phải đăng nhập
 * */
		http.authorizeHttpRequests(configurer -> configurer.requestMatchers("/user").authenticated().anyRequest().permitAll()
		).formLogin(login -> login
				.loginPage("/login")// hiển thị form login
				.usernameParameter("email")//sử dụng email để đăng nhập
				.permitAll())
		.logout(logout->logout.permitAll())
		.rememberMe(me->me
						  .key("3a46695e7e504b2e94c07d17f18d0366")
						  .tokenValiditySeconds(14*24*60*60)
				)
		;
		return http.build();

	}
	@Bean
	 UserDetailsService userDetailsService() {
		return new CustomerUserDetailsService();
	}

	/**
	 * Hàm thêm các thư thư viện
	 * 
	 * @return
	 */
	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().requestMatchers("/css/**", "/images/**", "/js/**", "/fontawesome/**");
	}

}
