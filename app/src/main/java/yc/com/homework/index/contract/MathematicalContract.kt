package yc.com.homework.index.contract

import yc.com.base.IPresenter
import yc.com.base.IView
import yc.com.homework.index.domain.bean.CompositionInfo

/**
 *
 * Created by wanglin  on 2019/4/2 10:52.
 */
interface MathematicalContract {
    interface View : IView {
        fun showMathematicalList(data: ArrayList<CompositionInfo>?)
    }

    interface Presenter : IPresenter {}
}