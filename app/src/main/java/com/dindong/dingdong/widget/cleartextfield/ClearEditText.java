package com.dindong.dingdong.widget.cleartextfield;

import com.dindong.dingdong.R;
import com.dindong.dingdong.widget.cleartextfield.validator.DefaultTextValidator;
import com.dindong.dingdong.widget.cleartextfield.validator.EmptyValidator;
import com.dindong.dingdong.widget.cleartextfield.validator.FieldValidateError;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by wangcong on 2017/1/11.
 */

@SuppressLint("AppCompatCustomView")
public class ClearEditText extends EditText {

  protected Drawable rightImg;
  protected Drawable defRightRes;

  private boolean isField = false;
  private boolean isShow = true;

  private TypedArray typedArray;

  private DefaultTextValidator defaultTextValidator;

  private RightDrawableClickListener rightDrawableClickListener;

  public ClearEditText(Context context) {
    this(context, null);
    init(context, null, isField);
  }

  public ClearEditText(Context context, AttributeSet attrs) {
    this(context, attrs, android.R.attr.editTextStyle);
    init(context, attrs, isField);
  }

  public ClearEditText(Context context, AttributeSet attrs, boolean isField) {
    super(context, attrs, android.R.attr.editTextStyle);
    init(context, attrs, isField);
  }

  public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, attrs, isField);
  }

  public Drawable getRightImg() {
    return rightImg;
  }

  public void setRightImg(Drawable rightImg) {
    this.rightImg = rightImg;
  }

  public void setRightDrawableClickListener(RightDrawableClickListener rightDrawableClickListener) {
    this.rightDrawableClickListener = rightDrawableClickListener;
  }

  protected void init(Context context, AttributeSet attrs, boolean isField) {
    this.isField = isField;
    initField(context, attrs, isField);
    defRightRes = ContextCompat.getDrawable(context, (Integer) DefValue.RIGHT_IMG.value);
    if (getCompoundDrawables()[2] == null && rightImg == null) {
      rightImg = defRightRes;
    }
    rightImg.setBounds(0, 0, rightImg.getIntrinsicWidth(), rightImg.getIntrinsicHeight());
    defaultTextValidator = new DefaultTextValidator(this);
    String errMsg = null;
    if (isField)
      errMsg = getContext().obtainStyledAttributes(attrs, R.styleable.ClearTextField)
          .getString(R.styleable.ClearTextField_edit_errMsg);
    else
      errMsg = getContext().obtainStyledAttributes(attrs, R.styleable.ClearEditText)
          .getString(R.styleable.ClearEditText_errMsg);
    defaultTextValidator.addValidator(new EmptyValidator(errMsg));
  }

  private void initField(Context context, AttributeSet attrs, boolean isField) {
    if (!isField)
      return;
    typedArray = null;
    if (attrs != null) {
      typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ClearTextField);
      setBackgroundResource((Integer) DefValue.BACKGROUND.value);
      isShow = typedArray.getBoolean(R.styleable.ClearTextField_showClear, true);
      setText(typedArray.getString(R.styleable.ClearTextField_edit_text));
      setTextSize(ClearTextField.px2dip(context, typedArray.getDimensionPixelSize(
          R.styleable.ClearTextField_edit_text_size, DefValue.TXT_SIZE.value)));
      setFilters(new InputFilter[] {
          new InputFilter.LengthFilter(
              typedArray.getInt(R.styleable.ClearTextField_edit_maxLength, Integer.MAX_VALUE)) });
      setHint(typedArray.getString(R.styleable.ClearTextField_edit_hint));
      Drawable leftDrawable = typedArray.getDrawable(R.styleable.ClearTextField_edit_left_drawable);
      if (leftDrawable != null) {
        leftDrawable.setBounds(0, 0, leftDrawable.getIntrinsicWidth(),
            leftDrawable.getIntrinsicHeight());
        setCompoundDrawables(leftDrawable, getCompoundDrawables()[1], null,
            getCompoundDrawables()[3]);
      }
      int padding = (int) typedArray.getDimension(R.styleable.ClearTextField_edit_padding, 0);
      int paddingLeft = (int) typedArray.getDimension(R.styleable.ClearTextField_edit_padding_left,
          0);
      int paddingTop = (int) typedArray.getDimension(R.styleable.ClearTextField_edit_padding_top,
          0);
      int paddingRight = (int) typedArray
          .getDimension(R.styleable.ClearTextField_edit_padding_right, 0);
      int paddingBottom = (int) typedArray
          .getDimension(R.styleable.ClearTextField_edit_padding_bottom, 0);
      setPadding(padding == 0 ? paddingLeft : padding, padding == 0 ? paddingTop : padding,
          padding == 0 ? paddingRight : padding, padding == 0 ? paddingBottom : padding);
      ClearTextField.setGravity(this,
          typedArray.getInt(R.styleable.ClearTextField_edit_gravity, -1));
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_UP) {
      if (getCompoundDrawables()[2] != null) {
        if (event.getX() > (getWidth() - getTotalPaddingRight())
            && (event.getX() < ((getWidth() - getPaddingRight())))) {
          if (rightDrawableClickListener == null)
            this.setText(null);
          else
            rightDrawableClickListener.onRightClick(this);
        }
      }
    }

    return super.onTouchEvent(event);
  }

  @Override
  protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
    super.onFocusChanged(focused, direction, previouslyFocusedRect);
    updateClearIcon(focused ? getText().length() > 0 : focused, getRightImg());
  }

  public FieldValidateError validateEditText() {
    return defaultTextValidator.isValid() ? null : defaultTextValidator.getError();
  }

  public void updateClearIcon(boolean visible, Drawable img) {
    setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1],
        isShow ? (visible ? img : null) : null, getCompoundDrawables()[3]);
    setCompoundDrawablePadding(ClearTextField.dip2px(getContext(), 5));
  }

  @Override
  protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
    super.onTextChanged(text, start, lengthBefore, lengthAfter);
    updateClearIcon(text.length() > 0, getRightImg());
  }

  @Override
  public void setTextColor(int color) {
    if (isField) {
      color = DefValue.TXT_COLOR.value;

    }
    super.setTextColor(color);
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    if (isField) {
      ViewGroup.LayoutParams layoutParams = getLayoutParams();
      layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
      layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
      setLayoutParams(layoutParams);
      setHintTextColor(typedArray.getColor(R.styleable.ClearTextField_edit_hint_color,
          getResources().getColor(R.color.gray)));
    }
    updateClearIcon(hasFocus() ? getText().length() > 0 : hasFocus(), getRightImg());
  }

  public interface RightDrawableClickListener {

    void onRightClick(ClearEditText view);
  }
}
