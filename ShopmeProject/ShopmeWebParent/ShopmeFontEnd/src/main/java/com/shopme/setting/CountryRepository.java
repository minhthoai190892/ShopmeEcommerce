package com.shopme.setting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopme.common.entity.Country;


public interface CountryRepository extends JpaRepository<Country, Integer> {
	/**
	 * Hàm tìm kiếm tất cả Country và sắp xếp 
	 * */
	public List<Country> findAllByOrderByNameAsc();
}
