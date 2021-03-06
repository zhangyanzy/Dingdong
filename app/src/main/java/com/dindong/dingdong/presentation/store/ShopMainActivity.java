package com.dindong.dingdong.presentation.store;

import java.util.ArrayList;
import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.adapter.SubjectPresenter;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityShopMainBinding;
import com.dindong.dingdong.databinding.TabOrderListBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.like.usecase.CancelFollowLikeCase;
import com.dindong.dingdong.network.api.like.usecase.FollowLikeCase;
import com.dindong.dingdong.network.api.moment.usecase.MomentCase;
import com.dindong.dingdong.network.api.shop.usecase.GetShopCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.comment.Comment;
import com.dindong.dingdong.network.bean.entity.GlobalImage;
import com.dindong.dingdong.network.bean.like.LikeEntityType;
import com.dindong.dingdong.network.bean.store.Shop;
import com.dindong.dingdong.network.bean.store.Subject;
import com.dindong.dingdong.presentation.discovery.MomentConverter;
import com.dindong.dingdong.presentation.subject.SubjectDetailActivity;
import com.dindong.dingdong.util.DensityUtil;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.KeyboardUtil;
import com.dindong.dingdong.util.PhoneUtil;
import com.dindong.dingdong.util.PhotoUtil;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.NavigationTopBar;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 门店主页
 */
public class ShopMainActivity extends BaseActivity {

  ActivityShopMainBinding binding;

  private Shop shop;

  private List<Fragment> fragments;

  private List<TabOrderListBinding> tabBindings;
  private List<TabOrderListBinding> tabSuspendBindings;
  private List<View> bottomViews = new ArrayList<>();

  private int topHeight = -1;// 顶部布局高度

  private String relationId = null;// 用于记录动态评论的关联ID

  public static final int REQUEST_CODE = 0x01;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_main);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
    bottomViews.add(binding.layoutHomeComment);
    bottomViews.add(binding.layoutEdit);
    bottomViews.add(binding.layoutJoin);
    addInterceptView(binding.layoutBottomRoot);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    if (getIntent().getSerializableExtra(AppConfig.IntentKey.DATA) != null) {
      shop = (Shop) getIntent().getSerializableExtra(AppConfig.IntentKey.DATA);
      initShopImage(shop);
      getShop(shop.getId());
    }
  }

  /**
   * 获取门店信息
   * 
   * @param shopId
   */
  private void getShop(final String shopId) {
    new GetShopCase(shopId).execute(new HttpSubscriber<Shop>() {
      @Override
      public void onFailure(String errorMsg, Response<Shop> response) {
        binding.setItem(shop);
        initViewPager(binding);

        binding.topLayout.post(new Runnable() {
          @Override
          public void run() {
            topHeight = binding.topLayout.getMeasuredHeight();
          }
        });
      }

      @Override
      public void onSuccess(Response<Shop> response) {
        shop.setFavoriteCount(response.getData().getFavoriteCount());
        shop.setFavorite(response.getData().isFavorite());
        shop.setLongitude(response.getData().getLongitude());
        shop.setLatitude(response.getData().getLatitude());
        binding.setItem(shop);
        initViewPager(binding);

        ((ShopMainHomeFragment) fragments.get(0))
            .initHotSubject(response.getData().getSubjects().size() >= 2
                ? response.getData().getSubjects().subList(0, 2)
                : response.getData().getSubjects());
        binding.topLayout.post(new Runnable() {
          @Override
          public void run() {
            topHeight = binding.topLayout.getMeasuredHeight();
          }
        });
      }
    });
  }

  /**
   * 加载门店展示图
   * 
   * @param shop
   */
  private void initShopImage(final Shop shop) {
    if (shop == null || shop.getImages() == null)
      return;
    final int column = 3;
    final int margin = DensityUtil.dip2px(this, 4);// px
    binding.getRoot().post(new Runnable() {
      @Override
      public void run() {
        int itemWidth = (binding.getRoot().getMeasuredWidth() - margin * (column - 1)) / column;
        int position = 0;
        for (GlobalImage image : shop.getImages()) {
          ImageView imageView = new ImageView(ShopMainActivity.this);
          LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth, itemWidth);
          params.rightMargin = margin;
          imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
          imageView.setLayoutParams(params);
          imageView.setTag(R.id.position, position++);
          PhotoUtil.load(ShopMainActivity.this, image.getUrl(), imageView);
          imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              PhotoUtil.preview(ShopMainActivity.this, (Integer) v.getTag(R.id.position),
                  shop.getImages());
            }
          });
          binding.layoutShopImg.addView(imageView);
        }
        updateViewPagerHeight();
      }
    });

  }

  /**
   * 实时更新viewpager item高度
   */
  public void updateViewPagerHeight() {
    if (binding.vp == null)
      return;
    binding.vp.postDelayed(new Runnable() {
      @Override
      public void run() {
        binding.vp.updateHeight(binding.vp.getCurrentItem());
      }
    }, 200);

  }

  @Override
  protected void createEventHandlers() {
    KeyboardUtil.setKeyboardVisibleListener(binding.edtComment, this,
        new KeyboardUtil.OnKeyboardVisibleListener() {
          @Override
          public void onKeyboardVisible(boolean visible) {
            if (visible) {
              binding.layoutEdit.setVisibility(View.VISIBLE);
            } else {
              binding.layoutEdit.setVisibility(View.GONE);
            }
          }
        });

    binding.nb
        .setNavigationTopBarClickListener(new NavigationTopBar.NavigationTopBarClickListener() {
          @Override
          public void leftImageClick() {
            finish();
          }
        });
    binding.setPresenter(new Presenter());
    binding.sv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
      @Override
      public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX,
          int oldScrollY) {

        if (topHeight > 0) {
          // 滑动距离超过tab时，显示悬浮tab否则隐藏
          binding.stLayoutSuspend.setVisibility(scrollY >= topHeight ? View.VISIBLE : View.GONE);
        }

        if (binding == null || binding.vp.getCurrentItem() != 1 || fragments == null)
          return;
        final RecyclerView recyclerView = ((ShopMainMomentFragment) fragments
            .get(binding.vp.getCurrentItem())).getRecyclerView();
        // 滑动顶部或底部时，允许用户下拉刷新上拉加载
        binding.sv.setOnTouchListener(null);
        if (scrollY - oldScrollY > 0) {
          if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
            recyclerView.setNestedScrollingEnabled(true);
          } else {
            recyclerView.setNestedScrollingEnabled(false);
          }
        } else {
          if (scrollY == 0) {
            final float[] mLastX = {
                0 };
            final float[] mLastY = {
                0 };
            binding.sv.setOnTouchListener(new View.OnTouchListener() {
              @Override
              public boolean onTouch(View v, MotionEvent event) {
                int dx = 0;
                int dy = 0;
                float x = event.getX();
                float y = event.getY();
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                  mLastX[0] = x;
                  mLastY[0] = y;
                  break;
                case MotionEvent.ACTION_MOVE:
                  dx = (int) (mLastX[0] - x);
                  dy = (int) (mLastY[0] - y);
                  // recyclerView.setNestedScrollingEnabled(true);
                  break;
                }
                return false;
              }
            });
          } else {
            recyclerView.setNestedScrollingEnabled(false);
          }
        }
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
      // 评论门店成功后刷新门店评论列表
      ((ShopMainHomeFragment) fragments.get(0)).listComment(true, shop.getId());
    }
  }

  private void initViewPager(ActivityShopMainBinding activityShopMainBinding) {
    tabBindings = new ArrayList<>();
    tabSuspendBindings = new ArrayList<>();
    fragments = new ArrayList<>();

    // 首页
    ShopMainHomeFragment shopMainHomeFragment = new ShopMainHomeFragment();
    Bundle bundle = new Bundle();
    bundle.putSerializable(AppConfig.IntentKey.DATA, shop);
    shopMainHomeFragment.setArguments(bundle);

    // 动态
    ShopMainMomentFragment shopMainMomentFragment = new ShopMainMomentFragment();
    shopMainMomentFragment.setArguments(bundle);

    // 详情
    ShopMainRemarkFragment mainRemarkFragment = new ShopMainRemarkFragment();
    mainRemarkFragment.setArguments(bundle);

    fragments.add(shopMainHomeFragment);
    fragments.add(shopMainMomentFragment);
    fragments.add(mainRemarkFragment);

    // 初始化tab
    activityShopMainBinding.vp.setAdapter(new TabAdapter(getSupportFragmentManager()));
    activityShopMainBinding.vp.setOffscreenPageLimit(3);
    final String[] tabs = getResources().getStringArray(R.array.shop_main_tabs);
    activityShopMainBinding.stLayout.setCustomTabView(new SmartTabLayout.TabProvider() {
      @Override
      public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
        TabOrderListBinding tabOrderListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(ShopMainActivity.this), R.layout.tab_order_list, container, false);
        tabOrderListBinding.txt.setText(tabs[position]);
        tabOrderListBinding.txt
            .setTextColor(getResources().getColorStateList(R.color.color_tab_shop_main));
        if (position == 0) {
          tabOrderListBinding.txt.getPaint().setFakeBoldText(true);
          tabOrderListBinding.getRoot().setSelected(true);
        }

        tabBindings.add(tabOrderListBinding);
        return tabOrderListBinding.getRoot();
      }
    });

    // 同步到悬浮tab
    activityShopMainBinding.stLayoutSuspend.setCustomTabView(new SmartTabLayout.TabProvider() {
      @Override
      public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
        TabOrderListBinding tabOrderListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(ShopMainActivity.this), R.layout.tab_order_list, container, false);
        tabOrderListBinding.txt.setText(tabs[position]);
        tabOrderListBinding.txt
            .setTextColor(getResources().getColorStateList(R.color.color_tab_shop_main));
        if (position == 0) {
          tabOrderListBinding.txt.getPaint().setFakeBoldText(true);
          tabOrderListBinding.getRoot().setSelected(true);
        }

        tabSuspendBindings.add(tabOrderListBinding);
        return tabOrderListBinding.getRoot();
      }
    });

    activityShopMainBinding.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        if (tabBindings == null)
          return;

        refreshBottomByIndex(position, false);

        for (TabOrderListBinding binding : tabBindings) {
          binding.txt.getPaint().setFakeBoldText(false);
        }
        tabBindings.get(position).txt.getPaint().setFakeBoldText(true);

        if (tabSuspendBindings == null)
          return;
        for (TabOrderListBinding binding : tabSuspendBindings) {
          binding.txt.getPaint().setFakeBoldText(false);
        }
        tabSuspendBindings.get(position).txt.getPaint().setFakeBoldText(true);
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });
    activityShopMainBinding.stLayout.setViewPager(activityShopMainBinding.vp);
    activityShopMainBinding.stLayoutSuspend.setViewPager(activityShopMainBinding.vp);
  }

  /**
   * 评论门店动态
   * 
   * @param relationId
   */
  private void commentMoment(final String relationId) {
    if (relationId == null)
      return;
    if (IsEmpty.string(binding.edtComment.getText().toString().trim())) {
      ToastUtil.toastHint(ShopMainActivity.this, R.string.discovery_comment_empty);
      return;
    }
    new MomentCase(
        MomentConverter.createComment(binding.edtComment.getText().toString(), relationId))
            .execute(new HttpSubscriber<Comment>(ShopMainActivity.this) {
              @Override
              public void onFailure(String errorMsg, Response<Comment> response) {
                DialogUtil.getErrorDialog(ShopMainActivity.this, errorMsg).show();
              }

              @Override
              public void onSuccess(Response<Comment> response) {
                binding.edtComment.setText(null);
                KeyboardUtil.control(binding.edtComment, false);
                ToastUtil.toastHint(ShopMainActivity.this, R.string.discovery_comment_success);
                ((ShopMainMomentFragment) fragments.get(1)).refreshMomentCommentCount(relationId);
                ShopMainActivity.this.relationId = null;
              }
            });
  }

  /**
   * 评论门店动态，由子fragment主动调用
   * 
   * @param relationId
   */
  public void commentMomentFromFragment(String relationId) {
    this.relationId = relationId;
    refreshBottomByIndex(1, true);// 显示发送布局
    binding.layoutEdit.post(new Runnable() {
      @Override
      public void run() {
        binding.edtComment.requestFocus();
      }
    });
    KeyboardUtil.control(binding.edtComment, true);
  }

  /**
   * 根据当前viewpager index刷新底部状态栏
   * 
   * @param index
   * @param isForce
   *          是否强制显示，用于动态评论的软键盘弹出
   */
  private void refreshBottomByIndex(int index, boolean isForce) {
    if (bottomViews.size() == 0)
      return;
    binding.layoutBottomRoot.setVisibility(View.GONE);
    for (int i = 0; i < bottomViews.size(); i++) {
      bottomViews.get(i).setVisibility(View.GONE);
      if (index == i && (index == 0 || index == 2 || isForce)) {
        bottomViews.get(i).setVisibility(View.VISIBLE);
        binding.layoutBottomRoot.setVisibility(View.VISIBLE);
      }
    }
  }

  private class TabAdapter extends FragmentStatePagerAdapter {
    public TabAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      return fragments.get(position);
    }

    @Override
    public int getCount() {
      return fragments.size();
    }
  }

  public class Presenter implements SubjectPresenter {

    @Override
    public void onSubjectItemClick(Subject subject) {
      Intent intent = new Intent(ShopMainActivity.this, SubjectDetailActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, subject);
      intent.putExtra(AppConfig.IntentKey.SUMMARY, shop);
      startActivity(intent);
    }

    /**
     * 评论门店
     */
    public void onShopComment(View view) {
      Intent intent = new Intent(ShopMainActivity.this, CommentShopActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, shop.getId());
      startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * 评论动态
     *
     * @param view
     */
    public void onMomentComment(View view) {
      commentMoment(relationId);
    }

    public void onMobile(final String mobile) {
      PhoneUtil.call(ShopMainActivity.this, mobile);
    }

    /**
     * 地址详情
     *
     * @param shop
     */
    public void onAdd(Shop shop) {
      Intent intent = new Intent(ShopMainActivity.this, ShopMapActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, shop);
      startActivity(intent);
    }

    /**
     * 关注/取消关注门店
     * 
     * @param shop
     */
    public void follow(final Shop shop) {
      if (shop.isFavorite()) {
        // 如果已关注，则取消关注
        new CancelFollowLikeCase(LikeEntityType.store, shop.getId())
            .execute(new HttpSubscriber<Void>(ShopMainActivity.this) {
              @Override
              public void onFailure(String errorMsg, Response<Void> response) {
                DialogUtil.getErrorDialog(ShopMainActivity.this, errorMsg).show();
              }

              @Override
              public void onSuccess(Response<Void> response) {
                ToastUtil.toastHint(ShopMainActivity.this, "取消关注");
                shop.setFavorite(false);
                shop.setFavoriteCount(shop.getFavoriteCount() - 1);
                binding.setItem(shop);
              }
            });
        return;
      }
      // 关注该门店
      new FollowLikeCase(LikeEntityType.store, shop.getId())
          .execute(new HttpSubscriber<Void>(ShopMainActivity.this) {
            @Override
            public void onFailure(String errorMsg, Response<Void> response) {
              DialogUtil.getErrorDialog(ShopMainActivity.this, errorMsg).show();
            }

            @Override
            public void onSuccess(Response<Void> response) {
              ToastUtil.toastHint(ShopMainActivity.this, "关注成功");
              shop.setFavorite(true);
              shop.setFavoriteCount(shop.getFavoriteCount() + 1);
              binding.setItem(shop);
            }
          });
    }
  }

}
