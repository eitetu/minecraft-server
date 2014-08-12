package com.eitetu.minecraft.server.cache;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

final class UserCacheEntryType implements ParameterizedType {
	public Type[] getActualTypeArguments() {
		return new Type[] { UserCacheEntry.class };
	}

	public Type getRawType() {
		return List.class;
	}

	public Type getOwnerType() {
		return null;
	}
}