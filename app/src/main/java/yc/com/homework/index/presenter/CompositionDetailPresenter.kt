package yc.com.homework.index.presenter

import android.content.Context
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import rx.Subscriber
import yc.com.base.BasePresenter
import yc.com.homework.base.HomeworkApp
import yc.com.homework.base.dao.CompositionInfoDao
import yc.com.homework.index.contract.CompositionDetailContract
import yc.com.homework.index.domain.bean.CompositionInfo
import yc.com.homework.index.domain.bean.NewsInfoWrapper
import yc.com.homework.index.domain.engine.CompositionDetailEngine

/**
 *
 * Created by wanglin  on 2019/3/21 10:08.
 */
class CompositionDetailPresenter(context: Context, view: CompositionDetailContract.View) : BasePresenter<CompositionDetailEngine, CompositionDetailContract.View>(context, view), CompositionDetailContract.Presenter {
    private var compositionInfoDao: CompositionInfoDao? = null

    init {
        mEngine = CompositionDetailEngine(context)
        compositionInfoDao = HomeworkApp.daoSession?.compositionInfoDao
    }

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {

    }

    fun getCompositionDetailInfo(id: String, isHide: Boolean) {
        mView.showLoading()
        val subscription = mEngine.getCompositionDetail(id).subscribe(object : Subscriber<ResultInfo<NewsInfoWrapper>>() {


            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                mView.showNoNet()
            }

            override fun onNext(t: ResultInfo<NewsInfoWrapper>?) {
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.hide()
                        val info = t.data.info
                        if (!isHide) {
                            val composition = queryComposition(info.id)
                            if (composition != null) {
                                info.isCollect = 1
                            }
                        }

                        mView.showCompositionDetailInfo(info)
                    } else {
                        mView.showNoData()
                    }
                } else {
                    mView.showNoNet()
                }
            }
        })
        mSubscriptions.add(subscription)
    }

    fun collectComposition(compositionInfo: CompositionInfo?) {
        val composition = queryComposition(compositionInfo?.compostion_id)
        var isCollect = compositionInfo?.isCollect
        isCollect = if (isCollect == 1) 0 else 1

        if (composition == null) {
            compositionInfo?.saveTime = System.currentTimeMillis()
            compositionInfoDao?.insert(compositionInfo)
        } else {
            composition.isCollect = 0
            compositionInfoDao?.delete(composition)
        }
        mView.showCompositionCollectState(isCollect)

    }

    private fun queryComposition(id: String?): CompositionInfo? {
        return compositionInfoDao?.queryBuilder()?.where(CompositionInfoDao.Properties.Compostion_id.eq(id))?.unique()
    }
}