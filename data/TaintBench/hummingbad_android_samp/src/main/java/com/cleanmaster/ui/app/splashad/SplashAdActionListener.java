package com.cleanmaster.ui.app.splashad;

import android.view.View;

public interface SplashAdActionListener {
    void onClicked();

    void onFailed(String str);

    void onLoaded(View view);

    void onShowedFinish();
}
