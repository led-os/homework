package yc.com.homework.read.contract

import yc.com.base.*
import yc.com.homework.read.domain.bean.BookDetailInfo

/**
 *
 * Created by wanglin  on 2019/1/14 13:51.
 */
interface BookDetailInfoContract {

    interface View : IView, INoData, INoNet, IHide, ILoading {
        fun showBookDetailInfo(bookDetailInfo: BookDetailInfo?)
    }

    interface Presenter : IPresenter {
        fun getBookDetailInfo(book_id: String?, page: Int)
    }
}