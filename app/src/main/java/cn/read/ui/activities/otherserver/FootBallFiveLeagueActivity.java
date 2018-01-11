package cn.read.ui.activities.otherserver;

import cn.read.BaseActivity;
import cn.read.R;

/**
 * Created by lw on 2017-03-17.
 * 足球五大联赛
 */

public class FootBallFiveLeagueActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_football_five_league;
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }
}
