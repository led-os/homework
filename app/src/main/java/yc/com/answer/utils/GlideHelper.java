package yc.com.answer.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import yc.com.homework.R;

/**
 * Created by wanglin  on 2018/4/20 15:37.
 */

public class GlideHelper {


    public static void loadImage(Context context, String path, ImageView imageView, int placeHolder, boolean isCenterCrop) {

        BitmapTypeRequest<String> request = Glide.with(context).load(path).asBitmap();
        if (isCenterCrop) {
            request.centerCrop();
        }
        request.placeholder(placeHolder).error(placeHolder).diskCacheStrategy(DiskCacheStrategy.RESULT).skipMemoryCache(true).into(imageView);
    }

    public static void loadImage(Context context, String path, ImageView imageView, int placeHolder) {
        loadImage(context, path, imageView, placeHolder, true);
    }

    public static void loadImage(Context context, String path, ImageView imageView) {
        loadImage(context, path, imageView, R.mipmap.small_placeholder);
    }


}
