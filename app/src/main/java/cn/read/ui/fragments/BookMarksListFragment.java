package cn.read.ui.fragments;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import cn.read.BaseFragment;
import cn.read.R;
import cn.read.bean.BeautyPhotoInfo;
import cn.read.bean.BookMarks;
import cn.read.bean.NewsSummary;
import cn.read.bean.VideosSummary;
import cn.read.common.Constants;
import cn.read.common.LoadNewsType;
import cn.read.event.MessageEvent;
import cn.read.event.ScrollToTopEvent;
import cn.read.ui.activities.GrilPhotoDetailActivity;
import cn.read.ui.activities.NewsDetailActivity;
import cn.read.ui.adapter.BeautyPhotoAdapter;
import cn.read.ui.adapter.NewsListAdapter;
import cn.read.ui.adapter.VideosListAdapter;
import cn.read.ui.presenter.impl.BookMarksListPresenterImpl;
import cn.read.ui.view.BookMarksListView;
import cn.read.utils.NetUtil;
import cn.read.utils.RxBus;
import rx.functions.Action1;

/**
 * Created by lw on 2017/3/21.
 */

public class BookMarksListFragment extends BaseFragment implements BookMarksListView, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.bookmarks_rv)
    RecyclerView bookmarksRv;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.empty_view)
    TextView emptyView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    @Inject
    BeautyPhotoAdapter mBeautyPhotoAdapter;
    @Inject
    NewsListAdapter mNewsListAdapter;
    @Inject
    VideosListAdapter mVideosListAdapter;
    @Inject
    BookMarksListPresenterImpl mBookMarksListPresenter;
    @Inject
    Activity mActivity;
    private String mBookMarksId;
    private String mBookMarksType;
    private boolean mIsAllLoaded;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initValues();
        NetUtil.isNetworkErrThenShowMsg();
    }

    private void initValues() {
        if (getArguments() != null) {
            mBookMarksId = getArguments().getString(Constants.BOOKMARKS_ID);
            mBookMarksType = getArguments().getString(Constants.BOOKMARKS_TYPE);
        }
    }

    @Override
    public void initViews(View view) {
        initSwipeRefreshLayout();
        initRecyclerView();
        setListener();
        initPresenter();
        registerScrollToTopEvent();
        registerMessageEvent();
    }

    private void registerMessageEvent() {
        mSubscription = RxBus.getInstance().toObservable(MessageEvent.class)
                .subscribe(new Action1<MessageEvent>() {
                    @Override
                    public void call(MessageEvent messageEvent) {
                        if (messageEvent.getTag().equals(Constants.BOOKMARKS_PHOTO)
                                || messageEvent.getTag().equals(Constants.BOOKMARKS_NEWS)
                                || messageEvent.getTag().equals(Constants.BOOKMARKS_VIDEO)) {
                            String[] msg = (String[]) messageEvent.getMessage();
                            mBookMarksId = msg[0];
                            mBookMarksType = msg[1];
                            initPresenter();
                        }
                    }
                });
    }

    private void setListener() {
        mBeautyPhotoAdapter.setOnItemClickListener(new BeautyPhotoAdapter.OnBeautyPhotoListItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), GrilPhotoDetailActivity.class);
                intent.putParcelableArrayListExtra(Constants.GIRL_PHOTO_KEY, (ArrayList<? extends Parcelable>) mBeautyPhotoAdapter.getList());
                intent.putExtra(Constants.GIRL_PHOTO_INDEX_KEY, position);
                intent.putExtra(Constants.FROM_LOVE_ACTIVITY, true);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.expand_vertical_entry, R.anim.hold);
            }
        });
        mNewsListAdapter.setOnItemClickListener(new NewsListAdapter.OnNewsListItemClickListener() {
            @Override
            public void onItemClick(View view, int position, boolean isPhoto) {
                goToNewsDetailActivity(view, position);
            }

            @Override
            public void onItemClick(View view, int position) {
                //goToNewsDetailActivity(view, position);
            }
        });
    }

    private void goToNewsDetailActivity(View view, int position) {
        Intent intent = setIntent(position);
        startActivity(view, intent);
    }

    @NonNull
    private Intent setIntent(int position) {
        List<NewsSummary> newsSummaryList = mNewsListAdapter.getList();
        Intent intent = new Intent(mActivity, NewsDetailActivity.class);
        intent.putExtra(Constants.NEWS_POST_ID, newsSummaryList.get(position).getPostid());
        intent.putExtra(Constants.NEWS_IMG_RES, newsSummaryList.get(position).getImgsrc());
        intent.putExtra(Constants.NEWS_BEAN, newsSummaryList.get(position));
        return intent;
    }

    private void startActivity(View view, Intent intent) {
        ImageView newsSummaryPhotoIv = (ImageView) view.findViewById(R.id.news_summary_photo_iv);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(mActivity, newsSummaryPhotoIv, Constants.TRANSITION_ANIMATION_NEWS_PHOTOS);
            startActivity(intent, options.toBundle());
        } else {
/*            ActivityOptionsCompat.makeCustomAnimation(this,
                    R.anim.slide_bottom_in, R.anim.slide_bottom_out);
            这个我感觉没什么用处，类似于
            overridePendingTransition(R.anim.slide_bottom_in, android.R.anim.fade_out);*/

/*            ActivityOptionsCompat.makeThumbnailScaleUpAnimation(source, thumbnail, startX, startY)
            这个方法可以用于4.x上，是将一个小块的Bitmpat进行拉伸的动画。*/

            //让新的Activity从一个小的范围扩大到全屏
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);
            ActivityCompat.startActivity(mActivity, intent, options.toBundle());
        }
    }

    private void registerScrollToTopEvent() {
        mSubscription = RxBus.getInstance().toObservable(ScrollToTopEvent.class)
                .subscribe(new Action1<ScrollToTopEvent>() {
                    @Override
                    public void call(ScrollToTopEvent scrollToTopEvent) {
                        bookmarksRv.getLayoutManager().scrollToPosition(0);
                    }
                });
    }

    private void initPresenter() {
        mBookMarksListPresenter.setBookMarksTypeAndId(mBookMarksType, mBookMarksId);
        mPresenter = mBookMarksListPresenter;
        mPresenter.attachView(this);
        mPresenter.onCreate();
    }

    private void initRecyclerView() {
        bookmarksRv.setHasFixedSize(true);
        if (Constants.BOOKMARKS_PHOTO.equals(mBookMarksType))
            bookmarksRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        else
            bookmarksRv.setLayoutManager(new LinearLayoutManager(mActivity,
                    LinearLayoutManager.VERTICAL, false));
        bookmarksRv.setItemAnimator(new DefaultItemAnimator());
        bookmarksRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int lastVisibleItemPosition = 0;
                int visibleItemCount = 0;
                int totalItemCount = 0;
                if (layoutManager instanceof LinearLayoutManager) {
                    lastVisibleItemPosition = ((LinearLayoutManager) layoutManager)
                            .findLastVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                    ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastPositions);
                    lastVisibleItemPosition = findMax(lastPositions);
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                }

                if (!mIsAllLoaded && visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItemPosition >= totalItemCount - 1) {
                    mBookMarksListPresenter.loadMore();
                    if (mBookMarksType.equals(Constants.BOOKMARKS_PHOTO)) {
                        mBeautyPhotoAdapter.showFooter();
                        bookmarksRv.scrollToPosition(mBeautyPhotoAdapter.getItemCount() - 1);
                    } else if (mBookMarksType.equals(Constants.BOOKMARKS_NEWS)) {
                        mNewsListAdapter.showFooter();
                        bookmarksRv.scrollToPosition(mNewsListAdapter.getItemCount() - 1);
                    } else {
                        mVideosListAdapter.showFooter();
                        bookmarksRv.scrollToPosition(mVideosListAdapter.getItemCount() - 1);
                    }
                }
            }

        });
        if (mBookMarksType.equals(Constants.BOOKMARKS_PHOTO)) {
            bookmarksRv.setAdapter(mBeautyPhotoAdapter);
        } else if (mBookMarksType.equals(Constants.BOOKMARKS_NEWS)) {
            bookmarksRv.setAdapter(mNewsListAdapter);
        } else {
            bookmarksRv.setAdapter(mVideosListAdapter);
        }
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
        return R.layout.fragment_bookmarks_list;
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
            Snackbar.make(bookmarksRv, message, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRefresh() {
        mBookMarksListPresenter.refreshData();
    }

    @OnClick(R.id.empty_view)
    public void onClick() {
        swipeRefreshLayout.setRefreshing(true);
        mBookMarksListPresenter.refreshData();
    }


    @Override
    public void setBookMarksesList(BookMarks bookMarks, @LoadNewsType.checker int loadType) {
        if (mBookMarksType.equals(Constants.BOOKMARKS_PHOTO)) {
            setBeautyPhoto(bookMarks, loadType);
        } else if (mBookMarksType.equals(Constants.BOOKMARKS_NEWS)) {
            setNews(bookMarks, loadType);
        } else {
            setVideo(bookMarks, loadType);
        }
    }

    private void setVideo(BookMarks bookMarks, int loadType) {
        List<VideosSummary> videosSummaries = bookMarks.getVideosSummaryListMap().get(mBookMarksType);
        switch (loadType) {
            case LoadNewsType.TYPE_REFRESH_SUCCESS:
                swipeRefreshLayout.setRefreshing(false);
                mVideosListAdapter.setList(videosSummaries);
                mVideosListAdapter.notifyDataSetChanged();
                checkVideoIsEmpty(videosSummaries);
                break;
            case LoadNewsType.TYPE_REFRESH_ERROR:
                swipeRefreshLayout.setRefreshing(false);
                checkVideoIsEmpty(videosSummaries);
                break;
            case LoadNewsType.TYPE_LOAD_MORE_SUCCESS:
                mVideosListAdapter.hideFooter();
                if (videosSummaries == null || videosSummaries.size() == 0) {
                    mIsAllLoaded = true;
                    Snackbar.make(bookmarksRv, getString(R.string.no_more), Snackbar.LENGTH_SHORT).show();
                } else {
                    mVideosListAdapter.addMore(videosSummaries);
                }
                break;
            case LoadNewsType.TYPE_LOAD_MORE_ERROR:
                mVideosListAdapter.hideFooter();
                break;
        }
    }

    private void setNews(BookMarks bookMarks, int loadType) {
        List<NewsSummary> newsSummaries = bookMarks.getNewsSummaryListMap().get(mBookMarksType);
        for (NewsSummary newsSummary : newsSummaries) {
            KLog.i(newsSummary.toString());
        }
        switch (loadType) {
            case LoadNewsType.TYPE_REFRESH_SUCCESS:
                swipeRefreshLayout.setRefreshing(false);
                mNewsListAdapter.setList(newsSummaries);
                mNewsListAdapter.notifyDataSetChanged();
                checkNewsIsEmpty(newsSummaries);
                break;
            case LoadNewsType.TYPE_REFRESH_ERROR:
                swipeRefreshLayout.setRefreshing(false);
                checkNewsIsEmpty(newsSummaries);
                break;
            case LoadNewsType.TYPE_LOAD_MORE_SUCCESS:
                mNewsListAdapter.hideFooter();
                if (newsSummaries == null || newsSummaries.size() == 0) {
                    mIsAllLoaded = true;
                    Snackbar.make(bookmarksRv, getString(R.string.no_more), Snackbar.LENGTH_SHORT).show();
                } else {
                    mNewsListAdapter.addMore(newsSummaries);
                }
                break;
            case LoadNewsType.TYPE_LOAD_MORE_ERROR:
                mNewsListAdapter.hideFooter();
                break;
        }
    }

    private void setBeautyPhoto(BookMarks bookMarks, @LoadNewsType.checker int loadType) {
        List<BeautyPhotoInfo> beautyPhotoInfos = bookMarks.getBeautyPhotoInfoListMap().get(mBookMarksType);
        switch (loadType) {
            case LoadNewsType.TYPE_REFRESH_SUCCESS:
                swipeRefreshLayout.setRefreshing(false);
                mBeautyPhotoAdapter.setList(beautyPhotoInfos);
                mBeautyPhotoAdapter.notifyDataSetChanged();
                checkBeautyPhotoIsEmpty(beautyPhotoInfos);
                break;
            case LoadNewsType.TYPE_REFRESH_ERROR:
                swipeRefreshLayout.setRefreshing(false);
                checkBeautyPhotoIsEmpty(beautyPhotoInfos);
                break;
            case LoadNewsType.TYPE_LOAD_MORE_SUCCESS:
                mBeautyPhotoAdapter.hideFooter();
                if (beautyPhotoInfos == null || beautyPhotoInfos.size() == 0) {
                    mIsAllLoaded = true;
                    Snackbar.make(bookmarksRv, getString(R.string.no_more), Snackbar.LENGTH_SHORT).show();
                } else {
                    mBeautyPhotoAdapter.addMore(beautyPhotoInfos);
                }
                break;
            case LoadNewsType.TYPE_LOAD_MORE_ERROR:
                mBeautyPhotoAdapter.hideFooter();
                break;
        }
    }

    private void checkBeautyPhotoIsEmpty(List<BeautyPhotoInfo> beautyPhotoInfos) {
        if (beautyPhotoInfos == null && mBeautyPhotoAdapter.getList() == null) {
            bookmarksRv.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            bookmarksRv.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    private void checkNewsIsEmpty(List<NewsSummary> newsSummaries) {
        if (newsSummaries == null && mNewsListAdapter.getList() == null) {
            bookmarksRv.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            bookmarksRv.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    private void checkVideoIsEmpty(List<VideosSummary> videosSummaries) {
        if (videosSummaries == null && mVideosListAdapter.getList() == null) {
            bookmarksRv.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            bookmarksRv.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBookMarksListPresenter.onDestroy();
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
