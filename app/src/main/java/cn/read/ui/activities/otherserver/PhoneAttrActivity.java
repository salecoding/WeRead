package cn.read.ui.activities.otherserver;

import cn.read.BaseActivity;
import cn.read.R;

/**
 * Created by lw on 2017-03-17.
 * 手机号码归属地
 */

public class PhoneAttrActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_phone_attr;
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }
}
