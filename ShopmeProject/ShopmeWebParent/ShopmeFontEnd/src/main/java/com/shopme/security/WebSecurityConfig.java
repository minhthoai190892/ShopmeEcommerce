package com.shopme.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.shopme.security.oauth.CustomerOAuth2UserService;
import com.shopme.security.oauth.OAuth2LoginSuccessHandler;

@Configuration

public class WebSecurityConfig {
	@Autowired
	private CustomerOAuth2UserService auth2UserService;
	@Autowired
	private OAuth2LoginSuccessHandler auth2LoginSuccessHandler;
	@Autowired
	private DatabaseLoginSuccessHandler databaseLoginSuccessHandler;
	@Bean
	 PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		/*
		http.authorizeHttpRequests(configurer -> configurer.anyRequest().permitAll()// không cần xét đăng nhập
//                        .authenticated()//phải đăng nhập
 * */
		http.authorizeHttpRequests(configurer -> configurer.requestMatchers("/account_details","/update_account_details","/cart").authenticated().anyRequest().permitAll()
		).formLogin(login -> login
				.loginPage("/login")// hiển thị form login
				.usernameParameter("email")//sử dụng email để đăng nhập
				.successHandler(databaseLoginSuccessHandler)
				.permitAll())
		.oauth2Login(oAuth2->oAuth2
								  .loginPage("/login")
								  .userInfoEndpoint()
								  .userService(auth2UserService)
								  .and().successHandler(auth2LoginSuccessHandler)
								  )
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
