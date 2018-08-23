package cn.libery.carousel.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import cn.libery.carousel.R;

/**
 * @author shizhiqiang on 2018/8/22.
 * @description 带封面的播放器
 */
public class SampleVideo extends StandardGSYVideoPlayer {
    ImageView mCoverImage;
    private OnPlayerStateListener onPlayerStateListener;

    public void setOnPlayerStateListener(OnPlayerStateListener onPlayerStateListener) {
        this.onPlayerStateListener = onPlayerStateListener;
    }

    public SampleVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public SampleVideo(Context context) {
        super(context);
    }

    public SampleVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        mCoverImage = (ImageView) findViewById(R.id.thumbImage);
        setVideoAllCallBack(new VideoAllCallBack() {
            @Override
            public void onStartPrepared(String url, Object... objects) {

            }

            @Override
            public void onPrepared(String url, Object... objects) {

            }

            @Override
            public void onClickStartIcon(String url, Object... objects) {
                if (onPlayerStateListener != null) {
                    onPlayerStateListener.onStart();
                }
            }

            @Override
            public void onClickStartError(String url, Object... objects) {
                if (onPlayerStateListener != null) {
                    onPlayerStateListener.onStart();
                }
            }

            @Override
            public void onClickStop(String url, Object... objects) {
                if (onPlayerStateListener != null) {
                    onPlayerStateListener.onFinish();
                }
            }

            @Override
            public void onClickStopFullscreen(String url, Object... objects) {

            }

            @Override
            public void onClickResume(String url, Object... objects) {
                if (onPlayerStateListener != null) {
                    onPlayerStateListener.onStart();
                }
            }

            @Override
            public void onClickResumeFullscreen(String url, Object... objects) {

            }

            @Override
            public void onClickSeekbar(String url, Object... objects) {

            }

            @Override
            public void onClickSeekbarFullscreen(String url, Object... objects) {

            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                if (onPlayerStateListener != null) {
                    onPlayerStateListener.onFinish();
                }
            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {
                if (onPlayerStateListener != null) {
                    onPlayerStateListener.onStart();
                }
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                if (onPlayerStateListener != null) {
                    onPlayerStateListener.onFinish();
                }
            }

            @Override
            public void onQuitSmallWidget(String url, Object... objects) {

            }

            @Override
            public void onEnterSmallWidget(String url, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekVolume(String url, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekPosition(String url, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekLight(String url, Object... objects) {

            }

            @Override
            public void onPlayError(String url, Object... objects) {

            }

            @Override
            public void onClickStartThumb(String url, Object... objects) {

            }

            @Override
            public void onClickBlank(String url, Object... objects) {

            }

            @Override
            public void onClickBlankFullscreen(String url, Object... objects) {

            }
        });
    }

    public void setVideoUrlAndThumbUrl(String videoUrl, String thumbUrl) {
        Glide.with(getContext().getApplicationContext()).load(thumbUrl).into(mCoverImage);
        setUp(videoUrl, true, null, "");
        setRotateViewAuto(false);
        setLockLand(false);
        setPlayTag(videoUrl);
        setShowFullAnimation(true);
        setIsTouchWiget(false);
        setNeedLockFull(false);
        setPlayPosition(0);

        //增加title
        getTitleTextView().setVisibility(View.GONE);

        //设置返回键
        getBackButton().setVisibility(View.GONE);

        //设置全屏按键功能
        getFullscreenButton().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startWindowFullscreen(mContext, true, true);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_sample_video;
    }

    @Override
    protected void changeUiToCompleteShow() {
        super.changeUiToCompleteShow();
        setViewShowState(mBottomContainer, GONE);
    }

    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
    }

    public interface OnPlayerStateListener {
        void onStart();

        void onFinish();
    }
}
