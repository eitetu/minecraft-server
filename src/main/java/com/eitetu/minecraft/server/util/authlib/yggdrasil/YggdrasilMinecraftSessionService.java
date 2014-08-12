package com.eitetu.minecraft.server.util.authlib.yggdrasil;

import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.eitetu.minecraft.server.util.authlib.AuthenticationService;
import com.eitetu.minecraft.server.util.authlib.GameProfile;
import com.eitetu.minecraft.server.util.authlib.HttpAuthenticationService;
import com.eitetu.minecraft.server.util.authlib.exceptions.AuthenticationException;
import com.eitetu.minecraft.server.util.authlib.exceptions.AuthenticationUnavailableException;
import com.eitetu.minecraft.server.util.authlib.minecraft.HttpMinecraftSessionService;
import com.eitetu.minecraft.server.util.authlib.minecraft.InsecureTextureException;
import com.eitetu.minecraft.server.util.authlib.minecraft.MinecraftProfileTexture;
import com.eitetu.minecraft.server.util.authlib.properties.Property;
import com.eitetu.minecraft.server.util.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.eitetu.minecraft.server.util.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import com.eitetu.minecraft.server.util.authlib.yggdrasil.request.JoinMinecraftServerRequest;
import com.eitetu.minecraft.server.util.authlib.yggdrasil.response.HasJoinedMinecraftServerResponse;
import com.eitetu.minecraft.server.util.authlib.yggdrasil.response.MinecraftProfilePropertiesResponse;
import com.eitetu.minecraft.server.util.authlib.yggdrasil.response.MinecraftTexturesPayload;
import com.eitetu.minecraft.server.util.authlib.yggdrasil.response.Response;

import net.minecraft.util.com.mojang.util.UUIDTypeAdapter;
import net.minecraft.util.org.apache.commons.codec.Charsets;
import net.minecraft.util.org.apache.commons.codec.binary.Base64;
import net.minecraft.util.org.apache.commons.io.IOUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class YggdrasilMinecraftSessionService extends HttpMinecraftSessionService {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final String BASE_URL = "https://sessionserver.mojang.com/session/minecraft/";
	private static final URL JOIN_URL = HttpAuthenticationService.constantURL("https://sessionserver.mojang.com/session/minecraft/join");
	private static final URL CHECK_URL = HttpAuthenticationService.constantURL("https://sessionserver.mojang.com/session/minecraft/hasJoined");
	private final PublicKey publicKey;
	private final Gson gson = new GsonBuilder().registerTypeAdapter(UUID.class,
			new UUIDTypeAdapter()).create();

	protected YggdrasilMinecraftSessionService(YggdrasilAuthenticationService authenticationService) {
		super(authenticationService);

		try {
			X509EncodedKeySpec spec = new X509EncodedKeySpec(
					IOUtils.toByteArray(YggdrasilMinecraftSessionService.class
							.getResourceAsStream("/yggdrasil_session_pubkey.der")));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			this.publicKey = keyFactory.generatePublic(spec);
		} catch (Exception e) {
			throw new Error("Missing/invalid yggdrasil public key!");
		}
	}

	public void joinServer(GameProfile profile, String authenticationToken,
			String serverId) throws AuthenticationException {
		JoinMinecraftServerRequest request = new JoinMinecraftServerRequest();
		request.accessToken = authenticationToken;
		request.selectedProfile = profile.getId();
		request.serverId = serverId;

		getYggdrasilMinecraftSessionService().makeRequest(JOIN_URL, request, Response.class);
	}

	public GameProfile hasJoinedServer(GameProfile user, String serverId) throws AuthenticationUnavailableException {
		Map<String, Object> arguments = new HashMap<String, Object>();

		arguments.put("username", user.getName());
		arguments.put("serverId", serverId);

		URL url = HttpAuthenticationService.concatenateURL(CHECK_URL, HttpAuthenticationService.buildQuery(arguments));
		try {
			HasJoinedMinecraftServerResponse response = (HasJoinedMinecraftServerResponse) getYggdrasilMinecraftSessionService().makeRequest(url, null, HasJoinedMinecraftServerResponse.class);

			if ((response != null) && (response.getId() != null)) {
				GameProfile result = new GameProfile(response.getId(),
						user.getName());

				if (response.getProperties() != null) {
					result.getProperties().putAll(response.getProperties());
				}

				return result;
			}
			return null;
		} catch (AuthenticationUnavailableException e) {
			throw e;
		} catch (AuthenticationException e) {
		}
		return null;
	}

	public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getTextures(
			GameProfile profile, boolean requireSecure) {
		Property textureProperty = (Property) Iterables.getFirst(profile
				.getProperties().get("textures"), null);

		if (textureProperty == null) {
			return new HashMap<MinecraftProfileTexture.Type, MinecraftProfileTexture>();
		}

		if (requireSecure) {
			if (!(textureProperty.hasSignature())) {
				LOGGER.error("Signature is missing from textures payload");
				throw new InsecureTextureException(
						"Signature is missing from textures payload");
			}

			if (!(textureProperty.isSignatureValid(this.publicKey))) {
				LOGGER.error("Textures payload has been tampered with (signature invalid)");
				throw new InsecureTextureException(
						"Textures payload has been tampered with (signature invalid)");
			}
		}
		MinecraftTexturesPayload result;
		try {
			String json = new String(Base64.decodeBase64(textureProperty
					.getValue()), Charsets.UTF_8);
			result = (MinecraftTexturesPayload) this.gson.fromJson(json, MinecraftTexturesPayload.class);
		} catch (JsonParseException e) {
			LOGGER.error("Could not decode textures payload", e);
			return new HashMap<MinecraftProfileTexture.Type, MinecraftProfileTexture>();
		}

		return ((result.getTextures() == null) ? new HashMap<MinecraftProfileTexture.Type, MinecraftProfileTexture>() : result.getTextures());
	}

	public GameProfile fillProfileProperties(GameProfile profile,
			boolean requireSecure) {
		if (profile.getId() == null) {
			return profile;
		}
		try {
			URL url = HttpAuthenticationService.constantURL("https://sessionserver.mojang.com/session/minecraft/profile/" + UUIDTypeAdapter.fromUUID(profile.getId()));
			url = HttpAuthenticationService.concatenateURL(url, "unsigned=" + (!(requireSecure)));
			MinecraftProfilePropertiesResponse response = (MinecraftProfilePropertiesResponse) getYggdrasilMinecraftSessionService().makeRequest(url, null, MinecraftProfilePropertiesResponse.class);

			if (response == null) {
				LOGGER.debug("Couldn't fetch profile properties for " + profile
						+ " as the profile does not exist");
				return profile;
			}
			GameProfile result = new GameProfile(response.getId(), response.getName());
			result.getProperties().putAll(response.getProperties());
			profile.getProperties().putAll(response.getProperties());
			LOGGER.debug("Successfully fetched profile properties for "
					+ profile);
			return result;
		} catch (AuthenticationException e) {
			LOGGER.warn("Couldn't look up profile properties for " + profile, e);
		}
		return profile;
	}

	public YggdrasilAuthenticationService getYggdrasilMinecraftSessionService() {
		return (YggdrasilAuthenticationService)super.getHttpAuthenticationService();
	}
}
