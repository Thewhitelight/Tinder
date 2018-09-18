package cn.libery.slideback;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * @author shizhiqiang on 2018/9/18.
 * @description
 */
public class SlideBackView extends ConstraintLayout {

    public static final String TAG = "SlideBackView";
    /**
     * backView 高度
     */
    private float backViewHeight;
    /**
     * 边缘触发距离
     */
    private float backEdgeWidth;
    private float backMaxWidth;
    private Paint backPaint;

    private Paint arrowPaint;
    private Path backPath;
    private Path arrowPath;
    private float y;
    private float downX;
    private boolean isEdge;
    private float deltaX;

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
    }

    private void init() {
        backPath = new Path();
        arrowPath = new Path();

        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint.setColor(Color.BLACK);
        backPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        arrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arrowPaint.setColor(Color.WHITE);
        arrowPaint.setStyle(Paint.Style.STROKE);
        arrowPaint.setStrokeWidth(6);
        setWillNotDraw(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float x = ev.getRawX();
        y = ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getRawX();
                if (x <= backEdgeWidth) {
                    isEdge = true;
                } else if (x >= getWidth() - backEdgeWidth) {
                    isEdge = true;
                }
                Log.e(TAG, isEdge + " ACTION_DOWN " + x);
                break;
            case MotionEvent.ACTION_MOVE:
                deltaX = x - downX;
                if (isEdge) {
                    Log.e(TAG, " moveX " + deltaX);
                    invalidate();
                }
                Log.e(TAG, isEdge + " ACTION_MOVE " + x);
                break;
            case MotionEvent.ACTION_UP:
                if (isEdge) {
                    deltaX = 0;
                    invalidate();
                }
                isEdge = false;
                Log.e(TAG, isEdge + " ACTION_UP " + x);
                break;
            default:
                break;
        }
        return isEdge;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (deltaX > backMaxWidth) {
            deltaX = backMaxWidth;
        } else if (deltaX < -backMaxWidth) {
            deltaX = -backMaxWidth;
        }
        float deltaY = y - backViewHeight;
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
            deltaX = Math.abs(deltaX);
            backPath.moveTo(1081, deltaY);
            backPath.quadTo(1081, backViewHeight / 4 + deltaY, 1081 - deltaX / 3, backViewHeight * 3 / 8 + deltaY);
            backPath.quadTo(1081 - deltaX * 5 / 8, backViewHeight / 2 + deltaY, 1081 - deltaX / 3, backViewHeight * 5 / 8 + deltaY);
            backPath.quadTo(1081, backViewHeight * 6 / 8 + deltaY, 1081, backViewHeight + deltaY);
            canvas.drawPath(backPath, backPaint);

            arrowPath.reset();
            arrowPath.moveTo(1080 - deltaX / 6 - (15 * (deltaX / (1080 / 6))), backViewHeight * 15 / 32 + deltaY);
            arrowPath.lineTo(1080 - deltaX / 6, backViewHeight * 16.1f / 32 + deltaY);
            arrowPath.moveTo(1080 - deltaX / 6, backViewHeight * 15.9f / 32 + deltaY);
            arrowPath.lineTo(1080 - deltaX / 6 - (15 * (deltaX / (1080 / 6))), backViewHeight * 17 / 32 + deltaY);
            canvas.drawPath(arrowPath, arrowPaint);
        }

        setAlpha(deltaX / backMaxWidth);
    }

}
