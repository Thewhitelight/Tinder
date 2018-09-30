package cn.libery.slideback;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * @author shizhiqiang on 2018/9/18.
 * @description
 */
public class SlideBackView extends FrameLayout {

    public static final String TAG = "SlideBackView";

    /**
     * backView 高度
     */
    private float backViewHeight;
    /**
     * 边缘触发距离
     */
    private float backEdgeWidth;
    /**
     * 最大返回触发距离
     */
    private float backMaxWidth;


    private float y;
    private float downX;
    private boolean isEdge;
    private float deltaX;
    private int width;
    private int statusBarHeight = 0;
    private boolean isOnlyLeftBack;

    private Paint backPaint;
    private Paint arrowPaint;
    private Path backPath;
    private Path arrowPath;

    private OnBackListener onBackListener;

    public void setOnBackListener(OnBackListener onBackListener) {
        this.onBackListener = onBackListener;
    }

    public void setOnlyLeftBack(boolean onlyLeftBack) {
        isOnlyLeftBack = onlyLeftBack;
    }

    public SlideBackView(Context context) {
        this(context, null);
    }

    public SlideBackView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideBackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = getContext().obtainStyledAttributes(R.styleable.SlideBackView);
        backViewHeight = array.getDimension(R.styleable.SlideBackView_back_view_height, getResources().getDimension(R.dimen.back_view_height));
        backEdgeWidth = array.getDimension(R.styleable.SlideBackView_back_edge_width, getResources().getDimension(R.dimen.back_edge_width));
        backMaxWidth = array.getDimension(R.styleable.SlideBackView_back_max_width, getResources().getDimension(R.dimen.back_max_width));
        array.recycle();
        init();
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
    }

    private void init() {
        backPath = new Path();
        arrowPath = new Path();

        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint.setColor(0xAA000000);
        backPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        arrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arrowPaint.setColor(Color.WHITE);
        arrowPaint.setStyle(Paint.Style.STROKE);
        arrowPaint.setStrokeWidth(6);
        setWillNotDraw(false);
    }

    boolean left = false, right = false;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                if (x <= backEdgeWidth) {
                    isEdge = true;
                    left = true;
                } else if (x >= getWidth() - backEdgeWidth) {
                    isEdge = true;
                    right = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                deltaX = x - downX;
                if (isEdge) {
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isEdge) {
                    deltaX = 0;
                    invalidate();
                    if (x > backMaxWidth && left) {
                        back();
                    } else if (x < getWidth() - backMaxWidth && right) {
                        if (!isOnlyLeftBack) {
                            back();
                        }
                    }
                }
                left = false;
                right = false;
                isEdge = false;
                break;
            default:
                break;
        }
        return isEdge;
    }

    private void back() {
        if (onBackListener != null) {
            onBackListener.onBack();
        } else {
            ((Activity) getContext()).finish();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec) + 1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (deltaX > backMaxWidth) {
            deltaX = backMaxWidth;
        } else if (deltaX < -backMaxWidth) {
            deltaX = -backMaxWidth;
        }
        float deltaY = y - backViewHeight / 2;
        backPath.reset();
        if (deltaX > 0) {
            backPath.moveTo(0, deltaY);
            backPath.quadTo(0, backViewHeight / 4 + deltaY, deltaX / 3, backViewHeight * 3 / 8 + deltaY);
            backPath.quadTo(deltaX * 5 / 8, backViewHeight / 2 + deltaY, deltaX / 3, backViewHeight * 5 / 8 + deltaY);
            backPath.quadTo(0, backViewHeight * 6 / 8 + deltaY, 0, backViewHeight + deltaY);
            canvas.drawPath(backPath, backPaint);

            arrowPath.reset();
            arrowPath.moveTo(deltaX / 6 + (15 * (deltaX / (1080 / 6))), backViewHeight * 15 / 32 + deltaY);
            arrowPath.lineTo(deltaX / 6, backViewHeight * 16.1f / 32 + deltaY);
            arrowPath.moveTo(deltaX / 6, backViewHeight * 15.9f / 32 + deltaY);
            arrowPath.lineTo(deltaX / 6 + (15 * (deltaX / (1080 / 6))), backViewHeight * 17 / 32 + deltaY);
            canvas.drawPath(arrowPath, arrowPaint);
        } else {
            if (!isOnlyLeftBack) {
                deltaX = Math.abs(deltaX);
                backPath.moveTo(width, deltaY);
                backPath.quadTo(width, backViewHeight / 4 + deltaY, width - deltaX / 3, backViewHeight * 3 / 8 + deltaY);
                backPath.quadTo(width - deltaX * 5 / 8, backViewHeight / 2 + deltaY, width - deltaX / 3, backViewHeight * 5 / 8 + deltaY);
                backPath.quadTo(width, backViewHeight * 6 / 8 + deltaY, width, backViewHeight + deltaY);
                canvas.drawPath(backPath, backPaint);

                arrowPath.reset();
                arrowPath.moveTo(width - deltaX / 6 - (15 * (deltaX / (width / 6))), backViewHeight * 15 / 32 + deltaY);
                arrowPath.lineTo(width - deltaX / 6, backViewHeight * 16.1f / 32 + deltaY);
                arrowPath.moveTo(width - deltaX / 6, backViewHeight * 15.9f / 32 + deltaY);
                arrowPath.lineTo(width - deltaX / 6 - (15 * (deltaX / (width / 6))), backViewHeight * 17 / 32 + deltaY);
                canvas.drawPath(arrowPath, arrowPaint);
            }
        }
        setAlpha(deltaX / backMaxWidth);
    }

    public void setBackViewHeight(float backViewHeight) {
        this.backViewHeight = backViewHeight;
    }

    public void setBackEdgeWidth(float backEdgeWidth) {
        this.backEdgeWidth = backEdgeWidth;
    }

    public void setBackMaxWidth(float backMaxWidth) {
        this.backMaxWidth = backMaxWidth;
    }

    public float getBackEdgeWidth() {
        return backEdgeWidth;
    }

    public float getBackMaxWidth() {
        return backMaxWidth;
    }

    public float getBackViewHeight() {
        return backViewHeight;
    }

    public interface OnBackListener {
        void onBack();
    }
}
