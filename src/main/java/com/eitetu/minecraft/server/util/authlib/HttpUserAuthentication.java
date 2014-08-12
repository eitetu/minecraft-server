package com.eitetu.minecraft.server.util.authlib;

public abstract class HttpUserAuthentication extends BaseUserAuthentication {
	protected HttpUserAuthentication(HttpAuthenticationService authenticationService) {
		super((AuthenticationService)authenticationService);
	}

	public HttpAuthenticationService getHttpAuthenticationService() {
		return (HttpAuthenticationService)super.getAuthenticationService();
	}
}
