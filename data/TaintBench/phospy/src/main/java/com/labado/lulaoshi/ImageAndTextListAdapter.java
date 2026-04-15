package com.labado.lulaoshi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.lulaoshi.R;
import com.labado.lulaoshi.AsyncImageLoader.ImageCallback;
import java.util.List;

public class ImageAndTextListAdapter extends ArrayAdapter<ImageAndText> {
    private AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
    /* access modifiers changed from: private */
    public ListView listView;

    public ImageAndTextListAdapter(Activity activity, List<ImageAndText> imageAndTexts, ListView listView) {
        super(activity, 0, imageAndTexts);
        this.listView = listView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewCache viewCache;
        final Activity activity = (Activity) getContext();
        View rowView = convertView;
        if (rowView == null) {
            rowView = activity.getLayoutInflater().inflate(R.layout.main, null);
            viewCache = new ViewCache(rowView);
            rowView.setTag(viewCache);
        } else {
            viewCache = (ViewCache) rowView.getTag();
        }
        ImageAndText imageAndText = (ImageAndText) getItem(position);
        String imageUrl = imageAndText.getImageUrl();
        ImageView imageView = viewCache.getImageView();
        imageView.setTag(imageUrl);
        Drawable cachedImage = this.asyncImageLoader.loadDrawable(imageUrl, new ImageCallback() {
            public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                ImageView imageViewByTag = (ImageView) ImageAndTextListAdapter.this.listView.findViewWithTag(imageUrl);
                if (imageViewByTag != null) {
                    imageViewByTag.setImageDrawable(imageDrawable);
                }
            }
        });
        if (cachedImage == null) {
            imageView.setImageResource(R.drawable.ic_launcher);
        } else {
            imageView.setImageDrawable(cachedImage);
        }
        final TextView textView = viewCache.getTextView();
        textView.setText(imageAndText.getText());
        final TextView vodn = viewCache.getvodn();
        vodn.setText(imageAndText.getvodname());
        viewCache.getbtn().setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent playIntent = new Intent("QvodPlayer.VIDEO_PLAY_ACTION");
                    playIntent.setDataAndType(Uri.parse(textView.getText().toString()), "video/*");
                    activity.startActivity(playIntent);
                    Toast.makeText(ImageAndTextListAdapter.this.getContext(), "现在正在播放 ：" + vodn.getText().toString() + "  \n感谢您使用撸老师！", 1).show();
                } catch (Exception e) {
                    Toast.makeText(ImageAndTextListAdapter.this.getContext(), "出现错误 您确定您的手机已经安装了快播？", 1).show();
                }
            }
        });
        return rowView;
    }
}
