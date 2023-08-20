package com.shopme.admin.paging;


import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.shopme.common.entity.User;

@NoRepositoryBean
public interface SearchRepository<T,ID> extends JpaRepository<User, Integer> {
	public Page<User> findAll(String keyword,org.springframework.data.domain.Pageable pageable);
	
}
