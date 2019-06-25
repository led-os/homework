package yc.com.homework.index.contract

import yc.com.base.*
import yc.com.homework.index.domain.bean.NewsInfo

/**
 *
 * Created by wanglin  on 2019/3/2 10:36.
 */
interface NewMoreContract {
    interface View : IView, ILoading, INoData, INoNet, IHide {
        fun showNewsInfos(data: ArrayList<NewsInfo>)
    }

    interface Presenter : IPresenter {}
}