package cn.read.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import cn.read.BaseFragment;
import cn.read.R;
import cn.read.bean.BeautyPhotoInfo;
import cn.read.bean.WelfarePhotoList;
import cn.read.common.LoadNewsType;
import cn.read.event.ScrollToTopEvent;
import cn.read.ui.adapter.BeautyPhotoAdapter;
import cn.read.ui.adapter.WelfarePhotoAdapter;
import cn.read.ui.presenter.impl.BeautyPhotoPresenterImpl;
import cn.read.ui.presenter.impl.WelfarePhotoPresenterImpl;
import cn.read.ui.view.WelfarePhotoView;
import cn.read.utils.NetUtil;
import cn.read.utils.RxBus;
import rx.functions.Action1;

/**
 * Created by lw on 2017-03-10.
 * 福利图片
 */

public class WelfarePhotoFragment extends BaseFragment implements WelfarePhotoView,SwipeRefreshLayout.OnRefreshListener, WelfarePhotoAdapter.OnWelfarePhotoListItemClickListener {

    @Bind(R.id.welfare_photo_rv)
    RecyclerView welfarePhotoRv;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.empty_view)
    TextView emptyView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    @Inject
    WelfarePhotoAdapter mWelfarePhotoAdapter;
    @Inject
    WelfarePhotoPresenterImpl mWelfarePhotoPresenter;
    @Inject
    Activity mActivity;
    private boolean mIsAllLoaded;
    public static WelfarePhotoFragment newInstance() {
        return new WelfarePhotoFragment();
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

    private void registerScrollToTopEvent() {
        mSubscription = RxBus.getInstance().toObservable(ScrollToTopEvent.class)
                .subscribe(new Action1<ScrollToTopEvent>() {
                    @Override
                    public void call(ScrollToTopEvent scrollToTopEvent) {
                        welfarePhotoRv.getLayoutManager().scrollToPosition(0);
                    }
                });
    }

    private void initPresenter() {
        mPresenter = mWelfarePhotoPresenter;
        mPresenter.attachView(this);
        mPresenter.onCreate();
    }

    private void initRecyclerView() {
        welfarePhotoRv.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        welfarePhotoRv.setLayoutManager(staggeredGridLayoutManager);
        welfarePhotoRv.setItemAnimator(new DefaultItemAnimator());
        welfarePhotoRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    mWelfarePhotoPresenter.loadMore();
                    mWelfarePhotoAdapter.showFooter();
                    welfarePhotoRv.scrollToPosition(mWelfarePhotoAdapter.getItemCount() - 1);
                }
            }

        });

        mWelfarePhotoAdapter.setOnItemClickListener(this);
        welfarePhotoRv.setAdapter(mWelfarePhotoAdapter);
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getIntArray(R.array.gplus_colors)
        );
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_welfare_photo;
    }

    @Override
    public void setWelfarePhotoList(List<WelfarePhotoList.ResultsBean> welfarePhotoLists, @LoadNewsType.checker int loadType) {
        switch (loadType) {
            case LoadNewsType.TYPE_REFRESH_SUCCESS:
                swipeRefreshLayout.setRefreshing(false);
                mWelfarePhotoAdapter.setList(welfarePhotoLists);
                mWelfarePhotoAdapter.notifyDataSetChanged();
                checkIsEmpty(welfarePhotoLists);
                break;
            case LoadNewsType.TYPE_REFRESH_ERROR:
                swipeRefreshLayout.setRefreshing(false);
                checkIsEmpty(welfarePhotoLists);
                break;
            case LoadNewsType.TYPE_LOAD_MORE_SUCCESS:
                mWelfarePhotoAdapter.hideFooter();
                if (welfarePhotoLists == null || welfarePhotoLists.size() == 0) {
                    mIsAllLoaded = true;
                    Snackbar.make(welfarePhotoRv, getString(R.string.no_more), Snackbar.LENGTH_SHORT).show();
                } else {
                    mWelfarePhotoAdapter.addMore(welfarePhotoLists);
                }
                break;
            case LoadNewsType.TYPE_LOAD_MORE_ERROR:
                mWelfarePhotoAdapter.hideFooter();
                break;
        }
    }
    private void checkIsEmpty(List<WelfarePhotoList.ResultsBean> welfarePhotoLists) {
        if (welfarePhotoLists == null && mWelfarePhotoAdapter.getList() == null) {
            welfarePhotoRv.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            welfarePhotoRv.setVisibility(View.VISIBLE);
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
            Snackbar.make(welfarePhotoRv, message, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRefresh() {
        mWelfarePhotoPresenter.refreshData();
    }

    @OnClick(R.id.empty_view)
    public void onClick() {
        swipeRefreshLayout.setRefreshing(true);
        mWelfarePhotoPresenter.refreshData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mWelfarePhotoPresenter.onDestroy();
    }

    @Override
    public void onItemClick(View view, int position) {

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
}
