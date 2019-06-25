package yc.com.homework.base.utils

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.kk.utils.ScreenUtil
import yc.com.blankj.utilcode.util.ImageUtils
import yc.com.homework.R
import yc.com.homework.mine.utils.GlideCircleTransformation
import yc.com.homework.mine.utils.GlideRoundTransformation

/**
 *
 * Created by wanglin  on 2018/11/30 09:46.
 */
object GlideHelper {


    fun circleIvator(context: Context, imageView: ImageView, path: String?, placeHolder: Int, errorHolder: Int, flag: Boolean) {
        val bitmapRequest = Glide.with(context).load(path).asBitmap()
        if (flag) {
            val transformation = GlideCircleTransformation(context)
            bitmapRequest.transform(transformation)
        }
        bitmapRequest.placeholder(placeHolder).error(errorHolder).into(imageView)
    }

    fun circleIvator(context: Context, imageView: ImageView, path: String?, flag: Boolean) {
        circleIvator(context, imageView, path, R.mipmap.deafult_avator, R.mipmap.deafult_avator, flag)
    }

    fun cornerPic(context: Context, imageView: ImageView, path: String?, placeHolder: Int, errorHolder: Int, skipMemory: Boolean, flag: Boolean) {
        val bitmapRequest = Glide.with(context).load(path)
        if (flag) {
            val transformation = GlideRoundTransformation(context, ScreenUtil.dip2px(context, 10f), 0)
            bitmapRequest.bitmapTransform(transformation).crossFade(1000)
        }
        val typeRequest = bitmapRequest.asBitmap()
        if (placeHolder != 0)
            typeRequest.placeholder(placeHolder)
        if (errorHolder != 0)
            typeRequest.error(errorHolder)
        typeRequest.centerCrop().diskCacheStrategy(DiskCacheStrategy.RESULT).skipMemoryCache(skipMemory).thumbnail(0.1f).into(imageView)
    }

    fun cornerPic(context: Context, imageView: ImageView, path: String?, flag: Boolean) {
        cornerPic(context, imageView, path, R.mipmap.homework_exam, R.mipmap.exam_homework_exam, true, flag)
    }

    fun roundPic(context: Context, imageView: ImageView, path: Any?, placeHolder: Int, errorHolder: Int, raduis: Float, flag: Boolean) {
        val request = Glide.with(context).load(path).asBitmap()
        if (placeHolder != 0) {
            request.placeholder(placeHolder)
        }
        if (errorHolder != 0) {
            request.error(errorHolder)
        }
        request.centerCrop().into(object : BitmapImageViewTarget(imageView) {
            override fun onResourceReady(bitmap: Bitmap?, p1: GlideAnimation<in Bitmap>?) {
                var result = bitmap
                if (flag) {
                    result = ImageUtils.toRoundCorner(bitmap, ScreenUtil.dip2px(context, raduis).toFloat())
                }
                imageView.setImageBitmap(result)
            }
        })

    }

    fun roundPic(context: Context, imageView: ImageView, path: Any?, raduis: Float, flag: Boolean) {
        roundPic(context, imageView, path, R.mipmap.homework_exam, R.mipmap.exam_homework_exam, raduis, flag)
    }


}