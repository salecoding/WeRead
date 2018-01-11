package cn.read.ui.activities.otherserver;

import cn.read.BaseActivity;
import cn.read.R;

/**
 * Created by lw on 2017-03-17.
 * 身份证查询
 */

public class IdCardInquiriesActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_id_card_inquiries;
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }
}
