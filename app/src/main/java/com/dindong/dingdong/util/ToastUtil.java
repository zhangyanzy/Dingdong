package com.dindong.dingdong.util;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.dindong.dingdong.R;
import com.dindong.dingdong.databinding.ItemToastBinding;

/**
 * Created by wcong on 2018/3/10.
 */

public class ToastUtil {
    private static Toast currentToast;

    public static void toastHint(Context context, int stringResId) {
        String s = context.getResources().getString(stringResId);
        toast(context, s, 0);
    }

    public static void toastHint(Context context, String text) {
        toast(context, text, 0);
    }

    public static void toastFailure(Context context, int stringResId) {
        String text = context.getResources().getString(stringResId);
        toast(context, text, R.mipmap.ic_toast_warning);
    }

    public static void toastFailure(Context context, String text) {
        toast(context, text, R.mipmap.ic_toast_warning);
    }

    public static void toastSuccess(Context context, String text) {
        toast(context, text, R.mipmap.ic_toast_success);
    }

    public static void toastSuccess(Context context, int stringResId) {
        String text = context.getResources().getString(stringResId);
        toast(context, text, R.mipmap.ic_toast_success);
    }

    private static void toast(Context context, String text, int imageResource) {
        ItemToastBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context.getApplicationContext()), R.layout.item_toast, null, false);
        Toast toast = new Toast(context.getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        if (0 == imageResource) {
            binding.imageIv.setVisibility(View.GONE);
        }
        binding.imageIv.setImageResource(imageResource);
        binding.titleTv.setText(text);
        binding.titleTv.setMaxWidth(Resources.getSystem().getDisplayMetrics().widthPixels * 3 / 5);
        toast.setView(binding.getRoot());
        if (!IsEmpty.object(currentToast))
            currentToast.cancel();
        currentToast = toast;
        currentToast.show();
    }
}
