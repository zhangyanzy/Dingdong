package com.dindong.dingdong.widget.upgrade;

import com.dindong.dingdong.R;
import com.dindong.dingdong.databinding.DialogAppUpgradeBinding;
import com.dindong.dingdong.util.DensityUtil;
import com.dindong.dingdong.util.StringUtil;
import com.dindong.dingdong.widget.EnterInfoDialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.RelativeLayout;

/**
 * Created by wcong on 2018/5/27.
 * <p>
 * APP更新弹框</>
 */

public class AppUpgradeDialog extends Dialog {
  private String versionName;// 版本名
  private String description;// 版本说明
  private EnterInfoDialog.OnConfirmListener onConfirmListener;

  DialogAppUpgradeBinding binding;

  public AppUpgradeDialog(@NonNull Context context, String versionName, String description,
      EnterInfoDialog.OnConfirmListener onConfirmListener) {
    super(context, R.style.FullScreenDialog);
    this.versionName = versionName;
    this.description = description;
    this.onConfirmListener = onConfirmListener;

    initView();
  }

  private void initView() {
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setCanceledOnTouchOutside(false);
    binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
        R.layout.dialog_app_upgrade, null, false);
    binding.setPresenter(new Presenter());

    binding.tvTitle.setText(StringUtil.format("V{0} 版本特征", versionName));
    binding.tvContent.setText(description);

    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
        DensityUtil.dip2px(getContext(), 283), DensityUtil.dip2px(getContext(), 384));
    setContentView(binding.getRoot(), params);
  }

  public class Presenter {
    public void onConfirm() {
      dismiss();
      if (onConfirmListener != null)
        onConfirmListener.onConfirm(null);
    }
  }

}
