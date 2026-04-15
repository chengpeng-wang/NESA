package com.labado.lulaoshi;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.lulaoshi.R;

public class ViewCache {
    private View baseView;
    private Button btn;
    private ImageView imageView;
    private TextView textView;
    private TextView vodn;

    public ViewCache(View baseView) {
        this.baseView = baseView;
    }

    public TextView getTextView() {
        if (this.textView == null) {
            this.textView = (TextView) this.baseView.findViewById(R.id.text);
        }
        return this.textView;
    }

    public TextView getvodn() {
        if (this.vodn == null) {
            this.vodn = (TextView) this.baseView.findViewById(R.id.vodname);
        }
        return this.vodn;
    }

    public ImageView getImageView() {
        if (this.imageView == null) {
            this.imageView = (ImageView) this.baseView.findViewById(R.id.image);
        }
        return this.imageView;
    }

    public Button getbtn() {
        if (this.btn == null) {
            this.btn = (Button) this.baseView.findViewById(R.id.playbtn);
        }
        return this.btn;
    }
}
