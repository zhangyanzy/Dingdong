package com.dindong.dingdong.presentation.main.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseFragment;
import com.dindong.dingdong.databinding.FragmentHomeBinding;
import com.dindong.dingdong.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by wcong on 2018/3/10.
 * 首页
 */

public class HomeFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private FragmentHomeBinding binding;

    private List<Map<String, Object>> dataList;
    //GridView图片
    private int[] icon = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    //GridView文字
    private String[] iconName = {"全部门店", "拼团上课", "叮咚公益", "全部课程"};
    //GridView适配器
    private SimpleAdapter mSimpleAdapter;


    @Override
    protected View initComponent(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }


    @Override
    protected void loadData(Bundle savedInstanceState) {
        /**
         * 初始化GridView
         */
        dataList = new ArrayList<Map<String, Object>>();
        mSimpleAdapter = new SimpleAdapter(getContext(), getData(), R.layout.item_grid_view,
                new String[]{"image", "text"},
                new int[]{R.id.item_image, R.id.item_textview});
        binding.gvChannel.setAdapter(mSimpleAdapter);
        binding.gvChannel.setOnItemClickListener(this);
    }

    /**
     * 加载GridView的图片和文件名
     *
     * @return
     */
    private List<Map<String, Object>> getData() {
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("name", iconName[i]);
            dataList.add(map);
        }
        return dataList;
    }

    /**
     * GridView点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                ToastUtil.toastSuccess(getContext(), "全部门店");
                break;
            case 1:
                ToastUtil.toastSuccess(getContext(), "拼团上课");
                break;
            case 2:
                ToastUtil.toastSuccess(getContext(), "叮咚公益");
                break;
            case 3:
                ToastUtil.toastSuccess(getContext(), "全部课程");
                break;
            default:
                break;
        }

    }
}
