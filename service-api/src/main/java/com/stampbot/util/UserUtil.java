package com.stampbot.util;

import com.google.common.collect.Maps;
import com.stampbot.domain.UserIntent;
import org.symphonyoss.symphony.clients.model.SymUser;

import java.util.Map;

public class UserUtil {

	private static final Map<Long, SymUser> userBaseMap = Maps.newConcurrentMap();

	private static final Map<Long, UserIntent> userIntentMap = Maps.newHashMap();

	public static void addUser(SymUser user){
		userBaseMap.put(user.getId(), user);
	}

	public static SymUser getUser(Long id){
		return userBaseMap.get(id);
	}

	public static UserIntent getIntent(Long id) {
		return userIntentMap.get(id);
	}

	public static void putIntent(Long id, UserIntent userIntent) {
		userIntentMap.put(id, userIntent);
	}
}
