package com.uppidy.android.sdk.api;

/**
 * Base class for data transfer objects
 * 
 * @author arudnev@uppidy.com
 */
public class ApiObject {

	@Override
	public String toString() {
		try {
			return new org.codehaus.jackson.map.ObjectMapper().writeValueAsString(this);
		} catch (Exception ex) {
			return super.toString();
		}
	}
}
