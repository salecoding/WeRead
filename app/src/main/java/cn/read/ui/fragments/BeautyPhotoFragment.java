package cn.read.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import cn.read.BaseFragment;
import cn.read.R;
import cn.read.bean.BeautyPhotoInfo;
import cn.read.common.Constants;
import cn.read.common.LoadNewsType;
import cn.read.event.ScrollToTopEvent;
import cn.read.ui.activities.GrilPhotoDetailActivity;
import cn.read.ui.adapter.BeautyPhotoAdapter;
import cn.read.ui.presenter.impl.BeautyPhotoPresenterImpl;
import cn.read.ui.view.BeautyPhotoView;
import cn.read.utils.NetUtil;
import cn.read.utils.RxBus;
import rx.functions.Action1;

/**
 * Created by lw on 2017-03-10.
 * 美女图片
 */

public class BeautyPhotoFragment extends BaseFragment implements BeautyPhotoView, SwipeRefreshLayout.OnRefreshListener, BeautyPhotoAdapter.OnBeautyPhotoListItemClickListener {
    @Bind(R.id.beauty_photo_rv)
    RecyclerView beautyPhotoRv;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.empty_view)
    TextView emptyView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    @Inject
    BeautyPhotoAdapter mBeautyPhotoAdapter;
    @Inject
    BeautyPhotoPresenterImpl mBeautyPhotoPresenter;
    @Inject
    Activity mActivity;
    private boolean mIsAllLoaded;

    public static BeautyPhotoFragment newInstance() {
        return new BeautyPhotoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetUtil.isNetworkErrThenShowMsg();
    }

    @Override
    public void initViews(View view) {
        initSwipeRefreshLayout();
        initRecyclerView();
        initPresenter();
        registerScrollToTopEvent();
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getIntArray(R.array.gplus_colors)
        );
    }

    private void initPresenter() {
        mPresenter = mBeautyPhotoPresenter;
        mPresenter.attachView(this);
        mPresenter.onCreate();
    }

    private void registerScrollToTopEvent() {
        mSubscription = RxBus.getInstance().toObservable(ScrollToTopEvent.class)
                .subscribe(new Action1<ScrollToTopEvent>() {
                    @Override
                    public void call(ScrollToTopEvent scrollToTopEvent) {
                        beautyPhotoRv.getLayoutManager().scrollToPosition(0);
                    }
                });
    }

    private void initRecyclerView() {
        beautyPhotoRv.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        beautyPhotoRv.setLayoutManager(staggeredGridLayoutManager);
        beautyPhotoRv.setItemAnimator(new DefaultItemAnimator());
        beautyPhotoRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //因为StaggeredGridLayoutManager的特殊性可能导致最后显示的item存在多个，所以这里取到的是一个数组
                //得到这个数组后再取到数组中position值最大的那个就是最后显示的position值了
                int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastPositions);
                int lastVisibleItemPosition = findMax(lastPositions);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                if (!mIsAllLoaded && visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItemPosition >= totalItemCount - 1) {
                    mBeautyPhotoPresenter.loadMore();
                    mBeautyPhotoAdapter.showFooter();
                    beautyPhotoRv.scrollToPosition(mBeautyPhotoAdapter.getItemCount() - 1);
                }
            }

        });

        mBeautyPhotoAdapter.setOnItemClickListener(this);
        beautyPhotoRv.setAdapter(mBeautyPhotoAdapter);
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_beauty_photo;
    }

    @Override
    public void setBeautyPhotoList(List<BeautyPhotoInfo> beautyPhotoInfos, @LoadNewsType.checker int loadType) {
        switch (loadType) {
            case LoadNewsType.TYPE_REFRESH_SUCCESS:
                swipeRefreshLayout.setRefreshing(false);
                mBeautyPhotoAdapter.setList(beautyPhotoInfos);
                mBeautyPhotoAdapter.notifyDataSetChanged();
                checkIsEmpty(beautyPhotoInfos);
                break;
            case LoadNewsType.TYPE_REFRESH_ERROR:
                swipeRefreshLayout.setRefreshing(false);
                checkIsEmpty(beautyPhotoInfos);
                break;
            case LoadNewsType.TYPE_LOAD_MORE_SUCCESS:
                mBeautyPhotoAdapter.hideFooter();
                if (beautyPhotoInfos == null || beautyPhotoInfos.size() == 0) {
                    mIsAllLoaded = true;
                    Snackbar.make(beautyPhotoRv, getString(R.string.no_more), Snackbar.LENGTH_SHORT).show();
                } else {
                    mBeautyPhotoAdapter.addMore(beautyPhotoInfos);
                }
                break;
            case LoadNewsType.TYPE_LOAD_MORE_ERROR:
                mBeautyPhotoAdapter.hideFooter();
                break;
        }
    }

    private void checkIsEmpty(List<BeautyPhotoInfo> beautyPhotoInfos) {
        if (beautyPhotoInfos == null && mBeautyPhotoAdapter.getList() == null) {
            beautyPhotoRv.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            beautyPhotoRv.setVisibility(View.VISIBLE);
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
            Snackbar.make(beautyPhotoRv, message, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRefresh() {
        mBeautyPhotoPresenter.refreshData();
    }

    @OnClick(R.id.empty_view)
    public void onClick() {
        swipeRefreshLayout.setRefreshing(true);
        mBeautyPhotoPresenter.refreshData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBeautyPhotoPresenter.onDestroy();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getContext(), GrilPhotoDetailActivity.class);
        intent.putParcelableArrayListExtra(Constants.GIRL_PHOTO_KEY, (ArrayList<? extends Parcelable>) mBeautyPhotoAdapter.getList());
        intent.putExtra(Constants.GIRL_PHOTO_INDEX_KEY, position);
        intent.putExtra(Constants.FROM_LOVE_ACTIVITY, false);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.expand_vertical_entry, R.anim.hold);
    }
}
