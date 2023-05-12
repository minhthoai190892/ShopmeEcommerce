package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	// khai báo trường repository
	@Autowired
	private UserRepository repo;
	// kiểm tra đơn vị với kho lưu trữ
	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateUserWithOneRole() {
		// lấy "id" của "role" trong bảng "roles" trong database
		Role roleAdmin = entityManager.find(Role.class, 1);
		// tạo mới "user"
		User userThoai = new User("minhthoai@gmailqwe.com", "123", "Nguyen", "Thoaias");
		// thêm "role" cho "user"
		userThoai.addRole(roleAdmin);
		// lưu "user" và database
		User saveUser = repo.save(userThoai);
		// khẳn định khi save "user" lớn hơn 0 nghĩa là đối tượng đã được lưu
		assertThat(saveUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateUserWithOneRoles() {
		// tạo mới một "user"
		User userDoann = new User("doanasdle@gmail.com", "123", "Le", "Doansdf");
		// lấy "id" của "role"
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		// thêm roles vào user
		userDoann.addRole(roleAssistant);
		userDoann.addRole(roleEditor);
		// lưu "user" vào database
		User saveUser = repo.save(userDoann);
		// khẳng định
		assertThat(saveUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAllUsers() {
		// tạo một danh sách users
//		List<User> listUsers = (List<User>) repo.findAll();
		Iterable<User> listUsers = repo.findAll();
		// biểu thức lambda
		listUsers.forEach(user -> System.out.println(user));
	}

	@Test
	public void testGetUserById() {
		// lấy id của người dùng
		User userThoai = repo.findById(3).get();
		System.out.println(userThoai);
		assertThat(userThoai).isNotNull();
	}

	@Test
	public void testUpdateUserDetails() {
		// lấy id của người dùng
		User userThoai = repo.findById(3).get();
		// cập nhật dữ liệu
		userThoai.setEmail("Lethidoan@gmail.com");
		userThoai.setEnable(true);
		// lưu lại user
		repo.save(userThoai);
	}

	@Test
	public void testUpdateUserRoles() {
		// lấy user
		User userDoan = repo.findById(3).get();
		// lấy role
		Role roleEditor = new Role(3);
		Role roleShipper = new Role(4);
		// xóa một role
		userDoan.getRoles().remove(roleEditor);
		// thêm một role mới
		userDoan.addRole(roleShipper);
		// lưu lại thông tin
		repo.save(userDoan);

	}

	@Test
	public void testDeleteUser() {
		// lấy "id" của "user"
		Integer id = 1;
		// xóa "user" bằng id
		repo.deleteById(id);
	}

	@Test
	public void testGetUserByEmail() {
		// lấy user bằng email
		String email = "thoainguyen@gmail.com";
		User user = repo.getUserByEmail(email);
		System.out.println(user);
		assertThat(user).isNotNull();
	}
	@Test
	public void testCountById() {
		Integer id =10;
		Long countById = repo.countById(id);
		assertThat(countById).isNotNull().isGreaterThan(0);
	}

}
