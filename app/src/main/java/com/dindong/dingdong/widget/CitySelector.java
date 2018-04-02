package com.dindong.dingdong.widget;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.network.bean.entity.Region;
import com.dindong.dingdong.util.DensityUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.StringUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

/**
 * Created by wcong on 2018/3/24.
 * <p>
 * </>
 */

public class CitySelector extends GridLayout {
  private int column = 3;// 列数
  private int margin = 12;// item间距 unit/dp
  private int padding = 8;// 文本内间距 unit/dp

  private CitySelectListener citySelectListener;

  public CitySelector(Context context) {
    super(context);
  }

  public CitySelector(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public CitySelector(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void init(List<Region> cities) {
    init(null, cities);
  }

  public void init(String currentCity, List<Region> cities) {
    addView(currentCity, cities);
  }

  public void setCitySelectListener(CitySelectListener citySelectListener) {
    this.citySelectListener = citySelectListener;
  }

  private void addView(final String currentProvince, final List<Region> cities) {
    setColumnCount(column);
    // 计算item宽度
    postDelayed(new Runnable() {
      @Override
      public void run() {
        int itemWidth = (getMeasuredWidth()
            - (column - 1) * DensityUtil.dip2px(getContext(), margin)) / column;
        for (int i = 0; i < cities.size(); i++) {
          GridLayout.Spec rowSpec = GridLayout.spec(i / column);
          GridLayout.Spec columnSpec = GridLayout.spec(i % column);
          GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, columnSpec);
          layoutParams.width = itemWidth;
          layoutParams.leftMargin = DensityUtil.dip2px(getContext(), i % column == 0 ? 0 : margin);
          layoutParams.bottomMargin = DensityUtil.dip2px(getContext(), margin);

          // 设置文本样式
          TextView textView = new TextView(getContext());
          textView.setBackgroundResource(R.drawable.select_corner_gradient_blue);
          textView.setTextColor(
              getContext().getResources().getColorStateList(R.color.color_tab_payment));
          textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
          textView.setGravity(Gravity.CENTER);
          textView.setText(StringUtil.formatCity(cities.get(i).getText()));
          textView.setTag(cities.get(i));
          if (!IsEmpty.string(currentProvince) && cities.get(i).getText().equals(currentProvince))
            // 设置被选中城市
            textView.setSelected(true);
          else {
            textView.setSelected(false);
          }
          textView.setPadding(0, DensityUtil.dip2px(getContext(), padding), 0,
              DensityUtil.dip2px(getContext(), padding));
          textView.setLayoutParams(layoutParams);
          textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              if (citySelectListener != null)
                citySelectListener.onSelect((Region) v.getTag());
            }
          });
          addView(textView);
        }
      }
    }, 0);

  }

  public interface CitySelectListener {
    void onSelect(Region city);
  }
}
