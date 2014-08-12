package com.eitetu.minecraft.server.util.authlib.minecraft;

import com.eitetu.minecraft.server.util.authlib.AuthenticationService;
import com.eitetu.minecraft.server.util.authlib.HttpAuthenticationService;

public abstract class HttpMinecraftSessionService extends BaseMinecraftSessionService {
	protected HttpMinecraftSessionService(HttpAuthenticationService authenticationService) {
		super((AuthenticationService) authenticationService);
	}

	public HttpAuthenticationService getHttpAuthenticationService() {
		return (HttpAuthenticationService)super.getAuthenticationService();
	}
}
