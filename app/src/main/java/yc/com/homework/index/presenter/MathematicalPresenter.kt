package yc.com.homework.index.presenter

import android.content.Context
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import rx.Subscriber
import yc.com.base.BasePresenter
import yc.com.homework.index.contract.MathematicalContract
import yc.com.homework.index.domain.bean.CompositionInfo
import yc.com.homework.index.domain.engine.MathematicalEngine

/**
 *
 * Created by wanglin  on 2019/4/2 10:52.
 */
class MathematicalPresenter(context: Context, view: MathematicalContract.View) :
        BasePresenter<MathematicalEngine, MathematicalContract.View>(context, view), MathematicalContract.Presenter {
    init {
        mEngine = MathematicalEngine(context)
    }

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {
        if (!isForceUI) return

    }

    fun getMathematicalList(page: Int, pageSize: Int) {
        val subscription = mEngine.getMathematicalList(page, pageSize).subscribe(object : Subscriber<ResultInfo<ArrayList<CompositionInfo>>>() {


            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
            }
            override fun onNext(t: ResultInfo<ArrayList<CompositionInfo>>?) {
                if(t!=null&&t.code==HttpConfig.STATUS_OK&&t.data!=null){
                    mView.showMathematicalList(t.data)
                }
            }
        })
        mSubscriptions.add(subscription)
    }
}