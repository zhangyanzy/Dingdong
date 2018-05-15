package com.dindong.dingdong.presentation.discovery;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityDiscoveryDetailBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.like.usecase.CancelPraiseLikeCase;
import com.dindong.dingdong.network.api.like.usecase.PraiseLikeCase;
import com.dindong.dingdong.network.api.moment.usecase.GetMomentCase;
import com.dindong.dingdong.network.api.moment.usecase.ListMomentCommentCase;
import com.dindong.dingdong.network.api.moment.usecase.MomentCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.comment.Comment;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.KeyboardUtil;
import com.dindong.dingdong.util.StringUtil;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.NavigationTopBar;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

/**
 * 发现详情
 */
public class DiscoveryDetailActivity extends BaseActivity {

  ActivityDiscoveryDetailBinding binding;

  private Comment comment;

  private SingleTypeAdapter adapter;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_discovery_detail);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
    binding.edtComment.postDelayed(new Runnable() {
      @Override
      public void run() {
        KeyboardUtil.control(binding.edtComment, false);
      }
    }, 0);
    addInterceptView(binding.layoutEdit);
    adapter = new SingleTypeAdapter(this, R.layout.item_discovery_comment);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    comment = (Comment) getIntent().getSerializableExtra(AppConfig.IntentKey.DATA);
    binding.setComment(comment);
    binding.pl.setRatio(0.6f);
    binding.pl.setSource(comment.getImages(), true);
    listComment();
    statistic(comment.getId());
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
    binding.setPresenter(new Presenter());
  }

  /**
   * 获取评论
   */
  private void listComment() {
    new ListMomentCommentCase(comment.getId()).execute(new HttpSubscriber<List<Comment>>(this) {
      @Override
      public void onFailure(String errorMsg, Response<List<Comment>> response) {
        DialogUtil.getErrorDialog(DiscoveryDetailActivity.this, errorMsg).show();
      }

      @Override
      public void onSuccess(Response<List<Comment>> response) {
        loadRecyclerView(response.getData());
      }
    });
  }

  private void loadRecyclerView(List<Comment> data) {
    adapter.clear();
    adapter.addAll(data);
    LinearLayoutManager manager = new LinearLayoutManager(this);
    manager.setOrientation(LinearLayoutManager.VERTICAL);
    binding.lst.setLayoutManager(manager);
    binding.lst.setAdapter(adapter);
  }

  /**
   * 统计访问量
   * 
   * @param id
   */
  private void statistic(String id) {
    new GetMomentCase(id).execute(new HttpSubscriber<Comment>() {
      @Override
      public void onFailure(String errorMsg, Response<Comment> response) {

      }

      @Override
      public void onSuccess(Response<Comment> response) {
        binding.txtViewCount.setText(
            StringUtil.format(getString(R.string.label_view), response.getData().getViewCount()));
        comment = response.getData();
      }
    });
  }

  private void setResult() {
    Intent intent = new Intent();
    intent.putExtra(AppConfig.IntentKey.DATA, comment);
    setResult(RESULT_OK, intent);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  public class Presenter {
    /**
     * 评论
     */
    public void onComment() {
      if (IsEmpty.string(binding.edtComment.getText().toString().trim())) {
        ToastUtil.toastHint(DiscoveryDetailActivity.this, R.string.discovery_comment_empty);
        return;
      }
      new MomentCase(
          MomentConverter.createComment(binding.edtComment.getText().toString(), comment.getId()))
              .execute(new HttpSubscriber<Comment>(DiscoveryDetailActivity.this) {
                @Override
                public void onFailure(String errorMsg, Response<Comment> response) {
                  DialogUtil.getErrorDialog(DiscoveryDetailActivity.this, errorMsg).show();
                }

                @Override
                public void onSuccess(Response<Comment> response) {
                  binding.edtComment.setText(null);
                  KeyboardUtil.control(binding.edtComment, false);
                  ToastUtil.toastHint(DiscoveryDetailActivity.this,
                      R.string.discovery_comment_success);
                  comment.setCommentCount(comment.getCommentCount() + 1);
                  setResult();
                  listComment();
                }
              });
    }

    /**
     * 点赞
     *
     * @param comment
     */
    public void onPraise(final Comment comment) {
      if (comment.isPraise()) {
        // 取消赞
        new CancelPraiseLikeCase(comment.getId())
            .execute(new HttpSubscriber<Void>(DiscoveryDetailActivity.this) {
              @Override
              public void onFailure(String errorMsg, Response<Void> response) {
                DialogUtil.getErrorDialog(DiscoveryDetailActivity.this, errorMsg).show();
              }

              @Override
              public void onSuccess(Response<Void> response) {
                ToastUtil.toastHint(DiscoveryDetailActivity.this,
                    R.string.discovery_cancel_praise_success);
                comment.setPraise(false);
                binding.setComment(comment);
                setResult();
              }
            });

        return;
      }
      // 点赞
      new PraiseLikeCase(comment.getId())
          .execute(new HttpSubscriber<Void>(DiscoveryDetailActivity.this) {
            @Override
            public void onFailure(String errorMsg, Response<Void> response) {
              DialogUtil.getErrorDialog(DiscoveryDetailActivity.this, errorMsg).show();
            }

            @Override
            public void onSuccess(Response<Void> response) {
              ToastUtil.toastHint(DiscoveryDetailActivity.this, R.string.discovery_praise_success);
              comment.setPraise(true);
              binding.setComment(comment);
              setResult();
            }
          });
    }

    public void onCommentClick() {
      binding.edtComment.requestFocus();
      KeyboardUtil.control(binding.edtComment, true);
    }
  }
}
