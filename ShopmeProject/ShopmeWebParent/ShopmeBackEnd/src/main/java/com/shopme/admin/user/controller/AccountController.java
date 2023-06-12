package com.shopme.admin.user.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.user.UserService;
import com.shopme.common.entity.User;

@Controller
public class AccountController {
	// khai báo service
	@Autowired
	private UserService service;

	// tạo liên kết
	@GetMapping("/account")
	public String viewDetails(Model model
			// lấy user đại diện cho user đang đăng nhập
			, @AuthenticationPrincipal ShopmeUserDetails logedUser) {
		// ta lấy được user đang đăng nhập (email)
		String email = logedUser.getUsername();
		// gọi hàm lấy User
		User user = service.getByEmail(email);
		//thêm đối tượng vào model
		model.addAttribute("user",user);
		//trả về trên chế độ xem "account_form"
		return "users/account_form";

	}
	@PostMapping("/account/update")
	public String saveDetails(User user
			// lấy user đại diện cho user đang đăng nhập
			, @AuthenticationPrincipal ShopmeUserDetails logedUser
			,RedirectAttributes redirectAttributes
			,@RequestParam("image") MultipartFile multipartFile //image là tên file của html,MultipartFile là đại diện cho các file được upload  
			) throws IOException {
		System.out.println(user);
		System.out.println(multipartFile.getOriginalFilename());
		//kiểm tra biểu mẫu có được tải lên không (image)
		if (!multipartFile.isEmpty()) { //biễu mẫu không trống(image)
			//=> có hình ảnh mới tạo thư mục
			//StringUtils: dùng để làm sạch đường dẫn
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());//tên file(hình ảnh) 
			//gán hình ảnh được tải lên vào field photo
			user.setPhotos(fileName);
			//save người dùng
			User savedUser = service.updateAccount(user);
			String uploadDir = "user-photos/"+savedUser.getId(); //tạo thư mục với id của người dùng
			//gọi hàm làm sạch tập tin
			FileUploadUtil.cleanDir(uploadDir);
			
			//gọi hàm lưu thư tập tin
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile); 
			
		} else {
			if (user.getPhotos().isEmpty()) {
				user.setPhotos(null);
			}
//		//lưu "user"
			service.updateAccount(user);
		}
		//lấy thông tin người đang đăng nhập
		logedUser.setFirstName(user.getFirstName());
		logedUser.setLastName(user.getLastName());
		
//		//thông báo lưu thành công (để hiển thị tại trang danh sách "user")
		redirectAttributes.addFlashAttribute("message", "Your account details have been updated.");
//		
		return "redirect:/account";
	}
}
