package com.shopme.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
public class UserService {
	// khai báo repository
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;

	// khai báo lơp
	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<User> listAll() {
		return (List<User>) userRepository.findAll();
	}

	public List<Role> listRoles() {
		return (List<Role>) roleRepository.findAll();
	}

	public void save(User user) {
		// gọi hàm mã hóa mật khẩu
		encodePassword(user);
		// lưu "user"
		userRepository.save(user);
	}

	private void encodePassword(User user) {
		// mã hóa mật khẩu người dùng
		String encodePassword = passwordEncoder.encode(user.getPassword());
		// thây đổi mật khảu người dùng bằng mật khẩu đã mã hóa
		user.setPassword(encodePassword);
	}

	/**
	 * @param email email cần tìm
	 * @return true/false
	 */
	public boolean isEmailUnique(String email) {
		// lấy user bằng email
		User userByEmail = userRepository.getUserByEmail(email);
		//userByEmail = null: thì người dùng là duy nhất
		return userByEmail == null;

	}
}