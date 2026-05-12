/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Creates a reference to a Unit of measurement.
 * DATE: 05/03/2026
 */
package com.coreyrobinson.inventory.model;

public class Unit {
	
	private int unitId;
	private String unitName;
	
	public Unit() {
		
	}
	
	/**
	 * Constructor for units of measurement.
	 * @param unitId	I.D. for the unit of measurement.
	 * @param unitName		Unit of measurement's name.
	 */
	public Unit(int unitId, String unitName) {
		this.unitId = unitId;
		this.unitName = unitName;
	}
	
	/**
	 * Constructor for adding new units of measurement.
	 * @param unitName		Unit of measurement's name.
	 */
	public Unit(String unitName) {
		this.unitName = unitName;
	}

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	@Override
	public String toString() {
		return "Unit [unitId=" + unitId + ", unitName=" + unitName + "]";
	}
}
