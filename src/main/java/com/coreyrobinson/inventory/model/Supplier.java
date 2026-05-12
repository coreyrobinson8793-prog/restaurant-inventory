/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Creates a reference to a Supplier.
 * DATE: 05/03/2026
 */
package com.coreyrobinson.inventory.model;

public class Supplier {
	
	private int supplierId;
	private String supplierName;
	private String repName;
	private String phone;
	private String email;
	private String address;
	private boolean active;
	
	public Supplier() {
		
	}
	
	/**
	 * Constructor for Supplier information.
	 * @param supplierId	I.D. for a supplier.
	 * @param supplierName	Name of the supplier.
	 * @param repName		Name of the representative.
	 * @param phone			Phone number for the representative.
	 * @param email			Email for the representative.
	 * @param address		Address of the supplier.
	 * @param active		If this supplier is currently active.
	 */
	public Supplier(int supplierId, String supplierName, String repName, String phone, String email, String address, boolean active) {
		this.supplierId = supplierId;
		this.supplierName = supplierName;
		this.repName = repName;
		this.phone = phone;
		this.email = email;
		this.address = address;
		this.active = active;
	}
	
	/**
	 * Constructor for Supplier information only needed at registration.
	 * @param supplierName		Name of the supplier.
	 */
	public Supplier(String supplierName) {
		this.supplierName = supplierName;
		this.active = true;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getRepName() {
		return repName;
	}

	public void setRepName(String repName) {
		this.repName = repName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "Supplier [supplierId=" + supplierId + ", supplierName=" + supplierName + ", repName=" + repName
				+ ", phone=" + phone + ", email=" + email + ", address=" + address + ", active=" + active + "]";
	}
}
