/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Creates a reference to an Audit Log which logs every data-changing action across the application.
 * DATE: 05/06/2026
 */
package com.coreyrobinson.inventory.model;

import java.time.LocalDateTime;

public class AuditLog {
	
	private int auditId;
	private int changedBy;
	private String actionTaken;
	private String tableAffected;
	private int recordId;
	private String details;
	private LocalDateTime auditDate;
	
	public AuditLog() {
		
	}
	
	/**
	 * Constructor for creating an audit log.
	 * @param auditId		I.D. of the audit log.
	 * @param changedBy		Who made a change in the application.
	 * @param actionTaken	What change was made in the application.
	 * @param tableAffected	Which table was affected by the change.
	 * @param recordId		I.D. for an individual change.
	 * @param details		Details regarding a given change.
	 * @param auditDate		Date the audit log was generated.
	 */
	public AuditLog(int auditId, int changedBy, String actionTaken, String tableAffected, int recordId, String details, LocalDateTime auditDate) {
		this.auditId = auditId;
		this.changedBy = changedBy;
		this.actionTaken = actionTaken;
		this.tableAffected = tableAffected;
		this.recordId = recordId;
		this.details = details;
		this.auditDate = auditDate;
	}
	
	/**
	 * Constructor for required information at creation of an audit log.
	 * @param changedBy		Who made a change in the application.
	 * @param actionTaken	What change was made in the application.
	 * @param tableAffected	Which table was affected by the change.
	 * @param recordId		I.D. for an individual change.
	 * @param details		Details regarding a given change.
	 */
	public AuditLog(int changedBy, String actionTaken, String tableAffected, int recordId, String details) {
		this.changedBy = changedBy;
		this.actionTaken = actionTaken;
		this.tableAffected = tableAffected;
		this.recordId = recordId;
		this.details = details;
	}

	public int getAuditId() {
		return auditId;
	}

	public void setAuditId(int auditId) {
		this.auditId = auditId;
	}

	public int getChangedBy() {
		return changedBy;
	}

	public void setChangedBy(int changedBy) {
		this.changedBy = changedBy;
	}

	public String getActionTaken() {
		return actionTaken;
	}

	public void setActionTaken(String actionTaken) {
		this.actionTaken = actionTaken;
	}

	public String getTableAffected() {
		return tableAffected;
	}

	public void setTableAffected(String tableAffected) {
		this.tableAffected = tableAffected;
	}

	public int getRecordId() {
		return recordId;
	}

	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public LocalDateTime getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(LocalDateTime auditDate) {
		this.auditDate = auditDate;
	}

	@Override
	public String toString() {
		return "AuditLog [auditId=" + auditId + ", changedBy=" + changedBy + ", actionTaken=" + actionTaken
				+ ", tableAffected=" + tableAffected + ", recordId=" + recordId + ", details=" + details
				+ ", auditDate=" + auditDate + "]";
	}
}
