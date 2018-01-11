package cn.read.ui.presenter.impl;


import java.util.List;

import javax.inject.Inject;

import cn.read.bean.BeautyPhotoInfo;
import cn.read.common.LoadNewsType;
import cn.read.listener.RequestCallBack;
import cn.read.ui.interactor.BeautyPhotoInteractor;
import cn.read.ui.interactor.impl.BeautyPhotoInteractorImpl;
import cn.read.ui.presenter.BeautyPhotoPresenter;
import cn.read.ui.view.BeautyPhotoView;

/**
 * Created by lw on 2017/1/17.
 */

public class BeautyPhotoPresenterImpl extends BasePresenterImpl<BeautyPhotoView, List<BeautyPhotoInfo>>
        implements BeautyPhotoPresenter, RequestCallBack<List<BeautyPhotoInfo>> {

    private BeautyPhotoInteractor<List<BeautyPhotoInfo>> mBeautyPhotoInteractor;
    private int mStartPage;

    private boolean misFirstLoad;
    private boolean mIsRefresh = true;

    @Inject
    public BeautyPhotoPresenterImpl(BeautyPhotoInteractorImpl beautyPhotoInteractor) {
        mBeautyPhotoInteractor = beautyPhotoInteractor;
    }

    @Override
    public void onCreate() {
        if (mView != null) {
            loadBeautyPhotoData();
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
            mView.setBeautyPhotoList(null, loadType);
        }
    }

    @Override
    public void success(List<BeautyPhotoInfo> items) {
        misFirstLoad = true;
        if (items != null) {
            mStartPage++;
        }

        int loadType = mIsRefresh ? LoadNewsType.TYPE_REFRESH_SUCCESS : LoadNewsType.TYPE_LOAD_MORE_SUCCESS;
        if (mView != null) {
            mView.setBeautyPhotoList(items, loadType);
            mView.hideProgress();
        }

    }

    @Override
    public void refreshData() {
        mStartPage = 0;
        mIsRefresh = true;
        loadBeautyPhotoData();
    }

    @Override
    public void loadMore() {
        mIsRefresh = false;
        loadBeautyPhotoData();
    }

    private void loadBeautyPhotoData() {
        mSubscription = mBeautyPhotoInteractor.loadBeautyPhotos(this, mStartPage);
    }
}
