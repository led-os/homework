package yc.com.homework.base.utils

import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.Log
import android.util.SparseArray
import cn.jpush.android.api.JPushInterface
import cn.jpush.android.api.JPushMessage
import com.vondear.rxtools.RxNetTool
import java.util.*

/**
 *
 * Created by wanglin  on 2018/12/14 09:25.
 */
class JPushHelper {

    private val TAG = "JPushHelper"
    var sequence = 1//设置别名自定义的操作序列号，同操作结果一起返回，用来标识一次操作的唯一性。

    /**增加*/
    val ACTION_ADD = 1
    /**覆盖*/
    val ACTION_SET = 2
    /**删除部分*/
    val ACTION_DELETE = 3
    /**删除所有*/
    val ACTION_CLEAN = 4
    /**查询*/
    private val ACTION_GET = 5

    private val ACTION_CHECK = 6

    private val DELAY_SEND_ACTION = 1

    private val DELAY_SET_MOBILE_NUMBER_ACTION = 2

    companion object {
        private var jPushHelper: JPushHelper? = null

        fun get(): JPushHelper {
            synchronized(JPushHelper, block = {
                if (jPushHelper == null) {
                    jPushHelper = JPushHelper()
                }
            })
            return jPushHelper!!
        }
    }

    private var context: Context? = null


    private fun init(context: Context?) {
        if (context != null) {
            this.context = context.applicationContext
        }
    }

    private val setActionCache: SparseArray<Any> = SparseArray()

    fun get(sequence: Int): Any {
        return setActionCache.get(sequence)
    }

    fun remove(sequence: Int): Any {
        return setActionCache.get(sequence)
    }

    fun put(sequence: Int, tagAliasBean: Any) {
        setActionCache.put(sequence, tagAliasBean)
    }

    private val delaySendHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                DELAY_SEND_ACTION -> if (msg.obj != null && msg.obj is TagAliasBean) {
                    Log.i(TAG, "on delay time")
                    sequence++
                    val tagAliasBean = msg.obj as TagAliasBean
                    setActionCache.put(sequence, tagAliasBean)
                    if (context != null) {
                        handleAction(context, sequence, tagAliasBean)
                    } else {
                        Log.e(TAG, "#unexcepted - context was null")
                    }
                } else {
                    Log.w(TAG, "#unexcepted - msg obj was incorrect")
                }
                DELAY_SET_MOBILE_NUMBER_ACTION -> if (msg.obj != null && msg.obj is String) {
                    Log.i(TAG, "retry set mobile number")
                    sequence++
                    val mobileNumber = msg.obj as String
                    setActionCache.put(sequence, mobileNumber)
                    if (context != null) {
                        handleAction(context, sequence, mobileNumber)
                    } else {
                        Log.e(TAG, "#unexcepted - context was null")
                    }
                } else {
                    Log.w(TAG, "#unexcepted - msg obj was incorrect")
                }
            }
        }
    }

    fun handleAction(context: Context?, sequence: Int, mobileNumber: String) {
        put(sequence, mobileNumber);
        Log.d(TAG, "sequence:   $sequence,mobileNumber:    $mobileNumber")
        JPushInterface.setMobileNumber(context, sequence, mobileNumber)
    }


    /**
     * 处理设置tag
     * */
    fun handleAction(context: Context?, sequence: Int, tagAliasBean: TagAliasBean?) {
        init(context)
        if (tagAliasBean == null) {
            Log.w(TAG, "tagAliasBean was null")
            return
        }
        put(sequence, tagAliasBean)
        if (tagAliasBean.isAliasAction) {
            when (tagAliasBean.action) {
                ACTION_GET -> JPushInterface.getAlias(context, sequence)

                ACTION_DELETE -> JPushInterface.deleteAlias(context, sequence)

                ACTION_SET -> JPushInterface.setAlias(context, sequence, tagAliasBean.alias)
            }
        } else {
            when (tagAliasBean.action) {
                ACTION_ADD -> JPushInterface.addTags(context, sequence, tagAliasBean.tags)

                ACTION_SET -> JPushInterface.setTags(context, sequence, tagAliasBean.tags);
                ACTION_DELETE -> JPushInterface.deleteTags(context, sequence, tagAliasBean.tags)
                ACTION_CHECK -> {
                    //一次只能check一个tag

                    val tag = tagAliasBean.tags!!.first()
                    JPushInterface.checkTagBindState(context, sequence, tag)
                }
                ACTION_GET -> JPushInterface.getAllTags(context, sequence)
                ACTION_CLEAN -> JPushInterface.cleanTags(context, sequence)

            }
        }
    }

    private fun retryActionIfNeeded(errorCode: Int, tagAliasBean: TagAliasBean): Boolean {
        if (!RxNetTool.isConnected(context)) {
            Log.w(TAG, "no network")
            return false
        }
        //返回的错误码为6002 超时,6014 服务器繁忙,都建议延迟重试
        if (errorCode == 6002 || errorCode == 6014) {
            Log.d(TAG, "need retry")
            if (tagAliasBean != null) {
                val message = Message()
                message.what = DELAY_SEND_ACTION
                message.obj = tagAliasBean
                delaySendHandler.sendMessageDelayed(message, 1000 * 60);
                val logs = getRetryStr(tagAliasBean.isAliasAction, tagAliasBean.action, errorCode)
                ToastUtils.showCenterToast(context!!, logs)
                return true
            }
        }
        return false
    }

    private fun retrySetMobileNumberActionIfNeeded(errorCode: Int, mobileNumber: String): Boolean {
        if (!RxNetTool.isConnected(context)) {
            Log.w(TAG, "no network")
            return false
        }
        //返回的错误码为6002 超时,6024 服务器内部错误,建议稍后重试
        if (errorCode == 6002 || errorCode == 6024) {
            Log.d(TAG, "need retry")
            val message = Message()
            message.what = DELAY_SET_MOBILE_NUMBER_ACTION
            message.obj = mobileNumber
            delaySendHandler.sendMessageDelayed(message, 1000 * 60)
            var str = "Failed to set mobile number due to %s. Try again after 60s."
            str = String.format(Locale.ENGLISH, str, (if (errorCode == 6002) "timeout" else "server internal error”"))
            ToastUtils.showCenterToast(context!!, str)
            return true
        }
        return false

    }

    private fun getRetryStr(isAliasAction: Boolean, actionType: Int, errorCode: Int): String {
        var str = "Failed to %s %s due to %s. Try again after 60s."
        str = String.format(Locale.ENGLISH, str, getActionStr(actionType), (if (isAliasAction) "alias" else " tags"), (if (errorCode == 6002) "timeout" else "server too busy"))
        return str
    }

    private fun getActionStr(actionType: Int): String {
        when (actionType) {
            ACTION_ADD ->
                return "add"
            ACTION_SET ->
                return "set"
            ACTION_DELETE ->
                return "delete"
            ACTION_GET ->
                return "get"
            ACTION_CLEAN ->
                return "clean"
            ACTION_CHECK ->
                return "check"
        }
        return "unkonw operation"
    }

    fun onTagOperatorResult(context: Context, jPushMessage: JPushMessage) {
        val sequence = jPushMessage.sequence;
        Log.i(TAG, "action - onTagOperatorResult, sequence:" + sequence + ",tags:" + jPushMessage.tags)
        Log.i(TAG, "tags size:" + jPushMessage.tags.size)
        init(context)
        //根据sequence从之前操作缓存中获取缓存记录
        val tagAliasBean = setActionCache.get(sequence) as TagAliasBean
        if (tagAliasBean == null) {
            ToastUtils.showCenterToast(context, "获取缓存记录失败")
            return
        }
        if (jPushMessage.errorCode == 0) {
            Log.i(TAG, "action - modify tag Success,sequence:$sequence")
            setActionCache.remove(sequence)
            val logs = getActionStr(tagAliasBean.action) + " tags success"
            Log.i(TAG, logs)
            ToastUtils.showCenterToast(context, logs)
        } else {
            var logs = "Failed to " + getActionStr(tagAliasBean.action) + " tags"
            if (jPushMessage.errorCode == 6018) {
                //tag数量超过限制,需要先清除一部分再add
                logs += ", tags is exceed limit need to clean"
            }
            logs += ", errorCode:" + jPushMessage.errorCode
            Log.e(TAG, logs)
            if (!retryActionIfNeeded(jPushMessage.errorCode, tagAliasBean)) {
                ToastUtils.showCenterToast(context, logs)
            }
        }
    }

    fun onCheckTagOperatorResult(context: Context, jPushMessage: JPushMessage) {
        val sequence = jPushMessage.sequence
        Log.i(TAG, "action - onCheckTagOperatorResult, sequence:" + sequence + ",checktag:" + jPushMessage.checkTag)
        init(context);
        //根据sequence从之前操作缓存中获取缓存记录
        val tagAliasBean = setActionCache.get(sequence) as TagAliasBean
        if (tagAliasBean == null) {
            ToastUtils.showCenterToast(context, "获取缓存记录失败")
            return
        }
        if (jPushMessage.errorCode == 0) {
            Log.i(TAG, "tagBean:$tagAliasBean")
            setActionCache.remove(sequence)
            val logs = getActionStr(tagAliasBean.action) + " tag " + jPushMessage.checkTag + " bind state success,state:" + jPushMessage.tagCheckStateResult
            Log.i(TAG, logs)
            ToastUtils.showCenterToast(context, logs)
        } else {
            val logs = "Failed to " + getActionStr(tagAliasBean.action) + " tags, errorCode:" + jPushMessage.errorCode
            Log.e(TAG, logs)
            if (!retryActionIfNeeded(jPushMessage.errorCode, tagAliasBean)) {
                ToastUtils.showCenterToast(context, logs)
            }
        }
    }

    fun onAliasOperatorResult(context: Context, jPushMessage: JPushMessage) {
        val sequence = jPushMessage.sequence
        Log.i(TAG, "action - onAliasOperatorResult, sequence:" + sequence + ",alias:" + jPushMessage.alias)
        init(context)
        //根据sequence从之前操作缓存中获取缓存记录
        val tagAliasBean = setActionCache.get(sequence) as TagAliasBean
        if (tagAliasBean == null) {
            ToastUtils.showCenterToast(context, "获取缓存记录失败")
            return
        }
        if (jPushMessage.errorCode == 0) {
            Log.i(TAG, "action - modify alias Success,sequence:$sequence");
            setActionCache.remove(sequence)
            val logs = getActionStr(tagAliasBean.action) + " alias success"
            Log.i(TAG, logs)
            ToastUtils.showCenterToast(context, logs)
        } else {
            val logs = "Failed to " + getActionStr(tagAliasBean.action) + " alias, errorCode:" + jPushMessage.errorCode
            Log.e(TAG, logs)
            if (!retryActionIfNeeded(jPushMessage.errorCode, tagAliasBean)) {
                ToastUtils.showCenterToast(context, logs)
            }
        }
    }

    //设置手机号码回调
    fun onMobileNumberOperatorResult(context: Context, jPushMessage: JPushMessage) {
        val sequence = jPushMessage.sequence
        Log.i(TAG, "action - onMobileNumberOperatorResult, sequence:" + sequence + ",mobileNumber:" + jPushMessage.mobileNumber)
        init(context)
        if (jPushMessage.errorCode == 0) {
            Log.i(TAG, "action - set mobile number Success,sequence:$sequence")
            setActionCache.remove(sequence);
        } else {
            val logs = "Failed to set mobile number, errorCode:" + jPushMessage.errorCode
            Log.e(TAG, logs)
            if (!retrySetMobileNumberActionIfNeeded(jPushMessage.errorCode, jPushMessage.mobileNumber)) {
                ToastUtils.showCenterToast(context, logs)
            }
        }
    }

    class TagAliasBean {
        var action: Int = 0
        var tags: Set<String>? = null
        var alias: String? = null
        var isAliasAction: Boolean = false

        override fun toString(): String {
            return "[action: $action]  [tags: $tags]  [alias: $alias]  [isAliasAction  $isAliasAction]"
        }
    }
}