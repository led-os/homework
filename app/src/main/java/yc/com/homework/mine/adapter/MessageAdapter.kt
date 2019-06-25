package yc.com.homework.mine.adapter

import android.text.TextUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import yc.com.homework.R
import yc.com.homework.mine.domain.bean.MessageInfo
import yc.com.homework.wall.utils.UiUtils

/**
 *
 * Created by wanglin  on 2018/11/23 17:25.
 */
class MessageAdapter(data: List<MessageInfo>?) : BaseQuickAdapter<MessageInfo, BaseViewHolder>(R.layout.message_item, data) {


    override fun convert(helper: BaseViewHolder?, item: MessageInfo?) {
        helper!!.setText(R.id.tv_message_title, item!!.title)
                .setText(R.id.tv_message_desc, item.desc)
                .setText(R.id.tv_message_time, UiUtils.longToString(item.time))

        helper.setVisible(R.id.iv_unread, item.read != 1)

        if (!TextUtils.isEmpty(item.icon)) {
            Glide.with(mContext).load(item.icon).asBitmap().into(helper.getView(R.id.iv_message_icon))
        }
    }
}