package com.uppidy.android.sdk.api;

import java.util.Collection;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
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

	@JsonSerialize(include = Inclusion.NON_NULL)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonSerialize(include = Inclusion.NON_NULL)
	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	@JsonSerialize(include = Inclusion.NON_NULL)
	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	@JsonSerialize(include = Inclusion.NON_NULL)
	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
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
	
	// TODO unexpected NullPointerException is thrown from this method. Need to find out why
	public static void copyFromRefs(Collection<? extends ApiEntity> entities, MultiValueMap<String, ApiEntity> map) {
		if(entities != null && map != null) {
			for(ApiEntity entity : entities) {
				entity.copyFromRef(map);
			}
		}		
	}
}
