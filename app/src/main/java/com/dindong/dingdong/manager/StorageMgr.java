package com.dindong.dingdong.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.dindong.dingdong.network.bean.auth.User;
import com.dindong.dingdong.util.GsonUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcong on 2018/3/10.
 */

public class StorageMgr {
    private static SharedPreferences storage;
    public static String LEVEL_USER = "user";// 用户级别（必需登录后使用）
    public static String LEVEL_GLOBAL = "global";// 全局级别

    public static void init(Context context) {
        storage = context.getSharedPreferences("qianfan123", Context.MODE_PRIVATE);
    }

    // 设置缓存信息
    private static void setStorage(String key, String value) {
        SharedPreferences.Editor editor = storage.edit();
        editor.putString(key, value);
        editor.apply(); // 先提交内存，再异步提交硬盘
        // editor.commit(); //同步提交内存及硬盘
    }

    /**
     * 获取对应key的缓存
     *
     * @param key 键值
     * @param t   缓存的类
     * @return
     * @throws Exception
     */
    public static <T> void set(String key, T t) throws RuntimeException {
        set(key, GsonUtil.toJson(t), StorageMgr.LEVEL_USER);
    }

    /**
     * 获取对应key的缓存
     *
     * @param key   键值
     * @param t     缓存的类
     * @param level 缓存级别(用户，门店，全局）
     * @return
     * @throws Exception
     */
    public static <T> void set(String key, T t, String level) throws RuntimeException {
        set(key, GsonUtil.toJson(t), level);
    }

    /**
     * 设置key对应缓存
     *
     * @param key   键值
     * @param value 字符串
     * @return
     * @throws Exception
     */
    public static void set(String key, String value) throws RuntimeException {
        set(key, value, StorageMgr.LEVEL_USER);
    }

    /**
     * 设置key对应缓存
     *
     * @param key   键值
     * @param value 字符串
     * @param level 缓存级别(用户，门店，全局）
     * @return
     * @throws Exception
     */
    public static void set(String key, String value, String level) throws RuntimeException {
        User user = SessionMgr.getUser();
        String k = "";
        if (level.equals(StorageMgr.LEVEL_USER)) {
            if (!IsEmpty.object(user)) {
                k += user.getId();
            } else {
                throw new NullPointerException("用户为空");
            }
            k += "_";
        }
        k += key;
        setStorage(k, value);
    }

    // 获取缓存信息
    private static String getStorage(String key) {
        return storage.getString(key, null);
    }

    /**
     * 获取对应key的缓存
     *
     * @param key 键值
     * @param c   需要序列化的类
     * @return
     * @throws Exception
     */
    public static <T> T get(String key, Class<T> c) {
        String value = get(key, StorageMgr.LEVEL_USER);
        if (value == null) {
            return null;
        }
        return GsonUtil.parse(value, c);
    }

    /**
     * 获取对应key的缓存
     *
     * @param key   键值
     * @param c     需要序列化的类
     * @param level 缓存级别(用户，门店，全局）
     * @return
     * @throws Exception
     */
    public static <T> T get(String key, Class<T> c, String level) {
        String value = get(key, level);
        if (value == null) {
            return null;
        }
        return GsonUtil.parse(value, c);
    }

    /**
     * 获取对应key的缓存
     *
     * @param key 键值
     * @throws Exception
     */
    public static String get(String key) {
        return get(key, StorageMgr.LEVEL_USER);
    }

    /**
     * 获取对应key的缓存
     *
     * @param key   键值
     * @param level 缓存级别(用户，门店，全局）
     * @throws Exception
     */
    public static String get(String key, String level) {
        User user = SessionMgr.getUser();
        String k = "";
        if (level.equals(StorageMgr.LEVEL_USER)) {
            if (!IsEmpty.object(user)) {
                k += user.getId();
            }
            k += "_";
        }
        k += key;
        return getStorage(k);
    }

    /**
     * 获取对应key的缓存
     *
     * @param key      键值
     * @param level    缓存级别(用户，门店，全局）
     * @param defValue 默认值
     */
    public static Boolean getBoolean(String key, String level, boolean defValue) {
        String temp = get(key, level);
        return temp == null ? defValue : Boolean.valueOf(temp);
    }

    /**
     * 获取对应key的缓存(List类型)
     *
     * @param key   键值
     * @param c     需要序列化的类
     * @param level 缓存级别(用户，门店，全局）
     * @return
     */
    public static <T> List<T> getList(String key, Class<T> c, String level) {
        String value = get(key, level);
        if (value == null) {
            return null;
        }
        List<T> list = new ArrayList<>();
        JsonArray array = new JsonParser().parse(value).getAsJsonArray();
        for (JsonElement elem : array) {
            list.add(GsonUtil.parse(elem.toString(), c));
        }
        return list;
    }
}
