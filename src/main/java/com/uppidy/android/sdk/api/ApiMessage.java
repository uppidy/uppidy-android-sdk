package com.uppidy.android.sdk.api;

import java.util.Date;
import java.util.List;

import org.springframework.util.MultiValueMap;

/**
 * Model class representing an entry in a feed or chat conversation.
 * 
 * Part of the Uppidy Web Services API
 * 
 * @author arudnev@uppidy.com
 */
public class ApiMessage extends ApiEntity {

	private ApiContactInfo from;

	private List<ApiContactInfo> to;

	private String text;

	private boolean sent;

	private Date sentTime;
	
	private ApiLocation location;

	private List<ApiBodyPart> parts;

	public ApiContactInfo getFrom() {
		return from;
	}

	public String getText() {
		return text;
	}

	public boolean isSent() {
		return sent;
	}

	public Date getSentTime() {
		return sentTime;
	}

	public List<ApiContactInfo> getTo() {
		return to;
	}

	public void setFrom(ApiContactInfo from) {
		this.from = from;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	public void setSentTime(Date sentTime) {
		this.sentTime = sentTime;
	}

	public void setTo(List<ApiContactInfo> to) {
		this.to = to;
	}

	public ApiLocation getLocation() {
		return location;
	}

	public void setLocation(ApiLocation location) {
		this.location = location;
	}

	public List<ApiBodyPart> getParts() {
		return parts;
	}

	public void setParts(List<ApiBodyPart> parts) {
		this.parts = parts;
	}

	@Override
	public void copyFromRef(MultiValueMap<String, ApiEntity> map) {
		super.copyFromRef(map);
		copyFromRefs(parts, map);
	}
}
