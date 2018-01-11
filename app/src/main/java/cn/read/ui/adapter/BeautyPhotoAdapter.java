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
import cn.read.listener.OnItemClickListener;
import cn.read.utils.StringUtils;

/**
 * Created by lw on 2017/1/16.
 */

public class BeautyPhotoAdapter extends BaseRecyclerViewAdapter<BeautyPhotoInfo> {
    private final int mPhotoWidth;

    public interface OnBeautyPhotoListItemClickListener extends OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Inject
    public BeautyPhotoAdapter() {
        super(null);
        int widthPixels = App.getContext().getResources().getDisplayMetrics().widthPixels;
        int marginPixels = App.getContext().getResources().getDimensionPixelOffset(R.dimen.photo_margin_width);
        mPhotoWidth = widthPixels / 2 - marginPixels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view;
        switch (viewType) {
            case TYPE_FOOTER:
                view = getView(parent, R.layout.item_footer);
                return new FooterViewHolder(view);
            case TYPE_ITEM:
                view = getView(parent, R.layout.item_beauty_photo);
                final BeautyPhotoViewHolder beautyPhotoViewHolder = new BeautyPhotoViewHolder(view);
                setItemOnClickEvent(beautyPhotoViewHolder);
                return beautyPhotoViewHolder;
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
        if (holder instanceof BeautyPhotoViewHolder) {
            BeautyPhotoViewHolder beautyPhotoViewHolder = (BeautyPhotoViewHolder) holder;
            BeautyPhotoInfo beautyPhotoInfo = mList.get(position);
            int photoHeight = StringUtils.calcPhotoHeight(beautyPhotoInfo.getPixel(), mPhotoWidth);
            // 接口返回的数据有像素分辨率，根据这个来缩放图片大小
            final ViewGroup.LayoutParams params = beautyPhotoViewHolder.ivPhoto.getLayoutParams();
            params.width = mPhotoWidth;
            params.height = photoHeight;
            beautyPhotoViewHolder.ivPhoto.setLayoutParams(params);
            Glide.with(App.getContext())
                    .load(beautyPhotoInfo.getImgsrc())
                    .fitCenter()
                    .dontAnimate()
                    .into(beautyPhotoViewHolder.ivPhoto);
            beautyPhotoViewHolder.tvTitle.setText(beautyPhotoInfo.getTitle());
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

    class BeautyPhotoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_photo)
        ImageView ivPhoto;
        @Bind(R.id.tv_title)
        TextView tvTitle;

        public BeautyPhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
