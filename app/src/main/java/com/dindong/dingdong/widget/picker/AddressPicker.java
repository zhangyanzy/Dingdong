package com.dindong.dingdong.widget.picker;

import java.util.ArrayList;
import java.util.List;

import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.region.usecase.GetRegionCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.Region;

import android.content.Context;

/**
 * Created by wangcong on 2018/3/28.
 *
 * 地址选择组件
 * <p>
 */

public class AddressPicker extends PickerDialog {
  private final int LEVEL_PROVINCE = 0;
  private final int LEVEL_CITY = 1;
  private final int LEVEL_DISTRICT = 2;

  private int tabSize;

  public AddressPicker(Context context, Region province, Region city, Region district) {
    super(context);
    tabSize = 4;
    if (province != null && city != null && district != null) {
      selectMapDatas.put(LEVEL_PROVINCE, province);
      selectMapDatas.put(LEVEL_CITY, city);
      selectMapDatas.put(LEVEL_DISTRICT, district);
    }

    init();
  }

  @Override
  public String getTitle() {
    return "所在地区";
  }

  @Override
  public int getTabSize() {
    return tabSize;
  }

  @Override
  public void query(final int level, String parentCode, final boolean auto) {

    String sLvl;
    if (level == LEVEL_PROVINCE) {
      sLvl = "province";
      parentCode = "";
    } else if (level == LEVEL_CITY) {
      sLvl = "city";
    } else if (level == LEVEL_DISTRICT) {
      sLvl = "district";
    } else {
      sLvl = null;
    }

    new GetRegionCase(parentCode).execute(new HttpSubscriber<List<Region>>() {
      @Override
      public void onFailure(String errorMsg, Response<List<Region>> response) {
        onRequestFailure();
      }

      @Override
      public void onSuccess(Response<List<Region>> response) {
        List<Region> result = new ArrayList<>();
        result.addAll(response.getData());
        onRequestSuccess(level, result, auto);
      }
    });
  }
}
