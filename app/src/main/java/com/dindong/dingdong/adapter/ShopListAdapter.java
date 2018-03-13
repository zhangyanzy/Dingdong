package com.dindong.dingdong.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dindong.dingdong.R;

import java.util.List;

import static com.dindong.dingdong.R.id.shop_list_ke_money;

/**
 * Created by zhangyan on 2018/3/11.
 */

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.ShopViewHolder> {


    private Context context;
    private List<Object> mlist;

    public ShopListAdapter(Context context, List<Object> mlist) {
        this.context = context;
        this.mlist = mlist;
    }

    @Override
    public ShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ShopViewHolder holder = new ShopViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_shop_list, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ShopViewHolder holder, int position) {
//        holder.mPhoto.setImageResource(R.mipmap.ic_launcher);
        holder.mSchoolName.setText("英孚教育（浦江店）");
        holder.mListDistance.setText("678");
        holder.mListAddress.setText("上海市闵行区江文路599弄54号");
        holder.mListCampus.setText("迈皋桥校区");
        holder.mListEnglish.setText("英语外教");

        holder.mTuanName.setText("新概念（青少年版）二年级 全集随到随学");
        holder.mTuanMoney.setText("1916");
        holder.mKeName.setText("新概念（青少年版）一年级 全集随到随学");
        holder.mKeMoney.setText("1100");

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    class ShopViewHolder extends RecyclerView.ViewHolder {

        private ImageView mPhoto;
        private TextView mSchoolName;
        private TextView mListDistance;//距离
        private TextView mListAddress;
        private TextView mListCampus;//迈皋桥校区
        private TextView mListEnglish;//英语外教
        //团   暂时未给TextView
        private TextView mTuanName;
        private TextView mTuanMoney;
        //课
        private TextView mKeName;
        private TextView mKeMoney;


        public ShopViewHolder(View itemView) {
            super(itemView);
            mPhoto = (ImageView) itemView.findViewById(R.id.shop_list_subject_photo);
            mSchoolName = (TextView) itemView.findViewById(R.id.shop_list_school_name);
            mListAddress = (TextView) itemView.findViewById(R.id.shop_list_item_address);
            mListDistance = (TextView) itemView.findViewById(R.id.shop_list_item_distance);
            mListCampus = (TextView) itemView.findViewById(R.id.shop_list_Campus);
            mListEnglish = (TextView) itemView.findViewById(R.id.shop_list_english_teacher);
            mTuanName = (TextView) itemView.findViewById(R.id.shop_list_tuan_name);
            mTuanMoney = (TextView) itemView.findViewById(R.id.shop_list_tuan_money);
            mKeName = (TextView) itemView.findViewById(R.id.shop_list_ke_name);
            mKeMoney = (TextView) itemView.findViewById(shop_list_ke_money);

        }
    }
}
