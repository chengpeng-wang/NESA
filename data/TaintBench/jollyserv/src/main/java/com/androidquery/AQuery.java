package com.androidquery;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class AQuery extends AbstractAQuery<AQuery> {
    public AQuery(Activity act) {
        super(act);
    }

    public AQuery(View view) {
        super(view);
    }

    public AQuery(Context context) {
        super(context);
    }

    public AQuery(Activity act, View root) {
        super(act, root);
    }
}
