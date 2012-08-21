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

	private List<ApiEntity> created;
	private List<ApiEntity> updated;
	private List<ApiEntity> deleted;

	public List<ApiEntity> getCreated() {
		return created;
	}

	public void setCreated(List<ApiEntity> created) {
		this.created = created;
	}

	public List<ApiEntity> getUpdated() {
		return updated;
	}

	public void setUpdated(List<ApiEntity> updated) {
		this.updated = updated;
	}

	public List<ApiEntity> getDeleted() {
		return deleted;
	}

	public void setDeleted(List<ApiEntity> deleted) {
		this.deleted = deleted;
	}
	
	public MultiValueMap<String, ApiEntity> refsToEntities() {
		MultiValueMap<String, ApiEntity> map = new LinkedMultiValueMap<String, ApiEntity>();
		mapRefsToEntities(created, map);
		mapRefsToEntities(updated, map);
		mapRefsToEntities(deleted, map);
		return map;
	}
	private void mapRefsToEntities(List<ApiEntity> list, MultiValueMap<String, ApiEntity> map) {
		if(list != null) {
			for(ApiEntity entity : list) {
				if(entity.getRef() != null) map.add(entity.getRef(), entity);
			}
		}		
	}
}
