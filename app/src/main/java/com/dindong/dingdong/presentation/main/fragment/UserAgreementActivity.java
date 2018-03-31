package com.dindong.dingdong.presentation.main.fragment;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityUserAgreementBinding;
import com.dindong.dingdong.widget.NavigationTopBar;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class UserAgreementActivity extends BaseActivity {
  ActivityUserAgreementBinding binding;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_user_agreement);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    binding.webView.loadUrl("http://terms.dingdongbanxue.com/legal-agreement/terms/person.html");
    binding.webView.getSettings().setJavaScriptEnabled(true);
    binding.webView.setWebChromeClient(new WebChromeClient() {

      @Override
      public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (newProgress == binding.progressBar.getMax()) {
          binding.progressBar.setVisibility(View.GONE);
        } else {
          binding.progressBar.setProgress(newProgress);
        }
      }
    });
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
}
