package cn.read.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import cn.read.BaseFragment;
import cn.read.R;
import cn.read.bean.VideosSummary;
import cn.read.common.Constants;
import cn.read.common.LoadNewsType;
import cn.read.event.MessageEvent;
import cn.read.event.ScrollToTopEvent;
import cn.read.ui.adapter.VideosListAdapter;
import cn.read.ui.presenter.impl.VideosListPresenterImpl;
import cn.read.ui.view.VideosListView;
import cn.read.utils.NetUtil;
import cn.read.utils.RxBus;
import rx.functions.Action1;


/**
 * Created by lw on 2017-03-06.
 * 视频list
 */

public class VideosListFragment extends BaseFragment implements VideosListView,
        SwipeRefreshLayout.OnRefreshListener, VideosListAdapter.OnVideosListItemClickListener {
    @Bind(R.id.videos_rv)
    RecyclerView videosRv;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.empty_view)
    TextView emptyView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    @Inject
    VideosListAdapter mVideosListAdapter;
    @Inject
    VideosListPresenterImpl mVideosListPresenter;
    @Inject
    Activity mActivity;

    private String mVideosId;
    private String mVideosType;

    private boolean mIsAllLoaded;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initValues();
        NetUtil.isNetworkErrThenShowMsg();
    }

    @Override
    public void initViews(View view) {
        initSwipeRefreshLayout();
        initRecyclerView();
        initPresenter();
        registerScrollToTopEvent();
    }

    private void initValues() {
        if (getArguments() != null) {
            mVideosId = getArguments().getString(Constants.VIDEOS_ID);
            mVideosType = getArguments().getString(Constants.VIDEOS_TYPE);
        }
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getIntArray(R.array.gplus_colors)
        );
    }

    private void initPresenter() {
        mVideosListPresenter.setVideosTypeAndId(mVideosType, mVideosId);
        mPresenter = mVideosListPresenter;
        mPresenter.attachView(this);
        mPresenter.onCreate();
    }

    private void registerScrollToTopEvent() {
        mSubscription = RxBus.getInstance().toObservable(ScrollToTopEvent.class)
                .subscribe(new Action1<ScrollToTopEvent>() {
                    @Override
                    public void call(ScrollToTopEvent scrollToTopEvent) {
                        videosRv.getLayoutManager().scrollToPosition(0);
                    }
                });
    }

    private void initRecyclerView() {
        videosRv.setHasFixedSize(true);
        videosRv.setLayoutManager(new LinearLayoutManager(mActivity,
                LinearLayoutManager.VERTICAL, false));
        videosRv.setItemAnimator(new DefaultItemAnimator());
        videosRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager)
                        .findLastVisibleItemPosition();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();

                if (!mIsAllLoaded && visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItemPosition >= totalItemCount - 1) {
                    mVideosListPresenter.loadMore();
                    mVideosListAdapter.showFooter();
                    videosRv.scrollToPosition(mVideosListAdapter.getItemCount() - 1);
                }
            }

        });

        mVideosListAdapter.setOnItemClickListener(this);
        videosRv.setAdapter(mVideosListAdapter);
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_videos_list;
    }

    @Override
    public void setVideosList(List<VideosSummary> newsSummary, @LoadNewsType.checker int loadType) {
        switch (loadType) {
            case LoadNewsType.TYPE_REFRESH_SUCCESS:
                swipeRefreshLayout.setRefreshing(false);
                mVideosListAdapter.setList(newsSummary);
                mVideosListAdapter.notifyDataSetChanged();
                checkIsEmpty(newsSummary);
                break;
            case LoadNewsType.TYPE_REFRESH_ERROR:
                swipeRefreshLayout.setRefreshing(false);
                checkIsEmpty(newsSummary);
                break;
            case LoadNewsType.TYPE_LOAD_MORE_SUCCESS:
                mVideosListAdapter.hideFooter();
                if (newsSummary == null || newsSummary.size() == 0) {
                    mIsAllLoaded = true;
                    Snackbar.make(videosRv, getString(R.string.no_more), Snackbar.LENGTH_SHORT).show();
                } else {
                    mVideosListAdapter.addMore(newsSummary);
                }
                break;
            case LoadNewsType.TYPE_LOAD_MORE_ERROR:
                mVideosListAdapter.hideFooter();
                break;
        }
    }

    private void checkIsEmpty(List<VideosSummary> videosSummary) {
        if (videosSummary == null && mVideosListAdapter.getList() == null) {
            videosRv.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            videosRv.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showMsg(String message) {
        progressBar.setVisibility(View.GONE);
        // 网络不可用状态在此之前已经显示了提示信息
        if (NetUtil.isNetworkAvailable()) {
            Snackbar.make(videosRv, message, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRefresh() {
        mVideosListPresenter.refreshData();
    }

    @OnClick(R.id.empty_view)
    public void onClick() {
        swipeRefreshLayout.setRefreshing(true);
        mVideosListPresenter.refreshData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mVideosListPresenter.onDestroy();
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
