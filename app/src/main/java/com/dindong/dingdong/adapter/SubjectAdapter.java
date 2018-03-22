package com.dindong.dingdong.adapter;

import com.dindong.dingdong.R;
import com.dindong.dingdong.databinding.ItemGlobalSubjectBinding;
import com.dindong.dingdong.network.bean.shop.Shop;
import com.dindong.dingdong.widget.baseadapter.BaseViewAdapter;
import com.dindong.dingdong.widget.baseadapter.BindingViewHolder;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by wcong on 2018/3/20.
 * <p>
 * </>
 */

public class SubjectAdapter extends SingleTypeAdapter {
  private Shop shop = null;

  public SubjectAdapter(Context context) {
    super(context, R.layout.item_global_subject);
    setDecorator(new Decorator());
  }

  public void setPresenter(SubjectPresenter subjectPresenter) {
    super.setPresenter(subjectPresenter);
  }

  public void setShop(Shop shop) {
    this.shop = shop;
  }

  class Decorator implements BaseViewAdapter.Decorator {
    @Override
    public void decorator(BindingViewHolder holder, int position, int viewType) {
      if (holder == null || holder.getBinding() == null)
        return;
      ItemGlobalSubjectBinding itemBinding = (ItemGlobalSubjectBinding) holder.getBinding();
      itemBinding.setShop(shop);
      itemBinding.txtOriginal.setVisibility(itemBinding.getItem().getAmount()
          .compareTo(itemBinding.getItem().getOriginalAmount()) == 0 ? View.GONE : View.VISIBLE);
      itemBinding.txtOriginal.getPaint()
          .setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
    }
  }
}
