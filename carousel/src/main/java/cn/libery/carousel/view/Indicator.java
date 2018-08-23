package cn.libery.carousel.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author shizhiqiang on 2018/8/22.
 * @description
 */
public class Indicator extends View {

    private Paint paint;
    private int total = 6;
    private int position = 0;
    private int space = 10;
    private int radius = 10;

    public Indicator(Context context) {
        this(context, null);
    }

    public Indicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Indicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float startPosition = getWidth() / 2 - (radius * 2 * total + space * (total - 1)) / 2;
        canvas.save();
        for (int i = 0; i < total; i++) {
            if (position == i) {
                paint.setColor(Color.WHITE);
            } else {
                paint.setColor(Color.RED);
            }
            canvas.drawCircle(startPosition + radius * (2 * i + 1) + i * space, getHeight() / 2, radius, paint);
        }
        canvas.restore();
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        invalidate();
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

}
