package com.dindong.dingdong.widget.cleartextfield;

import com.dindong.dingdong.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by wangcong on 2017/1/11.
 */

@SuppressLint("AppCompatCustomView")
public class LabelTxt extends TextView {

  private int width;

  private TypedArray typedArray;

  public LabelTxt(Context context) {
    super(context);
    init(context, null);
  }

  public LabelTxt(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public LabelTxt(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    if (attrs != null) {
      typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ClearTextField);
      setText(typedArray.getString(R.styleable.ClearTextField_label_text));
      setTextSize(ClearTextField.px2dip(context, typedArray.getDimension(
          R.styleable.ClearTextField_label_text_size, (Integer) DefValue.TXT_SIZE.value)));
      setTextColor(typedArray.getInt(R.styleable.ClearTextField_label_text_color,
          (Integer) DefValue.TXT_COLOR.value));
      int padding = (int) typedArray.getDimension(R.styleable.ClearTextField_label_padding, 0);
      int paddingLeft = (int) typedArray.getDimension(R.styleable.ClearTextField_label_padding_left,
          0);
      int paddingTop = (int) typedArray.getDimension(R.styleable.ClearTextField_label_padding_top,
          0);
      int paddingRight = (int) typedArray
          .getDimension(R.styleable.ClearTextField_label_padding_right, 0);
      int paddingBottom = (int) typedArray
          .getDimension(R.styleable.ClearTextField_label_padding_bottom, 0);
      setPadding(padding == 0 ? paddingLeft : padding, padding == 0 ? paddingTop : padding,
          padding == 0 ? paddingRight : padding, padding == 0 ? paddingBottom : padding);
      ClearTextField.setGravity(this,
          typedArray.getInt(R.styleable.ClearTextField_label_gravity, -1));
    }
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    if (typedArray != null)
      width = (int) typedArray.getDimension(R.styleable.ClearTextField_label_layout_width,
          Float.valueOf(ViewGroup.LayoutParams.WRAP_CONTENT));
    ViewGroup.LayoutParams layoutParams = getLayoutParams();
    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
    layoutParams.width = width;
    setLayoutParams(layoutParams);
  }

}
