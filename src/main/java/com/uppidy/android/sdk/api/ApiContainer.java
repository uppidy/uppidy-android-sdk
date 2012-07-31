package com.uppidy.android.sdk.api;


/**
 * Backup container, i.e. container for backup of SMS / MMS messages from specific device
 * 
 * Part of the Uppidy Web Services API
 * 
 * @author arudnev@uppidy.com
 */
public class ApiContainer extends ApiExtensible {
	private String id;
	private String description;
	private ApiContactInfo owner; 
	
	public String getId() {
		return id;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setId(String id) {
		this.id = id;
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
	
	@Override
	public int hashCode() {
		int result = 1;
		result = 73 * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof ApiContainer)) {
			return false;
		}
		
		ApiContainer other = (ApiContainer) obj;
		if (id == null || other.id == null) {
			return false;						
		}
		return id.equals(other.id);
	}
	
}
