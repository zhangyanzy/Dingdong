package com.dindong.dingdong.widget;

import java.math.BigDecimal;

import com.dindong.dingdong.R;
import com.dindong.dingdong.util.CopyUtil;
import com.dindong.dingdong.util.StringUtil;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by wcong on 2018/3/23.
 * <p>
 * </>
 */

public class CountView extends LinearLayout implements View.OnClickListener {
  public static final int BUY_SCAN = 0;
  public static final int BUY_COLLECT = 1;
  public static final int BUY_ALL = 2;
  public static final int BUY_SEARCH = 3;
  public static final int BUY_BASKET = 4;

  public enum State {
    FOLD(0), UNFOLD(1);

    private int id;

    State(int id) {
      this.id = id;
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public static State valueOf(int id) {
      for (State state : values()) {
        if (state.getId() == id) {
          return state;
        }
      }
      return null;
    }
  }

  protected State mState = State.FOLD;

  private BigDecimal minValue;
  private BigDecimal maxValue = BigDecimal.ZERO;
  private int module;
  private boolean weigh;
  private boolean isCart;

  public CountView(Context context) {
    this(context, null);
  }

  public CountView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    initAttr(attrs, defStyleAttr);
    init();

    setState(mState, false);
  }

  private void initAttr(@Nullable AttributeSet attributeSet, int defStyleAttr) {
    TypedArray ta = getContext().obtainStyledAttributes(attributeSet, R.styleable.CountView,
        defStyleAttr, 0);
    int foldState = ta.getInt(R.styleable.CountView_fold_state, 1);
    mState = State.valueOf(foldState);
    minValue = BigDecimal.valueOf(ta.getFloat(R.styleable.CountView_min_value, 0));
    isCart = ta.getBoolean(R.styleable.CountView_isCart, false);
    ta.recycle();
  }

  public void setWeigh(boolean weigh) {
    if (this.weigh != weigh && null != mCount) {
      this.weigh = weigh;
      changeCount(mCount);
    }
  }

  private View bg;
  private ImageView ivMinus;
  private TextView tvCount;
  private ImageView ivAdd;
  private LinearLayout llMinus;
  private LinearLayout llAdd;

  private void init() {
    View view = LayoutInflater.from(getContext()).inflate(R.layout.count_view, this, true);
    bg = view.findViewById(R.id.bg);
    ivMinus = (ImageView) view.findViewById(R.id.iv_minus);
    tvCount = (TextView) view.findViewById(R.id.tv_count);
    ivAdd = (ImageView) view.findViewById(R.id.iv_add);
    llMinus = (LinearLayout) view.findViewById(R.id.ll_minus);
    llAdd = (LinearLayout) view.findViewById(R.id.ll_add);

    ivMinus.setOnClickListener(this);
    llMinus.setOnClickListener(this);
    // ivAdd.setOnClickListener(this);
    llAdd.setOnClickListener(this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    if (mCount != null) {
      setCount(mCount);
    } else {
      setCount(minValue);
    }
  }

  // private Integer mCount;
  private BigDecimal mCount;

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.iv_minus:
    case R.id.ll_minus: {
      if (mCount.subtract(BigDecimal.ONE).compareTo(minValue) >= 0) {
        setCount(mCount.subtract(BigDecimal.ONE));
      } else if (mCount.compareTo(minValue) > 0
          && mCount.compareTo(minValue.add(BigDecimal.ONE)) < 0) {
        setCount(minValue);
      } else {
        return;
      }
      if (mCount.equals(BigDecimal.ZERO) && !isCart) {
        setState(State.FOLD, true);
      }
      break;
    }
    case R.id.iv_add:
    case R.id.ll_add: {
      if (mState == State.FOLD) {
        setState(State.UNFOLD, true);
      }
      // 与需求耦合：当数量为0时
      BigDecimal temp = mCount.add(BigDecimal.ONE);
      boolean addEnable = !weigh || temp.compareTo(new BigDecimal(99.999)) <= 0;
      if (addEnable)
        setCount(temp);
      break;
    }
    }
  }

  public void setCount(BigDecimal count, boolean listen) {
    changeCount(count);
    if (bindingCountChangeListener != null) {
      bindingCountChangeListener.onCountChange(mCount);
    }
    if (listen) {
      notifyCountChange();
    }
  }

  public void notifyCountChange() {
    if (mOnCountChangeListener != null) {
      mOnCountChangeListener.onCountChange(mCount);
    }
  }

  /**
   * 设置 mCount, 改变显示文字，改变左边按钮是否能够点按（需求改变后不重要了），触发 listener
   *
   * @param count
   */
  public void setCount(BigDecimal count) {
    changeCount(count);
    if (bindingCountChangeListener != null) {
      bindingCountChangeListener.onCountChange(mCount);
    }
    if (mOnCountChangeListener != null) {
      mOnCountChangeListener.onCountChange(mCount);
    }
  }

  public void setMaxValue(BigDecimal maxValue) {
    this.maxValue = maxValue;
  }

  private void changeCount(BigDecimal count) {
    if (maxValue.compareTo(BigDecimal.ZERO) != 0 && count.compareTo(maxValue) == 1)
      return;
    mCount = CopyUtil.copy(count);
    String temp = getContext().getString(R.string.global_count_format);
    tvCount.setText(StringUtil.format(temp, mCount));
    boolean minusEnable = count.compareTo(minValue) > 0;
    boolean addEnable = !weigh || count.add(BigDecimal.ONE).compareTo(new BigDecimal(99.999)) <= 0;
    llMinus.setEnabled(minusEnable);
    ivMinus.setEnabled(minusEnable);
    llAdd.setEnabled(addEnable);
    ivAdd.setEnabled(addEnable);
    ivMinus.setImageResource(minusEnable ? R.mipmap.btn_minus : R.mipmap.btn_minus_disable);
    ivAdd.setImageResource(addEnable ? R.mipmap.btn_add : R.mipmap.btn_add_disable);
  }

  public BigDecimal getCount() {
    return mCount;
  }

  private int dp2px(float dp) {
    return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
  }

  public void setState(final State foldState, boolean isAnimated) {
    if (foldState == State.UNFOLD) {
      // 展开
      bg.setVisibility(VISIBLE);
      ivMinus.setVisibility(VISIBLE);
      tvCount.setVisibility(VISIBLE);

      setStateAndTriggerListener(foldState);

      if (isAnimated) {
        ValueAnimator animator = ValueAnimator.ofInt(dp2px(104), 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator animation) {
            int x = (int) animation.getAnimatedValue();
            bg.setTranslationX(x);
            ivMinus.setTranslationX(x);
            tvCount.setTranslationX(x);
          }
        });
        animator.setDuration(250);
        animator.start();
      }
    } else if (foldState == State.FOLD) {
      if (isAnimated) {
        ValueAnimator animator = ValueAnimator.ofInt(0, dp2px(104));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator animation) {
            int x = (int) animation.getAnimatedValue();
            bg.setTranslationX(x);
            ivMinus.setTranslationX(x);
            tvCount.setTranslationX(x);
          }
        });
        animator.addListener(new AnimatorListenerAdapter() {
          @Override
          public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);

            // 折叠
            bg.setVisibility(INVISIBLE);
            ivMinus.setVisibility(INVISIBLE);
            tvCount.setVisibility(INVISIBLE);

            bg.setTranslationX(0);
            ivMinus.setTranslationX(0);
            tvCount.setTranslationX(0);

            setStateAndTriggerListener(foldState);
          }
        });
        animator.setDuration(250);
        animator.start();
      } else {
        // 折叠
        bg.setVisibility(INVISIBLE);
        ivMinus.setVisibility(INVISIBLE);
        tvCount.setVisibility(INVISIBLE);

        setStateAndTriggerListener(foldState);
      }
    }
  }

  private void setStateAndTriggerListener(State foldState) {
    mState = foldState;
    if (mOnFoldStateChangeListener != null) {
      mOnFoldStateChangeListener.onFoldStateChange(mState);
    }
  }

  public ImageView getIvAdd() {
    return ivAdd;
  }

  public interface OnCountChangeListener {
    void onCountChange(BigDecimal count);
  }

  public interface OnFoldStateChangeListener {
    void onFoldStateChange(State foldState);
  }

  private OnCountChangeListener bindingCountChangeListener;
  private OnCountChangeListener mOnCountChangeListener;
  public OnFoldStateChangeListener mOnFoldStateChangeListener;

  public void setBindingCountChangeListener(OnCountChangeListener listener) {
    bindingCountChangeListener = listener;
  }

  public void setOnCountChangeListener(int module, OnCountChangeListener listener) {
    this.module = module;
    mOnCountChangeListener = listener;
  }

  public void setOnFoldStateChangeListener(OnFoldStateChangeListener listener) {
    mOnFoldStateChangeListener = listener;
  }
}
