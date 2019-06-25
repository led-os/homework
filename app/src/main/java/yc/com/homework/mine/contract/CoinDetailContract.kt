package yc.com.homework.mine.contract

import yc.com.base.*
import yc.com.homework.mine.domain.bean.CoinDetailInfoWrapper

/**
 *
 * Created by wanglin  on 2018/11/24 09:49.
 */
interface CoinDetailContract {

    interface View : IView, ILoading, INoData, INoNet, IHide {
        fun showCoinDetailInfos(t: CoinDetailInfoWrapper)
    }

    interface Presenter : IPresenter {
        fun getCoinDetailList(page: Int, page_size: Int)
    }
}