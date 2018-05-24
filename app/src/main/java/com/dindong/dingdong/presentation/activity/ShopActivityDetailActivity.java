package com.dindong.dingdong.presentation.activity;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityShopActivityDetailBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.activity.usecase.GetShopActivityCase;
import com.dindong.dingdong.network.api.like.usecase.CancelFavoriteLikeCase;
import com.dindong.dingdong.network.api.like.usecase.FavoriteLikeCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.activity.ShopActivity;
import com.dindong.dingdong.network.bean.entity.GlobalImage;
import com.dindong.dingdong.network.bean.like.LikeEntityType;
import com.dindong.dingdong.network.bean.store.Shop;
import com.dindong.dingdong.presentation.pay.OrderUtil;
import com.dindong.dingdong.presentation.store.ShopMapActivity;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.PhoneUtil;
import com.dindong.dingdong.util.PhotoUtil;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.NavigationTopBar;
import com.dindong.dingdong.widget.baseadapter.BaseViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * 门店活动详情
 */
public class ShopActivityDetailActivity extends BaseActivity {

  private ActivityShopActivityDetailBinding binding;
  private Shop shop;

  private ShopActivity shopActivity;

  @Override
  protected void initComponent() {

    binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_activity_detail);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
    binding.txtOriginal.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    if (getIntent().getSerializableExtra(AppConfig.IntentKey.DATA) != null) {
      shopActivity = (ShopActivity) getIntent().getSerializableExtra(AppConfig.IntentKey.DATA);
      binding.setShopActivity(shopActivity);
      initLogo(shopActivity.getImage());
      initDescriptionImg(shopActivity.getImages());
      getShopActivity(shopActivity.getId());
    }

    if (getIntent().getSerializableExtra(AppConfig.IntentKey.SUMMARY) != null) {
      shop = (Shop) getIntent().getSerializableExtra(AppConfig.IntentKey.SUMMARY);
      binding.setShop(shop);
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

  /**
   * 加载主图
   *
   * @param image
   */
  private void initLogo(GlobalImage image) {
    WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics dm = new DisplayMetrics();
    wm.getDefaultDisplay().getMetrics(dm);
    int width = dm.widthPixels;
    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.imgLogo
        .getLayoutParams();
    params.height = (int) (width * 0.4);
    binding.imgLogo.setLayoutParams(params);
    PhotoUtil.load(this, image == null ? "" : image.getUrl(), binding.imgLogo);
  }

  /**
   * 加载商品图片
   *
   * @param images
   */
  private void initDescriptionImg(List<GlobalImage> images) {
    WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics dm = new DisplayMetrics();
    wm.getDefaultDisplay().getMetrics(dm);
    int width = dm.widthPixels;
    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.imgDescription
        .getLayoutParams();
    params.height = (int) (width * 0.5);
    binding.imgDescription.setLayoutParams(params);
    PhotoUtil.load(this, IsEmpty.list(images) ? "" : images.get(0).getUrl(),
        binding.imgDescription);
  }

  /**
   * 获取活动信息
   *
   * @param goodId
   */
  private void getShopActivity(String goodId) {
    new GetShopActivityCase(goodId).execute(new HttpSubscriber<ShopActivity>() {
      @Override
      public void onFailure(String errorMsg, Response<ShopActivity> response) {

      }

      @Override
      public void onSuccess(Response<ShopActivity> response) {
        // 获取到最新信息重新加载
        shopActivity = response.getData();
        binding.setShopActivity(shopActivity);
        initLogo(shopActivity.getImage());
        initDescriptionImg(shopActivity.getImages());
      }
    });
  }

  public class Presenter implements BaseViewAdapter.Presenter {

    /**
     * 地址详情
     *
     * @param shop
     */
    public void onAdd(Shop shop) {
      Intent intent = new Intent(ShopActivityDetailActivity.this, ShopMapActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, shop);
      startActivity(intent);
    }

    /**
     * 收藏/取消收藏
     *
     * @param shopActivity
     */
    public void onFav(final ShopActivity shopActivity) {
      if (shopActivity.isFavorite()) {
        // 如果已收藏，则取消收藏
        new CancelFavoriteLikeCase(LikeEntityType.store, shopActivity.getId())
            .execute(new HttpSubscriber<Void>(ShopActivityDetailActivity.this) {
              @Override
              public void onFailure(String errorMsg, Response<Void> response) {
                DialogUtil.getErrorDialog(ShopActivityDetailActivity.this, errorMsg).show();
              }

              @Override
              public void onSuccess(Response<Void> response) {
                ToastUtil.toastHint(ShopActivityDetailActivity.this, "取消收藏");
                shopActivity.setFavorite(false);
                shopActivity.setFavCount(shopActivity.getFavCount() - 1);
                binding.setShopActivity(shopActivity);
              }
            });
        return;
      }
      // 收藏该课程
      new FavoriteLikeCase(LikeEntityType.course, shopActivity.getId())
          .execute(new HttpSubscriber<Void>(ShopActivityDetailActivity.this) {
            @Override
            public void onFailure(String errorMsg, Response<Void> response) {
              DialogUtil.getErrorDialog(ShopActivityDetailActivity.this, errorMsg).show();
            }

            @Override
            public void onSuccess(Response<Void> response) {
              ToastUtil.toastHint(ShopActivityDetailActivity.this, "收藏成功");
              shopActivity.setFavorite(true);
              shopActivity.setFavCount(shopActivity.getFavCount() + 1);
              binding.setShopActivity(shopActivity);
            }
          });
    }

    public void onMobile(final String mobile) {
      PhoneUtil.call(ShopActivityDetailActivity.this, mobile);
    }

    /**
     * 去支付
     *
     * @param shopActivity
     */
    public void onPay(ShopActivity shopActivity) {
      OrderUtil.startOrderConfirmActivity(ShopActivityDetailActivity.this,
          OrderUtil.createOrder(shopActivity), shop.getName(), false, shopActivity.getUnit());
    }

  }

}
