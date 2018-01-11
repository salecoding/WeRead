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
import cn.read.ui.interactor.GrilPhotoDetailInteractor;
import cn.read.utils.MyUtils;
import cn.read.utils.TransformUtils;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;


/**
 * Created by lw on 2017/1/17.
 */

public class GrilPhotoDetailInteractorImpl implements GrilPhotoDetailInteractor<List<BeautyPhotoInfo>> {

    //    private boolean mIsNetError;
    private List<BeautyPhotoInfo> mDbLovedData;

    @Inject
    public GrilPhotoDetailInteractorImpl() {
        mDbLovedData = App.getBeautyPhotoInfoDao().queryBuilder().list();
    }

    @Override
    public Subscription lodeFirstGrilPhotos(final RequestCallBack<List<BeautyPhotoInfo>> callBack, List<BeautyPhotoInfo> beautyPhotoInfos) {
        return Observable.from(beautyPhotoInfos)
                .compose(mTransformer)
                .subscribe(new Subscriber<List<BeautyPhotoInfo>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e.toString());
                        callBack.onError(MyUtils.analyzeNetworkError(e));
                    }

                    @Override
                    public void onNext(List<BeautyPhotoInfo> photoList) {
                        KLog.d();
                        callBack.success(photoList);
                    }
                });

    }

    @Override
    public Subscription lodeGrilPhotos(final RequestCallBack<List<BeautyPhotoInfo>> callBack, int offset) {
        return RetrofitManager.getInstance(HostType.NETEASE_NEWS_VIDEO).getBeautyPhotoObservable(offset)
                .flatMap(new Func1<Map<String, List<BeautyPhotoInfo>>, Observable<BeautyPhotoInfo>>() {
                    @Override
                    public Observable<BeautyPhotoInfo> call(Map<String, List<BeautyPhotoInfo>> map) {
                        return Observable.from(map.get(App.getContext().getString(R.string.beauty)));
                    }
                })
                .compose(mTransformer)
                .subscribe(new Subscriber<List<BeautyPhotoInfo>>() {
                    @Override
                    public void onCompleted() {
                        KLog.d();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e.toString());
                        callBack.onError(MyUtils.analyzeNetworkError(e));
                    }

                    @Override
                    public void onNext(List<BeautyPhotoInfo> photoList) {
                        KLog.d();
                        callBack.success(photoList);
                    }
                });
    }


    /**
     * 统一变换
     */
    private Observable.Transformer<BeautyPhotoInfo, List<BeautyPhotoInfo>> mTransformer
            = new Observable.Transformer<BeautyPhotoInfo, List<BeautyPhotoInfo>>() {

        @Override
        public Observable<List<BeautyPhotoInfo>> call(Observable<BeautyPhotoInfo> listObservable) {
            return listObservable
                    .doOnNext(new Action1<BeautyPhotoInfo>() {
                        BeautyPhotoInfo tmpBean;

                        @Override
                        public void call(BeautyPhotoInfo bean) {
                            // 判断数据库是否有数据，有则设置对应参数
                            KLog.i(bean.toString());
                            if (mDbLovedData.contains(bean)) {
                                tmpBean = mDbLovedData.get(mDbLovedData.indexOf(bean));
                                bean.setLove(tmpBean.isLove());
                                bean.setPraise(tmpBean.isPraise());
                                bean.setDownload(tmpBean.isDownload());
                            }
                        }
                    })
                    .distinct()
                    .toList()
                    .compose(TransformUtils.<List<BeautyPhotoInfo>>defaultSchedulers());
        }
    };
}
