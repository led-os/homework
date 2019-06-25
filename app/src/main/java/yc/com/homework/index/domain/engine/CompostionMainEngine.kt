package yc.com.homework.index.domain.engine

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import yc.com.base.BaseEngine
import yc.com.homework.base.config.UrlConfig
import yc.com.homework.index.domain.bean.CompositionInfo
import yc.com.homework.index.domain.bean.VersionInfo

/**
 *
 * Created by wanglin  on 2019/3/20 11:11.
 */
class CompostionMainEngine(context: Context) : BaseEngine(context) {


    fun getCompostionInfos(): Observable<ResultInfo<ArrayList<CompositionInfo>>> {
        return Observable.just("").subscribeOn(Schedulers.io()).map {
            val resultInfo = ResultInfo<ArrayList<CompositionInfo>>()
            val compostionInfos = arrayListOf<CompositionInfo>()
            val composition1 = CompositionInfo("《我的好朋友》", "人教版", "三年级", "上册", "第三单元", "她，长着蘑菇似的头发，白里透红的脸上长着一双炯炯有神的眼睛，下面是一个高挺的鼻子。再往下看，是一张能说会道的樱桃小嘴。", "开头简介人物的外貌特征，给人留下整体印象。通过爱帮助人的具体事例的描写，我们能够体会到夏优璇的善良、热情")
            composition1.compostion_id = "1"
            compostionInfos.add(composition1)
            val compositionInfo2 = CompositionInfo("《我的朋友》", "人教版", "三年级", "上册", "第七单元", "相信大家都有自己的朋友，我也不例外。我也有自己的朋友——王小伊。她有一头乌黑亮丽的头发，扎一束马尾辫，一双炯炯有神的大眼睛，鼻子小小的，下面是一张樱桃小嘴。", "这是一篇写人的文章，小作者通过王小伊把拾到的金表交给小区的保安，代为寻找失主这件事，说明王小伊是一个拾金不昧的好孩子。虽为一件小事，但作者却描写得有声有色，较好地突出了文章的主题。")
            compositionInfo2.compostion_id = "2"
            compostionInfos.add(compositionInfo2)
            val compositionInfo3 = CompositionInfo("《我的同学》", "人教版", "三年级", "上册", "第六单元", "我们班有个同学叫小红，她皮肤白白的，脸圆圆的，浓眉大眼，看上去很有精神，小红很乐于助人，所以大家都叫她“爱心大使”。", "本文中，作者通过两个典型事例表现了小红心地善良、乐于助人的性格特点，而略写了小红的外貌，这样安排使得文章有详有略，重点突出。文章结构完整，首尾呼应，语句通顺，对于二年级的孩子来说是一篇优秀的作文。")
            compositionInfo3.compostion_id = "3"
            compostionInfos.add(compositionInfo3)
            val compositionInfo4 = CompositionInfo("《我的好朋友》", "人教版", "三年级", "上册", "第五单元", "在三年级时，我认识了一个叫张哲玉的女孩，别看她的名字不太起眼，可她很可爱呢！她有一双水汪汪的大眼睛，仿佛会说话似的，充满了灵气。她有一条特别长的辫子，乌黑漂亮。她的性格开朗极了，不时会逗得人哈哈大笑。很快我们就成为了好朋友。", "由名人名言引出写作对象，引起读者的阅读兴趣，好。“她有一双水汪汪的大眼睛，仿佛会说话似的，充满了灵气。”外貌描写形神兼备，突出人物个性特征。")
            compositionInfo4.compostion_id = "4"
            compostionInfos.add(compositionInfo4)
            val compositionInfo5 = CompositionInfo("《猜猜他是谁》", "人教版", "三年级", "上册", "第八单元", "他，高高的个儿，小小的脸，淡淡的眉毛。他的眼睛小小的，整天眯着，好像没有睡醒似的。", "本篇习作先介绍人物的外貌特点，然后用两件小事描写了小男孩爱运动、有点儿聪明的特点。篇幅虽短，但叙述具体，结构清楚，值得我们借鉴。")
            compositionInfo5.compostion_id = "5"
            compostionInfos.add(compositionInfo5)
            resultInfo.data = compostionInfos

            return@map resultInfo

        }.observeOn(AndroidSchedulers.mainThread())
    }

    fun getCompostionIndexInfos(page: Int, pageSize: Int): Observable<ResultInfo<ArrayList<CompositionInfo>>> {

        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.composition_index_info, object : TypeReference<ResultInfo<ArrayList<CompositionInfo>>>() {}.type, mutableMapOf(
                "page" to "$page",
                "page_size" to "$pageSize"
        ), true, true, true) as Observable<ResultInfo<ArrayList<CompositionInfo>>>

    }

    fun getCompositionConditions(): Observable<ResultInfo<VersionInfo>> {
        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.composition_version_attr, object : TypeReference<ResultInfo<VersionInfo>>() {}.type, null,
                true, true, true) as Observable<ResultInfo<VersionInfo>>
    }
}