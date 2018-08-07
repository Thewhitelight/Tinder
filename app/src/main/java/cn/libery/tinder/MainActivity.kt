package cn.libery.tinder

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import cn.libery.badgeview.BadgeView
import cn.libery.selectavatar.Avatar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        img.setOnClickListener {
            Avatar.getInstance()
                    .setSelectMode(Avatar.ALL)
                    .setImageFileDir("Tinder")
                    .setHasCrop(true)
                    .imageFile { imageFile ->
                        run {
                            Log.e("file:", imageFile.absolutePath)
                            img.setImageBitmap(BitmapFactory.decodeFile(imageFile.absolutePath))
                        }
                    }
                    .build(this)
        }

        val badge = BadgeView(this)
        badge.setDisplacement(BadgeView.dp2px(50), BadgeView.dp2px(10))
        badge.bindView(img2_layout)

        val badge2 = BadgeView(this)
        badge2.setDisplacement(0, 0)
        badge2.setBackgroundResource(R.drawable.ic_badge_1)
        badge2.bindView(img2_layout)

        val badge3 = BadgeView(this)
        badge3.setDisplacement(50, 50)
        badge3.setText("66")
        badge3.setBackgroundResource(R.drawable.ic_badge_2)
        badge3.bindView(img2_layout)
    }
}
