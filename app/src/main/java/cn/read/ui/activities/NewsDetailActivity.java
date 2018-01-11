package cn.read.ui.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.socks.library.KLog;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import cn.read.BaseActivity;
import cn.read.R;
import cn.read.base.App;
import cn.read.bean.BeautyPhotoInfo;
import cn.read.bean.NewsDetail;
import cn.read.bean.NewsSummary;
import cn.read.common.Constants;
import cn.read.event.MessageEvent;
import cn.read.ui.presenter.impl.NewsDetailPresenterImpl;
import cn.read.ui.view.NewsDetailView;
import cn.read.utils.MyUtils;
import cn.read.utils.NetUtil;
import cn.read.utils.RxBus;
import cn.read.utils.ShareUtils;
import cn.read.utils.TransformUtils;
import cn.read.widget.URLImageGetter;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by lw on 2017-03-06.
 */

public class NewsDetailActivity extends BaseActivity implements NewsDetailView {
    @Bind(R.id.news_detail_photo_iv)
    ImageView mNewsDetailPhotoIv;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @Bind(R.id.app_bar)
    AppBarLayout mAppBar;
    @Bind(R.id.news_detail_from_tv)
    TextView mNewsDetailFromTv;
    @Bind(R.id.news_detail_body_tv)
    TextView mNewsDetailBodyTv;
    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.mask_view)
    View mMaskView;

    @Inject
    NewsDetailPresenterImpl mNewsDetailPresenter;

    private URLImageGetter mUrlImageGetter;
    private String mNewsTitle;
    private String mShareLink;
    private String mNewsBoty;
    private String mNewsImgSrc;
    private NewsSummary mNewsSummary;

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_detail;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initViews() {
        String postId = getIntent().getStringExtra(Constants.NEWS_POST_ID);
        mNewsSummary = getIntent().getParcelableExtra(Constants.NEWS_BEAN);
        mNewsDetailPresenter.setPosId(postId);
        mPresenter = mNewsDetailPresenter;
        mPresenter.attachView(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setNewsDetail(NewsDetail newsDetail) {
        mShareLink = newsDetail.getShareLink();
        mNewsTitle = newsDetail.getTitle();
        String newsSource = newsDetail.getSource();
        String newsTime = MyUtils.formatDate(newsDetail.getPtime());
        String newsBody = newsDetail.getBody();
        String newsImgSrc = getImgSrcs(newsDetail);
        mNewsBoty = newsBody;
        mNewsImgSrc = newsImgSrc;
        setToolBarLayout(mNewsTitle);
        mNewsDetailFromTv.setText(getString(R.string.news_from, newsSource, newsTime));
        setNewsDetailPhotoIv(newsImgSrc);
        setNewsDetailBodyTv(newsDetail, newsBody);
    }

    private void setToolBarLayout(String newsTitle) {
        mToolbarLayout.setTitle(newsTitle);
        mToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.white));
        mToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.primary_text_white));
    }

    private void setNewsDetailPhotoIv(String imgSrc) {
        Glide.with(this).load(imgSrc).asBitmap()
                .placeholder(R.drawable.ic_loading)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .error(R.drawable.ic_load_fail)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mNewsDetailPhotoIv);
    }

    private void setNewsDetailBodyTv(final NewsDetail newsDetail, final String newsBody) {
        mSubscription = Observable.timer(500, TimeUnit.MILLISECONDS)
                .compose(TransformUtils.<Long>defaultSchedulers())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        mProgressBar.setVisibility(View.GONE);
                        mFab.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.RollIn).playOn(mFab);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        setBody(newsDetail, newsBody);
                    }
                });
    }

    private void setBody(NewsDetail newsDetail, String newsBody) {
        int imgTotal = newsDetail.getImg().size();
        if (isShowBody(newsBody, imgTotal)) {
//              mNewsDetailBodyTv.setMovementMethod(LinkMovementMethod.getInstance());//加这句才能让里面的超链接生效,实测经常卡机崩溃
            mUrlImageGetter = new URLImageGetter(mNewsDetailBodyTv, newsBody, imgTotal);
            mNewsDetailBodyTv.setText(Html.fromHtml(newsBody, mUrlImageGetter, null));
        } else {
            mNewsDetailBodyTv.setText(Html.fromHtml(newsBody));
        }
    }

    private boolean isShowBody(String newsBody, int imgTotal) {
        return App.isHavePhoto() && imgTotal >= 2 && newsBody != null;
    }

    private String getImgSrcs(NewsDetail newsDetail) {
        List<NewsDetail.ImgBean> imgSrcs = newsDetail.getImg();
        String imgSrc;
        if (imgSrcs != null && imgSrcs.size() > 0) {
            imgSrc = imgSrcs.get(0).getSrc();
        } else {
            imgSrc = getIntent().getStringExtra(Constants.NEWS_IMG_RES);
        }
        return imgSrc;
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
//        mProgressBar.setVisibility(View.GONE);

    }

    @Override
    public void showMsg(String message) {
        mProgressBar.setVisibility(View.GONE);
        if (NetUtil.isNetworkAvailable()) {
            Snackbar.make(mAppBar, message, Snackbar.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_more, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.action_more) {
            final BottomSheetDialog dialog = new BottomSheetDialog(this);
            final View view = getLayoutInflater().inflate(R.layout.menu_more_actions_sheet, null);
            final TextView textView = (TextView) view.findViewById(R.id.textView);
            final ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            if (mNewsSummary.getIsFavorites()) {
                textView.setText(R.string.action_delete_from_bookmarks);
                imageView.setColorFilter(getResources().getColor(R.color.colorPrimary));
            }
            view.findViewById(R.id.layout_bookmark).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    addToFavorites();
                }
            });

            view.findViewById(R.id.layout_open_in_browser).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    openByBrowser();
                }
            });

            view.findViewById(R.id.layout_copy_link).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    copyLink();
                }
            });
            dialog.setContentView(view);
            dialog.show();
        }
        return true;
    }

    private void addToFavorites() {
        if (!mNewsSummary.getIsFavorites()) {
            mNewsSummary.setIsFavorites(true);
            App.getNewsSummaryDao().insertOrReplace(mNewsSummary);
            Snackbar.make(mNewsDetailPhotoIv, R.string.added_to_collection, Snackbar.LENGTH_SHORT).show();
        } else {
            mNewsSummary.setIsFavorites(false);
            App.getNewsSummaryDao().delete(mNewsSummary);
            Snackbar.make(mNewsDetailPhotoIv, R.string.deled_to_collection, Snackbar.LENGTH_SHORT).show();
        }
        RxBus.getInstance().post(new MessageEvent(Constants.BOOKMARKS_NEWS, new String[]{Constants.BOOKMARKS_NEWS_ID, Constants.BOOKMARKS_NEWS}));
    }

    private void openByBrowser() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        if (canBrowse(intent)) {
            Uri uri = Uri.parse(mShareLink);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    private boolean canBrowse(Intent intent) {
        return intent.resolveActivity(getPackageManager()) != null && mShareLink != null;
    }

    private void copyLink() {

        if (mShareLink != null) {
            ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = null;
            clipData = ClipData.newPlainText("text", Html.fromHtml(mShareLink).toString());
            manager.setPrimaryClip(clipData);
            Snackbar.make(getWindow().getDecorView(), R.string.copied_to_clipboard, Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(getWindow().getDecorView(), R.string.copied_to_clipboard_failed, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        cancelUrlImageGetterSubscription();
        super.onDestroy();

    }

    private void cancelUrlImageGetterSubscription() {
        try {
            if (mUrlImageGetter != null && mUrlImageGetter.mSubscription != null
                    && !mUrlImageGetter.mSubscription.isUnsubscribed()) {
                mUrlImageGetter.mSubscription.unsubscribe();
                KLog.d("UrlImageGetter unsubscribe");
            }
        } catch (Exception e) {
            KLog.e("取消UrlImageGetter Subscription 出现异常： " + e.toString());
        }
    }

    @OnClick(R.id.fab)
    public void onClick() {
        ShareUtils.share(mNewsTitle, mShareLink, mNewsBoty, mNewsImgSrc, null);
    }
}
