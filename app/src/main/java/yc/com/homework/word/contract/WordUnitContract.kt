package yc.com.homework.word.contract

import yc.com.base.IDialog
import yc.com.base.IFinish
import yc.com.base.IHide
import yc.com.base.ILoading
import yc.com.base.INoData
import yc.com.base.INoNet
import yc.com.base.IPresenter
import yc.com.base.IView
import yc.com.homework.word.model.domain.BookWordInfo
import yc.com.homework.word.model.domain.WordUnitInfoList

/**
 * Created by zhangkai on 2017/7/25.
 */

interface WordUnitContract {
    interface View : IView, IDialog, IFinish, INoData, INoNet, ILoading, IHide {
        fun showBookInfo(bookInfo: BookWordInfo?)
        fun showWordUnitListData(wordUnitInfoList: WordUnitInfoList?)
    }

    interface Presenter : IPresenter {
        fun getBookInfoById(bookId: String?)
        fun getWordUnitByBookId(currentPage: Int, pageCount: Int, bookId: String) //根据课本ID获取单词对应的单元列表集合
    }
}
