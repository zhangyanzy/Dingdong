package com.dindong.dingdong.presentation.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityIdentitySwitchBinding;
import com.dindong.dingdong.network.bean.auth.AuthIdentity;

public class IdentitySwitchActivity extends BaseActivity {

    private ActivityIdentitySwitchBinding binding;

    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {

    }

    @Override
    protected void createEventHandlers() {
        binding.setPresenter(new Presenter());
    }

    public class Presenter {
        public void onIdentitySwitch(AuthIdentity identity) {
            if (identity.equals(AuthIdentity.STUDENT)) {
                //学生
            } else if (identity.equals(AuthIdentity.TEACHER)) {
                //老师
            } else if (identity.equals(AuthIdentity.INSTITUTION)) {
                //机构
            }
        }
    }
}
