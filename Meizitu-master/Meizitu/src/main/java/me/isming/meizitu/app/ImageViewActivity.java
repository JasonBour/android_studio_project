package me.isming.meizitu.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import com.umeng.analytics.MobclickAgent;

import me.isming.meizitu.dao.LikesDataHelper;
import me.isming.meizitu.model.Feed;
import me.isming.meizitu.util.CToast;
import me.isming.meizitu.view.ProgressWheel;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by Sam on 14-4-15.
 */
public class ImageViewActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    public static final String IMAGE_URL = "image_url";
    public static final String IMAGE_NAME = "image_name";
    public static final String IMAGE_ID = "image_id";

    //PhotoView photoView;

    //ProgressWheel progressWheel;

    private ArrayList<PhotoViewAttacher> mAttachers;
    private ArrayList<PhotoView> photoViews;
    private ArrayList<ProgressWheel> progressWheels;
    private ArrayList<String> urls;
    private ArrayList<View> views;
    private ViewPager pager;
    private String mName;
    private TextView tv;
    private int mId;
    private boolean mIsFavd;
    private LikesDataHelper mLikeHelper;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iv);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);  //设置返回按钮

        //String imageUrl = getIntent().getStringExtra(IMAGE_URL);
        //Type t = new TypeToken<String>(){}.getType();
        urls = getIntent().getStringArrayListExtra(IMAGE_URL);
        mName = getIntent().getStringExtra(IMAGE_NAME);
        mId = getIntent().getIntExtra(IMAGE_ID, -1);
        if (mId <= 0) {
            finish();
            return;
        }

        mLikeHelper = new LikesDataHelper(this);
        mIsFavd = mLikeHelper.query(mId) != null ;
        setTitle(mName);   //从数据库中获取相册
        views = new ArrayList<View>();
        tv = (TextView) findViewById(R.id.textView);
        tv.setText(1+"/"+urls.size());
        pager = (ViewPager) findViewById(R.id.viewpage);
        photoViews = new ArrayList<PhotoView>();
        mAttachers = new ArrayList<PhotoViewAttacher>();
        progressWheels = new ArrayList<ProgressWheel>();
        for (int i = 0; i < urls.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.activity_imageview, null);

            PhotoView pv = (PhotoView) view.findViewById(R.id.photoView);
            final ProgressWheel progress = (ProgressWheel) view.findViewById(R.id.progressWheel);
            final PhotoViewAttacher attacher = new PhotoViewAttacher(pv);
            final ImageView addMyPhoto = (ImageView)findViewById(R.id.addMyPhoto);
            photoViews.add(pv);
            mAttachers.add(attacher);
            progressWheels.add(progress);
            /*
            每一个相册内部图片的点击事件    这里可以设置为点击之后设置自身图片
             */
            attacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    Toast.makeText(getApplicationContext(),"you have click this photo,do you want it? ",Toast.LENGTH_SHORT).show();

                    //finish();
                }
            });

            /*
            这个是底部图片的点击事件   如果有必要   需要将向对应的图片信息传递过去
             */
            addMyPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Intent intent = new Intent();
                    intent.setClass(ImageViewActivity.this,me.isming.meizitu.view.PublishActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisc(true)
                    .considerExifParams(true).build();
            ImageLoader.getInstance().displayImage(urls.get(i), pv, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progress.setVisibility(View.GONE);
                    attacher.update();
                }
            }, new ImageLoadingProgressListener() {
                @Override
                public void onProgressUpdate(String imageUri, View view, int current, int total) {
                    progress.setProgress(360 * current / total);
                }
            });

            views.add(view);

        }

        PagerAdapter mPagerAdapter = new PagerAdapter() {


            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(View container, int position, Object object) {
                ((ViewPager) container).removeView(views.get(position));
            }

            @Override
            public Object instantiateItem(View container, int position) {
                ((ViewPager) container).addView(views.get(position));
                return views.get(position);
            }

        };
        pager.setAdapter(mPagerAdapter);
        pager.setOnPageChangeListener(this);

    }

    /*
    将menu菜单载入到actionbar上
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_like);
        if (mIsFavd) {
            item.setIcon(R.drawable.ic_favorite_white);
        } else {
            item.setIcon(R.drawable.ic_favorite_outline);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_like:
                doFav();             //收藏图片
                return true;
            case R.id.action_down:
                savePicture();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAttachers == null) {
            return;
        }
        for(int i=0;i<mAttachers.size();i++) {
            if (mAttachers.get(i) != null) {
                mAttachers.get(i).cleanup();
            }
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tv.setText((position + 1) + "/" + urls.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ImageViewAct"+mName); //统计页面
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ImageViewAct"+mName); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }

    private void doFav() {
        if (mIsFavd) {
            mLikeHelper.delete(mId);
        } else {
            Feed feed = new Feed();
            feed.setImgs(urls);
            feed.setName(mName);
            feed.setId(mId);

            mLikeHelper.insert(feed);
        }
        mIsFavd = !mIsFavd;

        invalidateOptionsMenu();

    }

    private void savePicture() {
        if (pager == null || photoViews == null) {
            return ;
        }

        int now = pager.getCurrentItem();
        //photoViews.get(now).getDrawingCache();
        /*
        imageLoader载入图片
         */
        ImageLoader.getInstance().loadImage(urls.get(now), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                writeImgToFile(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });

    }

    /*
    保持图片
     */
    public void writeImgToFile(final Bitmap bit) {
        String externalStorageState = Environment.getExternalStorageState();
        if(!externalStorageState.equals(Environment.MEDIA_MOUNTED)){
            CToast.showToast(this, R.string.no_sdcard);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sdcard = Environment.getExternalStorageDirectory().getPath();
                String fileDir = sdcard + "/meizitu/";
                File dir = new File(fileDir);
                if(!dir.exists()) {
                    dir.mkdirs();
                }

                final String imgFileName = fileDir+"meizi_"+ SystemClock.elapsedRealtime()+".jpg";
                File imgFile = new File(imgFileName);
                try {
                    if(imgFile.exists()) {
                        imgFile.delete();
                    }
                    imgFile.createNewFile();
                    BufferedOutputStream bos = new BufferedOutputStream(
                            new FileOutputStream(imgFile));
                    bit.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                    try {
                        bos.flush();
                    } catch (IOException e) {

                    }

                    try {
                        bos.close();
                    } catch (IOException e) {

                    }
                    ImageViewActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CToast.showToast(ImageViewActivity.this, "妹子已经成功为你保存到"+imgFileName);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();


    }
}
