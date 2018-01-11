package cn.read.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.read.R;
import cn.read.base.App;
import cn.read.bean.BeautyPhotoInfo;
import cn.read.bean.WelfarePhotoList;
import cn.read.listener.OnItemClickListener;
import cn.read.utils.StringUtils;

/**
 * Created by lw on 2017/1/16.
 */

public class WelfarePhotoAdapter extends BaseRecyclerViewAdapter<WelfarePhotoList.ResultsBean> {

    public interface OnWelfarePhotoListItemClickListener extends OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Inject
    public WelfarePhotoAdapter() {
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
                view = getView(parent, R.layout.item_welfare_photo);
                final WelfatePhotoViewHolder welfatePhotoViewHolder = new WelfatePhotoViewHolder(view);
                setItemOnClickEvent(welfatePhotoViewHolder);
                return welfatePhotoViewHolder;
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
        if (holder instanceof WelfatePhotoViewHolder) {
            WelfatePhotoViewHolder beautyPhotoViewHolder = (WelfatePhotoViewHolder) holder;
            WelfarePhotoList.ResultsBean welfarePhotoList = mList.get(position);
            Glide.with(App.getContext())
                    .load(welfarePhotoList.getUrl())
                    .centerCrop()
                    .dontAnimate()
                    .into(beautyPhotoViewHolder.ivPhoto);
            beautyPhotoViewHolder.tvPublishedAt.setText(welfarePhotoList.getPublishedAt());
            setItemAppearAnimation(holder, position, R.anim.anim_bottom_in);
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

    class WelfatePhotoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_photo)
        ImageView ivPhoto;
        @Bind(R.id.tv_publishedAt)
        TextView tvPublishedAt;

        public WelfatePhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
