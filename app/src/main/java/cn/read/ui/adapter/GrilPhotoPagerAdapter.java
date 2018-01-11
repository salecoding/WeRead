package cn.read.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import cn.read.R;
import cn.read.bean.BeautyPhotoInfo;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by lw on 2017/3/11.
 */

public class GrilPhotoPagerAdapter extends PagerAdapter {
    // 限制 Adapter 在倒数第3个位置时启动加载更多回调
    private final static int LOAD_MORE_LIMIT = 3;
    private List<BeautyPhotoInfo> mImgList;
    private OnTapListener mTapListener;
    private OnLoadMoreListener mLoadMoreListener;
    private boolean mIsLoadMore = false;
    private Context mContext;

    @Inject
    public GrilPhotoPagerAdapter() {
        super();
    }

    public void updateData(List<BeautyPhotoInfo> imgList) {
        this.mImgList = imgList;
        notifyDataSetChanged();
    }

    public void addData(List<BeautyPhotoInfo> imgList) {
        mImgList.addAll(imgList);
        notifyDataSetChanged();
        mIsLoadMore = false;
    }

    public void addData(int postion, List<BeautyPhotoInfo> imgList) {
        mImgList.addAll(postion, imgList);
        notifyDataSetChanged();
        mIsLoadMore = false;
    }

    @Override
    public int getCount() {
        if (mImgList == null) {
            return 0;
        }
        return mImgList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        mContext = container.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_gril_photo, null, false);
        final PhotoView photo = (PhotoView) view.findViewById(R.id.iv_photo);
        final SpinKitView loadingView = (SpinKitView) view.findViewById(R.id.loading_view);
        final TextView tvReload = (TextView) view.findViewById(R.id.tv_reload);

        if ((position >= mImgList.size() - LOAD_MORE_LIMIT) && !mIsLoadMore) {
            if (mLoadMoreListener != null) {
                mIsLoadMore = true;
                mLoadMoreListener.onLoadMore();
            }
        }

        final RequestListener<String, GlideDrawable> requestListener = new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                loadingView.setVisibility(View.GONE);
                tvReload.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                loadingView.setVisibility(View.GONE);
                tvReload.setVisibility(View.GONE);
                photo.setImageDrawable(resource);
                return true;
            }
        };
        Glide.with(mContext)
                .load(mImgList.get(position % mImgList.size())
                        .getImgsrc())
                .centerCrop()
                .dontAnimate()
                .listener(requestListener)
                .into(photo);
        photo.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (mTapListener != null) {
                    mTapListener.onPhotoClick();
                }
            }

            @Override
            public void onOutsidePhotoTap() {

            }
        });
        tvReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvReload.setVisibility(View.GONE);
                loadingView.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(mImgList.get(position % mImgList.size())
                                .getImgsrc())
                        .centerCrop()
                        .dontAnimate()
                        .listener(requestListener)
                        .into(photo);
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * 是否收藏
     *
     * @param position
     * @return
     */
    public boolean isLoved(int position) {
        return mImgList.get(position).isLove();
    }

    /**
     * 是否点赞
     *
     * @param position
     * @return
     */
    public boolean isPraise(int position) {
        return mImgList.get(position).isPraise();
    }

    /**
     * 是否下载
     *
     * @param position
     * @return
     */
    public boolean isDownload(int position) {
        return mImgList.get(position).isDownload();
    }

    /**
     * 获取对应数据
     *
     * @param position
     * @return
     */
    public BeautyPhotoInfo getData(int position) {
        return mImgList.get(position);
    }

    public BeautyPhotoInfo getData(String url) {
        for (BeautyPhotoInfo bean : mImgList) {
            if (bean.getImgsrc().equals(url)) {
                return bean;
            }
        }
        return null;
    }

    public void setTapListener(OnTapListener listener) {
        mTapListener = listener;
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    public interface OnTapListener {
        void onPhotoClick();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
