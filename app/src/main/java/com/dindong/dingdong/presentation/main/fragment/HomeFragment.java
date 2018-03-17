package com.dindong.dingdong.presentation.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseFragment;
import com.dindong.dingdong.databinding.FragmentHomeBinding;
import com.dindong.dingdong.presentation.main.ShopTeacherListActivity;
import com.dindong.dingdong.util.ToastUtil;
import com.github.markzhai.recyclerview.BaseViewAdapter;
import com.github.markzhai.recyclerview.SingleTypeAdapter;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wcong on 2018/3/10.
 * 首页
 */

public class HomeFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private FragmentHomeBinding binding;
    //GridView数据放置在此List中
    private List<Map<String, Object>> dataList;
    //GridView图片
    private int[] icon = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    //GridView文字
    private String[] iconName = {"全部门店", "拼团上课", "叮咚公益", "全部课程"};
    //GridView适配器
    private SimpleAdapter mSimpleAdapter;
    //RecycleView所需List
    private List<String> mData;

    //    private HomeRecycleAdapter mRecycleAdapter;
    private List<String> mBannerList;
    private Intent intent;
    private SingleTypeAdapter mRecycleAdapter;

    @Override
    protected View initComponent(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {
        binding.excessHintHomeFragment.setMovementMethod(new ScrollingMovementMethod());
        /**
         * 初始化GridView
         */
        dataList = new ArrayList<Map<String, Object>>();
        mSimpleAdapter = new SimpleAdapter(getContext(), getGridViewData(), R.layout.item_grid_view,
                new String[]{"image", "text"},
                new int[]{R.id.item_image, R.id.item_textview});
        binding.gvChannel.setAdapter(mSimpleAdapter);
        binding.gvChannel.setOnItemClickListener(this);


        initRecycleAdapter();
        /**
         * 将获取到的网络图片放置在Banner中
         */
        mBannerList = new ArrayList();
        mBannerList.add("http://img4.imgtn.bdimg.com/it/u=1849328229,2650485437&fm=214&gp=0.jpg");
        mBannerList.add("http://images.7723.cn/attachments/jietu/44953/jietub7eee801f9dafadf265e3ad5cd1d132c20141202BM3YYc.jpg");
        mBannerList.add("http://img2.imgtn.bdimg.com/it/u=4100327403,4075914187&fm=214&gp=0.jpg");
        for (int i = 0; i < mBannerList.size(); i++) {
            String image = mBannerList.get(i);
        }
        binding.bannerHomeFragment.setBannerStyle(BannerConfig.CIRCLE_INDICATOR).setIndicatorGravity(BannerConfig.CENTER).setDelayTime(2000)
                .setImageLoader(new GlideImageLoader()).setImages(mBannerList).start();

    }

    /**
     * 初始化RecycleViewAdapter
     */
    private void initRecycleAdapter() {
//        mRecycleAdapter = new HomeRecycleAdapter(mData, getContext());
//        binding.subjectListHomeFragment.setLayoutManager(new LinearLayoutManager(getContext()));
//        binding.subjectListHomeFragment.setItemAnimator(new DefaultItemAnimator());
//        binding.subjectListHomeFragment.setAdapter(mRecycleAdapter);
        mRecycleAdapter = new SingleTypeAdapter(getContext(), R.layout.item_subject_list_home_fragment);
        mRecycleAdapter.setPresenter(new Presenter());
        //TODO HomeFragment数据添加
        mRecycleAdapter.set(getGridViewData());
        binding.subjectListHomeFragment.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.subjectListHomeFragment.setAdapter(mRecycleAdapter);
    }

    /**
     * 加载GridView的图片和文件名
     */
    private List<Map<String, Object>> getGridViewData() {
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            dataList.add(map);
        }
        return dataList;
    }
    /**
     *  加载RecycleViewData
     */
    private List<String> getRecycleData(){
        mData = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            mData.add("" + i);
        }
        return mData;
    }

    /**
     * GridView点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
//                intent = new Intent(getContext(), AllShopActivity.class);
                intent = new Intent(getContext(), ShopTeacherListActivity.class);
                startActivity(intent);
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

    /**
     * banner类图片加载
     */
    public class GlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, final Object path, ImageView imageView) {
            int index = 0;
            for (int i = 0; i < mBannerList.size(); i++) {
                if (mBannerList.get(i).equals(path)) {
                    index = i;
                }
            }
//            Glide.with(getContext()).load(path).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(imageView);
            final int finalIndex = index;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (finalIndex) {
                        case 0:
                            Toast.makeText(getActivity(), (String) path, Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Toast.makeText(getActivity(), (String) path, Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(getActivity(), (String) path, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }
    /**
     * RecycleView Item点击事件
     */
    class Presenter implements BaseViewAdapter.Presenter{
        public void onItemClick(){

        }

    }
}
