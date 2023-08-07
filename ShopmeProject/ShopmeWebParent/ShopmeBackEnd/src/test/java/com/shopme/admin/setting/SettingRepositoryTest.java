package com.shopme.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingBag;
import com.shopme.common.entity.SettingCategory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SettingRepositoryTest {
	@Autowired
	private SettingRepository settingRepository;
	@Test
	public void testCreateGeneralSettings() {
//		Setting siteName =new Setting("SITE_NAME", "Shopme", SettingCategory.GENERAL);
		Setting siteLogo =new Setting("SITE_LOGO", "Shopme.png", SettingCategory.GENERAL);
		Setting copyright =new Setting("COPYRIGHT", "Copyright (C) 2023 Shopme Ltd.", SettingCategory.GENERAL);
		settingRepository.saveAll(List.of(siteLogo,copyright));
		Iterable<Setting> iterable=settingRepository.findAll();
		System.err.println("find all: "+iterable);
	
		assertThat(iterable).size().isGreaterThan(0);
	}
	@Test
	public void testCreateCurrenceSettings() {
		Setting currencyId = new Setting("CURRENCY_ID", "1", SettingCategory.CURRENCY);
		Setting symbol = new Setting("CURRENCY_SYMBOL", "$", SettingCategory.CURRENCY);
		Setting symbolPosition = new Setting("CURRENCY_SYMBOL_POSITION", "before", SettingCategory.CURRENCY);
		Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE", "POINT", SettingCategory.CURRENCY);
		Setting decimalDigits = new Setting("DECIMAL_DIGITS", "2", SettingCategory.CURRENCY);
		Setting thousandsPointType = new Setting("THOUSANDS_POINT_TYPE", "COMMA", SettingCategory.CURRENCY);
		settingRepository.saveAll(List.of(currencyId, symbol, symbolPosition, decimalPointType, decimalDigits, thousandsPointType));
	}
	@Test
	public void testShow() {
		List<Setting> list = settingRepository.findAll();
		list.forEach(System.err::println);
	}
	
	@Test
	public void testSettingBag() {
		List<Setting> listSettings = settingRepository.findAll();
		int index = listSettings.indexOf(new Setting("SITE_LOGO"));
		if (index>=0) {
			Setting setting =listSettings.get(index);
			if (setting!=null) {
				 System.err.println("Value: "+setting.getValue());
			}
			 System.err.println("Object: "+setting);
		}else {
		 System.err.println("null");
		}
	}
	@Test
	public void testListSettingsByCategory() {
		List<Setting> list = settingRepository.findByCategory(SettingCategory.GENERAL);
		list.forEach(System.err::println);
	}
	@Test
	public void testfindByTwoCategories() {
//		List<Setting> settings = settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
		List<Setting> settings = settingRepository.findAll();
		settings.forEach(System.out::println);
	}
}
