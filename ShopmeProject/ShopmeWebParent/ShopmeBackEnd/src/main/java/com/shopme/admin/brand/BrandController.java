package com.shopme.admin.brand;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
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
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class BrandController {
	@Autowired
	private BrandService brandService;
	@Autowired
	private CategoryService categoryService;

	/**
	 * Hàm hiển thị danh sách Brands
	 * @param model 
	 * @return Hàm trả về trand đầu tiên
	 */
	@GetMapping("/brands")
	public String listBrands() {
//		List<Brand> listBrands = brandService.listAll();
//		model.addAttribute("listBrands",listBrands);
//		return  "brands/brands";
		return "redirect:/brands/page/1?sortField=name&sortDir=asc";
	}

	/**
	 * Hàm hiển thị số lượng danh sách trong một trang
	 * @param pageNum số trang
	 * @param model
	 * @param sortField sort theo field
	 * @param sortDir sort theo asc hoặc desc
	 * @param keyword giá trị tìm kiếm
	 * @return trả về trang html
	 */
	@GetMapping("/brands/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listBrands",moduleURL = "/brands") PagingAndSortingHelper helper
			,@PathVariable(name = "pageNum") int pageNum
			) {
		 brandService.listByPage(pageNum, helper);
		
		return "brands/brands";

	}

	/**
	 * Hàm tạo mới một Brand
	 * @param model
	 * @return mở trang brand_form.html
	 */
	@GetMapping("/brands/new")
	public String newBrand(Model model) {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("brand", new Brand());
		model.addAttribute("pageTitle", "Create New Brand");
		return "brands/brand_form";

	}

	/**
	 * Hàm save thông tin brand từ trang brand_form.html
	 * @param brand nhận vào đối tượng brand từ brand_form.html
	 * @param multipartFile hình ảnh từ brand_form.html
	 * @param redirectAttributes thông báo 
	 * @return save thành công sẽ trả về đối tượng vừa tạo
	 * @throws IOException
	 */
	@PostMapping("/brands/save")
	public String saveBrand(Brand brand, @RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes redirectAttributes) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);

			Brand saveBrand = brandService.save(brand);
			String uploadDir = "../brand-logos/" + saveBrand.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			brandService.save(brand);
		}
		redirectAttributes.addFlashAttribute("message", "The brand has been saved successfully");
		
//		return "redirect:/brands";
		return getRedirectURLtoAffectedBrand(brand);
	}
	/**
	 * hàm hàm trả về đường dẫn khi tạo, cập nhật, và tiềm kiếm brand
	 * @param brand
	 * @return
	 */
	private String getRedirectURLtoAffectedBrand(Brand brand) {
		//cắt lấy phần đầu của email
//		String firstPartOfEmail = user.getEmail().split("@")[0];
		String searchBrand = brand.getName();
		return "redirect:/brands/page/1?sortField=id&sortDir=asc&keyword="+searchBrand;
	}
	@GetMapping("/brands/edit/{id}")
	public String editBrand(Model model, RedirectAttributes redirectAttributes, @PathVariable(name = "id") Integer id) {
		try {
			Brand brand = brandService.get(id);
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("brand", brand);
			model.addAttribute("pageTitle", "Edit Brand (ID: " + id + " )");
			return "brands/brand_form";
		} catch (BrandNotFoundException e) {
			// TODO: handle exception
			redirectAttributes.addFlashAttribute("message", e.getMessage());
//			redirectAttributes.addFlashAttribute("message","The brand has been edit successfully");
			return "redirect:/brands";
		}
	}

	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			brandService.delete(id);
			String brandDir = "../brand-logos/" + id;
			FileUploadUtil.removeDir(brandDir);
			redirectAttributes.addFlashAttribute("message", "The brand ID " + id + " has been deleted successfully");
		} catch (BrandNotFoundException e) {
			// TODO: handle exception
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/brands";
	}
	@GetMapping("/brands/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<Brand> listBrands = brandService.listAll();
		BrandCsvExport brandCsvExport = new BrandCsvExport();
		brandCsvExport.export(listBrands, response);
	}
}
