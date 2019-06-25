package yc.com.homework.base.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import cn.jpush.android.api.JPushInterface
import com.kk.utils.LogUtil
import org.json.JSONException
import org.json.JSONObject
import yc.com.base.ObservableManager
import yc.com.homework.base.config.Constant
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.mine.activity.MessageActivity

/**
 *
 * Created by wanglin  on 2018/12/14 08:43.
 */
class MyPushReceiver : BroadcastReceiver() {

    private val TAG = "MyPushReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            val bundle = intent!!.extras
            val action = intent.action
            printBundle(bundle, context)
//            Log.d(TAG, "[MyReceiver] onReceive - " + action + ", extras: " + printBundle(bundle!!, context))
            when (action) {
                JPushInterface.ACTION_REGISTRATION_ID -> {
                    val regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID)
                    Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId!!)
                }
                JPushInterface.ACTION_MESSAGE_RECEIVED -> {
                    Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE)!!)
//                    processCustomMessage(context, bundle)
                }
                JPushInterface.ACTION_NOTIFICATION_RECEIVED -> {
                    Log.d(TAG, "[MyReceiver] 接收到推送下来的通知")
                    val notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID)
                    Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: $notifactionId")
                }
                JPushInterface.ACTION_NOTIFICATION_OPENED -> {
                    Log.d(TAG, "[MyReceiver] 用户点击打开了通知")
                    //打开自定义的Activity
                    val i = Intent(context, MessageActivity::class.java)
                    i.putExtras(bundle)
                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    context!!.startActivity(i)
                }
                JPushInterface.ACTION_RICHPUSH_CALLBACK -> {
                    Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA)!!)
                    //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
                }
                JPushInterface.ACTION_CONNECTION_CHANGE -> {
                    val connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false)
                    Log.w(TAG, "[MyReceiver]   $action connected state change to   $connected")
                }
                else ->
                    Log.d(TAG, "[MyReceiver] Unhandled intent - $action")

            }
        } catch (e: Exception) {

        }


    }

    // 打印所有的 intent extra 数据
    private fun printBundle(bundle: Bundle, context: Context?): String {
        val sb = StringBuilder()
        for (key in bundle.keySet()) {
            if (key == JPushInterface.EXTRA_NOTIFICATION_ID) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key))
            } else if (key == JPushInterface.EXTRA_CONNECTION_CHANGE) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key))
            } else if (key == JPushInterface.EXTRA_EXTRA) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data")
                    continue
                }
                try {
                    val json = JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA))
                    val it = json.keys()

                    while (it.hasNext()) {
                        val myKey = it.next()
                        LogUtil.msg("mkey:  $myKey")
                        if (TextUtils.equals(myKey, "type_id")) {
                            if (TextUtils.equals(json.optString(myKey), "3") || TextUtils.equals(json.optString(myKey), "6")) {
                                UserInfoHelper.getNewUserInfo(context)
                            } else if (TextUtils.equals(json.optString(myKey), "5") || TextUtils.equals(json.optString(myKey), "8")) {
                                ObservableManager.get().notifyMyObserver(Constant.EXAM_FINISH)
                            }
                        }
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]")
                    }
                } catch (e: JSONException) {
                    Log.e(TAG, "Get message extra JSON error!")
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.get(key))
            }
        }
        return sb.toString()
    }

//    //send msg to MainActivity
//    private fun processCustomMessage(context: Context, bundle: Bundle) {
//        if (MainActivity.isForeground) {
//            val message = bundle.getString(JPushInterface.EXTRA_MESSAGE)
//            val extras = bundle.getString(JPushInterface.EXTRA_EXTRA)
//            val msgIntent = Intent(MainActivity.MESSAGE_RECEIVED_ACTION)
//            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message)
//            if (!TextUtils.isEmpty(extras)) {
//                try {
//                    val extraJson = JSONObject(extras)
//                    if (extraJson.length() > 0) {
//                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras)
//                    }
//                } catch (e: JSONException) {
//
//                }
//
//            }
//            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent)
//        }
//    }
}