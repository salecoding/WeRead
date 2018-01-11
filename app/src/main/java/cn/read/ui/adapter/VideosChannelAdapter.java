package cn.read.ui.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.read.R;
import cn.read.base.App;
import cn.read.bean.VideosChannelTable;
import cn.read.event.ChannelItemMoveEvent;
import cn.read.listener.OnItemClickListener;
import cn.read.utils.ClickUtil;
import cn.read.utils.MyUtils;
import cn.read.utils.RxBus;
import cn.read.widget.ItemDragHelperCallback;

/**
 * Created by lw on 2017/1/19.
 */

public class VideosChannelAdapter extends BaseRecyclerViewAdapter<VideosChannelTable> implements
        ItemDragHelperCallback.OnItemMoveListener {
    private static final int TYPE_CHANNEL_FIXED = 0;
    private static final int TYPE_CHANNEL_NO_FIXED = 1;

    private ItemDragHelperCallback mItemDragHelperCallback;

    private OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setItemDragHelperCallback(ItemDragHelperCallback itemDragHelperCallback) {
        mItemDragHelperCallback = itemDragHelperCallback;
    }

    public VideosChannelAdapter(List<VideosChannelTable> videosChannelTables) {
        super(videosChannelTables);
    }

    public List<VideosChannelTable> getData() {
        return mList;
    }

    @Override
    public VideosChannelViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_videos_channel, parent, false);
        final VideosChannelViewHolder videosChannelViewHolder = new VideosChannelViewHolder(view);
        handleLongPress(videosChannelViewHolder);
        handleOnClick(videosChannelViewHolder);
        return videosChannelViewHolder;
    }

    private void handleLongPress(final VideosChannelViewHolder videosChannelViewHolder) {
        if (mItemDragHelperCallback != null) {
            videosChannelViewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    VideosChannelTable videosChannelTable = mList.get(videosChannelViewHolder.getLayoutPosition());
                    boolean isChannelFixed = videosChannelTable.getVideosChannelFixed();
                    if (isChannelFixed) {
                        mItemDragHelperCallback.setLongPressEnabled(false);
                    } else {
                        mItemDragHelperCallback.setLongPressEnabled(true);
                    }
                    return false;
                }
            });
        }
    }

    private void handleOnClick(final VideosChannelViewHolder videosChannelViewHolder) {
        if (mOnItemClickListener != null) {
            videosChannelViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ClickUtil.isFastDoubleClick()) {
                        mOnItemClickListener.onItemClick(v, videosChannelViewHolder.getLayoutPosition());
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final VideosChannelTable videosChannelTable = mList.get(position);
        String videosChannelName = videosChannelTable.getVideosChannelName();
        VideosChannelViewHolder viewHolder = (VideosChannelViewHolder) holder;
        viewHolder.mVideosChannelTv.setText(videosChannelName);

        if (videosChannelTable.getVideosChannelIndex() == 0) {
            viewHolder.mVideosChannelTv.setTextColor(ContextCompat
                    .getColor(App.getContext(), getColorId()));
        }
    }

    private int getColorId() {
        int colorId;
        if (MyUtils.isNightMode()) {
            colorId = R.color.alpha_40_white;
        } else {
            colorId = R.color.alpha_40_black;
        }
        return colorId;
    }

    @Override
    public int getItemViewType(int position) {
        Boolean isFixed = mList.get(position).getVideosChannelFixed();
        if (isFixed) {
            return TYPE_CHANNEL_FIXED;
        } else {
            return TYPE_CHANNEL_NO_FIXED;
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (isChannelFixed(fromPosition, toPosition)) {
            return false;
        }
        Collections.swap(mList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        RxBus.getInstance().post(new ChannelItemMoveEvent(fromPosition, toPosition));
        return true;
    }

    private boolean isChannelFixed(int fromPosition, int toPosition) {
        return mList.get(fromPosition).getVideosChannelFixed() ||
                mList.get(toPosition).getVideosChannelFixed();
    }

    class VideosChannelViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.videos_channel_tv)
        TextView mVideosChannelTv;

        public VideosChannelViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
