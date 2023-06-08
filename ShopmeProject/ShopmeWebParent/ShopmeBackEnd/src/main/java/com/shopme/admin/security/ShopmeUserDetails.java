package com.shopme.admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

public class ShopmeUserDetails implements UserDetails {
	
	private User user;
	
	public ShopmeUserDetails(User user) {
		super();
		this.user = user;
	}

	@Override
    //hàm trả về một tập hợp các thẩm quyền (authority) được cấp hoặc một danh sách các roles
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//tạo một tập "roles"
		Set<Role> roles = user.getRoles();
		//tạo một danh sách
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (Role role: roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return user.isEnable();
	}
    public String getFullName(){
        return this.user.getFullName();
    }

}
