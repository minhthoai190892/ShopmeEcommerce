package com.shopme.admin.currency;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopme.common.entity.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Integer>{
	/**
	 * Lấy danh sách Currency và sắp xếp theo tên
	 * */
	public List<Currency> findAllByOrderByNameAsc();
}
