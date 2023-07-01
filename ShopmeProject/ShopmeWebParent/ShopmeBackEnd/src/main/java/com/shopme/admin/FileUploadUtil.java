package com.shopme.admin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
	private static final Logger LOGGER=LoggerFactory.getLogger(FileUploadUtil.class);
	/**
	 * Hàm tạo thư mục và lưu tệp hình ảnh
	 * @param uploadDir  tạo tên thư mục
	 * @param fileName nhận tên file(hình ảnh)
	 * @param multipartFile  là một class đại diện cho các file được upload lên từ multipart request từ html
	 * @throws IOException
	 */
	public static void saveFile(String uploadDir
			,String fileName
			,MultipartFile multipartFile
			) throws IOException {
		Path uploadPath = Paths.get(uploadDir);//nhận đường dẫn thư mục
		//kiểm tra Dir đã tồn tại chưa
		if (!Files.exists(uploadPath)) {//=> dir chưa tồn tại
			Files.createDirectories(uploadPath);//lưu tệp trong hệ thống
		}
		try(InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName);
//			StandardCopyOption.REPLACE_EXISTING: di chuyển file và ghi đè 
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			
		} catch (IOException e) {
			throw new IOException("Could not save file: "+fileName);
		}
	}
	
	public static void cleanDir(String dir) {
		Path dirPath = Paths.get(dir);//nhận đường dẫn thư mục
		try {
			Files.list(dirPath).forEach(file->{
				if (!Files.isDirectory(file)) {//kiểm tra xem một tập tin có phải là thư mục hay không
					try {
						Files.delete(file);
					} catch (Exception e) {
						LOGGER.error("Could not delete file: "+file);
//						System.out.println("Could not delete file: "+file);
					}
				}
			});
		} catch (IOException e) {
			LOGGER.error("Could not list directory: "+dirPath);
//			System.out.println("Could not list directory: "+dirPath);
		}
	}

	public static void removeDir(String dir) {
		// TODO Auto-generated method stub
		cleanDir(dir);
		try {
			Files.delete(Paths.get(dir));
		} catch (Exception e) {
			LOGGER.error("Could not remove directory: "+dir);
		}
	}
	
}
