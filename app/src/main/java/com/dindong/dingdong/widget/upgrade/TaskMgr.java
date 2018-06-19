package com.dindong.dingdong.widget.upgrade;

import java.io.File;

import com.dindong.dingdong.R;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;

/**
 * Created by wangcong on 2018/6/19.
 * <p>
 * 异步下载任务管理类
 */

public class TaskMgr {

  private Context context;

  private static TaskMgr taskMgr;

  private String apkFile = "";

  private NotificationCompat.Builder builder;
  private NotificationManager notificationManager;

  private int notificationId = 0x01;

  private DownloadTask downloadTask;

  public static TaskMgr getTaskMgr(Context context) {
    if (taskMgr == null)
      return taskMgr = new TaskMgr(context);
    return taskMgr;
  }

  public TaskMgr(Context context) {
    this.context = context;
  }

  /**
   * 启动异步下载任务
   *
   * @param downloadUrl
   *          最新apk链接
   * @param versionName
   *          apk版本
   */
  public void executeDownloadTask(String downloadUrl, final String versionName) {
    builder = new NotificationCompat.Builder(context).setSmallIcon(R.mipmap.app_icon)
        .setProgress(100, 0, false);

    notificationManager = (NotificationManager) context
        .getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.notify(notificationId, builder.build());

    apkFile = getApkFile(versionName);
    downloadTask = new DownloadTask(context, downloadUrl, apkFile)
        .setDownloadListener(new DownloadTask.DownloadListener() {
          @Override
          public void onDownload(int progress) {
            builder.setProgress(100, progress, false);
            notificationManager.notify(notificationId, builder.build());
          }

          @Override
          public void onFinish() {
            notificationManager.cancel(notificationId);
            installApk(context, apkFile);
            new File(apkFile).delete();
          }

          @Override
          public void onException(Exception e) {
            notificationManager.cancel(notificationId);
          }
        });
    downloadTask.execute();
  }

  /**
   * 安装apk
   * 
   * @param context
   * @param apkPath
   */
  private void installApk(Context context, String apkPath) {
    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_VIEW);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      Uri contentUri = FileProvider.getUriForFile(context,
          context.getPackageName() + ".fileProvider", new File(apkPath));
      intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
    } else {
      intent.setDataAndType(Uri.fromFile(new File(apkPath)),
          "application/vnd.android.package-archive");
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
    context.startActivity(intent);
    android.os.Process.killProcess(android.os.Process.myPid());
  }

  public void cancel() {
    if (downloadTask != null)
      downloadTask.cancel();
  }

  private String getApkFile(String versionName) {
    return getFilesPath() + "/" + versionName + "dingdong.apk";
  }

  private String getFilesPath() {
    File cacheDir;
    if (android.os.Environment.getExternalStorageState()
        .equals(android.os.Environment.MEDIA_MOUNTED))
      cacheDir = context.getExternalFilesDir("");
    else
      cacheDir = context.getFilesDir();
    if (!cacheDir.exists())
      cacheDir.mkdirs();
    return cacheDir.getAbsolutePath();
  }

}
