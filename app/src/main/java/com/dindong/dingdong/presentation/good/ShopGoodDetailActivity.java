package com.dindong.dingdong.presentation.good;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityShopGoodDetailBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.good.usecase.GetGoodCase;
import com.dindong.dingdong.network.api.like.usecase.CancelFavoriteLikeCase;
import com.dindong.dingdong.network.api.like.usecase.FavoriteLikeCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.GlobalImage;
import com.dindong.dingdong.network.bean.good.ShopGood;
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
 * 门店商品详情
 */
public class ShopGoodDetailActivity extends BaseActivity {
  ActivityShopGoodDetailBinding binding;
  private Shop shop;

  private ShopGood shopGood;

  @Override
  protected void initComponent() {

    binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_good_detail);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
    binding.txtOriginal.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    if (getIntent().getSerializableExtra(AppConfig.IntentKey.DATA) != null) {
      shopGood = (ShopGood) getIntent().getSerializableExtra(AppConfig.IntentKey.DATA);
      binding.setGood(shopGood);
      initLogo(shopGood.getImage());
      initDescriptionImg(shopGood.getImages());
      getShopGood(shopGood.getId());
    }

    if (getIntent().getSerializableExtra(AppConfig.IntentKey.SUMMARY) != null) {
      shop = (Shop) getIntent().getSerializableExtra(AppConfig.IntentKey.SUMMARY);
      binding.setShop(shop);
    }

    if (!IsEmpty.string(getIntent().getStringExtra(AppConfig.IntentKey.ID))) {
      getShopGood(getIntent().getStringExtra(AppConfig.IntentKey.ID));
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
   * 获取商品信息
   *
   * @param goodId
   */
  private void getShopGood(String goodId) {
    new GetGoodCase(goodId).execute(new HttpSubscriber<ShopGood>() {
      @Override
      public void onFailure(String errorMsg, Response<ShopGood> response) {

      }

      @Override
      public void onSuccess(Response<ShopGood> response) {
        // 获取到最新信息重新加载
        shopGood = response.getData();
        if (response.getData().getStore() != null) {
          shop = response.getData().getStore();
          binding.setShop(shop);
        }
        binding.setGood(shopGood);
        initLogo(shopGood.getImage());
        initDescriptionImg(shopGood.getImages());
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
      Intent intent = new Intent(ShopGoodDetailActivity.this, ShopMapActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, shop);
      startActivity(intent);
    }

    /**
     * 收藏/取消收藏
     *
     * @param good
     */
    public void onFav(final ShopGood good) {
      if (good.isFavorite()) {
        // 如果已收藏，则取消收藏
        new CancelFavoriteLikeCase(LikeEntityType.store, good.getId())
            .execute(new HttpSubscriber<Void>(ShopGoodDetailActivity.this) {
              @Override
              public void onFailure(String errorMsg, Response<Void> response) {
                DialogUtil.getErrorDialog(ShopGoodDetailActivity.this, errorMsg).show();
              }

              @Override
              public void onSuccess(Response<Void> response) {
                ToastUtil.toastHint(ShopGoodDetailActivity.this, "取消收藏");
                good.setFavorite(false);
                good.setFavCount(good.getFavCount() - 1);
                binding.setGood(good);
              }
            });
        return;
      }
      // 收藏该课程
      new FavoriteLikeCase(LikeEntityType.course, good.getId())
          .execute(new HttpSubscriber<Void>(ShopGoodDetailActivity.this) {
            @Override
            public void onFailure(String errorMsg, Response<Void> response) {
              DialogUtil.getErrorDialog(ShopGoodDetailActivity.this, errorMsg).show();
            }

            @Override
            public void onSuccess(Response<Void> response) {
              ToastUtil.toastHint(ShopGoodDetailActivity.this, "收藏成功");
              good.setFavorite(true);
              good.setFavCount(good.getFavCount() + 1);
              binding.setGood(good);
            }
          });
    }

    public void onMobile(final String mobile) {
      PhoneUtil.call(ShopGoodDetailActivity.this, mobile);
    }

    /**
     * 去支付
     *
     * @param good
     */
    public void onPay(ShopGood good) {
      OrderUtil.startOrderConfirmActivity(ShopGoodDetailActivity.this, OrderUtil.createOrder(good),
          shop.getName(), false, good.getUnit());
    }

  }

}
