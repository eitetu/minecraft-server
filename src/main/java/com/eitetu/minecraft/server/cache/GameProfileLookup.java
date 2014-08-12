package com.eitetu.minecraft.server.cache;

import com.eitetu.minecraft.server.authlib.GameProfile;
import com.eitetu.minecraft.server.authlib.ProfileLookupCallback;

public class GameProfileLookup implements ProfileLookupCallback {
	private GameProfile[] gameProfiles;

	public GameProfileLookup() {
		this.gameProfiles = new GameProfile[0];
	}

	public GameProfileLookup(GameProfile[] gameProfiles) {
		this.gameProfiles = gameProfiles;
	}

	public void onProfileLookupSucceeded(GameProfile gameProfile) {
		this.gameProfiles[0] = gameProfile;
	}

	public void onProfileLookupFailed(GameProfile gameProfile, Exception paramException) {
		this.gameProfiles[0] = null;
	}
}
