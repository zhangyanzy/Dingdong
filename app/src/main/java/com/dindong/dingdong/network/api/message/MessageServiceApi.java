package com.dindong.dingdong.network.api.message;

import java.util.List;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.message.Message;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by wcong on 2018/4/14.
 * <p>
 * 消息相关api</>
 */

public interface MessageServiceApi {
  /**
   * 获取消息列表
   * 
   * @param param
   *          type，消息类型 system-系统消息，notice-公告<br/>
   * @return
   */
  @POST("app/message/listMsg")
  Observable<Response<List<Message>>> listMsg(@Body QueryParam param);
}
