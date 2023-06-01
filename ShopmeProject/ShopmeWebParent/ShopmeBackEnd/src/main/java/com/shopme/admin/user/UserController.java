package com.shopme.admin.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
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
	public String listFirstPage(Model model) {
//		//tạo một danh sách "users"
//		List<User> listUsers = service.listAll();
//		//thêm vào model
//		model.addAttribute("listUsers", listUsers);
//		return "users";
		return listByPage(1, model,"firstName","asc", null);
	}
	/**
	 * hàm hiển thị phân trên html
	 * @param pageNum lấy số trang trên đường dẫn
	 * @param model
	 * @return
	 */
	@GetMapping("/users/page/{pageNum}")
	public String listByPage(
			@PathVariable(name = "pageNum")int pageNum //@PathVariable thì được dùng để trích xuất dữ liệu từ URL path
			,Model model
			,@Param("sortField") String sortField,@Param("sortDir") String sortDir
			,@Param("keyword") String keyword
			) {
		Page<User> pageUser = service.listByPage(pageNum,sortField,sortDir, keyword);
		//trả về nội dung của trang dưới dạng danh sách
		List<User> listUsers = pageUser.getContent();
		long startCount = (pageNum-1)*UserService.USER_PER_PAGE+1;
		long endCount = startCount+UserService.USER_PER_PAGE-1;
		if (endCount>pageUser.getTotalElements()) {
			endCount=pageUser.getTotalElements();
		}
		String reverseSortDir = sortDir.equals("asc")?"desc":"asc";
		System.out.println("Pagenum = "+pageNum);
		System.out.println("Total elements = "+pageUser.getTotalElements());
		System.out.println("Total page = "+pageUser.getTotalPages());
		System.out.println("------------------");
		System.out.println("startCount = "+startCount);
		System.out.println("TendCount = "+endCount);
		System.out.println("--------Sort----------");
		System.out.println("Sort Field: "+sortField);
		System.out.println("Sort Dir: "+sortDir);
		System.out.println("reverseSortDir: "+reverseSortDir);
		System.out.println("keyword: "+keyword);
		
		//thêm vào model dùng để hiển thị trên html 
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", pageUser.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageUser.getTotalElements());
		//thêm vào model dùng để hiển thị icon trên html 
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		//search
		model.addAttribute("keyword", keyword);
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
	public String saveUser(User user
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
			User savedUser = service.save(user);
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
			service.save(user);
		}
		
//		//thông báo lưu thành công (để hiển thị tại trang danh sách "user")
		redirectAttributes.addFlashAttribute("message", "The user has been save successfully.");
//		
		
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
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id")Integer id //lấy id từ trang html
			,RedirectAttributes redirectAttributes //thông báo cho trang html
			,Model model 
			) {
		try {
			
			//xóa user
			service.delete(id);
			//thông báo 
			redirectAttributes.addFlashAttribute("message", "The user ID "+id+" has been deleted successfully");
			
		} catch (UserNotFoundException e) {
			//dùng để hiện thông báo trên trang html
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			//không tìm thấy trả về trang users 
		}
		return "redirect:/users";
	}
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable("id")Integer id,@PathVariable("status") boolean enable,RedirectAttributes redirectAttributes  ) {
		service.updateUserEnabledStatus(id, enable);
		String status = enable?"enabled":"disable";
		String message = "The user ID "+ id+" has been "+status;
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/users";
		
	}
}



