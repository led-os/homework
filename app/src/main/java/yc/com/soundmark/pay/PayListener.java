package yc.com.soundmark.pay;

import yc.com.homework.pay.OrderInfo;

/**
 * Created by wanglin  on 2018/11/26 17:25.
 */
public interface PayListener {

    void onPayResult(OrderInfo orderInfo);
}
