package cn.read.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dl7.drag.DragSlopLayout;
import com.jakewharton.rxbinding.view.RxView;
import com.socks.library.KLog;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import cn.read.BaseActivity;
import cn.read.R;
import cn.read.base.App;
import cn.read.base.RetrofitManager;
import cn.read.bean.BeautyPhotoInfo;
import cn.read.common.Constants;
import cn.read.common.HostType;
import cn.read.common.LoadNewsType;
import cn.read.event.MessageEvent;
import cn.read.ui.adapter.GrilPhotoPagerAdapter;
import cn.read.ui.presenter.impl.GrilPhotoDetailPresenterImpl;
import cn.read.ui.view.GrilPhotoDetailView;
import cn.read.utils.FileUtils;
import cn.read.utils.MyUtils;
import cn.read.utils.NetUtil;
import cn.read.utils.RxBus;
import cn.read.utils.ShareUtils;
import cn.read.utils.SharedPreferencesUtil;
import cn.read.utils.TransformUtils;
import cn.read.widget.AnimateHelper;
import cn.read.widget.PhotoViewPager;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by lw on 2017/3/10.
 */

public class GrilPhotoDetailActivity extends BaseActivity implements GrilPhotoDetailView {
    @Bind(R.id.vp_photo)
    PhotoViewPager vpPhoto;
    @Bind(R.id.ll_layout)
    LinearLayout llLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.drag_layout)
    DragSlopLayout dragLayout;
    @Bind(R.id.iv_favorite)
    ImageView ivFavorite;
    @Bind(R.id.iv_download)
    ImageView ivDownload;
    @Bind(R.id.iv_praise)
    ImageView ivPraise;
    @Bind(R.id.iv_share)
    ImageView ivShare;
    @Inject
    GrilPhotoDetailPresenterImpl mGrilPhotoDetailPresenter;
    @Inject
    Activity mActivity;
    private List<BeautyPhotoInfo> mPhotoList;
    private int mIndex; // 初始索引
    private boolean mIsFromLoveActivity;    // 是否从 LoveActivity 启动进来
    @Inject
    GrilPhotoPagerAdapter mGrilPhotoPagerAdapter;
    private boolean mIsAllLoaded;

    private String title, shareLine, imageUrl;
    private int mCurPosition;
    private List<BeautyPhotoInfo> mDbLovedData;

    @Override
    public int getLayoutId() {
        return R.layout.activity_girl_photo_detail;
    }

    private void initValue() {
        mPhotoList = getIntent().getParcelableArrayListExtra(Constants.GIRL_PHOTO_KEY);
        mIndex = getIntent().getIntExtra(Constants.GIRL_PHOTO_INDEX_KEY, 0);
        mIsFromLoveActivity = getIntent().getBooleanExtra(Constants.FROM_LOVE_ACTIVITY, false);
    }

    /**
     * 第一次进入设置图片
     */
    private void initPhotoViewPager() {
        vpPhoto.setAdapter(mGrilPhotoPagerAdapter);
        dragLayout.interactWithViewPager(true);
        dragLayout.setAnimatorMode(DragSlopLayout.FLIP_Y);
    }

    @Override
    public void initViews() {
        initValue();
        initPhotoViewPager();
        initListener();
        initPresenter();
        NetUtil.isNetworkErrThenShowMsg();
        mDbLovedData = App.getBeautyPhotoInfoDao().queryBuilder().list();
    }

    private void initListener() {
        vpPhoto.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setToolbarTitle(position);
            }

            @Override
            public void onPageSelected(int position) {
                setToolbarTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (!mIsFromLoveActivity) {
            mGrilPhotoPagerAdapter.setLoadMoreListener(new GrilPhotoPagerAdapter.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    mGrilPhotoDetailPresenter.loadMore();
                }
            });
        }

        RxPermissions rxPermissions = new RxPermissions(this);
        RxView.clicks(ivDownload)
                .compose(rxPermissions.ensure(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            downLoadImage(ivDownload);
                        } else {
                            Snackbar.make(getWindow().getDecorView(), R.string.down_fail, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void downLoadImage(final View ivDownload) {
        boolean isSelected = !ivDownload.isSelected();
        ivDownload.setSelected(isSelected);
        if (isSelected) {
            int index = imageUrl.lastIndexOf(".");
            final String imageEnd = imageUrl.substring(index, imageUrl.length());
            RetrofitManager.getInstance(HostType.NETEASE_NEWS_VIDEO).getDownLoadImageObservable(imageUrl)
                    .compose(TransformUtils.<ResponseBody>defaultSchedulers())
                    .subscribe(new Subscriber<ResponseBody>() {
                        @Override
                        public void onCompleted() {
                            mGrilPhotoPagerAdapter.getData(mCurPosition).setDownload(true);
                            ivDownload.setSelected(true);
                            insert(mGrilPhotoPagerAdapter.getData(mCurPosition));
                            Toast.makeText(mActivity, R.string.download_completed, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                            String saveUrl = SharedPreferencesUtil.getString(mActivity, "image_save_path", Constants.IMAGE_SAVE_PATH);
                            saveUrl += "/" + System.currentTimeMillis() + imageEnd;
                            try {
                                FileUtils.betyToFile(responseBody.bytes(), saveUrl);
                                //通知系统图库更新
                                MyUtils.noticeSysRefGallery(mActivity, saveUrl);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            delete(mGrilPhotoPagerAdapter.getData(mCurPosition));
        }
    }

    /**
     * 设置标题
     *
     * @param position
     */
    private void setToolbarTitle(int position) {
        mCurPosition = position;
        // 设置图标状态
        ivFavorite.setSelected(mGrilPhotoPagerAdapter.isLoved(position));
        ivDownload.setSelected(mGrilPhotoPagerAdapter.isDownload(position));
        ivPraise.setSelected(mGrilPhotoPagerAdapter.isPraise(position));
        toolbar.setTitle(mPhotoList.get(position).getTitle());
        title = mPhotoList.get(position).getTitle();
        shareLine = mPhotoList.get(position).getImgsrc();
        imageUrl = mPhotoList.get(position).getImgsrc();
    }

    private void initPresenter() {
        mGrilPhotoDetailPresenter.setBeautyPhotoInfos(mPhotoList);
        mPresenter = mGrilPhotoDetailPresenter;
        mPresenter.attachView(this);
        mPresenter.onCreate();
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @OnClick({R.id.iv_favorite, R.id.iv_praise, R.id.iv_share})
    public void onClick(View view) {
        final boolean isSelected = !view.isSelected();
        switch (view.getId()) {
            case R.id.iv_favorite:
                mGrilPhotoPagerAdapter.getData(mCurPosition).setLove(isSelected);
                break;
            case R.id.iv_praise:
                mGrilPhotoPagerAdapter.getData(mCurPosition).setPraise(isSelected);
                break;
            case R.id.iv_share:
                ShareUtils.share(title, shareLine, "", imageUrl, null);
                break;
        }
        if (view.getId() != R.id.iv_share) {
            view.setSelected(isSelected);
            AnimateHelper.doHeartBeat(view, 500);
            if (isSelected) {
                insert(mGrilPhotoPagerAdapter.getData(mCurPosition));
            } else {
                delete(mGrilPhotoPagerAdapter.getData(mCurPosition));
            }
        }
        RxBus.getInstance().post(new MessageEvent(Constants.BOOKMARKS_PHOTO, new String[]{Constants.BOOKMARKS_PHOTO_ID, Constants.BOOKMARKS_PHOTO}));
    }

    private void insert(BeautyPhotoInfo beautyPhotoInfo) {
        if (mDbLovedData.contains(beautyPhotoInfo)) {
            App.getBeautyPhotoInfoDao().update(beautyPhotoInfo);
        } else {
            App.getBeautyPhotoInfoDao().insertOrReplace(beautyPhotoInfo);
        }
    }

    private void delete(BeautyPhotoInfo beautyPhotoInfo) {
        if (!beautyPhotoInfo.isLove() && !beautyPhotoInfo.isDownload() && !beautyPhotoInfo.isPraise()) {
            App.getBeautyPhotoInfoDao().delete(beautyPhotoInfo);
            mDbLovedData.remove(beautyPhotoInfo);
        } else {
            App.getBeautyPhotoInfoDao().update(beautyPhotoInfo);
        }
    }

    @Override
    public void setBeautyPhotoInfos(List<BeautyPhotoInfo> beautyPhotoInfos, @LoadNewsType.checker int loadType) {
        switch (loadType) {
            case LoadNewsType.TYPE_REFRESH_SUCCESS:
                mGrilPhotoPagerAdapter.updateData(beautyPhotoInfos);
                vpPhoto.setCurrentItem(mIndex);
                break;
            case LoadNewsType.TYPE_LOAD_MORE_SUCCESS:
                if (beautyPhotoInfos == null || beautyPhotoInfos.size() == 0) {
                    mIsAllLoaded = true;
                    Snackbar.make(vpPhoto, getString(R.string.no_more), Snackbar.LENGTH_SHORT).show();
                } else {
                    mGrilPhotoPagerAdapter.addData(beautyPhotoInfos);
                }
                break;
            case LoadNewsType.TYPE_LOAD_MORE_ERROR:
                break;
        }
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
