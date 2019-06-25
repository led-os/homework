package yc.com.homework.word.contract

import yc.com.base.IDialog
import yc.com.base.IFinish
import yc.com.base.IHide
import yc.com.base.ILoading
import yc.com.base.INoData
import yc.com.base.INoNet
import yc.com.base.IPresenter
import yc.com.base.IView
import yc.com.homework.word.model.domain.WordInfo

/**
 * Created by zhangkai on 2017/7/25.
 */

interface ReadWordContract {
    interface View : IView, IDialog, IFinish, ILoading, INoData, INoNet, IHide {
        fun showWordListData(list: List<WordInfo>?)
    }

    interface Presenter : IPresenter {
        fun getWordListByUnitId(currentPage: Int, pageCount: Int, unitId: String?) //获取单词集合
    }
}
