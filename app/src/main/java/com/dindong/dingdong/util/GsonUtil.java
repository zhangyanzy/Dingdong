package com.dindong.dingdong.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by wangcong on 2018/3/9.
 */

public class GsonUtil {
    private static Gson gson;

    /**
     * 将json字符串转为 java对象
     */
    synchronized public static <T> T parse(String json, Class<T> classOfT) {
        return getGsonInstance(false).fromJson(json, classOfT);
    }

    /**
     * 将json字符串转为 java列表对象
     */
    synchronized public static <T> List<T> parse(String json, Type type) {
        return getGsonInstance(false).fromJson(json, type);
    }

    /**
     * 将obj对象转为json格式数据
     */
    synchronized public static String toJson(Object obj) {
        return getGsonInstance(false).toJson(obj);
    }

    /**
     * 获取gson实例
     */
    public static final Gson getGsonInstance(boolean isExplain) {
        if (!isExplain) {
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").registerTypeAdapter(DateTime.class, new DateTimeAdapter()).create();

        } else {
            gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(DateTime.class, new DateTimeAdapter()).create();
        }
        return gson;
    }

    /**
     * 获取gson实例(排除了FINAL、TRANSIENT、STATIC)
     */
    public static final Gson getGsonInstance() {
        gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .serializeNulls().create();
        return gson;
    }

    public static class DateTimeAdapter implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {


        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        @Override
        public DateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (!(jsonElement instanceof JsonPrimitive)) {
                throw new JsonParseException("The data should be a string value");
            }
            return format.parseDateTime(jsonElement.getAsString());


        }

        @Override
        public JsonElement serialize(DateTime time, Type type, JsonSerializationContext jsonSerializationContext) {
            String dataFormatAsString = time.toString(format);
            return new JsonPrimitive(dataFormatAsString);
        }

    }
}
