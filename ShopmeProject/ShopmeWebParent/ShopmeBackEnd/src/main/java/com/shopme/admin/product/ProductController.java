package com.shopme.admin.product;

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
import com.shopme.admin.brand.BrandService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;

@Controller
public class ProductController {
	@Autowired
	private ProductService productService;
	@Autowired 
	private BrandService brandService;
	@GetMapping("/products")
	public String listAll(Model model) {
		List<Product> listProducts = productService.listAll();
		model.addAttribute("listProducts", listProducts);
		return "products/products";
	}
	/** 
	 * Hàm tạo mới một product
	 * @param model
	 * @return mở trang html và nhận thông tin từ người dùng
	 */
	@GetMapping("/products/new")
	public String newProduct(Model model) {
		//danh sách brand
		List<Brand> listBrands = brandService.listAll();
		Product product = new Product();
		product.setInStock(true);
		product.setEnabled(true);
		
		model.addAttribute("product",product);
		model.addAttribute("listBrands",listBrands);
		model.addAttribute("pageTitle","Create new Product");
		return "products/product_form";
	}
	/**
	 * Hàm lưu thông tin product 
	 * @param model đối tượng model
	 * @param product product từ trang html
	 * @param redirectAttributes thông báo kết quả
	 * @param multipartFile hình ảnh 
	 * @return trả về một product mới và mở trang html
	 * @throws IOException
	 */
	@PostMapping("/products/save")
	public String saveProduct(Model model,Product product,RedirectAttributes redirectAttributes,@RequestParam("fileImage")MultipartFile multipartFile) throws IOException {
		
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			product.setMainImage(fileName);
			Product saveProduct = productService.save(product);
			String uploadDir = "../product-images/"+saveProduct.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			productService.save(product);
		}
		
	
		redirectAttributes.addFlashAttribute("message","The product has been saved successfully.");
		System.err.println("Product Name: "+product.getName());
		System.err.println("Alias: "+product.getAlias());
		System.err.println("Brand Id: "+product.getBrand().getId());
		System.err.println("Category Id: "+product.getCategory().getId());
		System.err.println("Cost: "+product.getCost());
		System.err.println("price: "+product.getPrice());
		System.err.println("discount: "+product.getDiscountPercent());
		System.err.println("short desciption: "+product.getShortDescription());
		System.err.println("full desciption: "+product.getFullDescription());
		System.err.println("length: "+product.getLength());
		System.err.println("width : "+product.getWidth());
		System.err.println("height : "+product.getHeight());
		System.err.println("weight : "+product.getWeight());
		return "redirect:/products";
	}
	
	/**
	 * Hàm cập nhật status
	 * @param id
	 * @param enabled
	 * @param redirectAttributes
	 * @return
	 */
	@GetMapping("/products/{id}/enabled/{status}")
	public String updateProductEnabledStatus(@PathVariable( "id") Integer id, @PathVariable("status")boolean enabled,RedirectAttributes redirectAttributes) {
		productService.updateProductEnabledStatus(id, enabled);
		String status = enabled?"enabled":"disabled";
		String message = "The Product ID "+id+" has been "+status;
		redirectAttributes.addFlashAttribute("message",message);
		return "redirect:/products";
	}
	/**
	 * Hàm xóa status
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id")Integer id,RedirectAttributes redirectAttributes) {
		try {
			productService.delete(id);
			redirectAttributes.addFlashAttribute("message","The product ID "+id+" has been deleted successfully");
		} catch (ProductNotFoundException e) {
			redirectAttributes.addFlashAttribute("message",e.getMessage());
		
		}
		return "redirect:/products";
	}

}
