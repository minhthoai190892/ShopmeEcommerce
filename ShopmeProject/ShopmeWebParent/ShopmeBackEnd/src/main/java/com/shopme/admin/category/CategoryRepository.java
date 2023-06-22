package com.shopme.admin.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	/**
     * Hàm update enabled
	 * @param id nhận id của category
	 * @param enabled giá tị của enabled
	 */
	@Query("update Category c set c.enabled=?2 where c.id=?1")//?1:là vị trí đặt tham số trong hàm updateEnabledStatus
	@Modifying // chú thích sửa đổi
	public void updateEnabledStatus(Integer id, boolean enabled);

	@Query("select c from Category c where c.parent.id is null")
	public List<Category> findRootCategories();
}
