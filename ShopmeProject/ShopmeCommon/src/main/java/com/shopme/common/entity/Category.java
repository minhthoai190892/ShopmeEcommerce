package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "categories")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 128, nullable = false, unique = true)
	private String name;
	@Column(length = 128, nullable = false, unique = true)
	private String alias;
	@Column(length = 128, nullable = false)
	private String image;
	@Column(name = "all_parent_ids",length = 256,nullable = true)
	private String allParentIDs;
	private boolean enabled;
	@OneToOne
	@JoinColumn(name = "parent_id")
	private Category parent;
	@OneToMany(mappedBy = "parent")
	private Set<Category> children = new HashSet<>();

	public Category() {
		super();
	}

	/**
	 * Hàm tạo mới một Category
	 * 
	 * @param name nhận vào một tên category
	 */
	public Category(String name) {
		super();
		this.name = name;
		this.alias = name;
		this.image = "image.png";
	}

	/**
	 * Hàm thêm vào danh mục cha(subCatagory)
	 * 
	 * @param name   tên sub category
	 * @param parent tên category cha cần thêm vào
	 * 
	 */
	public Category(String subName, Category parent) {
		this(subName);
		this.parent = parent;
	}

	public Category(Integer id, String name, String alias) {
		this.id = id;
		this.name = name;
		this.alias = alias;
	}

	public Category(Integer id) {
		super();
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public Set<Category> getChildren() {
		return children;
	}

	
	
	public String getAllParentIDs() {
		return allParentIDs;
	}

	public void setAllParentIDs(String allParentIDs) {
		this.allParentIDs = allParentIDs;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
	}

	/**
	 * Hàm sao chép id và tên
	 * 
	 * @param category nhận một đối tượng Category
	 * @return trả về một đối tượng đã được sao chép
	 */
	public static Category copyIdAndName(Category category) {
		Category copyCategory = new Category();
		copyCategory.setId(category.getId());
		copyCategory.setName(category.getName());
		return copyCategory;
	}

	/**
	 * Hàm copy id và tên
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	public static Category copyIdAndName(Integer id, String name) {
		Category copyCategory = new Category();
		copyCategory.setId(id);
		copyCategory.setName(name);
		return copyCategory;
	}

	/**
	 * hàm copy tất cả thông tin
	 * 
	 * @param category nhận vào đối tượng
	 * @return trả về một đối tượng được copy
	 */
	public static Category copyFull(Category category) {
		Category copyCategory = new Category();
		copyCategory.setId(category.getId());
		copyCategory.setName(category.getName());
		copyCategory.setAlias(category.alias);
		copyCategory.setImage(category.getImage());
		copyCategory.setEnabled(category.isEnabled());
		copyCategory.setHasChildren(category.getChildren().size()>0);
		return copyCategory;
	}

	/**
	 * hàm copy toàn bộ thông tin và thây đổi tên
	 * 
	 * @param category nhận vào một đối tượng
	 * @param name     nhận vào một chuổi "tên"
	 * @return trả về một đối tượng đã được copy
	 */
	public static Category copyFull(Category category, String name) {
		Category copyCategory = Category.copyFull(category);
		copyCategory.setName(name);
		return copyCategory;
	}

	@Transient
	public String getImagePath() {
		if (this.id == null) {
			return "/images/image-thumbnail.png";
		}
		return "/category-images/" + this.id + "/" + this.image;
	}

	// @Override
	// public String toString() {
	// 	return "Category [id=" + id + ", name=" + name + ", alias=" + alias + ", image=" + image + ", enabled="
	// 			+ enabled + "]";
	// }

	//khai báo một biến không có trong class entity
	@Transient
	private boolean hasChildren;
	@Override
	public String toString() {
		return  this.name ;
	}

	//getter and setter
	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
	
	
	

}
