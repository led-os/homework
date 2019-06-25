package yc.com.homework.welfare.contract

import yc.com.base.IPresenter
import yc.com.base.IView
import yc.com.homework.welfare.domain.bean.WelfareInfo

/**
 *
 * Created by wanglin  on 2019/3/12 15:53.
 */
interface WelfareContract {
    interface View : IView {
        fun showWelfareInfo(h5page: WelfareInfo)
    }

    interface Presenter : IPresenter {}
}