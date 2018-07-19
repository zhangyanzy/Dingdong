package com.dindong.dingdong.widget;

import java.util.ArrayList;
import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.databinding.ActivityWidgetTestBinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * 组件测试类
 */
public class WidgetTestActivity extends AppCompatActivity {

  ActivityWidgetTestBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = DataBindingUtil.setContentView(this, R.layout.activity_widget_test);

    binding.btnTest.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new SelectionDialog(v)
            .setOnItemSelectListener(new SelectionDialog.OnItemSelectListener<String>() {
              @Override
              public void onItemSelect(SelectionDialog selectionDialog, int position,
                  SelectionDialog.SelectionSource<String> data) {
                if (position == 0) {
                  selectionDialog.setData(position, getTag1(3));
                } else if (position == 1) {
                  selectionDialog.setData(position, getTag2(data.getData(), 5));
                } else if (position > 1) {
                  binding.txt.setText(data.getData());
                }
              }
            }).show();
      }
    });
  }

  private List<SelectionDialog.SelectionSource> getTag1(int limit) {
    List<SelectionDialog.SelectionSource> sources = new ArrayList<>();
    for (int i = 0; i < limit; i++) {
      SelectionDialog.SelectionSource selectionSource = new SelectionDialog.SelectionSource();
      selectionSource.setData(String.format("tag\b%s", i + ""));
      selectionSource.setText(String.format("tag\b%s", i + ""));
      sources.add(selectionSource);
    }
    return sources;
  }

  private List<SelectionDialog.SelectionSource> getTag2(String tag, int limit) {
    List<SelectionDialog.SelectionSource> sources = new ArrayList<>();
    for (int i = 0; i < limit; i++) {
      SelectionDialog.SelectionSource source = new SelectionDialog.SelectionSource();
      source.setText(String.format(" from %s tab2 %s", tag, i));
      source.setData(String.format(" from %s tab2 %s", tag, i));
      sources.add(source);
    }
    return sources;
  }
}
