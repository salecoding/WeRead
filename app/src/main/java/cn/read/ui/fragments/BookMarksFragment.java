package cn.read.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.read.BaseFragment;
import cn.read.R;
import cn.read.base.App;
import cn.read.bean.BookMarks;
import cn.read.bean.NewsChannelTable;
import cn.read.common.Constants;
import cn.read.ui.adapter.BookMarksFragmentPagerAdapter;
import cn.read.ui.adapter.PhotosFragmentPagerAdapter;
import cn.read.ui.presenter.impl.BookMarksPresenterImpl;
import cn.read.ui.presenter.impl.PhotosPresenterImpl;
import cn.read.ui.view.BookMarksView;
import cn.read.utils.MyUtils;

/**
 * Created by lw on 2017/3/6.
 * 收藏
 */

public class BookMarksFragment extends BaseFragment implements BookMarksView {

    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    private String mCurrentViewPagerName;
    private List<String> mChannelNames;
    @Inject
    BookMarksPresenterImpl mBookMarksPresenter;

    private List<Fragment> mBookMarksFragmentList = new ArrayList<>();

    public static BookMarksFragment newInstance() {
        return new BookMarksFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initViews(View view) {
        mPresenter = mBookMarksPresenter;
        mPresenter.attachView(this);
        mPresenter.onCreate();
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bookmarks;
    }

    @Override
    public void initViewPager(List<String> bookMarksChannelNames) {
        final List<String> channelNames = new ArrayList<>();
        if (bookMarksChannelNames != null) {
            setBookMarksList(bookMarksChannelNames, channelNames);
            setViewPager(channelNames);
        }
    }

    private void setBookMarksList(List<String> bookMarksChannelNames, List<String> channelNames) {
        mBookMarksFragmentList.clear();
        for (String channelName : bookMarksChannelNames) {
            BookMarksListFragment bookMarksListFragment = createListFragments(channelName);
            mBookMarksFragmentList.add(bookMarksListFragment);
            channelNames.add(channelName);
        }
    }

    private BookMarksListFragment createListFragments(String channelName) {
        BookMarksListFragment fragment = new BookMarksListFragment();
        Bundle bundle = new Bundle();
        String bookMarksId = getBookMarksInfo(channelName)[0];
        String bookMarksType = getBookMarksInfo(channelName)[1];
        bundle.putString(Constants.BOOKMARKS_ID, bookMarksId);
        bundle.putString(Constants.BOOKMARKS_TYPE, bookMarksType);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 获取id 和 type
     *
     * @param channelName
     * @return
     */
    private String[] getBookMarksInfo(String channelName) {
        //用于存储bookmarks id 和 type
        String[] bookMarksInfo = new String[2];
        List<String> channelNames = Arrays.asList(App.getContext().getResources()
                .getStringArray(R.array.bookmarks_channel_name));
        List<String> channelIds = Arrays.asList(App.getContext().getResources()
                .getStringArray(R.array.bookmarks_channel_id));
        List<String> channelTypes = Arrays.asList(App.getContext().getResources()
                .getStringArray(R.array.bookmarks_channel_type));
        for (int i = 0; i < channelNames.size(); i++) {
            if (channelNames.get(i).equals(channelName)) {
                bookMarksInfo[0] = channelIds.get(i);
                bookMarksInfo[1] = channelTypes.get(i);
                break;
            }

        }
        return bookMarksInfo;
    }

    private void setViewPager(List<String> channelNames) {
        BookMarksFragmentPagerAdapter adapter = new BookMarksFragmentPagerAdapter(
                getChildFragmentManager(), channelNames, mBookMarksFragmentList);
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
        MyUtils.dynamicSetTabLayoutMode(tabs);
//        mTabs.setTabsFromPagerAdapter(adapter);
        setPageChangeListener();

        mChannelNames = channelNames;
        int currentViewPagerPosition = getCurrentViewPagerPosition();
        viewPager.setCurrentItem(currentViewPagerPosition, false);
    }

    private int getCurrentViewPagerPosition() {
        int position = 0;
        if (mCurrentViewPagerName != null) {
            for (int i = 0; i < mChannelNames.size(); i++) {
                if (mCurrentViewPagerName.equals(mChannelNames.get(i))) {
                    position = i;
                }
            }
        }
        return position;
    }

    private void setPageChangeListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentViewPagerName = mChannelNames.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String message) {

    }
}
