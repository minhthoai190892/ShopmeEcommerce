package com.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

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

	/** Lấy danh sách users từ cơ sở dữ liệu
	 * @return
	 */
	public List<User> listAll() {
		return (List<User>) userRepository.findAll();
	}

	/**
	 * Lấy danh sách Roles từ cơ sở dữ liệu
	 * @return
	 */
	public List<Role> listRoles() {
		return (List<Role>) roleRepository.findAll();
	}

	/**
	 * Hàm lưu thông tin người dùng
	 * @param user
	 */
	public void save(User user) {
		// kiểm tra cập nhật
		// tạo biến boolen để kiểm tra xem id người dùng đã có hay chưa
		boolean isUpdatingUser = (user.getId() != null);// ==>người dùng đã tồn tại
		// kiểm tra trạng thái update hay không
		if (isUpdatingUser) {// ==> đang update user
			// lấy người dùng đã tồn tại
			User existingUser = userRepository.findById(user.getId()).get();
			// kiểm tra biếu mẫu ở html có trống không
			if (user.getPassword().isEmpty()) {// =>biếu mẫu trên html trống
				// muốn giữ lại password
				// --ta lấy lại password từ csdl
				user.setPassword(existingUser.getPassword());
			} else {// =>biếu mẫu trên html không trống
					// muốn đổi lại password
					// --ta mã hóa lại password
				encodePassword(user);
			}
		} else {// nếu không update thì phải đặt mật khẩu

			// gọi hàm mã hóa mật khẩu
			encodePassword(user);
		}

		// lưu "user"
		userRepository.save(user);
	}


	/**
	 * Hàm mã hóa passowrd
	 * @param user 
	 */
	private void encodePassword(User user) {
		// mã hóa mật khẩu người dùng
		String encodePassword = passwordEncoder.encode(user.getPassword());
		// thây đổi mật khảu người dùng bằng mật khẩu đã mã hóa
		user.setPassword(encodePassword);
	}

	
	/**
	 * hàm kiểm tra email là duy nhất
	 * kiểm tra email mới tạo hay là email được cập nhật
	 * @param id id người dùng
	 * @param email email người dùng
	 * @return
	 */
	@SuppressWarnings("unused")
	public boolean isEmailUnique(Integer id, String email) {
		// lấy user bằng email
		User userByEmail = userRepository.getUserByEmail(email);
		// kiểm tra email có phải là duy nhất không
		if (userByEmail == null) {
			// nếu đúng thì email đó là duy nhất trong csdl
			return true;
		}
		// kiểm tra người đung đang được chỉnh sửa
		// ---tạo biến boolen để kiểm tra người dùng đã có hay chưa
		boolean isCreatingNew = (id == null);// ==>người dùng mới
		// kiểm ra xem isCreatingNew là mới hay củ
		if (isCreatingNew) {
			// kiểm tra email user có hay chưa
			if (userByEmail != null) {
				// nếu user email đã tồn tại
				return false;
			}
		} else {
			// kiểm tra id của người với id được chỉnh sủa
			if (userByEmail.getId() != id) {
				// ==> email không phải là duy nhất
				return false;
			}
		}

		return true;

	}

	/**
	 * Phương thức tìm người đùng
	 * @param id id người dùng cần tìm
	 * @return trả về id của người dùng hoặc là thông báo không tìm thấy
	 * @throws UserNotFoundException
	 */
	public User get(Integer id) throws UserNotFoundException {
		try {
			return userRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			// gọi lớp
			throw new UserNotFoundException("Could not find any user with id: " + id);
		}

	}
	public void delete(Integer id) throws UserNotFoundException {
		//lấy id của người dùng
		Long countById = userRepository.countById(id);
		//kiểm tra id người dùng có tồn tại không
		if (countById==null || countById==0) {
			//=> người dùng không tồn tại
			throw new UserNotFoundException("Could not find any user with id: " + id);
		}
		//=> người dùng có tồn tại
		//gọi hàm deleteById của interface CrudRepository
		userRepository.deleteById(id);
	}
}