package yc.com.homework.read.contract

import yc.com.base.*
import yc.com.homework.read.domain.bean.BookUnitInfoWrapper

/**
 *
 * Created by wanglin  on 2019/1/11 11:19.
 */
interface BookInfoContract {
    interface View : IView, IHide, INoData, INoNet, ILoading {
        fun showBookUnitInfo(data: BookUnitInfoWrapper?)
    }

    interface Presenter : IPresenter {
        fun getBookUnitInfo(volumes_id: String?, version_id: String?, grade_id: String?, subject_id: String?)
    }
}