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

/**
 * Created by zhangyan on 2018/3/10.
 */

public class HomeRecycleAdapter extends RecyclerView.Adapter<HomeRecycleAdapter.ViewHolder> {

    private List<String> mData;
    private Context mContext;

    public HomeRecycleAdapter(List<String> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject_list_home_fragment, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.mPhoto.setImageResource(R.mipmap.ic_launcher);
        holder.mTitle.setText("初中英语1V1体验试学班"+ mData.get(position));
        holder.mSchoolName.setText("英孚教育（浦江店）");
        holder.mDistance.setText("600m");
        holder.mAddress.setText("上海市闵行区江文路599弄54号");
        holder.mPrice.setText("¥ 1234");
        holder.mPurchaseSum.setText("30人");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mPhoto;
        private TextView mTitle;
        private TextView mSchoolName;
        private TextView mDistance;
        private TextView mAddress;
        private TextView mPrice;
        private TextView mPurchaseSum;


        public ViewHolder(View itemView) {
            super(itemView);
            mPhoto = (ImageView) itemView.findViewById(R.id.item_image);
            mTitle = (TextView) itemView.findViewById(R.id.item_subject_title);
            mSchoolName = (TextView) itemView.findViewById(R.id.item_subject_school_name);
            mDistance = (TextView) itemView.findViewById(R.id.item_distance);
            mAddress = (TextView) itemView.findViewById(R.id.item_address);
            mPrice = (TextView) itemView.findViewById(R.id.item_price);
            mPurchaseSum = (TextView) itemView.findViewById(R.id.item_purchase_sum);
        }
    }
}
