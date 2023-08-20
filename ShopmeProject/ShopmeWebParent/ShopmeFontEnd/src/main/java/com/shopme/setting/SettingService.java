package com.shopme.setting;

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
	 * Hàm lấy danh sách thuộc tính bằng SettingCategory.GENERAL và CURRENCY
	 * @return trả về danh sách SettingCategory.GENERAL và CURRENCY
	 * */
	public List<Setting> getGeneralSettingBag() {
	
		List<Setting> settings = settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
		System.out.println("settings>>>> "+settings);
		return settings;
	}
	/**
	 * Hàm lấy thông tin Email vơi MAIL_SERVER và MAIL_TEMPLATES
	 * @return trả về một danh sách mới với cách thuộc tính gồm MAIL_SERVER và MAIL_TEMPLATES
	 * */
	public EmailSettingBag	 getEmailSettings() {
		//lấy danh sách bằng MAIL_SERVER
		List<Setting> settings = settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
		//thêm câc danh sách của MAIL_TEMPLATES
		settings.addAll(settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES));
		//tạo mới và cấp phát vùng nhớ
		return new EmailSettingBag(settings);
	}

}
