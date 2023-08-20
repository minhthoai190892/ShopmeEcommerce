package com.shopme.admin.brand;


import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Brand;

import jakarta.transaction.Transactional;

@Service
@Transactional

public class BrandService {
	public static int BRANDS_PER_PAGE=3;
	@Autowired
	private BrandRepository brandRepository;

	public List<Brand> listAll() {
		return brandRepository.findAll();
	}
	public void listByPage(int pageNum,PagingAndSortingHelper helper){
		Sort sort = Sort.by(helper.getSortField());
		sort = helper.getSortDir().equals("asc")?sort.ascending():sort.descending();
		Pageable pageable = PageRequest.of(pageNum-1, BRANDS_PER_PAGE,sort);
		Page<Brand> page=null;
		if (helper.getKeyword() != null) {
			page= brandRepository.findAll(helper.getKeyword(),pageable);
		}else {
			page= brandRepository.findAll(pageable);
		}
		helper.updateModelAttributes(pageNum, page);
		
		
	}

	public Brand save(Brand brand) {
		return brandRepository.save(brand);
	}

	public Brand get(Integer id) throws BrandNotFoundException {
		try {
			return brandRepository.findById(id).get();
		} catch (NoSuchElementException e) {

			throw new BrandNotFoundException("Could not find any brand with ID " + id);

		}
	}

	public void delete(Integer id) throws BrandNotFoundException {
		Long countById = brandRepository.countById(id);
		if (countById == null || countById == 0) {
			throw new BrandNotFoundException("Could not find any brand with ID " + id);
		}
		brandRepository.deleteById(id);
	}

	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		Brand brandByName = brandRepository.findByName(name);
		if (isCreatingNew) {
			if (brandByName != null) {
				return "Duplicate";
			}
		} else {
			if (brandByName != null && brandByName.getId() != id) {
				return "Duplicate";
			}
		}
		return "Ok";
	}
}
