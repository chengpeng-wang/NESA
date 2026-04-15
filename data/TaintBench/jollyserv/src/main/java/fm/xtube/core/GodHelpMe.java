package fm.xtube.core;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import fm.xtube.R;

public class GodHelpMe extends Activity {
    private ProgressDialog progressDialog = null;
    protected GodHelpMe self = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.self = this;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this.self.hideProgress();
    }

    public void showProgress() {
        if (this.progressDialog == null) {
            try {
                this.progressDialog = ProgressDialog.show(this, "", this.self.getResources().getString(R.string.loading));
                this.progressDialog.setCancelable(false);
            } catch (Exception e) {
            }
        }
    }

    public void hdProgress() {
        if (this.progressDialog == null) {
            try {
                this.progressDialog = ProgressDialog.show(this, "", this.self.getResources().getString(R.string.hd_loading));
                this.progressDialog.setCancelable(false);
            } catch (Exception e) {
            }
        }
    }

    public void categoryProgress() {
        if (this.progressDialog == null) {
            try {
                this.progressDialog = ProgressDialog.show(this, "", this.self.getResources().getString(R.string.category_loading));
                this.progressDialog.setCancelable(false);
            } catch (Exception e) {
            }
        }
    }

    public void movieProgress() {
        if (this.progressDialog == null) {
            try {
                this.progressDialog = ProgressDialog.show(this, "", this.self.getResources().getString(R.string.movie_loading));
                this.progressDialog.setCancelable(false);
            } catch (Exception e) {
            }
        }
    }

    public void hideProgress() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
            this.progressDialog = null;
        }
    }
}
