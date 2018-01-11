package cn.read.ui.presenter.impl;


import java.util.List;

import javax.inject.Inject;

import cn.read.bean.BeautyPhotoInfo;
import cn.read.bean.WelfarePhotoList;
import cn.read.common.LoadNewsType;
import cn.read.listener.RequestCallBack;
import cn.read.ui.interactor.BeautyPhotoInteractor;
import cn.read.ui.interactor.WelfarePhotoInteractor;
import cn.read.ui.interactor.impl.BeautyPhotoInteractorImpl;
import cn.read.ui.interactor.impl.WelfarePhotoInteractorImpl;
import cn.read.ui.presenter.BeautyPhotoPresenter;
import cn.read.ui.presenter.WelfarePhotoPresenter;
import cn.read.ui.view.BeautyPhotoView;
import cn.read.ui.view.WelfarePhotoView;

/**
 * Created by lw on 2017/1/17.
 */

public class WelfarePhotoPresenterImpl extends BasePresenterImpl<WelfarePhotoView, List<WelfarePhotoList.ResultsBean>>
        implements WelfarePhotoPresenter, RequestCallBack<List<WelfarePhotoList.ResultsBean>> {

    private WelfarePhotoInteractor<List<WelfarePhotoList.ResultsBean>> mWelfarePhotoInteractor;
    private int mStartPage;

    private boolean misFirstLoad;
    private boolean mIsRefresh = true;

    @Inject
    public WelfarePhotoPresenterImpl(WelfarePhotoInteractorImpl welfarePhotoInteractor) {
        mWelfarePhotoInteractor = welfarePhotoInteractor;
    }

    @Override
    public void onCreate() {
        if (mView != null) {
            loadWelfarePhotoData();
        }
    }

    @Override
    public void beforeRequest() {
        if (!misFirstLoad) {
            mView.showProgress();
        }
    }

    @Override
    public void onError(String errorMsg) {
        super.onError(errorMsg);
        if (mView != null) {
            int loadType = mIsRefresh ? LoadNewsType.TYPE_REFRESH_ERROR : LoadNewsType.TYPE_LOAD_MORE_ERROR;
            mView.setWelfarePhotoList(null, loadType);
        }
    }

    @Override
    public void success(List<WelfarePhotoList.ResultsBean> items) {
        misFirstLoad = true;
        if (items != null) {
            mStartPage++;
        }

        int loadType = mIsRefresh ? LoadNewsType.TYPE_REFRESH_SUCCESS : LoadNewsType.TYPE_LOAD_MORE_SUCCESS;
        if (mView != null) {
            mView.setWelfarePhotoList(items, loadType);
            mView.hideProgress();
        }

    }

    @Override
    public void refreshData() {
        mStartPage = 0;
        mIsRefresh = true;
        loadWelfarePhotoData();
    }

    @Override
    public void loadMore() {
        mIsRefresh = false;
        loadWelfarePhotoData();
    }

    private void loadWelfarePhotoData() {
        mSubscription = mWelfarePhotoInteractor.loadWelfarePhotos(this, mStartPage);
    }
}
