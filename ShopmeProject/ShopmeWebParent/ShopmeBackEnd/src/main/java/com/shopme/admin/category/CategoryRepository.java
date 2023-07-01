package com.shopme.admin.category;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	/**
	 * Hàm update enabled
	 * 
	 * @param id      nhận id của category
	 * @param enabled giá tị của enabled
	 */
	@Query("update Category c set c.enabled=?2 where c.id=?1") // ?1:là vị trí đặt tham số trong hàm updateEnabledStatus
	@Modifying // chú thích sửa đổi
	public void updateEnabledStatus(Integer id, boolean enabled);

	/**
	 * Hàm hiển thị thông tin và sắp xếp
	 * @param sort cách thức sắp xếp (asc hoặc desc)
	 * @return
	 */
	@Query("select c from Category c where c.parent.id is null")
	public List<Category> findRootCategories(Sort sort);

	/**
	 * Hàm tìm kiếm thông qua tên
	 * @param name nhận một tên tìm kiếm
	 * @return trả về đối tượng tìm kiếm
	 */
	public Category findByName(String name);
	/**
	 * hàm tìm kiếm thông qua bí danh
	 * @param alias nhận một bí danh
	 * @return trả về một đối tượng tìm kiếm
	 */
	public Category findByAlias(String alias);
	public Long countById(Integer id) ;
	
	/**
	 * Hàm hiển thị thông tin và phân trang
	 * @param pageable 
	 * @return
	 */
	@Query("select c from Category c where c.parent.id is null")
	public Page<Category> findRootCategories(Pageable pageable);
	@Query("select c from Category c where c.name like %?1%")
	public Page<Category> search(String keyword,Pageable pageable);
}
