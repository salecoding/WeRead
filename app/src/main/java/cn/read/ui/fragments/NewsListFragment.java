package cn.read.ui.fragments;

import android.annotation.TargetApi;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import cn.read.BaseFragment;
import cn.read.R;
import cn.read.bean.NewsPhotoDetail;
import cn.read.bean.NewsSummary;
import cn.read.common.Constants;
import cn.read.common.LoadNewsType;
import cn.read.event.ScrollToTopEvent;
import cn.read.ui.activities.NewsDetailActivity;
import cn.read.ui.activities.NewsPhotoDetailActivity;
import cn.read.ui.activities.NewsPhotoSetActivity;
import cn.read.ui.adapter.NewsListAdapter;
import cn.read.ui.presenter.impl.NewsListPresenterImpl;
import cn.read.ui.view.NewsListView;
import cn.read.utils.GlideImageLoader;
import cn.read.utils.NetUtil;
import cn.read.utils.RxBus;
import rx.functions.Action1;


/**
 * Created by lw on 2017-03-06.
 * 新闻
 */

public class NewsListFragment extends BaseFragment implements NewsListView, NewsListAdapter.OnNewsListItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.news_rv)
    RecyclerView newsRv;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.empty_view)
    TextView emptyView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    @Inject
    NewsListAdapter mNewsListAdapter;
    @Inject
    NewsListPresenterImpl mNewsListPresenter;
    @Inject
    Activity mActivity;

    private String mNewsId;
    private String mNewsType;

    private boolean mIsAllLoaded;
    private Banner mBannerAds;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initValues();
        NetUtil.isNetworkErrThenShowMsg();
    }

    private void initValues() {
        if (getArguments() != null) {
            mNewsId = getArguments().getString(Constants.NEWS_ID);
            mNewsType = getArguments().getString(Constants.NEWS_TYPE);
        }
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
        mNewsListPresenter.setNewsTypeAndId(mNewsType, mNewsId);
        mPresenter = mNewsListPresenter;
        mPresenter.attachView(this);
        mPresenter.onCreate();
    }

    private void registerScrollToTopEvent() {
        mSubscription = RxBus.getInstance().toObservable(ScrollToTopEvent.class)
                .subscribe(new Action1<ScrollToTopEvent>() {
                    @Override
                    public void call(ScrollToTopEvent scrollToTopEvent) {
                        newsRv.getLayoutManager().scrollToPosition(0);
                    }
                });
    }

    private void initRecyclerView() {
        newsRv.setHasFixedSize(true);
        newsRv.setLayoutManager(new LinearLayoutManager(mActivity,
                LinearLayoutManager.VERTICAL, false));
        newsRv.setItemAnimator(new DefaultItemAnimator());
        newsRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    mNewsListPresenter.loadMore();
                    mNewsListAdapter.showFooter();
                    newsRv.scrollToPosition(mNewsListAdapter.getItemCount() - 1);
                }
            }

        });

        mNewsListAdapter.setOnItemClickListener(this);
        newsRv.setAdapter(mNewsListAdapter);
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_news_list;
    }

    @Override
    public void setNewsList(List<NewsSummary> newsSummary, @LoadNewsType.checker int loadType) {
        switch (loadType) {
            case LoadNewsType.TYPE_REFRESH_SUCCESS:
                swipeRefreshLayout.setRefreshing(false);
                mNewsListAdapter.setList(newsSummary);
                mNewsListAdapter.notifyDataSetChanged();
                checkIsEmpty(newsSummary);
                break;
            case LoadNewsType.TYPE_REFRESH_ERROR:
                swipeRefreshLayout.setRefreshing(false);
                checkIsEmpty(newsSummary);
                break;
            case LoadNewsType.TYPE_LOAD_MORE_SUCCESS:
                mNewsListAdapter.hideFooter();
                if (newsSummary == null || newsSummary.size() == 0) {
                    mIsAllLoaded = true;
                    Snackbar.make(newsRv, getString(R.string.no_more), Snackbar.LENGTH_SHORT).show();
                } else {
                    mNewsListAdapter.addMore(newsSummary);
                }
                break;
            case LoadNewsType.TYPE_LOAD_MORE_ERROR:
                mNewsListAdapter.hideFooter();
                break;
        }
    }

    @Override
    public void setNewsHead(final int position, final NewsSummary newsSummary) {
        if (newsSummary == null) return;
        View view = LayoutInflater.from(getContext()).inflate(R.layout.news_head_list, null);
        mBannerAds = (Banner) view.findViewById(R.id.banner_ads);
        List<String> images = new ArrayList();
        List<String> titles = new ArrayList();
        final List<NewsSummary.AdsBean> adsBeens = newsSummary.getAds();
        final List<NewsSummary.AdsBean> tempAdsBeans = new ArrayList<>();
        for (NewsSummary.AdsBean adsBean : adsBeens) {
            if (Constants.NEWS_ITEM_PHOTO_SET.equals(adsBean.getTag())) {
                images.add(adsBean.getImgsrc());
                titles.add(adsBean.getTitle());
            } else {
                tempAdsBeans.add(adsBean);
            }
        }
        adsBeens.removeAll(tempAdsBeans);
        mBannerAds.setImages(images)
                .setBannerTitles(titles)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                .setImageLoader(new GlideImageLoader())
                .start();
        mNewsListAdapter.addHeaderView(view);
        mBannerAds.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int i) {
                String tag = adsBeens.get(i).getTag();
                if (Constants.NEWS_ITEM_PHOTO_SET.equals(tag)) {    //图片设置
                    Intent intent = new Intent(getContext(), NewsPhotoSetActivity.class);
                    intent.putExtra(Constants.NEWS_PHOTO_SET_KEY, adsBeens.get(i).getUrl());
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.banner_right_entry, R.anim.hold);
                }
            }
        });
    }

    private void checkIsEmpty(List<NewsSummary> newsSummary) {
        if (newsSummary == null && mNewsListAdapter.getList() == null) {
            newsRv.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);

        } else {
            newsRv.setVisibility(View.VISIBLE);
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
            Snackbar.make(newsRv, message, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onRefresh() {
        mNewsListPresenter.refreshData();
    }

    @OnClick(R.id.empty_view)
    public void onClick() {
        swipeRefreshLayout.setRefreshing(true);
        mNewsListPresenter.refreshData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mNewsListPresenter.onDestroy();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onItemClick(View view, int position, boolean isPhoto) {
        if (isPhoto) {
            NewsPhotoDetail newsPhotoDetail = getPhotoDetail(position);
            goToPhotoDetailActivity(newsPhotoDetail);
        } else {
            goToNewsDetailActivity(view, position);
        }
    }

    private NewsPhotoDetail getPhotoDetail(int position) {
        NewsSummary newsSummary = mNewsListAdapter.getList().get(position);
        NewsPhotoDetail newsPhotoDetail = new NewsPhotoDetail();
        newsPhotoDetail.setTitle(newsSummary.getTitle());
        setPictures(newsSummary, newsPhotoDetail);
        return newsPhotoDetail;
    }

    private void setPictures(NewsSummary newsSummary, NewsPhotoDetail newsPhotoDetail) {
        List<NewsPhotoDetail.Picture> pictureList = new ArrayList<>();

        if (newsSummary.getAds() != null) {
            for (NewsSummary.AdsBean entity : newsSummary.getAds()) {
                setValuesAndAddToList(pictureList, entity.getTitle(), entity.getImgsrc());
            }
        } else if (newsSummary.getImgextra() != null) {
            for (NewsSummary.ImgextraBean entity : newsSummary.getImgextra()) {
                setValuesAndAddToList(pictureList, null, entity.getImgsrc());
            }
        } else {
            setValuesAndAddToList(pictureList, null, newsSummary.getImgsrc());
        }

        newsPhotoDetail.setPictures(pictureList);
    }

    private void setValuesAndAddToList(List<NewsPhotoDetail.Picture> pictureList, String title, String imgsrc) {
        NewsPhotoDetail.Picture picture = new NewsPhotoDetail.Picture();
        if (title != null) {
            picture.setTitle(title);
        }
        picture.setImgSrc(imgsrc);

        pictureList.add(picture);
    }

    private void goToPhotoDetailActivity(NewsPhotoDetail newsPhotoDetail) {
        Intent intent = new Intent(getActivity(), NewsPhotoDetailActivity.class);
        intent.putExtra(Constants.PHOTO_DETAIL, newsPhotoDetail);
        startActivity(intent);
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
}
