package yc.com.homework.index.contract

import yc.com.base.INoNet
import yc.com.base.IPresenter
import yc.com.base.IView
import yc.com.homework.index.domain.bean.IndexInfo
import yc.com.homework.index.domain.bean.NewsInfo

/**
 *
 * Created by wanglin  on 2019/2/15 14:34.
 */
interface IndexContract {
    interface View : IView, INoNet {
        fun showIndexInfo(t: IndexInfo?)
        fun showIndexRecommendInfos(t: ArrayList<NewsInfo>, fresh: Boolean)
    }

    interface Presenter : IPresenter {
        fun getIndexInfo()
    }
}