package com.dindong.dingdong.presentation.store;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityShopCommentListBinding;
import com.dindong.dingdong.databinding.ItemShopCommentCommentBinding;
import com.dindong.dingdong.databinding.ItemShopCommentListBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.comment.usecase.CommentCase;
import com.dindong.dingdong.network.api.comment.usecase.ListCommentCase;
import com.dindong.dingdong.network.api.comment.usecase.ListCommentOfCommentCase;
import com.dindong.dingdong.network.api.like.PraiseLikeCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.comment.Comment;
import com.dindong.dingdong.network.bean.entity.FilterParam;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.store.Shop;
import com.dindong.dingdong.presentation.discovery.MomentConverter;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.KeyboardUtil;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.NavigationTopBar;
import com.dindong.dingdong.widget.baseadapter.BaseViewAdapter;
import com.dindong.dingdong.widget.baseadapter.BindingViewHolder;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseFooterView;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseHeaderView;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

/**
 * 门店评论列表
 */
public class ShopCommentListActivity extends BaseActivity {

  ActivityShopCommentListBinding binding;

  private SingleTypeAdapter<Comment> adapter;

  private String relationId = null;

  private Shop shop;

  private final int requestComment = 0X01;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_comment_list);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);

    adapter = new SingleTypeAdapter(this, R.layout.item_shop_comment_list);
    adapter.setDecorator(new Decorator());
    adapter.setPresenter(new Presenter());
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    shop = (Shop) getIntent().getSerializableExtra(AppConfig.IntentKey.DATA);
    // List<Comment> comments = (List<Comment>) getIntent()
    // .getSerializableExtra(AppConfig.IntentKey.DATA);

    listComment(true, true);
    // if (IsEmpty.list(comments)) {
    // // 评论为空，重新拉取数据保证与服务端数据同步
    // listComment(true, true);
    // } else {
    // loadRecyclerView(comments, true,
    // getIntent().getBooleanExtra(AppConfig.IntentKey.SUMMARY, false));
    // }
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
    binding.refreshLayout.setOnRefreshListener(new BaseHeaderView.OnRefreshListener() {
      @Override
      public void onRefresh(BaseHeaderView baseHeaderView) {
        listComment(false, true);
      }
    });
    binding.refreshLayout.setOnLoadListener(new BaseFooterView.OnLoadListener() {
      @Override
      public void onLoad(BaseFooterView baseFooterView) {
        listComment(false, false);
      }
    });
    KeyboardUtil.setKeyboardVisibleListener(binding.edtComment, this,
        new KeyboardUtil.OnKeyboardVisibleListener() {
          @Override
          public void onKeyboardVisible(boolean visible) {
            if (visible) {
              binding.layoutEdit.setVisibility(View.VISIBLE);
            } else {
              binding.layoutEdit.setVisibility(View.GONE);
            }
          }
        });
    binding.lst.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        KeyboardUtil.control(binding.edtComment, false);
        return false;
      }
    });
  }

  /**
   * 获取门店评论
   *
   * @param showProgress
   */
  public void listComment(boolean showProgress, final boolean isRefresh) {
    final QueryParam param = new QueryParam();
    if (isRefresh)
      param.setStart(0);
    else
      param.setStart(adapter.getData().size());
    param.getFilters().add(new FilterParam("relationId", shop.getId()));

    new ListCommentCase(param).execute(
        new HttpSubscriber<List<Comment>>(showProgress ? ShopCommentListActivity.this : null) {
          @Override
          public void onFailure(String errorMsg, Response<List<Comment>> response) {
            DialogUtil.getErrorDialog(ShopCommentListActivity.this, errorMsg).show();
          }

          @Override
          public void onSuccess(Response<List<Comment>> response) {

            loadRecyclerView(response.getData(), isRefresh, response.isMore());
          }
        });
  }

  private void loadRecyclerView(List<Comment> data, boolean isRefresh, boolean isMore) {
    if (isRefresh) {
      adapter.clear();
      binding.refreshLayout.stopRefresh();
    } else
      binding.refreshLayout.stopLoad();
    adapter.addAll(data);
    binding.refreshLayout.setHasFooter(isMore);
    if (isRefresh) {
      LinearLayoutManager manager = new LinearLayoutManager(ShopCommentListActivity.this);
      manager.setOrientation(LinearLayoutManager.VERTICAL);
      binding.lst.setLayoutManager(manager);
      binding.lst.setAdapter(adapter);
    }
  }

  /**
   * 获取评论回复
   *
   * @param relationId
   * @param itemShopCommentListBinding
   */
  public void listCommentOfComment(String relationId,
      final ItemShopCommentListBinding itemShopCommentListBinding) {
    final QueryParam param = new QueryParam();
    param.setLimit(3);
    param.getFilters().add(new FilterParam("relationId", relationId));

    new ListCommentOfCommentCase(param).execute(new HttpSubscriber<List<Comment>>() {
      @Override
      public void onFailure(String errorMsg, Response<List<Comment>> response) {
        DialogUtil.getErrorDialog(ShopCommentListActivity.this, errorMsg).show();
      }

      @Override
      public void onSuccess(Response<List<Comment>> response) {

        loadCommentOfComment(response.getData(), itemShopCommentListBinding);
      }
    });
  }

  /**
   * 加载评论回复
   * 
   * @param comments
   * @param itemShopCommentListBinding
   */
  private void loadCommentOfComment(List<Comment> comments,
      ItemShopCommentListBinding itemShopCommentListBinding) {
    itemShopCommentListBinding.dividerComment
        .setVisibility(IsEmpty.list(comments) ? View.GONE : View.VISIBLE);
    itemShopCommentListBinding.layoutComment.removeAllViews();
    for (Comment comment : comments) {
      ItemShopCommentCommentBinding itemShopCommentCommentBinding = DataBindingUtil
          .inflate(LayoutInflater.from(this), R.layout.item_shop_comment_comment, null, false);
      SpannableString spannableString = new SpannableString(
          comment.getUcn().getUserName() + ":\b\b\b" + comment.getMessage());
      ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#0099EE"));
      spannableString.setSpan(colorSpan, 0, comment.getUcn().getUserName().length() + 2,
          Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
      itemShopCommentCommentBinding.txt.setText(spannableString);
      itemShopCommentListBinding.layoutComment.addView(itemShopCommentCommentBinding.getRoot());
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == requestComment && resultCode == RESULT_OK) {
      adapter.notifyDataSetChanged();
    }
  }

  public class Decorator implements BaseViewAdapter.Decorator {

    @Override
    public void decorator(BindingViewHolder holder, int position, int viewType) {
      if (holder == null || holder.getBinding() == null)
        return;
      ItemShopCommentListBinding itemShopCommentListBinding = (ItemShopCommentListBinding) holder
          .getBinding();
      itemShopCommentListBinding.pl.setRatio(1f);
      itemShopCommentListBinding.pl.setSource(itemShopCommentListBinding.getItem().getImages(),
          true);

      listCommentOfComment(itemShopCommentListBinding.getItem().getId(),
          itemShopCommentListBinding);
    }
  }

  /**
   * 回复评论
   *
   * @param relationId
   */
  private void comment(final String relationId) {
    if (IsEmpty.string(binding.edtComment.getText().toString().trim())) {
      ToastUtil.toastHint(ShopCommentListActivity.this, R.string.discovery_comment_empty);
      return;
    }
    new CommentCase(
        MomentConverter.createComment(binding.edtComment.getText().toString(), relationId))
            .execute(new HttpSubscriber<Comment>(ShopCommentListActivity.this) {
              @Override
              public void onFailure(String errorMsg, Response<Comment> response) {
                DialogUtil.getErrorDialog(ShopCommentListActivity.this, errorMsg).show();
              }

              @Override
              public void onSuccess(Response<Comment> response) {
                adapter.notifyDataSetChanged();
                binding.edtComment.setText(null);
                KeyboardUtil.control(binding.edtComment, false);
                ToastUtil.toastHint(ShopCommentListActivity.this,
                    R.string.discovery_comment_success);
              }
            });
  }

  public class Presenter implements BaseViewAdapter.Presenter {
    /**
     * 查看详情
     *
     * @param comment
     */
    public void onItemClick(Comment comment) {
      Intent intent = new Intent(ShopCommentListActivity.this, ShopCommentDetailActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, comment);
      intent.putExtra(AppConfig.IntentKey.SUMMARY, shop);
      startActivityForResult(intent, requestComment);
    }

    /**
     * 发送评论
     */
    public void onComment() {
      comment(relationId);
    }

    /**
     * 评论
     *
     * @param comment
     */
    public void onCommentClick(Comment comment) {
      if (relationId != null && !relationId.equals(comment.getId())) {
        binding.edtComment.setText(null);
      }
      relationId = comment.getId();
      binding.layoutEdit.setVisibility(View.VISIBLE);
      binding.layoutEdit.post(new Runnable() {
        @Override
        public void run() {
          binding.edtComment.requestFocus();
        }
      });
      KeyboardUtil.control(binding.edtComment, true);
    }

    /**
     * 点赞
     *
     * @param comment
     */
    public void onPraise(final Comment comment) {
      if (comment.isPraise()) {
        ToastUtil.toastHint(ShopCommentListActivity.this, R.string.discovery_praised);
        return;
      }
      new PraiseLikeCase(comment.getId())
          .execute(new HttpSubscriber<Void>(ShopCommentListActivity.this) {
            @Override
            public void onFailure(String errorMsg, Response<Void> response) {
              DialogUtil.getErrorDialog(ShopCommentListActivity.this, errorMsg).show();
            }

            @Override
            public void onSuccess(Response<Void> response) {
              ToastUtil.toastHint(ShopCommentListActivity.this, R.string.discovery_praise_success);
              for (Comment comment1 : adapter.getData()) {
                if (comment.getId().equals(comment1.getId())) {
                  comment.setPraise(true);
                  comment.setPraiseCount(comment.getPraiseCount() + 1);
                }

              }
              adapter.notifyDataSetChanged();
            }
          });
    }
  }

}
