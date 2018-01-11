package cn.read.ui.interactor.impl;

import com.socks.library.KLog;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import cn.read.base.App;
import cn.read.base.RetrofitManager;
import cn.read.bean.BeautyPhotoInfo;
import cn.read.bean.VideosSummary;
import cn.read.common.HostType;
import cn.read.listener.RequestCallBack;
import cn.read.ui.interactor.VideosListInteractor;
import cn.read.utils.MyUtils;
import cn.read.utils.TransformUtils;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;


/**
 * Created by lw on 2017/1/17.
 */

public class VideosListInteractorImpl implements VideosListInteractor<List<VideosSummary>> {

    //    private boolean mIsNetError;
    private List<VideosSummary> videosSummaries;

    @Inject
    public VideosListInteractorImpl() {
        videosSummaries = App.getVideosSummaryDao().loadAll();
    }

    @Override
    public Subscription loadVideos(final RequestCallBack<List<VideosSummary>> listener, final String type, final String id, final int startPage) {
        return RetrofitManager.getInstance(HostType.NETEASE_NEWS_VIDEO).getVideosListObservable(type, id, startPage)
                .flatMap(new Func1<Map<String, List<VideosSummary>>, Observable<VideosSummary>>() {
                    @Override
                    public Observable<VideosSummary> call(Map<String, List<VideosSummary>> map) {
                        return Observable.from(map.get(id));
                    }
                })
                .map(new Func1<VideosSummary, VideosSummary>() {
                    @Override
                    public VideosSummary call(VideosSummary videosSummary) {
                        String ptime = MyUtils.formatDate(videosSummary.getPtime());
                        videosSummary.setPtime(ptime);
                        return videosSummary;
                    }
                })
                .distinct()
                .compose(mTransformer)
                .subscribe(new Subscriber<List<VideosSummary>>() {
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
                    public void onNext(List<VideosSummary> videosSummaries) {
                        KLog.d();
                        listener.success(videosSummaries);
                    }
                });
    }

    /**
     * 统一变换
     */
    private Observable.Transformer<VideosSummary, List<VideosSummary>> mTransformer
            = new Observable.Transformer<VideosSummary, List<VideosSummary>>() {

        @Override
        public Observable<List<VideosSummary>> call(Observable<VideosSummary> listObservable) {
            return listObservable
                    .doOnNext(new Action1<VideosSummary>() {
                        VideosSummary tmpBean;

                        @Override
                        public void call(VideosSummary bean) {
                            // 判断数据库是否有数据，有则设置对应参数
                            KLog.i(bean.toString());
                            if (videosSummaries.contains(bean)) {
                                tmpBean = videosSummaries.get(videosSummaries.indexOf(bean));
                                bean.setIsFavorites(tmpBean.getIsFavorites());
                            }
                        }
                    })
                    .distinct()
                    .toSortedList(new Func2<VideosSummary, VideosSummary, Integer>() {
                        @Override
                        public Integer call(VideosSummary videosSummary, VideosSummary videosSummary2) {
                            return videosSummary2.getPtime().compareTo(videosSummary.getPtime());
                        }
                    })
                    .compose(TransformUtils.<List<VideosSummary>>defaultSchedulers());
        }
    };
}
