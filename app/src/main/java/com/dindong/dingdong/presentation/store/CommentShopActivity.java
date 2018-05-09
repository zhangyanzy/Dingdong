package com.dindong.dingdong.presentation.store;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityCommentShopBinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

/**
 * 门店评论
 */
public class CommentShopActivity extends BaseActivity {

  ActivityCommentShopBinding binding;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_comment_shop);

    binding.pl.setMaxLength(9);
    binding.pl.init();
  }

  @Override
  protected void createEventHandlers() {
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {

  }
}
