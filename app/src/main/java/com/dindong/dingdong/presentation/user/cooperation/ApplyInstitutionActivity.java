package com.dindong.dingdong.presentation.user.cooperation;

import java.util.Map;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityApplyInstitutionBinding;
import com.dindong.dingdong.databinding.LayoutUploadIdCardOppositeBinding;
import com.dindong.dingdong.databinding.LayoutUploadIdCardPositiveBinding;
import com.dindong.dingdong.databinding.LayoutUploadLicenseBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.apply.usecase.ApplyInstitutionCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.apply.RequestInstitutionApply;
import com.dindong.dingdong.network.bean.apply.ResponseApply;
import com.dindong.dingdong.network.bean.entity.Address;
import com.dindong.dingdong.network.bean.entity.Region;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.StringUtil;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.NavigationTopBar;
import com.dindong.dingdong.widget.picker.AddressPicker;
import com.dindong.dingdong.widget.picker.PickerDialog;
import com.wcong.validator.ValidateResultCall;
import com.wcong.validator.Validator;
import com.wcong.validator.rules.MinLengthRule;
import com.wcong.validator.rules.RequiredRule;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 机构申请
 */
public class ApplyInstitutionActivity extends BaseActivity {

  ActivityApplyInstitutionBinding binding;

  private Address tempAddress;// 缓存当前选择地址

  private Validator validator;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_apply_institution);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
    registerEditValidator();
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
   * 表单验证
   */
  private void registerEditValidator() {
    validator = new Validator();
    validator.register(binding.edtInstitutionName, new RequiredRule("机构名称不能为空"));
    validator.register(binding.edtContactName, new RequiredRule("联系人不能为空"));
    validator.register(binding.edtMobile,
        new RequiredRule(getString(R.string.wrist_add_empty_mobile)),
        new MinLengthRule(11, getString(R.string.wrist_add_len_mobile)));
  }

  /**
   * 初始化上传图片布局
   */
  private void initUploadPhoto() {
    final LayoutUploadLicenseBinding layoutUploadLicenseBinding = DataBindingUtil
        .inflate(LayoutInflater.from(this), R.layout.layout_upload_license, null, false);
    RelativeLayout.LayoutParams licenseLayoutParams = new RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
    layoutUploadLicenseBinding.getRoot().setLayoutParams(licenseLayoutParams);
    binding.license.setCustomAddView(layoutUploadLicenseBinding.getRoot());
    binding.license.setColumnCount(1);
    binding.license.setMaxLength(1);
    binding.license.setRatio(0.4f);
    binding.license.init();

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
   * 机构申请
   */
  private void applyTeacher() {
    RequestInstitutionApply requestInstitutionApply = new RequestInstitutionApply();
    requestInstitutionApply.setIdCardId1(binding.cardPositive.getSource().get(0).getId());
    requestInstitutionApply.setIdCardUrl1(binding.cardPositive.getSource().get(0).getUrl());

    requestInstitutionApply.setIdCardId2(binding.cardOpposite.getSource().get(0).getId());
    requestInstitutionApply.setIdCardUrl2(binding.cardOpposite.getSource().get(0).getUrl());

    requestInstitutionApply.setBusLicenseId(binding.license.getSource().get(0).getId());
    requestInstitutionApply.setBusLicenseUrl(binding.license.getSource().get(0).getUrl());

    requestInstitutionApply.setAddress(binding.edtStreet.getText().toString());
    requestInstitutionApply.setContact(binding.edtContactName.getText().toString());
    requestInstitutionApply.setFax(binding.edtFax.getText().toString());
    requestInstitutionApply.setName(binding.edtInstitutionName.getText().toString());
    requestInstitutionApply.setTel(binding.edtMobile.getText().toString());
    requestInstitutionApply.setProvinceCode(tempAddress.getProvince().getCode());
    requestInstitutionApply.setProvinceName(tempAddress.getProvince().getName());
    requestInstitutionApply.setCityCode(tempAddress.getCity().getCode());
    requestInstitutionApply.setCityName(tempAddress.getCity().getName());
    requestInstitutionApply.setDistrictCode(tempAddress.getDistrict().getCode());
    requestInstitutionApply.setDistrictName(tempAddress.getDistrict().getName());

    new ApplyInstitutionCase(requestInstitutionApply)
        .execute(new HttpSubscriber<ResponseApply>(this) {
          @Override
          public void onFailure(String errorMsg, Response<ResponseApply> response) {
            DialogUtil.getErrorDialog(ApplyInstitutionActivity.this, errorMsg).show();
          }

          @Override
          public void onSuccess(Response<ResponseApply> response) {
            ToastUtil.toastSuccess(ApplyInstitutionActivity.this, "提交成功");
            finish();
          }
        });
  }

  public class Presenter {
    /**
     * 地址选择
     */
    public void onAddSelect() {
      AddressPicker dialog = new AddressPicker(ApplyInstitutionActivity.this,
          tempAddress != null ? tempAddress.getProvince() : null,
          tempAddress != null ? tempAddress.getCity() : null,
          tempAddress != null ? tempAddress.getDistrict() : null);
      dialog.setListener(new PickerDialog.ISelectListener() {
        @Override
        public void onSelect(Map<Integer, Region> map) {
          if (map == null || map.isEmpty())
            return;
          tempAddress = new Address();
          tempAddress.setProvince(map.get(0));
          tempAddress.setCity(map.get(1));
          tempAddress.setDistrict(map.get(2));

          binding.txtAdd.setTextColor(
              ContextCompat.getColor(ApplyInstitutionActivity.this, R.color.light_black));
          binding.txtAdd.setText(tempAddress.getProvince().getName()
              + tempAddress.getCity().getName() + tempAddress.getDistrict().getName());
        }
      });
      dialog.show();
    }

    /**
     * 提交申请
     */
    public void onConfirm() {
      validator.validateAll(new ValidateResultCall() {
        @Override
        public void onSuccess() {
          if (!StringUtil.checkMobile(binding.edtMobile.getText().toString())) {
            ToastUtil.toastHint(ApplyInstitutionActivity.this, "手机号不正确");
            return;
          }
          if (tempAddress == null || tempAddress.getProvince() == null
              || tempAddress.getCity() == null) {
            ToastUtil.toastHint(ApplyInstitutionActivity.this,
                getString(R.string.wrist_add_empty_add));
            return;
          }
          if (IsEmpty.list(binding.license.getSource())) {
            ToastUtil.toastHint(ApplyInstitutionActivity.this, "营业执照不能为空");
            return;
          }
          if (IsEmpty.list(binding.cardPositive.getSource())) {
            ToastUtil.toastHint(ApplyInstitutionActivity.this, "正面身份证件不能为空");
            return;
          }
          if (IsEmpty.list(binding.cardOpposite.getSource())) {
            ToastUtil.toastHint(ApplyInstitutionActivity.this, "反面身份证件不能为空");
            return;
          }
          applyTeacher();

        }

        @Override
        public void onFailure(TextView view, String message) {
          ToastUtil.toastHint(ApplyInstitutionActivity.this, message);
        }
      });

    }
  }
}
