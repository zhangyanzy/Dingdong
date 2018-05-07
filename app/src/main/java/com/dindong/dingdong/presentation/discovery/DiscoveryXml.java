package com.dindong.dingdong.presentation.discovery;

import com.dindong.dingdong.DDApp;
import com.dindong.dingdong.R;

import android.graphics.drawable.Drawable;

/**
 * Created by wcong on 2018/5/6.
 * <p>
 * </>
 */

public class DiscoveryXml {

  public static Drawable getPraiseDrawable(boolean isPraise) {
    return DDApp.getInstance().getResources()
        .getDrawable(isPraise ? R.mipmap.ic_zan : R.mipmap.ic_zan_line);
  }
}
