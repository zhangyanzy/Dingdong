package com.dindong.dingdong.presentation.discovery;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseFragment;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.FragmentDiscoveryMomentBinding;
import com.dindong.dingdong.databinding.ItemDiscoveryMomentBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.like.usecase.CancelPraiseLikeCase;
import com.dindong.dingdong.network.api.like.usecase.PraiseLikeCase;
import com.dindong.dingdong.network.api.moment.usecase.ListImpressiveCase;
import com.dindong.dingdong.network.api.moment.usecase.ListMomentCase;
import com.dindong.dingdong.network.api.moment.usecase.MomentCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.comment.Comment;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.KeyboardUtil;
import com.dindong.dingdong.util.TextFoldUtil;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.baseadapter.BaseViewAdapter;
import com.dindong.dingdong.widget.baseadapter.BindingViewHolder;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseFooterView;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseHeaderView;

import android.annotation.SuppressLint;
import android.app.Activity;
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

@SuppressLint("ValidFragment")
public class DiscoveryMomentFragment extends BaseFragment {
  FragmentDiscoveryMomentBinding binding;

  private SingleTypeAdapter<Comment> adapter;

  private String relationId = null;

  private final int INTENT_REQUEST = 0x01;

  private FragmentType fragmentType;// moment-动态，impressive-有声有色

  private TextFoldUtil textFoldUtil;

  public DiscoveryMomentFragment(FragmentType fragmentType) {
    this.fragmentType = fragmentType;
  }

  @Override
  protected View initComponent(LayoutInflater inflater, ViewGroup container) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_discovery_moment, container,
        false);

    textFoldUtil = new TextFoldUtil();
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
        loadData(false, true);
      }
    });
    binding.refreshLayout.setOnLoadListener(new BaseFooterView.OnLoadListener() {
      @Override
      public void onLoad(BaseFooterView baseFooterView) {
        loadData(false, false);
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
    loadData(true, true);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == INTENT_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
      // 接收到详情界面的最新信息时，同步列表中数据
      Comment intentComment = (Comment) data.getSerializableExtra(AppConfig.IntentKey.DATA);
      for (Comment comment : adapter.getData()) {
        if (intentComment.getId().equals(comment.getId())) {
          comment.setCommentCount(intentComment.getCommentCount());
          comment.setPraise(intentComment.isPraise());
          comment.setPraiseCount(intentComment.getPraiseCount());
        }
      }
      adapter.notifyDataSetChanged();
    }
  }

  // private void initPhotoLayout() {
  // binding.pl.setSource(images, true);
  // }

  /**
   * 根据fragment不同类型，加载数据
   * 
   * @param showProgress
   * @param isRefresh
   */
  private void loadData(boolean showProgress, final boolean isRefresh) {
    if (fragmentType.equals(FragmentType.moment)) {
      listMoment(showProgress, isRefresh);
    } else if (fragmentType.equals(FragmentType.impressive)) {
      listImpressive(showProgress, isRefresh);
    }
  }

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

  /**
   * 获取有声有色列表
   * 
   * @param showProgress
   * @param isRefresh
   */
  private void listImpressive(boolean showProgress, final boolean isRefresh) {
    final QueryParam param = new QueryParam();
    if (isRefresh)
      param.setStart(0);
    else
      param.setStart(adapter.getData().size());

    new ListImpressiveCase(param)
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
      textFoldUtil.clean();
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
      itemDiscoveryMomentBinding.pl.setRatio(1f);
      itemDiscoveryMomentBinding.pl.setMargin(4);
      itemDiscoveryMomentBinding.pl.setSource(itemDiscoveryMomentBinding.getItem().getImages(),
          true);
      textFoldUtil.attach(itemDiscoveryMomentBinding.txtContent,
          itemDiscoveryMomentBinding.txtBottomContent, itemDiscoveryMomentBinding.txtFold,
          itemDiscoveryMomentBinding.getItem().getMessage(), position);
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

  @Override
  public void onDestroy() {
    super.onDestroy();
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
      startActivityForResult(intent, INTENT_REQUEST);
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
        // 取消点赞
        new CancelPraiseLikeCase(comment.getId()).execute(new HttpSubscriber<Void>(getContext()) {
          @Override
          public void onFailure(String errorMsg, Response<Void> response) {
            DialogUtil.getErrorDialog(getContext(), errorMsg).show();
          }

          @Override
          public void onSuccess(Response<Void> response) {
            ToastUtil.toastHint(getContext(), R.string.discovery_cancel_praise_success);
            for (Comment comment1 : adapter.getData()) {
              if (comment1.getId().equals(comment1.getId())) {
                comment1.setPraise(false);
                comment1.setPraiseCount(comment1.getPraiseCount() - 1);
              }
            }
            adapter.notifyDataSetChanged();
          }
        });

        return;
      }
      // 点赞
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
              comment1.setPraiseCount(comment1.getPraiseCount() + 1);
            }

          }
          adapter.notifyDataSetChanged();
        }
      });
    }
  }

  public enum FragmentType {
    moment, impressive
  }
}
