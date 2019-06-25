package yc.com.homework.base.listener;

import yc.com.homework.base.user.UserAccreditInfo;

/**
 * Created by wanglin  on 2018/11/19 15:19.
 */
public interface ThirdLoginListener {

    void onLoginResult(UserAccreditInfo userDataInfo);
}
