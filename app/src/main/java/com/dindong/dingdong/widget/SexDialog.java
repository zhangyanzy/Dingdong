package com.dindong.dingdong.widget;

import java.util.ArrayList;
import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.widget.loopview.LoopView;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by wcong on 2018/3/28.
 * <p>
 * 性别选择 </>
 */

public class SexDialog extends Dialog implements View.OnClickListener {

  private DialogListener dialogListener;

  private List<Sex> source;

  public SexDialog(Context context) {
    super(context, R.style.FullScreenDialog);
    setContentView(R.layout.layout_picker_sex);
    initLoopView();
  }

  private void initLoopView() {
    source = new ArrayList<>();
    LoopView lp = (LoopView) findViewById(R.id.lp);
    findViewById(R.id.tv_cancel).setOnClickListener(this);
    findViewById(R.id.tx_finish).setOnClickListener(this);
    source.add(Sex.man);
    source.add(Sex.woman);
    List<String> items = new ArrayList<>();
    for (Sex sex : source) {
      items.add(sex.getName());
    }
    lp.setArrayList(items);
    lp.setNotLoop();
  }

  public SexDialog setDialogListener(DialogListener dialogListener) {
    this.dialogListener = dialogListener;
    return this;
  }

  public SexDialog setDefaultData(String defaultData) {
    for (int i = 0; i < source.size(); i++) {
      if (source.get(i).getName().equals(defaultData)) {
        ((LoopView) findViewById(R.id.lp)).setCurrentPosition(i);
        break;
      }
    }
    return this;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.tv_cancel:
      dismiss();
      break;
    case R.id.tx_finish:
      if (dialogListener != null)
        dialogListener.onSelect(source.get(((LoopView) findViewById(R.id.lp)).getSelectedItem()));
      dismiss();
      break;
    }
  }

  @Override
  public void show() {
    super.show();
    Window win = getWindow();
    win.getDecorView().setPadding(0, 0, 0, 0);
    WindowManager.LayoutParams lp = win.getAttributes();
    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
    win.setAttributes(lp);
    win.setGravity(Gravity.BOTTOM);
    win.setWindowAnimations(R.style.theme_animation_bottom_rising);
  }

  public interface DialogListener {
    void onSelect(Sex sex);
  }

  public enum Sex {
    man("男"), woman("女"), unknown("未知");
    private String name;

    Sex(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }
}