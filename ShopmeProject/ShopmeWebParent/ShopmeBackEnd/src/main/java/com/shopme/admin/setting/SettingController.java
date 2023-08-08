package com.shopme.admin.setting;

import java.io.IOException;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.currency.CurrencyRepository;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.Setting;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SettingController implements saveSiteLogo {
	@Autowired
	private SettingService settingService;
	@Autowired
	private CurrencyRepository currencyRepository;

	@GetMapping("/settings")
	public String listAll(Model model) {
		List<Setting> listSettings = settingService.listAllSettings();
		List<Currency> listCurrencies = currencyRepository.findAllByOrderByNameAsc();
		model.addAttribute("listCurrencies", listCurrencies);
		model.addAttribute("pageTitle", "Settings - Shopme Admin");
		for (Setting setting : listSettings) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}
		return "settings/settings";
	}

	@PostMapping("/settings/save_general")
	public String saveGeneralSettings(@RequestParam("fileImage") MultipartFile multipartFile,
			HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException {
		System.err.println("@PostMapping(\"/settings/save_general\") ");
		GeneralSettingBag settingBag = settingService.getGeneralSettingBag();
		System.err.println("settingBag >>>>>>>>>> "+settingBag);
		saveSiteLogo(multipartFile, settingBag);
		saveCurrencySymbol(request, settingBag);

		updateSettingValuesFromForm(request, settingBag.list());
		redirectAttributes.addFlashAttribute("message", "General settings have been saved");
		return "redirect:/settings";

	}
	/**
	 * Hàm lưu thông tin hình ảnh
	 * @param multipartFile tên hình ảnh từ form
	 * @param settingBag 
	 * */
	private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String value = "/site-logo/" + fileName;
			settingBag.updateSiteLog(value);
			String uploadDir = "../site-logo/";
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}
	}
	/**
	 * Hàm save Currency Symbol
	 * @param settingBag nhận một đối tượng
	 * */
	private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag) {
		//lấy id với CURRENCY_ID được người dùng chọn
		Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
		Optional<Currency> findByIdResult = currencyRepository.findById(currencyId);
		System.err.println("findByIdResult >>>>>>>>> "+findByIdResult);
		//kiểm tra xem id có giá trị không 
		if (findByIdResult.isPresent()) {
			//tạo đối tượng với kết quả 
			Currency currency = findByIdResult.get();
			System.err.println("currency>>>>>>> "+currency);
			System.err.println("currency.getSymbol() "+currency.getSymbol());
			String value = currency.getSymbol();
			settingBag.updateCurrencySymbol(value);
			
			
		}
	}
	/**
	 * Hàm  update Setting Values From Form
	 * @param listSettings nhận một danh sách
	 * */
	private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSettings) {
		for (Setting setting : listSettings) {
			String value = request.getParameter(setting.getKey());
			if (value != null) {
				setting.setValue(value);
			}
		}
		settingService.saveAll(listSettings);
	}
	
	@PostMapping("/settings/save_mail_server")
	public String saveMailServerSettings(HttpServletRequest request,RedirectAttributes redirectAttributes) {
		List<Setting> mailServerSettings = settingService.getEmailServerSettings();
		updateSettingValuesFromForm(request, mailServerSettings);
		redirectAttributes.addFlashAttribute("message","Mail server settings have been saved");
		return "redirect:/settings";
	}
	@PostMapping("/settings/save_mail_templates")
	public String saveMailTemplateSettings(HttpServletRequest request,RedirectAttributes redirectAttributes) {
		List<Setting> mailTemplateSettings = settingService.getEmailTemplateSettings();
		updateSettingValuesFromForm(request, mailTemplateSettings);
		redirectAttributes.addFlashAttribute("message","Mail template settings have been saved");
		return "redirect:/settings";
	}
}
