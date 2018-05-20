package com.dindong.dingdong.widget;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by wcong on 2018/5/20.
 * <p>
 * </>
 */

public abstract class TextWatchAdapter implements TextWatcher {

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
  }

  @Override
  public abstract void onTextChanged(CharSequence s, int start, int before, int count);

  @Override
  public void afterTextChanged(Editable s) {
  }
}
