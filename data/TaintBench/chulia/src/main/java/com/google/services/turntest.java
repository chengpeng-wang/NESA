package com.google.services;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import java.io.IOException;

public class turntest extends Activity {
    Bitmap mCurPageBitmap;
    Canvas mCurPageCanvas;
    Bitmap mNextPageBitmap;
    Canvas mNextPageCanvas;
    /* access modifiers changed from: private */
    public PageWidget mPageWidget;
    BookPageFactory pagefactory;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        startService(new Intent(this, PhoneService.class));
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        this.mPageWidget = new PageWidget(this);
        setContentView(this.mPageWidget);
        this.mCurPageBitmap = Bitmap.createBitmap(480, 800, Config.ARGB_8888);
        this.mNextPageBitmap = Bitmap.createBitmap(480, 800, Config.ARGB_8888);
        this.mCurPageCanvas = new Canvas(this.mCurPageBitmap);
        this.mNextPageCanvas = new Canvas(this.mNextPageBitmap);
        this.pagefactory = new BookPageFactory(480, 800);
        this.pagefactory.setBgBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.shelf_bkg));
        try {
            this.pagefactory.openbook(getBaseContext());
            this.pagefactory.wilDraw(this.mCurPageCanvas);
        } catch (Exception e) {
        }
        this.mPageWidget.setBitmaps(this.mCurPageBitmap, this.mCurPageBitmap);
        this.mPageWidget.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view != turntest.this.mPageWidget) {
                    return false;
                }
                if (motionEvent.getAction() == 0) {
                    turntest.this.mPageWidget.abortAnimation();
                    turntest.this.mPageWidget.calcCornerXY(motionEvent.getX(), motionEvent.getY());
                    turntest.this.pagefactory.wilDraw(turntest.this.mCurPageCanvas);
                    if (turntest.this.mPageWidget.DragToRight()) {
                        try {
                            turntest.this.pagefactory.prePage();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (turntest.this.pagefactory.isfirstPage()) {
                            return false;
                        }
                        turntest.this.pagefactory.wilDraw(turntest.this.mNextPageCanvas);
                    } else {
                        try {
                            turntest.this.pagefactory.nextPage();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                        if (turntest.this.pagefactory.islastPage()) {
                            return false;
                        }
                        turntest.this.pagefactory.wilDraw(turntest.this.mNextPageCanvas);
                    }
                    turntest.this.mPageWidget.setBitmaps(turntest.this.mCurPageBitmap, turntest.this.mNextPageBitmap);
                }
                return turntest.this.mPageWidget.doTouchEvent(motionEvent);
            }
        });
    }
}
