package com.shopme.admin.user;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;



import com.shopme.common.entity.User;

public interface UserRepository  extends JpaRepository<User, Integer>{
	@Query("select u from User u where u.email = :email")
	public User getUserByEmail(@Param("email")String email);
	
	public Long countById(Integer id);
	@Query("update User u set u.enable=?2 where u.id=?1")//?1:là vị trí đặt tham số trong hàm updateEnabledStatus
	@Modifying // chú thích sửa đổi
	public void updateEnabledStatus(Integer id, boolean enabled);
//	tìm theo từng field
//	@Query("select u from User u where u.firstName like %?1% or u.lastName like %?1% or u.email like %?1%") 
	@Query("select u from User u where concat(u.id,' ',u.email,' ',u.firstName,' ',u.lastName) like %?1% ")
	public Page<User> findAll(String keyword,Pageable pageable);
}
