package com.weilay.pos.app;

import java.util.HashMap;
import java.util.Map;

public class TicketColor {
	static Map<String, String> colorMap = null;
	public static String getcolor(String color) {
		if (colorMap == null) {
			colorMap = new HashMap<String, String>();
			colorMap.put("Color010", "#63b359");
			colorMap.put("Color020", "#2c9f67");
			colorMap.put("Color030", "#509fc9");
			colorMap.put("Color040", "#5885cf");
			colorMap.put("Color050", "#9062c0");
			colorMap.put("Color060", "#d09a45");
			colorMap.put("Color070", "#e4b138");
			colorMap.put("Color080", "#ee903c");
			colorMap.put("Color081", "#f08500");
			colorMap.put("Color082", "#a9d92d");
			colorMap.put("Color090", "#dd6549");
			colorMap.put("Color100", "#cc463d");
			colorMap.put("Color101", "#cf3e36");
			colorMap.put("Color102", "#5E6671");
		}
		if (colorMap.containsKey(color)) {
			return colorMap.get(color);
		}

		return null;

	}
}
