package com.dindong.dingdong.network.api.subject;

import java.util.List;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.store.Subject;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by wcong on 2018/3/22.
 * <p>
 * 课程 </>
 */

public interface SubjectServiceApi {
  /**
   * 查询门店课程列表，支持分页查询。
   *
   * @param param
   *          shop:=id，指定门店查询<br/>
   *          keyword:%=%，关键字类似于，支持门店名称<br/>
   *          SortParam#property=range,direction=asc，按距离查询<br/>
   *          SortParam#property=popular,direction=desc，按人气查询<br/>
   *          SortParam#property=evaluate,direction=desc，按评价查询<br/>
   * @return
   */
  @POST("subject/list")
  Observable<Response<List<Subject>>> listSubject(@Body QueryParam param);

  /**
   * 查询热门推荐课程，所有门店，优先按城市
   *
   * @param param
   *          keyword，关键字<br/>
   *          cityCode，城市名<br/>
   *          range，距离<br/>
   *          longitude，经度<br/>
   *          latitude，纬度<br/>
   * @return
   */
  @POST("subject/hot/list")
  Observable<Response<List<Subject>>> listHotSubject(@Body QueryParam param);

}
