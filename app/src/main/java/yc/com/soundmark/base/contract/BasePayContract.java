package yc.com.soundmark.base.contract;

import java.util.List;

import yc.com.base.IDialog;
import yc.com.base.IPresenter;
import yc.com.base.IView;
import yc.com.homework.pay.OrderInfo;
import yc.com.soundmark.base.model.domain.GoodInfo;

/**
 * Created by wanglin  on 2018/10/30 14:58.
 */
public interface BasePayContract {

    interface View extends IView,IDialog {
        void showOrderInfo(OrderInfo orderInfo);

        void showBindSuccess();

        void showVipInfoList(List<GoodInfo> vip_list);
    }

    interface Presenter extends IPresenter {
        void createOrder(String payway_name, String money, String goods_id, String title, List<GoodInfo> list);
    }
}
