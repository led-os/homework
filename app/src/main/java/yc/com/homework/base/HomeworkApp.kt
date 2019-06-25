package yc.com.homework.base

import android.os.Build
import android.support.multidex.MultiDexApplication
import cn.jpush.android.api.JPushInterface
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechUtility
import com.kk.securityhttp.domain.GoagalInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.kk.share.UMShareImpl
import com.umeng.analytics.MobclickAgent
import com.umeng.analytics.game.UMGameAgent
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.UMShareAPI
import com.vondear.rxtools.RxTool
import org.greenrobot.greendao.query.QueryBuilder
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import yc.com.blankj.utilcode.util.SPUtils
import yc.com.blankj.utilcode.util.Utils
import yc.com.homework.base.config.Constant
import yc.com.homework.base.config.SpConstant
import yc.com.homework.base.dao.DaoMaster
import yc.com.homework.base.dao.DaoSession
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.base.utils.ShareInfoHelper
import java.util.*


/**
 * Created by wanglin  on 2018/11/19 11:09.
 */
class HomeworkApp : MultiDexApplication() {


    override fun onCreate() {
        super.onCreate()
        app = this
        Utils.init(this)
        RxTool.init(this)
        Observable.just("").observeOn(AndroidSchedulers.mainThread()).subscribe {
            init()
            SpeechUtility.createUtility(this@HomeworkApp.applicationContext, SpeechConstant.APPID + "=5c7dcb08")
        }

//                boxStore = MyObjectBox.builder().androidContext(HomeworkApp.this).build();
//                if (BuildConfig.DEBUG) {
//                    new AndroidObjectBrowser(boxStore).start(this);
//                }

        // do this once, for example in your Application class
        val helper = DaoMaster.DevOpenHelper(this, "book-db", null)
        val db = helper.writableDatabase
        val daoMaster = DaoMaster(db)
        daoSession = daoMaster.newSession()
        QueryBuilder.LOG_SQL = true
        QueryBuilder.LOG_VALUES = true

        //        LeakCanary.install(this);
    }

    private fun init() {
        //友盟统计
        UMGameAgent.setDebugMode(false)
        UMGameAgent.init(this)
        UMGameAgent.setPlayerLevel(1)
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL)

        //初始化友盟SDK
        UMShareAPI.get(this)//初始化sd
        UMConfigure.init(this, Constant.UMENG_KEY, "umeng", UMConfigure.DEVICE_TYPE_PHONE, "")//58edcfeb310c93091c000be2 5965ee00734be40b580001a0
        //开启debug模式，方便定位错误，具体错误检查方式可以查看
        //http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        UMConfigure.setLogEnabled(Constant.DEBUG)

        val builder = UMShareImpl.Builder()

        builder.setWeixin("wx6e9dde560be26eb0", "36fa84bc419f849660b2afd361ff8a9e")
                .setQQ("1107909873", "ewXTsqVi0HdrVM9m")
                .build(this)
        //初始化极光推送配置
        //setDebugMode接口需在 init 接口之前调用，避免出现部分日志没打印的情况
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)

        //全局信息初始化
        GoagalInfo.get().init(applicationContext)
        HttpConfig.setPublickey("-----BEGIN PUBLIC KEY-----\n" +
                "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA5KaI8l7xplShIEB0Pwgm\n" +
                "MRX/3uGG9BDLPN6wbMmkkO7H1mIOXWB/Jdcl4/IMEuUDvUQyv3P+erJwZ1rvNsto\n" +
                "hXdhp2G7IqOzH6d3bj3Z6vBvsXP1ee1SgqUNrjX2dn02hMJ2Swt4ry3n3wEWusaW\n" +
                "mev4CSteSKGHhBn5j2Z5B+CBOqPzKPp2Hh23jnIH8LSbXmW0q85a851BPwmgGEan\n" +
                "5HBPq04QUjo6SQsW/7dLaaAXfUTYETe0HnpLaimcHl741ftGyrQvpkmqF93WiZZX\n" +
                "wlcDHSprf8yW0L0KA5jIwq7qBeu/H/H5vm6yVD5zvUIsD7htX0tIcXeMVAmMXFLX\n" +
                "35duvYDpTYgO+DsMgk2Q666j6OcEDVWNBDqGHc+uPvYzVF6wb3w3qbsqTnD0qb/p\n" +
                "WxpEdgK2BMVz+IPwdP6hDsDRc67LVftYqHJLKAfQt5T6uRImDizGzhhfIfJwGQxI\n" +
                "7TeJq0xWIwB+KDUbFPfTcq0RkaJ2C5cKIx08c7lYhrsPXbW+J/W4M5ZErbwcdj12\n" +
                "hrfV8TPx/RgpJcq82otrNthI3f4QdG4POUhdgSx4TvoGMTk6CnrJwALqkGl8OTfP\n" +
                "KojOucENSxcA4ERtBw4It8/X39Mk0aqa8/YBDSDDjb+gCu/Em4yYvrattNebBC1z\n" +
                "ulK9uJIXxVPi5tNd7KlwLRMCAwEAAQ==\n" +
                "-----END PUBLIC KEY-----")

        setHttpDefaultParams()

        UserInfoHelper.selectLogin(this)
        ShareInfoHelper.getShareInfos(this)
        calculateUseTimes()


    }

    private fun setHttpDefaultParams() {
        //设置http默认参数
        var agent_id = "1"
        val params = HashMap<String, String>()
        if (GoagalInfo.get().channelInfo != null && GoagalInfo.get().channelInfo.agent_id != null) {
            params["from_id"] = GoagalInfo.get().channelInfo.from_id + ""
            params["author"] = GoagalInfo.get().channelInfo.author + ""
            agent_id = GoagalInfo.get().channelInfo.agent_id
        }
        params["agent_id"] = agent_id
        params["ts"] = System.currentTimeMillis().toString() + ""
        params["device_type"] = "2"
        params["app_id"] = "1"
        params["imeil"] = GoagalInfo.get().uuid
        val sv = if (android.os.Build.MODEL.contains(android.os.Build.BRAND))
            android.os.Build.MODEL + " " + android
                    .os.Build.VERSION.RELEASE
        else
            Build.BRAND + " " + android
                    .os.Build.MODEL + " " + android.os.Build.VERSION.RELEASE
        params["sys_version"] = sv
        if (GoagalInfo.get().packageInfo != null) {
            params["app_version"] = GoagalInfo.get().packageInfo.versionCode.toString() + ""
        }
        HttpConfig.setDefaultParams(params)
    }

    companion object {

        //    private static BoxStore boxStore;

        //    public static BoxStore getBoxStore() {
        //        return boxStore;
        //    }

        private lateinit var app: HomeworkApp

        fun getApp(): HomeworkApp {
            return app
        }

        var daoSession: DaoSession? = null
            private set
    }


    private fun calculateUseTimes() {

        val saveDate = SPUtils.getInstance().getInt(SpConstant.save_date)

        val currentDate = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)

        var userCount = SPUtils.getInstance().getInt(SpConstant.teach_numbers, 0)

        if (saveDate < currentDate) {//
            SPUtils.getInstance().put(SpConstant.save_date, currentDate)
            userCount++
            SPUtils.getInstance().put(SpConstant.teach_numbers, userCount)

        }

    }


}
