package cn.read.ui.adapter;

import android.media.Image;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.read.R;
import cn.read.base.App;
import cn.read.bean.VideosSummary;
import cn.read.common.Constants;
import cn.read.event.MessageEvent;
import cn.read.listener.OnItemClickListener;
import cn.read.utils.RxBus;
import cn.read.utils.ShareUtils;
import cn.read.utils.SnackbarUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by lw on 2017/1/16.
 */

public class VideosListAdapter extends BaseRecyclerViewAdapter<VideosSummary> {

    public interface OnVideosListItemClickListener extends OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Inject
    public VideosListAdapter() {
        super(null);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view;
        switch (viewType) {
            case TYPE_FOOTER:
                view = getView(parent, R.layout.item_footer);
                return new FooterViewHolder(view);
            case TYPE_ITEM:
                view = getView(parent, R.layout.item_videos);
                final VideosViewHolder videosViewHolder = new VideosViewHolder(view);
                setItemOnClickEvent(videosViewHolder);
                return videosViewHolder;
            default:
                throw new RuntimeException("there is no type that matches the type " +
                        viewType + " + make sure your using types correctly");
        }
    }

    private void setItemOnClickEvent(final RecyclerView.ViewHolder holder) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, holder.getLayoutPosition());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mIsShowFooter && isFooterPosition(position)) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        setVideos(holder, position);
        setItemAppearAnimation(holder, position, R.anim.anim_bottom_in);
    }

    private void setVideos(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VideosViewHolder) {
            final VideosViewHolder videosViewHolder = (VideosViewHolder) holder;
            final JCVideoPlayerStandard videoplayer = videosViewHolder.videoplayer;
            final VideosSummary videosSummary = mList.get(position);
            videoplayer.titleTextView.setText(videosSummary.getTitle());
            Glide.with(App.getContext()).load(videosSummary.getCover()).into(videoplayer.thumbImageView);
            videoplayer.thumbImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(App.getContext()).load(videosSummary.getTopicImg()).into(videosViewHolder.ivAvatar);
            videosViewHolder.tvFrom.setText(videosSummary.getVideosource());
            videosViewHolder.tvCommentCount.setText(String.valueOf(videosSummary.getPlayCount()));
            videoplayer.setUp(videosSummary.getMp4_url(), JCVideoPlayer.SCREEN_LAYOUT_LIST);
            videosViewHolder.ivFavorite.setSelected(videosSummary.getIsFavorites());
            videosViewHolder.ivShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareUtils.share(videosSummary.getTitle(), videosSummary.getMp4_url(),
                            videosSummary.getDescription(), videosSummary.getCover(),
                            videosSummary.getMp4_url());
                }
            });
            videosViewHolder.ivFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isSelected = !v.isSelected();
                    v.setSelected(isSelected);
                    if (isSelected) {
                        videosSummary.setIsFavorites(isSelected);
                        App.getVideosSummaryDao().insertOrReplace(videosSummary);
                        Snackbar.make(videosViewHolder.itemView, R.string.added_to_collection, Snackbar.LENGTH_SHORT).show();
                    } else {
                        App.getVideosSummaryDao().delete(videosSummary);
                        Snackbar.make(videosViewHolder.itemView, R.string.deled_to_collection, Snackbar.LENGTH_SHORT).show();
                    }
                    RxBus.getInstance().post(new MessageEvent(Constants.BOOKMARKS_VIDEO, new String[]{Constants.BOOKMARKS_VIDEO_ID, Constants.BOOKMARKS_VIDEO}));
                }
            });
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (isShowingAnimation(holder)) {
            holder.itemView.clearAnimation();
        }
    }

    private boolean isShowingAnimation(RecyclerView.ViewHolder holder) {
        return holder.itemView.getAnimation() != null && holder.itemView
                .getAnimation().hasStarted();
    }


    class VideosViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.videoplayer)
        JCVideoPlayerStandard videoplayer;
        @Bind(R.id.ivAvatar)
        CircleImageView ivAvatar;
        @Bind(R.id.tvFrom)
        TextView tvFrom;
        @Bind(R.id.tvCommentCount)
        TextView tvCommentCount;
        @Bind(R.id.ivFavorite)
        ImageView ivFavorite;
        @Bind(R.id.ivShare)
        ImageView ivShare;

        public VideosViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
