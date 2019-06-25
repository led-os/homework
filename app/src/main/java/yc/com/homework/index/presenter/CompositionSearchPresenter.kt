package yc.com.homework.index.presenter

import android.content.Context

import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import rx.Subscriber
import yc.com.base.BasePresenter
import yc.com.base.UIUtils
import yc.com.homework.base.HomeworkApp
import yc.com.homework.base.dao.SearchRecordInfoDao
import yc.com.homework.base.utils.EngineUtils
import yc.com.homework.index.contract.CompostionSearchContract
import yc.com.homework.index.domain.bean.CompositionInfo
import yc.com.homework.index.domain.bean.SearchRecordInfo
import yc.com.homework.index.domain.engine.CompositionSearchEngine

/**
 *
 * Created by wanglin  on 2019/3/29 10:41.
 */
class CompositionSearchPresenter(context: Context, view: CompostionSearchContract.View) : BasePresenter<CompositionSearchEngine, CompostionSearchContract.View>(context, view), CompostionSearchContract.Presenter {
    private var searchRecordInfoDao: SearchRecordInfoDao? = null

    init {
        mEngine = CompositionSearchEngine(context)
        searchRecordInfoDao = HomeworkApp.daoSession?.searchRecordInfoDao
    }

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {
    }

    override fun getHotSearchRecords() {
        val subscription = mEngine.getHotSearchRecords().subscribe(object : Subscriber<ResultInfo<ArrayList<SearchRecordInfo>>>() {


            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
            }

            override fun onNext(t: ResultInfo<ArrayList<SearchRecordInfo>>?) {
                if (t != null && t.code == HttpConfig.STATUS_OK && t.data != null) {
                    mView.showHotSearchRecords(t.data)
                }
            }
        })
        mSubscriptions.add(subscription)

    }


    fun saveSearchRecord(name: String?) {
        var recordInfo = queryUniqueSearchRecord(name)
        val currentTime = System.currentTimeMillis()
        if (recordInfo == null) {
            recordInfo = SearchRecordInfo()
            recordInfo.name = name
            recordInfo.saveTime = currentTime
            searchRecordInfoDao?.insert(recordInfo)
        } else {
            recordInfo.saveTime = currentTime
            searchRecordInfoDao?.update(recordInfo)
        }


    }

    private fun queryUniqueSearchRecord(name: String?): SearchRecordInfo? {

        return searchRecordInfoDao?.queryBuilder()?.where(SearchRecordInfoDao.Properties.Name.eq(name))?.build()?.unique()
    }

    fun getSearchRecords() {
        val list = searchRecordInfoDao?.queryBuilder()?.orderDesc(SearchRecordInfoDao.Properties.SaveTime)?.build()?.list()
        UIUtils.post {
            mView.showRecentRecords(list)
        }
    }

    fun clearSearchRecord() {
        searchRecordInfoDao?.deleteAll()
    }

    fun compositionSearch(page: Int, pageSize: Int, title: String?, grade: String?, version: String?, unit: String?) {
        if (page == 1) {
            mView.showLoading()
        }
        val subscription = mEngine.compositionSearch(page, pageSize, title, grade, version, unit).subscribe(object : Subscriber<ResultInfo<ArrayList<CompositionInfo>>>() {

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                if (page == 1) {
                    mView.showNoNet()
                }
            }

            override fun onNext(t: ResultInfo<ArrayList<CompositionInfo>>?) {
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null && t.data.size > 0) {
                        mView.hide()
                        mView.showSearchResultInfos(t.data)
                    } else {
                        if (page == 1) {
                            mView.showNoData()
                        }
                    }
                } else {
                    if (page == 1) {
                        mView.showNoNet()
                    }
                }
            }

        })
        mSubscriptions.add(subscription)
    }

}