package com.stampbot.common;

public class Utils {

	public static void trySafe(MethodTrier methodTrier){
		try {
			methodTrier.wrap();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
