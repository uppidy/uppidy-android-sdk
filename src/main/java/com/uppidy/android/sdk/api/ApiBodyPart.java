package com.uppidy.android.sdk.api;

import java.util.Date;

import org.springframework.core.io.Resource;

/**
 * Model class representing a body part of a message, i.e. picture, video, audio or other text or binary attachment. 
 * 
 * Part of the Uppidy Web Services API
 * 
 * @author arudnev@uppidy.com
 */
public class ApiBodyPart {
	
	private String id;
	
	private Resource resource;
	
	private String contentType;
	
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

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
