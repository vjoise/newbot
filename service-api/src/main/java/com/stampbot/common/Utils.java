package com.stampbot.common;

public class Utils {

	public static void trySafe(MethodTrier methodTrier, boolean throwBack){
		try {
			methodTrier.wrap();
		} catch (Exception e) {
			if(throwBack)
				throw new RuntimeException(e);
		}
	}

}
