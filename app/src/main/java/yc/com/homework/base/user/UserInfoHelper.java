package yc.com.homework.base.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.LogUtil;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.RxTool;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import yc.com.base.ObservableManager;
import yc.com.blankj.utilcode.util.EmptyUtils;
import yc.com.blankj.utilcode.util.SPUtils;
import yc.com.blankj.utilcode.util.UIUitls;
import yc.com.homework.base.config.Constant;
import yc.com.homework.base.config.SpConstant;
import yc.com.homework.base.utils.JPushHelper;
import yc.com.homework.base.utils.EngineUtils;
import yc.com.homework.mine.activity.LoginActivity;

/**
 * Created by wanglin  on 2018/11/19 12:04.
 */
public class UserInfoHelper {
    private static UserInfo mUserInfo;

    public static UserInfo getUserInfo() {
        if (mUserInfo != null) {
            return mUserInfo;
        }
        UserInfo userInfo = null;
        try {
            String str = SPUtils.getInstance().getString(SpConstant.user_info);
            userInfo = JSON.parseObject(str, UserInfo.class);
        } catch (Exception e) {
            LogUtil.msg("json parse err-->" + e.getMessage());
        }
        mUserInfo = userInfo;

        return mUserInfo;
    }

    public static void saveUserInfo(UserInfo userInfo) {
        UserInfoHelper.mUserInfo = userInfo;
        try {
            String str = JSON.toJSONString(userInfo);
            SPUtils.getInstance().put(SpConstant.user_info, str);
        } catch (Exception e) {
            LogUtil.msg("to json err-->" + e.getMessage());
        }
    }


    public static String getUid() {
        String uid = "";
        if (null != getUserInfo()) {
            uid = getUserInfo().getId();
        }

        if (TextUtils.isEmpty(uid)) {
            uid = SPUtils.getInstance().getString(SpConstant.guest_id);
        }

        return uid;
    }


    public static boolean isGotoLogin(Activity activity) {

        if (!isLogin()) {
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
            return true;
        }


        return false;
    }

    public static boolean isLogin() {
        UserInfo userInfo = getUserInfo();
        boolean isLogin = false;
        if (userInfo != null) {
            if (userInfo.getType() == 2 || userInfo.getType() == 3 || userInfo.getType() == 4) {
                isLogin = true;
            }
        }

        return isLogin;

    }

    public interface Callback {
        void showUserInfo(UserInfo userInfo);

        void showNoLogin();
    }


    public static void getUserInfoDao(final Callback callback) {
        Observable.just("").map(new Func1<String, UserInfo>() {
            @Override
            public UserInfo call(String s) {
                return getUserInfo();
            }
        }).subscribeOn(Schedulers.io()).filter(new Func1<UserInfo, Boolean>() {
            @Override
            public Boolean call(UserInfo userInfo) {
                boolean flag = EmptyUtils.isNotEmpty(userInfo);
                if (!flag) {
                    UIUitls.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.showNoLogin();
                        }
                    });
                }
                return flag;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<UserInfo>() {
            @Override
            public void call(UserInfo userInfo) {
                callback.showUserInfo(userInfo);
            }
        });
    }


    public static void selectLogin(Context context) {
        if (getUserInfo() != null) {//登录过
            UserInfo userInfo = getUserInfo();
            if (userInfo.getType() == 1) {
                guestLogin(context);
            } else if (userInfo.getType() == 2) {
                phoneLogin(context);
            }

        } else {
            guestLogin(context);
        }
    }


    private static void guestLogin(final Context context) {
        EngineUtils.Companion.guestLogin(context).subscribe(new Subscriber<ResultInfo<UserInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<UserInfo> userInfoResultInfo) {
                if (userInfoResultInfo != null && userInfoResultInfo.code == HttpConfig.STATUS_OK && userInfoResultInfo.data != null) {
                    UserInfo userInfo = userInfoResultInfo.data;
                    LogUtil.msg("guestLogin: " + userInfo.getId());
                    saveUserInfo(userInfo);
                    SPUtils.getInstance().put(SpConstant.guest_id, userInfo.getId());
                }
            }
        });
    }

    private static void phoneLogin(final Context context) {
        UserInfo userInfo = getUserInfo();
        String pwd = SPUtils.getInstance().getString(SpConstant.pwd);
        EngineUtils.Companion.phoneLogin(context, userInfo.getTel(), pwd).subscribe(new Subscriber<ResultInfo<UserInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<UserInfo> userInfoResultInfo) {
                if (userInfoResultInfo != null && userInfoResultInfo.code == HttpConfig.STATUS_OK && userInfoResultInfo.data != null) {
                    saveUserInfo(userInfoResultInfo.data);
//                    setAlias(context);
                }
            }
        });
    }

    public static void setAlias(Context context) {
        JPushHelper.TagAliasBean tagAliasBean = new JPushHelper.TagAliasBean();
        tagAliasBean.setAliasAction(true);
        tagAliasBean.setAlias(GoagalInfo.get().uuid);
        tagAliasBean.setAction(JPushHelper.Companion.get().getACTION_SET());
        JPushHelper.Companion.get().handleAction(context, JPushHelper.Companion.get().getSequence(), tagAliasBean);
    }


    public static void getNewUserInfo(Context context) {
        EngineUtils.Companion.getUserInfo(context).subscribe(new Subscriber<ResultInfo<UserInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<UserInfo> userInfoResultInfo) {
                if (userInfoResultInfo != null && userInfoResultInfo.code == HttpConfig.STATUS_OK && userInfoResultInfo.data != null) {
                    saveUserInfo(userInfoResultInfo.data);
                    ObservableManager.get().notifyMyObserver(Constant.RECHARGE_SUCCESS);
//                    ObservableManager.get().notifyMyObserver(userInfoResultInfo.data);
                }
            }
        });
    }

    private static String mToken;

    /**
     * 保存token信息
     *
     * @param token
     */
    public static void setToken(String token) {

        try {
            RxSPTool.putString(RxTool.getContext(), yc.com.answer.constant.SpConstant.TOKEN, token);
            mToken = token;
        } catch (Exception e) {
            RxLogTool.e("error->" + e);
        }
    }

    public static String getToken() {
        if (!TextUtils.isEmpty(mToken)) {
            return mToken;
        }
        try {
            mToken = RxSPTool.getString(RxTool.getContext(), yc.com.answer.constant.SpConstant.TOKEN);
        } catch (Exception e) {
            RxLogTool.e("error->" + e);
        }
        return mToken;
    }


    public static boolean isVip() {
        if (mUserInfo != null && mUserInfo.getHas_vip() == 1) {
            return true;
        }
        return false;
    }

}
