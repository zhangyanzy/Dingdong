package com.dindong.dingdong.widget;

import com.dindong.dingdong.R;
import com.dindong.dingdong.databinding.DialogEnterInfoBinding;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by wcong on 2018/5/20.
 * <p>
 * 编辑弹框</>
 */

public class EnterInfoDialog extends Dialog {
  private Context mContext;
  private DialogEnterInfoBinding mBinding;
  private OnConfirmListener mOnConfirmListener;
  private int mLength;

  public EnterInfoDialog(@NonNull Context context, String title, OnConfirmListener listener) {
    super(context, R.style.FullScreenDialog);
    this.mContext = context;
    this.mOnConfirmListener = listener;
    mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_enter_info, null,
        false);
    setContentView(mBinding.getRoot());

    mBinding.setPresenter(new Presenter());

    mBinding.tvTitle.setText(title);

    init();
  }

  public void setDefaultValue(String defaultValue){
    mBinding.etCode.setText(defaultValue);
  }

  @Override
  public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
    if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
      mBinding.btnConfirm.performClick();
    }
    return super.dispatchKeyEvent(event);
  }

  private void init() {
    // mBinding.etCode.setOnEditorActionListener(new
    // TextView.OnEditorActionListener() {
    // @Override
    // public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
    // if (actionId == EditorInfo.IME_ACTION_DONE) {
    // new Presenter().onConfirm();
    // return true;
    // }
    // return false;
    // }
    // });

    mBinding.etCode.addTextChangedListener(new TextWatchAdapter() {
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mLength > 0) {
          if (s.length() <= mLength) {
            mBinding.btnConfirm.setEnabled(true);
          } else {
            mBinding.btnConfirm.setEnabled(false);
          }
        } else {
          if (s.length() > 0) {
            mBinding.btnConfirm.setEnabled(true);
          } else {
            mBinding.btnConfirm.setEnabled(false);
          }
        }
      }
    });

    softInputShow(true);
  }

  public void setHint(String hint) {
    mBinding.etCode.setHint(hint);
  }

  public void setInputType(int inputType) {
    switch (inputType) {
    case 0:
      mBinding.etCode.setInputType(InputType.TYPE_CLASS_TEXT);
      break;
    case 1:
      mBinding.etCode.setInputType(InputType.TYPE_CLASS_NUMBER);
      break;
    case 2:
      mBinding.etCode
          .setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
      break;
    case 3:
      mBinding.etCode.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
      break;
    default:
      break;
    }
  }

  public void setEnterLength(int length) {
    mLength = length;
    mBinding.etCode.setFilters(new InputFilter[] {
        new InputFilter.LengthFilter(length) });
  }

  public void setCancelText(String cancel) {
    mBinding.btnCancel.setText(cancel);
  }

  public void setCancelTextColor(int color) {
    mBinding.btnCancel.setTextColor(color);
  }

  public void setConfirmText(String confirm) {
    mBinding.btnConfirm.setText(confirm);
  }

  public void setConfirmTextColor(int color) {
    mBinding.btnConfirm.setTextColor(color);
  }

  private void softInputShow(boolean isShow) {
    InputMethodManager imm = (InputMethodManager) getContext()
        .getSystemService(Context.INPUT_METHOD_SERVICE);
    if (isShow) {
      imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    } else {
      imm.hideSoftInputFromWindow(mBinding.etCode.getWindowToken(), 0);
    }
  }

  @Override
  public void dismiss() {
    softInputShow(false);
    super.dismiss();
  }

  public class Presenter {
    public void onCancel() {
      dismiss();
    }

    public void onConfirm() {
      dismiss();
      if (mOnConfirmListener != null) {
        mOnConfirmListener.onConfirm(mBinding.etCode.getText().toString().trim());
      }
    }
  }

  public interface OnConfirmListener {
    void onConfirm(String code);
  }
}
