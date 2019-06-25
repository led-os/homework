package yc.com.homework.index.adapter


import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kk.utils.LogUtil

import yc.com.homework.R
import yc.com.homework.examine.widget.ZoomImageView


/**
 * Created by wanglin  on 2017/7/24 17:37.
 * 显示发布图片作业详情的信息
 */

class NewsPicDetailAdapter(private val mActivity: AppCompatActivity, private val mList: List<String>?) : PagerAdapter() {


    override fun getCount(): Int {
        return mList?.size ?: 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)

    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view = View.inflate(container.context, R.layout.activity_preview_picture, null)

        val imageView = view.findViewById<ZoomImageView>(R.id.zoomImageView)
        //        RelativeLayout mContainer = (RelativeLayout) view.findViewById(R.id.container);
        //        applyRotation(0, 90, mContainer);
        container.addView(view)
        val path = mList!![position]

        Glide.with(mActivity).load(path).thumbnail(0.1f).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageView)


        imageView.setOnClickListener { mActivity.finish() }


        return view
    }


}
