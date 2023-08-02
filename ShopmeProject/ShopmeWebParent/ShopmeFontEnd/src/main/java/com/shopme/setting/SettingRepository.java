package com.shopme.setting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;



public interface SettingRepository extends JpaRepository<Setting, String>{
	/**
	 * lấy danh sach setting bằng category
	 * @param category đối tượng category
	 * */
	public List<Setting> findByCategory(SettingCategory category);
	/**
	 * hàm tìm kiếm bằng 2 categories
	 * @param categoryOne 
	 * @param categoryTwo
	 * */
	@Query("select s from Setting s where s.category=?1 or s.category=?2 ")
	public List<Setting> findByTwoCategories(SettingCategory categoryOne,SettingCategory categoryTwo) ;
}
