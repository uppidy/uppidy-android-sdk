package com.uppidy.android.sdk.api;

import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;

/**
 * Base class for data transfer objects
 * 
 * @author arudnev@uppidy.com
 */
public class ApiObject {
	
	private Map<String, JsonNode> extra = new LinkedHashMap<String, JsonNode>(1);
	
	@JsonAnyGetter
	public Map<String, JsonNode> getProperties() {
		return extra;
	}

	@JsonAnySetter
	public void setProperty(String key, JsonNode value) {
		if(value == null) {
			extra.remove(key);
		} else {
			extra.put(key, value);			
		}
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
