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

	private String type;
	
	private String model;
	
	private String version;
	
	private ApiProfile master;
	
	private ApiAppInfo app;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public ApiProfile getMaster() {
		return master;
	}

	public void setMaster(ApiProfile master) {
		this.master = master;
	}

	public ApiAppInfo getApp() {
		return app;
	}

	public void setApp(ApiAppInfo app) {
		this.app = app;
	}

	public ApiContactInfo getOwner() {
		return owner;
	}

	public void setOwner(ApiContactInfo owner) {
		this.owner = owner;
	}
}
