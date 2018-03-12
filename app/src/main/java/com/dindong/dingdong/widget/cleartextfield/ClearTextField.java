package com.dindong.dingdong.widget.cleartextfield;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dindong.dingdong.R;
import com.dindong.dingdong.widget.cleartextfield.validator.FieldValidateError;

/**
 * Created by wangcong on 2017/1/11.
 */

public class ClearTextField extends LinearLayout {

  private ClearEditText clearEditText;
  private LabelTxt labelTxt;

  public ClearTextField(Context context) {
    super(context);
    init(context);
  }

  public ClearTextField(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public ClearTextField(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  private void init(Context context) {
    init(context, null);
  }

  private void init(Context context, AttributeSet attrs) {
    setOrientation(HORIZONTAL);
    if (attrs != null) {
      labelTxt = new LabelTxt(context, attrs);
      clearEditText = new ClearEditText(context, attrs, true);
      addView(labelTxt);
      addView(clearEditText);
      setGravity(Gravity.CENTER_VERTICAL);
      int type = getContext().obtainStyledAttributes(attrs, R.styleable.ClearTextField)
          .getInt(R.styleable.ClearTextField_edit_inputType, EditorInfo.TYPE_CLASS_TEXT);
      getClearEditText().setInputType(
          type == 0 ? EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD : type);
    }
  }

  public static int px2dip(Context context, float pxValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);
  }

  public static int dip2px(Context context, float dpValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }

  public static void setGravity(TextView view, int gravity) {
    switch (gravity) {
    case FieldGravity.GRAVITY_LEFT:
      view.setGravity(Gravity.LEFT);
      break;
    case FieldGravity.GRAVITY_TOP:
      view.setGravity(Gravity.TOP);
      break;
    case FieldGravity.GRAVITY_RIGHT:
      view.setGravity(Gravity.RIGHT);
      break;
    case FieldGravity.GRAVITY_BOTTOM:
      view.setGravity(Gravity.BOTTOM);
      break;
    case FieldGravity.CENTER:
      view.setGravity(Gravity.CENTER);
      break;
    case FieldGravity.CENTER_VERTICAL:
      view.setGravity(Gravity.CENTER_VERTICAL);
      break;
    case FieldGravity.CENTER_HORIZONTAL:
      view.setGravity(Gravity.CENTER_HORIZONTAL);
      break;
    default:
      view.setGravity(Gravity.CENTER_VERTICAL);
      break;
    }

  }

  public FieldValidateError validateEditText() {
    return getClearEditText().validateEditText();
  }

  public void setText(String text) {
    getClearEditText().setText(text);
  }

  public Editable getText() {
    return getClearEditText().getText();
  }

  public String getTxt() {
    return getText().toString().trim();
  }

  public ClearEditText getClearEditText() {
    return clearEditText;
  }

  public LabelTxt getLabelTxt() {
    return labelTxt;
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
  }

  public void seEditOnClickListener(OnClickListener l) {
    getClearEditText().setOnClickListener(l);
  }

  public void addEditTextChangedListener(TextWatcher watcher) {
    getClearEditText().addTextChangedListener(watcher);
  }

  public void setEditOnEditorActionListener(TextView.OnEditorActionListener l) {
    getClearEditText().setOnEditorActionListener(l);
  }

  public class FieldGravity {

    public static final int GRAVITY_LEFT = 0;
    public static final int GRAVITY_TOP = 1;
    public static final int GRAVITY_RIGHT = 2;
    public static final int GRAVITY_BOTTOM = 3;
    public static final int CENTER = 4;
    public static final int CENTER_VERTICAL = 5;
    public static final int CENTER_HORIZONTAL = 6;

  }
}
