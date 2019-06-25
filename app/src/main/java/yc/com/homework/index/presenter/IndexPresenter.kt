package yc.com.homework.index.presenter

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import rx.Subscriber
import yc.com.base.BasePresenter
import yc.com.base.CommonInfoHelper
import yc.com.homework.base.config.SpConstant
import yc.com.homework.base.utils.EngineUtils
import yc.com.homework.index.contract.IndexContract
import yc.com.homework.index.domain.bean.BannerInfo
import yc.com.homework.index.domain.bean.IndexInfo
import yc.com.homework.index.domain.bean.NewsInfo
import yc.com.homework.index.domain.bean.ReadCountInfo
import yc.com.homework.index.domain.engine.IndexEngine

/**
 *
 * Created by wanglin  on 2019/2/15 14:32.
 */
class IndexPresenter(context: Context, view: IndexContract.View) : BasePresenter<IndexEngine, IndexContract.View>(context, view), IndexContract.Presenter {
    init {
        mEngine = IndexEngine(context)
    }

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {
        if (!isForceUI) return
        getIndexInfo()

    }

    override fun getIndexInfo() {
        CommonInfoHelper.getO(mContext, SpConstant.index_info, object : TypeReference<ResultInfo<IndexInfo>>() {}.type, object : CommonInfoHelper.onParseListener<ResultInfo<IndexInfo>> {
            override fun onParse(o: ResultInfo<IndexInfo>?) {
                if (o?.data != null) {
                    mView.showIndexInfo(o.data)

                }
            }

            override fun onFail(json: String?) {
            }
        })

        val subscription = mEngine.getIndexInfo().subscribe(object : Subscriber<ResultInfo<IndexInfo>>() {


            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                mView.showNoNet()
            }

            override fun onNext(t: ResultInfo<IndexInfo>?) {
                if (t != null && t.code == HttpConfig.STATUS_OK && t.data != null) {
                    mView.showIndexInfo(t.data)
                    CommonInfoHelper.setO(mContext, t, SpConstant.index_info)
                } else {
                    mView.showNoNet()
                }
            }


        })
        mSubscriptions.add(subscription)
    }

    fun getBannerImages(indexInfo: IndexInfo?): List<String>? {
        if (indexInfo == null) return null
        val images = indexInfo.images
        val imageList = arrayListOf<String>()
        images?.let {
            for (i in 0 until images.size) {
                imageList.add(images[i].img!!)
            }
        }
        return imageList

    }


    fun getBannerInfo(position: Int, indexInfo: IndexInfo?): BannerInfo? {
        if (indexInfo == null) return null
        val images = indexInfo.images
        var bannerInfo: BannerInfo? = null
        images?.let {
            bannerInfo = images[position]
        }
        return bannerInfo

    }


    fun getNewsInfos(page: Int, pageSize: Int,isFresh:Boolean) {
        val subscription = EngineUtils.getNewsInfos(mContext, page, pageSize).subscribe(object : Subscriber<ResultInfo<ArrayList<NewsInfo>>>() {


            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {

            }

            override fun onNext(t: ResultInfo<ArrayList<NewsInfo>>?) {
                if (t != null && t.code == HttpConfig.STATUS_OK && t.data != null) {
                    mView.showIndexRecommendInfos(t.data,isFresh)
                } else {
                    mView.showNoNet()
                }
            }
        })
        mSubscriptions.add(subscription)
    }


    fun statisticsCount(id: String?) {
        val subscription = EngineUtils.statisticsCount(mContext, id).subscribe(object : Subscriber<ResultInfo<ReadCountInfo>>() {


            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
            }

            override fun onNext(t: ResultInfo<ReadCountInfo>?) {
                if (t != null && t.code == HttpConfig.STATUS_OK) {

                }
            }
        })
        mSubscriptions.add(subscription)
    }

}