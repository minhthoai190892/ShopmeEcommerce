package com.shopme.admin.user.controller;

import java.io.IOException;

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
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.user.UserNotFoundException;
import com.shopme.admin.user.UserService;
import com.shopme.admin.user.export.UserCsvExporter;
import com.shopme.admin.user.export.UserExcelExporter;
import com.shopme.admin.user.export.UserPDFExporter;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;



@Controller
public class UserController {

//	khai báo Service
	@Autowired
	private UserService service;
	/**
	 * PagingAndSortingHelper cần đăng ký ở MvcConfig.java
	 * @param model cần quyền truy cập vào model
	 * @return về trang html
	 * 
	 */
	@GetMapping("/users")
	//cần quyền truy cập Model
	public String listFirstPage() {
//		//tạo một danh sách "users"
//		List<User> listUsers = service.listAll();
//		//thêm vào model
//		model.addAttribute("listUsers", listUsers);
//		return "users";
//		return listByPage(1, model,"id","asc", null);
//		return listByPage(helper,1, model,"id","asc", null);
		return "redirect:/users/page/1?sortField=firstName&sortDir=asc";
	}
	/**
	 * listUsers là dùng cho trang html, users là đường dẫn 
	 */
	@GetMapping("/users/page/{pageNum}")
	public String listByPage(@PagingAndSortingParam(listName = "listUsers",moduleURL = "/users") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum")int pageNum //@PathVariable thì được dùng để trích xuất dữ liệu từ URL path
			
			) {
		service.listByPage(pageNum,helper);
		return "users/users";
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
		return "/users/user_form";
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
		return getRedirectURLtoAffectedUser(user);
	}
	
	
	private String getRedirectURLtoAffectedUser(User user) {
		//cắt lấy phần đầu của email
		String firstPartOfEmail = user.getEmail().split("@")[0];
		
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword="+firstPartOfEmail;
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
			return "users/user_form";
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
	/**
	 * Hàm xuất file CSV
	 * @param response nhận một response
	 * @throws IOException
	 */
	@GetMapping("/users/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException{
		//lấy danh sách user
		List<User> lisUsers = service.listAll();
		//khởi tạo đối tượng Export
		UserCsvExporter exporter = new UserCsvExporter();
		//gọi hàm export
		exporter.exprot(lisUsers, response);
	}
	/**
	 * Hàm xuất file excel
	 * @param response nhận một response
	 * @throws IOException
	 */
	@GetMapping("/users/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException{
		//lấy danh sách user
		List<User> lisUsers = service.listAll();
		//khởi tạo đối tượng Export
		UserExcelExporter exporter = new UserExcelExporter();
		//gọi hàm export
		exporter.exprot(lisUsers, response);
	}
	@GetMapping("/users/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException{
		//lấy danh sách user
		List<User> lisUsers = service.listAll();
		//khởi tạo đối tượng Export
		UserPDFExporter exporter = new UserPDFExporter();
		//gọi hàm export
		exporter.exprot(lisUsers, response);
	}
}



