package com.dindong.dingdong.util;

import java.util.HashMap;
import java.util.Map;

import com.dindong.dingdong.R;

import android.text.Layout;
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

  private SparseArray<Integer> mTextStateList;// 保存文本状态集合
  private Map<Integer, String> mBottomLineStr;// 保存末位文本

  private final int STATE_UNKNOW = -1;// 未知状态

  private final int STATE_NOT_OVERFLOW = 1;// 文本行数小于最大可显示行数

  private final int STATE_COLLAPSED = 2;// 折叠状态

  private final int STATE_EXPANDED = 3;// 展开状态

  public TextFoldUtil() {
    mTextStateList = new SparseArray<>();
    mBottomLineStr = new HashMap<>();
  }

  public void clean() {
    mTextStateList = new SparseArray<>();
    mBottomLineStr = new HashMap<>();
  }

  /**
   * 默认6行折叠
   * 
   * @param contentTextView
   * @param bottomTextView
   * @param foldView
   * @param content
   * @param position
   */
  public void attach(final TextView contentTextView, final TextView bottomTextView,
      final TextView foldView, String content, int position) {
    attach(6, contentTextView, bottomTextView, foldView, content, position);
  }

  /**
   * 绑定需折叠的textView
   * 
   * @param maxLine
   * @param contentTextView
   *          显示的textView
   * @param bottomTextView
   * @param foldView
   *          用于折叠的textView
   * @param content
   *          显示文本
   * @param position
   */
  public void attach(final int maxLine, final TextView contentTextView,
      final TextView bottomTextView, final TextView foldView, String content, final int position) {
    final int foldLine = maxLine - 1;
    foldView.setVisibility(View.GONE);
    bottomTextView.setVisibility(View.GONE);
    if (mBottomLineStr.get(position) != null)
      bottomTextView.setText(mBottomLineStr.get(position));
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
              if (contentTextView.getLineCount() > foldLine + 1) {
                mTextStateList.put(position, STATE_COLLAPSED);// 保存状态

                // 截取最后一行
                String lineStr = "";
                Layout layout = contentTextView.getLayout();
                String text = layout.getText().toString();
                for (int i = 0; i < contentTextView.getLayout().getLineCount() - 1; i++) {
                  if (i != maxLine - 1)
                    continue;
                  int start = layout.getLineStart(i);
                  int end = layout.getLineEnd(i);
                  lineStr = text.substring(start, end);
                  break;
                }
                mBottomLineStr.put(position, lineStr);// 设置末位文本
                bottomTextView.setText(lineStr);

                showFold(true, foldLine, contentTextView, bottomTextView, foldView);
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
        showFold(true, foldLine, contentTextView, bottomTextView, foldView);
        break;
      case STATE_EXPANDED:
        showFold(false, foldLine, contentTextView, bottomTextView, foldView);
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
          showFold(false, foldLine, contentTextView, bottomTextView, foldView);
          mTextStateList.put(position, STATE_EXPANDED);
        } else if (state == STATE_EXPANDED) {
          showFold(true, foldLine, contentTextView, bottomTextView, foldView);
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
  private void showFold(boolean isFold, final int foldLine, final TextView contentTextView,
      final TextView bottomTextView, final TextView foldView) {
    foldView.setVisibility(View.VISIBLE);
    if (isFold) {
      // 折叠样式
      contentTextView.setMaxLines(foldLine);
      foldView.setText("全文");
      bottomTextView.setVisibility(View.VISIBLE);
    } else {
      // 打开样式
      contentTextView.setMaxLines(Integer.MAX_VALUE);
      foldView.setText("收起");
      bottomTextView.setVisibility(View.GONE);
    }
  }
}
