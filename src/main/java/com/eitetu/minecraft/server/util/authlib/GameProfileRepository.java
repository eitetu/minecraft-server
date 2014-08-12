package com.eitetu.minecraft.server.util.authlib;

public abstract interface GameProfileRepository {
	public abstract void findProfilesByNames(String[] names, Agent agent, ProfileLookupCallback profileLookupCallback);
}
