package yc.com.homework.read.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.kk.utils.LogUtil
import com.kk.utils.ScreenUtil

import yc.com.homework.R
import yc.com.homework.read.domain.bean.BookInfo
import yc.com.homework.read.domain.bean.BookUnitInfo

/**
 * Created by User on 2017/7/24.
 */

class ItemHeaderDecoration(mContext: Context, private val mList: List<BookUnitInfo>?) : RecyclerView.ItemDecoration() {
    private val mLayoutInflater: LayoutInflater
    private var mTitleHight = 50
    private val mTitleTextSize = 20

    init {
        mTitleHight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, mContext.resources.displayMetrics).toInt()
        // mTitleTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, context.getResources().getDisplayMetrics());
        this.mLayoutInflater = LayoutInflater.from(mContext)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        //获取到视图中第一个可见的item的position
        mList?.let {
            val position = (parent.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            val tag = mList[position].tag
            val title = mList[position].commonTitle
            val child = parent.findViewHolderForLayoutPosition(position)?.itemView
            var flag = false
            if (position + 1 < mList.size) {
                val suspensionTag = mList[position + 1].tag
                if (tag != suspensionTag) {
                    Log.d("ZHG-TEST", "!!!!!!!!!!!!!child.Height() = " + child?.height + " , child.top = " + child?.top + " , mTitleHight = " + mTitleHight)
                    child?.let {
                        if (child.height + child.top < mTitleHight) {
                            c.save()
                            flag = true
                            c.translate(0f, (child.height + child.top - mTitleHight).toFloat())
                        }
                    }

                }
            }
            val topTitleView = mLayoutInflater.inflate(R.layout.book_outlin_item, parent, false)
            val textView = topTitleView.findViewById<TextView>(R.id.tv_item_title)
            //textView.setTextSize(mTitleTextSize);
            textView.text = title
            val toDrawWidthSpec: Int//用于测量的widthMeasureSpec
            val toDrawHeightSpec: Int//用于测量的heightMeasureSpec
            var lp: RecyclerView.LayoutParams? = topTitleView.layoutParams as RecyclerView.LayoutParams
            if (lp == null) {
                lp = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//这里是根据复杂布局layout的width height，new一个Lp
                topTitleView.layoutParams = lp
            }
            topTitleView.layoutParams = lp
            toDrawWidthSpec = when {
                lp.width == ViewGroup.LayoutParams.MATCH_PARENT -> //如果是MATCH_PARENT，则用父控件能分配的最大宽度和EXACTLY构建MeasureSpec。
                    View.MeasureSpec.makeMeasureSpec(parent.width - parent.paddingLeft - parent.paddingRight, View.MeasureSpec.EXACTLY)
                lp.width == ViewGroup.LayoutParams.WRAP_CONTENT -> //如果是WRAP_CONTENT，则用父控件能分配的最大宽度和AT_MOST构建MeasureSpec。
                    View.MeasureSpec.makeMeasureSpec(parent.width - parent.paddingLeft - parent.paddingRight, View.MeasureSpec.AT_MOST)
                else -> //否则则是具体的宽度数值，则用这个宽度和EXACTLY构建MeasureSpec。
                    View.MeasureSpec.makeMeasureSpec(lp.width, View.MeasureSpec.EXACTLY)
            }
            //高度同理
            toDrawHeightSpec = when {
                lp.height == ViewGroup.LayoutParams.MATCH_PARENT -> View.MeasureSpec.makeMeasureSpec(parent.height - parent.paddingTop - parent.paddingBottom, View.MeasureSpec.EXACTLY)
                lp.height == ViewGroup.LayoutParams.WRAP_CONTENT -> View.MeasureSpec.makeMeasureSpec(parent.height - parent.paddingTop - parent.paddingBottom, View.MeasureSpec.AT_MOST)
                else -> View.MeasureSpec.makeMeasureSpec(mTitleHight, View.MeasureSpec.EXACTLY)
            }
            //依次调用 measure,layout,draw方法，将复杂头部显示在屏幕上。
            topTitleView.measure(toDrawWidthSpec, toDrawHeightSpec)
            topTitleView.layout(parent.paddingLeft, parent.paddingTop, parent.paddingLeft + topTitleView.measuredWidth, parent.paddingTop + topTitleView.measuredHeight)
            topTitleView.draw(c)//Canvas默认在视图顶部，无需平移，直接绘制
            if (flag)
                c.restore()//恢复画布到之前保存的状态
            if (tag != currentTag) {
                currentTag = tag
            }
        }


    }

    companion object {
        var currentTag: Int = 0

//        fun setCurrentTag(tag: Int) {
//            ItemHeaderDecoration.currentTag = tag
//        }
    }
}
