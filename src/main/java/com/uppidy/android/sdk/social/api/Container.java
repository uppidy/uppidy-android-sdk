package com.uppidy.android.sdk.social.api;

import com.uppidy.util.StringUtil;

/**
 * Backup container, i.e. container for backup of SMS / MMS messages from specific device
 * 
 * Part of the Uppidy Web Services API
 * 
 * @author arudnev@uppidy.com
 */
public class Container {
	private String id;
	private String description;
	private Contact owner; 
	
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
	
	public Contact getOwner() {
		return owner;
	}
	
	public void setOwner(Contact owner) {
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
		if (obj == null || !(obj instanceof Container)) {
			return false;
		}
		
		Container other = (Container) obj;
		if (id == null || other.id == null) {
			return false;						
		}
		return id.equals(other.id);
	}
	
	public String toString() {
		return "{ \"description\":" + StringUtil.quote(description) + ", \"id\":\"" + id + "\" }";
	}
	
}
