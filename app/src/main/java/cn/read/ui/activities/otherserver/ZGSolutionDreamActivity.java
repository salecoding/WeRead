package cn.read.ui.activities.otherserver;

import cn.read.BaseActivity;
import cn.read.R;

/**
 * Created by lw on 2017-03-17.
 * 周公解梦
 */

public class ZGSolutionDreamActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_zg_solution_dream;
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }
}
