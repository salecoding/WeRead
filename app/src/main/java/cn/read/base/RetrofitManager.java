package cn.read.base;

import android.support.annotation.NonNull;
import android.util.SparseArray;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.read.bean.BeautyPhotoInfo;
import cn.read.bean.ChatReceive;
import cn.read.bean.NewsDetail;
import cn.read.bean.NewsSummary;
import cn.read.bean.PhotoInfo;
import cn.read.bean.PhotoSetInfo;
import cn.read.bean.VideosChannelTable;
import cn.read.bean.VideosSummary;
import cn.read.bean.WelfarePhotoList;
import cn.read.common.ApiConstants;
import cn.read.common.HostType;
import cn.read.utils.NetUtil;
import cn.read.utils.StringUtils;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by lw on 2017/1/16.
 */

public class RetrofitManager {
    private NewsService mNewsService;

    /**
     * 设缓存有效期为两天
     */
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;

    /**
     * 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
     * max-stale 指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可接收超出超时期指定值之内的响应消息。
     */
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;

    /**
     * 查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
     * (假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
     */
    private static final String CACHE_CONTROL_AGE = "max-age=0";
    /**
     * 避免出现 HTTP 403 Forbidden，参考：http://stackoverflow.com/questions/13670692/403-forbidden-with-java-but-not-web-browser
     */
    static final String AVOID_HTTP403_FORBIDDEN = "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";

    private static volatile OkHttpClient sOkHttpClient;

    private static SparseArray<RetrofitManager> sRetrofitManager = new SparseArray<>(HostType.TYPE_COUNT);

    public RetrofitManager(@HostType.HostTypeChecker int hostType) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiConstants.getHost(hostType))
                .client(getOkHttpClient()).addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        mNewsService = retrofit.create(NewsService.class);
    }

    private OkHttpClient getOkHttpClient() {
        if (sOkHttpClient == null) {
            synchronized (RetrofitManager.class) {
                Cache cache = new Cache(new File(App.getContext().getCacheDir(), "HttpCache"), 1024 * 1024 * 100);
                if (sOkHttpClient == null) {
                    sOkHttpClient = new OkHttpClient.Builder().cache(cache)
                            .connectTimeout(6, TimeUnit.SECONDS)
                            .readTimeout(6, TimeUnit.SECONDS)
                            .writeTimeout(6, TimeUnit.SECONDS)
                            .addInterceptor(mRewriteCacheControlInterceptor)
                            .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(mLoggingInterceptor).build();
                }
            }
        }
        return sOkHttpClient;
    }

    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private final Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetUtil.isNetworkAvailable()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if (NetUtil.isNetworkAvailable()) {
                //有网的时候读接口上的@Headers里的配置，可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

    private final Interceptor mLoggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            return response;
        }
    };

    /**
     * @param hostType NETEASE_NEWS_VIDEO：1 （新闻，视频），GANK_GIRL_PHOTO：2（图片新闻）;
     *                 EWS_DETAIL_HTML_PHOTO:3新闻详情html图片)
     */
    public static RetrofitManager getInstance(int hostType) {
        RetrofitManager retrofitManager = sRetrofitManager.get(hostType);
        if (retrofitManager == null) {
            retrofitManager = new RetrofitManager(hostType);
            sRetrofitManager.put(hostType, retrofitManager);
            return retrofitManager;
        }
        return retrofitManager;
    }

    /**
     * 根据网络状况获取缓存的策略
     *
     * @return http缓存策略
     */
    @NonNull
    private String getCacheControl() {
        return NetUtil.isNetworkAvailable() ? CACHE_CONTROL_AGE : CACHE_CONTROL_CACHE;
    }

    /**
     * 网易新闻列表 例子：http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html
     * <p>
     * 对API调用了observeOn(MainThread)之后，线程会跑在主线程上，包括onComplete也是，
     * unsubscribe也在主线程，然后如果这时候调用call.cancel会导致NetworkOnMainThreadException
     * 加一句unsubscribeOn(io)
     *
     * @param newsType  新闻类别：headline为头条,list为其他
     * @param newsId    新闻类别id
     * @param startPage 开始的页码
     * @return 被观察对象
     */
    public Observable<Map<String, List<NewsSummary>>> getNewsListObservable(
            String newsType, String newsId, int startPage) {
        return mNewsService.getNewsList(getCacheControl(), newsType, newsId, startPage);
    }

    /**
     * example：http://c.m.163.com/nc/article/BG6CGA9M00264N2N/full.html
     */
    public Observable<Map<String, NewsDetail>> getNewsDetailObservable(String postId) {
        return mNewsService.getNewDetail(getCacheControl(), postId);
    }

    public Observable<ResponseBody> getNewsBodyHtmlPhoto(String photoPath) {
        return mNewsService.getNewsBodyHtmlPhoto(photoPath);
    }

    /**
     * @param offset 开始页码
     * @return被观察对象
     */
    public Observable<Map<String, List<BeautyPhotoInfo>>> getBeautyPhotoObservable(int offset) {
        return mNewsService.getBeautyPhoto(getCacheControl(), offset);
    }

    /**
     * @param photoSetId
     * @return被观察对象
     */
    public Observable<PhotoSetInfo> getPhotoSetInfoObservable(String photoSetId) {
        return mNewsService.getPhotoSet(getCacheControl(), StringUtils.clipPhotoSetId(photoSetId));
    }

    /**
     * @param offset
     * @return
     */
    public Observable<WelfarePhotoList> getWelfarePhotoObservable(int offset) {
        return mNewsService.getWelfarePhoto(ApiConstants.WELFARE_PHOTO_HOST + offset);
    }

    /**
     * @return
     */
    public Observable<List<PhotoInfo>> getPhotoInfoObservable() {
        return mNewsService.getPhotoList(getCacheControl());
    }

    /**
     * @return
     */
    public Observable<List<PhotoInfo>> getPhotoMoreListObservable(String setId) {
        return mNewsService.getPhotoMoreList(getCacheControl(), setId);
    }

    /**
     * @param videosType 视频类别：默认list
     * @param videosId   视频类别id
     * @param startPage  开始的页码
     * @return 被观察对象
     */
    public Observable<Map<String, List<VideosSummary>>> getVideosListObservable(
            String videosType, String videosId, int startPage) {
        return mNewsService.getVideosList(getCacheControl(), videosType, videosId, startPage);
    }

    /**
     * 获取接收到的信息
     *
     * @param url
     * @return
     */
    public Observable<ChatReceive> getChatReceiveObservable(String url) {
        return mNewsService.getChatReceive(url);
    }

    /**
     * 获取接收到的信息
     *
     * @param url
     * @return
     */
    public Observable<ResponseBody> getDownLoadImageObservable(String url) {
        return mNewsService.downLoadImage(url);
    }
}
