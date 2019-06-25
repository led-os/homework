package yc.com.homework.index.domain.engine

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import yc.com.base.BaseEngine
import yc.com.blankj.utilcode.util.SPUtils
import yc.com.homework.base.config.SpConstant
import yc.com.homework.base.config.UrlConfig
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.index.domain.bean.IndexInfo
import yc.com.homework.index.domain.bean.NewsInfo
import yc.com.homework.index.utils.StarlightSummarizing

/**
 *
 * Created by wanglin  on 2019/2/15 14:35.
 */
class IndexEngine(context: Context) : BaseEngine(context) {

    val getIndexInfo: Observable<IndexInfo>
        get() = Observable.just(UrlConfig.index_info).subscribeOn(Schedulers.io()).map {
            val indexInfo = IndexInfo()
            //添加数据逻辑
            val images = arrayListOf<String>()
            images.add("http://zyl.wk2.com/uploads/20181225/a8d8b253143590b9c1cc1407863ee322.png")
            images.add("http://zyl.wk2.com/uploads/20181225/391f4e74c015667061abf7065f7c3aad.png")

//            indexInfo.images = images
            indexInfo.today_starlight = getRandomStarlight()
            indexInfo.teach_numbers = SPUtils.getInstance().getInt(SpConstant.teach_numbers)

            indexInfo


        }.observeOn(AndroidSchedulers.mainThread())


    fun getIndexInfo(): Observable<ResultInfo<IndexInfo>> {

        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.index_info, object : TypeReference<ResultInfo<IndexInfo>>() {}.type, mutableMapOf(
                "user_id" to UserInfoHelper.getUid()), true, true, true) as Observable<ResultInfo<IndexInfo>>
    }

    private fun getRandomStarlight(): String {

        val index = (Math.random() * StarlightSummarizing.starlightInfos.size).toInt()

        return StarlightSummarizing.starlightInfos[index]
    }

    val getRecommendInfos: Observable<ArrayList<NewsInfo>>
        get() = Observable.just("").subscribeOn(Schedulers.io()).map {
            val mDatas = arrayListOf<NewsInfo>()
            val newsInfo = NewsInfo()
            newsInfo.readCount = 2345
            newsInfo.title = "数学成绩好的孩子都有这3个习惯，家长一定要告诉他"
            newsInfo.subTitle = "学习有方法"
//            newsInfo.iconId = R.mipmap.news_exam_icon
            mDatas.add(newsInfo)

            val newsInfo1 = NewsInfo()
            newsInfo1.readCount =3421
            newsInfo1.title = "语文学习有妙招，快来看看吧"
            newsInfo1.subTitle = "学习有方法"
//            newsInfo1.iconId = R.mipmap.news_exam_icon
            mDatas.add(newsInfo1)

            val newsInfo3 = NewsInfo()
            newsInfo3.readCount = 67922
            newsInfo3.title = "数学成绩好的孩子都有这3个习惯，家长一定要告诉他"
            newsInfo3.subTitle = "学习有方法"
//            newsInfo3.iconId = R.mipmap.news_exam_icon
            mDatas.add(newsInfo3)
            val newsInfo4 = NewsInfo()
            newsInfo4.readCount = 234922
            newsInfo4.title = "数学成绩好的孩子都有这3个习惯，家长一定要告诉他"
            newsInfo4.subTitle = "学习有方法"
//            newsInfo3.iconId = R.mipmap.news_exam_icon
            mDatas.add(newsInfo4)


            mDatas
        }.observeOn(AndroidSchedulers.mainThread())


}