package com.dindong.dingdong.presentation.main;

import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.Resource;
import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityShopTeacherListBinding;
import com.dindong.dingdong.databinding.ItemShopTeacherListBinding;
import com.dindong.dingdong.network.bean.TeacherBase;
import com.github.markzhai.recyclerview.BaseViewAdapter;
import com.github.markzhai.recyclerview.BindingViewHolder;
import com.github.markzhai.recyclerview.SingleTypeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 门店老师列表
 */

public class ShopTeacherListActivity extends BaseActivity {
    private ActivityShopTeacherListBinding binding;
    //数据存储list
    private List<TeacherBase.TeacherInfo> teacherInfos;
    private SingleTypeAdapter adapter;

    @Override

    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_teacher_list);
        binding.shopTeacherTopBar.setCenterTitleText(R.string.shop_teacher_list);
        initAdapter();
    }

    public List<TeacherBase.TeacherInfo> getData() {

        teacherInfos = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            teacherInfos.add(new TeacherBase.TeacherInfo("张琰", "Android 开发工程师，毕业于英国阿尔斯特大学，工科硕士学位"));
            Log.i("TAG", "getData: " + teacherInfos);
        }
        return teacherInfos;

    }

    private void initAdapter() {
        adapter = new SingleTypeAdapter(this, R.layout.item_shop_teacher_list);
        adapter.setPresenter(new Presenter());
        adapter.setDecorator(new Decorator());
        adapter.set(getData());
        binding.shopTeacherListRecycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.shopTeacherListRecycleView.setAdapter(adapter);
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {

    }

    /**
     * 添加老师姓名列表
     */

//    private TextView createDirectorTeacherName(String name) {
//        TextView textView = new TextView(ShopTeacherListActivity.this);
//        textView.setText(name);
////        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f);
////        textView.setTextColor(getResources().getColor(R.color.black));
////        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
////        textView.setLayoutParams(params);
////        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, Resources.getSystem().getDisplayMetrics());
////        params.setMargins(margin, 0, margin, 0);
////        textView.setLayoutParams(params);
//        return textView;
//    }

    public class Decorator implements BaseViewAdapter.Decorator {

        @Override
        public void decorator(BindingViewHolder holder, int position, int viewType) {
            if (holder.getBinding() == null || teacherInfos == null || teacherInfos.size() == 0)
                return;
            //Item布局文件的binding
            ItemShopTeacherListBinding itemBinding = (ItemShopTeacherListBinding) holder.getBinding();
//            itemBinding.itemShopTeacherListTeacherName.removeAllViews();
            itemBinding.itemShopTeacherListTeacherName.setText(teacherInfos.get(position).getName());
            itemBinding.itemShopTeacherListTeacherInfo.setText(teacherInfos.get(position).getInfo());

//            for (TeacherBase.TeacherInfo teacherInfo : teacherInfos) {
//                itemBinding.itemShopTeacherListTeacherName.addView(createDirectorTeacherName(teacherInfo.getName()));
//            }
        }
    }


    public class Presenter implements BaseViewAdapter.Presenter {
        public void onItemClick(TeacherBase.TeacherInfo item) {
            Toast.makeText(getApplicationContext(), item.getInfo(), Toast.LENGTH_SHORT).show();
        }

    }

}
