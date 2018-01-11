package cn.read.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatDelegate;

import com.baidu.mapapi.SDKInitializer;
import com.socks.library.KLog;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;

import cn.read.BuildConfig;
import cn.read.bean.VideosSummary;
import cn.read.common.Constants;
import cn.read.dao.BeautyPhotoInfoDao;
import cn.read.dao.ChatLogDao;
import cn.read.dao.DaoMaster;
import cn.read.dao.DaoSession;
import cn.read.dao.NewsChannelTableDao;
import cn.read.dao.NewsSummaryDao;
import cn.read.dao.VideosChannelTableDao;
import cn.read.dao.VideosSummaryDao;
import cn.read.db.DBHelper;
import cn.read.di.component.ApplicationComponent;
import cn.read.di.component.DaggerApplicationComponent;
import cn.read.di.module.ApplicationModule;
import cn.read.utils.MyUtils;
import cn.read.utils.SharedPreferencesUtil;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by lw on 2017-03-06.
 */

public class App extends Application {
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;
    private static App instance;
    private ApplicationComponent mApplicationComponent;
    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        App application = (App) context.getApplicationContext();
        return application.refWatcher;
    }

    public static Context getContext() {
        synchronized (App.class) {
            return instance.getApplicationContext();
        }
    }

    public static App getInstance() {
        return instance;
    }

    public DaoSession getSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initLeakCanary();
        initActivityLifecycleLogs();
        initStrictMode();
        initBugly();
        SDKInitializer.initialize(this);
        ShareSDK.initSDK(this, Constants.SHARESDK_SHARE_APP_KEY);
        initDayNightMode();
        KLog.init(BuildConfig.DEBUG);
        initDatabass();
        initApplicationComponent();
        boolean isNight = SharedPreferencesUtil.getBoolean(this, Constants.ISNIGHT, false);

        if (isNight) {
            //使用夜间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            //不使用夜间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        boolean isEnglish = SharedPreferencesUtil.getBoolean(this, Constants.ISENGLISH, false);
        if (isEnglish) {
            MyUtils.changeLanguage(getContext(), true);
        } else {
            MyUtils.changeLanguage(getContext(), false);
        }
    }

    private void initDayNightMode() {
        if (MyUtils.isNightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void initDatabass() {
        //这里之后会修改，关于升级数据库
        String regularDBPath = "smartbutler-db";
        dbHelper = new DBHelper(this, regularDBPath, null);
        db = dbHelper.getWritableDatabase();

        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    private void initLeakCanary() {
        if (BuildConfig.DEBUG) {
            refWatcher = LeakCanary.install(this);
        } else {
            refWatcher = installLeakCanary();
        }
    }

    private void initActivityLifecycleLogs() {
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    /**
     * release版本使用此方法
     */
    protected RefWatcher installLeakCanary() {
        return RefWatcher.DISABLED;
    }

    private void initBugly() {
        CrashReport.initCrashReport(getApplicationContext(), Constants.BUGLY_APPID, false);
    }

    // Fixme
    private void initApplicationComponent() {
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    private void initStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                    new StrictMode.ThreadPolicy.Builder()
                            .detectAll()
//                            .penaltyDialog() // 弹出违规提示对话框
                            .penaltyLog() // 在logcat中打印违规异常信息
                            .build());
            StrictMode.setVmPolicy(
                    new StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyLog()
                            .build());
        }
    }

    public static ChatLogDao getChatLogDao() {
        return mDaoSession.getChatLogDao();
    }

    public static NewsChannelTableDao getNewsChannelTableDao() {
        return mDaoSession.getNewsChannelTableDao();
    }

    public static VideosChannelTableDao getVideosChannelTableDao() {
        return mDaoSession.getVideosChannelTableDao();
    }

    public static BeautyPhotoInfoDao getBeautyPhotoInfoDao() {
        return mDaoSession.getBeautyPhotoInfoDao();
    }

    public static NewsSummaryDao getNewsSummaryDao() {
        return mDaoSession.getNewsSummaryDao();
    }

    public static VideosSummaryDao getVideosSummaryDao() {
        return mDaoSession.getVideosSummaryDao();
    }

    public static boolean isHavePhoto() {
        return MyUtils.getSharedPreferences().getBoolean(Constants.SHOW_NEWS_PHOTO, true);
    }
}
