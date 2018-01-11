package cn.read.base;


import java.util.List;
import java.util.Map;

import cn.read.bean.BeautyPhotoInfo;
import cn.read.bean.ChatReceive;
import cn.read.bean.NewsDetail;
import cn.read.bean.NewsSummary;
import cn.read.bean.PhotoInfo;
import cn.read.bean.PhotoSetInfo;
import cn.read.bean.VideosSummary;
import cn.read.bean.WelfarePhotoList;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

import static cn.read.base.RetrofitManager.AVOID_HTTP403_FORBIDDEN;

/**
 * Created by lw on 2017/1/16.
 */

public interface NewsService {
    /**
     * 获取新闻列表
     *
     * @param cacheControl
     * @param type
     * @param id
     * @param startPage
     * @return
     */
    @GET("nc/article/{type}/{id}/{startPage}-20.html")
    Observable<Map<String, List<NewsSummary>>> getNewsList(
            @Header("Cache-Control") String cacheControl,
            @Path("type") String type,
            @Path("id") String id,
            @Path("startPage") int startPage);

    /**
     * 获取新闻详情
     *
     * @param cacheControl
     * @param postId
     * @return
     */
    @GET("nc/article/{postId}/full.html")
    Observable<Map<String, NewsDetail>> getNewDetail(
            @Header("Cache-Control") String cacheControl,
            @Path("postId") String postId);

    @GET
    Observable<ResponseBody> getNewsBodyHtmlPhoto(
            @Url String photoPath);
    //@Url，它允许我们直接传入一个请求的URL。这样以来我们可以将上一个请求的获得的url直接传入进来，baseUrl将被无视
    // baseUrl 需要符合标准，为空、""、或不合法将会报错

    /**
     * 获取新闻详情
     * eg: http://c.3g.163.com/photo/api/set/0006/2136404.json
     *
     * @param photoId 图集ID
     * @return
     */
    @Headers(AVOID_HTTP403_FORBIDDEN)
    @GET("photo/api/set/{photoId}.json")
    Observable<PhotoSetInfo> getPhotoSet(@Header("Cache-Control") String cacheControl, @Path("photoId") String photoId);

    /**
     * 获取图片列表
     * eg: http://c.3g.163.com/photo/api/list/0096/4GJ60096.json
     *
     * @return
     */
    @GET("photo/api/list/0096/4GJ60096.json")
    Observable<List<PhotoInfo>> getPhotoList(@Header("Cache-Control") String cacheControl);

    /**
     * 获取更多图片列表
     * eg: http://c.3g.163.com/photo/api/morelist/0096/4GJ60096/106571.json
     *
     * @return
     */
    @GET("photo/api/morelist/0096/4GJ60096/{setId}.json")
    Observable<List<PhotoInfo>> getPhotoMoreList(@Header("Cache-Control") String cacheControl, @Path("setId") String setId);

    /**
     * 获取美女图片，这个API不完整，省略了好多参数
     * eg: http://c.3g.163.com/recommend/getChanListNews?channel=T1456112189138&size=20&offset=0
     *
     * @param offset 起始页码
     * @return
     */
    @GET("recommend/getChanListNews?channel=T1456112189138&size=20")
    Observable<Map<String, List<BeautyPhotoInfo>>> getBeautyPhoto(@Header("Cache-Control") String cacheControl, @Query("offset") int offset);

    /**
     * 获取福利图片
     * eg: http://gank.io/api/data/福利/20/1
     *
     * @param welfarePhotoUrl
     * @return
     */
    @GET
    Observable<WelfarePhotoList> getWelfarePhoto(@Url String welfarePhotoUrl);

    /**
     * 获取视频列表
     *
     * @param cacheControl
     * @param type
     * @param id
     * @param startPage
     * @return
     */
    //http://c.3g.163.com/nc/video/list/V9LG4B3A0/n/10-10.html
    @Headers(AVOID_HTTP403_FORBIDDEN)
    @GET("nc/video/{type}/{id}/n/{startPage}-10.html")
    Observable<Map<String, List<VideosSummary>>> getVideosList(
            @Header("Cache-Control") String cacheControl,
            @Path("type") String type,
            @Path("id") String id,
            @Path("startPage") int startPage);

    /**
     * 获取接收到的信息
     *
     * @param url
     * @return
     */
    @GET
    Observable<ChatReceive> getChatReceive(@Url String url);

    /**
     * 下载图片
     *
     * @param url
     * @return
     */
    @GET
    Observable<ResponseBody> downLoadImage(@Url String url);
}
