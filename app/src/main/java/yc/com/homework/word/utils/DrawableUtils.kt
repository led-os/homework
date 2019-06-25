package yc.com.homework.word.utils


import android.app.Activity
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import yc.com.blankj.utilcode.util.SizeUtils

/**
 * Created by admin on 2017/7/28.
 */

object DrawableUtils {

    /**
     * @param mContext
     * @param radius   圆角半径
     * @param resColor resources资源颜色
     * @return
     */
    fun getBgColor(mContext: Context, radius: Int, resColor: Int): GradientDrawable {

        val roundRadius = SizeUtils.dp2px(radius.toFloat())
        //创建drawable
        val gradientDrawable = GradientDrawable()
        gradientDrawable.setColor(ContextCompat.getColor(mContext, resColor))
        gradientDrawable.cornerRadius = roundRadius.toFloat()
        return gradientDrawable
    }


    fun setBg(mContext: Context, radius: Int, width: Int, resColor: Int): GradientDrawable {
        val roundRadius = SizeUtils.dp2px(radius.toFloat())
        val widthpx = SizeUtils.dp2px(width.toFloat())
        val gradientDrawable = GradientDrawable()
        gradientDrawable.cornerRadius = roundRadius.toFloat()
        gradientDrawable.setStroke(widthpx, ContextCompat.getColor(mContext, resColor))
        return gradientDrawable
    }

//    /**
//     * 获取资源下的图片Uri
//     *
//     * @param context
//     * @return
//     */
//    fun getAddUri(context: Context): Uri {
//        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
//                + context.resources.getResourcePackageName(R.mipmap.note_image_add_icon) + "/"
//                + context.resources.getResourceTypeName(R.mipmap.note_image_add_icon) + "/"
//                + context.resources.getResourceEntryName(R.mipmap.note_image_add_icon))
//    }

    /**
     * 根据
     * @param context
     * @param uri
     * @return
     */
    fun getPathBuUri(context: Context, uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = (context as Activity).contentResolver.query(uri, projection, null, null, null)
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

}
