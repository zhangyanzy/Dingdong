package com.dindong.dingdong.util;

import java.util.ArrayList;
import java.util.List;

import com.dindong.dingdong.widget.sweetAlert.SweetAlertDialog;
import com.tencent.tmassistantbase.common.download.TMAssistantDownloadTaskState;
import com.tencent.tmselfupdatesdk.ITMSelfUpdateListener;
import com.tencent.tmselfupdatesdk.TMSelfUpdateConst;
import com.tencent.tmselfupdatesdk.TMSelfUpdateManager;
import com.tencent.tmselfupdatesdk.YYBDownloadListener;
import com.tencent.tmselfupdatesdk.model.TMSelfUpdateUpdateInfo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by wcong on 2018/3/30.
 * <p>
 * 应用更新</>
 */

public class UpgradeUtil {
  public static final String marketPkg = "com.tencent.android.qqdownloader";

  private static UpgradeUtil instance;

  private Context context;

  private final String SELF_UPDATE_CHANNEL = "990483";

  public static UpgradeUtil getInstance() {
    if (instance == null)
      return instance = new UpgradeUtil();
    return instance;
  }

  /**
   * 检查更新
   */
  public void checkVersion(final Context context) {

    this.context = context;
    Bundle bundle = new Bundle();
    bundle.putString(TMSelfUpdateConst.BUNDLE_KEY_SCENE, "FOO");
    TMSelfUpdateManager.getInstance().init(context.getApplicationContext(), SELF_UPDATE_CHANNEL,
        mSelfUpdateListener, mDownloadYYBCallback, bundle);
    TMSelfUpdateManager.getInstance().checkSelfUpdate();// 检测更新
  }

  /**
   * 自更新任务监听，包括检查更新回调和下载状态回调
   */
  private ITMSelfUpdateListener mSelfUpdateListener = new ITMSelfUpdateListener() {
    /**
     * 更新时的下载状态回调
     * 
     * @param state
     *          状态码
     * @param errorCode
     *          错误码
     * @param errorMsg
     *          错误信息
     */
    @Override
    public void onDownloadAppStateChanged(int state, int errorCode, String errorMsg) {
      // TODO更新包下载状态变化的处理逻辑
    }

    /**
     * 点击普通更新时的下载进度回调
     * 
     * @param receiveDataLen
     *          已经接收的数据长度
     * @param totalDataLen
     *          全部需要接收的数据长度（如果无法获取目标文件的总长度，此参数返回-1）
     */
    @Override
    public void onDownloadAppProgressChanged(final long receiveDataLen, final long totalDataLen) {
      // TODO 更新包下载进度发生变化的处理逻辑
    }

    /**
     * 检查更新返回更新信息回调
     * 
     * @param tmSelfUpdateUpdateInfo
     *          更新信息结构体
     */
    @Override
    public void onUpdateInfoReceived(TMSelfUpdateUpdateInfo tmSelfUpdateUpdateInfo) {
      // TODO 收到更新信息的处理逻辑
      if (null != tmSelfUpdateUpdateInfo) {
        int state = tmSelfUpdateUpdateInfo.getStatus();
        if (state == TMSelfUpdateUpdateInfo.STATUS_CHECKUPDATE_FAILURE) {
          ToastUtil.toastHint(context, "检查更新失败");
          return;
        }

        switch (tmSelfUpdateUpdateInfo.getUpdateMethod()) {
        case TMSelfUpdateUpdateInfo.UpdateMethod_NoUpdate:
          Log.i(this.getClass().getSimpleName(), "无更新");
          // 无更新
          // ToastUtil.toastHint(context, "已是最新版本" +
          // tmSelfUpdateUpdateInfo.versioncode);
          break;
        default:
          if (TMSelfUpdateManager.getInstance()
              .checkYYBInstallState() == TMAssistantDownloadTaskState.ALREADY_INSTALLED
              && tmSelfUpdateUpdateInfo.getPatchSize() != 0) {

            DialogUtil.getConfirmDialog(context, "有新版本，请立即更新")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                  @Override
                  public void onClick(SweetAlertDialog sweetAlertDialog) {
                    TMSelfUpdateManager.getInstance().startSelfUpdate(true);
                  }
                }).show();
          } else {
            Uri uri = Uri.parse("http://app.qq.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
          }
          break;
        }
      }
    }
  };

  /**
   * 应用宝状态监听
   */
  private YYBDownloadListener mDownloadYYBCallback = new YYBDownloadListener() {
    /**
     * 预下载应用宝状态变化回调
     *
     * @param url
     *          指定任务的url
     * @param state
     *          下载状态: 取自 TMAssistantDownloadTaskState.DownloadSDKTaskState_*
     * @param errorCode
     *          错误码
     * @param errorMsg
     *          错误描述，有可能为null
     */
    @Override
    public void onDownloadYYBStateChanged(String url, int state, int errorCode, String errorMsg) {
      // TODO 应用宝下载状态变化的处理逻辑
    }

    /**
     * 预下载应用宝进度回调
     *
     * @param url
     *          当前任务的url
     * @param receiveDataLen
     *          已经接收的数据长度
     * @param totalDataLen
     *          全部需要接收的数据长度（如果无法获取目标文件的总长度，此参数返回 －1）
     */
    @Override
    public void onDownloadYYBProgressChanged(String url, long receiveDataLen, long totalDataLen) {
      // TODO 应用宝下载进度变化的处理逻辑
    }

    /**
     * 查询应用宝的下载状态：当用户调了查询应用宝状态时回调
     *
     * @param url
     *          当前任务的url
     * @param state
     *          下载状态: 取自 TMAssistantDownloadTaskState.DownloadSDKTaskState_*
     * @param receiveDataLen
     *          已经接收的数据长度
     * @param totalDataLen
     *          全部需要接收的数据长度（如果无法获取目标文件的总长度，此参数返回 －1）
     */
    @Override
    public void onCheckDownloadYYBState(String url, int state, long receiveDataLen,
        long totalDataLen) {
      // TODO 检查应用宝下载状态处理逻辑
    }
  };

  private void launchAppDetail(Context context, String marketPkg) {
    String appPkg = getAppProcessName(context);
    if (!isAvilible(context, marketPkg)) {
      Uri uri = Uri.parse("http://app.qq.com");
      Intent intent = new Intent(Intent.ACTION_VIEW, uri);
      context.startActivity(intent);
      return;
    }
    try {
      if (TextUtils.isEmpty(appPkg))
        return;
      Uri uri = Uri.parse("market://details?id=" + appPkg);
      Intent intent = new Intent(Intent.ACTION_VIEW, uri);
      if (!TextUtils.isEmpty(marketPkg))
        intent.setPackage(marketPkg);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intent);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private boolean isAvilible(Context context, String packageName) {
    final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
    List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
    List<String> pName = new ArrayList<String>();// 用于存储所有已安装程序的包名
    // 从pinfo中将包名字逐一取出，压入pName list中
    if (pinfo != null) {
      for (int i = 0; i < pinfo.size(); i++) {
        String pn = pinfo.get(i).packageName;
        pName.add(pn);
      }
    }
    return pName.contains(packageName);// 判断pName中是否有目标程序的包名，有TRUE，没有FALSE
  }

  /**
   * 获取当前包名
   * 
   * @param context
   * @return
   */
  private String getAppProcessName(Context context) {
    // 当前应用pid
    int pid = android.os.Process.myPid();
    // 任务管理类
    ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    // 遍历所有应用
    List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
    for (ActivityManager.RunningAppProcessInfo info : infos) {
      if (info.pid == pid)// 得到当前应用
        return info.processName;// 返回包名
    }
    return "";
  }

}
