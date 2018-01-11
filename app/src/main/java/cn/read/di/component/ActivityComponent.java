package cn.read.di.component;

import android.app.Activity;
import android.content.Context;

import cn.read.di.module.ActivityModule;
import cn.read.di.scope.ContextLife;
import cn.read.di.scope.PerActivity;
import cn.read.ui.activities.GrilPhotoDetailActivity;
import cn.read.ui.activities.HomeActivity;
import cn.read.ui.activities.NewsChannelActivity;
import cn.read.ui.activities.NewsDetailActivity;
import cn.read.ui.activities.NewsPhotoDetailActivity;
import cn.read.ui.activities.NewsPhotoSetActivity;
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
import dagger.Component;

/**
 * Created by lw on 2017/1/19.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(HomeActivity homeActivity);

    void inject(NewsDetailActivity newsDetailActivity);

    void inject(NewsChannelActivity newsChannelActivity);

    void inject(VideosChannelActivity videosChannelActivity);

    void inject(NewsPhotoDetailActivity newsPhotoDetailActivity);

    void inject(GrilPhotoDetailActivity girlPhotoDetailActivity);

    void inject(NewsPhotoSetActivity newsPhotoSetActivity);

    void inject(WeatherActivity weatherActivity);

    void inject(PhoneAttrActivity phoneAttrActivity);

    void inject(ZipCodeActivity zipCodeActivity);

    void inject(ZGSolutionDreamActivity zgSolutionDreamActivity);

    void inject(TrainTicketInquiriesActivity trainTicketInquiriesActivity);

    void inject(RecipeActivity recipeActivity);

    void inject(IdCardInquiriesActivity idCardInquiriesActivity);

    void inject(FootBallFiveLeagueActivity footBallFiveLeagueActivity);

    void inject(BaseStationLocationActivity baseStationLocationActivity);

    void inject(TheQuestionBankActivity theQuestionBankActivity);
}
