package com.uppidy.android.sdk.api;

import java.util.Map;

/**
 * Base class that provides means to extend default set of attributes
 * 
 * @author arudnev@uppidy.com
 */
public class ApiExtensible {
	private Map<String, String> extra;

	/**
	 * @return the extra properties
	 */
	public Map<String, String> getExtra() {
		return extra;
	}

	/**
	 * @param extra the extra properties to set
	 */
	public void setExtra(Map<String, String> extra) {
		this.extra = extra;
	}
	
	@Override
	public String toString() {
		try {
			return new org.codehaus.jackson.map.ObjectMapper().writeValueAsString(this);			
		} catch (Exception ex) {
			return super.toString();
		}
	}

}
