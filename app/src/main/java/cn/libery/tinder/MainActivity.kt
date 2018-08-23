package cn.libery.tinder

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import cn.libery.avatar.Avatar
import cn.libery.badgeview.BadgeView
import cn.libery.carousel.model.Banner
import cn.libery.carousel.model.Banner.IMAGE
import cn.libery.carousel.model.Banner.VIDEO
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


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
        badge3.text = "66"
        badge3.setBackgroundResource(R.drawable.ic_badge_2)
        badge3.bindView(img2_layout)

        val banners = ArrayList<Banner>(6)
        banners.add(Banner("http://wx1.sinaimg.cn/orj360/5d098bccly1fuihjws6ihj21yw2567wh.jpg", IMAGE))
        banners.add(Banner("http://jmtvideo.oss-cn-hangzhou.aliyuncs.com/DDIezgXLSxUZCOU.mp4", VIDEO))
        banners.add(Banner("https://upload-images.jianshu.io/upload_images/7078011-430b292666591af5.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/700", IMAGE))
        banners.add(Banner("http://img.soogif.com/HkkOHvJc0bSj10yemFqUfe3rcf8B628o.mp4", VIDEO))
        banners.add(Banner("http://n.sinaimg.cn/tech/transform/529/w284h245/20180822/t3QG-fzrwica1378454.gif", IMAGE))
        banners.add(Banner("http://f.us.sinaimg.cn/000XduoYlx07n2fcfO1G010402001PpW0k010.mp4?label=mp4_720p&template=1280x720.28&Expires=1534949695&ssig=bxZduauD09&KID=unistore,video", VIDEO))
        carousel.banners = banners
        carousel.delaySecond = 7
        carousel.setOnItemClickListener { position ->
            Toast.makeText(this, banners[position].url, Toast.LENGTH_SHORT).show()
        }
    }
}
