package com.dindong.dingdong.presentation.store;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.adapter.SubjectAdapter;
import com.dindong.dingdong.adapter.SubjectPresenter;
import com.dindong.dingdong.base.BaseFragment;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.FragmentShopMainHomeBinding;
import com.dindong.dingdong.databinding.ItemShopMainHomeCommentBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.comment.usecase.ListCommentCase;
import com.dindong.dingdong.network.api.shop.usecase.ListTeacherCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.comment.Comment;
import com.dindong.dingdong.network.bean.entity.FilterParam;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.store.Shop;
import com.dindong.dingdong.network.bean.store.Subject;
import com.dindong.dingdong.network.bean.store.Teacher;
import com.dindong.dingdong.presentation.subject.SubjectDetailActivity;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.StringUtil;
import com.dindong.dingdong.widget.baseadapter.BaseViewAdapter;
import com.dindong.dingdong.widget.baseadapter.BindingViewHolder;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wcong on 2018/5/8.
 * <p>
 * 门店主页-首页</>
 */

public class ShopMainHomeFragment extends BaseFragment {
  FragmentShopMainHomeBinding binding;
  private Shop shop;

  private List<Comment> comments = new ArrayList<>();

  private boolean isMore = false;

  @Override
  protected View initComponent(LayoutInflater inflater, ViewGroup container) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop_main_home, container, false);

    ((ShopMainActivity) getActivity()).updateViewPagerHeight();
    binding.txtRatingTotal
        .setText(StringUtil.format(getString(R.string.shop_main_comment_title), 0));
    return binding.getRoot();
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    if (getArguments().getSerializable(AppConfig.IntentKey.DATA) != null) {
      shop = (Shop) getArguments().getSerializable(AppConfig.IntentKey.DATA);
      initHotSubject(shop.getSubjects());
      listTeacher(shop.getId());
      listComment(false, shop.getId());
    }

  }

  @Override
  protected void createEventHandlers() {
    binding.setPresenter(new Presenter());
  }

  /**
   * 加载热门课程
   *
   * @param subjects
   */
  private void initHotSubject(List<Subject> subjects) {
    if (IsEmpty.list(subjects))
      return;
    SubjectAdapter subjectAdapter = new SubjectAdapter(getContext());
    subjectAdapter.setPresenter(new Presenter());
    subjectAdapter.setMargin100(true);
    subjectAdapter.set(subjects);
    subjectAdapter.setShop(shop);
    LinearLayoutManager manager = new LinearLayoutManager(getContext());
    manager.setOrientation(LinearLayoutManager.VERTICAL);
    binding.lstHotSubject.setLayoutManager(manager);
    binding.lstHotSubject.setAdapter(subjectAdapter);
    binding.lstHotSubject.setNestedScrollingEnabled(false);
  }

  private ListTeacherCase listTeacherCase;

  /**
   * 获取老师列表
   */
  private void listTeacher(String shopId) {
    final QueryParam param = new QueryParam();
    param.setLimit(4);

    listTeacherCase = new ListTeacherCase(shopId, param);
    listTeacherCase.execute(new HttpSubscriber<List<Teacher>>() {
      @Override
      public void onFailure(String errorMsg, Response<List<Teacher>> response) {
        ((ShopMainActivity) getActivity()).updateViewPagerHeight();
      }

      @Override
      public void onSuccess(Response<List<Teacher>> response) {
        SingleTypeAdapter adapter = new SingleTypeAdapter(getContext(),
            R.layout.item_shop_main_teahcer);
        adapter.addAll(response.getData());
        GridLayoutManager manager = new GridLayoutManager(getContext(), 4);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.lstTeacher.setLayoutManager(manager);
        binding.lstTeacher.setAdapter(adapter);
        binding.lstTeacher.setNestedScrollingEnabled(false);

        ((ShopMainActivity) getActivity()).updateViewPagerHeight();
      }
    });
  }

  private ListCommentCase listCommentCase;

  /**
   * 获取门店评论
   * 
   * @param showProgress
   * @param shopId
   */
  public void listComment(boolean showProgress, String shopId) {
    final QueryParam param = new QueryParam();
    param.setLimit(999);
    param.getFilters().add(new FilterParam("relationId", shopId));

    listCommentCase = new ListCommentCase(param);
    listCommentCase.execute(new HttpSubscriber<List<Comment>>(showProgress ? getContext() : null) {
      @Override
      public void onFailure(String errorMsg, Response<List<Comment>> response) {
        DialogUtil.getErrorDialog(getContext(), errorMsg).show();
        ((ShopMainActivity) getActivity()).updateViewPagerHeight();
      }

      @Override
      public void onSuccess(Response<List<Comment>> response) {

        isMore = response.isMore();
        comments = response.getData();
        // 计算平均评分
        BigDecimal ratingTotal = BigDecimal.ZERO;
        for (Comment comment : response.getData()) {
          ratingTotal = ratingTotal.add(comment.getRating());
        }
        ratingTotal = ratingTotal.compareTo(BigDecimal.ZERO) == 0 ? ratingTotal
            : ratingTotal.divide(new BigDecimal(response.getData().size()), 1,
                BigDecimal.ROUND_HALF_UP);
        if (new BigDecimal(ratingTotal.intValue()).compareTo(ratingTotal) != 0) {
          String rating = StringUtil.format("{0,number,0.0}", ratingTotal);
          if (rating.indexOf(".") > 0) {
            BigDecimal point = new BigDecimal(
                rating.substring(rating.indexOf(".") + 1, rating.length()));
            ratingTotal.setScale(0,
                point.compareTo(BigDecimal.valueOf(5)) >= 0 ? BigDecimal.ROUND_UP
                    : BigDecimal.ROUND_DOWN);
          }
        }
        binding.rating.setRating(ratingTotal.floatValue());

        binding.txtRatingTotal.setText(StringUtil
            .format(getString(R.string.shop_main_comment_title), response.getData().size()));

        SingleTypeAdapter adapter = new SingleTypeAdapter(getContext(),
            R.layout.item_shop_main_home_comment);
        adapter.addAll(
            response.getData().size() >= 3 ? response.getData().subList(0, 3) : response.getData());
        adapter.setDecorator(new Decorator());
        adapter.setPresenter(new Presenter());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.lstComment.setLayoutManager(manager);
        binding.lstComment.setAdapter(adapter);
        binding.lstComment.setNestedScrollingEnabled(false);

        ((ShopMainActivity) getActivity()).updateViewPagerHeight();
      }
    });
  }

  @Override
  public void onDestroy() {
    if (listTeacherCase != null)
      listTeacherCase.unSubscribe();
    if (listCommentCase != null)
      listCommentCase.unSubscribe();
    super.onDestroy();
  }

  public class Decorator implements BaseViewAdapter.Decorator {

    @Override
    public void decorator(BindingViewHolder holder, int position, int viewType) {
      if (holder == null || holder.getBinding() == null)
        return;
      ItemShopMainHomeCommentBinding itemShopMainHomeCommentBinding = (ItemShopMainHomeCommentBinding) holder
          .getBinding();
      itemShopMainHomeCommentBinding.pl.setRatio(1f);
      itemShopMainHomeCommentBinding.pl
          .setSource(itemShopMainHomeCommentBinding.getItem().getImages(), true);
      if (position == binding.lstComment.getAdapter().getItemCount() - 1)
        // 图片全部加载完时更新UI
        ((ShopMainActivity) getActivity()).updateViewPagerHeight();
    }
  }

  public class Presenter implements SubjectPresenter {

    @Override
    public void onSubjectItemClick(Subject subject) {
      Intent intent = new Intent(getContext(), SubjectDetailActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, subject);
      intent.putExtra(AppConfig.IntentKey.SUMMARY, shop);
      startActivity(intent);
    }

    public void onCommentItemClick(Comment comment) {
      Intent intent = new Intent(getContext(), ShopCommentDetailActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, comment);
      intent.putExtra(AppConfig.IntentKey.SUMMARY, shop);
      startActivity(intent);
    }

    /**
     * 更多教师
     */
    public void onMoreTeacher() {
      Intent intent = new Intent(getContext(), TeacherListActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, shop);
      startActivity(intent);
    }

    /**
     * 更多评论
     */
    public void onMoreComment() {
      Intent intent = new Intent(getContext(), ShopCommentListActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, shop);
      // intent.putExtra(AppConfig.IntentKey.SUMMARY, isMore);
      // intent.putExtra(AppConfig.IntentKey.ID, shop.getId());
      startActivity(intent);
    }

  }

}
