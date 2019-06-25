package yc.com.homework.index.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.youth.banner.loader.ImageLoader
import yc.com.homework.base.utils.GlideHelper

/**
 *
 * Created by wanglin  on 2019/2/13 11:27.
 */
class BannerImageLoader : ImageLoader() {
    override fun displayImage(context: Context, path: Any?, imageView: ImageView) {
        /**
        注意：
        1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
        2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
        传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
        切记不要胡乱强转！
         */

        //Glide 加载图片简单用法


//        GlideHelper.roundPic(context, imageView, path, 0, 0, 10f, true)

        Glide.with(context).load(path).asBitmap().into(imageView)

//        //Picasso 加载图片简单用法
//        Picasso.with(context).load(path).into(imageView);
//
//        //用fresco加载图片简单用法，记得要写下面的createImageView方法
//        Uri uri = Uri.parse((String) path);
//        imageView.setImageURI(uri);

    }

}