package com.uppidy.android.sdk.social.api;

import java.util.Date;

/**
 * Model class containing a Uppidy user's profile information.
 * 
 * Part of the Uppidy Web Services API
 * 
 * @author arudnev@uppidy.com
 */
public class Profile extends Extensible {

	private Date createdTime;

	private String email;

	private String id;

	private String name;

	private Date updatedTime;

	private String username;

	private Boolean verified;

	/**
	 * The time the user's profile was created.
	 * @return the time that the user's profile was updated
	 */
	public Date getCreatedTime() {
		return createdTime;
	}

	/**
	 * The user's email address.
	 * Available only with "email" permission.
	 * @return The user's email address
	 */
	public String getEmail() {
	    return email;
    }

	/**
	 * The user's Uppidy id
	 * @return The user's Uppidy id
	 */
	public String getId() {
		return id;
	}

	/**
	 * The user's full name
	 * @return The user's full name
	 */
	public String getName() {
		return name;
	}

	/**
	 * The last time the user's profile was updated.
	 * @return the time that the user's profile was updated
	 */
	public Date getUpdatedTime() {
		return updatedTime;
	}
	
	/**
	 * The user's Uppidy username
	 * @return the user's Uppidy username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * The user's account verification status.
	 * Available only if requested by an authenticated user.
	 * @return true if the profile has been verified, false if it has not, or null if not available.
	 */
	public Boolean isVerified() {
		return verified;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setVerified(Boolean verified) {
		this.verified = verified;
	}
	
	public AppInfo createReference() {
		AppInfo result = new AppInfo();
		result.setId(getId());
		result.setName(getName());
		return result;
	}
	
}
