package com.eitetu.minecraft.server.util.authlib.yggdrasil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Proxy;
import java.net.URL;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.eitetu.minecraft.server.util.authlib.Agent;
import com.eitetu.minecraft.server.util.authlib.GameProfile;
import com.eitetu.minecraft.server.util.authlib.GameProfileRepository;
import com.eitetu.minecraft.server.util.authlib.UserAuthentication;
import com.eitetu.minecraft.server.util.authlib.HttpAuthenticationService;
import com.eitetu.minecraft.server.util.authlib.exceptions.AuthenticationException;
import com.eitetu.minecraft.server.util.authlib.exceptions.AuthenticationUnavailableException;
import com.eitetu.minecraft.server.util.authlib.exceptions.InvalidCredentialsException;
import com.eitetu.minecraft.server.util.authlib.exceptions.UserMigratedException;
import com.eitetu.minecraft.server.util.authlib.minecraft.MinecraftSessionService;
import com.eitetu.minecraft.server.util.authlib.properties.PropertyMap;
import com.eitetu.minecraft.server.util.authlib.yggdrasil.YggdrasilGameProfileRepository;
import com.eitetu.minecraft.server.util.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import com.eitetu.minecraft.server.util.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.eitetu.minecraft.server.util.authlib.yggdrasil.response.ProfileSearchResultsResponse;
import com.eitetu.minecraft.server.util.authlib.yggdrasil.response.Response;
import com.eitetu.minecraft.server.util.UUIDTypeAdapter;
import org.apache.commons.lang3.StringUtils;

public class YggdrasilAuthenticationService extends HttpAuthenticationService {
	private final String clientToken;
	private final Gson gson;

	public YggdrasilAuthenticationService(Proxy proxy, String clientToken) {
		super(proxy);
		this.clientToken = clientToken;
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(GameProfile.class, new GameProfileSerializer());
		builder.registerTypeAdapter(PropertyMap.class, new PropertyMap.Serializer());
		builder.registerTypeAdapter(UUID.class, new UUIDTypeAdapter());
		builder.registerTypeAdapter(ProfileSearchResultsResponse.class, new ProfileSearchResultsResponse.Serializer());
		this.gson = builder.create();
	}

	public UserAuthentication createUserAuthentication(Agent agent) {
		return new YggdrasilUserAuthentication(this, agent);
	}

	public MinecraftSessionService createMinecraftSessionService() {
		return new YggdrasilMinecraftSessionService(this);
	}

	public GameProfileRepository createProfileRepository() {
		return new YggdrasilGameProfileRepository(this);
	}

	protected <T extends Response> T makeRequest(URL url, Object input, Class<T> clazz) throws AuthenticationException {
		try {
			String jsonResult = (input == null) ? performGetRequest(url)
					: performPostRequest(url, this.gson.toJson(input),
							"application/json");
			T result = (T) this.gson.fromJson(jsonResult, clazz);

			if (result == null)
				return null;

			if (StringUtils.isNotBlank(result.getError())) {
				if ("UserMigratedException".equals(result.getCause()))
					throw new UserMigratedException(result.getErrorMessage());
				if (result.getError().equals("ForbiddenOperationException")) {
					throw new InvalidCredentialsException(
							result.getErrorMessage());
				}
				throw new AuthenticationException(result.getErrorMessage());
			}

			return result;
		} catch (IOException e) {
			throw new AuthenticationUnavailableException(
					"Cannot contact authentication server", e);
		} catch (IllegalStateException e) {
			throw new AuthenticationUnavailableException(
					"Cannot contact authentication server", e);
		} catch (JsonParseException e) {
			throw new AuthenticationUnavailableException(
					"Cannot contact authentication server", e);
		}
	}

	public String getClientToken() {
		return this.clientToken;
	}

	private static class GameProfileSerializer implements JsonSerializer<GameProfile>, JsonDeserializer<GameProfile> {
		public GameProfile deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject object = (JsonObject) json;
			UUID id = (object.has("id")) ? (UUID) context.deserialize(object.get("id"), UUID.class) : null;
			String name = (object.has("name")) ? object.getAsJsonPrimitive("name").getAsString() : null;
			return new GameProfile(id, name);
		}

		public JsonElement serialize(GameProfile src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject result = new JsonObject();
			if (src.getId() != null)
				result.add("id", context.serialize(src.getId()));
			if (src.getName() != null)
				result.addProperty("name", src.getName());
			return result;
		}
	}
}