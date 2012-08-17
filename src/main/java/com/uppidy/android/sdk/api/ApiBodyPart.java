package com.uppidy.android.sdk.api;

import java.util.Date;

/**
 * Model class representing a body part of a message, i.e. picture, video, audio or other text or binary attachment. 
 * 
 * Part of the Uppidy Web Services API
 * 
 * @author arudnev@uppidy.com
 */
public class ApiBodyPart {
	
	private String id;
	
	private byte[] data;
	
	private String contentType;
	
	private String fileName;
	
	private Date createdTime;

	private Date updatedTime;
	
	public String getId() {
		return id;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
