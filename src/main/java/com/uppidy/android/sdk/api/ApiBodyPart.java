package com.uppidy.android.sdk.api;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.core.io.InputStreamSource;

/**
 * Model class representing a body part of a message, i.e. picture, video, audio
 * or other text or binary attachment.
 * 
 * Part of the Uppidy Web Services API
 * 
 * @author arudnev@uppidy.com
 */
public class ApiBodyPart extends ApiEntity {

	private InputStreamSource data;

	private String contentType;
	
	private String fileName;

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

	@JsonIgnore
	public InputStreamSource getData() {
		return data;
	}

	@JsonIgnore
	public void setData(InputStreamSource data) {
		this.data = data;
	}
}
