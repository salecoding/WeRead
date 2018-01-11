package cn.read.ui.interactor.impl;

import com.socks.library.KLog;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import cn.read.R;
import cn.read.base.App;
import cn.read.base.RetrofitManager;
import cn.read.bean.NewsSummary;
import cn.read.common.ApiConstants;
import cn.read.common.HostType;
import cn.read.dao.NewsSummaryDao;
import cn.read.listener.RequestCallBack;
import cn.read.ui.interactor.NewsListInteractor;
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

public class NewsListInteractorImpl implements NewsListInteractor<List<NewsSummary>> {

    //    private boolean mIsNetError;
    private List<NewsSummary> newsSummaryList;

    @Inject
    public NewsListInteractorImpl() {
        newsSummaryList = App.getNewsSummaryDao().queryBuilder()
                .where(NewsSummaryDao.Properties.IsFavorites.eq(true)).list();
    }

    @Override
    public Subscription loadNews(final RequestCallBack<List<NewsSummary>> listener, String type,
                                 final String id, int startPage) {
//        mIsNetError = false;
        // 对API调用了observeOn(MainThread)之后，线程会跑在主线程上，包括onComplete也是，
        // unsubscribe也在主线程，然后如果这时候调用call.cancel会导致NetworkOnMainThreadException
        // 加一句unsubscribeOn(io)
        return RetrofitManager.getInstance(HostType.NETEASE_NEWS_VIDEO).getNewsListObservable(type, id, startPage)
                .flatMap(new Func1<Map<String, List<NewsSummary>>, Observable<NewsSummary>>() {
                    @Override
                    public Observable<NewsSummary> call(Map<String, List<NewsSummary>> map) {
                        if (id.endsWith(ApiConstants.HOUSE_ID)) {
                            // 房产实际上针对地区的它的id与返回key不同
                            return Observable.from(map.get(App.getContext().getString(R.string.beijing)));
                        }
                        return Observable.from(map.get(id));
                    }
                })
                .map(new Func1<NewsSummary, NewsSummary>() {
                    @Override
                    public NewsSummary call(NewsSummary newsSummary) {
                        String ptime = MyUtils.formatDate(newsSummary.getPtime());
                        newsSummary.setPtime(ptime);
                        return newsSummary;
                    }
                })
                .compose(mTransformer)
                .subscribe(new Subscriber<List<NewsSummary>>() {
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
                    public void onNext(List<NewsSummary> newsSummaries) {
                        KLog.d();
                        listener.success(newsSummaries);
                    }
                });

    }

    /**
     * 统一变换
     */
    private Observable.Transformer<NewsSummary, List<NewsSummary>> mTransformer
            = new Observable.Transformer<NewsSummary, List<NewsSummary>>() {

        @Override
        public Observable<List<NewsSummary>> call(Observable<NewsSummary> listObservable) {
            return listObservable
                    .doOnNext(new Action1<NewsSummary>() {
                        NewsSummary tmpBean;

                        @Override
                        public void call(NewsSummary bean) {
                            // 判断数据库是否有数据，有则设置对应参数
                            KLog.i(bean.toString());
                            if (newsSummaryList.contains(bean)) {
                                tmpBean = newsSummaryList.get(newsSummaryList.indexOf(bean));
                                bean.setIsFavorites(tmpBean.getIsFavorites());
                            }
                        }
                    })
                    .distinct()
                    .toSortedList(new Func2<NewsSummary, NewsSummary, Integer>() {
                        @Override
                        public Integer call(NewsSummary NewsSummary, NewsSummary NewsSummary2) {
                            return NewsSummary2.getPtime().compareTo(NewsSummary.getPtime());
                        }
                    })
                    .compose(TransformUtils.<List<NewsSummary>>defaultSchedulers());
        }
    };
}
