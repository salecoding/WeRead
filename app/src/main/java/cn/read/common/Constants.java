package cn.read.common;

import android.os.Environment;

import java.io.File;

/**
 * Created by lw on 2017-03-06.
 */

public class Constants {
    public static final String ISNIGHT = "isNight";
    public static final String ISENGLISH = "isEnglish";
    public static final String ISGRID = "isGrid";
    public static final String APP_CONFIG = "appConfig";

    public static final String NEWS_FRAGMENT = "NewsFragment";
    public static final String PHOTOS_FRAGMENT = "PhotosFragment";
    public static final String VIDEOS_FRAGMENT = "VideosFragment";
    public static final String BOOKMARKS_FRAGMENT = "BookMarksFragment";
    public static final String BEAUTYCHAT_FRAGMENT = "BeautyChatFragment";
    public static final String OTHERSERVER_FRAGMENT = "OtherServerFragment";
    public static final String ABOUT_FRAGMENT = "AboutFragment";
    public static final String SETTING_FRAGMENT = "SettingFragment";

    public static final String NEWS_ID = "news_id";
    public static final String NEWS_TYPE = "news_type";
    public static final String CHANNEL_POSITION = "channel_position";
    public static final String VIDEOS_ID = "videos_id";
    public static final String VIDEOS_TYPE = "videos_type";
    public final static String PREFIX_PHOTO_ID = "0096";
    public static final String BOOKMARKS_ID = "bookmarks_id";
    public static final String BOOKMARKS_TYPE = "bookmarks_type";

    public static final String BOOKMARKS_NEWS = "news";
    public static final String BOOKMARKS_PHOTO = "photo";
    public static final String BOOKMARKS_VIDEO = "video";
    public static final String BOOKMARKS_NEWS_ID = "1";
    public static final String BOOKMARKS_PHOTO_ID = "2";
    public static final String BOOKMARKS_VIDEO_ID = "3";
    /**
     * 腾讯BUGLY
     */
    public final static String BUGLY_APPID = "0aa4f7c08f";
    public final static String BUGLY_APPKEY = "36a0780e-f497-41a2-acb4-7d447e439c92";

    public static final String INIT_DB = "init_db";
    public static final String DB_NAME = "NewsChannelTable";
    public static final String SHARES_COLOURFUL_NEWS = "shares_colourful_news";
    public static final String NIGHT_THEME_MODE = "night_theme_mode";

    public static final int NEWS_CHANNEL_MINE = 0;
    public static final int NEWS_CHANNEL_MORE = 1;

    public static final int VIDEOS_CHANNEL_MINE = 0;
    public static final int VIDEOS_CHANNEL_MORE = 1;

    public static final String SHOW_NEWS_PHOTO = "show_news_photo";
    public static final String NEWS_POST_ID = "news_post_id";
    public static final String NEWS_IMG_RES = "news_img_res";
    public static final String NEWS_BEAN = "news_bean";
    public static final String PHOTO_DETAIL = "photo_detail";
    public static final String PHOTO_DETAIL_IMGSRC = "photo_detail_imgsrc";
    public static final String TRANSITION_ANIMATION_NEWS_PHOTOS = "transition_animation_news_photos";

    public static final String GIRL_PHOTO_KEY = "GIRLPhotoKey";
    public static final String GIRL_PHOTO_INDEX_KEY = "GIRLPhotoIndexKey";
    public static final String FROM_LOVE_ACTIVITY = "FromLoveActivity";

    public static final String NEWS_ITEM_PHOTO_SET = "photoset";
    public static final String NEWS_PHOTO_SET_KEY = "PhotoSetKey";

    /**
     * SharkSdk分享appkey
     */
    public static final String SHARESDK_SHARE_APP_KEY = "1c017fdc7ed00";
    /**
     * ShareSdk数据查询appkey
     */
    public static final String SHARESDK_DATA_QUERY_APP_KEY = "1c258e83e61c1";

    //获取SD目录
    private static File sdPath = Environment.getExternalStorageDirectory().getAbsoluteFile();
    public static final String IMAGE_SAVE_PATH = sdPath + "/WeRead/image";
    public static final String VIDEO_SAVE_PATH = sdPath + "/WeRead/video";

    public static final String CHAR = "char";

}
