package yc.com.answer.index.contract;

import yc.com.answer.index.model.bean.BookAnswerInfo;
import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;
import yc.com.homework.read.domain.bean.BookInfo;

/**
 * Created by wanglin  on 2018/3/12 10:54.
 */

public interface AnswerDetailContract {
    interface View extends IView, IHide, INoNet, INoData, ILoading {
        void showAnswerDetailInfo(BookAnswerInfo data, boolean isReload);

        void showFavoriteResult(String data, boolean isCollect);
    }

    interface Presenter extends IPresenter {
    }
}
