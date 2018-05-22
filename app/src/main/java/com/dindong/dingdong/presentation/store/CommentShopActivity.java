package com.dindong.dingdong.presentation.store;

import java.math.BigDecimal;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityCommentShopBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.comment.usecase.CommentCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.comment.Comment;
import com.dindong.dingdong.presentation.discovery.MomentConverter;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.NavigationTopBar;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

/**
 * 评论门店
 */
public class CommentShopActivity extends BaseActivity {

  ActivityCommentShopBinding binding;

  private String relationId;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_comment_shop);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
    TextView textView = new TextView(this);
    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
    textView.setTextColor(Color.parseColor("#468DE6"));
    textView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        comment();
      }
    });
    textView.setText(R.string.send_moment_confirm);
    binding.nb.addRightView(textView);

    binding.pl.setMaxLength(6);
    binding.pl.init();
  }

  @Override
  protected void createEventHandlers() {
    binding.nb
        .setNavigationTopBarClickListener(new NavigationTopBar.NavigationTopBarClickListener() {
          @Override
          public void leftImageClick() {
            finish();
          }
        });
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    relationId = getIntent().getStringExtra(AppConfig.IntentKey.DATA);
  }

  /**
   * 评论门店
   */
  private void comment() {
    if (IsEmpty.string(binding.edtMsg.getText().toString().trim())) {
      ToastUtil.toastHint(CommentShopActivity.this, R.string.send_moment_empty);
      return;
    }
    Comment comment = MomentConverter.createMoment(binding.pl.getSource(),
        binding.edtMsg.getText().toString().trim());
    comment.setRating(new BigDecimal(binding.rating.getRating()));
    comment.setRelationId(relationId);
    comment.setStoreId(relationId);
    new CommentCase(comment).execute(new HttpSubscriber<Comment>(CommentShopActivity.this) {
      @Override
      public void onFailure(String errorMsg, Response<Comment> response) {
        DialogUtil.getErrorDialog(CommentShopActivity.this, errorMsg).show();
      }

      @Override
      public void onSuccess(Response<Comment> response) {
        ToastUtil.toastHint(CommentShopActivity.this, R.string.send_moment_success);
        setResult(RESULT_OK);
        finish();
      }
    });
  }

}
