package yc.com.answer.index.presenter;

import android.content.Context;
import android.text.Html;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import yc.com.answer.index.contract.CollectContract;
import yc.com.answer.index.model.engine.CollectEngine;
import yc.com.answer.constant.HttpStatus;
import yc.com.answer.index.model.bean.BookAnswerInfo;
import yc.com.answer.index.model.bean.BookAnswerInfoWrapper;
import yc.com.base.BasePresenter;
import yc.com.homework.base.HomeworkApp;
import yc.com.homework.base.dao.BookAnswerInfoDao;

/**
 * Created by wanglin  on 2018/3/8 18:45.
 */

public class CollectPresenter extends BasePresenter<CollectEngine, CollectContract.View> implements CollectContract.Presenter {


    public CollectPresenter(Context context, CollectContract.View view) {
        super(context, view);
        mEngine = new CollectEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {

    }

    public void getCollectList(final int page, final int limit, boolean isRefresh) {
        if (page == 1 && !isRefresh) {
            mView.showLoading();
        }
        Subscription subscription = mEngine.getCollectList(page, limit).subscribe(new Subscriber<ResultInfo<BookAnswerInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(ResultInfo<BookAnswerInfoWrapper> bookInfoWrapperResultInfo) {
                if (bookInfoWrapperResultInfo != null) {
                    if (bookInfoWrapperResultInfo.code == HttpConfig.STATUS_OK && bookInfoWrapperResultInfo.data != null && bookInfoWrapperResultInfo.data.getLists() != null) {
                        mView.hide();
                        mView.showCollectList(bookInfoWrapperResultInfo.data);
                    } else {
                        List<BookAnswerInfo> bookInfos = queryCollectBooks(page, limit);
                        if (bookInfos != null && bookInfos.size() > 0) {
                            BookAnswerInfoWrapper wrapper = new BookAnswerInfoWrapper();
                            wrapper.setCount(bookInfos.size());
                            wrapper.setLists((ArrayList<BookAnswerInfo>) bookInfos);
                            mView.hide();
                            mView.showCollectList(wrapper);
                            return;
                        }

                        if (bookInfoWrapperResultInfo.code == HttpStatus.TOKEN_EXPIRED) {
//                            ToastUtils.showCenterToast(mContext, "请先登录");

                            mView.showTintInfo(Html.fromHtml("查看已收藏书籍，请先<font color=\"#FF0000\">登录</font>"));
                            return;
                        }
                        if (page == 1) {
                            mView.showNoData();
                        } else {
                            mView.showEnd();
                        }

                    }
                } else {
                    if (page == 1) mView.showNoNet();
                }
            }
        });

        mSubscriptions.add(subscription);

    }


    private List<BookAnswerInfo> queryCollectBooks(int page, int pageSize) {
        BookAnswerInfoDao dao = HomeworkApp.Companion.getDaoSession().getBookAnswerInfoDao();

        return dao.queryBuilder().orderDesc(BookAnswerInfoDao.Properties.SaveTime).limit(pageSize).offset((page - 1) * pageSize).build().list();

    }
}
