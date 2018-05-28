package com.dindong.dingdong.presentation.user.cooperation;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityApplyProxyBinding;
import com.dindong.dingdong.databinding.LayoutUploadIdCardOppositeBinding;
import com.dindong.dingdong.databinding.LayoutUploadIdCardPositiveBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.apply.usecase.ApplyProxyCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.apply.RequestApply;
import com.dindong.dingdong.network.bean.apply.ResponseApply;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.IdentityUtils;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.NavigationTopBar;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

/**
 * 申请代理
 */
public class ApplyProxyActivity extends BaseActivity {
  ActivityApplyProxyBinding binding;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_apply_proxy);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
    initUploadPhoto();
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {

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

  /**
   * 初始化上传图片布局
   */
  private void initUploadPhoto() {
    final LayoutUploadIdCardPositiveBinding layoutUploadIdCardPositiveBinding = DataBindingUtil
        .inflate(LayoutInflater.from(this), R.layout.layout_upload_id_card_positive, null, false);
    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
    layoutUploadIdCardPositiveBinding.getRoot().setLayoutParams(layoutParams);
    binding.cardPositive.setCustomAddView(layoutUploadIdCardPositiveBinding.getRoot());
    binding.cardPositive.setColumnCount(1);
    binding.cardPositive.setMaxLength(1);
    binding.cardPositive.setRatio(0.4f);
    binding.cardPositive.init();

    final LayoutUploadIdCardOppositeBinding layoutUploadIdCardOppositeBinding = DataBindingUtil
        .inflate(LayoutInflater.from(this), R.layout.layout_upload_id_card_opposite, null, false);
    RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
    layoutUploadIdCardOppositeBinding.getRoot().setLayoutParams(layoutParams2);
    binding.cardOpposite.setCustomAddView(layoutUploadIdCardOppositeBinding.getRoot());
    binding.cardOpposite.setColumnCount(1);
    binding.cardOpposite.setMaxLength(1);
    binding.cardOpposite.setRatio(0.4f);
    binding.cardOpposite.init();
  }

  /**
   * 申请代理
   */
  private void applyProxy() {
    RequestApply requestApply = new RequestApply();
    requestApply.setIdCardId1(binding.cardPositive.getSource().get(0).getId());
    requestApply.setIdCardUrl1(binding.cardPositive.getSource().get(0).getUrl());

    requestApply.setIdCardId2(binding.cardOpposite.getSource().get(0).getId());
    requestApply.setIdCardUrl2(binding.cardOpposite.getSource().get(0).getUrl());

    requestApply.setName(binding.edtName.getText().toString());
    requestApply.setIdCardNo(binding.edtIdCard.getText().toString());

    new ApplyProxyCase(requestApply).execute(new HttpSubscriber<ResponseApply>(this) {
      @Override
      public void onFailure(String errorMsg, Response<ResponseApply> response) {
        DialogUtil.getErrorDialog(ApplyProxyActivity.this, errorMsg).show();
      }

      @Override
      public void onSuccess(Response<ResponseApply> response) {
        ToastUtil.toastSuccess(ApplyProxyActivity.this, "提交成功");
        finish();
      }
    });
  }

  public class Presenter {
    /**
     * 提交申请
     */
    public void onConfirm() {
      if (IsEmpty.string(binding.edtName.getText().toString())) {
        ToastUtil.toastHint(ApplyProxyActivity.this, "姓名不能为空");
        return;
      }
      if (IsEmpty.string(binding.edtIdCard.getText().toString())) {
        ToastUtil.toastHint(ApplyProxyActivity.this, "身份证号不能为空");
        return;
      }
      if (!IdentityUtils.checkIDCard(binding.edtIdCard.getText().toString())) {
        ToastUtil.toastHint(ApplyProxyActivity.this, "请输入正确身份证号");
        return;
      }
      if (IsEmpty.list(binding.cardPositive.getSource())) {
        ToastUtil.toastHint(ApplyProxyActivity.this, "正面身份证件不能为空");
        return;
      }
      if (IsEmpty.list(binding.cardOpposite.getSource())) {
        ToastUtil.toastHint(ApplyProxyActivity.this, "反面身份证件不能为空");
        return;
      }
      applyProxy();
    }
  }
}
