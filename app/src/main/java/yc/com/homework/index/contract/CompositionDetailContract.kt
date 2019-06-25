package yc.com.homework.index.contract

import yc.com.base.*
import yc.com.homework.index.domain.bean.NewsInfo

/**
 *
 * Created by wanglin  on 2019/3/21 10:09.
 */
interface CompositionDetailContract {
    interface View : IView, ILoading, INoData, INoNet, IHide {
        fun showCompositionDetailInfo(data: NewsInfo?)
        fun showCompositionCollectState(collect: Int)
    }

    interface Presenter : IPresenter {}
}