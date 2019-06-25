package yc.com.base;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;

import com.vondear.rxtools.RxNetTool;

/**
 * Created by wanglin  on 2018/12/29 13:55.
 */
public class NetworkManager {

    private static NetworkManager instance;
    private NetworkCheckReceiver networkCheckReceiver;

    public static NetworkManager getDefault() {
        if (instance == null) {
            synchronized (NetworkManager.class) {
                instance = new NetworkManager();
            }
        }

        return instance;

    }


    public void init(Activity activity, NetChangeListener listener) {
        inspectNet(activity);
//        networkCheckReceiver = new NetworkCheckReceiver();
//        networkCheckReceiver.setListener(listener);
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//
//        activity.registerReceiver(networkCheckReceiver, filter);

    }

    /**
     * * 初始化时判断有没有网络     
     */
    private boolean inspectNet(Activity context) {
        int netMobile = RxNetTool.getNetWorkType(context);
        showNetErrorState(netMobile, context);
        return isNetConnect(netMobile);
    }

    private boolean isShow = false;

    public void showNetErrorState(int status, Activity activity) {
        if (!isShow && (status == RxNetTool.NETWORK_NO || status == RxNetTool.NETWORK_UNKNOWN)) {
            Snackbar.make(activity.findViewById(android.R.id.content), "网络错误，请检查网络", Snackbar.LENGTH_SHORT).show();
            isShow = true;
        } else {
            isShow = false;
        }
    }

    private boolean isNetConnect(int netMobile) {
        switch (netMobile) {
            case RxNetTool.NETWORK_NO:
            case RxNetTool.NETWORK_UNKNOWN:
                return false;
            case RxNetTool.NETWORK_WIFI:
            case RxNetTool.NETWORK_2G:
            case RxNetTool.NETWORK_3G:
            case RxNetTool.NETWORK_4G:
                return true;
        }

        return false;
    }


    public void unRegisterReceiver(Context context) {
        if (networkCheckReceiver != null) {
            context.unregisterReceiver(networkCheckReceiver);
        }
    }


}
