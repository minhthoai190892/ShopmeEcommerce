package com.shopme.admin.setting;

import java.util.List;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingBag;

public class GeneralSettingBag extends SettingBag {
	/**
	 * mảng mới Setting Bag
	 * */
	public GeneralSettingBag(List<Setting> listSettings) {
		super(listSettings);
	}
	/**
	 * Hàm update Currency Symbol
	 * @param value giá trị từ người dùng
	 * */
	public void updateCurrencySymbol(String value) {
	
		super.update("CURRENCY_SYMBOL", value);
	}
	/**
	 * Hàm update Site Logo
	 * @param value giá trị từ người dùng
	 * */
	public void updateSiteLog(String value) {
		super.update("SITE_LOGO", value);
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
}
