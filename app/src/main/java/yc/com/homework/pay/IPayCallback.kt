package yc.com.homework.pay

/**
 * Created by zhangkai on 2017/3/17.
 */

interface IPayCallback {
    fun onSuccess(orderInfo: OrderInfo)

    fun onFailure(orderInfo: OrderInfo)
}
