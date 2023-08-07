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

	public List<Setting> getGeneralSettingBag() {
		System.err.println("getGeneralSettingBag>>>>>>>>> ");
		List<Setting> settings = settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
		System.out.println("settings>>>> "+settings);
		return settings;
	}

}
