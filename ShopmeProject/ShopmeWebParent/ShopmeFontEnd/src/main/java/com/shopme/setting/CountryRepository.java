package com.shopme.setting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Country;


public interface CountryRepository extends JpaRepository<Country, Integer> {
	/**
	 * Hàm tìm kiếm tất cả Country và sắp xếp 
	 * */
	public List<Country> findAllByOrderByNameAsc();
	/**
	 * Hàm lấy country bằng code của country
	 * */
	@Query("select c from Country c where c.code =?1")
	public Country findByCode(String code);
}
