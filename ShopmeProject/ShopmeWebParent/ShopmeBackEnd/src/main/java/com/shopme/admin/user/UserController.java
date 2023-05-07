package com.shopme.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.common.entity.User;



@Controller
public class UserController {

//	khai báo Service
	@Autowired
	private UserService service;
	@GetMapping("/users")
	//cần quyền truy cập Model
	public String listAll(Model model) {
		//tạo một danh sách "users"
		List<User> listUsers = service.listAll();
		//thêm vào model
		model.addAttribute("listUsers", listUsers);
		return "users";
	}
}
