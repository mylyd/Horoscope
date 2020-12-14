package com.mobo.horoscope.common;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by dfwm on 2016/5/12.
 */
public class GsonUtils {

    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static JsonObject getJsonObject(JsonElement data, String memberName) {
        JsonElement element = data.getAsJsonObject().get(memberName);
        return element.getAsJsonObject();
    }

    public static boolean getBoolean(JsonElement data, String memberName) {
        JsonObject jsonObj = data.getAsJsonObject();
        return getBoolean(jsonObj, memberName);
    }

    public static String getString(JsonElement data, String memberName) {
        JsonObject jsonObj = data.getAsJsonObject();
        return getString(jsonObj, memberName);
    }

    public static String getString(JsonObject jsonObj, String memberName) {
        JsonElement element = jsonObj.get(memberName);
        if (element != null) {
            return element.getAsString();
        } else {
            return "";
        }
    }

    public static boolean getBoolean(JsonObject jsonObj, String memberName) {
        JsonElement element = jsonObj.get(memberName);
        if (element != null) {
            return element.getAsBoolean();
        } else {
            return false;
        }
    }

    public static String getString(JsonObject jsonObject, String key, String memberName) {
        JsonObject jsonObj = jsonObject.getAsJsonObject(key);
        return getString(jsonObj, memberName);
    }

    public static <T> T fromJsonString(String json, Class<T> classType) {
        return (T) new Gson().fromJson(json, classType);
    }

    public static <T> Map<String, T> jsonToMap(String json, Class<T> classType) {
        try {
            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> set = jsonObject.entrySet();
            Map<String, T> map = new HashMap<>(set.size());
            for (Map.Entry<String, JsonElement> me : set) {
                T t = new Gson().fromJson(me.getValue(), classType);
                map.put(me.getKey(), t);
            }
            return map;
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    public static <T> T fromJsonObject(JsonObject jsonObject, Class<T> classType) {
        return new Gson().fromJson(jsonObject, classType);
    }

    public static <T> T fromJsonObject(JsonObject jsonObject, String key, Class<T> classType) {
        JsonObject jsonObj = jsonObject.getAsJsonObject(key);
        return fromJsonObject(jsonObj, classType);
    }

    public static <T> List<T> fromJsonArray(JsonArray jsonArray, Class<T> classType) {
        List<T> list = new ArrayList<>();
        if (jsonArray != null) {
            for (JsonElement element : jsonArray) {
                T t = fromJsonObject(element.getAsJsonObject(), classType);
                list.add(t);
            }
        }
        return list;
    }

    public static <T> List<T> fromJsonArray(JsonElement element, String key, Class<T> classType) {
        JsonObject jsonObject = element.getAsJsonObject();
        return fromJsonArray(jsonObject, key, classType);
    }

    public static <T> List<T> fromJsonArray(JsonObject jsonObject, String key, Class<T> classType) {
        JsonArray jsonArr = jsonObject.getAsJsonArray(key);
        return fromJsonArray(jsonArr, classType);
    }

    public static List<String> fromJsonElement(JsonElement element) {
        List<String> list = new ArrayList<>();
        if (element.isJsonArray()) {
            JsonArray jsonArray = element.getAsJsonArray();
            if (jsonArray != null) {
                for (JsonElement e : jsonArray) {
                    list.add(e.getAsString());
                }
            }
        }
        return list;
    }

    public static <T> List<T> fromJsonArray(JsonArray jsonArray, Type type) {
        return new Gson().fromJson(jsonArray, type);
    }

    public static String toJson(Object obj) {
        return new Gson().toJson(obj);
    }
}
