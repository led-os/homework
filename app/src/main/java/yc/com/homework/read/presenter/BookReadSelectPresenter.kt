package yc.com.homework.read.presenter

import android.content.Context
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import rx.Subscriber
import yc.com.base.BasePresenter
import yc.com.homework.read.contract.BookReadSelectContract
import yc.com.homework.read.domain.bean.BookConditonInfo
import yc.com.homework.read.domain.bean.BookInfo
import yc.com.homework.read.domain.bean.GradeVersionInfo
import yc.com.homework.read.domain.engine.BookReadSelectEngine

/**
 *
 * Created by wanglin  on 2019/1/10 15:57.
 */
class BookReadSelectPresenter(context: Context, view: BookReadSelectContract.View) : BasePresenter<BookReadSelectEngine, BookReadSelectContract.View>(context, view), BookReadSelectContract.Presenter {



    init {
        mEngine = BookReadSelectEngine(context)

    }

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {
        if (!isForceUI) return
        getBookReadSelectInfo()
    }

    override fun getBookReadSelectInfo() {
        mView.showLoading()
        val subscription = mEngine.getBookReadSelectInfo().subscribe(object : Subscriber<ResultInfo<BookConditonInfo>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                mView.showNoNet()
            }

            override fun onNext(t: ResultInfo<BookConditonInfo>?) {
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.hide()
                        mView.showBookConditionInfo(t.data)
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

    override fun getVersionsByGrade(grade: String?, subject_id: String?, volumes_id: String?) {
        val subscription = mEngine.getVersionsByGrade(grade, subject_id, volumes_id).subscribe(object : Subscriber<ResultInfo<ArrayList<GradeVersionInfo>>>() {


            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {

            }

            override fun onNext(t: ResultInfo<ArrayList<GradeVersionInfo>>?) {
                if (t != null && t.code == HttpConfig.STATUS_OK && t.data != null) {
                    mView.showVersionsByGrade(t.data)
                }
            }
        })
        mSubscriptions.add(subscription)
    }

    override fun getBookInfoByGradeVersion(volumes_id: String, version_id: String, grade: String) {
        val subscription = mEngine.getBookInfoByGradeVersion(volumes_id, version_id, grade).subscribe(object : Subscriber<ResultInfo<ArrayList<BookInfo>>>() {


            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {

            }

            override fun onNext(t: ResultInfo<ArrayList<BookInfo>>?) {
                if (t != null && t.code == HttpConfig.STATUS_OK && t.data != null && t.data.isNotEmpty()) {
                    mView.showBookInfo(t.data[0])
                }
            }
        })
        mSubscriptions.add(subscription)
    }
}