package yc.com.homework.wall.presenter

import android.content.Context
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import rx.Subscriber
import yc.com.base.BasePresenter
import yc.com.homework.base.utils.EngineUtils
import yc.com.homework.wall.contract.WallDetailContract
import yc.com.homework.wall.domain.bean.HomeworkDetailInfoWrapper
import yc.com.homework.wall.domain.bean.HomeworkInfo
import yc.com.homework.wall.domain.engine.WallEngine

/**
 *
 * Created by wanglin  on 2018/11/27 10:17.
 */
class WallDetailPresenter(context: Context, view: WallDetailContract.View) : BasePresenter<WallEngine, WallDetailContract.View>(context, view), WallDetailContract.Presenter {



    init {
        mEngine = WallEngine(context)
    }

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {
        if (!isForceUI) return
    }


    override fun getWalInfos(subject_id: Int, page: Int, page_size: Int, isRefresh: Boolean) {
        if (page == 1 && !isRefresh)
            mView.showLoading()
        val subscription = EngineUtils.getWalInfos(mContext,subject_id, page, page_size).subscribe(object : Subscriber<ResultInfo<ArrayList<HomeworkInfo>>>() {


            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                if (page == 1)
                    mView.showNoNet()
            }

            override fun onNext(t: ResultInfo<ArrayList<HomeworkInfo>>?) {
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.hide()
                        mView.showWallDetailInfos(t.data)

                    } else {
                        if (page == 1)
                            mView.showNoData()
                    }
                } else {
                    if (page == 1)
                        mView.showNoNet()
                }

            }
        })

        mSubscriptions.add(subscription)
    }

    override fun getWallDetailInfo(task_id: String?) {
        mView.showLoading()
        val subscription = EngineUtils.getWallDetailInfo(mContext,task_id).subscribe(object : Subscriber<ResultInfo<HomeworkDetailInfoWrapper>>() {


            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                mView.showNoNet()
            }

            override fun onNext(t: ResultInfo<HomeworkDetailInfoWrapper>?) {
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null && t.data.task_detail != null && t.data.task_detail!!.isNotEmpty()) {
                        val remark = t.data.remark
                        val homeworkDetailInfo = t.data.task_detail!![0]
                        homeworkDetailInfo.remark = remark
                        mView.showHomeworkInfo(homeworkDetailInfo)

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


    override fun getHistoryDetailInfo(task_id: String?) {
        mView.showLoading()
        val subscription = mEngine.getHistoryDetailInfo(task_id).subscribe(object : Subscriber<ResultInfo<HomeworkDetailInfoWrapper>>() {


            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                mView.showNoNet()
            }

            override fun onNext(t: ResultInfo<HomeworkDetailInfoWrapper>?) {
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null && t.data.task_detail != null && t.data.task_detail!!.isNotEmpty()) {

                        val remark = t.data.remark
                        val homeworkDetailInfo = t.data.task_detail!![0]
                        homeworkDetailInfo.remark = remark

                        mView.showHomeworkInfo(homeworkDetailInfo)
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

    override fun feedHomework(task_id: String?) {
        val subscription = mEngine.feedHomework(task_id).subscribe(object : Subscriber<ResultInfo<String>>() {


            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
            }

            override fun onNext(t: ResultInfo<String>?) {
                if (t != null && t.code == HttpConfig.STATUS_OK) {

                }
            }
        })
        mSubscriptions.add(subscription)
    }

}