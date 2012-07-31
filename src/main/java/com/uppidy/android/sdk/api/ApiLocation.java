package com.uppidy.android.sdk.api;

/**
 * Location of a message, event, etc.
 * 
 * Part of the Uppidy Web Services API
 * 
 * @author arudnev@uppidy.com
 */
public class ApiLocation {

	private double latitude;
	private double longitude;
		
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "{ \"latitude\":" + latitude + ", \"longitude\":" + longitude + " }";
	}

	@Override
	public int hashCode() {
		long lat = Double.doubleToLongBits(latitude);
		long lon = Double.doubleToLongBits(longitude);
		return (int)(lat^(lat>>>32)) +(int)(lon^(lon>>>32));
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if(obj != null && obj instanceof ApiLocation) {
			ApiLocation other = (ApiLocation) obj;
			return other.latitude == latitude && other.longitude == longitude;
		}
		return false;
	}
}
