package com.dindong.dingdong.network;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.greenrobot.eventbus.EventBus;

import com.dindong.dingdong.network.exception.TokenExpiredException;
import com.dindong.dingdong.util.IsEmpty;
import com.trello.rxlifecycle.LifecycleProvider;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by wangcong on 2018/3/9.
 */

public abstract class BaseUseCase<T> {

  private Observable observable;
  protected Subscription subscription = Subscriptions.empty();
  protected Subscriber subscriber;

  public static final boolean isMock = true;

  public void subscribe(LifecycleProvider provider, Subscriber useCaseSubscriber) {
    if (!IsEmpty.object(provider))
      this.subscription = this.buildCase().subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread()).compose(provider.bindToLifecycle())
          .onErrorResumeNext(new Func1<Throwable, Observable>() {
            @Override
            public Observable call(Throwable throwable) {
              if (throwable instanceof TokenExpiredException) {
                EventBus.getDefault().post(new AuthEvent(AuthEvent.TOKEN_EXPIRED));
                return Observable.empty();
              } else {
                return Observable.error(throwable);
              }
            }
          }).subscribe(useCaseSubscriber);
    else
      execute(useCaseSubscriber);
  }

  public void execute(final Subscriber response) {
    observable = buildCase();
    subscriber = response;
    if (observable == null)
      return;
    subscription = observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(response);
  }

  public void unSubscribe() {
    if (subscriber == null || subscription == null)
      return;
    if (!subscription.isUnsubscribed()) {
      subscriber.unsubscribe();
      subscription.unsubscribe();
    }
  }

  protected T createConnection() {
    return createConnection(false);
  }

  protected T createConnection(boolean useMock) {
    return ApiClient.instance(useMock).create(getType());
  }

  // protected T createUpdateConnection() {
  // return UpdateClient.updateClient().create(getType());
  // }

  protected abstract Observable buildCase();

  private Class<T> getType() {
    Class<T> entityClass = null;
    Type t = getClass().getGenericSuperclass();
    Type[] p = ((ParameterizedType) t).getActualTypeArguments();
    entityClass = (Class<T>) p[0];
    return entityClass;
  }
}
