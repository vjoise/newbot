package com.stampbot.lang.domain;

import lombok.Data;

@Data
public class LangFeature {

	private String name;

	private String desc;

	private Boolean isPresent = false;

}
