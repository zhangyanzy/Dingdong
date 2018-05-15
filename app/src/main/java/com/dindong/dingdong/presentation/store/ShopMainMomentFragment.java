package com.dindong.dingdong.presentation.store;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseFragment;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.FragmentShopMainMomentBinding;
import com.dindong.dingdong.databinding.ItemShopMainMomentBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.like.usecase.PraiseLikeCase;
import com.dindong.dingdong.network.api.moment.usecase.ListMomentCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.comment.Comment;
import com.dindong.dingdong.network.bean.entity.FilterParam;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.store.Shop;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.baseadapter.BaseViewAdapter;
import com.dindong.dingdong.widget.baseadapter.BindingViewHolder;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseFooterView;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseHeaderView;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wcong on 2018/5/8.
 * <p>
 * 门店主页-动态</>
 */

public class ShopMainMomentFragment extends BaseFragment {
  FragmentShopMainMomentBinding binding;
  private Shop shop;

  private SingleTypeAdapter<Comment> adapter;

  @Override
  protected View initComponent(LayoutInflater inflater, ViewGroup container) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop_main_moment, container,
        false);

    adapter = new SingleTypeAdapter(getContext(), R.layout.item_shop_main_moment);
    adapter.setDecorator(new Decorator());
    adapter.setPresenter(new Presenter());
    return binding.getRoot();
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    if (getArguments().getSerializable(AppConfig.IntentKey.DATA) != null) {
      shop = (Shop) getArguments().getSerializable(AppConfig.IntentKey.DATA);
    }

    ((ShopMainActivity) getActivity()).updateViewPagerHeight();
  }

  @Override
  protected void firstVisible() {
    super.firstVisible();

    listComment(true, true);
  }

  @Override
  protected void createEventHandlers() {
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
  }

  /**
   * 获取门店动态
   *
   * @param showProgress
   * @param isRefresh
   */
  private void listComment(boolean showProgress, final boolean isRefresh) {
    final QueryParam param = new QueryParam();
    if (isRefresh) {
      binding.lst.setNestedScrollingEnabled(false);
      param.setStart(0);
    } else
      param.setStart(adapter.getData().size());
    param.getFilters().add(new FilterParam("relationId", shop.getId()));

    new ListMomentCase(param)
        .execute(new HttpSubscriber<List<Comment>>(showProgress ? getContext() : null) {
          @Override
          public void onFailure(String errorMsg, Response<List<Comment>> response) {
            DialogUtil.getErrorDialog(getContext(), errorMsg).show();
            ((ShopMainActivity) getActivity()).updateViewPagerHeight();
          }

          @Override
          public void onSuccess(Response<List<Comment>> response) {
            loadRecyclerView(response.getData(), isRefresh, response.isMore());
            ((ShopMainActivity) getActivity()).updateViewPagerHeight();
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
      final LinearLayoutManager manager = new LinearLayoutManager(getContext());
      manager.setOrientation(LinearLayoutManager.VERTICAL);
      manager.setSmoothScrollbarEnabled(true);
      manager.setAutoMeasureEnabled(true);
      binding.lst.setLayoutManager(manager);
      binding.lst.setAdapter(adapter);

      binding.lst.setHasFixedSize(true);
      binding.lst.setNestedScrollingEnabled(false);
    }
  }

  /**
   * 刷新动态的评论数
   */
  public void refreshMomentCommentCount(String relationId) {
    for (Comment comment1 : adapter.getData()) {
      if (relationId.equals(comment1.getId())) {
        comment1.setPraise(true);
        comment1.setCommentCount(comment1.getCommentCount() + 1);
      }
    }
    adapter.notifyDataSetChanged();
  }

  public RecyclerView getRecyclerView() {
    return binding == null ? null : binding.lst;
  }

  public class Decorator implements BaseViewAdapter.Decorator {

    @Override
    public void decorator(BindingViewHolder holder, int position, int viewType) {
      if (holder == null || holder.getBinding() == null)
        return;
      ItemShopMainMomentBinding itemShopMainMomentBinding = (ItemShopMainMomentBinding) holder
          .getBinding();
      itemShopMainMomentBinding.pl.setRatio(0.67f);
      itemShopMainMomentBinding.pl.setSource(itemShopMainMomentBinding.getItem().getImages(), true);
      if (position == binding.lst.getAdapter().getItemCount() - 1)
        // 图片全部加载完时更新UI
        ((ShopMainActivity) getActivity()).updateViewPagerHeight();

    }
  }

  public class Presenter implements BaseViewAdapter.Presenter {
    /**
     * 查看详情
     *
     * @param comment
     */
    public void onItemClick(Comment comment) {

    }

    /**
     * 评论门店状态
     * 
     * @param comment
     */
    public void onCommentShopMoment(Comment comment) {
      ((ShopMainActivity) getActivity()).commentMomentFromFragment(comment.getId());// 通知容器activity发送评论
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
              comment.setPraiseCount(comment.getPraiseCount() + 1);
            }

          }
          adapter.notifyDataSetChanged();
        }
      });
    }
  }

}
