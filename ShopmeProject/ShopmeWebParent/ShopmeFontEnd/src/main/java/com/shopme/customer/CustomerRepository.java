package com.shopme.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	/**
	 * Hàm tìm kiếm bằng Email của customer
	 * @param email email của customer
	 * */
	@Query("select c from Customer c Where c.email=?1")
	public Customer findByEmail(String email);
	/**
	 * Hàm tìm customer bằng Verification Code
	 * @param code là Verification Code của customer
	 * */
	@Query("select c from Customer c where c.verificationCode=?1")
	public Customer findByVerificationCode(String code);
	/**
	 * Hàm cập nhật enabled và verificationCode code khi click vào "Verify"
	 * @param id id customer
	 * */
	@Query("update Customer c set c.enabled = true, c.verificationCode=null where c.id=?1")
	@Modifying
	public void enabled(Integer id);
	/**
	 * hàm dùng để update loại xác thực (database, facebook, google)
	 * @param customerId là id của customer cầm cập nhật
	 * @param type là loại xác thực
	 * */
	@Query("update Customer c set c.authenticationType = ?2 where c.id=?1")
	@Modifying
	public void updateAuthenticationType(Integer customerId,AuthenticationType type);
	/**
	 * Hàm lấy customer bằng field resetPassword
	 * */
	public Customer findByResetPasswordToken(String token);
	
}
