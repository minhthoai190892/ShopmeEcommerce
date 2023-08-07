package com.shopme.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Category;

public interface CategoryRepository  extends JpaRepository<Category	, Integer> {

	/**
	 * Hàm lấy danh sách Category với enabled là true
	 * @return trả về danh sách category
	 */
	@Query("select c from Category c where c.enabled=true order by c.name asc")
	public List<Category> findAllEnable();
	@Query("select c from Category c where c.enabled=true  and c.alias=?1")
	public Category findByAliasEnabled(String alias);
}
