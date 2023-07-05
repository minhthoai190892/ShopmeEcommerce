package com.shopme.admin.brand;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Brand;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BrandService {
	@Autowired
	private BrandRepository brandRepository;
	public List<Brand> listAll(){
		return brandRepository.findAll();
	}
}
