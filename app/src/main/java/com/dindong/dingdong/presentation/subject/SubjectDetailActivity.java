package com.dindong.dingdong.presentation.subject;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivitySubjectDetailBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.groupbuy.usecase.ListGroupBuyCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.GlobalImage;
import com.dindong.dingdong.network.bean.groupbuy.GroupBuy;
import com.dindong.dingdong.network.bean.store.Shop;
import com.dindong.dingdong.network.bean.store.Subject;
import com.dindong.dingdong.network.bean.store.SubjectType;
import com.dindong.dingdong.presentation.pay.OrderConfirmActivity;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.PhoneUtil;
import com.dindong.dingdong.util.PhotoUtil;
import com.dindong.dingdong.util.StringUtil;
import com.dindong.dingdong.widget.NavigationTopBar;
import com.dindong.dingdong.widget.baseadapter.BaseViewAdapter;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
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

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_subject_detail);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
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
      binding.setSubject(subject);
      initSubjectImg(subject.getImage());
      initSubjectDescriptionImg(subject.getImages());
    }
    if (getIntent().getSerializableExtra(AppConfig.IntentKey.SUMMARY) != null) {
      binding.setShop((Shop) getIntent().getSerializableExtra(AppConfig.IntentKey.SUMMARY));
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (subject != null && binding != null)
      listGroup(subject.getId());
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

  public class Presenter implements BaseViewAdapter.Presenter {

    public void onGroupBuyClick(GroupBuy groupBuy) {
      Intent intent = new Intent(SubjectDetailActivity.this, OrderConfirmActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, subject);
      intent.putExtra("groupId", groupBuy.getId());
      startActivity(intent);
    }

    public void onFav() {

    }

    public void onMobile(final String mobile) {
      PhoneUtil.call(SubjectDetailActivity.this, mobile);
    }

    public void onPay(Subject subject) {
      // ToastUtil.toastHint(SubjectDetailActivity.this, "暂未开放");
      Intent intent = new Intent(SubjectDetailActivity.this, OrderConfirmActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, subject);
      startActivity(intent);
    }
  }

}
