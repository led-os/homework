package yc.com.soundmark.index.contract;

import yc.com.base.IPresenter;
import yc.com.base.IView;
import yc.com.soundmark.index.model.domain.ContactInfo;

/**
 * Created by wanglin  on 2018/10/29 09:10.
 */
public interface IndexContract {

    interface View extends IView {
        void showIndexInfo(ContactInfo contactInfo);
    }

    interface Presenter extends IPresenter {
        void getIndexInfo();
    }
}
