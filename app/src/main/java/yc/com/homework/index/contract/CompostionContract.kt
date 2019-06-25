package yc.com.homework.index.contract

import yc.com.base.IPresenter
import yc.com.base.IView
import yc.com.homework.index.domain.bean.CompositionInfo

/**
 *
 * Created by wanglin  on 2019/3/20 13:55.
 */
interface CompostionContract {
    interface View : IView {
        fun showCompostionInfos(data: ArrayList<CompositionInfo>?)
    }

    interface Presenter : IPresenter {}
}