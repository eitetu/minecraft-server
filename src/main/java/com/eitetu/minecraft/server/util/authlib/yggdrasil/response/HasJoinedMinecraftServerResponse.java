package com.eitetu.minecraft.server.util.authlib.yggdrasil.response;

import java.util.UUID;

import com.eitetu.minecraft.server.util.authlib.properties.PropertyMap;

public class HasJoinedMinecraftServerResponse extends Response {
	private UUID id;
	private PropertyMap properties;

	public UUID getId() {
		return this.id;
	}

	public PropertyMap getProperties() {
		return this.properties;
	}
}
