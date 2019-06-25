package yc.com.answer.index.presenter;

import android.content.Context;

import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.LogUtil;
import com.vondear.rxtools.RxNetTool;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import yc.com.answer.constant.BusAction;
import yc.com.answer.index.contract.BookConditionContract;
import yc.com.answer.index.model.bean.BookAnswerInfo;
import yc.com.answer.index.model.bean.BookAnswerInfoWrapper;
import yc.com.answer.index.model.bean.VersionDetailInfo;
import yc.com.answer.index.model.engine.BookConditionEngine;
import yc.com.answer.utils.EngineUtils;
import yc.com.answer.utils.ToastUtils;
import yc.com.base.BasePresenter;
import yc.com.base.ObservableManager;
import yc.com.homework.base.HomeworkApp;
import yc.com.homework.base.dao.BookAnswerInfoDao;
import yc.com.homework.base.dao.BookInfoDao;
import yc.com.homework.read.domain.bean.BookInfo;


/**
 * Created by wanglin  on 2018/3/12 08:58.
 */

public class BookConditionPresenter extends BasePresenter<BookConditionEngine, BookConditionContract.View> implements BookConditionContract.Presenter {

    private BookAnswerInfoDao infoDao;

    public BookConditionPresenter(Context context, BookConditionContract.View view) {
        super(context, view);
        mEngine = new BookConditionEngine(context);
        infoDao = HomeworkApp.Companion.getDaoSession().getBookAnswerInfoDao();
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {
        if (!isForceUI) return;

//        getConditionList();
    }

    public void getConditionList() {

        Subscription subscription = EngineUtils.getConditionList(mContext).subscribe(new Subscriber<ResultInfo<List<String>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<List<String>> listResultInfo) {
                if (listResultInfo != null) {
                    if (listResultInfo.code == HttpConfig.STATUS_OK && listResultInfo.data != null) {
                        mView.hide();
                        mView.showConditionList(listResultInfo.data);
                    } else {
                        mView.showNoData();
                    }

                } else {
                    mView.showNoNet();
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    private void createNewData(List<String> list) {
        if (list != null && list.size() > 0) {
            List<VersionDetailInfo> datas = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                datas.add(new VersionDetailInfo((i + 1) + "", list.get(i)));
            }

        }

    }

    public void getBookList(final int page, int limit, String name, String code, String grade_id, String grade,
                            String part_type_id, String part_type, String version_id, String version, String subject_id,
                            String subject, String flag_id, String year) {
        if (page == 1)
            mView.showLoading();
        Subscription subscription = EngineUtils.getBookInfoList(mContext, page, limit, name, code, grade_id, grade, part_type_id, part_type, version_id, version, subject_id, subject, flag_id, year, "", "").subscribe(new Subscriber<ResultInfo<BookAnswerInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (page == 1)
                    mView.showNoNet();
            }

            @Override
            public void onNext(ResultInfo<BookAnswerInfoWrapper> bookInfoWrapperResultInfo) {
                if (bookInfoWrapperResultInfo != null) {
                    if (bookInfoWrapperResultInfo.code == HttpConfig.STATUS_OK && bookInfoWrapperResultInfo.data != null && bookInfoWrapperResultInfo.data.getLists() != null) {
                        mView.hide();
//                        mView.showBookInfoList(bookInfoWrapperResultInfo.data.getLists());
                        createBookData(bookInfoWrapperResultInfo.data.getLists());
                    } else {
                        if (page == 1)
                            mView.showNoData();
                        if (bookInfoWrapperResultInfo.code == 0) {
                            mView.showHomeworkGuide();
                        }

                    }
                } else {
                    if (page == 1)
                        mView.showNoNet();
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    private void createBookData(List<BookAnswerInfo> bookInfoList) {
        if (bookInfoList == null) return;
        for (BookAnswerInfo bookInfo : bookInfoList) {

            List<BookAnswerInfo> localBooks = getLocalBooks();
            if (localBooks != null) {
                for (BookAnswerInfo localBook : localBooks) {
                    if (localBook.getBookId().equals(bookInfo.getBookId())) {
                        bookInfo.setFavorite(1);
                        break;
                    }
                }
            }
            try {
                bookInfo.setId(Long.parseLong(bookInfo.getBookId()));
            } catch (Exception e) {
                LogUtil.msg("error:  " + e.getMessage());
            }
            bookInfo.setType(BookInfo.CONTENT);
        }
        mView.showBookInfoList(bookInfoList);

    }

    public void favoriteAnswer(final BookAnswerInfo bookInfo) {
        if (!RxNetTool.isAvailable(mContext)) {
            ToastUtils.showCenterToast(mContext, "网络错误，请检查网络");
            return;
        }

        if (bookInfo == null) {
            ToastUtils.showCenterToast(mContext, "收藏的答案为空");
            return;
        }
        Subscription subscription = mEngine.favoriteAnswer(bookInfo.getBookId()).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                boolean isCollect = bookInfo.getFavorite() == 1;
                if (stringResultInfo != null) {
                    if (stringResultInfo.code == HttpConfig.STATUS_OK && stringResultInfo.data != null) {
                        isCollect = !isCollect;
                        mView.showFavoriteResult(isCollect);

                        bookInfo.setFavorite(isCollect ? 1 : 0);
//                        RxBus.get().post(BusAction.COLLECT, "list");
                        ObservableManager.get().notifyMyObserver("list");
                        ToastUtils.showCenterToast(mContext, (isCollect ? "收藏" : "取消收藏") + "成功");
                    } else {
                        ToastUtils.showCenterToast(mContext, stringResultInfo.message);
                    }

                } else {
                    ToastUtils.showCenterToast(mContext, HttpConfig.NET_ERROR);
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    private List<BookAnswerInfo> getLocalBooks() {
        return infoDao.queryBuilder().build().list();
    }

    public void saveBook(BookAnswerInfo bookInfo) {
        if (bookInfo == null) {
            ToastUtils.showCenterToast(mContext, "收藏的答案为空");
            return;
        }

        boolean isCollect;

        if (queryBook(bookInfo)) {
            //删除
            infoDao.delete(bookInfo);
            bookInfo.setFavorite(0);
            isCollect = false;
        } else {
            //保存
            bookInfo.setSaveTime(System.currentTimeMillis());
            infoDao.insert(bookInfo);
            bookInfo.setFavorite(1);
            isCollect = true;
        }
        mView.showFavoriteResult(isCollect);
//        RxBus.get().post(BusAction.COLLECT, "list");
        ObservableManager.get().notifyMyObserver("list");

    }

    private boolean queryBook(BookAnswerInfo bookInfo) {
        BookAnswerInfo result = infoDao.queryBuilder().where(BookAnswerInfoDao.Properties.BookId.eq(bookInfo.getBookId())).build().unique();
        return result != null;
    }
}
