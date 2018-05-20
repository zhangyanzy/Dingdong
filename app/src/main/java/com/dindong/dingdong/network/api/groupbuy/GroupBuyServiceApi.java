package com.dindong.dingdong.network.api.groupbuy;

import java.util.List;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.groupbuy.GroupBuy;
import com.dindong.dingdong.network.bean.groupbuy.GroupBuyMember;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by wcong on 2018/5/20.
 * <p>
 * 团购管理api </>
 */

public interface GroupBuyServiceApi {
  /**
   * 获取指定商品的正在进行的团购单列表
   * 
   * @param itemId
   * @return
   */
  @GET("groupby/list/{itemId}")
  Observable<Response<List<GroupBuy>>> listGroupBuy(@Path("itemId") String itemId);

  /**
   * 获取指定团购的参与人列表
   * 
   * @param id
   * @return
   */
  @GET("groupby/member/list/{id}")
  Observable<Response<List<GroupBuyMember>>> listMemberGroupBuy(@Path("id") String id);

  /**
   * getById
   * 
   * @param id
   * @return
   */
  @GET("groupby/{id}")
  Observable<Response<GroupBuy>> getGroupBuy(@Path("id") String id);
}
