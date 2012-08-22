package com.uppidy.android.sdk.api;

import java.util.List;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Transport object for results of modification operations
 * 
 * Part of the Uppidy Web Services API
 * 
 * @author arudnev@uppidy.com
 */
public class ApiModifications extends ApiObject {

	private List<ApiEntity> data;

	public List<ApiEntity> getData() {
		return data;
	}

	public void setData(List<ApiEntity> data) {
		this.data = data;
	}
	
	public MultiValueMap<String, ApiEntity> refsToEntities() {
		MultiValueMap<String, ApiEntity> map = new LinkedMultiValueMap<String, ApiEntity>();
		if(data != null) {
			for(ApiEntity entity : data) {
				if(entity.getRef() != null) map.add(entity.getRef(), entity);
			}
		}		
		return map;
	}
}
