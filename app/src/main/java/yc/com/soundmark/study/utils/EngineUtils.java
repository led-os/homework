package yc.com.soundmark.study.utils;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.kk.utils.LogUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import yc.com.homework.base.user.UserInfoHelper;
import yc.com.homework.pay.OrderInfo;
import yc.com.homework.pay.PayInfo;
import yc.com.soundmark.base.constant.UrlConfig;
import yc.com.soundmark.base.model.domain.GoodInfoWrapper;
import yc.com.soundmark.base.model.domain.IndexDialogInfoWrapper;
import yc.com.soundmark.pay.HttpUtils;
import yc.com.soundmark.pay.PayListener;

/**
 * Created by wanglin  on 2018/11/5 16:20.
 */
public class EngineUtils {


    public static Observable<ResultInfo<OrderInfo>> createOrder(Context context, int goods_num, String payway_name, String money, String goods_id) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", UserInfoHelper.getUid());
        params.put("goods_num", goods_num + "");
        params.put("payway_name", payway_name);
        params.put("app_id", String.valueOf(7));
        params.put("money", money);
        params.put("goods_id", goods_id);
//        params.put("goods_list", JSON.toJSONString(goods_list));
        return HttpCoreEngin.get(context).rxpost(UrlConfig.pay_url, new TypeReference<ResultInfo<OrderInfo>>() {
        }.getType(), params, true, true, true);

    }



    public static Observable<ResultInfo<String>> isBindPhone(Context context) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", UserInfoHelper.getUid());

        return HttpCoreEngin.get(context).rxpost(UrlConfig.is_bind_mobile_url, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);
    }


    public static Observable<ResultInfo<GoodInfoWrapper>> getVipInfoList(Context context) {
        Map<String,String> params= new HashMap<>();
        params.put("user_id",UserInfoHelper.getUid());

        return HttpCoreEngin.get(context).rxpost(UrlConfig.vip_info_url, new TypeReference<ResultInfo<GoodInfoWrapper>>() {
        }.getType(), params, true, true, true);

    }


    public static Observable<ResultInfo<IndexDialogInfoWrapper>> getIndexMenuInfo(Context context) {

        return HttpCoreEngin.get(context).rxpost(UrlConfig.index_menu_url, new TypeReference<ResultInfo<IndexDialogInfoWrapper>>() {
        }.getType(), null, true, true, true);
    }
}
