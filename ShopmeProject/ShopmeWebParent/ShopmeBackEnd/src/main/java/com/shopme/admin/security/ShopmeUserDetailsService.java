package com.shopme.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.shopme.admin.user.UserRepository;
import com.shopme.common.entity.User;

public class ShopmeUserDetailsService implements UserDetailsService {

		@Autowired 
		private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		//gọi hàm lấy email
	User user=	userRepository.getUserByEmail(email);
		//kiểm tra đối tượng người có null không
		if (user!=null) {//=>không null
			return new ShopmeUserDetails(user);
		}
		throw new UsernameNotFoundException("Could not find user with email: "+email);
	}

}
