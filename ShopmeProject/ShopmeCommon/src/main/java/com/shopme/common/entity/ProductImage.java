package com.shopme.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_image")
public class ProductImage {
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY )
	
	private Integer id;
	@Column(nullable = false)
	private String name;
	@ManyToOne //nhiều hành ảnh chỉ có một product
	@JoinColumn(name = "product_id")
	private Product product;
	
	
	
	public ProductImage() {
		super();
	}
	public ProductImage(String name, Product product) {
		super();
		this.name = name;
		this.product = product;
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
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	@Override
	public String toString() {
		return "ProductImage [name=" + name + "]";
	}
	
	
}
