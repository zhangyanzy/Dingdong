package com.dindong.dingdong.util;

import com.dindong.dingdong.R;
import com.dindong.dingdong.widget.EnterInfoDialog;
import com.dindong.dingdong.widget.sweetAlert.SweetAlertDialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;

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

  public static Dialog getEnterDialog(Context context, String title, String hint,
      String defaultValue, EnterInfoDialog.OnConfirmListener listener) {

    return getEnterDialog(context, title, hint, defaultValue, -1, -1, listener);
  }

  public static Dialog getEnterDialog(Context context, String title, String hint,
      String defaultValue, int length, int inputType, EnterInfoDialog.OnConfirmListener listener) {
    EnterInfoDialog dialog = new EnterInfoDialog(context, title, listener);
    dialog.setHint(hint);
    dialog.setDefaultValue(defaultValue);
    if (length > 0)
      dialog.setEnterLength(length);
    if (inputType > -1)
      dialog.setInputType(inputType);
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
    String confirm = context.getString(R.string.confirm);
    return getCustomDialog(context, title, "", "", confirm, 0);
  }

  public static SweetAlertDialog getNoContentDialog(Context context, String title) {
    String confirm = context.getString(R.string.confirm);
    String cancel = context.getString(R.string.cancel);
    return getCustomDialog(context, title, "", cancel, confirm, 0);
  }

  public static SweetAlertDialog getCustomDialog(Context context, String title, String content,
      String cancel, String confirm, int resourceId) {
    SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
    dialog.setTitleText(title).setContentText(content).setCancelText(cancel).setConfirmText(confirm)
        .setCustomImage(resourceId);
    return dialog;
  }

}
