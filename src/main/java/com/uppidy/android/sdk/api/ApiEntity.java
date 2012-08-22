package com.uppidy.android.sdk.api;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.springframework.util.MultiValueMap;

/**
 * Base class that provides object identity, creation and modification times as
 * well as means to extend default set of attributes
 * 
 * @author arudnev@uppidy.com
 */
public class ApiEntity extends ApiObject {
	
	private String ref;
	
	private String id;

	private Date createdTime;

	private Date updatedTime;

	private Map<String, String> extra;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Map<String, String> getExtra() {
		return extra;
	}

	public void setExtra(Map<String, String> extra) {
		this.extra = extra;
	}
	
	public void copyFromRef(MultiValueMap<String, ApiEntity> map) {
		if(ref != null && map != null) {
			copyRefData(map.getFirst(ref));					
		}
	}
	
	public void copyRefData(ApiEntity other) {
		if(other != null) {
			this.id = other.id;
			this.createdTime = other.createdTime;
			this.updatedTime = other.updatedTime;
			this.ref = other.ref;
		}	
	}
	
	public static void copyFromRefs(Collection<? extends ApiEntity> entities, MultiValueMap<String, ApiEntity> map) {
		if(entities != null && map != null) {
			for(ApiEntity entity : entities) {
				entity.copyFromRef(map);
			}
		}		
	}
}
