package com.uppidy.android.sdk.api;

import java.util.List;
import java.util.Map;

/**
 * Reference to a contact
 * 
 * Part of the Uppidy Web Services API
 * 
 * @author arudnev@uppidy.com
 */
public class ApiContact extends ApiExtensible {

	private String id;	
	private String name;
	private Map<String, List<String>> addressByType;
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Map<String, List<String>> getAddressByType() {
		return addressByType;
	}
	
	public void setAddressByType(Map<String, List<String>> addressByType) {
		this.addressByType = addressByType;
	}
	
	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + ((addressByType == null) ? 0 : addressByType.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof ApiContact)) {
			return false;
		}
		
		ApiContact other = (ApiContact) obj;
		if (addressByType == null || other.addressByType == null) {
			return false;						
		}
		return addressByType.equals(other.addressByType);
	}
	
}
