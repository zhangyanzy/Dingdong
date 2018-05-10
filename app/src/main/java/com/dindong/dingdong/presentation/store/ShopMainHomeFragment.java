package com.dindong.dingdong.presentation.store;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.adapter.SubjectAdapter;
import com.dindong.dingdong.adapter.SubjectPresenter;
import com.dindong.dingdong.base.BaseFragment;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.FragmentShopMainHomeBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.shop.usecase.ListTeacherCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.store.Shop;
import com.dindong.dingdong.network.bean.store.Subject;
import com.dindong.dingdong.network.bean.store.Teacher;
import com.dindong.dingdong.presentation.subject.SubjectDetailActivity;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.IsEmpty;
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

  @Override
  protected View initComponent(LayoutInflater inflater, ViewGroup container) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop_main_home, container, false);

    ((ShopMainActivity) getActivity()).updateViewPagerHeight();
    return binding.getRoot();
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    if (getArguments().getSerializable(AppConfig.IntentKey.DATA) != null) {
      shop = (Shop) getArguments().getSerializable(AppConfig.IntentKey.DATA);
      initHotSubject(shop.getSubjects());
      listTeacher(shop.getId());
    }

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
    subjectAdapter.set(subjects);
    subjectAdapter.setShop(shop);
    LinearLayoutManager manager = new LinearLayoutManager(getContext());
    manager.setOrientation(LinearLayoutManager.VERTICAL);
    binding.lstHotSubject.setLayoutManager(manager);
    binding.lstHotSubject.setAdapter(subjectAdapter);
    binding.lstHotSubject.setNestedScrollingEnabled(false);
  }

  /**
   * 获取老师列表
   */
  private void listTeacher(String shopId) {
    final QueryParam param = new QueryParam();
    param.setLimit(4);

    new ListTeacherCase(shopId, param).execute(new HttpSubscriber<List<Teacher>>() {
      @Override
      public void onFailure(String errorMsg, Response<List<Teacher>> response) {
        DialogUtil.getErrorDialog(getContext(), errorMsg).show();

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

  public class Presenter implements SubjectPresenter {

    @Override
    public void onSubjectItemClick(Subject subject) {
      Intent intent = new Intent(getContext(), SubjectDetailActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, subject);
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

  }

}
