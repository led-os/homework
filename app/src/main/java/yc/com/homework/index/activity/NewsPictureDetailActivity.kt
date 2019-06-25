package yc.com.homework.index.activity

import kotlinx.android.synthetic.main.activity_news_picture_item.*
import yc.com.base.BaseActivity
import yc.com.base.BaseEngine
import yc.com.base.BasePresenter
import yc.com.base.BaseView
import yc.com.homework.R
import yc.com.homework.index.adapter.NewsPicDetailAdapter

/**
 * Created by wanglin  on 2017/8/14 09:21.
 */

class NewsPictureDetailActivity : BaseActivity<BasePresenter<BaseEngine, BaseView>>() {


    override fun init() {
        if (intent != null) {
            val paths = intent.getStringArrayListExtra("mList")
            val position = intent.getIntExtra("position", -1)
            viewPager!!.adapter = NewsPicDetailAdapter(this, paths)
            viewPager!!.currentItem = position
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_news_picture_item
    }

    override fun isStatusBarMateria(): Boolean {
        return true
    }
}
