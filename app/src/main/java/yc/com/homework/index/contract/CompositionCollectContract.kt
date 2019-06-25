package yc.com.homework.index.contract

import yc.com.base.IHide
import yc.com.base.INoData
import yc.com.base.IPresenter
import yc.com.base.IView
import yc.com.homework.index.domain.bean.CompositionInfo

/**
 *
 * Created by wanglin  on 2019/4/2 10:04.
 */
interface CompositionCollectContract {
    interface View : IView,INoData,IHide {
        fun showCollectInfos(list: List<CompositionInfo>?)
    }
    interface Presenter : IPresenter {}


}