package com.dindong.dingdong.presentation.pay;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseFragment;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.FragmentOrderListBinding;
import com.dindong.dingdong.databinding.ItemOrderListBinding;
import com.dindong.dingdong.manager.pay.PayCallback;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.pay.usecase.CancelOrderCase;
import com.dindong.dingdong.network.api.pay.usecase.ListOrderCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.FilterParam;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.pay.Order;
import com.dindong.dingdong.network.bean.pay.OrderState;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.baseadapter.BaseViewAdapter;
import com.dindong.dingdong.widget.baseadapter.BindingViewHolder;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseFooterView;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseHeaderView;
import com.dindong.dingdong.widget.sweetAlert.SweetAlertDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wcong on 2018/3/24.
 * <p>
 * </>
 */

@SuppressLint("ValidFragment")
public class OrderListFragment extends BaseFragment {
  FragmentOrderListBinding binding;

  private SingleTypeAdapter adapter;

  private OrderState orderState;

  public static int REQUEST_CODE = 0x01;

  public OrderListFragment(OrderState orderState) {
    this.orderState = orderState;
  }

  @Override
  protected View initComponent(LayoutInflater inflater, ViewGroup container) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_list, container, false);

    adapter = new SingleTypeAdapter(getContext(), R.layout.item_order_list);
    return binding.getRoot();
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
  }

  @Override
  protected void firstVisible() {
    listOrder(true, true);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
      listOrder(true, true);
    }
  }

  @Override
  protected void createEventHandlers() {
    binding.refreshLayout.setOnRefreshListener(new BaseHeaderView.OnRefreshListener() {
      @Override
      public void onRefresh(BaseHeaderView baseHeaderView) {
        listOrder(false, true);
      }
    });
    binding.refreshLayout.setOnLoadListener(new BaseFooterView.OnLoadListener() {
      @Override
      public void onLoad(BaseFooterView baseFooterView) {
        listOrder(false, false);
      }
    });
  }

  public void listOrder(boolean showProgress, final boolean isRefresh) {
    final QueryParam param = new QueryParam();
    if (isRefresh)
      param.setStart(0);
    else
      param.setStart(adapter.getData().size());
    if (orderState != null)
      param.getFilters().add(new FilterParam("state", orderState));

    SweetAlertDialog sweetAlertDialog = null;
    if (showProgress) {
      sweetAlertDialog = DialogUtil.getProgressDialog(getContext());
      sweetAlertDialog.show();
    }

    final SweetAlertDialog finalSweetAlertDialog = sweetAlertDialog;
    new ListOrderCase(param).execute(new HttpSubscriber<List<Order>>() {
      @Override
      public void onFailure(String errorMsg, Response<List<Order>> response) {
        if (finalSweetAlertDialog != null)
          finalSweetAlertDialog.dismiss();
        DialogUtil.getErrorDialog(getContext(), errorMsg).show();
      }

      @Override
      public void onSuccess(Response<List<Order>> response) {
        loadRecyclerView(response.getData(), isRefresh, response.isMore());
        if (finalSweetAlertDialog != null)
          finalSweetAlertDialog.dismiss();

      }
    });
  }

  private void loadRecyclerView(List<Order> data, boolean isRefresh, boolean isMore) {
    if (isRefresh) {
      adapter.clear();
      binding.refreshLayout.stopRefresh();
    } else
      binding.refreshLayout.stopLoad();
    adapter.addAll(data);
    adapter.setPresenter(new Presenter());
    adapter.setDecorator(new Decorator());
    binding.refreshLayout.setHasFooter(isMore);
    if (isRefresh) {
      LinearLayoutManager manager = new LinearLayoutManager(getContext());
      manager.setOrientation(LinearLayoutManager.VERTICAL);
      binding.lst.setLayoutManager(manager);
      binding.lst.setAdapter(adapter);
    }
  }

  class Decorator implements BaseViewAdapter.Decorator {
    @Override
    public void decorator(BindingViewHolder holder, int position, int viewType) {
      if (holder == null || holder.getBinding() == null)
        return;
      ItemOrderListBinding itemBinding = (ItemOrderListBinding) holder.getBinding();

      itemBinding.txtName.setText(itemBinding.getItem().getItemName());
      itemBinding.txtShopName.setText(itemBinding.getItem().getStore().getName());
    }
  }

  public class Presenter implements BaseViewAdapter.Presenter {

    public void onItemClick(Order order) {
      if (order.getState().equals(OrderState.cannceled.toString())) {
        // 订单已取消无法查看
        return;
      }
      Intent intent = new Intent(getContext(), OrderDetailActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, order);
      startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * 取消订单
     * 
     * @param order
     */
    public void onCancel(final Order order) {
      new CancelOrderCase(order.getId()).execute(new HttpSubscriber<Void>(getContext()) {
        @Override
        public void onFailure(String errorMsg, Response<Void> response) {
          DialogUtil.getErrorDialog(getContext(), errorMsg).show();
        }

        @Override
        public void onSuccess(Response<Void> response) {
          List<Order> orders = adapter.getData();
          int index = -1;
          for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getId().equals(order.getId())) {
              index = i;
              break;
            }
          }
          if (index >= 0) {
            if (orderState == null) {
              // 如果列表状态为全部，则只订单状态
              orders.get(index).setState(OrderState.cannceled.toString());
            } else
              orders.remove(index);
            adapter.notifyDataSetChanged();
          }
          ToastUtil.toastSuccess(getContext(), "取消成功");
        }
      });
    }

    /**
     * 立即付款
     * 
     * @param order
     */
    public void onCommit(Order order) {
      new PayModeSelectDialog(getActivity(), order).setPayCallback(new PayCallback.Callback() {
        @Override
        public void onPaySuccess() {
          ToastUtil.toastSuccess(getContext(), "支付成功");
          Intent intent2 = new Intent(getContext(), OrderListActivity.class);
          intent2.putExtra(AppConfig.IntentKey.DATA, OrderListActivity.TYPE_USE);
          startActivity(intent2);
        }

        @Override
        public void onPayFailure() {
          ToastUtil.toastFailure(getContext(), "支付失败");
        }
      }).show();
    }

  }

}
