package com.shopme.admin.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Category;

@Controller
public class CategoryController {
	@Autowired
	private CategoryService categoryService;
	@GetMapping("/categories")
	public String listAll(Model model) {
		List<Category> categories = categoryService.listAll();
		model.addAttribute("categories", categories);
		return "categories/categories";
	}
	/**
	 * @param id
	 * @param enabled
	 * @param redirectAttributes
	 * @return
	 */
	@GetMapping("/categories/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(@PathVariable("id")Integer id, @PathVariable("status")boolean enabled,RedirectAttributes redirectAttributes) {
		categoryService.updateCategoryEnabledStatus(id, enabled);
		String status = enabled?"enabled":"disable";
		String message = "The category id "+id+" has been "+status;
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/categories";
		
	}	
}
