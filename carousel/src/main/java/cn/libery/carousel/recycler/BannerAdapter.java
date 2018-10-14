package cn.libery.carousel.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.util.List;

import cn.libery.carousel.Carousel;
import cn.libery.carousel.R;
import cn.libery.carousel.model.Banner;
import cn.libery.carousel.view.SampleVideo;

import static cn.libery.carousel.model.Banner.CUSTOM;
import static cn.libery.carousel.model.Banner.IMAGE;
import static cn.libery.carousel.model.Banner.VIDEO;

/**
 * @author shizhiqiang on 2018/8/22.
 * @description
 */
public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerHolder> {

    private Carousel carousel;
    private List<Banner> banners;
    private OnItemClickListener onItemClickListener;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public BannerAdapter(Carousel carousel, List<Banner> banners) {
        this.carousel = carousel;
        this.banners = banners;
    }

    public List<Banner> getBanners() {
        return banners;
    }

    public void setBanners(List<Banner> banners) {
        this.banners = banners;
    }

    @NonNull
    @Override
    public BannerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new BannerHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item_banner, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final @NonNull BannerHolder holder, int position) {
        Banner banner = banners.get(position % banners.size());
        switch (banner.getType()) {
            case IMAGE:
                holder.contentView.removeAllViews();
                ImageView img = new ImageView(holder.itemView.getContext());
                img.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(holder.itemView.getContext())
                        .load(banner.getUrl())
                        .into(img);
                holder.contentView.addView(img);
                break;
            case VIDEO:
                holder.contentView.removeAllViews();
                SampleVideo video = new SampleVideo(holder.itemView.getContext());
                video.setVideoUrlAndThumbUrl(banner.getUrl(), banner.getUrl());
                video.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                    @Override
                    public void onViewAttachedToWindow(View v) {
                    }

                    @Override
                    public void onViewDetachedFromWindow(View v) {
                        GSYVideoManager.onPause();
                    }
                });
                video.setOnPlayerStateListener(new SampleVideo.OnPlayerStateListener() {
                    @Override
                    public void onStart() {
                        carousel.stopExecutor();
                    }

                    @Override
                    public void onFinish() {
                        carousel.startExecutor();
                    }
                });
                holder.contentView.addView(video);
                break;
            case CUSTOM:
                holder.contentView.removeAllViews();
                holder.contentView.addView(banner.getView());
                break;
            default:
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder.getAdapterPosition() % banners.size());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public static class BannerHolder extends RecyclerView.ViewHolder {

        public FrameLayout contentView;

        public BannerHolder(@NonNull View itemView) {
            super(itemView);
            contentView = itemView.findViewById(R.id.item_banner);
        }
    }

}
