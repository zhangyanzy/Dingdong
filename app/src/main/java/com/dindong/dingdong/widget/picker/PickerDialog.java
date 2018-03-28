package com.dindong.dingdong.widget.picker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dindong.dingdong.R;
import com.dindong.dingdong.databinding.DialogAddressBinding;
import com.dindong.dingdong.databinding.ItemAddressBinding;
import com.dindong.dingdong.network.bean.entity.Region;
import com.dindong.dingdong.util.DensityUtil;
import com.dindong.dingdong.widget.baseadapter.BaseViewAdapter;
import com.dindong.dingdong.widget.baseadapter.BindingViewHolder;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by wangcong on 2018/3/28.
 * <p>
 */

public abstract class PickerDialog extends Dialog {
  private DialogAddressBinding binding;
  private Context mContext;
  private SingleTypeAdapter<Region> mAdapter;

  private ISelectListener listener;
  protected boolean isDefault;
  protected int currentLevel;
  private int deltaX;

  protected Map<Integer, List<Region>> mapDatas;
  protected Map<Integer, Region> selectMapDatas;

  public PickerDialog(Context context) {
    super(context, R.style.FullScreenDialog);
    this.mContext = context;
    initViews();
    initParams();
  }

  protected void init() {
    initTabView();
    initDefaultParams();
    initTabs();
    initData(0, null, true);
  }

  public abstract String getTitle();

  public abstract int getTabSize();

  public abstract void query(int level, String parentCode, boolean auto);

  private void initDefaultParams() {
    if (!selectMapDatas.isEmpty()) {
      isDefault = true;
      int count = getTabSize();
      for (int i = 0; i < count; i++) {
        ((TextView) getTabLayout().getChildAt(i)).setVisibility(View.VISIBLE);
        ((TextView) getTabLayout().getChildAt(i)).setText(selectMapDatas.get(i).getText());
      }
      currentLevel = getTabSize() - 1;
    }

  }

  private void initTabView() {
    binding.llTabLayout.removeAllViews();
    for (int i = 0; i < getTabSize(); i++) {
      final int index = i;
      TextView textView = createLabTextView();
      textView.setTag(i);
      if (i == 0) {
        textView.setVisibility(View.VISIBLE);
      } else {
        textView.setVisibility(View.GONE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        params.leftMargin = DensityUtil.dip2px(mContext, 15);
        textView.setLayoutParams(params);
      }
      textView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (index == currentLevel)
            return;
          isDefault = false;
          currentLevel = index;
          initTabs();

          int count = binding.llTabLayout.getChildCount();
          for (int i = 0; i < count; i++) {
            bindingRecyclerViewData(currentLevel, mapDatas.get(currentLevel));
          }
        }
      });
      binding.llTabLayout.addView(textView);
    }
  }

  private void initData(int level, String code, boolean auto) {
    getProgressBar().setVisibility(View.VISIBLE);
    query(level, code, auto);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.getScreenHeight(mContext) * 3 / 5);
    setContentView(binding.getRoot(), params);
  }

  private void initViews() {

    binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_address, null,
        false);
    binding.setPresenter(new Presenter());

    binding.tvTitle.setText(getTitle());

    setWindowStyle();
    setCanceledOnTouchOutside(true);
    setCancelable(true);

  }

  public LinearLayout getTabLayout() {
    return binding.llTabLayout;
  }

  public ProgressBar getProgressBar() {
    return binding.progressBar;
  }

  private TextView createLabTextView() {
    View view = LayoutInflater.from(mContext).inflate(R.layout.text_view, null);
    return (TextView) view;
  }

  private void setWindowStyle() {
    Window window = this.getWindow();
    window.setGravity(Gravity.BOTTOM);
    window.setWindowAnimations(R.style.theme_animation_bottom_rising);
    window.getDecorView().setPadding(0, 0, 0, 0);
    WindowManager.LayoutParams lp = window.getAttributes();
    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
    window.setAttributes(lp);
  }

  private void initParams() {
    deltaX = DensityUtil.dip2px(mContext, 8);
    mapDatas = new HashMap<>();
    selectMapDatas = new HashMap<>();

    mAdapter = new SingleTypeAdapter(mContext, R.layout.item_address);
    mAdapter.setPresenter(new Presenter());

    LinearLayoutManager manager = new LinearLayoutManager(mContext);
    manager.setOrientation(LinearLayoutManager.VERTICAL);
    binding.recyclerView.setLayoutManager(manager);
    binding.recyclerView.setAdapter(mAdapter);
  }

  private void initTabs() {

    computeIndicateLineWidth(currentLevel);
    ViewPropertyAnimator.animate(binding.indicateLine)
        .translationX(computeTranslationX(currentLevel)).setDuration(100);

    int count = binding.llTabLayout.getChildCount();
    for (int i = 0; i < count; i++) {
      binding.llTabLayout.getChildAt(i).setSelected(currentLevel == i);
    }
  }

  private int getItemIndex(List<Region> list, Region item) {
    if (item == null)
      return -1;
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).getText().equals(item.getText()))
        return i;
    }

    return -1;
  }

  protected void notifyDataSetChanged(List<Region> list, int index) {

    mAdapter.clear();
    mAdapter.set(list);
    mAdapter.setDecorator(new AdapterDecorator());
    mAdapter.notifyDataSetChanged();

    ((LinearLayoutManager) binding.recyclerView.getLayoutManager())
        .scrollToPositionWithOffset(index, 0);
  }

  private void bindingRecyclerViewData(int level, List<Region> list) {
    if (list == null || list.isEmpty())
      return;
    int index = 0;

    for (int i = 0; i < getTabSize(); i++) {
      if (i == level) {
        mapDatas.put(level, list);
        if (selectMapDatas.get(level) != null) {
          index = getItemIndex(list, selectMapDatas.get(level));
          if (isDefault) {
            if (level == getTabSize() - 1)
              break;
            initData(level + 1, selectMapDatas.get(level).getId(), true);
            return;
          }
        }
      }
    }

    notifyDataSetChanged(list, index);

  }

  protected void onRequestFailure() {
    binding.progressBar.setVisibility(View.GONE);
    if (!isDefault)
      initTabs();
  }

  protected void onRequestSuccess(int level, List<Region> list, boolean auto) {

    if (!isDefault && !auto) {

      int count = binding.llTabLayout.getChildCount();
      for (int i = 0; i < count; i++) {
        if (i <= level) {
          binding.llTabLayout.getChildAt(i).setVisibility(View.VISIBLE);
        } else {
          binding.llTabLayout.getChildAt(i).setVisibility(View.GONE);
        }
      }
      ((TextView) binding.llTabLayout.getChildAt(level)).setText("请选择");

    }
    currentLevel = level;
    if (!isDefault)
      initTabs();
    binding.progressBar.setVisibility(View.GONE);

    bindingRecyclerViewData(level, list);
  }

  private class AdapterDecorator implements BaseViewAdapter.Decorator {

    @Override
    public void decorator(BindingViewHolder holder, int position, int viewType) {

      ItemAddressBinding binding = (ItemAddressBinding) holder.getBinding();

      binding.setSelectItem(selectMapDatas.get(currentLevel));
    }
  }

  private int computeTextViewWidth(TextView textView) {
    return (int) textView.getPaint().measureText(textView.getText().toString());
  }

  private int computeTranslationX(int index) {
    if (index == 0)
      return 0;
    int distance = 0;
    for (int i = 0; i < index; i++) {
      distance += computeTextViewWidth((TextView) binding.llTabLayout.getChildAt(i))
          + DensityUtil.dip2px(mContext, 15);
    }

    return distance;
  }

  private void computeIndicateLineWidth(int index) {
    int width;
    width = computeTextViewWidth((TextView) binding.llTabLayout.getChildAt(index));
    binding.indicateLine.getLayoutParams().width = width + deltaX;
  }

  public class Presenter implements BaseViewAdapter.Presenter {

    public void onClose() {
      dismiss();
    }

    public void onItemClick(Region item) {
      if (binding.progressBar.getVisibility() == View.VISIBLE)
        return;
      isDefault = false;
      mAdapter.notifyDataSetChanged();
      selectMapDatas.put(currentLevel, item);

      for (int i = currentLevel + 1; i < getTabSize(); i++) {
        selectMapDatas.remove(i);
      }

      ((TextView) binding.llTabLayout.getChildAt(currentLevel)).setText(item.getText());

      for (int i = 0; i < getTabSize(); i++) {
        if (i <= currentLevel) {
          binding.llTabLayout.getChildAt(i).setVisibility(View.VISIBLE);
        } else {
          binding.llTabLayout.getChildAt(i).setVisibility(View.GONE);
        }

      }
      if (currentLevel < getTabSize() - 1)
        initData(currentLevel + 1, item.getId(), false);

      if (currentLevel == getTabSize() - 1) {
        if (listener != null) {
          listener.onSelect(selectMapDatas);
        }
        dismiss();
        return;
      }

    }
  }

  public void setListener(ISelectListener listener) {
    this.listener = listener;
  }

  public interface ISelectListener {
    void onSelect(Map<Integer, Region> map);
  }

}