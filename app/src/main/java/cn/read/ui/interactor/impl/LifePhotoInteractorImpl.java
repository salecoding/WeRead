package cn.read.ui.interactor.impl;

import com.socks.library.KLog;

import java.util.List;

import javax.inject.Inject;

import cn.read.base.RetrofitManager;
import cn.read.bean.PhotoInfo;
import cn.read.common.HostType;
import cn.read.listener.RequestCallBack;
import cn.read.ui.interactor.LifePhotoInteractor;
import cn.read.utils.MyUtils;
import cn.read.utils.TransformUtils;
import rx.Subscriber;
import rx.Subscription;


/**
 * Created by lw on 2017/1/17.
 */

public class LifePhotoInteractorImpl implements LifePhotoInteractor<List<PhotoInfo>> {

    //    private boolean mIsNetError;
    @Inject
    public LifePhotoInteractorImpl() {
    }

    @Override
    public Subscription loadLifePhotos(final RequestCallBack<List<PhotoInfo>> listener) {
        return RetrofitManager.getInstance(HostType.NETEASE_NEWS_VIDEO).getPhotoInfoObservable()
                .compose(TransformUtils.<List<PhotoInfo>>defaultSchedulers())
                .subscribe(new Subscriber<List<PhotoInfo>>() {
                    @Override
                    public void onCompleted() {
                        KLog.d();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e.toString());
                        listener.onError(MyUtils.analyzeNetworkError(e));
                    }

                    @Override
                    public void onNext(List<PhotoInfo> resultsBeen) {
                        KLog.d();
                        listener.success(resultsBeen);
                    }
                });
    }

    @Override
    public Subscription loadMoreLifePhotos(final RequestCallBack<List<PhotoInfo>> listener, String setId) {
        return RetrofitManager.getInstance(HostType.NETEASE_NEWS_VIDEO).getPhotoMoreListObservable(setId)
                .compose(TransformUtils.<List<PhotoInfo>>defaultSchedulers())
                .subscribe(new Subscriber<List<PhotoInfo>>() {
                    @Override
                    public void onCompleted() {
                        KLog.d();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e.toString());
                        listener.onError(MyUtils.analyzeNetworkError(e));
                    }

                    @Override
                    public void onNext(List<PhotoInfo> resultsBeen) {
                        KLog.d();
                        listener.success(resultsBeen);
                    }
                });
    }
}
