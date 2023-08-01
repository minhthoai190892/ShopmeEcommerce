package com.shopme.admin.setting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;



public interface SettingRepository extends JpaRepository<Setting, String>{
	/**
	 * lấy danh sach setting bằng category
	 * @param category đối tượng category
	 * */
	public List<Setting> findByCategory(SettingCategory category);
}
