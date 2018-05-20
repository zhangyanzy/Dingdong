package com.dindong.dingdong.util;

import com.dindong.dingdong.R;
import com.dindong.dingdong.widget.sweetAlert.SweetAlertDialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by wcong on 2018/5/20.
 * <p>
 * </>
 */

public class PhoneUtil {

  /**
   * 打电话
   * 
   * @param context
   * @param mobile
   */
  public static void call(final Context context, final String mobile) {
    SweetAlertDialog dialog = DialogUtil.getConfirmDialog(context, mobile);
    dialog.setConfirmText(context.getString(R.string.shop_main_mobile));
    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
      @Override
      public void onClick(SweetAlertDialog sweetAlertDialog) {
        try {
          context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile)));
        } catch (Exception e) {
          e.printStackTrace();
          ToastUtil.toastFailure(context, context.getString(R.string.shop_main_mobile_err));
        }
        sweetAlertDialog.dismiss();
      }
    });
    dialog.setCanceledOnTouchOutside(true);
    dialog.show();
  }
}
