package com.dindong.dingdong.util;

import java.util.ArrayList;
import java.util.List;

import com.dindong.dingdong.manager.StorageMgr;
import com.google.gson.reflect.TypeToken;

import android.text.TextUtils;

/**
 * Created by wcong on 2018/3/14.
 *
 * 访问城市管理类，用于本地存储及查看
 */

public class RegionStorageUtil {

  private static final String key = "login";

  /**
   * 添加历史访问城市
   * 
   * @param region
   */
  public static void add(String region) {
    List<String> regions = getLocalRegion();
    int index = validateLocalData(region, regions);
    if (index >= 0) {
      regions.remove(index);
      regions.add(index, region);
    } else
      regions.add(region);
    setLocalList(regions);
  }

  /**
   * 获取本地访问过的城市
   * 
   * @return
   */
  public static List<String> getLocalRegion() {
    if (TextUtils.isEmpty(StorageMgr.get(key)))
      return new ArrayList<String>();
    return GsonUtil.parse(StorageMgr.get(key), new TypeToken<List<String>>() {
    }.getType());
  }

  private static void setLocalList(List<String> list) {
    if (list == null || list.size() == 0) {
      StorageMgr.set(key, null);
      return;
    }
    StorageMgr.set(key, list);
  }

  /**
   * 校验目标城市是否存在
   * 
   * @param currentRegion
   * @param regions
   * @return -1为不存在
   */
  private static int validateLocalData(String currentRegion, List<String> regions) {
    int index = -1;
    for (int i = 0; i < regions.size(); i++) {
      if (regions.get(i).equals(currentRegion)) {
        index = i;
        break;
      }
    }
    return index;
  }
}
