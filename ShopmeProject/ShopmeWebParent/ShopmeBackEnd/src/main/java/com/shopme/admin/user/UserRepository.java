package com.shopme.admin.user;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.User;

@Repository
public interface UserRepository  extends PagingAndSortingRepository<User, Integer>{
	@Query("select u from User u where u.email = :email")
	public User getUserByEmail(@Param("email")String email);
	
	public Long countById(Integer id);
	@Query("update User u set u.enable=?2 where u.id=?1")//?1:là vị trí đặt tham số trong hàm updateEnabledStatus
	@Modifying // chú thích sửa đổi
	public void updateEnabledStatus(Integer id, boolean enabled);

	public Streamable<User> findById(Integer id);

	public User save(User user);

	public void deleteById(Integer id);

	public List<User> findAll();
}
