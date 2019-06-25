package yc.com.homework.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import yc.com.homework.base.utils.ToastUtils;
import yc.com.homework.pay.IPayImpl;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, IPayImpl.Companion.getAppid()); //appid需换成商户自己开放平台appid
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX && IPayImpl.Companion.getUOrderInfo() != null && IPayImpl.Companion.getUiPayCallback() != null) {
            // resp.errCode == -1 原因：支付错误,可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等
            // resp.errCode == -2 原因 用户取消,无需处理。发生场景：用户不支付了，点击取消，返回APP
            if (resp.errCode == 0) //支付成功
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.INSTANCE.showCenterToast(WXPayEntryActivity.this, "支付成功");
                    }
                });
                IPayImpl.Companion.getUOrderInfo().setMessage("支付成功");
                IPayImpl.Companion.getUiPayCallback().onSuccess(IPayImpl.Companion.getUOrderInfo());
            } else if (resp.errCode == -1) // 支付错误
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.INSTANCE.showCenterToast(WXPayEntryActivity.this, "支付错误");
                    }
                });
                IPayImpl.Companion.getUOrderInfo().setMessage("支付错误");
                IPayImpl.Companion.getUiPayCallback().onFailure(IPayImpl.Companion.getUOrderInfo());

            } else if (resp.errCode == -2) // 支付取消
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.INSTANCE.showCenterToast(WXPayEntryActivity.this, "支付取消");
                    }
                });
                IPayImpl.Companion.getUOrderInfo().setMessage("支付取消");
                IPayImpl.Companion.getUiPayCallback().onFailure(IPayImpl.Companion.getUOrderInfo());

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.INSTANCE.showCenterToast(WXPayEntryActivity.this, "支付失败");
                    }
                });
                IPayImpl.Companion.getUOrderInfo().setMessage("支付失败");
                IPayImpl.Companion.getUiPayCallback().onFailure(IPayImpl.Companion.getUOrderInfo());
            }
            IPayImpl.Companion.setUOrderInfo(null);
            IPayImpl.Companion.setUiPayCallback(null);
            finish();
        }
    }
}