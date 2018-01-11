package cn.read.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lw on 2017/1/19.
 */

public class VideosFragmentPagerAdapter extends FragmentPagerAdapter {

    private final List<String> mTitles;
    private List<Fragment> mVideosFragmentList = new ArrayList<>();

    public VideosFragmentPagerAdapter(FragmentManager fm, List<String> titles, List<Fragment> newsFragmentList) {
        super(fm);
        mTitles = titles;
        mVideosFragmentList = newsFragmentList;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mVideosFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mVideosFragmentList.size();
    }

    @Override
    public long getItemId(int position) {
        super.getItemId(position);
        if (mVideosFragmentList != null) {
            if (position < mVideosFragmentList.size()) {
                //不同的Fragment分配的HashCode不同，从而实现刷新adapter中的fragment
                return mVideosFragmentList.get(position).hashCode();
            }
        }
        return super.getItemId(position);
    }
}
