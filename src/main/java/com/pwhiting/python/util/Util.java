package com.pwhiting.python.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class Util {

	public static List<String> getAsLines(InputStream in) {
		return new BufferedReader(new InputStreamReader(in)).lines().parallel().collect(Collectors.toList());
	}
	
	public static String getAsString(InputStream in) {
		return new BufferedReader(new InputStreamReader(in)).lines().parallel().collect(Collectors.joining("\n"));
	}
	
}
