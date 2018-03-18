package com.dindong.dingdong.presentation.shop;

import com.dindong.dingdong.DDApp;
import com.dindong.dingdong.R;
import com.dindong.dingdong.network.bean.shop.SubjectType;

import android.graphics.drawable.Drawable;

/**
 * Created by wcong on 2018/3/18.
 * <p>
 * </>
 */

public class ShopXml {

  /**
   * 课程类型背景色
   * 
   * @param type
   * @return
   */
  public static Drawable getSubjectBg(SubjectType type) {
    if (type.equals(SubjectType.GROUP))
      return DDApp.getInstance().getResources().getDrawable(R.drawable.bg_subject_group);
    else if (type.equals(SubjectType.NORMAL))
      return DDApp.getInstance().getResources().getDrawable(R.drawable.bg_subject_normal);
    return DDApp.getInstance().getResources().getDrawable(R.drawable.bg_subject_normal);
  }

  /**
   * 课程类型文本样式
   * 
   * @param type
   * @return
   */
  public static String getSubjectStr(SubjectType type) {
    if (type.equals(SubjectType.GROUP))
      return DDApp.getInstance().getString(R.string.shop_list_group);
    else if (type.equals(SubjectType.NORMAL))
      return DDApp.getInstance().getString(R.string.shop_list_normal);
    return DDApp.getInstance().getString(R.string.shop_list_normal);
  }
}
