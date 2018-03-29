package com.dindong.dingdong.adapter;

import com.dindong.dingdong.network.bean.store.Subject;
import com.dindong.dingdong.widget.baseadapter.BaseViewAdapter;

/**
 * Created by wcong on 2018/3/20.
 * <p>
 * </>
 */

public interface SubjectPresenter extends BaseViewAdapter.Presenter {
  void onSubjectItemClick(Subject subject);
}
