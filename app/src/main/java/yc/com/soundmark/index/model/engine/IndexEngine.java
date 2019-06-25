package yc.com.soundmark.index.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;

import rx.Observable;
import yc.com.base.BaseEngine;
import yc.com.soundmark.base.constant.UrlConfig;
import yc.com.soundmark.index.model.domain.IndexInfoWrapper;

/**
 * Created by wanglin  on 2018/10/29 08:50.
 */
public class IndexEngine extends BaseEngine {
    public IndexEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<IndexInfoWrapper>> getIndexInfo() {

        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.contact_info, new TypeReference<ResultInfo<IndexInfoWrapper>>() {
        }.getType(), null, true, true, true);

    }


}
