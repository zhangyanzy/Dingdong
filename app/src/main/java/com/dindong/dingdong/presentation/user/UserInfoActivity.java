package com.dindong.dingdong.presentation.user;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import org.joda.time.DateTime;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityUserInfoBinding;
import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.image.usecase.UploadImageCase;
import com.dindong.dingdong.network.api.member.usecase.ModifyAvatarCase;
import com.dindong.dingdong.network.api.member.usecase.ModifyBirthdayCase;
import com.dindong.dingdong.network.api.member.usecase.ModifyDescriptionCase;
import com.dindong.dingdong.network.api.member.usecase.ModifyNameCase;
import com.dindong.dingdong.network.api.member.usecase.ModifySexCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.GlobalImage;
import com.dindong.dingdong.util.DateUtil;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.EnterInfoDialog;
import com.dindong.dingdong.widget.NavigationTopBar;
import com.dindong.dingdong.widget.SexDialog;
import com.dindong.dingdong.widget.date.DatePicker;
import com.dindong.dingdong.widget.photo.PhotoLayout;
import com.dindong.dingdong.widget.photo.PhotoSelectActivity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserInfoActivity extends BaseActivity {

  ActivityUserInfoBinding binding;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_user_info);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    binding.setUser(SessionMgr.getUser());
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
   * 上传图片
   * 
   * @param photos
   */
  private void uploadPhoto(ArrayList<String> photos) {
    if (IsEmpty.list(photos))
      return;

    File file = new File(photos.get(0));
    final RequestBody requestFile;
    requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
    MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file", file.getName(),
        requestFile);

    new UploadImageCase(imagePart).execute(new HttpSubscriber<GlobalImage>(this) {
      @Override
      public void onFailure(String errorMsg, Response<GlobalImage> response) {
        DialogUtil.getErrorDialog(UserInfoActivity.this, errorMsg).show();
      }

      @Override
      public void onSuccess(final Response<GlobalImage> response) {
        new ModifyAvatarCase(response.getData().getUrl())
            .execute(new HttpSubscriber<Void>(UserInfoActivity.this) {
              @Override
              public void onFailure(String errorMsg, Response<Void> response) {
                DialogUtil.getErrorDialog(UserInfoActivity.this, errorMsg);
              }

              @Override
              public void onSuccess(Response<Void> response1) {
                SessionMgr.getUser().setUserImage(response.getData());
                SessionMgr.updateUser();
                binding.setUser(SessionMgr.getUser());
                ToastUtil.toastHint(UserInfoActivity.this, "修改成功");
              }
            });
      }
    });

  }

  public class Presenter {

    /**
     * 修改用户头像
     */
    public void onModifyAvatar() {
      Intent intent = new Intent(UserInfoActivity.this, PhotoSelectActivity.class);
      intent.putExtra(PhotoLayout.SELECT_COUNT, 1);
      startActivity(intent);
      PhotoSelectActivity.setPhotoListener(new PhotoSelectActivity.PhotoListener() {
        @Override
        public void onChoose(final ArrayList<String> photos) {
          uploadPhoto(photos);
        }
      });
    }

    /**
     * 修改用户昵称
     */
    public void onModifyName() {
      DialogUtil.getEnterDialog(UserInfoActivity.this, "修改昵称", "输入昵称",
          SessionMgr.getUser().getName(), new EnterInfoDialog.OnConfirmListener() {
            @Override
            public void onConfirm(final String code) {
              new ModifyNameCase(code).execute(new HttpSubscriber<Void>(UserInfoActivity.this) {
                @Override
                public void onFailure(String errorMsg, Response<Void> response) {
                  DialogUtil.getErrorDialog(UserInfoActivity.this, errorMsg);
                }

                @Override
                public void onSuccess(Response<Void> response) {
                  SessionMgr.getUser().setName(code);
                  SessionMgr.updateUser();
                  binding.setUser(SessionMgr.getUser());
                  ToastUtil.toastHint(UserInfoActivity.this, "修改成功");
                }
              });
            }
          }).show();
    }

    /**
     * 修改性别
     */
    public void onModifySex() {
      new SexDialog(UserInfoActivity.this).setDefaultData(SessionMgr.getUser().getSex())
          .setDialogListener(new SexDialog.DialogListener() {
            @Override
            public void onSelect(final SexDialog.Sex sex) {
              new ModifySexCase(sex.equals(SexDialog.Sex.man) ? "0" : "1")
                  .execute(new HttpSubscriber<Void>(UserInfoActivity.this) {
                    @Override
                    public void onFailure(String errorMsg, Response<Void> response) {
                      DialogUtil.getErrorDialog(UserInfoActivity.this, errorMsg);
                    }

                    @Override
                    public void onSuccess(Response<Void> response) {
                      SessionMgr.getUser().setSex(sex.equals(SexDialog.Sex.man) ? "0" : "1");
                      SessionMgr.updateUser();
                      binding.setUser(SessionMgr.getUser());
                      ToastUtil.toastHint(UserInfoActivity.this, "修改成功");
                    }
                  });

            }
          }).show();
    }

    /**
     * 修改生日
     */
    public void onModifyBirthday() {
      DatePicker dayPicker = new DatePicker.Builder(UserInfoActivity.this)
          .setSelectDate(SessionMgr.getUser().getBirthday() == null ? new Date()
              : SessionMgr.getUser().getBirthday())
          .setMinDate(DateTime.now().minusYears(100).toDate()).setMaxDate(new Date())
          .setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
            @Override
            public void onDateSelected(final Date date) {
              new ModifyBirthdayCase(DateUtil.format(date, DateUtil.DEFAULT_DATE_FORMAT_3))
                  .execute(new HttpSubscriber<Void>(UserInfoActivity.this) {
                    @Override
                    public void onFailure(String errorMsg, Response<Void> response) {
                      DialogUtil.getErrorDialog(UserInfoActivity.this, errorMsg);
                    }

                    @Override
                    public void onSuccess(Response<Void> response) {
                      SessionMgr.getUser().setBirthday(date);
                      SessionMgr.updateUser();
                      binding.setUser(SessionMgr.getUser());
                      ToastUtil.toastHint(UserInfoActivity.this, "修改成功");
                    }
                  });

            }

            @Override
            public void onCancel() {

            }
          }).create();
      dayPicker.show();
    }

    /**
     * 修改个人简介
     */
    public void onModifyDescription() {
      DialogUtil.getEnterDialog(UserInfoActivity.this, "修改个人简介", "输入个人简介",
          SessionMgr.getUser().getRemark(), new EnterInfoDialog.OnConfirmListener() {
            @Override
            public void onConfirm(final String code) {
              new ModifyDescriptionCase(code)
                  .execute(new HttpSubscriber<Void>(UserInfoActivity.this) {
                    @Override
                    public void onFailure(String errorMsg, Response<Void> response) {
                      DialogUtil.getErrorDialog(UserInfoActivity.this, errorMsg);
                    }

                    @Override
                    public void onSuccess(Response<Void> response) {
                      SessionMgr.getUser().setRemark(code);
                      SessionMgr.updateUser();
                      binding.setUser(SessionMgr.getUser());
                      ToastUtil.toastHint(UserInfoActivity.this, "修改成功");
                    }
                  });
            }
          }).show();
    }

  }
}
