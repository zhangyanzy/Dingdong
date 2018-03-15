package com.dindong.dingdong.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.dindong.dingdong.R;
import com.dindong.dingdong.widget.sweetAlert.SweetAlertDialog;


/**
 * Created by wangcong on 2018/3/9.
 */
public class DialogUtil {

  // 获取对话框
  public static SweetAlertDialog getProgressDialog(Context context) {
    SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
    dialog.setTitleText(context.getResources().getString(R.string.load_data));
    // dialog.getProgressHelper().setBarColor(ContextCompat.getColor(context,
    // R.color.theme_opaque));// colorPrimary
    dialog.setCancelable(false);
    return dialog;
  }

  public static SweetAlertDialog getProgressDialog(Context context, int stringResourceId) {
    SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
    dialog.setTitleText(context.getResources().getString(stringResourceId));
    dialog.getProgressHelper().setBarColor(ContextCompat.getColor(context, R.color.colorPrimary));
    return dialog;
  }

  public static SweetAlertDialog getProgressDialog(Context context, String string) {
    SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
    dialog.setTitleText(string);
    dialog.getProgressHelper().setBarColor(ContextCompat.getColor(context, R.color.colorPrimary));
    return dialog;
  }

  public static SweetAlertDialog getErrorDialog(Context context, String string) {
    SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
    dialog.setConfirmText(context.getString(R.string.ok));
    // dialog.setTitleText(string);
    dialog.setContentText(string);
    return dialog;
  }

  public static SweetAlertDialog getConfirmDialog(Context context, String title) {
    final SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
    dialog.setTitleText(title);
    dialog.setConfirmText(context.getString(R.string.confirm));
    dialog.setCancelText(context.getString(R.string.cancel));
    dialog.showCancelButton(true);
    dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
      @Override
      public void onClick(SweetAlertDialog sweetAlertDialog) {
        dialog.dismiss();
      }
    });
    return dialog;
  }
}
