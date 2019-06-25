package yc.com.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.sip.SipSession;
import android.util.Log;

import com.kk.utils.LogUtil;
import com.vondear.rxtools.RxNetTool;

/**
 * Created by wanglin  on 2018/12/11 19:26.
 */
public class NetworkCheckReceiver extends BroadcastReceiver {

        private NetChangeListener mListener = BaseActivity.listener;
//    private NetChangeListener mListener = null;


    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果相等的话就说明网络状态发生了变化
        Log.i("NetBroadcastReceiver", "NetBroadcastReceiver changed");

        if (intent != null) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                int netWorkState = RxNetTool.getNetWorkType(context);
                LogUtil.msg("netWorkState: " + netWorkState);
                // 当网络发生变化，判断当前网络状态，并通过NetEvent回调当前网络状态

                ObservableManager.get().notifyMyObserver(netWorkState);

                if (mListener != null) {
                    mListener.onNetChangeListener(netWorkState);
                }
            }
        }
    }

    public void setListener(NetChangeListener listener) {
        this.mListener = listener;
    }


}
