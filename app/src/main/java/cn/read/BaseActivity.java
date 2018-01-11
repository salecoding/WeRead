package cn.read;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.socks.library.KLog;
import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;
import cn.read.annotation.BindValues;
import cn.read.base.App;
import cn.read.common.Constants;
import cn.read.di.component.ActivityComponent;
import cn.read.di.component.DaggerActivityComponent;
import cn.read.di.module.ActivityModule;
import cn.read.event.MessageEvent;
import cn.read.event.OtherShowChangeEvent;
import cn.read.ui.activities.HomeActivity;
import cn.read.ui.activities.NewsChannelActivity;
import cn.read.ui.activities.VideosChannelActivity;
import cn.read.ui.activities.otherserver.BaseStationLocationActivity;
import cn.read.ui.activities.otherserver.FootBallFiveLeagueActivity;
import cn.read.ui.activities.otherserver.IdCardInquiriesActivity;
import cn.read.ui.activities.otherserver.PhoneAttrActivity;
import cn.read.ui.activities.otherserver.RecipeActivity;
import cn.read.ui.activities.otherserver.TheQuestionBankActivity;
import cn.read.ui.activities.otherserver.TrainTicketInquiriesActivity;
import cn.read.ui.activities.otherserver.WeatherActivity;
import cn.read.ui.activities.otherserver.ZGSolutionDreamActivity;
import cn.read.ui.activities.otherserver.ZipCodeActivity;
import cn.read.ui.presenter.BasePresenter;
import cn.read.utils.MyUtils;
import cn.read.utils.NetUtil;
import cn.read.utils.RxBus;
import cn.read.utils.SharedPreferencesUtil;
import rx.Subscription;

/**
 * Created by lw on 2017-03-06.
 */

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {
    protected boolean mIsHasNavigationView;
    protected NavigationView mBaseNavView;
    protected Toolbar toolbar;
    protected ActivityComponent mActivityComponent;
    private DrawerLayout mDrawerLayout;
    public Activity mActivity;
    private boolean mIsChangeTheme;
    protected Subscription mSubscription;
    protected T mPresenter;

    public abstract int getLayoutId();

    public abstract void initViews();

    public abstract void initInjector();

    public void showFragment(String fragmentTag) {

    }

    /**
     * 默认新闻
     */
    private String fragmentType = Constants.NEWS_FRAGMENT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        KLog.i(getClass().getSimpleName());
        initAnnotation();
        NetUtil.isNetworkErrThenShowMsg();
        initActivityComponent();
        initSystemBarTint();
        setStatusBarTranslucent();
        int layoutId = getLayoutId();
        setContentView(layoutId);
        initInjector();
        ButterKnife.bind(this);
        initToolBar();
        initViews();
        if (mIsHasNavigationView) {
            initDrawerLayout();
        }
        if (mPresenter != null) {
            mPresenter.onCreate();
        }
        initNightModeSwitch();
    }

    @Override
    public void onBackPressed() {
        if (mIsHasNavigationView && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (mIsHasNavigationView) {
            overridePendingTransition(0, 0);
        }
    }

    //colorPrimaryDark
    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void setStatusBarTranslucent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && isStatusBarTranslucent()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.colorPrimary);
        }
    }

    /**
     * 那些activity需要沉浸式
     *
     * @return
     */
    private boolean isStatusBarTranslucent() {
        return this instanceof NewsChannelActivity ||
                this instanceof VideosChannelActivity ||
                this instanceof WeatherActivity ||
                this instanceof PhoneAttrActivity ||
                this instanceof ZipCodeActivity ||
                this instanceof RecipeActivity ||
                this instanceof BaseStationLocationActivity ||
                this instanceof IdCardInquiriesActivity ||
                this instanceof TheQuestionBankActivity ||
                this instanceof TrainTicketInquiriesActivity ||
                this instanceof FootBallFiveLeagueActivity ||
                this instanceof ZGSolutionDreamActivity;
    }

    private void initAnnotation() {
        if (getClass().isAnnotationPresent(BindValues.class)) {
            BindValues annotation = getClass().getAnnotation(BindValues.class);
            mIsHasNavigationView = annotation.mIsHasNavigationView();
        }
    }

    private void initActivityComponent() {
        mActivityComponent = DaggerActivityComponent.builder()
                .applicationComponent(((App) getApplication()).getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initNightModeSwitch() {
        if (this instanceof HomeActivity) {
            MenuItem menuNightMode = mBaseNavView.getMenu().findItem(R.id.nav_night_mode);
            SwitchCompat dayNightSwitch = (SwitchCompat) MenuItemCompat
                    .getActionView(menuNightMode);
            setCheckedState(dayNightSwitch);
            setCheckedEvent(dayNightSwitch);
        }
    }

    private void setCheckedState(SwitchCompat dayNightSwitch) {
        boolean isNight = SharedPreferencesUtil.getBoolean(this, Constants.ISNIGHT, false);
        if (isNight) {
            dayNightSwitch.setChecked(true);
        } else {
            dayNightSwitch.setChecked(false);
        }
        KLog.i("dayNightSwitch：" + dayNightSwitch.isChecked());
    }

    private void setCheckedEvent(SwitchCompat dayNightSwitch) {
        dayNightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferencesUtil.setBoolean(mActivity, Constants.ISNIGHT, true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    SharedPreferencesUtil.setBoolean(mActivity, Constants.ISNIGHT, false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                mIsChangeTheme = true;
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

    private void initDrawerLayout() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            //将侧边栏顶部延伸至status bar
            mDrawerLayout.setFitsSystemWindows(true);
            //将主页面顶部延伸至status bar
            mDrawerLayout.setClipToPadding(false);
        }
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                switch (item.getItemId()) {
                    case R.id.nav_news:
                        fragmentType = Constants.NEWS_FRAGMENT;
                        showFragment(Constants.NEWS_FRAGMENT);
                        break;
                    case R.id.nav_gallery:
                        fragmentType = Constants.PHOTOS_FRAGMENT;
                        showFragment(Constants.PHOTOS_FRAGMENT);
                        break;
                    case R.id.nav_slideshow:
                        fragmentType = Constants.VIDEOS_FRAGMENT;
                        showFragment(Constants.VIDEOS_FRAGMENT);
                        break;
                    case R.id.nav_bookmarks:
                        fragmentType = Constants.BOOKMARKS_FRAGMENT;
                        showFragment(Constants.BOOKMARKS_FRAGMENT);
                        break;
                    case R.id.nav_beautychat:
                        fragmentType = Constants.BEAUTYCHAT_FRAGMENT;
                        showFragment(Constants.BEAUTYCHAT_FRAGMENT);
                        break;
                    case R.id.nav_otherserver:
                        fragmentType = Constants.OTHERSERVER_FRAGMENT;
                        showFragment(Constants.OTHERSERVER_FRAGMENT);
                        break;
                    case R.id.nav_about:
                        fragmentType = Constants.ABOUT_FRAGMENT;
                        showFragment(Constants.ABOUT_FRAGMENT);
                        break;
                    case R.id.nav_setting:
                        fragmentType = Constants.SETTING_FRAGMENT;
                        showFragment(Constants.SETTING_FRAGMENT);
                        break;
                }
                return false;
            }
        });

        mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (mIsChangeTheme) {
                    mIsChangeTheme = false;
                    getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                    recreate();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition();
            } else {
                finish();
            }
        } else if (id == R.id.action_day_night_yes) {
            int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                SharedPreferencesUtil.setBoolean(mActivity, Constants.ISNIGHT, false);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                SharedPreferencesUtil.setBoolean(mActivity, Constants.ISNIGHT, true);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            //调用 recreate(); 使设置生效
            getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
            recreate();
        } else if (id == R.id.action_settings) {
            showFragment(Constants.SETTING_FRAGMENT);
        } else if (id == R.id.action_clear_charlog) {
            App.getInstance().getChatLogDao().deleteAll();
            RxBus.getInstance().post(new MessageEvent(Constants.CHAR));
        } else if (id == R.id.action_grid_them) {
            boolean isGrid = SharedPreferencesUtil.getBoolean(mActivity, Constants.ISGRID, false);
            if (isGrid) {
                SharedPreferencesUtil.setBoolean(mActivity, Constants.ISGRID, false);
            } else {
                SharedPreferencesUtil.setBoolean(mActivity, Constants.ISGRID, true);
            }
            RxBus.getInstance().post(new OtherShowChangeEvent());
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mIsHasNavigationView) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem itemClear = menu.findItem(R.id.action_clear_charlog);
        MenuItem itemGrid = menu.findItem(R.id.action_grid_them);
        if (itemClear != null)
            itemClear.setVisible(Constants.BEAUTYCHAT_FRAGMENT.equals(fragmentType) ? true : false);
        if (itemGrid != null)
            itemGrid.setVisible(Constants.OTHERSERVER_FRAGMENT.equals(fragmentType) ? true : false);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = App.getRefWatcher(this);
        refWatcher.watch(this);
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        MyUtils.cancelSubscription(mSubscription);
        MyUtils.fixInputMethodManagerLeak(this);
    }

    /**
     * 设置状态栏颜色
     */
    protected void initSystemBarTint() {
        // 设置状态栏全透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
