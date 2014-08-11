package com.eitetu.minecraft.server.cache;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import com.eitetu.minecraft.server.authlib.GameProfile;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

class BanEntrySerializer implements JsonDeserializer<UserCacheEntry>, JsonSerializer<UserCacheEntry> {
	public JsonElement serialize(UserCacheEntry userCacheEntry, Type paramType, JsonSerializationContext paramJsonSerializationContext) {
		JsonObject localJsonObject = new JsonObject();
		localJsonObject.addProperty("name", userCacheEntry.getGameProfile().getName());
		UUID localUUID = userCacheEntry.getGameProfile().getId();
		localJsonObject.addProperty("uuid", (localUUID == null) ? "" : localUUID.toString());
		localJsonObject.addProperty("expiresOn", UserCache.dateFormatter.format(userCacheEntry.getDate()));
		return localJsonObject;
	}

	public UserCacheEntry deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
		if (jsonElement.isJsonObject()) {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			JsonElement nameElement = jsonObject.get("name");
			JsonElement uuidElement = jsonObject.get("uuid");
			JsonElement expiresOnElement = jsonObject.get("expiresOn");
			if ((nameElement == null) || (nameElement == null)) {
				return null;
			}
			String uuidStr = uuidElement.getAsString();
			String nameStr = nameElement.getAsString();
			Date localDate = null;
			if (expiresOnElement != null) {
				try {
					localDate = UserCache.dateFormatter.parse(expiresOnElement.getAsString());
				} catch (ParseException localParseException) {
					localDate = null;
				}
			}
			if ((uuidStr == null) || (nameStr == null))
				return null;
			UUID uuid;
			try {
				uuid = UUID.fromString(uuidStr);
			} catch (Throwable localThrowable) {
				return null;
			}
			UserCacheEntry userCacheEntry = new UserCacheEntry(new GameProfile(uuid, nameStr), localDate);
			return userCacheEntry;
		}
		return null;
	}
}