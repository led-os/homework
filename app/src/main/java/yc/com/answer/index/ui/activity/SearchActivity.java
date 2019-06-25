package yc.com.answer.index.ui.activity;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.RxKeyboardTool;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import yc.com.answer.index.contract.SearchContract;
import yc.com.answer.index.presenter.SearchPresenter;
import yc.com.answer.index.ui.adapter.AutoCompleteAdapter;
import yc.com.answer.index.ui.fragment.SearchFragment;
import yc.com.answer.utils.ActivityScanHelper;
import yc.com.answer.utils.ToastUtils;
import yc.com.base.BaseActivity;
import yc.com.homework.R;
import yc.com.tencent_adv.AdvDispatchManager;

/**
 * Created by wanglin  on 2018/3/9 20:08.
 */

public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchContract.View {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.et_search)
    AppCompatAutoCompleteTextView etSearch;
    @BindView(R.id.iv_scan)
    ImageView ivScan;
    @BindView(R.id.rl_container)
    RelativeLayout rlContainer;
    @BindView(R.id.btn_search)
    TextView btnSearch;
    @BindView(R.id.container)
    FrameLayout container;


    private String code;


    private boolean isFoucusable = false;
    private SearchFragment searchFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void init() {

        if (getIntent() != null) {

            code = getIntent().getStringExtra("code");
        }


        mPresenter = new SearchPresenter(this, this);

        searchFragment = new SearchFragment();
        replaceFragment(code);

//        SpanUtils.setEditTextHintSize(etSearch);


        initListener();
    }


    private void initListener() {
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        RxView.clicks(btnSearch).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String inputText = etSearch.getText().toString().trim();
                if (TextUtils.isEmpty(inputText)) {
                    ToastUtils.showCenterToast(SearchActivity.this, "请输入相关书籍名称");
                    return;
                }

                search(inputText);


            }
        });

        RxView.clicks(ivScan).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                ActivityScanHelper.startScanerCode(SearchActivity.this);
                finish();
            }
        });
        RxView.clicks(etSearch).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                etSearch.setFocusable(true);
                etSearch.setFocusableInTouchMode(true);
                etSearch.requestFocus();
                RxKeyboardTool.showSoftInput(SearchActivity.this, etSearch);
            }
        });

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                isFoucusable = hasFocus;
                if (hasFocus) {
                    mPresenter.searchTips(((AutoCompleteTextView) v).getText().toString().trim());
                }
            }
        });
    }

    private void search(String inputText) {
        etSearch.dismissDropDown();
        etSearch.setFocusable(false);
        RxKeyboardTool.hideSoftInput(SearchActivity.this);
        if (searchFragment != null) {
            searchFragment.setName(inputText);
        }

    }


    public void replaceFragment(String code) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("code", code);
        searchFragment.setArguments(bundle);

        ft.add(R.id.container, searchFragment);
        ft.commit();

    }


    private void initAutoTextView() {

        RxTextView.textChanges(etSearch)
                .debounce(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        if (isFoucusable) {
                            mPresenter.searchTips(charSequence.toString().trim());
                        }
                    }
                });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            initAutoTextView();
        }
    }



    @Override
    public void showSearchTips(List<String> data) {
        etSearch.setDropDownHorizontalOffset(-RxImageTool.dp2px(75));

//        etSearch.setDropDownWidth(RxDeviceTool.getScreenWidth(this) - RxImageTool.dip2px(btnSearch.getMeasuredWidth() + 30));
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//
//                android.R.layout.simple_dropdown_item_1line, list);

        AutoCompleteAdapter adapter = new AutoCompleteAdapter(this, data);
        etSearch.setAdapter(adapter);
        etSearch.showDropDown();
        etSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                search(etSearch.getText().toString().trim());
            }
        });

    }

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AdvDispatchManager.getManager().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
