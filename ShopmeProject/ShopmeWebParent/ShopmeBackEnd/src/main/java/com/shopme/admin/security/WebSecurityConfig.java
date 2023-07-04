package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

import org.springframework.security.core.userdetails.UserDetailsService;
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
	public UserDetailsService userDetailsService() {
		return new ShopmeUserDetailsService();
	}



    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .authorizeHttpRequests(configurer -> configurer
        				
                        .requestMatchers("/users/**").hasAnyAuthority("Admin")//chỉ những roles được chỉ định mới có quyền vào đường dẫn users
                        .requestMatchers("/categories/**").hasAnyAuthority("Admin", "Editor")//chỉ những roles được chỉ định mới có quyền vào đường dẫn users
                        .anyRequest()
                        .authenticated()
        )


                .formLogin(login -> login//hiển thị form login
                        .loginPage("/login")//hiển thị form login custom
                        .usernameParameter("email")//sử dụng email để đăng nhập
                        .permitAll())
                .logout(logout -> logout//logout 
                        .permitAll())
                .rememberMe(me -> me//kích hoạt nhớ tài khoản
                        //tạo một key mặc định cho ứng dụng trong cookie
                        .key("bWluaHRob2FpJTQwZ21haWxxd2UuY29tOjE2ODc0OTQ0NzEwNTA6U0hBMjU2OjdiNTk5NTliOTU1MDUxOTgwOTJjMzFhYzMwMGIyNDc0MTA5ZjQ0YWE5YmJlZmE0ZWExZDIyZDRjOWYyMjlkMjA")
                        .tokenValiditySeconds(7 * 24 * 60 * 60))//thây đổi thời gian hết hạn của key 
		;

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
