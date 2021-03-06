package com.dindong.dingdong.widget.baseadapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v4.util.ArrayMap;
import android.view.ViewGroup;

/**
 * Created by wcong on 2018/3/18.
 */

public class MultiTypeAdapter extends BaseViewAdapter<Object> {

  private OnViewBindingListener onViewBindingListener;

  public interface MultiViewTyper {
    int getViewType(Object item);
  }

  public interface CustomMultiViewTyper {
    int getViewType(Object item, int pos);
  }

  protected ArrayList<Integer> mCollectionViewType;

  private ArrayMap<Integer, Integer> mItemTypeToLayoutMap = new ArrayMap<>();

  public MultiTypeAdapter(Context context) {
    this(context, null);
  }

  public MultiTypeAdapter(Context context, Map<Integer, Integer> viewTypeToLayoutMap) {
    super(context);
    mCollection = new ArrayList<>();
    mCollectionViewType = new ArrayList<>();
    if (viewTypeToLayoutMap != null && !viewTypeToLayoutMap.isEmpty()) {
      mItemTypeToLayoutMap.putAll(viewTypeToLayoutMap);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    ViewDataBinding dataBinding = DataBindingUtil.inflate(mLayoutInflater, getLayoutRes(viewType),
        parent, false);
    if (onViewBindingListener != null) {
      onViewBindingListener.onBinding(dataBinding);
    }
    return new BindingViewHolder(dataBinding);
  }

  public void addViewTypeToLayoutMap(Integer viewType, Integer layoutRes) {
    mItemTypeToLayoutMap.put(viewType, layoutRes);
  }

  @Override
  public int getItemViewType(int position) {
    return mCollectionViewType.get(position);
  }

  public void set(List viewModels, int viewType) {
    mCollection.clear();
    mCollectionViewType.clear();

    addAll(viewModels, viewType);
  }

  public void set(List viewModels, MultiViewTyper viewTyper) {
    mCollection.clear();
    mCollectionViewType.clear();

    addAll(viewModels, viewTyper);
  }

  public void add(Object viewModel, int viewType) {
    mCollection.add(viewModel);
    mCollectionViewType.add(viewType);
    notifyItemInserted(0);
  }

  public void add(int position, Object viewModel, int viewType) {
    mCollection.add(position, viewModel);
    mCollectionViewType.add(position, viewType);
    notifyItemInserted(position);
  }

  public void addAll(List viewModels, int viewType) {
    mCollection.addAll(viewModels);
    for (int i = 0; i < viewModels.size(); ++i) {
      mCollectionViewType.add(viewType);
    }
    notifyDataSetChanged();
  }

  public void addAll(List viewModels, MultiViewTyper multiViewTyper) {
    mCollection.addAll(viewModels);
    for (int i = 0; i < viewModels.size(); ++i) {
      mCollectionViewType.add(multiViewTyper.getViewType(viewModels.get(i)));
    }
    notifyDataSetChanged();
  }

  public void addAll(List viewModels, CustomMultiViewTyper multiViewTyper) {
    mCollection.addAll(viewModels);
    for (int i = 0; i < viewModels.size(); ++i) {
      mCollectionViewType.add(multiViewTyper.getViewType(viewModels.get(i), i));
    }
    notifyDataSetChanged();
  }

  public void remove(int position) {
    mCollectionViewType.remove(position);
    super.remove(position);
  }

  public void clear() {
    mCollectionViewType.clear();
    super.clear();
  }

  public OnViewBindingListener getOnViewBindingListener() {
    return onViewBindingListener;
  }

  public void setOnViewBindingListener(OnViewBindingListener onViewBindingListener) {
    this.onViewBindingListener = onViewBindingListener;
  }

  @LayoutRes
  protected int getLayoutRes(int viewType) {
    return mItemTypeToLayoutMap.get(viewType);
  }
}
