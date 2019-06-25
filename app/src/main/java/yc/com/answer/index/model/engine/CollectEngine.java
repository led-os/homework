package yc.com.answer.index.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import yc.com.answer.base.BaseEngine;
import yc.com.answer.constant.NetConstant;
import yc.com.answer.index.model.bean.BookAnswerInfoWrapper;


/**
 * Created by wanglin  on 2018/3/8 18:45.
 */

public class CollectEngine extends BaseEngine {
    public CollectEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<BookAnswerInfoWrapper>> getCollectList(int page, int limit) {

        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("limit", limit + "");

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.book_myfavorite_url, new TypeReference<ResultInfo<BookAnswerInfoWrapper>>() {
        }.getType(), params, getHeaders(), false, false, false);


    }
}
