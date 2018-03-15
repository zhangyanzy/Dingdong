package com.dindong.dingdong.network;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by wcong on 2018/3/14.
 */

public class MockInterceptor implements Interceptor {

  private Context appContext;
  private boolean supportSDCard;
  private boolean useMock;

  public MockInterceptor(Context appContext, boolean useMock) {
    this(appContext, false, useMock);
  }

  public MockInterceptor(Context appContext, boolean supportSDCard, boolean useMock) {
    this.appContext = appContext;
    this.supportSDCard = supportSDCard;
    this.useMock = useMock;
  }

  @Override
  public Response intercept(Interceptor.Chain chain) throws IOException {
    Request request = chain.request();
    HttpUrl httpUrl = request.url();
    String path = httpUrl.url().getPath();

    if (useMock) {
      Log.e("MockInterceptor", "Mock");
      // 返回模拟数据
      String jsonData = readMockData(path);
      if (jsonData != null) {
        // 1s ~ 3s 随机
        sleep(new Random().nextInt(3000 - 1000 + 1) + 1000);
        return new Response.Builder().code(200).message(jsonData).request(chain.request())
            .protocol(Protocol.HTTP_1_0).body(ResponseBody
                .create(MediaType.parse("application/json"), jsonData.getBytes("UTF-8")))
            .addHeader("content-type", "application/json").build();
      }
    }

    return chain.proceed(request);
  }

  private String readMockData(String path) throws IOException {

    StringBuilder sbJson = new StringBuilder();
    // 约定 mock 数据存放要求！！！
    String relativePath = "mock" + path + ".json";

    InputStream inputStream = openMockFile(relativePath);
    if (inputStream == null) {
      return null;
    }
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

    try {
      String line;
      while ((line = reader.readLine()) != null) {
        sbJson.append(line);
      }
    } finally {
      try {
        reader.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return sbJson.toString();
  }

  private InputStream openMockFile(String relativePath) {
    InputStream inputStream = null;
    try {
      if (supportSDCard) {
        File file = new File(Environment.getExternalStorageDirectory(), relativePath);
        inputStream = new FileInputStream(file);
      } else {
        inputStream = appContext.getAssets().open(relativePath);
      }
    } catch (IOException e) {
    }

    return inputStream;
  }

  private void sleep(long time) {
    try {
      if (time > 0) {
        TimeUnit.MILLISECONDS.sleep(time);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private int queryPage(String page) {
    return (page == null || page.trim().length() <= 0) ? 1 : Integer.valueOf(page);
  }
}
