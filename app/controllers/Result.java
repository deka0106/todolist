package controllers;

import java.util.HashMap;
import java.util.Map;

public class Result {

	private final Map<String, Object> map;
	public boolean ok;
	public String error;

	public Result() {
		map = new HashMap<>();
		ok = true;
	}

	public void put(String s, Object o) {
		map.put(s, o);
	}

	public void error(String s) {
		ok = false;
		error = s;
	}

	public Map<String, Object> getMap() {
		map.put("ok", ok);
		if (!ok) map.put("error", error);
		return map;
	}

}
