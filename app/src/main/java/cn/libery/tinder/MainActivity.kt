package cn.libery.tinder

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import cn.libery.avatar.Avatar
import cn.libery.badgeview.BadgeView
import cn.libery.behavior.BehaviorActivity
import cn.libery.carousel.model.Banner
import cn.libery.carousel.model.Banner.IMAGE
import cn.libery.carousel.model.Banner.VIDEO
import cn.libery.slideback.SlideBack
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SlideBack.Builder().init(R.layout.activity_main).build(this)
        img.setOnClickListener {
            Avatar.getInstance()
                    .setSelectMode(Avatar.ALL)
                    .setImageFileDir("Tinder")
                    .setHasCrop(true)
                    .setImageSize(500, 500)
                    .setAspectSize(1, 1)
                    .imageFile { imageFile ->
                        run {
                            Log.e("MainActivity-file:", imageFile.absolutePath)
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
        badge3.text = "66"
        badge3.setBackgroundResource(R.drawable.ic_badge_2)
        val v = badge3.bindView(img2_layout)
        v.setOnClickListener { _ ->
            startActivity(Intent(this, BehaviorActivity::class.java))
        }

        val banners = ArrayList<Banner>(6)
        banners.add(Banner(IMAGE, "http://wx1.sinaimg.cn/orj360/5d098bccly1fuihjws6ihj21yw2567wh.jpg"))
        banners.add(Banner(VIDEO, "http://jmtvideo.oss-cn-hangzhou.aliyuncs.com/DDIezgXLSxUZCOU.mp4"))
        banners.add(Banner(IMAGE, "https://upload-images.jianshu.io/upload_images/7078011-430b292666591af5.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/700"))
        banners.add(Banner(VIDEO, "http://img.soogif.com/HkkOHvJc0bSj10yemFqUfe3rcf8B628o.mp4"))
        banners.add(Banner(IMAGE, "http://n.sinaimg.cn/tech/transform/529/w284h245/20180822/t3QG-fzrwica1378454.gif"))
        banners.add(Banner(VIDEO, "http://f.us.sinaimg.cn/000XduoYlx07n2fcfO1G010402001PpW0k010.mp4?label=mp4_720p&template=1280x720.28&Expires=1534949695&ssig=bxZduauD09&KID=unistore,video"))

        banners.add(Banner(layoutInflater.inflate(R.layout.layout_item_text, null)))
        carousel.banners = banners
        carousel.delaySecond = 7

        val indicator = TextView(this)
        indicator.text = "1/" + banners.size
        indicator.gravity = Gravity.CENTER
        indicator.setBackgroundColor(Color.GRAY)
        val indicatorParams = FrameLayout.LayoutParams(100, 60)
        indicatorParams.gravity = (Gravity.BOTTOM or Gravity.LEFT)
        carousel.setIndicator(indicator, indicatorParams)
        carousel.setOnScrollPositionListener { position -> indicator.text = (position + 1).toString() + "/" + banners.size }
        carousel.setOnItemClickListener { position ->
            Toast.makeText(this, banners[position].url, Toast.LENGTH_SHORT).show()
        }
        slide.setOnClickListener { startActivity(Intent(this, SlideBackActivity::class.java)) }
    }

    override fun onDestroy() {
        super.onDestroy()
        Avatar.getInstance().clear()
    }
}
