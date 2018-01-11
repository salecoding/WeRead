package cn.read.ui.interactor.impl;

import com.socks.library.KLog;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import cn.read.R;
import cn.read.base.App;
import cn.read.base.RetrofitManager;
import cn.read.bean.BeautyPhotoInfo;
import cn.read.common.HostType;
import cn.read.listener.RequestCallBack;
import cn.read.ui.interactor.BeautyPhotoInteractor;
import cn.read.utils.MyUtils;
import cn.read.utils.TransformUtils;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;


/**
 * Created by lw on 2017/1/17.
 */

public class BeautyPhotoInteractorImpl implements BeautyPhotoInteractor<List<BeautyPhotoInfo>> {

    //    private boolean mIsNetError;
    @Inject
    public BeautyPhotoInteractorImpl() {
    }

    @Override
    public Subscription loadBeautyPhotos(final RequestCallBack<List<BeautyPhotoInfo>> listener, int offset) {
        return RetrofitManager.getInstance(HostType.NETEASE_NEWS_VIDEO).getBeautyPhotoObservable(offset)
                .flatMap(new Func1<Map<String, List<BeautyPhotoInfo>>, Observable<BeautyPhotoInfo>>() {
                    @Override
                    public Observable<BeautyPhotoInfo> call(Map<String, List<BeautyPhotoInfo>> map) {
                        return Observable.from(map.get(App.getContext().getString(R.string.beauty)));
                    }
                })
                .distinct()
                .toList()
                .compose(TransformUtils.<List<BeautyPhotoInfo>>defaultSchedulers())
                .subscribe(new Subscriber<List<BeautyPhotoInfo>>() {
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
                    public void onNext(List<BeautyPhotoInfo> videosSummaries) {
                        KLog.d();
                        listener.success(videosSummaries);
                    }
                });
    }
}
