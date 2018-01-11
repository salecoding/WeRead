package cn.read.ui.activities.otherserver;

import cn.read.BaseActivity;
import cn.read.R;

/**
 * Created by lw on 2017-03-17.
 * 基站定位查询
 */

public class BaseStationLocationActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_base_station_location;
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }
}
