package com.dindong.dingdong.presentation.subject;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivitySubjectDetailBinding;
import com.dindong.dingdong.network.bean.entity.GlobalImage;
import com.dindong.dingdong.network.bean.store.Shop;
import com.dindong.dingdong.network.bean.store.Subject;
import com.dindong.dingdong.presentation.pay.OrderConfirmActivity;
import com.dindong.dingdong.util.PhotoUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.widget.NavigationTopBar;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class SubjectDetailActivity extends BaseActivity {

  ActivitySubjectDetailBinding binding;

  private Subject subject;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_subject_detail);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    if (getIntent().getSerializableExtra(AppConfig.IntentKey.DATA) != null) {
      subject = (Subject) getIntent().getSerializableExtra(AppConfig.IntentKey.DATA);
      binding.setSubject(subject);
      initSubjectImg(subject.getImages());
    }
    if (getIntent().getSerializableExtra(AppConfig.IntentKey.SUMMARY) != null) {
      binding.setShop((Shop) getIntent().getSerializableExtra(AppConfig.IntentKey.SUMMARY));
    }
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

  private void initSubjectImg(List<GlobalImage> images) {
    WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics dm = new DisplayMetrics();
    wm.getDefaultDisplay().getMetrics(dm);
    int width = dm.widthPixels;
    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.imgSubject
        .getLayoutParams();
    params.height = (int) (width * 0.5);
    binding.imgSubject.setLayoutParams(params);
    PhotoUtil.load(this, IsEmpty.list(images) ? "" : images.get(0).getUrl(), binding.imgSubject);
  }

  public class Presenter {
    public void onFav() {

    }

    public void onPay(Subject subject) {
//      ToastUtil.toastHint(SubjectDetailActivity.this, "暂未开放");
      Intent intent = new Intent(SubjectDetailActivity.this, OrderConfirmActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, subject);
      startActivity(intent);
    }
  }

}
