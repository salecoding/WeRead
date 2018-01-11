package cn.read.di.component;

import android.content.Context;

import cn.read.di.module.ServiceModule;
import cn.read.di.scope.ContextLife;
import cn.read.di.scope.PerService;
import dagger.Component;

/**
 * Created by lw on 2017/1/19.
 */
@PerService
@Component(dependencies = ApplicationComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {
    @ContextLife("Service")
    Context getServiceContext();

    @ContextLife("Application")
    Context getApplicationContext();
}
