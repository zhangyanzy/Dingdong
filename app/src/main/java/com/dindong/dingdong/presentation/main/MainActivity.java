package com.dindong.dingdong.presentation.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityMainBinding;
import com.dindong.dingdong.network.bean.auth.AuthIdentity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;

    private String[] tab_student;
    private String[] tab_teacher;
    private String[] tab_institution;
    private String[] tabs;

    private List<View> tabViews;

    private View lastSelectView;

    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        tab_student = new String[]{getString(R.string.main_tab_home), getString(R.string.main_tab_discovery),
                getString(R.string.main_tab_msg), getString(R.string.main_tab_mine)};
        tab_teacher = new String[]{getString(R.string.main_tab_discovery), getString(R.string.main_tab_student),
                getString(R.string.main_tab_msg), getString(R.string.main_tab_mine)};
        tab_institution = new String[]{getString(R.string.main_tab_work),
                getString(R.string.main_tab_msg), getString(R.string.main_tab_mine)};
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {
        if (getIntent().getSerializableExtra(AppConfig.IntentKey.DATA) != null)
            initTab((AuthIdentity) getIntent().getSerializableExtra(AppConfig.IntentKey.DATA));

    }


    @Override
    protected void createEventHandlers() {
        binding.setPresenter(new Presenter());
    }

    private void initTab(AuthIdentity identity) {
        tabViews = new ArrayList<>();
        int tabPosition = 0;
        if (identity.equals(AuthIdentity.STUDENT))
            tabs = tab_student;
        else if (identity.equals(AuthIdentity.STUDENT))
            tabs = tab_teacher;
        else if (identity.equals(AuthIdentity.STUDENT))
            tabs = tab_institution;
        for (int i = 0; i < binding.tab.getChildCount(); i++) {
            if (binding.tab.getChildAt(i) instanceof CardView) {
                if (identity.equals(AuthIdentity.INSTITUTION))
                    //机构身份，隐藏中间btn
                    binding.tab.getChildAt(i).setVisibility(View.GONE);
                continue;
            }
            if (identity.equals(AuthIdentity.INSTITUTION) && i == 0) {
                //机构身份，隐藏第一个tab
                binding.tab.getChildAt(i).setVisibility(View.GONE);
                continue;
            }
            tabViews.add(((ViewGroup) binding.tab.getChildAt(i)).getChildAt(0));
            if (tabPosition == 0) {
                lastSelectView = tabViews.get(0);
                setTabSelect(0);
            }
            ((TextView) ((ViewGroup) tabViews.get(tabPosition))
                    .getChildAt(1)).setText(tabs[tabPosition++]);
        }
    }

    private void setTabSelect(int index) {
        if (tabViews == null)
            return;
        lastSelectView.setSelected(false);
        lastSelectView = tabViews.get(index);
        lastSelectView.setSelected(true);
    }

    public class Presenter {
        public void onTabSelect(View view) {
            for (int i = 0; i < tabs.length; i++) {
                if (((TextView) (((ViewGroup) view).getChildAt(1))).getText().toString().equals(tabs[i])) {
                    setTabSelect(i);
                    return;
                }
            }
        }
    }
}
