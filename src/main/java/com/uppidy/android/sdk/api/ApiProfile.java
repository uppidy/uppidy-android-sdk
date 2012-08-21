package com.uppidy.android.sdk.api;


/**
 * Model class containing a Uppidy user's profile information.
 * 
 * Part of the Uppidy Web Services API
 * 
 * @author arudnev@uppidy.com
 */
public class ApiProfile extends ApiEntity {

	private String email;

	private String name;

	private String username;

	private Boolean verified;

	/**
	 * The user's email address. Available only with "email" permission.
	 * 
	 * @return The user's email address
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * The user's full name
	 * 
	 * @return The user's full name
	 */
	public String getName() {
		return name;
	}

	/**
	 * The user's Uppidy username
	 * 
	 * @return the user's Uppidy username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * The user's account verification status. Available only if requested by an
	 * authenticated user.
	 * 
	 * @return true if the profile has been verified, false if it has not, or
	 *         null if not available.
	 */
	public Boolean isVerified() {
		return verified;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}
}
