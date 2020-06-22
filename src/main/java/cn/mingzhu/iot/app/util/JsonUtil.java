package cn.mingzhu.iot.app.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {
	public static <T> String toString(T object) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
		return gson.toJson(object, object.getClass());
	}
}
