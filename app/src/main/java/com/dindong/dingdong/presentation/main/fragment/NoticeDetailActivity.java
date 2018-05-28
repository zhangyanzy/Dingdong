package com.dindong.dingdong.presentation.main.fragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityNoticeDetailBinding;
import com.dindong.dingdong.network.bean.notice.PublicNotice;
import com.dindong.dingdong.widget.NavigationTopBar;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;

/**
 * 公告详情
 */
public class NoticeDetailActivity extends BaseActivity {
  ActivityNoticeDetailBinding binding;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_notice_detail);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    final PublicNotice notice = (PublicNotice) getIntent()
        .getSerializableExtra(AppConfig.IntentKey.DATA);
    binding.setNotice(notice);
    binding.txtContent.setText(Html.fromHtml(notice.getContent(), new NetworkImageGetter(), null));
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
  }

  private final class NetworkImageGetter implements Html.ImageGetter {

    @Override
    public Drawable getDrawable(String source) {
      // TODO Auto-generated method stub

      LevelListDrawable d = new LevelListDrawable();
      new LoadImage().execute(source, d);
      return d;
    }

  }

  /**** 异步加载图片 **/
  private final class LoadImage extends AsyncTask<Object, Void, Bitmap> {

    private LevelListDrawable mDrawable;

    @Override
    protected Bitmap doInBackground(Object... params) {
      String source = (String) params[0];
      mDrawable = (LevelListDrawable) params[1];

      try {
        InputStream is = new URL(source).openStream();
        return BitmapFactory.decodeStream(is);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (MalformedURLException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

      if (bitmap != null) {
        BitmapDrawable d = new BitmapDrawable(bitmap);
        mDrawable.addLevel(1, 1, d);
        mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        // mDrawable.setBounds(0, 0,
        // getWindowManager().getDefaultDisplay().getWidth(),
        // bitmap.getHeight());
        mDrawable.setLevel(1);
        CharSequence t = binding.txtContent.getText();
        binding.txtContent.setText(t);
      }
    }
  }
}
