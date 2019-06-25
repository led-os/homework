package yc.com.homework.word.presenter

import android.content.Context

import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig

import java.util.ArrayList

import rx.Subscriber
import rx.Subscription
import yc.com.base.BasePresenter
import yc.com.homework.word.contract.AddBookContract
import yc.com.homework.word.model.domain.BookWordInfo
import yc.com.homework.word.model.domain.CourseVersionInfo
import yc.com.homework.word.model.domain.CourseVersionInfoList
import yc.com.homework.word.model.domain.GradeInfo
import yc.com.homework.word.model.domain.GradeInfoList
import yc.com.homework.word.model.engin.BookEngin

/**
 * Created by admin on 2017/8/7.
 */

class AddBookPresenter(context: Context, view: AddBookContract.View) : BasePresenter<BookEngin, AddBookContract.View>(context, view), AddBookContract.Presenter {

    init {
        mEngine = BookEngin(context)
    }

    override fun getGradeListFromLocal() {
        val gradeInfos = mEngine.getGradeListFromDB()
        mView.showGradeListData(gradeInfos)
    }

    override fun gradeList() {
        mView.showLoading()
        val subscribe = mEngine.gradeList(mContext).subscribe(object : Subscriber<ResultInfo<GradeInfoList>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                mView.showNoNet()
            }

            override fun onNext(resultInfo: ResultInfo<GradeInfoList>?) {

                if (resultInfo != null && resultInfo.code == HttpConfig.STATUS_OK && resultInfo.data != null && resultInfo.data.getList().size > 0) {
                    mView.showGradeListData(resultInfo.data.list)
                    mView.hide()
                } else {
                    mView.showNoData()
                }
            }

        })

        mSubscriptions.add(subscribe)
    }

    override fun getCVListByGradeId(gradeId: String?, partType: String?) {
        val subscribe = mEngine.getCVListByGradeId(mContext, gradeId, partType).subscribe(object : Subscriber<ResultInfo<CourseVersionInfoList>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(resultInfo: ResultInfo<CourseVersionInfoList>?) {
                if (resultInfo != null && resultInfo.code == HttpConfig.STATUS_OK && resultInfo.data != null) {

                    val cList = resultInfo.data.list as ArrayList<CourseVersionInfo>

                    for (cInfo in cList) {
                        val bookWordInfo = mEngine.queryBook(cInfo.bookId)
                        if (bookWordInfo != null) {
                            cInfo.isAdd = false
//                            break
                        }
                    }

                    mView.showCVListData(cList)
                }
            }
        })

        mSubscriptions.add(subscribe)
    }

    override fun loadData(forceUpdate: Boolean, showLoadingUI: Boolean) {

    }
}
