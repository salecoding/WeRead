package cn.read.di.component;

import android.content.Context;

import cn.read.di.module.ApplicationModule;
import cn.read.di.scope.ContextLife;
import cn.read.di.scope.PerApp;
import dagger.Component;

/**
 * Created by lw on 2017/1/19.
 */
@PerApp
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    @ContextLife("Application")
    Context getApplication();
}