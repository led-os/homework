package yc.com.homework.read.contract

import yc.com.base.IPresenter
import yc.com.base.IView
import yc.com.homework.read.domain.bean.BookInfo

/**
 *
 * Created by wanglin  on 2019/2/15 10:11.
 */
interface BookReadMainContract {

    interface View : IView {
        fun showBookInfos(t: List<BookInfo>?)
    }

    interface Presenter : IPresenter {
        fun getBookInfos()
        fun deleteBook(bookInfo: BookInfo?)
    }
}