package com.shopme.admin.brand;

import java.util.List;
import java.util.NoSuchElementException;

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
	public Brand save(Brand brand) {
		return brandRepository.save(brand);
	}
	public Brand get(Integer id) throws BrandNotFoundException {
		try {
			return brandRepository.findById(id).get();
		} catch (NoSuchElementException e) {

			throw new BrandNotFoundException("Could not find any brand with ID "+id);
		
		}
	}
	public void delete(Integer id) throws BrandNotFoundException {
		Long countById = brandRepository.countById(id);
		if (countById==null || countById==0) {
			throw new BrandNotFoundException("Could not find any brand with ID "+id);
		}
		brandRepository.deleteById(id);
	}
}
