package com.android.service.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import com.android.service.R;
import com.monkey.photo.ilb.PhotoView;
import com.xmpp.client.util.AppPreferences;
import com.xmpp.client.util.DecodeUtils;
import com.xmpp.client.util.ValueUtiles;
import java.io.File;

public class PhotoRecord extends Activity {
    public static String FilePath;
    public static String Product_Name;
    public static String Select_num;
    private static Context ctx;
    static File[] files = null;
    public static int intCurrentPage = 1;
    public static int intTotalCount = 0;
    static String strSelect_Pairnum;
    private AppPreferences _appPrefs;
    int bmpHeight;
    int bmpWidth;
    public Button btn_pager;
    Drawable drawableTemp = null;
    String hostname;
    private RelativeLayout layout_pager;
    /* access modifiers changed from: private */
    public ViewPager mViewPager;
    public RelativeLayout relativeLayout_pager;
    public String strDataPath = "";
    private String unzipLocation;

    class SamplePagerAdapter extends PagerAdapter {
        SamplePagerAdapter() {
        }

        public int getCount() {
            return PhotoRecord.intTotalCount;
        }

        public View instantiateItem(ViewGroup container, int position) {
            Uri imageUri;
            PhotoView photoView = new PhotoView(container.getContext());
            PhotoRecord.intCurrentPage = position - 1;
            if (position == 0 || position == PhotoRecord.intTotalCount - 1) {
                imageUri = Uri.parse("android.resource://com.example.monkeydiy/drawable/b1_0");
            } else {
                imageUri = Uri.fromFile(new File(new StringBuilder(String.valueOf(PhotoRecord.this.strDataPath)).append(PhotoRecord.strSelect_Pairnum).append("MonkeyPhoto/").append(PhotoRecord.files[position - 1].getName()).toString()));
            }
            System.out.println("image: " + imageUri.toString());
            photoView.setImageBitmap(DecodeUtils.decode(container.getContext(), imageUri, -1, -1));
            photoView.setScaleType(ScaleType.FIT_XY);
            container.addView(photoView, -1, -1);
            return photoView;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.my_pager);
        this.hostname = getString(R.string.hostname);
        this._appPrefs = new AppPreferences(this);
        strSelect_Pairnum = this._appPrefs.getPairnum();
        this.strDataPath = this._appPrefs.getDataPath();
        System.out.println("----strDataPath---:" + this.strDataPath);
        if (this.strDataPath.length() <= 0) {
            this.strDataPath = ValueUtiles.getDataPath(this);
        }
        files = new File(this.strDataPath + strSelect_Pairnum + "MonkeyPhoto").listFiles();
        intTotalCount = files.length + 2;
        this.mViewPager = (HackyViewPager) findViewById(R.id.mypager_id);
        this.mViewPager.setAdapter(new SamplePagerAdapter());
        this.mViewPager.setCurrentItem(intCurrentPage);
        this.mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageSelected(int position) {
                if (position != PhotoRecord.intTotalCount - 1 && position != 0) {
                    return;
                }
                if (position == 0) {
                    PhotoRecord.this.mViewPager.setCurrentItem(position + 1);
                } else {
                    PhotoRecord.this.mViewPager.setCurrentItem(position - 1);
                }
            }

            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
        });
    }

    public void onStart() {
        super.onStart();
        this.layout_pager = (RelativeLayout) findViewById(R.id.pagerLayout_id);
    }

    public static Bitmap readBitMap(Context context, int resId) {
        Options opt = new Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        opt.inSampleSize = 5;
        return BitmapFactory.decodeStream(context.getResources().openRawResource(resId), null, opt);
    }
}
