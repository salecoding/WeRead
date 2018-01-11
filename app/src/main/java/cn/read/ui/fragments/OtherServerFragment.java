package cn.read.ui.fragments;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import cn.read.BaseFragment;
import cn.read.R;
import cn.read.base.App;
import cn.read.bean.ItemInfo;
import cn.read.common.Constants;
import cn.read.event.OtherShowChangeEvent;
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
import cn.read.ui.adapter.OtherServerAdapter;
import cn.read.utils.RxBus;
import cn.read.utils.SharedPreferencesUtil;
import cn.read.widget.LineRecyclerView;
import rx.functions.Action1;

/**
 * Created by lw on 2017-03-16.
 */

public class OtherServerFragment extends BaseFragment implements OtherServerAdapter.OnOtherServerItemClickListener {
    @Bind(R.id.other_rv)
    LineRecyclerView otherRv;
    private List<ItemInfo> itemInfos = new ArrayList<>();
    private List<Class> activities = new ArrayList<>();
    private OtherServerAdapter otherServerAdapter;
    private boolean isGrid;

    public static OtherServerFragment newInstance() {
        return new OtherServerFragment();
    }

    @Override
    public void initViews(View view) {
        initData();
        initRecyclerView();
        registerOtherShowChangeEvent();
    }

    private void initData() {
        List<String> operIds = Arrays.asList(App.getContext().getResources()
                .getStringArray(R.array.other_oper_id));
        List<String> operNames = Arrays.asList(App.getContext().getResources()
                .getStringArray(R.array.other_oper_name));
        TypedArray typedArray = getResources().obtainTypedArray(R.array.other_oper_icon);
        for (int i = 0; i < operIds.size(); i++) {
            itemInfos.add(new ItemInfo(operIds.get(i),
                    typedArray.getResourceId(i, R.drawable.ic_weather), operNames.get(i)));
        }
        activities.add(WeatherActivity.class);
        activities.add(PhoneAttrActivity.class);
        activities.add(ZipCodeActivity.class);
        activities.add(RecipeActivity.class);
        activities.add(BaseStationLocationActivity.class);
        activities.add(IdCardInquiriesActivity.class);
        activities.add(TheQuestionBankActivity.class);
        activities.add(TrainTicketInquiriesActivity.class);
        activities.add(FootBallFiveLeagueActivity.class);
        activities.add(ZGSolutionDreamActivity.class);
        /**
         * 是否是网格显示
         */
        isGrid = SharedPreferencesUtil.getBoolean(getContext(), Constants.ISGRID, false);
    }

    private void registerOtherShowChangeEvent() {
        mSubscription = RxBus.getInstance().toObservable(OtherShowChangeEvent.class)
                .subscribe(new Action1<OtherShowChangeEvent>() {
                    @Override
                    public void call(OtherShowChangeEvent otherShowChangeEvent) {
                        isGrid = SharedPreferencesUtil.getBoolean(getContext(), Constants.ISGRID, false);
                        initRecyclerView();
                    }
                });
    }

    private void initRecyclerView() {
        otherServerAdapter = new OtherServerAdapter(getContext(), itemInfos, isGrid);
        otherServerAdapter.setOnOtherServerItemClickListener(this);
        if (isGrid) {
            otherRv.setDrawLine(true);
            otherRv.setLayoutManager(new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false));
        } else {
            otherRv.setDrawLine(false);
            otherRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        }
        otherRv.setAdapter(otherServerAdapter);
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_other_server;
    }

    @Override
    public void onItemClick(View view, int position) {
        startActivity(new Intent(getContext(), getActivity(position)));
    }

    private Class getActivity(int position) {
        return activities.get(position) == null ? WeatherActivity.class : activities.get(position);
    }
}
