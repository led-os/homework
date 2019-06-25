package yc.com.homework.index.domain.bean

import com.alibaba.fastjson.annotation.JSONField
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.qq.e.ads.nativ.NativeExpressADView
import org.greenrobot.greendao.annotation.Transient

/**
 *
 * Created by wanglin  on 2019/3/1 16:45.
 */

class NewsInfo : MultiItemEntity {


    lateinit var id: String
    lateinit var title: String
    @JSONField(name = "type_name")
    lateinit var subTitle: String
    @JSONField(name = "pv_num")
    var readCount: Int = 0

    lateinit var img: String

    @JSONField(name = "add_time")
    var addTime: Long = 0

    lateinit var body: String
    @JSONField(name = "appraise")
    lateinit var comment: String
    lateinit var version: String
    lateinit var unit: String
    lateinit var grade: String

    var isCollect: Int = 0


    var view: NativeExpressADView? = null


    companion object {
        val ADV = 1

        val CONTENT = 2
    }


    var type: Int = CONTENT



    override fun getItemType(): Int {
        return type
    }

}