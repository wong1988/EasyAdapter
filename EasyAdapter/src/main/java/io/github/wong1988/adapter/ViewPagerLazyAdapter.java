package io.github.wong1988.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPager对应的懒加载方案，可以在Fragment执行onResume时执行网络请求
 */
public class ViewPagerLazyAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragments;
    private final List<String> mTitles;

    public ViewPagerLazyAdapter(@NonNull FragmentManager fm, List<Fragment> fragments) {
        this(fm, fragments, new ArrayList<>());
    }

    public ViewPagerLazyAdapter(@NonNull FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mFragments = fragments;
        mTitles = titles;
    }

    @NonNull
    @Override
    public final Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public final int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    @Nullable
    @Override
    public final CharSequence getPageTitle(int position) {
        String pageTitle = "";

        if (mTitles != null && mTitles.size() == mFragments.size())
            pageTitle = mTitles.get(position);

        return pageTitle;
    }
}
