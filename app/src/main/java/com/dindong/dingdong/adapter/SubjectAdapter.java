package com.dindong.dingdong.adapter;

import com.dindong.dingdong.R;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;

import android.content.Context;

/**
 * Created by wcong on 2018/3/20.
 * <p>
 * </>
 */

public class SubjectAdapter extends SingleTypeAdapter {
  public SubjectAdapter(Context context) {
    super(context, R.layout.item_global_subject);
  }

  public void setPresenter(SubjectPresenter presenter) {
    setPresenter(presenter);
  }
}
