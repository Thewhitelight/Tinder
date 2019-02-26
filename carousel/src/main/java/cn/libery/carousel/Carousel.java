package cn.libery.carousel;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.libery.carousel.model.Banner;
import cn.libery.carousel.recycler.BannerAdapter;
import cn.libery.carousel.recycler.OnItemClickListener;
import cn.libery.carousel.recycler.SmoothLinearLayoutManager;
import cn.libery.carousel.view.Indicator;

/**
 * @author shizhiqiang on 2018/8/22.
 * @description
 */
public class Carousel extends FrameLayout {

    public static final int MSG_WHAT = 1;
    private int delaySecond = 5;
    private boolean canAutoScroll;
    private List<Banner> banners;

    private MyHandler myHandler;
    private RecyclerView bannerRecycler;
    private Indicator defaultIndicator;
    private SmoothLinearLayoutManager layoutManager;
    private OnItemClickListener onItemClickListener;
    private OnScrollPositionListener onScrollPositionListener;

    public Carousel(@NonNull Context context) {
        this(context, null);
    }

    public Carousel(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Carousel(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        myHandler = new MyHandler(this);
    }

    public List<Banner> getBanners() {
        return banners;
    }

    public void setBanners(List<Banner> banners) {
        this.banners = banners;
        initView();
    }

    public int getDelaySecond() {
        return delaySecond;
    }

    public void setDelaySecond(int delaySecond) {
        this.delaySecond = delaySecond;
        startExecutor();
    }

    public boolean isCanAutoScroll() {
        return canAutoScroll;
    }

    /**
     * @param canAutoScroll 是否自动轮播 默认true
     */
    public void setCanAutoScroll(boolean canAutoScroll) {
        this.canAutoScroll = canAutoScroll;
        if (canAutoScroll) {
            startExecutor();
        } else {
            stopExecutor();
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnScrollPositionListener(OnScrollPositionListener onScrollPositionListener) {
        this.onScrollPositionListener = onScrollPositionListener;
    }

    private void initView() {
        bannerRecycler = new RecyclerView(getContext());
        BannerAdapter adapter = new BannerAdapter(this, banners);
        layoutManager = new SmoothLinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        bannerRecycler.setLayoutManager(layoutManager);
        bannerRecycler.setHasFixedSize(true);
        bannerRecycler.setAdapter(adapter);
        bannerRecycler.scrollToPosition(banners.size() * 10);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(bannerRecycler);

        addView(bannerRecycler);
        setIndicator(null, null);

        bannerRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int i = layoutManager.findFirstVisibleItemPosition() % banners.size();
                if (defaultIndicator != null) {
                    defaultIndicator.setPosition(i);
                    if (banners.get(i).getType() == Banner.VIDEO) {
                        defaultIndicator.setVisibility(GONE);
                    } else {
                        defaultIndicator.setVisibility(VISIBLE);
                    }
                }

                if (onScrollPositionListener != null) {
                    onScrollPositionListener.onScrollPosition(i);
                }
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }

            }
        });

    }

    public void setIndicator(View indicatorView, FrameLayout.LayoutParams params) {
        if (indicatorView == null) {
            LayoutParams defaultParams = new LayoutParams(LayoutParams.WRAP_CONTENT, 40);
            defaultParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
            defaultIndicator = new Indicator(getContext());
            defaultIndicator.setTotal(banners.size());
            addView(defaultIndicator, defaultParams);
        } else {
            removeView(defaultIndicator);
            indicatorView.setLayoutParams(params);
            addView(indicatorView);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            int height = (int) ((getScreenWith() / (getScreenHeight() * 1.0)) * width);
            setMeasuredDimension(width, height);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            if (bannerRecycler != null) {
                bannerRecycler.measure(widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopExecutor();
                break;
            case MotionEvent.ACTION_UP:
                startExecutor();
                break;
            default:
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startExecutor();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopExecutor();
    }

    public void startExecutor() {
        if (myHandler != null) {
            myHandler.sendEmptyMessageDelayed(MSG_WHAT, delaySecond * 1000);
        }
    }

    public void stopExecutor() {
        if (myHandler != null) {
            myHandler.removeMessages(MSG_WHAT);
        }
    }

    public void setIndicatorVisible(int visible) {
        defaultIndicator.setVisibility(visible);
    }

    private void scroll() {
        myHandler.sendEmptyMessageDelayed(MSG_WHAT, delaySecond * 1000);
        bannerRecycler.smoothScrollToPosition(layoutManager.findFirstVisibleItemPosition() + 1);
    }

    public static class MyHandler extends Handler {
        private WeakReference<Carousel> reference;

        public MyHandler(Carousel carousel) {
            reference = new WeakReference<>(carousel);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Carousel carousel = reference.get();
            if (reference != null && carousel != null) {
                carousel.scroll();
            }
        }

    }

    public int getScreenWith() {
        Context context = getContext();
        if (context == null) {
            return 0;
        }
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(dm);
        }
        return dm.widthPixels;
    }

    public int getScreenHeight() {
        Context context = getContext();
        if (context == null) {
            return 0;
        }
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(dm);
        }
        return dm.heightPixels;
    }

    public interface OnScrollPositionListener {
        void onScrollPosition(int position);
    }
}
