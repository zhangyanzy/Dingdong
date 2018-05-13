package com.dindong.dingdong.presentation.discovery;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseFragment;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.FragmentDiscoveryMomentBinding;
import com.dindong.dingdong.databinding.ItemDiscoveryMomentBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.like.PraiseLikeCase;
import com.dindong.dingdong.network.api.moment.usecase.ListMomentCase;
import com.dindong.dingdong.network.api.moment.usecase.MomentCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.comment.Comment;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.KeyboardUtil;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.baseadapter.BaseViewAdapter;
import com.dindong.dingdong.widget.baseadapter.BindingViewHolder;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseFooterView;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseHeaderView;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wcong on 2018/5/5.
 * <p>
 * 发现-动态</>
 */

public class DiscoveryMomentFragment extends BaseFragment {
  FragmentDiscoveryMomentBinding binding;

  private SingleTypeAdapter<Comment> adapter;

  private String relationId = null;

  @Override
  protected View initComponent(LayoutInflater inflater, ViewGroup container) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_discovery_moment, container,
        false);

    addInterceptView(binding.layoutEdit);
    return binding.getRoot();
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    adapter = new SingleTypeAdapter(getContext(), R.layout.item_discovery_moment);
    adapter.setDecorator(new Decorator());
    adapter.setPresenter(new Presenter());
  }

  @Override
  protected void createEventHandlers() {
    binding.setPresenter(new Presenter());
    binding.refreshLayout.setOnRefreshListener(new BaseHeaderView.OnRefreshListener() {
      @Override
      public void onRefresh(BaseHeaderView baseHeaderView) {
        listMoment(false, true);
      }
    });
    binding.refreshLayout.setOnLoadListener(new BaseFooterView.OnLoadListener() {
      @Override
      public void onLoad(BaseFooterView baseFooterView) {
        listMoment(false, false);
      }
    });
    KeyboardUtil.setKeyboardVisibleListener(binding.edtComment, getActivity(),
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

  @Override
  protected void firstVisible() {
    super.firstVisible();
    listMoment(true, true);
  }

  // private void initPhotoLayout() {
  // binding.pl.setSource(images, true);
  // }

  /**
   * 获取动态
   * 
   * @param showProgress
   * @param isRefresh
   */
  private void listMoment(boolean showProgress, final boolean isRefresh) {
    final QueryParam param = new QueryParam();
    if (isRefresh)
      param.setStart(0);
    else
      param.setStart(adapter.getData().size());

    new ListMomentCase(param)
        .execute(new HttpSubscriber<List<Comment>>(showProgress ? getContext() : null) {
          @Override
          public void onFailure(String errorMsg, Response<List<Comment>> response) {
            DialogUtil.getErrorDialog(getContext(), errorMsg).show();
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
      LinearLayoutManager manager = new LinearLayoutManager(getContext());
      manager.setOrientation(LinearLayoutManager.VERTICAL);
      binding.lst.setLayoutManager(manager);
      binding.lst.setAdapter(adapter);
    }
  }

  public class Decorator implements BaseViewAdapter.Decorator {

    @Override
    public void decorator(BindingViewHolder holder, int position, int viewType) {
      if (holder == null || holder.getBinding() == null)
        return;
      ItemDiscoveryMomentBinding itemDiscoveryMomentBinding = (ItemDiscoveryMomentBinding) holder
          .getBinding();
      itemDiscoveryMomentBinding.pl.setRatio(0.6f);
      itemDiscoveryMomentBinding.pl.setSource(itemDiscoveryMomentBinding.getItem().getImages(),
          true);
    }
  }

  /**
   * 评论
   *
   */
  private void comment(final String relationId) {
    if (IsEmpty.string(binding.edtComment.getText().toString().trim())) {
      ToastUtil.toastHint(getContext(), R.string.discovery_comment_empty);
      return;
    }
    new MomentCase(
        MomentConverter.createComment(binding.edtComment.getText().toString(), relationId))
            .execute(new HttpSubscriber<Comment>(getContext()) {
              @Override
              public void onFailure(String errorMsg, Response<Comment> response) {
                DialogUtil.getErrorDialog(getContext(), errorMsg).show();
              }

              @Override
              public void onSuccess(Response<Comment> response) {
                for (Comment comment1 : adapter.getData()) {
                  if (relationId.equals(comment1.getId())) {
                    comment1.setCommentCount(comment1.getCommentCount() + 1);
                  }
                }
                adapter.notifyDataSetChanged();
                binding.edtComment.setText(null);
                KeyboardUtil.control(binding.edtComment, false);
                ToastUtil.toastHint(getContext(), R.string.discovery_comment_success);
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
      Intent intent = new Intent(getContext(), DiscoveryDetailActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, comment);
      startActivity(intent);
    }

    /**
     * 发送评论
     *
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
        ToastUtil.toastHint(getContext(), R.string.discovery_praised);
        return;
      }
      new PraiseLikeCase(comment.getId()).execute(new HttpSubscriber<Void>(getContext()) {
        @Override
        public void onFailure(String errorMsg, Response<Void> response) {
          DialogUtil.getErrorDialog(getContext(), errorMsg).show();
        }

        @Override
        public void onSuccess(Response<Void> response) {
          ToastUtil.toastHint(getContext(), R.string.discovery_praise_success);
          for (Comment comment1 : adapter.getData()) {
            if (comment.getId().equals(comment1.getId())) {
              comment.setPraise(true);
            }

          }
          adapter.notifyDataSetChanged();
        }
      });
    }
  }
}
