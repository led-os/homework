package yc.com.homework.index.widget

import android.app.Activity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import butterknife.BindView
import com.alibaba.fastjson.TypeReference
import com.chad.library.adapter.base.BaseQuickAdapter
import com.vondear.rxtools.RxSPTool

import yc.com.answer.index.model.bean.VersionDetailInfo
import yc.com.answer.index.ui.adapter.FilterItemDetailAdapter
import yc.com.base.BasePopwindow
import yc.com.base.CommonInfoHelper
import yc.com.homework.R
import yc.com.homework.base.config.SpConstant
import yc.com.homework.base.utils.ItemDecorationHelper
import yc.com.homework.index.domain.bean.VersionInfo


/**
 * Created by wanglin  on 2018/3/8 15:53.
 */

class CompositionFilterPop(context: Activity, private val mFlag: String) : BasePopwindow(context) {
    @BindView(R.id.subject_recyclerView)
    lateinit var subjectRecyclerView: RecyclerView


    private var subjectFilterItemAdapter: FilterItemDetailAdapter<String>? = null

    private var subjectDetailInfo: String? = null
    private var simple_flag = ""


    private var listener: OnPopClickListener? = null


    override fun getLayoutId(): Int {
        return R.layout.fragment_filter_view
    }

    override fun init() {

        isOutsideTouchable = true

        subjectRecyclerView.layoutManager = GridLayoutManager(mContext, 4)
        CommonInfoHelper.getO(mContext, SpConstant.composition_version, object : TypeReference<VersionInfo>() {

        }.type, object : CommonInfoHelper.onParseListener<VersionInfo> {


            override fun onParse(versionInfo: VersionInfo) {
                var subjectList: List<String>? = null
                when {
                    TextUtils.equals(mContext.getString(R.string.version), mFlag) -> {
                        subjectList = versionInfo.version
                        simple_flag = "composition_version"
                    }
                    TextUtils.equals(mContext.getString(R.string.grade), mFlag) -> {
                        subjectList = versionInfo.grade
                        simple_flag = "composition_grade"
                    }
                    TextUtils.equals(mContext.getString(R.string.unit), mFlag) -> {
                        subjectList = versionInfo.unit
                        simple_flag = "composition_unit"
                    }
                }

                if (subjectList != null)
                    subjectDetailInfo = subjectList[0]

                subjectFilterItemAdapter = FilterItemDetailAdapter(subjectList, simple_flag)

                subjectRecyclerView.adapter = subjectFilterItemAdapter

                subjectRecyclerView.addItemDecoration(ItemDecorationHelper(mContext, 10))
                subjectFilterItemAdapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
                    subjectFilterItemAdapter?.onClick(position)
                    subjectDetailInfo = subjectFilterItemAdapter?.getItem(position)
                    RxSPTool.putString(mContext, simple_flag, subjectDetailInfo)
                    listener?.onPopClick(subjectDetailInfo)

                }
            }

            override fun onFail(json: String) {

            }
        })


    }

    override fun getAnimationID(): Int {
        return 0
    }

    private fun createNewList(oldList: MutableList<VersionDetailInfo>?): List<VersionDetailInfo>? {

        if (oldList != null) {
            if (oldList.size > 0 && !TextUtils.equals(mContext.getString(R.string.all), oldList[0].name))
                oldList.add(0, VersionDetailInfo("", mContext.getString(R.string.all)))
        }
        return oldList
    }

    fun setOnPopClickListener(listener: OnPopClickListener) {
        this.listener = listener
    }

    interface OnPopClickListener {
        fun onPopClick(name: String?)
    }

}
