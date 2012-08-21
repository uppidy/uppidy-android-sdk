package com.uppidy.android.sdk.api;

/**
 * Backup container, i.e. container for backup of SMS / MMS messages from
 * specific device
 * 
 * Part of the Uppidy Web Services API
 * 
 * @author arudnev@uppidy.com
 */
public class ApiContainer extends ApiEntity {
	
	private String deviceId;

	private String description;

	private ApiContactInfo owner;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ApiContactInfo getOwner() {
		return owner;
	}

	public void setOwner(ApiContactInfo owner) {
		this.owner = owner;
	}
}
