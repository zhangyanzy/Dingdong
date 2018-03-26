package com.dindong.dingdong.util;

import java.util.ArrayList;
import java.util.List;

import com.dindong.dingdong.manager.StorageMgr;
import com.dindong.dingdong.network.bean.entity.Region;
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
  public static void add(Region region) {
    List<Region> regions = getLocalRegion();
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
  public static List<Region> getLocalRegion() {
    if (TextUtils.isEmpty(StorageMgr.get(key)))
      return new ArrayList<Region>();
    return GsonUtil.parse(StorageMgr.get(key), new TypeToken<List<Region>>() {
    }.getType());
  }

  private static void setLocalList(List<Region> list) {
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
  private static int validateLocalData(Region currentRegion, List<Region> regions) {
    int index = -1;
    for (int i = 0; i < regions.size(); i++) {
      if (regions.get(i).getText().equals(currentRegion.getText())) {
        index = i;
        break;
      }
    }
    return index;
  }
}
