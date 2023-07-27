package com.shopme.admin.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductImage;

@Controller
public class ProductController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
	@Autowired
	private ProductService productService;
	@Autowired
	private BrandService brandService;
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/products")
	public String listFirstPage(Model model) {
		return listByPage(1, "asc", model, "name", null, 0);
	}

	@GetMapping("/products/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, @Param("sortDir") String sortDir, Model model,
			@Param("sortField") String sortField, @Param("keyword") String keyword,
			@Param("categoryId") Integer categoryId) {
		System.err.println("categoryId: " + categoryId);
		Page<Product> page = productService.listByPage(pageNum, sortField, sortDir, keyword, categoryId);
		List<Product> listProducts = page.getContent();
//		lấy danh sách dropdown trên form
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		if (categoryId != null) {
			//hiển thị khi chọn category mà không bị xóa ở dropdown
			model.addAttribute("categoryId", categoryId);
		}
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("listCategories", listCategories);

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
			@RequestParam(value="fileImage", required = false) MultipartFile mainImageMultipartFile,
			@RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultipartFiles,
			@RequestParam(name = "detailIDS", required = false) String[] detailIDS,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailValues", required = false) String[] detailValues,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,
			@RequestParam(name = "imageNames", required = false) String[] imageNames,
			@AuthenticationPrincipal ShopmeUserDetails loggedUser

	) throws IOException {
		if (loggedUser.hasRole("Selesperson")) {
			productService.saveProductPrice(product);
			redirectAttributes.addFlashAttribute("message", "The product has been saved successfully.");
			return "redirect:/products";
		}
		// gọi hàm
		setMainImageName(mainImageMultipartFile, product);
		setExistingExtraImageNames(imageIDs, imageNames, product);
		setNewExtraImageNames(extraImageMultipartFiles, product);
		setProductDetails(detailIDS, detailNames, detailValues, product);
		// save product
		Product saveProduct = productService.save(product);
		saveUploadedImages(mainImageMultipartFile, extraImageMultipartFiles, saveProduct);

//		productService.save(product);
		deleteExtraImagesWereRemovedOnForm(product);
		redirectAttributes.addFlashAttribute("message", "The product has been saved successfully.");

		System.err.println("Product : " + product);
		return "redirect:/products";
	}

	private void deleteExtraImagesWereRemovedOnForm(Product product) {
		String extraImageDir = "../product-images/" + product.getId() + "/extras";
		System.err.println("extraImageDir" + extraImageDir);
		Path dirPath = Paths.get(extraImageDir);
		System.err.println("dirPath" + dirPath);
		try {
			Files.list(dirPath).forEach(file -> {
				String fileName = file.toFile().getName();
				System.err.println("fileName" + fileName);
				if (!product.containsImageName(fileName)) {
					try {
						Files.delete(file);
						LOGGER.info("Delete Extra image: " + fileName);
					} catch (IOException e) {
						// TODO: handle exception
						LOGGER.error("Could not delete extra image: " + fileName);
					}
				}
			});
		} catch (IOException e) {
			LOGGER.error("Could not not list directory: " + dirPath);
		}

	}

	private void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {
		// TODO Auto-generated method stub
		if (imageIDs == null || imageIDs.length == 0) {
			return;
		}
		Set<ProductImage> images = new HashSet<>();
		for (int count = 0; count < imageIDs.length; count++) {
			Integer id = Integer.parseInt(imageIDs[count]);
			String name = imageNames[count];
			images.add(new ProductImage(id, name, product));
		}
		product.setImages(images);
	}

	private void setProductDetails(String[] detailIDS, String[] detailNames, String[] detailValues, Product product) {
		// TODO Auto-generated method stub
		if (detailNames == null || detailNames.length == 0) {
			return;
		}
		for (int count = 0; count < detailNames.length; count++) {
			String name = detailNames[count];
			String value = detailValues[count];
			Integer id = Integer.parseInt(detailIDS[count]);
			if (id != 0) {
				product.addDetail(id, name, value);
			} else if (!name.isEmpty() && !value.isEmpty()) {
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

	private void setNewExtraImageNames(MultipartFile[] extraImageMultipartFiles, Product product) {
		// TODO Auto-generated method stub
		if (extraImageMultipartFiles.length > 0) {
			for (MultipartFile multipartFile : extraImageMultipartFiles) {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					if (!product.containsImageName(fileName)) {
						// thêm extra image vào product
						product.addExtraImage(fileName);
					}

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

			String productExtraImageDir = "../product-images/" + id + "/extras";
			FileUploadUtil.removeDir(productExtraImageDir);
			String productDir = "../product-images/" + id;
			// gọi hàm xóa thư mục chứa file ảnh
			FileUploadUtil.removeDir(productDir);
			redirectAttributes.addFlashAttribute("message", "The product ID " + id + " has been deleted successfully");
		} catch (ProductNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			// TODO: handle exception
		}
		return "redirect:/products";
	}

	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Product product = productService.get(id);
			List<Brand> listBrands = brandService.listAll();
			Integer numberOfExistingExtraImage = product.getImages().size();

			model.addAttribute("listBrands", listBrands);
			model.addAttribute("product", product);
			model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
			model.addAttribute("numberOfExistingExtraImage", numberOfExistingExtraImage);
			return "products/product_form";
		} catch (ProductNotFoundException e) {
			// TODO: handle exception
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
		}
	}

	@GetMapping("/products/detail/{id}")
	public String viewProductDetails(@PathVariable("id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Product product = productService.get(id);

			model.addAttribute("product", product);

			return "products/product_detail_modal";
		} catch (ProductNotFoundException e) {
			// TODO: handle exception
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
		}
	}

}
