package com.dindong.dingdong.presentation.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityShopTeacherListBinding;
import com.dindong.dingdong.databinding.ItemShopTeacherListBinding;
import com.dindong.dingdong.network.bean.TeacherInfo;
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
    private List<TeacherInfo> teacherInfos;
    private SingleTypeAdapter adapter;
    private TeacherInfo info;

    @Override

    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_teacher_list);

        binding.shopTeacherTopBar.setCenterTitleText(R.string.shop_teacher_list);
        initAdapter();
    }

    public List<TeacherInfo> getData() {
        teacherInfos = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            info = new TeacherInfo("张琰", "Android 开发工程师，毕业于英国阿尔斯特大学，工科硕士学位");
            teacherInfos.add(info);
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


    public class Decorator implements BaseViewAdapter.Decorator {

        @Override
        public void decorator(BindingViewHolder holder, int position, int viewType) {
            if (holder.getBinding() == null || teacherInfos == null || teacherInfos.size() == 0)
                return;
            //Item布局文件的binding
            ItemShopTeacherListBinding itemBinding = (ItemShopTeacherListBinding) holder.getBinding();

            itemBinding.itemShopTeacherListTeacherName.setText(teacherInfos.get(position).getName());
            itemBinding.itemShopTeacherListTeacherInfo.setText(teacherInfos.get(position).getInfo());


//            for (TeacherInfo teacherInfo : teacherInfos) {
//                itemBinding.itemShopTeacherListTeacherName.addView(createDirectorTeacherName(teacherInfo.getName()));
//            }
        }
    }


    public class Presenter implements BaseViewAdapter.Presenter {
        public void onItemClick(TeacherInfo item) {
            Toast.makeText(getApplicationContext(), item.toString(), Toast.LENGTH_SHORT).show();
        }

    }

}
