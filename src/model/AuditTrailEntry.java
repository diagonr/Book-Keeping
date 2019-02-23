package model;

import java.util.Date;

public class AuditTrailEntry {
	private int id;
	private Date dateAdded;
	private String message;
	
	public AuditTrailEntry() {
		
	}
	public AuditTrailEntry(int id, Date dateAdded, String message) {
		this.id = id;
		this.dateAdded = dateAdded;
		this.message = message;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return message;
	}
	
}
