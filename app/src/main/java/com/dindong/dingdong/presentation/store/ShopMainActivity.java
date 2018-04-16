package com.dindong.dingdong.presentation.store;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.adapter.SubjectAdapter;
import com.dindong.dingdong.adapter.SubjectPresenter;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityShopMainBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.shop.usecase.ListTeacherCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.GlobalImage;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.store.Shop;
import com.dindong.dingdong.network.bean.store.Subject;
import com.dindong.dingdong.network.bean.store.Teacher;
import com.dindong.dingdong.presentation.subject.SubjectDetailActivity;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.GlideUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.NavigationTopBar;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;
import com.dindong.dingdong.widget.sweetAlert.SweetAlertDialog;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class ShopMainActivity extends BaseActivity {

  ActivityShopMainBinding binding;

  private Shop shop;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_main);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    if (getIntent().getSerializableExtra(AppConfig.IntentKey.DATA) != null) {
      shop = (Shop) getIntent().getSerializableExtra(AppConfig.IntentKey.DATA);
      binding.setStore(shop);
      initShopImg(shop.getImages());
      initHotSubject(shop.getSubjects());
      listTeacher(shop.getId());
    }
  }

  /**
   * 加载门店图片
   *
   * @param images
   */
  private void initShopImg(List<GlobalImage> images) {
    WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics dm = new DisplayMetrics();
    wm.getDefaultDisplay().getMetrics(dm);
    int width = dm.widthPixels;
    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.imgShop
        .getLayoutParams();
    params.height = (int) (width * 0.7);
    binding.imgShop.setLayoutParams(params);
    GlideUtil.load(this, IsEmpty.list(images) ? "" : images.get(0).getUrl(), binding.imgShop);
  }

  /**
   * 加载热门课程
   *
   * @param subjects
   */
  private void initHotSubject(List<Subject> subjects) {
    if (IsEmpty.list(subjects))
      return;
    SubjectAdapter subjectAdapter = new SubjectAdapter(this);
    subjectAdapter.setPresenter(new Presenter());
    subjectAdapter.set(subjects);
    subjectAdapter.setShop(shop);
    LinearLayoutManager manager = new LinearLayoutManager(this);
    manager.setOrientation(LinearLayoutManager.VERTICAL);
    binding.lstHotSubject.setLayoutManager(manager);
    binding.lstHotSubject.setAdapter(subjectAdapter);

  }

  /**
   * 获取老师列表
   */
  private void listTeacher(String shopId) {
    final QueryParam param = new QueryParam();
    param.setLimit(2);

    new ListTeacherCase(shopId, param).execute(new HttpSubscriber<List<Teacher>>() {
      @Override
      public void onFailure(String errorMsg, Response<List<Teacher>> response) {
        DialogUtil.getErrorDialog(ShopMainActivity.this, errorMsg).show();
      }

      @Override
      public void onSuccess(Response<List<Teacher>> response) {
        SingleTypeAdapter adapter = new SingleTypeAdapter(ShopMainActivity.this,
            R.layout.item_teahcer_list);
        adapter.addAll(response.getData());
        LinearLayoutManager manager = new LinearLayoutManager(ShopMainActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.lstTeacher.setLayoutManager(manager);
        binding.lstTeacher.setAdapter(adapter);

      }
    });
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

  public class Presenter implements SubjectPresenter {

    @Override
    public void onSubjectItemClick(Subject subject) {
      Intent intent = new Intent(ShopMainActivity.this, SubjectDetailActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, subject);
      intent.putExtra(AppConfig.IntentKey.SUMMARY, shop);
      startActivity(intent);
    }

    /**
     * 写评论
     */
    public void onComment(View view) {
      ToastUtil.toastHint(ShopMainActivity.this, "暂不支持");
    }

    public void onMobile(final String mobile) {
      SweetAlertDialog dialog = DialogUtil.getConfirmDialog(ShopMainActivity.this, mobile);
      dialog.setConfirmText(getString(R.string.shop_main_mobile));
      dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {
          try {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile)));
          } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.toastFailure(ShopMainActivity.this, getString(R.string.shop_main_mobile_err));
          }
          sweetAlertDialog.dismiss();
        }
      });
      dialog.setCanceledOnTouchOutside(true);
      dialog.show();
    }

    /**
     * 更多教师
     */
    public void onMoreTeacher() {
      Intent intent = new Intent(ShopMainActivity.this, TeacherListActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, shop);
      startActivity(intent);
    }

    /**
     * 地址详情
     *
     * @param latitude
     * @param longitude
     */
    public void onAdd(String latitude, String longitude) {
      Intent intent = new Intent(ShopMainActivity.this, ShopMapActivity.class);
      intent.putExtra(AppConfig.IntentKey.LATITUDE, latitude);
      intent.putExtra(AppConfig.IntentKey.LONGITUDE, longitude);
      startActivity(intent);
    }

  }
}
