package cn.read;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;
import cn.read.base.App;
import cn.read.di.component.DaggerFragmentComponent;
import cn.read.di.component.FragmentComponent;
import cn.read.di.module.FragmentModule;
import cn.read.ui.presenter.BasePresenter;
import cn.read.utils.MyUtils;
import rx.Subscription;

/**
 * Created by lw on 2017-03-06.
 */

public abstract class BaseFragment<T extends BasePresenter> extends Fragment {
    protected FragmentComponent mFragmentComponent;
    protected T mPresenter;
    private View mFragmentView;
    protected Subscription mSubscription;
    public abstract void initViews(View view);
    public abstract void initInjector();
    public abstract int getLayoutId();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentComponent = DaggerFragmentComponent.builder()
                .applicationComponent(((App) getActivity().getApplication()).getApplicationComponent())
                .fragmentModule(new FragmentModule(this))
                .build();
        initInjector();

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mFragmentView == null) {
            mFragmentView = inflater.inflate(getLayoutId(), container, false);
            ButterKnife.bind(this, mFragmentView);
            initViews(mFragmentView);
        }
        return mFragmentView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = App.getRefWatcher(getActivity());
        refWatcher.watch(this);
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        MyUtils.cancelSubscription(mSubscription);
    }
}
