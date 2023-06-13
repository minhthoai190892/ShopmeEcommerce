package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "users")
public class User {
	//field
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 128,nullable = false,unique = true)
	private String	email;
	@Column(length = 64,nullable = false)
	private String	password;
	@Column(name = "first_name", length = 45,nullable = false)
	private String	firstName;
	@Column(name = "last_name", length = 45,nullable = false)
	private String	lastName;
	@Column(name = "photos", length = 64)
	private String	photos;
	private boolean	enable;
	
	//tập các roles
	@ManyToMany(fetch = FetchType.EAGER) // quan hệ nhiều - nhiều
	//@JoinTable: là tên bản tham gia
	@JoinTable(
			name = "users_roles",// tên bản tham gia
			joinColumns = @JoinColumn(name="user_id"),//chỉ định cột tham gia
			inverseJoinColumns = @JoinColumn(name = "role_id")//cột tham gia nghịch đảo
			//===> 2 khóa ngoại của bản tham gia
			)
	private Set<Role> roles = new HashSet<>();

	
	
	
	public User() {
		super();
	}
	
	

	public User(String email, String password, String firstName, String lastName) {
		super();
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public void addRole(Role role) {
		this.roles.add(role);
	}



	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", photos=" + photos + ", enable=" + enable + ", roles=" + roles + "]";
	}

	@Transient
	//getter
	public String getPhotosImagePath() {
		//kiểm tra xem id và image có null không
//		if (id == null || photos==null) {
//			return "images/ShopmeAdminSmall.png";
//		}
		return "/user-photos/"+this.id+"/"+this.photos;
	}

	
	@Transient
	public String getFullName() {
		return firstName+ " "+lastName;
	}




	
}
