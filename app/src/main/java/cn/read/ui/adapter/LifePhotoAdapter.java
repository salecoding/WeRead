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
import cn.read.bean.PhotoInfo;
import cn.read.listener.OnItemClickListener;

/**
 * Created by lw on 2017/1/16.
 */

public class LifePhotoAdapter extends BaseRecyclerViewAdapter<PhotoInfo> {

    public interface OnLifePhotoListItemClickListener extends OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Inject
    public LifePhotoAdapter() {
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
                view = getView(parent, R.layout.item_life_photo);
                final LifePhotoViewHolder lifePhotoViewHolder = new LifePhotoViewHolder(view);
                setItemOnClickEvent(lifePhotoViewHolder);
                return lifePhotoViewHolder;
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
        if (holder instanceof LifePhotoViewHolder) {
            LifePhotoViewHolder lifePhotoViewHolder = (LifePhotoViewHolder) holder;
            PhotoInfo photoInfo = mList.get(position);
            Glide.with(App.getContext()).load(photoInfo.getPics().get(0)).centerCrop().dontAnimate().into(lifePhotoViewHolder.ivPhoto1);
            Glide.with(App.getContext()).load(photoInfo.getPics().get(1)).centerCrop().dontAnimate().into(lifePhotoViewHolder.ivPhoto2);
            Glide.with(App.getContext()).load(photoInfo.getPics().get(2)).centerCrop().dontAnimate().into(lifePhotoViewHolder.ivPhoto3);
            lifePhotoViewHolder.tvTitle.setText(photoInfo.getSetname());
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

    class LifePhotoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_photo_1)
        ImageView ivPhoto1;
        @Bind(R.id.iv_photo_2)
        ImageView ivPhoto2;
        @Bind(R.id.iv_photo_3)
        ImageView ivPhoto3;
        @Bind(R.id.tv_title)
        TextView tvTitle;

        public LifePhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
