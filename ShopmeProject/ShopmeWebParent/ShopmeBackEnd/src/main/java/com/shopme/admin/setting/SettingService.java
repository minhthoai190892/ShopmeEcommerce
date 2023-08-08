package com.shopme.admin.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;

@Service
public class SettingService {
	@Autowired
	private SettingRepository settingRepository;
	/**
	 * Hàm lấy dánh sách Setting
	 * */
	public List<Setting> listAllSettings() {
		return settingRepository.findAll();
	}
	/**
	 * Hàm tạo mới danh sách Setting
	 * @return trả về một danh sách mới
	 * */
	public GeneralSettingBag getGeneralSettingBag() {
		List<Setting> settings = new ArrayList<>();
		//lấy danh sách settings với category GENERAL
		List<Setting> generalSettings = settingRepository.findByCategory(SettingCategory.GENERAL);
		//lấy danh sách settings với category CURRENCY
		List<Setting> currencySettings = settingRepository.findByCategory(SettingCategory.CURRENCY);
		//thêm mảng
		settings.addAll(generalSettings);
		settings.addAll(currencySettings);
		//trả về một mảng mới
		return new GeneralSettingBag(settings);
	}
	/**
	 * Hàm lưu tất cả thông tin
	 * */
	public void saveAll(Iterable<Setting> settings) {
		settingRepository.saveAll(settings);
	}
	public List<Setting> getEmailServerSettings() {
		return settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
	}
	public List<Setting> getEmailTemplateSettings() {
		return settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES);
	}
}
