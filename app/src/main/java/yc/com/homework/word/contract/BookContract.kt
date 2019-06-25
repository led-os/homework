package yc.com.homework.word.contract

import java.util.ArrayList

import yc.com.base.IDialog
import yc.com.base.IFinish
import yc.com.base.IHide
import yc.com.base.ILoading
import yc.com.base.IPresenter
import yc.com.base.IView
import yc.com.homework.word.model.domain.BookWordInfo

/**
 * Created by zhangkai on 2017/7/25.
 */

interface BookContract {
    interface View : IView, IDialog, IFinish, ILoading, IHide {
        fun showBookListData(list: ArrayList<BookWordInfo>?, isEdit: Boolean)

        fun deleteBookRefresh()
    }

    interface Presenter : IPresenter {
        fun bookList(currentPage: Int, pageCount: Int) //获取教材集合

        fun addBook(bookInfo: BookWordInfo?)

        fun deleteBook(bookInfo: BookWordInfo?)
    }
}
