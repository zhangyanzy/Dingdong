package com.dindong.dingdong.widget.upgrade;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by wangcong on 2018/6/19.
 * <p>
 * apk下载任务
 */

public class DownloadTask extends AsyncTask<Void, Integer, Integer> {

    private String downloadUrl;

    private DownloadListener downloadListener;

    private Context context;
    private final static int MSG_UPDATE = 0x01;
    private final static int MSG_FINISHED = 0x02;
    private final static int MSG_EXCEPTION = 0x03;
    private final static int MSG_CANCEL = 0x04;

    private String apkFile;

    private int interval = 200;// post周期
    private long lastMillis = -1;

    private boolean cancel = false;

    /**
     * @param context
     * @param downloadUrl 最新apk链接
     * @param apkFile     本地apk下载路径
     */
    public DownloadTask(Context context, String downloadUrl, String apkFile) {
        this.context = context;
        this.downloadUrl = downloadUrl;
        this.apkFile = apkFile;
    }

    public DownloadTask setDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
        return this;
    }

    public DownloadTask setInterval(int interval) {
        this.interval = interval;
        return this;
    }

    public String getFilesPath() {
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

    @Override
    protected Integer doInBackground(Void... params) {
        try {
            File file = null;
            URL url = new URL(downloadUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            FileOutputStream fileOutputStream = null;
            InputStream inputStream;
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();

                if (inputStream != null) {
                    file = new File(apkFile);
                    fileOutputStream = new FileOutputStream(file);
                    byte[] buffer = new byte[1024 / 10];
                    int length = 0;
                    int mDownloadProgress = 0;
                    int max = connection.getContentLength();
                    while ((length = inputStream.read(buffer)) != -1) {
                        if (cancel)
                            break;
                        fileOutputStream.write(buffer, 0, length);
                        mDownloadProgress += length;

                        publishProgress(mDownloadProgress * 100 / max);
                    }
                    fileOutputStream.close();
                    fileOutputStream.flush();
                }
                inputStream.close();
            }
            Thread.sleep(20);
            return cancel ? MSG_CANCEL : MSG_FINISHED;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return MSG_EXCEPTION;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        if (lastMillis < 0) {
            lastMillis = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - lastMillis < interval)
            return;
        lastMillis = System.currentTimeMillis();

        if (downloadListener != null)
            downloadListener.onDownload(values[0]);

    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);

        if (integer == MSG_FINISHED) {
            //下载完成
            if (downloadListener != null)
                downloadListener.onFinish();
        } else if (integer == MSG_EXCEPTION) {
            //更新异常
            if (downloadListener != null)
                downloadListener.onException(null);
        } else if (integer == MSG_CANCEL) {
            //更新取消
            if (downloadListener != null)
                downloadListener.onException(null);
        }
    }

    public void cancel() {
        cancel = true;
    }

    public interface DownloadListener {
        void onDownload(int progress);

        void onFinish();

        void onException(Exception e);
    }
}
