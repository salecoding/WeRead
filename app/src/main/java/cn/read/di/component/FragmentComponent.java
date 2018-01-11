package cn.read.di.component;

import android.app.Activity;
import android.content.Context;

import cn.read.di.module.FragmentModule;
import cn.read.di.scope.ContextLife;
import cn.read.di.scope.PerFragment;
import cn.read.ui.fragments.BeautyPhotoFragment;
import cn.read.ui.fragments.BookMarksFragment;
import cn.read.ui.fragments.BookMarksListFragment;
import cn.read.ui.fragments.LifePhotoFragment;
import cn.read.ui.fragments.NewsFragment;
import cn.read.ui.fragments.NewsListFragment;
import cn.read.ui.fragments.OtherServerFragment;
import cn.read.ui.fragments.PhotosFragment;
import cn.read.ui.fragments.VideosFragment;
import cn.read.ui.fragments.VideosListFragment;
import cn.read.ui.fragments.WelfarePhotoFragment;
import dagger.Component;

/**
 * Created by lw on 2017/1/19.
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(NewsListFragment newsListFragment);

    void inject(NewsFragment newsFragment);

    void inject(BookMarksFragment bookMarksFragment);

    void inject(PhotosFragment photosFragment);

    void inject(VideosFragment videosFragment);

    void inject(VideosListFragment videosListFragment);

    void inject(BeautyPhotoFragment beautyPhotoFragment);

    void inject(WelfarePhotoFragment welfarePhotoFragment);

    void inject(LifePhotoFragment lifePhotoFragment);

    void inject(OtherServerFragment otherServerFragment);

    void inject(BookMarksListFragment bookMarksListFragment);
}
