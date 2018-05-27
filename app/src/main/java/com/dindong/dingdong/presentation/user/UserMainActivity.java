package com.dindong.dingdong.presentation.user;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.adapter.SubjectAdapter;
import com.dindong.dingdong.adapter.SubjectPresenter;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityUserMainBinding;
import com.dindong.dingdong.databinding.ItemShopTagBinding;
import com.dindong.dingdong.databinding.ItemUserMomentBinding;
import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.like.usecase.CancelFollowLikeCase;
import com.dindong.dingdong.network.api.like.usecase.FollowLikeCase;
import com.dindong.dingdong.network.api.member.usecase.GetMemberCase;
import com.dindong.dingdong.network.api.moment.usecase.ListMomentCase;
import com.dindong.dingdong.network.api.subject.usecase.ListHotSubjectCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.auth.AuthIdentity;
import com.dindong.dingdong.network.bean.auth.User;
import com.dindong.dingdong.network.bean.comment.Comment;
import com.dindong.dingdong.network.bean.entity.FilterParam;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.like.LikeEntityType;
import com.dindong.dingdong.network.bean.store.Subject;
import com.dindong.dingdong.presentation.subject.SubjectDetailActivity;
import com.dindong.dingdong.presentation.subject.SubjectListActivity;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.TextFoldUtil;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.NavigationTopBar;
import com.dindong.dingdong.widget.baseadapter.BaseViewAdapter;
import com.dindong.dingdong.widget.baseadapter.BindingViewHolder;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 个人主页
 */
public class UserMainActivity extends BaseActivity {
  ActivityUserMainBinding binding;

  private boolean isCurrentUser = false;// 是否为当前用户
  private String userId;

  private TextFoldUtil textFoldUtil;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_user_main);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
    textFoldUtil = new TextFoldUtil();
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    userId = getIntent().getStringExtra(AppConfig.IntentKey.DATA);
    isCurrentUser = userId.equals(SessionMgr.getUser().getId());
    binding.setIsCurrentUser(isCurrentUser);
    getMember(userId);
    if (isCurrentUser) {
      binding.setUser(SessionMgr.getUser());
      for (String identity:SessionMgr.getUser().getIdentities()){
        if (identity.equals(AuthIdentity.ITEACHER.toString())||identity.equals(AuthIdentity.PTEACHER.toString())){
          listHotSubject();// 只有当前用户是老师身份才查看推荐课程
          continue;
        }
      }
      initTag(SessionMgr.getUser().getTags());
    }

    listMoment(true, true);
  }

  @Override
  protected void createEventHandlers() {
    binding.nb
        .setNavigationTopBarClickListener(new NavigationTopBar.NavigationTopBarClickListener() {
          @Override
          public void leftImageClick() {
            finish();
          }
        });
    binding.setPresenter(new Presenter());
  }

  /**
   * 获取热门推荐课程
   */
  private void listHotSubject() {
    binding.layoutSubject.setVisibility(View.VISIBLE);
    QueryParam queryParam = new QueryParam();
    queryParam.setLimit(2);
    queryParam.getFilters()
        .add(new FilterParam("cityCode", SessionMgr.getCurrentAdd().getCity().getId()));
    if (!IsEmpty.string(SessionMgr.getCurrentAdd().getLongitude())) {
      queryParam.getFilters()
          .add(new FilterParam("longitude", SessionMgr.getCurrentAdd().getLongitude()));
      queryParam.getFilters()
          .add(new FilterParam("latitude", SessionMgr.getCurrentAdd().getLatitude()));
    }

    new ListHotSubjectCase(queryParam).execute(new HttpSubscriber<List<Subject>>() {
      @Override
      public void onFailure(String errorMsg, Response<List<Subject>> response) {
      }

      @Override
      public void onSuccess(Response<List<Subject>> response) {
        SubjectAdapter subjectAdapter = new SubjectAdapter(UserMainActivity.this);
        subjectAdapter.setPresenter(new Presenter());
        subjectAdapter.set(response.getData());
        LinearLayoutManager manager = new LinearLayoutManager(UserMainActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.lstHotSubject.setLayoutManager(manager);
        binding.lstHotSubject.setAdapter(subjectAdapter);
        binding.lstHotSubject.setNestedScrollingEnabled(false);
      }
    });

  }

  /**
   * 获取个人状态
   * 
   * @param showProgress
   * @param isRefresh
   */
  private void listMoment(boolean showProgress, final boolean isRefresh) {
    final QueryParam param = new QueryParam();
    if (isRefresh) {
      param.setStart(0);
    }
    param.getFilters().add(new FilterParam("userId", userId));
    param.setLimit(99);

    new ListMomentCase(param)
        .execute(new HttpSubscriber<List<Comment>>(showProgress ? UserMainActivity.this : null) {
          @Override
          public void onFailure(String errorMsg, Response<List<Comment>> response) {
            DialogUtil.getErrorDialog(UserMainActivity.this, errorMsg).show();
          }

          @Override
          public void onSuccess(Response<List<Comment>> response) {
            loadRecyclerView(response.getData(), isRefresh, response.isMore());
          }
        });
  }

  /**
   * 加载用户标签
   * 
   * @param tags
   */
  private void initTag(List<String> tags) {
    binding.layoutTag.removeAllViews();
    for (int i = 0; i < tags.size(); i++) {
      ItemShopTagBinding itemShopTagBinding = DataBindingUtil.inflate(LayoutInflater.from(this),
          R.layout.item_shop_tag, null, false);
      if (i % 3 == 0) {
        itemShopTagBinding.root
            .setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_tag_user1));
      } else if (i % 3 == 1) {
        itemShopTagBinding.root
            .setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_tag_user2));
      } else if (i % 3 == 2) {
        itemShopTagBinding.root
            .setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_tag_user3));
      }
      itemShopTagBinding.txtTag.setText(tags.get(i));
      itemShopTagBinding.txtTag.setTextColor(Color.parseColor("#FFFFFF"));
      binding.layoutTag.addView(itemShopTagBinding.getRoot());
    }

  }

  public class Decorator implements BaseViewAdapter.Decorator {

    @Override
    public void decorator(BindingViewHolder holder, int position, int viewType) {
      if (holder == null || holder.getBinding() == null)
        return;
      ItemUserMomentBinding holderBinding = (ItemUserMomentBinding) holder.getBinding();
      holderBinding.pl.setRatio(1f);
      holderBinding.pl.setMargin(4);
      holderBinding.pl.setSource(holderBinding.getItem().getImages(), true);
      textFoldUtil.attach(holderBinding.txtContent, holderBinding.txtBottomContent,
          holderBinding.txtFold, holderBinding.getItem().getMessage(), position);
    }
  }

  /**
   * 获取用户最新信息
   * 
   * @param id
   */
  private void getMember(String id) {

    new GetMemberCase(id).execute(new HttpSubscriber<User>() {
      @Override
      public void onFailure(String errorMsg, Response<User> response) {

      }

      @Override
      public void onSuccess(Response<User> response) {
        binding.setUser(response.getData());
        initTag(response.getData().getTags());
      }
    });
  }

  private void loadRecyclerView(List<Comment> data, boolean isRefresh, boolean isMore) {
    SingleTypeAdapter adapter = new SingleTypeAdapter(this, R.layout.item_user_moment);
    if (isRefresh) {
      textFoldUtil.clean();
      adapter.clear();
    }
    adapter.addAll(data);
    adapter.setDecorator(new Decorator());
    if (isRefresh) {
      final LinearLayoutManager manager = new LinearLayoutManager(this);
      manager.setOrientation(LinearLayoutManager.VERTICAL);
      manager.setSmoothScrollbarEnabled(true);
      manager.setAutoMeasureEnabled(true);
      binding.lstMoment.setLayoutManager(manager);
      binding.lstMoment.setAdapter(adapter);

      binding.lstMoment.setHasFixedSize(true);
      binding.lstMoment.setNestedScrollingEnabled(false);

    }
  }

  public class Presenter implements SubjectPresenter {
    @Override
    public void onSubjectItemClick(Subject subject) {
      Intent intent = new Intent(UserMainActivity.this, SubjectDetailActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, subject);
      startActivity(intent);
    }

    /**
     * 更多课程
     */
    public void onMoreSubject() {
      startActivity(new Intent(UserMainActivity.this, SubjectListActivity.class));
    }

    /**
     * 关注/取消关注用户
     *
     * @param user
     */
    public void follow(final User user) {
      if (user.isFavorite()) {
        // 如果已关注，则取消关注
        new CancelFollowLikeCase(LikeEntityType.user, user.getId())
            .execute(new HttpSubscriber<Void>(UserMainActivity.this) {
              @Override
              public void onFailure(String errorMsg, Response<Void> response) {
                DialogUtil.getErrorDialog(UserMainActivity.this, errorMsg).show();
              }

              @Override
              public void onSuccess(Response<Void> response) {
                ToastUtil.toastHint(UserMainActivity.this, "取消关注");
                user.setFavorite(false);
                user.setFans(user.getFans() - 1);
                binding.setUser(user);
              }
            });
        return;
      }
      // 关注该用户
      new FollowLikeCase(LikeEntityType.user, user.getId())
          .execute(new HttpSubscriber<Void>(UserMainActivity.this) {
            @Override
            public void onFailure(String errorMsg, Response<Void> response) {
              DialogUtil.getErrorDialog(UserMainActivity.this, errorMsg).show();
            }

            @Override
            public void onSuccess(Response<Void> response) {
              ToastUtil.toastHint(UserMainActivity.this, "关注成功");
              user.setFavorite(true);
              user.setFans(user.getFans() + 1);
              binding.setUser(user);
            }
          });
    }
  }

}
