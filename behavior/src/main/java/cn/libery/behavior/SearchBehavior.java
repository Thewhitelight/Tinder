package cn.libery.behavior;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @author shizhiqiang on 2018/9/11.
 * @description
 */
public class SearchBehavior extends CoordinatorLayout.Behavior<FrameLayout> {
    private ArgbEvaluator argbEvaluator;

    public SearchBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        argbEvaluator = new ArgbEvaluator();
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull FrameLayout child, @NonNull View dependency) {
        return dependency.getId() == R.id.main_toolbar;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull FrameLayout child, @NonNull View dependency) {
        Resources res = parent.getResources();
        final float progress = 1.0f - Math.abs(dependency.getTranslationY() / (dependency.getHeight() - res.getDimension(R.dimen.collapsed_toolbar_height)));

        final float collapsedOffset = res.getDimension(R.dimen.collapsed_offset_y);
        final float initOffset = res.getDimension(R.dimen.init_offset_y);
        final float translateY = collapsedOffset + (initOffset - collapsedOffset) * progress;
        int color = (int) argbEvaluator.evaluate(progress, Color.BLUE, Color.RED);
        child.setBackgroundColor(color);
        final float collapsedMargin = res.getDimension(R.dimen.collapsed_offset_margin);
        final float initMargin = res.getDimension(R.dimen.init_offset_margin);
        final int marginLeft = (int) (collapsedMargin + (initMargin - collapsedMargin) * progress);
        int marginRight;
        if (progress > 0.9) {
            marginRight = marginLeft;
        } else {
            marginRight = marginLeft * 2;
        }
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        params.setMargins(marginLeft, 0, marginRight, 0);
        child.setTranslationY(translateY);
        child.setLayoutParams(params);
        return true;
    }
}
