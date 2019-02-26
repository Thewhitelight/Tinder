package cn.libery.carousel.detail;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.libery.carousel.R;
import cn.libery.carousel.glide.GlideApp;

/**
 * @author shizhiqiang on 2018/10/31.
 * @description
 */
public class ImageDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        ImageView image = findViewById(R.id.detail_image);
        final String url = getIntent().getStringExtra("url");
        if (url == null) {
            finish();
            return;
        }
        if (url.endsWith("gif")) {
            GlideApp.with(getApplicationContext())
                    .asGif()
                    .load(url)
                    .into(image);
        } else {
            GlideApp.with(getApplicationContext())
                    .load(url)
                    .into(image);
        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlideApp.with(getApplicationContext())
                        .asFile()
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .load(url)
                        .listener(new RequestListener<File>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                                String path = Environment.getExternalStorageDirectory().getPath() + "/";
                                try {
                                    FileInputStream inputStream = new FileInputStream(resource);
                                    FileOutputStream outputStream = new FileOutputStream(new File(path, "32.gif"));
                                    byte[] buf = new byte[4096];
                                    int length;
                                    while ((length = inputStream.read(buf)) != -1) {
                                        outputStream.write(buf, 0, length);
                                    }
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return true;
                            }
                        })
                        .submit();
            }
        });
    }
}
