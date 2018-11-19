package com.revature.model;

import java.sql.Timestamp;

public class Reimbursement {
	
	private int reimbursementId;
	private double reimbursementAmount;
	private Timestamp submitted;
	private Timestamp resolved;
	private String description;
	private int authorUserId;
	private String authorFirstName;
	private String authorLastName; 
	private int resolverUserId;
	private int statusId;
	private int reimbursementTypeId;
	
	public Reimbursement(){
		super();
	}
	
	public Reimbursement(int reimbursementId, double reimbursementAmount, Timestamp submitted, Timestamp resolved,
			String description, int authorUserId, String authorFirstName, String authorLastName, int resolverUserId,
			int statusId, int reimbursementTypeId) {
		super();
		this.reimbursementId = reimbursementId;
		this.reimbursementAmount = reimbursementAmount;
		this.submitted = submitted;
		this.resolved = resolved;
		this.description = description;
		this.authorUserId = authorUserId;
		this.authorFirstName = authorFirstName;
		this.authorLastName = authorLastName;
		this.resolverUserId = resolverUserId;
		this.statusId = statusId;
		this.reimbursementTypeId = reimbursementTypeId;
	}

	public int getReimbursementId() {
		return reimbursementId;
	}

	public void setReimbursementId(int reimbursementId) {
		this.reimbursementId = reimbursementId;
	}

	public double getReimbursementAmount() {
		return reimbursementAmount;
	}

	public void setReimbursementAmount(double reimbursementAmount) {
		this.reimbursementAmount = reimbursementAmount;
	}

	public Timestamp getSubmitted() {
		return submitted;
	}

	public void setSubmitted(Timestamp submitted) {
		this.submitted = submitted;
	}

	public Timestamp getResolved() {
		return resolved;
	}

	public void setResolved(Timestamp resolved) {
		this.resolved = resolved;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getAuthorUserId() {
		return authorUserId;
	}

	public void setAuthorUserId(int authorUserId) {
		this.authorUserId = authorUserId;
	}

	public String getAuthorFirstName() {
		return authorFirstName;
	}

	public void setAuthorFirstName(String authorFirstName) {
		this.authorFirstName = authorFirstName;
	}

	public String getAuthorLastName() {
		return authorLastName;
	}

	public void setAuthorLastName(String authorLastName) {
		this.authorLastName = authorLastName;
	}

	public int getResolverUserId() {
		return resolverUserId;
	}

	public void setResolverUserId(int resolverUserId) {
		this.resolverUserId = resolverUserId;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public int getReimbursementTypeId() {
		return reimbursementTypeId;
	}

	public void setReimbursementTypeId(int reimbursementTypeId) {
		this.reimbursementTypeId = reimbursementTypeId;
	}

	@Override
	public String toString() {
		return "Reimbursement [reimbursementId=" + reimbursementId + ", reimbursementAmount=" + reimbursementAmount
				+ ", submitted=" + submitted + ", resolved=" + resolved + ", description=" + description
				+ ", authorUserId=" + authorUserId + ", authorFirstName=" + authorFirstName + ", authorLastName="
				+ authorLastName + ", resolverUserId=" + resolverUserId + ", statusId=" + statusId
				+ ", reimbursementTypeId=" + reimbursementTypeId + "]";
	}
	
	
}

	