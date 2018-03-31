package com.dindong.dingdong.presentation.user.wrist;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.joda.time.DateTime;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityBlueWristAddBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.wrist.usecase.BindLshCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.Address;
import com.dindong.dingdong.network.bean.entity.Region;
import com.dindong.dingdong.network.bean.wrist.BlueWrist;
import com.dindong.dingdong.util.DateUtil;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.NavigationTopBar;
import com.dindong.dingdong.widget.SexDialog;
import com.dindong.dingdong.widget.date.DatePicker;
import com.dindong.dingdong.widget.picker.AddressPicker;
import com.dindong.dingdong.widget.picker.PickerDialog;
import com.wcong.validator.ValidateResultCall;
import com.wcong.validator.Validator;
import com.wcong.validator.rules.MinLengthRule;
import com.wcong.validator.rules.RequiredRule;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class BlueWristAddActivity extends BaseActivity {

  // http://www.dingdongbanxue.com/lsh/c1e384832e3492903febd058965df316
  ActivityBlueWristAddBinding binding;

  private Address tempAddress;// 缓存当前选择地址

  private Validator validator;

  private BlueWrist blueWrist;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_blue_wrist_add);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    // 设置默认值
    blueWrist = (BlueWrist) getIntent().getSerializableExtra(AppConfig.IntentKey.DATA);
    binding.txtWristNum.setText(blueWrist.getNum());
    binding.txtBirthday.setText(DateUtil.format(new Date(), DateUtil.DEFAULT_DATE_FORMAT_3));

    registerEditValidator();
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
   * 表单验证
   */
  private void registerEditValidator() {
    validator = new Validator();
    validator.register(binding.edtName, new RequiredRule(getString(R.string.wrist_add_empty_name)));
    validator.register(binding.edtMobile1,
        new RequiredRule(getString(R.string.wrist_add_empty_mobile)),
        new MinLengthRule(11, getString(R.string.wrist_add_len_mobile)));
  }

  private BlueWrist createWrist() {
    BlueWrist blueWrist = new BlueWrist();
    blueWrist.setId(this.blueWrist.getId());
    blueWrist.setNum(binding.txtWristNum.getText().toString());
    blueWrist.setName(binding.edtName.getText().toString().trim());
    blueWrist.setSex(
        binding.txtSex.getText().toString().equals(SexDialog.Sex.man.getName()) ? "0" : "1");
    try {
      blueWrist.setBirthday(
          DateUtil.parse(binding.txtBirthday.getText().toString(), DateUtil.DEFAULT_DATE_FORMAT_3));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    blueWrist.setPhone1(binding.edtMobile1.getText().toString().trim());
    blueWrist.setPhone2(binding.edtMobile2.getText().toString().trim());
    blueWrist.setPhone3(binding.edtMobile3.getText().toString().trim());
    if (tempAddress != null) {
      blueWrist.setAddress(tempAddress);
      tempAddress.setStreet(binding.edtStreet.getText().toString().trim());
    }

    blueWrist.setRemark(binding.edtRemark.getText().toString().trim());
    return blueWrist;
  }

  /**
   * 绑定手环
   */
  private void bindWrist() {
    new BindLshCase(createWrist()).execute(new HttpSubscriber<Object>(this) {
      @Override
      public void onFailure(String errorMsg, Response<Object> response) {
        DialogUtil.getErrorDialog(BlueWristAddActivity.this, errorMsg).show();
      }

      @Override
      public void onSuccess(Response<Object> response) {
        ToastUtil.toastSuccess(BlueWristAddActivity.this,
            getString(R.string.wrist_add_bind_success));
        startActivity(new Intent(BlueWristAddActivity.this, BlueWristMainActivity.class));
      }
    });
  }

  public class Presenter {
    /**
     * 地址选择
     */
    public void onAddSelect() {
      AddressPicker dialog = new AddressPicker(BlueWristAddActivity.this,
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

          binding.txtAdd.setText(tempAddress.getProvince().getName()
              + tempAddress.getCity().getName() + tempAddress.getDistrict().getName());
        }
      });
      dialog.show();
    }

    /**
     * 选择性别
     * 
     * @param view
     */
    public void onSexSelect(View view) {
      new SexDialog(BlueWristAddActivity.this)
          .setDefaultData(((TextView) view).getText().toString())
          .setDialogListener(new SexDialog.DialogListener() {
            @Override
            public void onSelect(SexDialog.Sex sex) {
              binding.txtSex.setText(sex.getName());
            }
          }).show();

    }

    /**
     * 日期选择
     */
    public void onDateSelect() {
      try {
        DatePicker dayPicker = new DatePicker.Builder(BlueWristAddActivity.this)
            .setSelectDate(
                TextUtils.isEmpty(binding.txtBirthday.getText().toString().trim()) ? new Date()
                    : DateUtil.parse(binding.txtBirthday.getText().toString(),
                        DateUtil.DEFAULT_DATE_FORMAT_3))
            .setMinDate(DateTime.now().minusYears(100).toDate()).setMaxDate(new Date())
            .setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
              @Override
              public void onDateSelected(Date date) {
                binding.txtBirthday.setText(DateUtil.format(date, DateUtil.DEFAULT_DATE_FORMAT_3));
              }

              @Override
              public void onCancel() {

              }
            }).create();
        dayPicker.show();
      } catch (ParseException e) {
        e.printStackTrace();
      }

    }

    public void onConfirm() {
      validator.validateAll(new ValidateResultCall() {
        @Override
        public void onSuccess() {
          // if (tempAddress == null || tempAddress.getProvince() == null
          // || tempAddress.getCity() == null) {
          // ToastUtil.toastHint(BlueWristAddActivity.this,
          // getString(R.string.wrist_add_empty_add));
          // return;
          // }
          bindWrist();

        }

        @Override
        public void onFailure(TextView view, String message) {
          ToastUtil.toastHint(BlueWristAddActivity.this, message);
        }
      });
    }
  }

}
