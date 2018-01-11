package cn.read.ui.activities.otherserver;

import cn.read.BaseActivity;
import cn.read.R;

/**
 * Created by lw on 2017-03-17.
 * 火车票查询
 */

public class TrainTicketInquiriesActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_train_ticket_inquiries;
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }
}
