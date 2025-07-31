package com.sonfind.chelsea.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class StringListConverter {

	private static final String DELIMITER = ",";

	private StringListConverter() {
	}

	public static String listToString(List<String> stringList) {
		if (stringList == null || stringList.isEmpty()) {
			return "";
		}
		return String.join(DELIMITER, stringList);
	}

	public static List<String> stringToList(String str) {
		if (str == null || str.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		return Arrays.asList(str.split(DELIMITER));
	}

}
