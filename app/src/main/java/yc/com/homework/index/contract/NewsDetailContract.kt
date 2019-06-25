package yc.com.homework.index.contract

import yc.com.base.*
import yc.com.homework.index.domain.bean.NewsInfo

/**
 *
 * Created by wanglin  on 2019/3/4 08:44.
 */
interface NewsDetailContract {

    interface View : IView, ILoading, INoNet, INoData, IHide {
        fun showNewsDetailInfo(info: NewsInfo)
    }

    interface Presenter : IPresenter {
        fun getNewsDetailInfo(newsId: String)
    }
}