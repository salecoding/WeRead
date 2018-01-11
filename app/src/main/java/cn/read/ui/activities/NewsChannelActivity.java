package cn.read.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.nio.channels.Channel;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import cn.read.BaseActivity;
import cn.read.R;
import cn.read.bean.NewsChannelTable;
import cn.read.event.ChannelItemMoveEvent;
import cn.read.listener.OnItemClickListener;
import cn.read.ui.adapter.NewsChannelAdapter;
import cn.read.ui.presenter.impl.NewsChannelPresenterImpl;
import cn.read.ui.view.NewsChannelView;
import cn.read.utils.RxBus;
import cn.read.widget.ItemDragHelperCallback;
import rx.functions.Action1;

/**
 * Created by lw on 2017-03-07.
 */

public class NewsChannelActivity extends BaseActivity implements NewsChannelView {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.news_channel_mine_rv)
    RecyclerView mNewsChannelMineRv;
    @Bind(R.id.news_channel_more_rv)
    RecyclerView mNewsChannelMoreRv;

    @Inject
    NewsChannelPresenterImpl mNewsChannelPresenter;

    private NewsChannelAdapter mNewsChannelAdapterMine;
    private NewsChannelAdapter mNewsChannelAdapterMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubscription = RxBus.getInstance().toObservable(ChannelItemMoveEvent.class)
                .subscribe(new Action1<ChannelItemMoveEvent>() {
                    @Override
                    public void call(ChannelItemMoveEvent channelItemMoveEvent) {
                        int fromPosition = channelItemMoveEvent.getFromPosition();
                        int toPosition = channelItemMoveEvent.getToPosition();
                        mNewsChannelPresenter.onItemSwap(fromPosition, toPosition);
                    }
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_channel;
    }

    @Override
    public void initViews() {
        mPresenter = mNewsChannelPresenter;
        mPresenter.attachView(this);
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initRecyclerViews(List<NewsChannelTable> newsChannelsMine, List<NewsChannelTable> newsChannelsMore) {
        initRecyclerViewMineAndMore(newsChannelsMine, newsChannelsMore);
    }

    private void initRecyclerViewMineAndMore(List<NewsChannelTable> newsChannelsMine, List<NewsChannelTable> newsChannelsMore) {
        initRecyclerView(mNewsChannelMineRv, newsChannelsMine, true);
        initRecyclerView(mNewsChannelMoreRv, newsChannelsMore, false);
    }

    private void initRecyclerView(RecyclerView recyclerView, List<NewsChannelTable> newsChannels
            , final boolean isChannelMine) {
        // !!!加上这句将不能动态增加列表大小。。。
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (isChannelMine) {
            mNewsChannelAdapterMine = new NewsChannelAdapter(newsChannels);
            recyclerView.setAdapter(mNewsChannelAdapterMine);
            setChannelMineOnItemClick();

            initItemDragHelper();
        } else {
            mNewsChannelAdapterMore = new NewsChannelAdapter(newsChannels);
            recyclerView.setAdapter(mNewsChannelAdapterMore);
            setChannelMoreOnItemClick();
        }

    }

    private void setChannelMineOnItemClick() {
        mNewsChannelAdapterMine.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NewsChannelTable newsChannel = mNewsChannelAdapterMine.getData().get(position);
                boolean isNewsChannelFixed = newsChannel.getNewsChannelFixed();
                if (!isNewsChannelFixed) {
                    mNewsChannelAdapterMore.add(mNewsChannelAdapterMore.getItemCount(), newsChannel);
                    mNewsChannelAdapterMine.delete(position);

                    mNewsChannelPresenter.onItemAddOrRemove(newsChannel, true);
                }
            }
        });
    }

    private void setChannelMoreOnItemClick() {
        mNewsChannelAdapterMore.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NewsChannelTable newsChannel = mNewsChannelAdapterMore.getData().get(position);
                mNewsChannelAdapterMine.add(mNewsChannelAdapterMine.getItemCount(), newsChannel);
                mNewsChannelAdapterMore.delete(position);

                mNewsChannelPresenter.onItemAddOrRemove(newsChannel, false);

            }
        });
    }

    private void initItemDragHelper() {
        ItemDragHelperCallback itemDragHelperCallback = new ItemDragHelperCallback(mNewsChannelAdapterMine);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragHelperCallback);
        itemTouchHelper.attachToRecyclerView(mNewsChannelMineRv);

        mNewsChannelAdapterMine.setItemDragHelperCallback(itemDragHelperCallback);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String message) {

    }
}
