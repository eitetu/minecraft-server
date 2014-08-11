package com.eitetu.minecraft.server.cache;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import com.eitetu.minecraft.server.MinecraftServer;
import com.eitetu.minecraft.server.authlib.GameProfile;
import com.google.common.base.Charsets;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.IOUtils;

public class UserCache {
	public static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
	private final Map c = Maps.newHashMap();
	private final Map d = Maps.newHashMap();
	private final LinkedList e = Lists.newLinkedList();
	private final MinecraftServer minecraftServer;
	protected final Gson gson;
	private final File file;
	private static final ParameterizedType h = new UserCacheEntryType();

	public UserCache(MinecraftServer minecraftServer, File file) {
		this.minecraftServer = minecraftServer;
		this.file = file;

		GsonBuilder gsonBuilder = new GsonBuilder();

		gsonBuilder.registerTypeHierarchyAdapter(UserCacheEntry.class, new BanEntrySerializer(this, null));

		this.gson = gsonBuilder.create();

		b();
	}

	private static GameProfile a(com.eitetu.minecraft.server.MinecraftServer minecraftServer,
			String paramString) {
		GameProfile[] gameProfiles = new GameProfile[1];
		GameProfileLookup gameProfileLookup = new GameProfileLookup(gameProfiles);

		paramMinecraftServer.getGameProfileRepository().findProfilesByNames(
				new String[] { paramString }, Agent.MINECRAFT,
				localGameProfileLookup);
		if ((!(paramMinecraftServer.getOnlineMode()))
				&& (arrayOfGameProfile[0] == null)) {
			UUID localUUID = EntityHuman.a(new GameProfile(null, paramString));
			GameProfile localGameProfile = new GameProfile(localUUID,
					paramString);
			localGameProfileLookup.onProfileLookupSucceeded(localGameProfile);
		}
		return arrayOfGameProfile[0];
	}

	public void a(GameProfile paramGameProfile) {
		a(paramGameProfile, null);
	}

	private void a(GameProfile gameProfile, Date date) {
		UUID localUUID = gameProfile.getId();
		if (date == null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.MONTH, 1);
			date = calendar.getTime();
		}

		String name = gameProfile.getName().toLowerCase(Locale.ROOT);

		UserCacheEntry userCacheEntry = new UserCacheEntry(this, gameProfile, date, null);

		synchronized (this.e) {
			if (this.d.containsKey(localUUID)) {
				UserCacheEntry localUserCacheEntry2 = (UserCacheEntry) this.d
						.get(localUUID);
				this.c.remove(localUserCacheEntry2.a().getName()
						.toLowerCase(Locale.ROOT));
				this.c.put(paramGameProfile.getName().toLowerCase(Locale.ROOT),
						localUserCacheEntry1);
				this.e.remove(paramGameProfile);
			} else {
				this.d.put(localUUID, localUserCacheEntry1);
				this.c.put(localObject1, localUserCacheEntry1);
			}
			this.e.addFirst(paramGameProfile);
		}
	}

	public GameProfile a(String paramString) {
    String str = paramString.toLowerCase(Locale.ROOT);
    UserCacheEntry localUserCacheEntry = (UserCacheEntry)this.c.get(str);

    if ((localUserCacheEntry != null) && (new Date().getTime() >= UserCacheEntry.a(localUserCacheEntry).getTime())) {
      this.d.remove(localUserCacheEntry.a().getId());
      this.c.remove(localUserCacheEntry.a().getName().toLowerCase(Locale.ROOT));
      synchronized (this.e) {
        this.e.remove(localUserCacheEntry.a());
      }
      localUserCacheEntry = null;
    }

    if (localUserCacheEntry != null)
    {
      ??? = localUserCacheEntry.a();
      synchronized (this.e) {
        this.e.remove(???);
        this.e.addFirst(???);
      }
    } else {
      ??? = a(this.f, str);
      if (??? != null) {
        a((GameProfile)???);
        localUserCacheEntry = (UserCacheEntry)this.c.get(str);
      }
    }
    c();
    return ((localUserCacheEntry == null) ? null : (GameProfile)(GameProfile)localUserCacheEntry.a());
  }

	public String[] a() {
		ArrayList localArrayList = Lists.newArrayList(this.c.keySet());
		return ((String[]) localArrayList.toArray(new String[localArrayList
				.size()]));
	}

	public GameProfile a(UUID paramUUID) {
		UserCacheEntry localUserCacheEntry = (UserCacheEntry) this.d
				.get(paramUUID);
		return ((localUserCacheEntry == null) ? null : localUserCacheEntry.a());
	}

	private UserCacheEntry b(UUID paramUUID) {
		UserCacheEntry localUserCacheEntry = (UserCacheEntry) this.d
				.get(paramUUID);
		if (localUserCacheEntry != null) {
			GameProfile localGameProfile = localUserCacheEntry.a();
			synchronized (this.e) {
				this.e.remove(localGameProfile);
				this.e.addFirst(localGameProfile);
			}
		}
		return localUserCacheEntry;
	}

	public void b() {
    List localList = null;
    BufferedReader localBufferedReader = null;
    try {
      localBufferedReader = Files.newReader(this.g, Charsets.UTF_8);
      localList = (List)this.b.fromJson(localBufferedReader, h);
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      return; } finally { IOUtils.closeQuietly(localBufferedReader);
    }
    if (localList != null) {
      this.c.clear();
      this.d.clear();
      synchronized (this.e) {
        this.e.clear();
      }

      localList = Lists.reverse(localList);
      for (??? = localList.iterator(); ((Iterator)???).hasNext(); ) { UserCacheEntry localUserCacheEntry = (UserCacheEntry)((Iterator)???).next();
        if (localUserCacheEntry != null)
          a(localUserCacheEntry.a(), localUserCacheEntry.b());
      }
    }
  }

	public void c() {
		String str = this.b.toJson(a(1000));
		BufferedWriter localBufferedWriter = null;
		try {
			localBufferedWriter = Files.newWriter(this.g, Charsets.UTF_8);
			localBufferedWriter.write(str);
		} catch (FileNotFoundException localFileNotFoundException) {
			return;
		} catch (IOException localIOException) {
			return;
		} finally {
			IOUtils.closeQuietly(localBufferedWriter);
		}
	}

	private List a(int paramInt) {
    ArrayList localArrayList1 = Lists.newArrayList();
    ArrayList localArrayList2;
    synchronized (this.e) {
      localArrayList2 = Lists.newArrayList(Iterators.limit(this.e.iterator(), paramInt));
    }
    for (??? = localArrayList2.iterator(); ((Iterator)???).hasNext(); ) { GameProfile localGameProfile = (GameProfile)((Iterator)???).next();
      UserCacheEntry localUserCacheEntry = b(localGameProfile.getId());
      if (localUserCacheEntry == null) {
        continue;
      }
      localArrayList1.add(localUserCacheEntry);
    }
    return ((List)localArrayList1);
  }
}
