package com.dindong.dingdong.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.databinding.DialogSelectionBinding;
import com.dindong.dingdong.databinding.ItemDialogSelectionBinding;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.widget.baseadapter.BaseViewAdapter;
import com.dindong.dingdong.widget.baseadapter.BindingViewHolder;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;

import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;

/**
 * Created by wangcong on 2018/7/19.
 * <p>
 * 多级筛选弹框,目前只支持二级
 */

public class SelectionDialog extends PopupWindow {
  DialogSelectionBinding binding;

  private View downView;

  private OnItemSelectListener onItemSelectListener;

  private int maxLen = 2;// 最多支持层级，默认为2

  private List<SingleTypeAdapter<BaseSource>> adapters;

  private HashMap<Integer, List<SelectionSource>> source;

  public SelectionDialog(View downView) {
    this.downView = downView;

    init();
  }

  private void init() {
    binding = DataBindingUtil.inflate(LayoutInflater.from(downView.getContext()),
        R.layout.dialog_selection, null, false);
    binding.setPresenter(new Presenter());
    setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
    setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
    setBackgroundDrawable(new BitmapDrawable());
    setFocusable(true);
    setOutsideTouchable(true);
    setContentView(binding.getRoot());

    initAdapter();
  }

  private void initAdapter() {
    source = new HashMap<>();
    adapters = new ArrayList<>();
    for (int i = 0; i < maxLen; i++) {
      SingleTypeAdapter adapter = new SingleTypeAdapter(downView.getContext(),
          R.layout.item_dialog_selection);
      adapter.setPresenter(new Presenter());
      adapter.setDecorator(new Decorator());
      adapters.add(adapter);
    }
  }

  private void setAdapter(final SingleTypeAdapter<BaseSource> adapter, int position) {
    if (position >= maxLen)
      return;
    final RecyclerView recyclerView = ((RecyclerView) binding.layout.getChildAt(position));
    LinearLayoutManager manager = new LinearLayoutManager(downView.getContext());
    manager.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(adapter);

    recyclerView.postDelayed(new Runnable() {
      @Override
      public void run() {
        if (adapter.getData().size() >= 6) {
          recyclerView.getLayoutParams().height = (int) TypedValue.applyDimension(
              TypedValue.COMPLEX_UNIT_DIP, 6 * 40 - 12f, Resources.getSystem().getDisplayMetrics());
          recyclerView.setLayoutManager(recyclerView.getLayoutManager());
          binding.invalidateAll();
        }
      }
    }, 0);

  }

  public SelectionDialog setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
    this.onItemSelectListener = onItemSelectListener;
    return this;
  }

  /**
   * 设置层级数据
   * 
   * @param position
   *          层级
   * @param data
   *          数据源
   */
  public void setData(int position, List<SelectionSource> data) {
    if (IsEmpty.list(data) || position >= maxLen)
      return;
    for (int i = 0; i < data.size(); i++) {
      data.get(i).setSelect(i == 0 && position < maxLen - 1);
      data.get(i).setPosition(position);
      data.get(i).setId(String.format("tag%s", i));
    }
    source.put(position, data);

    adapters.get(position).clear();
    List<BaseSource> baseSources = new ArrayList<>();
    baseSources.addAll(data);
    adapters.get(position).addAll(baseSources);

    setAdapter(adapters.get(position), position);

    if (onItemSelectListener != null && position < maxLen - 1)
      onItemSelectListener.onItemSelect(SelectionDialog.this, position + 1, data.get(0));
  }

  public void show() {
    if (onItemSelectListener != null)
      onItemSelectListener.onItemSelect(SelectionDialog.this, 0, null);

    binding.layout
        .startAnimation(AnimationUtils.loadAnimation(downView.getContext(), R.anim.slide_top_in));
    showAsDropDown(downView);
  }

  @Override
  public void dismiss() {
    binding.layout
        .startAnimation(AnimationUtils.loadAnimation(downView.getContext(), R.anim.slide_top_out));

    super.dismiss();
  }

  @Override
  public void showAsDropDown(View anchor) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      Rect rect = new Rect();
      anchor.getGlobalVisibleRect(rect);
      int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
      setHeight(h);
    }
    super.showAsDropDown(anchor);
  }

  public class Decorator implements BaseViewAdapter.Decorator {

    @Override
    public void decorator(BindingViewHolder holder, int position, int viewType) {
      if (holder == null)
        return;
      ItemDialogSelectionBinding itemBinding = (ItemDialogSelectionBinding) holder.getBinding();
      itemBinding.root.setBackgroundDrawable(ContextCompat.getDrawable(downView.getContext(),
          itemBinding.getItem().getPosition() < maxLen - 1 ? R.drawable.bg_item_dialog_selection
              : R.drawable.bg_item_dialog_selection2));
      itemBinding.root
          .setSelected(((ItemDialogSelectionBinding) holder.getBinding()).getItem().isSelect());
    }
  }

  public class Presenter implements BaseViewAdapter.Presenter {

    public void onItemClick(BaseSource source) {
      for (SelectionSource selectionSource : SelectionDialog.this.source
          .get(source.getPosition())) {
        if (source.getId().equals(selectionSource.getId()) && onItemSelectListener != null)
          onItemSelectListener.onItemSelect(SelectionDialog.this, source.getPosition() + 1,
              selectionSource);
      }

      if (source.getPosition() >= maxLen - 1) {
        // 选中最后一级元素时，结束筛选
        dismiss();
        return;
      }

      for (BaseSource baseSource : adapters.get(source.getPosition()).getData()) {
        baseSource.setSelect(false);
      }
      // 其他层级设置为选中状态
      source.setSelect(true);

      adapters.get(source.getPosition()).notifyDataSetChanged();
    }

    public void onOut() {
      dismiss();
    }
  }

  public interface OnItemSelectListener<T> {
    /**
     * 选中某元素时，由该接口返回数据
     * 
     * @param position
     *          层级,0-n
     * @param data
     *          选中数据
     */
    void onItemSelect(SelectionDialog selectionDialog, int position, SelectionSource<T> data);
  }

  public static class SelectionSource<T> extends BaseSource {
    private T data;// 源数据

    public T getData() {
      return data;
    }

    public void setData(T data) {
      this.data = data;
    }

  }

  public static class BaseSource {
    private String id;
    private String text;// 用于界面显示

    private int position;// 数据源层级:0-n
    private boolean select;// 是否为选中状态

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }

    public int getPosition() {
      return position;
    }

    public void setPosition(int position) {
      this.position = position;
    }

    public boolean isSelect() {
      return select;
    }

    public void setSelect(boolean select) {
      this.select = select;
    }
  }
}
