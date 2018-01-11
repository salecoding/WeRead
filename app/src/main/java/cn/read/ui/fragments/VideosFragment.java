package cn.read.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import cn.read.BaseFragment;
import cn.read.R;
import cn.read.bean.VideosChannelTable;
import cn.read.common.Constants;
import cn.read.event.ChannelChangeEvent;
import cn.read.ui.activities.VideosChannelActivity;
import cn.read.ui.adapter.NewsFragmentPagerAdapter;
import cn.read.ui.adapter.VideosFragmentPagerAdapter;
import cn.read.ui.presenter.impl.VideosPresenterImpl;
import cn.read.ui.view.VideosView;
import cn.read.utils.MyUtils;
import cn.read.utils.RxBus;
import rx.functions.Action1;

/**
 * Created by lw on 2017-03-06.
 * 视频
 */

public class VideosFragment extends BaseFragment implements VideosView {
    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.add_channel_iv)
    ImageView addChannelIv;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    private String mCurrentViewPagerName;
    private List<String> mChannelNames;
    @Inject
    VideosPresenterImpl mVideosPresenter;

    private List<Fragment> mVideosFragmentList = new ArrayList<>();

    public static VideosFragment newInstance() {
        return new VideosFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubscription = RxBus.getInstance().toObservable(ChannelChangeEvent.class)
                .subscribe(new Action1<ChannelChangeEvent>() {
                    @Override
                    public void call(ChannelChangeEvent channelChangeEvent) {
                        mVideosPresenter.onChannelDbChanged();
                    }
                });
    }

    @Override
    public void initViews(View view) {
        mPresenter = mVideosPresenter;
        mPresenter.attachView(this);
        mPresenter.onCreate();
    }

    @OnClick(R.id.add_channel_iv)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_channel_iv:
                Intent intent = new Intent(getContext(), VideosChannelActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_videos;
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void initViewPager(List<VideosChannelTable> videosChannelTables) {
        final List<String> channelNames = new ArrayList<>();
        if (videosChannelTables != null) {
            setNewsList(videosChannelTables, channelNames);
            setViewPager(channelNames);
        }
    }

    private void setNewsList(List<VideosChannelTable> videosChannelTables, List<String> channelNames) {
        mVideosFragmentList.clear();
        for (VideosChannelTable videosChannelTable : videosChannelTables) {
            VideosListFragment videosListFragment = createListFragments(videosChannelTable);
            mVideosFragmentList.add(videosListFragment);
            channelNames.add(videosChannelTable.getVideosChannelName());
        }
    }

    private VideosListFragment createListFragments(VideosChannelTable videosChannelTable) {
        VideosListFragment fragment = new VideosListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.VIDEOS_ID, videosChannelTable.getVideosChannelId());
        bundle.putString(Constants.VIDEOS_TYPE, videosChannelTable.getVideosChannelType());
        bundle.putInt(Constants.CHANNEL_POSITION, videosChannelTable.getVideosChannelIndex());
        fragment.setArguments(bundle);
        return fragment;
    }

    private void setViewPager(List<String> channelNames) {
        VideosFragmentPagerAdapter adapter = new VideosFragmentPagerAdapter(
                getChildFragmentManager(), channelNames, mVideosFragmentList);
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
