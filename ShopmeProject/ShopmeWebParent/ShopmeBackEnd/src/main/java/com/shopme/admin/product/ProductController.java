package com.shopme.admin.product;

import java.io.IOException;
import java.util.Iterator;
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
	 * 
	 * @param model
	 * @return mở trang form html
	 */
	@GetMapping("/products/new")
	public String newProduct(Model model) {
		// lấy danh sách brand
		List<Brand> listBrands = brandService.listAll();
		Product product = new Product();
		// mặc định là true khi mở trang form html
		product.setInStock(true);
		product.setEnabled(true);

		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTitle", "Create new Product");
		return "products/product_form";
	}

	@PostMapping("/products/save")
	public String saveProduct(Model model, Product product, RedirectAttributes redirectAttributes,
			@RequestParam("fileImage") MultipartFile mainImageMultipartFile,
			@RequestParam(name = "extraImage") MultipartFile[] extraImageMultipartFiles,
			@RequestParam(name = "detailNames",required = false) String[] detailNames,
			@RequestParam(name = "detailValues",required = false) String[] detailValues
			
			) throws IOException {
		// gọi hàm
		setMainImageName(mainImageMultipartFile, product);
		setExtraImageNames(extraImageMultipartFiles, product);
		setProductDetails(detailNames,detailValues,product);
		// save product
		Product saveProduct = productService.save(product);
		saveUploadedImages(mainImageMultipartFile, extraImageMultipartFiles, saveProduct);

		productService.save(product);
		redirectAttributes.addFlashAttribute("message", "The product has been saved successfully.");

		System.err.println("Product : " + product);
		return "redirect:/products";
	}

	private void setProductDetails(String[] detailNames, String[] detailValues, Product product) {
		// TODO Auto-generated method stub
		if (detailNames==null||detailNames.length==0) {
			return;
		}
		for (int count = 0; count < detailNames.length; count++) {
			String name = detailNames[count];
			String value = detailValues[count];
			if (!name.isEmpty()&&!value.isEmpty()) {
				product.addDetail(name, value);
			}
		}
	}

	/**
	 * 
	 * @param mainImageMultipartFile
	 * @param extraImageMultipartFiles
	 * @param saveProduct
	 * @throws IOException
	 */
	private void saveUploadedImages(MultipartFile mainImageMultipartFile, MultipartFile[] extraImageMultipartFiles,
			Product saveProduct) throws IOException {
		if (!mainImageMultipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipartFile.getOriginalFilename());
			String uploadDir = "../product-images/" + saveProduct.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipartFile);
		}
		if (extraImageMultipartFiles.length > 0) {
			String uploadDir = "../product-images/" + saveProduct.getId() + "/extras";
			for (MultipartFile multipartFile : extraImageMultipartFiles) {
				if (multipartFile.isEmpty()) {
					continue;
				}
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			}
		}

	}
	
	private void setExtraImageNames(MultipartFile[] extraImageMultipartFiles, Product product) {
		// TODO Auto-generated method stub
		if (extraImageMultipartFiles.length > 0) {
			for (MultipartFile multipartFile : extraImageMultipartFiles) {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					// thêm extra image vào product
					product.addExtraImage(fileName);
				}
			}
		}

	}

	/**
	 * 
	 * @param mainImageMultipartFile
	 * @param product
	 */
	private void setMainImageName(MultipartFile mainImageMultipartFile, Product product) {
		// TODO Auto-generated method stub
		if (!mainImageMultipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipartFile.getOriginalFilename());
			product.setMainImage(fileName);
		}

	}

	@GetMapping("/products/{id}/enabled/{status}")
	public String updateProductEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes) {
		productService.updateProductEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Product ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/products";
	}

	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			productService.delete(id);
			String productImageDir = "../product-images/" + id;
			String productExtraImageDir = "../product-images/" + id + "/extras";
			FileUploadUtil.removeDir(productImageDir);
			FileUploadUtil.removeDir(productExtraImageDir);
			redirectAttributes.addFlashAttribute("message", "The product ID " + id + " has been deleted successfully");
		} catch (ProductNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			// TODO: handle exception
		}
		return "redirect:/products";
	}

}








