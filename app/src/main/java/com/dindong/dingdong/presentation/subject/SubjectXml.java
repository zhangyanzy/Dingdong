package com.dindong.dingdong.presentation.subject;

import com.dindong.dingdong.DDApp;
import com.dindong.dingdong.R;
import com.dindong.dingdong.util.IsEmpty;

import android.graphics.drawable.Drawable;

/**
 * Created by wcong on 2018/5/20.
 * <p>
 * </>
 */

public class SubjectXml {
  /**
   * 根据团状态获取拼团按钮颜色
   * 
   * @param state
   * @return
   */
  public static Drawable getGroupBtnBg(String state) {
    if (IsEmpty.string(state))
      return DDApp.getInstance().getResources().getDrawable(R.drawable.btn_gradient_corner_blue);
    if (state.equals("grouping"))
      return DDApp.getInstance().getResources().getDrawable(R.drawable.btn_gradient_corner_blue);
    if (state.equals("ready"))
      return DDApp.getInstance().getResources().getDrawable(R.drawable.bg_gradient_corner_orange);
    return DDApp.getInstance().getResources().getDrawable(R.drawable.btn_gradient_corner_blue);
  }

  /**
   * 根据团状态获取拼团按钮文本
   * 
   * @param state
   * @return
   */
  public static String getGroupBtnTxt(String state) {
    if (IsEmpty.string(state))
      return "去参团";
    if (state.equals("grouping"))
      return "去参团";
    if (state.equals("ready"))
      return "再加入";
    return "去参团";
  }
}
