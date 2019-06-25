package yc.com.answer.index.contract;

import yc.com.answer.index.model.bean.BookAnswerInfoWrapper;
import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2018/3/8 09:16.
 */

public interface BookContract {

    interface View extends IView, ILoading, INoData, INoNet, IHide {


        void showBookList(BookAnswerInfoWrapper data);

        void showEnd();
    }

    interface Presenter extends IPresenter {
    }
}
