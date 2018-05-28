package com.dindong.dingdong.network.api.notice;

import java.util.List;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.notice.PublicNotice;

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
   * 获取门店公告列表
   * 
   * @param param
   *          type，消息类型 system-系统消息，notice-公告<br/>
   * @return
   */
  @POST("publicnotice/store/list")
  Observable<Response<List<PublicNotice>>> listShopMsg(@Body QueryParam param);

  /**
   * 获取会员公告列表
   * 
   * @param param
   * @return
   */
  @POST("publicnotice/member/list")
  Observable<Response<List<PublicNotice>>> listMemberMsg(@Body QueryParam param);
}
