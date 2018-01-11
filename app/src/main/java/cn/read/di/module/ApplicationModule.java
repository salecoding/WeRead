package cn.read.di.module;

import android.content.Context;

import cn.read.base.App;
import cn.read.di.scope.ContextLife;
import cn.read.di.scope.PerApp;
import dagger.Module;
import dagger.Provides;

/**
 * Created by lw on 2017/1/19.
 */
@Module
public class ApplicationModule {
    private App mApplication;

    public ApplicationModule(App application) {
        mApplication = application;
    }

    @Provides
    @PerApp
    @ContextLife("Application")
    public Context provideApplicationContext() {
        return mApplication.getApplicationContext();
    }
}
