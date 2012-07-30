package com.uppidy.android.sdk.social.api.impl;

import org.springframework.social.MissingAuthorizationException;

class AbstractUppidyOperations {
	
	private final boolean isAuthorized;

	public AbstractUppidyOperations(boolean isAuthorized) {
		this.isAuthorized = isAuthorized;
	}
	
	protected void requireAuthorization() {
		if (!isAuthorized) {
			throw new MissingAuthorizationException();
		}
	}
	
}
