package com.dindong.dingdong.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dindong.dingdong.R;
import com.dindong.dingdong.presentation.main.IdentitySwitchActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(this, IdentitySwitchActivity.class));
    }
}
