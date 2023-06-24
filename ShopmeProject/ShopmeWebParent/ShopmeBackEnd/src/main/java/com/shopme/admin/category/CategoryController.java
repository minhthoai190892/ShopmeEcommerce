package com.shopme.admin.category;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.shopme.common.entity.Category;

@Controller
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/categories")
	public String listAll(Model model) {
		List<Category> listCategories = categoryService.listAll();
		model.addAttribute("listCategories", listCategories);
		return "categories/categories";
	}

	/**
	 * @param id
	 * @param enabled
	 * @param redirectAttributes
	 * @return
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

	@GetMapping("/categories/new")
	public String newCategory(Model model) {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("category", new Category());
		model.addAttribute("pageTitle", "Create Category");

		return "categories/category_form";
	}

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
}
