package com.dindong.dingdong.presentation.main;

import com.bumptech.glide.request.RequestOptions;
import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivitySendMomentBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.moment.usecase.MomentCase;
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
 * 发动态
 */
public class SendMomentActivity extends BaseActivity {

  ActivitySendMomentBinding binding;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_send_moment);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
    TextView textView = new TextView(this);
    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
    textView.setTextColor(Color.parseColor("#468DE6"));
    textView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        moment();
      }
    });
    textView.setText(R.string.send_moment_confirm);
    binding.nb.addRightView(textView);

    binding.pl.setMaxLength(9);
    binding.pl.init();
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    RequestOptions options = new RequestOptions();
    options.placeholder(R.mipmap.img_placeholder);
    options.error(R.mipmap.img_load_failed);
  }

  /**
   * 发动态
   */
  private void moment() {
    if (IsEmpty.string(binding.edtMsg.getText().toString().trim())) {
      ToastUtil.toastHint(SendMomentActivity.this, R.string.send_moment_empty);
      return;
    }
    new MomentCase(MomentConverter.createMoment(binding.pl.getSource(),
        binding.edtMsg.getText().toString().trim()))
            .execute(new HttpSubscriber<Comment>(SendMomentActivity.this) {
              @Override
              public void onFailure(String errorMsg, Response<Comment> response) {
                DialogUtil.getErrorDialog(SendMomentActivity.this, errorMsg).show();
              }

              @Override
              public void onSuccess(Response<Comment> response) {
                ToastUtil.toastHint(SendMomentActivity.this, R.string.send_moment_success);
                setResult(RESULT_OK);
                finish();
              }
            });
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
}
