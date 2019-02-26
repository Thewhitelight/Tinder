package cn.libery.badgeview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * @author shizhiqiang on 2018/8/6.
 * @description
 */
public class BadgeView extends TextView {

    public BadgeView(final Context context) {
        this(context, null);
    }

    public BadgeView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BadgeView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setBackgroundResource(R.drawable.bg_red_point);
        setTextColor(Color.WHITE);
        setTextSize(8);
        setGravity(Gravity.CENTER);
    }

    public void setDisplacement(int x, int y) {
        setLeft(x);
        setTop(y);
    }

    /**
     * 只需要调用一次
     *
     * @param targetView 需要绑定的View
     * @return 包裹View
     */
    public View bindView(View targetView) {
        if (targetView == null) {
            throw new IllegalArgumentException("targetView is null");
        }
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
        ViewParent viewParent = targetView.getParent();
        if (viewParent instanceof ViewGroup) {
            ViewGroup targetContainer = (ViewGroup) viewParent;
            int index = targetContainer.indexOfChild(targetView);
            ViewGroup.LayoutParams targetParams = targetView.getLayoutParams();
            targetContainer.removeView(targetView);
            BadgeContainer container = new BadgeContainer(getContext(), this);
            if (targetContainer instanceof RelativeLayout || targetContainer instanceof ConstraintLayout) {
                container.setId(targetView.getId());
            }
            container.addView(targetView);
            container.addView(this);
            targetContainer.addView(container, index, targetParams);
            return targetContainer;
        } else {
            throw new IllegalArgumentException("targetView must has parent");
        }
    }

    private static class BadgeContainer extends ViewGroup {

        private int minWidth = dp2px(7);
        private int minHeight = dp2px(7);

        private BadgeView badgeView;

        public BadgeContainer(Context context, BadgeView badgeView) {
            super(context);
            this.badgeView = badgeView;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            View targetView = null, badgeView = null;
            if (getChildCount() == 0) {
                return;
            }
            for (int i = 0, size = getChildCount(); i < size; i++) {
                View child = getChildAt(i);
                if (child instanceof BadgeView) {
                    badgeView = child;
                } else {
                    targetView = child;
                }
            }
            if (targetView == null) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            } else {
                targetView.measure(widthMeasureSpec, heightMeasureSpec);
                if (badgeView != null) {
                    Drawable background = badgeView.getBackground();
                    if (background != null && background.getIntrinsicHeight() != -1) {
                        minHeight = background.getIntrinsicHeight();
                        minWidth = background.getIntrinsicWidth();
                    }
                    badgeView.measure(MeasureSpec.makeMeasureSpec(minWidth, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(minHeight, MeasureSpec.EXACTLY));
                }
                setMeasuredDimension(targetView.getMeasuredWidth(), targetView.getMeasuredHeight());
            }
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            if (getChildCount() == 0) {
                return;
            }
            for (int i = 0, size = getChildCount(); i < size; i++) {
                View child = getChildAt(i);
                if (child instanceof BadgeView) {
                    child.layout(badgeView.getLeft(), badgeView.getTop(), child.getMeasuredWidth() + badgeView.getLeft(), child.getMeasuredHeight() + badgeView.getTop());
                } else {
                    child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
                }
            }
        }
    }

    public static int dp2px(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dp * density);
    }

}
