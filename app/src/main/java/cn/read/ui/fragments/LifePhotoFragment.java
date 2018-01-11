package cn.read.ui.fragments;

import android.app.Activity;
import android.content.Intent;
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
import cn.read.bean.PhotoInfo;
import cn.read.common.Constants;
import cn.read.common.LoadNewsType;
import cn.read.event.ScrollToTopEvent;
import cn.read.ui.activities.NewsPhotoSetActivity;
import cn.read.ui.adapter.LifePhotoAdapter;
import cn.read.ui.presenter.impl.LifePhotoPresenterImpl;
import cn.read.ui.view.LifePhotoView;
import cn.read.utils.NetUtil;
import cn.read.utils.RxBus;
import rx.functions.Action1;

/**
 * Created by lw on 2017-03-10.
 * 生活图片
 */

public class LifePhotoFragment extends BaseFragment implements LifePhotoView, SwipeRefreshLayout.OnRefreshListener, LifePhotoAdapter.OnLifePhotoListItemClickListener {

    @Bind(R.id.life_photo_rv)
    RecyclerView lifePhotoRv;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.empty_view)
    TextView emptyView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    @Inject
    LifePhotoAdapter mLifePhotoAdapter;
    @Inject
    LifePhotoPresenterImpl mLifePhotoPresenter;
    @Inject
    Activity mActivity;
    private boolean mIsAllLoaded;

    public static LifePhotoFragment newInstance() {
        return new LifePhotoFragment();
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
                        lifePhotoRv.getLayoutManager().scrollToPosition(0);
                    }
                });
    }

    private void initPresenter() {
        mPresenter = mLifePhotoPresenter;
        mPresenter.attachView(this);
        mPresenter.onCreate();
    }

    private void initRecyclerView() {
        lifePhotoRv.setHasFixedSize(true);
        lifePhotoRv.setLayoutManager(new LinearLayoutManager(mActivity,
                LinearLayoutManager.VERTICAL, false));
        lifePhotoRv.setItemAnimator(new DefaultItemAnimator());
        lifePhotoRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    int count = mLifePhotoAdapter.getItemCount();
                    String setId = mLifePhotoAdapter.getList().get(count - 1).getSetid();
                    mLifePhotoPresenter.setId(setId);
                    mLifePhotoPresenter.loadMore();
                    mLifePhotoAdapter.showFooter();
                    lifePhotoRv.scrollToPosition(mLifePhotoAdapter.getItemCount() - 1);
                }
            }

        });

        mLifePhotoAdapter.setOnItemClickListener(this);
        lifePhotoRv.setAdapter(mLifePhotoAdapter);
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
        return R.layout.fragment_life_photo;
    }

    @Override
    public void setLiftPhotoList(List<PhotoInfo> photoInfos, @LoadNewsType.checker int loadType) {
        switch (loadType) {
            case LoadNewsType.TYPE_REFRESH_SUCCESS:
                swipeRefreshLayout.setRefreshing(false);
                mLifePhotoAdapter.setList(photoInfos);
                mLifePhotoAdapter.notifyDataSetChanged();
                checkIsEmpty(photoInfos);
                break;
            case LoadNewsType.TYPE_REFRESH_ERROR:
                swipeRefreshLayout.setRefreshing(false);
                checkIsEmpty(photoInfos);
                break;
            case LoadNewsType.TYPE_LOAD_MORE_SUCCESS:
                mLifePhotoAdapter.hideFooter();
                if (photoInfos == null || photoInfos.size() == 0) {
                    mIsAllLoaded = true;
                    Snackbar.make(lifePhotoRv, getString(R.string.no_more), Snackbar.LENGTH_SHORT).show();
                } else {
                    mLifePhotoAdapter.addMore(photoInfos);
                }
                break;
            case LoadNewsType.TYPE_LOAD_MORE_ERROR:
                mLifePhotoAdapter.hideFooter();
                break;
        }
    }

    private void checkIsEmpty(List<PhotoInfo> photoInfos) {
        if (photoInfos == null && mLifePhotoAdapter.getList() == null) {
            lifePhotoRv.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            lifePhotoRv.setVisibility(View.VISIBLE);
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
            Snackbar.make(lifePhotoRv, message, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRefresh() {
        mLifePhotoPresenter.refreshData();
    }

    @OnClick(R.id.empty_view)
    public void onClick() {
        swipeRefreshLayout.setRefreshing(true);
        mLifePhotoPresenter.refreshData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLifePhotoPresenter.onDestroy();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getContext(), NewsPhotoSetActivity.class);
        String setId = mLifePhotoAdapter.getList().get(position).getSetid();
        intent.putExtra(Constants.NEWS_PHOTO_SET_KEY, mergePhotoId(setId));
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.banner_right_entry, R.anim.hold);
    }

    private String mergePhotoId(String setId) {
        return Constants.PREFIX_PHOTO_ID + "|" + setId;
    }
}
