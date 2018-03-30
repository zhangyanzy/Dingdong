package com.dindong.dingdong.network;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.exception.ApiException;
import com.dindong.dingdong.network.exception.TokenExpiredException;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.widget.sweetAlert.SweetAlertDialog;

import android.content.Context;

/**
 * Created by wangcong on 2018/3/9.
 */

public abstract class HttpSubscriber<T> extends rx.Subscriber<Response<T>> {

  private Context context;
  private SweetAlertDialog dialog;

  public HttpSubscriber() {
  }

  public HttpSubscriber(Context context) {
    this.context = context;
    dialog = DialogUtil.getProgressDialog(context);
  }

  @Override
  public void onCompleted() {
    if (dialog != null && context != null)
      dialog.dismiss();
    if (isUnsubscribed()) {
      unsubscribe();
    }
  }

  @Override
  public void onError(Throwable throwable) {
    if (dialog != null && context != null)
      dialog.dismiss();
    if (throwable instanceof TokenExpiredException) {
      EventBus.getDefault().post(new AuthEvent(AuthEvent.TOKEN_EXPIRED));
      return;
    }
    onFailure(parseException(throwable), null);
  }

  @Override
  public void onStart() {
    super.onStart();
    if (dialog != null && context != null)
      dialog.show();
  }

  @Override
  public void onNext(Response<T> t) {
    if (dialog != null && context != null)
      dialog.dismiss();
    if (t.isSuccess()) {
      onSuccess(t);
    } else {
      if (t.getMessage() != null) {
        onFailure(t.getMessage(), t);
      } else {
        onFailure(null, t);
      }
    }

  }

  public abstract void onFailure(String errorMsg, Response<T> response);

  public abstract void onSuccess(Response<T> response);

  private String parseException(Throwable throwable) {
    String errorMessage;
    throwable.printStackTrace();
    if (throwable instanceof SocketTimeoutException) {
      errorMessage = "网络链接超时,请稍后重试!";
    } else if (throwable instanceof ConnectException) {
      errorMessage = "网络链接失败,请检查网络设置!";
    } else if (throwable instanceof JSONException) {
      errorMessage = "数据解析失败,请稍候重试!";
    } else if (throwable instanceof ApiException) {
      errorMessage = throwable.getMessage();
    } else if (throwable instanceof TokenExpiredException) {
      errorMessage = throwable.getMessage();
      EventBus.getDefault().post(new AuthEvent(AuthEvent.TOKEN_EXPIRED));
    } else {
      errorMessage = "未知错误!";
    }
    return errorMessage;
  }

}
