package cn.libery.tinder.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * @author shizhiqiang on 2018/11/19.
 * @description
 */
public class TestTextView extends AppCompatTextView {
    public TestTextView(Context context) {
        super(context);
        init();
    }


    public TestTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        setTextColor(Color.RED);
    }

}
