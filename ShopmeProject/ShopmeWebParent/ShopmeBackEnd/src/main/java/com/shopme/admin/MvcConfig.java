package com.shopme.admin;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String dirName = "user-photos";
		Path userPhotoDir = Paths.get(dirName);// lấy thư mục hình ảnh chung của tất cả người dùng
		// lấy file hình của người dùng
		String userPhotosPath = userPhotoDir.toFile().getAbsolutePath();
		// gọi hàm đăng ký
		registry.addResourceHandler("/" + dirName + "/**")// đăng ký cho tất cả các thư mục có trong "dirName"
				.addResourceLocations("file:/" + userPhotosPath + "/");// ánh xạ đến file hệ thống

		String categoryimagesDirName = "../category-images";
		Path categoryImagesDir = Paths.get(categoryimagesDirName);// lấy thư mục hình ảnh chung của tất cả người dùng
		// lấy file hình của người dùng
		String categoryImagesPath = categoryImagesDir.toFile().getAbsolutePath();
		// gọi hàm đăng ký
		registry.addResourceHandler("/category-images/**")// đăng ký cho tất cả các thư mục có trong "dirName"
				.addResourceLocations("file:/" + categoryImagesPath + "/");// ánh xạ đến file hệ thống

		String brandLogosDirName = "../brand-logos";
		Path brandLogosDir = Paths.get(brandLogosDirName);// lấy thư mục hình ảnh chung của tất cả người dùng
		// lấy file hình của người dùng
		String brandLogosPath = brandLogosDir.toFile().getAbsolutePath();
		// gọi hàm đăng ký
		registry.addResourceHandler("/brand-logos/**")// đăng ký cho tất cả các thư mục có trong "dirName"
				.addResourceLocations("file:/" + brandLogosPath + "/");// ánh xạ đến file hệ thống

	}

}
