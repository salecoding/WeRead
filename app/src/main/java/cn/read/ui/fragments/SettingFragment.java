package cn.read.ui.fragments;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;
import com.bumptech.glide.Glide;

import cn.read.R;
import cn.read.common.Constants;
import cn.read.db.NewsChannelTableManager;
import cn.read.db.VideosChannelTableManager;
import cn.read.event.MessageEvent;
import cn.read.ui.activities.HomeActivity;
import cn.read.utils.MyUtils;
import cn.read.utils.RxBus;
import cn.read.utils.SharedPreferencesUtil;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by lw on 2017-03-13.
 */
@RuntimePermissions
public class SettingFragment extends PreferenceFragmentCompat {
    private Subscription mSubscription;
    private Preference setting_no_image, setting_english, setting_image_save_path, setting_video_save_path, clear_glide_cache;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    private HomeActivity homeActivity;
    public static final int CLEAR_GLIDE_CACHE_DONE = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CLEAR_GLIDE_CACHE_DONE:
                    Snackbar.make(homeActivity.getWindow().getDecorView(), R.string.clear_cache_success, Snackbar.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preference_fragment);
        registerMessageEvent();
        homeActivity = (HomeActivity) getActivity();
        setting_no_image = findPreference("setting_no_image");
        setting_english = findPreference("setting_english");
        setting_image_save_path = findPreference("setting_image_save_path");
        setting_video_save_path = findPreference("setting_video_save_path");
        clear_glide_cache = findPreference("clear_glide_cache");
        setting_no_image.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                return true;
            }
        });
        setting_english.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isSettingEnglish = (boolean) newValue;
                MyUtils.changeLanguage(getContext(), isSettingEnglish);
                //更新数据库语言
                NewsChannelTableManager.initDB(true);
                VideosChannelTableManager.initDB(true);
                homeActivity.getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                homeActivity.recreate();
                return true;
            }
        });
        setting_image_save_path.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SettingFragmentPermissionsDispatcher.selectImageSavePathWithCheck(SettingFragment.this);
                return true;
            }
        });
        setting_video_save_path.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SettingFragmentPermissionsDispatcher.selectVideoSavePathWithCheck(SettingFragment.this);
                return true;
            }
        });
        clear_glide_cache.setOnPreferenceClickListener(new android.support.v7.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(android.support.v7.preference.Preference preference) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(getContext()).clearDiskCache();
                        Message msg = new Message();
                        msg.what = CLEAR_GLIDE_CACHE_DONE;
                        handler.sendMessage(msg);
                    }
                }).start();
                Glide.get(getContext()).clearMemory();
                return true;
            }
        });

        String imageSavePath = SharedPreferencesUtil.getString(getContext(), "image_save_path", Constants.IMAGE_SAVE_PATH);
        String videoSavePath = SharedPreferencesUtil.getString(getContext(), "video_save_path", Constants.VIDEO_SAVE_PATH);
        setting_image_save_path.setSummary(imageSavePath);
        setting_video_save_path.setSummary(videoSavePath);
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE})
    void selectVideoSavePath() {
        selectSavePath("video_save_path");
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE})
    void selectImageSavePath() {
        selectSavePath("image_save_path");
    }

    private void selectSavePath(String tag) {
        new FolderChooserDialog.Builder(homeActivity)
                .chooseButton(R.string.md_choose_label)
                .allowNewFolder(true, 0)
                .tag(tag)
                .show();
    }

    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE})
    void selectSavePathRationale(PermissionRequest request) {
        request.proceed(); // 提示用户权限使用的对话框
    }

    private void registerMessageEvent() {
        mSubscription = RxBus.getInstance().toObservable(MessageEvent.class)
                .subscribe(new Action1<MessageEvent>() {
                    @Override
                    public void call(MessageEvent messageEvent) {
                        if (messageEvent.getTag().equals("video_save_path")) {
                            setting_video_save_path.setSummary(messageEvent.getMessage().toString());
                            SharedPreferencesUtil.setString(getContext(), "video_save_path", messageEvent.getMessage().toString());
                        } else if (messageEvent.getTag().equals("image_save_path")) {
                            setting_image_save_path.setSummary(messageEvent.getMessage().toString());
                            SharedPreferencesUtil.setString(getContext(), "image_save_path", messageEvent.getMessage().toString());
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        SettingFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyUtils.cancelSubscription(mSubscription);
    }
}
