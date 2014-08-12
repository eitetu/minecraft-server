package com.eitetu.minecraft.server.util.authlib;

import java.util.HashMap;
import java.util.Map;

public enum UserType {
	LEGACY("LEGACY"), MOJANG("MOJANG");

	private static final Map<String, UserType> BY_NAME;
	private final String name;

	public static UserType byName(String name) {
		return BY_NAME.get(name.toLowerCase());
	}

	public String getName() {
		return this.name;
	}

	UserType(String name) {
		this.name = name;
	}

	static {
		BY_NAME = new HashMap<String, UserType>();

		for (UserType type : values())
			BY_NAME.put(type.name, type);
	}
}