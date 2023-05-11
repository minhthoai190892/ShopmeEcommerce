package com.shopme.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;



@Controller
public class UserController {

//	khai báo Service
	@Autowired
	private UserService service;
	/**
	 * @param model cần quyền truy cập vào model
	 * @return về trang html
	 */
	@GetMapping("/users")
	//cần quyền truy cập Model
	public String listAll(Model model) {
		//tạo một danh sách "users"
		List<User> listUsers = service.listAll();
		//thêm vào model
		model.addAttribute("listUsers", listUsers);
		return "users";
	}
	//đường dẫn giống với html "/users/new"
	@GetMapping("/users/new")
	public String newUser(Model model) {
		// lấy danh sách "role" từ cơ sở dữ liệu
		List<Role> listRoles = service.listRoles();
		//tạo mới user
		User user = new User();
		//enable luôn true
		user.setEnable(true);
		//thêm user vào model
		model.addAttribute("user", user);
		//thêm listrole vào model
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create New User");
		//trả về form html
		return "user_form";
	}
	//ánh xạ lưu người dùng giống với form html
	@PostMapping("/users/save")
	public String saveUser(User user,RedirectAttributes redirectAttributes) {
		System.out.println(user);
		//lưu "user"
		service.save(user);
		//thông báo lưu thành công (để hiển thị tại trang danh sách "user")
		redirectAttributes.addFlashAttribute("message", "The user has been save successfully.");
		
		return "redirect:/users";
	}
	// ánh xạ đường dẫn để lấy người dùng
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id")Integer id //lấy id từ trang html
			,RedirectAttributes redirectAttributes //thông báo cho trang html
			,Model model 
			) {
		try {
			//lấy thông tin của người dùng
			User user = service.get(id);
			//lấy danh sách Role
			List<Role> listRoles = service.listRoles();
			//có người dùng ta đặt vào model
			model.addAttribute("user", user);
			model.addAttribute("pageTitle", "Edit User");
			model.addAttribute("listRoles", listRoles);
			//trả về form user_form
			return "user_form";
		} catch (UserNotFoundException e) {
			//dùng để hiện thông báo trên trang html
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			//không tìm thấy trả về trang users 
			return "redirect:/users";
		}
		
	}
}
