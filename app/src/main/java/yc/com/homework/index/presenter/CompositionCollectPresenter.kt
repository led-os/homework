package yc.com.homework.index.presenter

import android.content.Context
import yc.com.base.BaseEngine
import yc.com.base.BasePresenter
import yc.com.base.BaseView
import yc.com.base.UIUtils
import yc.com.homework.base.HomeworkApp
import yc.com.homework.base.dao.CompositionInfoDao
import yc.com.homework.index.contract.CompositionCollectContract

/**
 *
 * Created by wanglin  on 2019/4/2 10:03.
 */
class CompositionCollectPresenter(context: Context, view: CompositionCollectContract.View) : BasePresenter<BaseEngine, CompositionCollectContract.View>(context, view), CompositionCollectContract.Presenter {
    private var compositionInfoDao: CompositionInfoDao? = null

    init {
        compositionInfoDao = HomeworkApp.daoSession?.compositionInfoDao
    }

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {
        if (!isForceUI) return
//        getCompositionCollectInfos()
    }

    fun getCompositionCollectInfos(page: Int, pageSize: Int) {
        val list = compositionInfoDao?.queryBuilder()?.limit(pageSize)?.offset((page - 1) * pageSize)?.orderDesc(CompositionInfoDao.Properties.SaveTime)?.build()?.list()
        UIUtils.post {
            if (list == null || list.isEmpty()) {
                if (page == 1) {
                    mView.showNoData()
                }
            } else {
                mView.hide()
                mView.showCollectInfos(list)
            }
        }
    }
}