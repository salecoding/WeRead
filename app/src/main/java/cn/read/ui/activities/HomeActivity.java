package cn.read.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;
import cn.read.BaseActivity;
import cn.read.R;
import cn.read.annotation.BindValues;
import cn.read.common.Constants;
import cn.read.event.MessageEvent;
import cn.read.event.ScrollToTopEvent;
import cn.read.ui.fragments.AboutFragment;
import cn.read.ui.fragments.BeautyChatFragment;
import cn.read.ui.fragments.BookMarksFragment;
import cn.read.ui.fragments.NewsFragment;
import cn.read.ui.fragments.OtherServerFragment;
import cn.read.ui.fragments.PhotosFragment;
import cn.read.ui.fragments.SettingFragment;
import cn.read.ui.fragments.VideosFragment;
import cn.read.utils.RxBus;
import rx.functions.Action1;

@BindValues(mIsHasNavigationView = true)
public class HomeActivity extends BaseActivity implements FolderChooserDialog.FolderCallback {

    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private NewsFragment newsFragment;
    private PhotosFragment photosFragment;
    private VideosFragment videosFragment;
    private BookMarksFragment bookMarksFragment;
    private BeautyChatFragment beautyChatFragment;
    private OtherServerFragment otherServerFragment;
    private AboutFragment aboutFragment;
    private SettingFragment settingFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void initViews() {
        mBaseNavView = navView;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragment(savedInstanceState);
        initDir();
    }

    private void initDir() {
        final File imageDir = new File(Constants.IMAGE_SAVE_PATH);
        final File videoDir = new File(Constants.VIDEO_SAVE_PATH);
        createDir(imageDir);
        createDir(videoDir);
    }

    private void createDir(final File dir) {
        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            if (!dir.exists() || !dir.isDirectory())
                                dir.delete();
                            dir.mkdirs();
                        } else {
                            Snackbar.make(getWindow().getDecorView(), R.string.create_dir_faile, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (newsFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, Constants.NEWS_FRAGMENT, newsFragment);
        }

        if (photosFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, Constants.PHOTOS_FRAGMENT, photosFragment);
        }

        if (videosFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, Constants.VIDEOS_FRAGMENT, videosFragment);
        }

        if (bookMarksFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, Constants.BOOKMARKS_FRAGMENT, bookMarksFragment);
        }

        if (beautyChatFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, Constants.BEAUTYCHAT_FRAGMENT, beautyChatFragment);
        }

        if (otherServerFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, Constants.OTHERSERVER_FRAGMENT, otherServerFragment);
        }

        if (aboutFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, Constants.ABOUT_FRAGMENT, aboutFragment);
        }

        if (settingFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, Constants.SETTING_FRAGMENT, settingFragment);
        }
    }


    @Override
    public void showFragment(String fragmentTag) {
        super.showFragment(fragmentTag);
        fab.show();
        switch (fragmentTag) {
            case Constants.NEWS_FRAGMENT:
                showNewsFrament();
                break;
            case Constants.PHOTOS_FRAGMENT:
                showPhotosFragment();
                break;
            case Constants.VIDEOS_FRAGMENT:
                showVideosFragment();
                break;
            case Constants.BOOKMARKS_FRAGMENT:
                showBookMarksFragment();
                break;
            case Constants.BEAUTYCHAT_FRAGMENT:
                showBeautyChatFragment();
                fab.hide();
                break;
            case Constants.OTHERSERVER_FRAGMENT:
                showOtherServerFragment();
                fab.hide();
                break;
            case Constants.ABOUT_FRAGMENT:
                showAboutFragment();
                fab.hide();
                break;
            case Constants.SETTING_FRAGMENT:
                showSettingFragment();
                fab.hide();
                break;
        }
    }

    /**
     * 其他服务
     */
    private void showOtherServerFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(otherServerFragment);
        fragmentTransaction.hide(newsFragment);
        fragmentTransaction.hide(photosFragment);
        fragmentTransaction.hide(videosFragment);
        fragmentTransaction.hide(bookMarksFragment);
        fragmentTransaction.hide(beautyChatFragment);
        fragmentTransaction.hide(aboutFragment);
        fragmentTransaction.hide(settingFragment);
        fragmentTransaction.commit();

        toolbar.setTitle(getResources().getString(R.string.other_server));
    }

    /**
     * 设置
     */
    private void showSettingFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(settingFragment);
        fragmentTransaction.hide(newsFragment);
        fragmentTransaction.hide(photosFragment);
        fragmentTransaction.hide(videosFragment);
        fragmentTransaction.hide(bookMarksFragment);
        fragmentTransaction.hide(beautyChatFragment);
        fragmentTransaction.hide(aboutFragment);
        fragmentTransaction.hide(otherServerFragment);
        fragmentTransaction.commit();

        toolbar.setTitle(getResources().getString(R.string.settings));
    }

    /**
     * 关于
     */
    private void showAboutFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(aboutFragment);
        fragmentTransaction.hide(newsFragment);
        fragmentTransaction.hide(photosFragment);
        fragmentTransaction.hide(videosFragment);
        fragmentTransaction.hide(bookMarksFragment);
        fragmentTransaction.hide(beautyChatFragment);
        fragmentTransaction.hide(settingFragment);
        fragmentTransaction.hide(otherServerFragment);
        fragmentTransaction.commit();

        toolbar.setTitle(getResources().getString(R.string.about));
    }

    /**
     * 美女陪聊
     */
    private void showBeautyChatFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(beautyChatFragment);
        fragmentTransaction.hide(newsFragment);
        fragmentTransaction.hide(photosFragment);
        fragmentTransaction.hide(videosFragment);
        fragmentTransaction.hide(bookMarksFragment);
        fragmentTransaction.hide(aboutFragment);
        fragmentTransaction.hide(settingFragment);
        fragmentTransaction.hide(otherServerFragment);
        fragmentTransaction.commit();

        toolbar.setTitle(getResources().getString(R.string.beautychat));
    }

    /**
     * 收藏
     */
    private void showBookMarksFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(bookMarksFragment);
        fragmentTransaction.hide(newsFragment);
        fragmentTransaction.hide(photosFragment);
        fragmentTransaction.hide(videosFragment);
        fragmentTransaction.hide(beautyChatFragment);
        fragmentTransaction.hide(aboutFragment);
        fragmentTransaction.hide(settingFragment);
        fragmentTransaction.hide(otherServerFragment);
        fragmentTransaction.commit();

        toolbar.setTitle(getResources().getString(R.string.bookmarks));
    }

    /**
     * 视频
     */
    private void showVideosFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(videosFragment);
        fragmentTransaction.hide(newsFragment);
        fragmentTransaction.hide(photosFragment);
        fragmentTransaction.hide(bookMarksFragment);
        fragmentTransaction.hide(beautyChatFragment);
        fragmentTransaction.hide(aboutFragment);
        fragmentTransaction.hide(settingFragment);
        fragmentTransaction.hide(otherServerFragment);
        fragmentTransaction.commit();

        toolbar.setTitle(getResources().getString(R.string.video));
    }

    /**
     * 图片
     */
    private void showPhotosFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(photosFragment);
        fragmentTransaction.hide(newsFragment);
        fragmentTransaction.hide(bookMarksFragment);
        fragmentTransaction.hide(videosFragment);
        fragmentTransaction.hide(beautyChatFragment);
        fragmentTransaction.hide(aboutFragment);
        fragmentTransaction.hide(settingFragment);
        fragmentTransaction.hide(otherServerFragment);
        fragmentTransaction.commit();

        toolbar.setTitle(getResources().getString(R.string.photo));
    }

    /**
     * 新闻
     */
    private void showNewsFrament() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(newsFragment);
        fragmentTransaction.hide(bookMarksFragment);
        fragmentTransaction.hide(photosFragment);
        fragmentTransaction.hide(videosFragment);
        fragmentTransaction.hide(beautyChatFragment);
        fragmentTransaction.hide(aboutFragment);
        fragmentTransaction.hide(settingFragment);
        fragmentTransaction.hide(otherServerFragment);
        fragmentTransaction.commit();

        toolbar.setTitle(getResources().getString(R.string.news));
    }

    /**
     * 初始化fragment
     *
     * @param savedInstanceState
     */
    private void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            newsFragment = (NewsFragment) getSupportFragmentManager().getFragment(savedInstanceState, Constants.NEWS_FRAGMENT);
            photosFragment = (PhotosFragment) getSupportFragmentManager().getFragment(savedInstanceState, Constants.PHOTOS_FRAGMENT);
            videosFragment = (VideosFragment) getSupportFragmentManager().getFragment(savedInstanceState, Constants.VIDEOS_FRAGMENT);
            bookMarksFragment = (BookMarksFragment) getSupportFragmentManager().getFragment(savedInstanceState, Constants.BOOKMARKS_FRAGMENT);
            beautyChatFragment = (BeautyChatFragment) getSupportFragmentManager().getFragment(savedInstanceState, Constants.BEAUTYCHAT_FRAGMENT);
            aboutFragment = (AboutFragment) getSupportFragmentManager().getFragment(savedInstanceState, Constants.ABOUT_FRAGMENT);
            settingFragment = (SettingFragment) getSupportFragmentManager().getFragment(savedInstanceState, Constants.SETTING_FRAGMENT);
            otherServerFragment = (OtherServerFragment) getSupportFragmentManager().getFragment(savedInstanceState, Constants.OTHERSERVER_FRAGMENT);
        } else {
            newsFragment = NewsFragment.newInstance();
            photosFragment = PhotosFragment.newInstance();
            videosFragment = VideosFragment.newInstance();
            bookMarksFragment = BookMarksFragment.newInstance();
            beautyChatFragment = BeautyChatFragment.newInstance();
            aboutFragment = AboutFragment.newInstance();
            settingFragment = SettingFragment.newInstance();
            otherServerFragment = OtherServerFragment.newInstance();
        }

        if (!newsFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layout_fragment, newsFragment, Constants.NEWS_FRAGMENT)
                    .commit();
        }

        if (!photosFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layout_fragment, photosFragment, Constants.PHOTOS_FRAGMENT)
                    .commit();
        }

        if (!videosFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layout_fragment, videosFragment, Constants.VIDEOS_FRAGMENT)
                    .commit();
        }

        if (!bookMarksFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layout_fragment, bookMarksFragment, Constants.BOOKMARKS_FRAGMENT)
                    .commit();
        }

        if (!beautyChatFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layout_fragment, beautyChatFragment, Constants.BEAUTYCHAT_FRAGMENT)
                    .commit();
        }

        if (!otherServerFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layout_fragment, otherServerFragment, Constants.OTHERSERVER_FRAGMENT)
                    .commit();
        }

        if (!aboutFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layout_fragment, aboutFragment, Constants.ABOUT_FRAGMENT)
                    .commit();
        }

        if (!settingFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layout_fragment, settingFragment, Constants.SETTING_FRAGMENT)
                    .commit();
        }
        /**
         * 第一次加载显示新闻
         */
        showNewsFrament();
    }

    @OnClick(R.id.fab)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                RxBus.getInstance().post(new ScrollToTopEvent());
                break;
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, R.string.exit_prompt, Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, HomeActivity.class));
    }

    @Override
    public void onFolderSelection(@NonNull FolderChooserDialog dialog, @NonNull File folder) {
        RxBus.getInstance().post(new MessageEvent(dialog.getTag(), folder.getAbsolutePath()));
    }
}
