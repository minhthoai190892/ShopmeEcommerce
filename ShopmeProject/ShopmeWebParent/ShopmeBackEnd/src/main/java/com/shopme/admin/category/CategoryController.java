package com.shopme.admin.category;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.shopme.admin.user.UserService;
import com.shopme.admin.user.export.CategoryCsvExpoter;
import com.shopme.common.entity.Category;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	/**
	 * Hàm hiển thị tất cả thông tin của Category
	 * 
	 * @param model   cần đối tượng model
	 * @param sortDir để phân biệt cách sort (asc hoặc desc)
	 * @return trả về trang html
	 */
	@GetMapping("/categories")
	public String listFirstPage(Model model, @Param("sortDir") String sortDir) {

//		return listByPage(model, sortDir, 1);
		return listByPage(model, sortDir, null, 1);
	}

	@GetMapping("/categories/page/{pageNum}")
	public String listByPage(Model model, @Param("sortDir") String sortDir,@Param("keyword") String keyword,
			@PathVariable(name = "pageNum") int pageNum) {

		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		CategoryPageInfo pageInfo= new CategoryPageInfo();
		// hiển thị tất cả thông tin và sắp xếp dữ liệu
		List<Category> listCategories = categoryService.listByPage(sortDir, pageNum, pageInfo, keyword);
	
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		long startCount = (pageNum-1)*CategoryService.ROOT_CATEGORIES_PER_PAGE+1;
		long endCount = startCount+CategoryService.ROOT_CATEGORIES_PER_PAGE-1;
		if (endCount>pageInfo.getTotalElements()) {
			endCount=pageInfo.getTotalElements();
		}
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("totalPages", pageInfo.getTotalPages());
		model.addAttribute("totalItems", pageInfo.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", "name");
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("moduleURL","/categories");
		return "categories/categories";

	}

	/**
	 * Hàm cập nhật category status
	 * 
	 * @param id                 lấy id trên URL
	 * @param enabled            lấy enable trên URL
	 * @param redirectAttributes thông báo lỗi
	 * @return sau khi update xong sẽ trả về trang hml
	 */
	@GetMapping("/categories/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes) {
		categoryService.updateCategoryEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disable";
		String message = "The category id " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/categories";

	}

	/**
	 * Hàm tạo mới một category
	 * 
	 * @param model đối tượng model
	 * @return gọi đến trang form html
	 */
	@GetMapping("/categories/new")
	public String newCategory(Model model) {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		Category category = new Category();
		category.setEnabled(true);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("category", category);
		model.addAttribute("pageTitle", "Create Category");

		return "categories/category_form";
	}

	/**
	 * Hàm lưu một category
	 * 
	 * @param category           nhận một đối tượng category từ form html
	 * @param multipartFile      nhận file hình ảnh từ form
	 * @param redirectAttributes thông báo lỗi cho trang html
	 * @return sau khi lưu lại sẽ trả về trang html
	 * @throws IOException thông báo lỗi hệ thống
	 */
	@PostMapping("/categories/save")
	public String saveCategory(Category category, @RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes redirectAttributes) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			category.setImage(fileName);

			Category saveCategory = categoryService.save(category);
			// vị trí lưu file và tên file
			String uploadDir = "../category-images/" + saveCategory.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			categoryService.save(category);
		}
		redirectAttributes.addFlashAttribute("message", "The category has been saved successfully");

		return "redirect:/categories";
	}

	/**
	 * Hàm cập nhật category
	 * 
	 * @param id                 nhận một id
	 * @param model
	 * @param redirectAttributes thông báo lỗi cho trang html
	 * @return sau khi cập nhật sẽ trả về trang html
	 * @throws CategoryNotFoundException thông báo lỗi hệ thống
	 */
	@GetMapping("/categories/edit/{id}")
	public String editCategory(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) throws CategoryNotFoundException {

		try {
			Category category = categoryService.get(id);
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("category", category);
			model.addAttribute("pageTitle", "Edit Category (ID: " + id + ")");
			return "categories/category_form";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/categories";
		}
	}

	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			// xóa category bằng id
			categoryService.delete(id);
			// lấy đường dẫn file chứa hình ảnh
			String categoryDir = "../category-images/" + id;
			// gọi hàm xóa thư mục chứa file ảnh
			FileUploadUtil.removeDir(categoryDir);
			redirectAttributes.addFlashAttribute("message", "the category ID " + id + " has been deleted successfully");
		} catch (CategoryNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/categories";
	}
	
	
	@GetMapping("/categories/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<Category>listCategories = categoryService.listCategoriesUsedInForm();
		CategoryCsvExpoter expoter = new CategoryCsvExpoter();
		expoter.exprot(listCategories, response);
	}
	
}
