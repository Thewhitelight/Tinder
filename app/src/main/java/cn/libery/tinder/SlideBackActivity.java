package cn.libery.tinder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cn.libery.slideback.SlideBack;

/**
 * @author shizhiqiang on 2018/9/30.
 * @description
 */
public class SlideBackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SlideBack.Builder().init(R.layout.activity_slideback)
                .setOnlyLeftBack(true)
                .setBackEdgeWidth(60)
                .build(this);
    }

}
