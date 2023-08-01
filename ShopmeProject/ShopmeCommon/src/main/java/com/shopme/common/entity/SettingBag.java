package com.shopme.common.entity;

import java.util.List;

public class SettingBag {

	private List<Setting> listSettings;

	public SettingBag(List<Setting> listSettings) {
		super();
		this.listSettings = listSettings;
	}

	public SettingBag() {
		super();
	}

	/**
	 * hàm tìm kiếm key
	 * 
	 * @param key key cần tìm
	 * @return trả về một đối tượng với key cần tìm hoặc null nếu không có
	 */
	public Setting get(String key) {
		// lấy vị trí trong mảng
		int index = listSettings.indexOf(new Setting(key));
		if (index >= 0) {
			return listSettings.get(index);
		}
		return null;
	}

	/**
	 * hàm lấy value
	 * 
	 * @param key key cần tìm
	 * @return trả về giá trị value hoặc null nếu không có
	 */
	public String getValue(String key) {
		
		Setting setting = get(key);
		if (setting != null) {
			return setting.getValue();
		}
		return null;
	}
	/**
	 * hàm update bằng key và value
	 * @param key nhận key trong sql
	 * @param value nhận value từ người dùng
	 * */
	public void update(String key, String value) {
		System.err.println("Setting bag");
		//lấy đối tượng với key và value
		Setting setting = get(key);
		System.err.println("get(key)"+get(key));
		
		if (setting != null && value != null) {
			setting.setValue(value);
			System.err.println("setting.getValue()"+ setting.getValue());
		}else {
			System.err.println("error");
		}
	}

	public List<Setting> list() {
		return listSettings;
	}

	@Override
	public String toString() {
		return "SettingBag [listSettings=" + listSettings + "]";
	}
	
	
}
