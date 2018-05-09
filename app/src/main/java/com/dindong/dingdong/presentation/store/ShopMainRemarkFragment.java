package com.dindong.dingdong.presentation.store;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseFragment;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.FragmentShopMainRemarkBinding;
import com.dindong.dingdong.network.bean.entity.GlobalImage;
import com.dindong.dingdong.network.bean.store.Shop;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.PhotoUtil;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by wcong on 2018/5/8.
 * <p>
 * 门店主页-机构介绍</>
 */

public class ShopMainRemarkFragment extends BaseFragment {
  FragmentShopMainRemarkBinding binding;

  @Override
  protected View initComponent(LayoutInflater inflater, ViewGroup container) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop_main_remark, container,
        false);

    return binding.getRoot();
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    if (getArguments().getSerializable(AppConfig.IntentKey.DATA) != null) {
      initShopImg(((Shop) getArguments().getSerializable(AppConfig.IntentKey.DATA)).getLogoImage());
      ((ShopMainActivity)getActivity()).updateViewPagerHeight();
    }

  }

  private void initShopImg(GlobalImage image) {
    WindowManager wm = (WindowManager) this.getActivity().getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics dm = new DisplayMetrics();
    wm.getDefaultDisplay().getMetrics(dm);
    int width = dm.widthPixels;
    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.imgShop
        .getLayoutParams();
    params.height = (int) (width * 0.5);
    binding.imgShop.setLayoutParams(params);
    PhotoUtil.load(getContext(), IsEmpty.string(image.getUrl()) ? "" : image.getUrl(),
        binding.imgShop);
  }

  public class Presenter {

  }

}
