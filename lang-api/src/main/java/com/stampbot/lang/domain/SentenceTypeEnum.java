package com.stampbot.lang.domain;

public enum  SentenceTypeEnum {
	QUESTION("question"), STATEMENT("statement"), NONE("none");

	String internal;

	SentenceTypeEnum(String internal) {
		this.internal = internal;
	}

	public static SentenceTypeEnum byInternal(String internal){
		for (SentenceTypeEnum e : SentenceTypeEnum.values()){
			if(e.internal.equalsIgnoreCase(internal)){
				return e;
			}
		}
		return null;
	}
}
