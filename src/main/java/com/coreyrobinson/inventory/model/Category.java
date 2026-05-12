/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Creates a reference to a Category for grouping items.
 * DATE: 05/06/2026
 */
package com.coreyrobinson.inventory.model;

public class Category {
	
	private int categoryId;
	private String categoryName;
	private String description;
	private boolean active;
	
	public Category() {
		
	}
	
	/**
	 * Constructor for creating a category to place items in.
	 * @param categoryId		I.D. for a given category.
	 * @param categoryName		Name of a given category.
	 * @param description		Description of a category.
	 * @param active			Flag for if the category is activated or deactivated.
	 */
	public Category(int categoryId, String categoryName, String description, boolean active) {
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.description = description;
		this.active = active;
	}
	
	/**
	 * Construction for only the required information at creation.
	 * @param categoryName		Name of a given category.
	 * @param description		Description of a category.
	 */
	public Category(String categoryName, String description) {
		this.categoryName = categoryName;
		this.description = description;
		this.active = true;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "Category [categoryId=" + categoryId + ", categoryName=" + categoryName + ", description=" + description
				+ ", active=" + active + "]";
	}
}
