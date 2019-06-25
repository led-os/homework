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
 * Created by wanglin  on 2018/4/24 16:06.
 */

public class UploadBookListEngine extends BaseEngine {
    public UploadBookListEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<BookAnswerInfoWrapper>> getUploadBookList(int page, int limit) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("limit", limit + "");
        return HttpCoreEngin.get(mContext).rxpost(NetConstant.upload_list_url, new TypeReference<ResultInfo<BookAnswerInfoWrapper>>() {
        }.getType(), params, getHeaders(), false, false, false);
    }
}
