package com.dindong.dingdong.util;

import com.dindong.dingdong.R;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * Created by wcong on 2018/5/15.
 * <p>
 * 文本折叠工具类，用于文本超长时，折叠文本</>
 */

public class TextFoldUtil {

  private static SparseArray<Integer> mTextStateList;// 保存文本状态集合

  private static final int STATE_UNKNOW = -1;// 未知状态

  private static final int STATE_NOT_OVERFLOW = 1;// 文本行数小于最大可显示行数

  private static final int STATE_COLLAPSED = 2;// 折叠状态

  private static final int STATE_EXPANDED = 3;// 展开状态

  public static void clean() {
    mTextStateList = new SparseArray<>();
  }

  public static void attach(final TextView contentTextView, final TextView foldView, String content,
      int position) {
    attach(3, contentTextView, foldView, content, position);
  }

  /**
   * 绑定需折叠的textView
   * 
   * @param foldLine
   *          折叠行数
   * @param contentTextView
   *          显示的textView
   * @param foldView
   *          用于折叠的textView
   * @param content
   *          显示文本
   */
  public static void attach(final int foldLine, final TextView contentTextView,
      final TextView foldView, String content, final int position) {
    foldView.setVisibility(View.GONE);
    int state = mTextStateList.get(position, STATE_UNKNOW);
    contentTextView.setTag(R.id.text_fold_content, content);// 缓存要显示的文本
    if (state == STATE_UNKNOW) {
      contentTextView.getViewTreeObserver()
          .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
              // 这个回掉会调用多次，获取完行数后记得注销监听
              contentTextView.getViewTreeObserver().removeOnPreDrawListener(this);
              // holder.content.getViewTreeObserver().addOnPreDrawListener(null);
              // 如果内容显示的行数大于最大显示行数
              if (contentTextView.getLineCount() > foldLine) {
                mTextStateList.put(position, STATE_COLLAPSED);// 保存状态
                showFold(true, foldLine, contentTextView, foldView);
              } else {
                foldView.setVisibility(View.GONE);
                mTextStateList.put(position, STATE_NOT_OVERFLOW);
              }
              return true;
            }
          });

      contentTextView.setMaxLines(Integer.MAX_VALUE);// 设置文本的最大行数，为整数的最大数值
      contentTextView.setText((String) contentTextView.getTag(R.id.text_fold_content));
    } else {
      // 如果之前已经初始化过了，则使用保存的状态。
      switch (state) {
      case STATE_NOT_OVERFLOW:
        foldView.setVisibility(View.GONE);
        break;
      case STATE_COLLAPSED:
        showFold(true, foldLine, contentTextView, foldView);
        break;
      case STATE_EXPANDED:
        showFold(false, foldLine, contentTextView, foldView);
        break;
      }
      contentTextView.setText((String) contentTextView.getTag(R.id.text_fold_content));
    }

    // 全文和收起的点击事件
    foldView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int state = mTextStateList.get(position, STATE_UNKNOW);
        if (state == STATE_COLLAPSED) {
          showFold(false, foldLine, contentTextView, foldView);
          mTextStateList.put(position, STATE_EXPANDED);
        } else if (state == STATE_EXPANDED) {
          showFold(true, foldLine, contentTextView, foldView);
          mTextStateList.put(position, STATE_COLLAPSED);
        }
      }
    });
  }

  /**
   * 设置折叠/展开样式
   *
   * @param isFold
   * @param foldLine
   * @param contentTextView
   * @param foldView
   */
  private static void showFold(boolean isFold, final int foldLine, final TextView contentTextView,
      final TextView foldView) {
    foldView.setVisibility(View.VISIBLE);
    if (isFold) {
      // 折叠样式
      contentTextView.setMaxLines(foldLine);
      foldView.setText("全文");

    } else {
      // 打开样式
      contentTextView.setMaxLines(Integer.MAX_VALUE);
      foldView.setText("收起");
    }
  }
}
