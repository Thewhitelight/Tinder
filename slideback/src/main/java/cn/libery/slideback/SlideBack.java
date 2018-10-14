package cn.libery.slideback;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * @author shizhiqiang on 2018/9/30.
 * @description
 */
public class SlideBack {

    public static class Builder {

        private int layoutResID;
        private float backViewHeight;
        private float backEdgeWidth;
        private float backMaxWidth;
        private boolean isOnlyLeftBack;
        private SlideBackView.OnBackListener onBackListener;

        public Builder init(@LayoutRes int layoutResID) {
            this.layoutResID = layoutResID;
            return this;
        }

        public Builder setBackViewHeight(float backViewHeight) {
            this.backViewHeight = backViewHeight;
            return this;
        }

        public Builder setBackEdgeWidth(float backEdgeWidth) {
            this.backEdgeWidth = backEdgeWidth;
            return this;
        }

        public Builder setBackMaxWidth(float backMaxWidth) {
            this.backMaxWidth = backMaxWidth;
            return this;
        }

        public Builder setOnBackListener(SlideBackView.OnBackListener onBackListener) {
            this.onBackListener = onBackListener;
            return this;
        }

        public Builder setOnlyLeftBack(boolean isOnlyLeftBack) {
            this.isOnlyLeftBack = isOnlyLeftBack;
            return this;
        }

        public void build(Activity activity) {
            if (activity == null) {
                return;
            }
            if (layoutResID == 0) {
                return;
            }
            SlideBackView slideBackView = new SlideBackView(activity);
            if (backEdgeWidth != 0) {
                slideBackView.setBackEdgeWidth(backEdgeWidth);
            }
            if (backViewHeight != 0) {
                slideBackView.setBackViewHeight(backViewHeight);
            }
            if (backMaxWidth != 0) {
                slideBackView.setBackMaxWidth(backMaxWidth);
            }
            if (onBackListener != null) {
                slideBackView.setOnBackListener(onBackListener);
            }
            slideBackView.setOnlyLeftBack(isOnlyLeftBack);
            activity.setContentView(layoutResID);
            ViewGroup viewGroup = (ViewGroup) activity.getWindow().getDecorView();
            FrameLayout content = viewGroup.findViewById(android.R.id.content);
            content.addView(slideBackView);
        }

    }

}
