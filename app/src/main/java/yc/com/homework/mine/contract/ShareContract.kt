package yc.com.homework.mine.contract

import yc.com.base.IPresenter
import yc.com.base.IView

/**
 * Created by wanglin  on 2018/3/22 10:32.
 */

interface ShareContract {

    interface View : IView {

        fun showSuccess()
    }


    interface Presenter : IPresenter{}
}
