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
public class ApiContact extends ApiEntity {

	private String name;

	private Map<String, List<String>> addressByType;

	public String getName() {
		return name;
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
}
