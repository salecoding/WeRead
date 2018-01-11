package cn.read.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import cn.read.BaseActivity;
import cn.read.R;
import cn.read.bean.VideosChannelTable;
import cn.read.event.ChannelItemMoveEvent;
import cn.read.listener.OnItemClickListener;
import cn.read.ui.adapter.VideosChannelAdapter;
import cn.read.ui.presenter.impl.VideosChannelPresenterImpl;
import cn.read.ui.view.VideosChannelView;
import cn.read.utils.RxBus;
import cn.read.widget.ItemDragHelperCallback;
import rx.functions.Action1;

/**
 * Created by lw on 2017-03-07.
 */

public class VideosChannelActivity extends BaseActivity implements VideosChannelView {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.videos_channel_mine_rv)
    RecyclerView mVideosChannelMineRv;
    @Bind(R.id.videos_channel_more_rv)
    RecyclerView mVideosChannelMoreRv;

    @Inject
    VideosChannelPresenterImpl mVideosChannelPresenter;

    private VideosChannelAdapter mVideosChannelAdapterMine;
    private VideosChannelAdapter mVideosChannelAdapterMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubscription = RxBus.getInstance().toObservable(ChannelItemMoveEvent.class)
                .subscribe(new Action1<ChannelItemMoveEvent>() {
                    @Override
                    public void call(ChannelItemMoveEvent channelItemMoveEvent) {
                        int fromPosition = channelItemMoveEvent.getFromPosition();
                        int toPosition = channelItemMoveEvent.getToPosition();
                        mVideosChannelPresenter.onItemSwap(fromPosition, toPosition);
                    }
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_videos_channel;
    }

    @Override
    public void initViews() {
        mPresenter = mVideosChannelPresenter;
        mPresenter.attachView(this);
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initRecyclerViews(List<VideosChannelTable> videosChannelsMine, List<VideosChannelTable> videosChannelsMore) {
        initRecyclerViewMineAndMore(videosChannelsMine, videosChannelsMore);
    }

    private void initRecyclerViewMineAndMore(List<VideosChannelTable> videosChannelsMine, List<VideosChannelTable> videosChannelsMore) {
        initRecyclerView(mVideosChannelMineRv, videosChannelsMine, true);
        initRecyclerView(mVideosChannelMoreRv, videosChannelsMore, false);
    }

    private void initRecyclerView(RecyclerView recyclerView, List<VideosChannelTable> videosChannelTables
            , final boolean isChannelMine) {
        // !!!加上这句将不能动态增加列表大小。。。
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (isChannelMine) {
            mVideosChannelAdapterMine = new VideosChannelAdapter(videosChannelTables);
            recyclerView.setAdapter(mVideosChannelAdapterMine);
            setChannelMineOnItemClick();

            initItemDragHelper();
        } else {
            mVideosChannelAdapterMore = new VideosChannelAdapter(videosChannelTables);
            recyclerView.setAdapter(mVideosChannelAdapterMore);
            setChannelMoreOnItemClick();
        }

    }

    private void setChannelMineOnItemClick() {
        mVideosChannelAdapterMine.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                VideosChannelTable videosChannelTable = mVideosChannelAdapterMine.getData().get(position);
                boolean isNewsChannelFixed = videosChannelTable.getVideosChannelFixed();
                if (!isNewsChannelFixed) {
                    mVideosChannelAdapterMore.add(mVideosChannelAdapterMore.getItemCount(), videosChannelTable);
                    mVideosChannelAdapterMine.delete(position);

                    mVideosChannelPresenter.onItemAddOrRemove(videosChannelTable, true);
                }
            }
        });
    }

    private void setChannelMoreOnItemClick() {
        mVideosChannelAdapterMore.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                VideosChannelTable videosChannelTable = mVideosChannelAdapterMore.getData().get(position);
                mVideosChannelAdapterMine.add(mVideosChannelAdapterMine.getItemCount(), videosChannelTable);
                mVideosChannelAdapterMore.delete(position);

                mVideosChannelPresenter.onItemAddOrRemove(videosChannelTable, false);

            }
        });
    }

    private void initItemDragHelper() {
        ItemDragHelperCallback itemDragHelperCallback = new ItemDragHelperCallback(mVideosChannelAdapterMine);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragHelperCallback);
        itemTouchHelper.attachToRecyclerView(mVideosChannelMineRv);

        mVideosChannelAdapterMine.setItemDragHelperCallback(itemDragHelperCallback);
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
