package com.uppidy.android.sdk.social.api;

import com.uppidy.util.StringUtil;

/**
 * Contact in the address book
 * 
 * Part of the Uppidy Web Services API
 * 
 * @author arudnev@uppidy.com
 */
public class Contact {

	private String address;
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Override
	public String toString() {
		return "{ \"name\":" + StringUtil.quote(name) + ", \"address\":\"" + address + "\" }";
	}
	
	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + ((address == null) ? 0 : address.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof Contact)) {
			return false;
		}
		
		Contact other = (Contact) obj;
		if (address == null || other.address == null) {
			return false;						
		}
		return address.equals(other.address);
	}
	
	public Reference createReference() {
		Reference result = new Reference();
		result.setId(getAddress());
		result.setName(getName());
		return result;
	}
	
}
