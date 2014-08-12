package com.eitetu.minecraft.server.util.authlib.yggdrasil.response;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.eitetu.minecraft.server.util.authlib.GameProfile;
import com.eitetu.minecraft.server.util.authlib.yggdrasil.response.ProfileSearchResultsResponse;
import com.eitetu.minecraft.server.util.authlib.yggdrasil.response.Response;

public class ProfileSearchResultsResponse extends Response {
	private GameProfile[] profiles;

	public GameProfile[] getProfiles() {
		return this.profiles;
	}

	public static class Serializer implements JsonDeserializer<ProfileSearchResultsResponse> {
		public ProfileSearchResultsResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			ProfileSearchResultsResponse result = new ProfileSearchResultsResponse();

			if (json instanceof JsonObject) {
				JsonObject object = (JsonObject)json;

				if (object.has("error")) {
					result.setError(object.getAsJsonPrimitive("error").getAsString());
				}

				if (object.has("errorMessage")) {
					result.setError(object.getAsJsonPrimitive("errorMessage").getAsString());
				}
				if (object.has("cause"))
					result.setError(object.getAsJsonPrimitive("cause").getAsString());
			} else {
				ProfileSearchResultsResponse.access$002(result, (GameProfile[])context.deserialize(json, GameProfile.class));
			}

			return result;
    }
	}
}
