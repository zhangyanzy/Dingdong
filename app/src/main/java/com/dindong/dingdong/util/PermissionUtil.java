package com.dindong.dingdong.util;

import java.util.Arrays;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseFragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by wcong on 2018/3/29.
 * <p>
 * </>
 */

public class PermissionUtil {
  public static final int reqCode = 1;

  private static final String XIAO_MI = "Xiaomi";

  /**
   * 检查系统权限并注册
   *
   * @param activity
   * @param permissions
   *          权限
   * @param reqMsg
   *          注册时的提示信息
   * @param dialogMsg
   *          跳到系统设置时的提示信息
   */
  public static void check(Activity activity, String[] permissions, String reqMsg,
      String dialogMsg) {
    check(activity, permissions, reqCode, reqMsg, dialogMsg, null);
  }

  public static void check(Activity activity, String[] permissions, int reqCode, String reqMsg,
      String dialogMsg) {
    check(activity, permissions, reqCode, reqMsg, dialogMsg, null);
  }

  public static void check(Activity activity, String[] permissions, int reqCode, String reqMsg,
      String dialogMsg, @NonNull EasyPermissions.PermissionCallbacks permissionCallbacks) {
    EasyPermissions.PermissionCallbacks callback = null;
    if (permissionCallbacks != null) {
      callback = permissionCallbacks;
    } else if (activity instanceof EasyPermissions.PermissionCallbacks) {
      callback = (EasyPermissions.PermissionCallbacks) activity;
    }
    if (EasyPermissions.hasPermissions(activity, permissions)) {
      if (callback != null)
        callback.onPermissionsGranted(reqCode, Arrays.asList(permissions));
    } else {
      if (EasyPermissions.somePermissionPermanentlyDenied(activity, Arrays.asList(permissions))) {
        checkDevice(activity, permissions, reqCode, dialogMsg, callback);
      } else {
        EasyPermissions.requestPermissions(activity, reqMsg, reqCode, permissions);
      }
    }
  }

  /**
   * 检查系统权限并注册
   *
   * @param fragment
   * @param permissions
   *          权限
   * @param reqMsg
   *          注册时的提示信息
   * @param dialogMsg
   *          跳到系统设置时的提示信息
   */
  public static void check(BaseFragment fragment, String[] permissions, String reqMsg,
      String dialogMsg) {
    check(fragment, permissions, reqCode, reqMsg, dialogMsg);
  }

  public static void check(BaseFragment fragment, String[] permissions, int reqCode, String reqMsg,
      String dialogMsg) {
    if (fragment instanceof EasyPermissions.PermissionCallbacks) {
      EasyPermissions.PermissionCallbacks callback = (EasyPermissions.PermissionCallbacks) fragment;

      if (EasyPermissions.hasPermissions(fragment.getContext(), permissions)) {
        callback.onPermissionsGranted(reqCode, Arrays.asList(permissions));
      } else {
        if (EasyPermissions.somePermissionPermanentlyDenied(fragment, Arrays.asList(permissions))) {
          checkDevice(fragment.getActivity(), permissions, reqCode, dialogMsg, callback);
        } else {
          EasyPermissions.requestPermissions(fragment, reqMsg, reqCode, permissions);
        }
      }
    }
  }

  private static void checkDevice(Activity activity, String[] permissions, int reqCode, String msg,
      EasyPermissions.PermissionCallbacks listener) {
    if (Build.MANUFACTURER.contains(PermissionUtil.XIAO_MI)) {
      showPlanDialog(activity, "无手机权限，请到手机权限管\n理中添加权限");
    } else {
      getPermissionDialog(activity, permissions, reqCode, msg, listener).show();
    }
  }

  private static void showPlanDialog(Context context, String msg) {
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.dialog_permission, null);
    ((TextView) view.findViewById(R.id.msg)).setText(msg);
    final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(context,
        R.style.alert_dialog).create();
    dialog.show();
    dialog.setCanceledOnTouchOutside(false);
    dialog.getWindow().setContentView(view);
    dialog.getWindow().findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });
  }

  private static AppSettingsDialog getPermissionDialog(Activity activity,
      final String[] permissions, final int reqCode, String msg,
      final EasyPermissions.PermissionCallbacks listener) {
    return new AppSettingsDialog.Builder(activity, msg)
        .setTitle(activity.getString(R.string.permission_title))
        .setPositiveButton(activity.getString(R.string.permission_ok)).setNegativeButton(
            activity.getString(R.string.permission_cancel), new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                if (listener != null)
                  listener.onPermissionsDenied(reqCode, Arrays.asList(permissions));
                dialog.dismiss();
              }
            })
        .build();
  }
}
