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
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.libery.carousel.model.Banner;
import cn.libery.carousel.view.Indicator;

/**
 * @author shizhiqiang on 2018/8/22.
 * @description
 */
public class Carousel extends FrameLayout {

    private List<Banner> banners;
    private int delaySecond = 5;
    private SmoothLinearLayoutManager layoutManager;
    private RecyclerView bannerRecycler;
    private MyHandler myHandler;
    private Indicator indicator;
    public static final int MSG_WHAT = 1;

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
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 40);
        params.gravity = Gravity.BOTTOM;
        indicator = new Indicator(getContext());
        indicator.setTotal(banners.size());
        addView(indicator, params);

        bannerRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int i = layoutManager.findFirstVisibleItemPosition() % banners.size();
                indicator.setPosition(i);
                if (banners.get(i).getType() == Banner.VIDEO) {
                    indicator.setVisibility(GONE);
                } else {
                    indicator.setVisibility(VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            int height = width / 16 * 9;
            setMeasuredDimension(width, width / 16 * 9);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            bannerRecycler.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("onTouchEvent", "down");
                stopExecutor();
                break;
            case MotionEvent.ACTION_UP:
                Log.e("onTouchEvent", "up");
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
        indicator.setVisibility(visible);
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

}
