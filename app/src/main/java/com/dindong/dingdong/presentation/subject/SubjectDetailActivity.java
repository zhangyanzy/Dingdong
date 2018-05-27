package com.dindong.dingdong.presentation.subject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivitySubjectDetailBinding;
import com.dindong.dingdong.databinding.ItemGroupBuyBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.groupbuy.usecase.ListGroupBuyCase;
import com.dindong.dingdong.network.api.like.usecase.CancelFavoriteLikeCase;
import com.dindong.dingdong.network.api.like.usecase.FavoriteLikeCase;
import com.dindong.dingdong.network.api.subject.usecase.GetSubjectCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.GlobalImage;
import com.dindong.dingdong.network.bean.groupbuy.GroupBuy;
import com.dindong.dingdong.network.bean.like.LikeEntityType;
import com.dindong.dingdong.network.bean.store.Shop;
import com.dindong.dingdong.network.bean.store.Subject;
import com.dindong.dingdong.network.bean.store.SubjectType;
import com.dindong.dingdong.presentation.pay.OrderUtil;
import com.dindong.dingdong.presentation.store.GroupCountDownQueue;
import com.dindong.dingdong.presentation.store.ShopMainActivity;
import com.dindong.dingdong.presentation.store.ShopMapActivity;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.PhoneUtil;
import com.dindong.dingdong.util.PhotoUtil;
import com.dindong.dingdong.util.StringUtil;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.NavigationTopBar;
import com.dindong.dingdong.widget.baseadapter.BaseViewAdapter;
import com.dindong.dingdong.widget.baseadapter.BindingViewHolder;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SubjectDetailActivity extends BaseActivity {

  ActivitySubjectDetailBinding binding;

  private Subject subject;
  private Shop shop;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_subject_detail);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
    binding.txtOriginal.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
    TextView textView = new TextView(this);
    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
    textView.setTextColor(Color.parseColor("#468DE6"));
    textView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
      }
    });
    textView.setText("推荐");
    binding.nb.addRightView(textView);

  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    if (getIntent().getSerializableExtra(AppConfig.IntentKey.DATA) != null) {
      subject = (Subject) getIntent().getSerializableExtra(AppConfig.IntentKey.DATA);
      shop = subject.getStore();
      binding.setSubject(subject);
      initSubjectImg(subject.getImage());
      initSubjectDescriptionImg(subject.getImages());
      getSubject(subject.getId());
    }
    if (getIntent().getSerializableExtra(AppConfig.IntentKey.SUMMARY) != null) {
      shop = (Shop) getIntent().getSerializableExtra(AppConfig.IntentKey.SUMMARY);
      binding.setShop(shop);
    }
    if (!IsEmpty.string(getIntent().getStringExtra(AppConfig.IntentKey.ID))) {
      getSubject(getIntent().getStringExtra(AppConfig.IntentKey.ID));
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (subject != null && binding != null)
      listGroup(subject.getId());
  }

  /**
   * 获取课程信息
   * 
   * @param subjectId
   */
  private void getSubject(String subjectId) {
    new GetSubjectCase(subjectId).execute(new HttpSubscriber<Subject>() {
      @Override
      public void onFailure(String errorMsg, Response<Subject> response) {
        binding.setSubject(subject);
        initSubjectImg(subject.getImage());
        initSubjectDescriptionImg(subject.getImages());
      }

      @Override
      public void onSuccess(Response<Subject> response) {
        subject = response.getData();
        if (response.getData().getStore() != null) {
          shop = response.getData().getStore();
          binding.setShop(shop);
        }
        binding.setSubject(subject);
        initSubjectImg(subject.getImage());
        initSubjectDescriptionImg(subject.getImages());
      }
    });
  }

  @Override
  protected void createEventHandlers() {
    binding.setPresenter(new Presenter());
    binding.nb
        .setNavigationTopBarClickListener(new NavigationTopBar.NavigationTopBarClickListener() {
          @Override
          public void leftImageClick() {
            finish();
          }
        });
  }

  private void initSubjectImg(GlobalImage image) {
    WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics dm = new DisplayMetrics();
    wm.getDefaultDisplay().getMetrics(dm);
    int width = dm.widthPixels;
    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.imgSubject
        .getLayoutParams();
    params.height = (int) (width * 0.5);
    binding.imgSubject.setLayoutParams(params);
    PhotoUtil.load(this, image == null ? "" : image.getUrl(), binding.imgSubject);
  }

  private void initSubjectDescriptionImg(List<GlobalImage> images) {
    WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics dm = new DisplayMetrics();
    wm.getDefaultDisplay().getMetrics(dm);
    int width = dm.widthPixels;
    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.imgSubjectDescription
        .getLayoutParams();
    params.height = (int) (width * 0.4);
    binding.imgSubjectDescription.setLayoutParams(params);
    PhotoUtil.load(this, IsEmpty.list(images) ? "" : images.get(0).getUrl(),
        binding.imgSubjectDescription);
  }

  private ListGroupBuyCase listGroupBuyCase;

  /**
   * 获取团购列表
   * 
   * @param itemId
   */
  private void listGroup(String itemId) {

    listGroupBuyCase = new ListGroupBuyCase(itemId);
    listGroupBuyCase.execute(new HttpSubscriber<List<GroupBuy>>() {
      @Override
      public void onFailure(String errorMsg, Response<List<GroupBuy>> response) {
      }

      @Override
      public void onSuccess(Response<List<GroupBuy>> response) {
        // 过滤掉已过期的团
        List<Integer> deleteIndex = new ArrayList<>();
        for (int i = 0; i < response.getData().size(); i++) {
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(response.getData().get(i).getEndTime());

          calendar.set(Calendar.MILLISECOND, 0);

          if (new Date().compareTo(calendar.getTime()) >= 0) {
            // 当前时间大于等于结束时间，则为活动已结束
            deleteIndex.add(i);
          }
        }
        for (int i = deleteIndex.size() - 1; i >= 0; i--) {
          response.getData().remove(i);
        }

        binding.layoutGroup.setVisibility(
            subject.getSubjectType().equals(SubjectType.GROUP) && !IsEmpty.list(response.getData())
                ? View.VISIBLE
                : View.GONE);
        binding.txtTitleLstGroup.setText(StringUtil
            .format(getString(R.string.subject_detail_group_count), response.getData().size()));
        SingleTypeAdapter adapter = new SingleTypeAdapter(SubjectDetailActivity.this,
            R.layout.item_group_buy);
        adapter.addAll(
            response.getData().size() >= 2 ? response.getData().subList(0, 2) : response.getData());
        adapter.setPresenter(new Presenter());
        adapter.setDecorator(new Decorator());
        LinearLayoutManager manager = new LinearLayoutManager(SubjectDetailActivity.this);
        binding.lstGroup.setLayoutManager(manager);
        binding.lstGroup.setNestedScrollingEnabled(false);
        binding.lstGroup.setAdapter(adapter);
      }
    });
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (listGroupBuyCase != null)
      listGroupBuyCase.unSubscribe();
  }

  public class Decorator implements BaseViewAdapter.Decorator {
    @Override
    public void decorator(BindingViewHolder holder, int position, int viewType) {
      if (holder == null || holder.getBinding() == null)
        return;
      final ItemGroupBuyBinding itemBinding = (ItemGroupBuyBinding) holder.getBinding();
      GroupCountDownQueue.getInstance().attach(itemBinding.txtEndTime,
          itemBinding.getItem().getEndTime());
    }
  }

  @Override
  protected void onDestroy() {
    GroupCountDownQueue.getInstance().cancel();
    super.onDestroy();
  }

  public class Presenter implements BaseViewAdapter.Presenter {

    /**
     * 地址详情
     *
     * @param shop
     */
    public void onAdd(Shop shop) {
      Intent intent = new Intent(SubjectDetailActivity.this, ShopMapActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, shop);
      startActivity(intent);
    }

    /**
     * 跳到门店界面
     * 
     * @param shop
     */
    public void onShopHome(Shop shop) {
      Intent intent = new Intent(SubjectDetailActivity.this, ShopMainActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, shop);
      startActivity(intent);
    }

    /**
     * 去参团
     * 
     * @param groupBuy
     */
    public void onGroupBuyClick(GroupBuy groupBuy) {
      OrderUtil.startOrderConfirmActivity(SubjectDetailActivity.this,
          OrderUtil.createOrder(subject, groupBuy.getId()), shop.getName(), true,
          subject.getUnit());

    }

    /**
     * 收藏/取消收藏
     * 
     * @param subject
     */
    public void onFav(final Subject subject) {
      if (subject.isFavorite()) {
        // 如果已收藏，则取消收藏
        new CancelFavoriteLikeCase(LikeEntityType.store, subject.getId())
            .execute(new HttpSubscriber<Void>(SubjectDetailActivity.this) {
              @Override
              public void onFailure(String errorMsg, Response<Void> response) {
                DialogUtil.getErrorDialog(SubjectDetailActivity.this, errorMsg).show();
              }

              @Override
              public void onSuccess(Response<Void> response) {
                ToastUtil.toastHint(SubjectDetailActivity.this, "取消收藏");
                subject.setFavorite(false);
                subject.setFavCount(subject.getFavCount() - 1);
                binding.setSubject(subject);
              }
            });
        return;
      }
      // 收藏该课程
      new FavoriteLikeCase(LikeEntityType.course, subject.getId())
          .execute(new HttpSubscriber<Void>(SubjectDetailActivity.this) {
            @Override
            public void onFailure(String errorMsg, Response<Void> response) {
              DialogUtil.getErrorDialog(SubjectDetailActivity.this, errorMsg).show();
            }

            @Override
            public void onSuccess(Response<Void> response) {
              ToastUtil.toastHint(SubjectDetailActivity.this, "收藏成功");
              subject.setFavorite(true);
              subject.setFavCount(subject.getFavCount() + 1);
              binding.setSubject(subject);
            }
          });
    }

    public void onMobile(final String mobile) {
      PhoneUtil.call(SubjectDetailActivity.this, mobile);
    }

    /**
     * 去支付
     * 
     * @param subject
     */
    public void onPay(Subject subject) {
      OrderUtil.startOrderConfirmActivity(SubjectDetailActivity.this,
          OrderUtil.createOrder(subject, null), shop.getName(),
          subject.getSubjectType().equals(SubjectType.GROUP), subject.getUnit());
    }

  }

}
