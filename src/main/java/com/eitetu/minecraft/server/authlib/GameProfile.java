package com.eitetu.minecraft.server.authlib;

import java.util.UUID;

import com.eitetu.minecraft.server.authlib.properties.PropertyMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class GameProfile {
	private final UUID id;
	private final String name;
	private final PropertyMap properties = new PropertyMap();
	private boolean legacy;

	public GameProfile(UUID id, String name) {
		if ((id == null) && (StringUtils.isBlank(name)))
			throw new IllegalArgumentException(
					"Name and ID cannot both be blank");

		this.id = id;
		this.name = name;
	}

	public UUID getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public PropertyMap getProperties() {
		return this.properties;
	}

	public boolean isComplete() {
		return ((this.id != null) && (StringUtils.isNotBlank(getName())));
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if ((o == null) || (super.getClass() != o.getClass()))
			return false;

		GameProfile that = (GameProfile) o;

		if(this.id != null && this.name != null) {
			if(this.id.equals(that.id) && this.name.equals(that.name)) {
				return true;
			} else {
				return false;
			}
		}

		if(this.id != null) {
			if(this.id.equals(that.id)) {
				return true;
			} else {
				return false;
			}
		}

		if(this.name != null) {
			if(this.name.equals(that.name)) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

	public int hashCode() {
		int result = (this.id != null) ? this.id.hashCode() : 0;
		result = 31 * result + ((this.name != null) ? this.name.hashCode() : 0);
		return result;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", this.id)
				.append("name", this.name)
				.append("properties", this.properties)
				.append("legacy", this.legacy).toString();
	}

	public boolean isLegacy() {
		return this.legacy;
	}
}
