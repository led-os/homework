package yc.com.soundmark.study.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import yc.com.homework.R;

/**
 * Created by wanglin  on 2018/10/25 17:12.
 */
public class StudyAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private String[] mTitles;

    public StudyAdapter(Context context, FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragments = fragmentList;
        mTitles = context.getResources().getStringArray(R.array.study_category);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return mTitles[position];
    }
}
