package com.androidquery.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.ProgressBar;
import com.androidquery.AQuery;

public class Progress implements Runnable {
    private Activity act;
    private int bytes;
    private int current;
    private ProgressBar pb;
    private ProgressDialog pd;
    private boolean unknown;
    private String url;
    private View view;

    public Progress(Object p) {
        if (p instanceof ProgressBar) {
            this.pb = (ProgressBar) p;
        } else if (p instanceof ProgressDialog) {
            this.pd = (ProgressDialog) p;
        } else if (p instanceof Activity) {
            this.act = (Activity) p;
        } else if (p instanceof View) {
            this.view = (View) p;
        }
    }

    public void reset() {
        if (this.pb != null) {
            this.pb.setProgress(0);
            this.pb.setMax(10000);
        }
        if (this.pd != null) {
            this.pd.setProgress(0);
            this.pd.setMax(10000);
        }
        if (this.act != null) {
            this.act.setProgress(0);
        }
        this.unknown = false;
        this.current = 0;
        this.bytes = 10000;
    }

    public void setBytes(int bytes) {
        if (bytes <= 0) {
            this.unknown = true;
            bytes = 10000;
        }
        this.bytes = bytes;
        if (this.pb != null) {
            this.pb.setProgress(0);
            this.pb.setMax(bytes);
        }
        if (this.pd != null) {
            this.pd.setProgress(0);
            this.pd.setMax(bytes);
        }
    }

    public void increment(int delta) {
        int i = 1;
        if (this.pb != null) {
            this.pb.incrementProgressBy(this.unknown ? 1 : delta);
        }
        if (this.pd != null) {
            ProgressDialog progressDialog = this.pd;
            if (!this.unknown) {
                i = delta;
            }
            progressDialog.incrementProgressBy(i);
        }
        if (this.act != null) {
            int p;
            if (this.unknown) {
                p = this.current;
                this.current = p + 1;
            } else {
                this.current += delta;
                p = (this.current * 10000) / this.bytes;
            }
            if (p > 9999) {
                p = 9999;
            }
            this.act.setProgress(p);
        }
    }

    public void done() {
        if (this.pb != null) {
            this.pb.setProgress(this.pb.getMax());
        }
        if (this.pd != null) {
            this.pd.setProgress(this.pd.getMax());
        }
        if (this.act != null) {
            this.act.setProgress(9999);
        }
    }

    public void run() {
        dismiss(this.url);
    }

    public void show(String url) {
        reset();
        if (this.pd != null) {
            new AQuery(this.pd.getContext()).show(this.pd);
        }
        if (this.act != null) {
            this.act.setProgressBarIndeterminateVisibility(true);
            this.act.setProgressBarVisibility(true);
        }
        if (this.pb != null) {
            this.pb.setTag(Constants.TAG_URL, url);
            this.pb.setVisibility(0);
        }
        if (this.view != null) {
            this.view.setTag(Constants.TAG_URL, url);
            this.view.setVisibility(0);
        }
    }

    public void hide(String url) {
        if (AQUtility.isUIThread()) {
            dismiss(url);
            return;
        }
        this.url = url;
        AQUtility.post(this);
    }

    private void dismiss(String url) {
        if (this.pd != null) {
            new AQuery(this.pd.getContext()).dismiss(this.pd);
        }
        if (this.act != null) {
            this.act.setProgressBarIndeterminateVisibility(false);
            this.act.setProgressBarVisibility(false);
        }
        if (this.pb != null) {
            this.pb.setTag(Constants.TAG_URL, url);
            this.pb.setVisibility(0);
        }
        View pv = this.pb;
        if (pv == null) {
            pv = this.view;
        }
        if (pv != null) {
            Object tag = pv.getTag(Constants.TAG_URL);
            if (tag == null || tag.equals(url)) {
                pv.setTag(Constants.TAG_URL, null);
                if (this.pb != null && this.pb.isIndeterminate()) {
                    pv.setVisibility(8);
                }
            }
        }
    }

    private void showProgress(Object p, String url, boolean show) {
        if (p == null) {
            return;
        }
        if (p instanceof View) {
            View pv = (View) p;
            ProgressBar pbar = null;
            if (p instanceof ProgressBar) {
                pbar = (ProgressBar) p;
            }
            if (show) {
                pv.setTag(Constants.TAG_URL, url);
                pv.setVisibility(0);
                if (pbar != null) {
                    pbar.setProgress(0);
                    pbar.setMax(100);
                    return;
                }
                return;
            }
            Object tag = pv.getTag(Constants.TAG_URL);
            if (tag == null || tag.equals(url)) {
                pv.setTag(Constants.TAG_URL, null);
                if (pbar != null && pbar.isIndeterminate()) {
                    pv.setVisibility(8);
                }
            }
        } else if (p instanceof Dialog) {
            Dialog pd = (Dialog) p;
            AQuery aq = new AQuery(pd.getContext());
            if (show) {
                aq.show(pd);
            } else {
                aq.dismiss(pd);
            }
        } else if (p instanceof Activity) {
            Activity act = (Activity) p;
            act.setProgressBarIndeterminateVisibility(show);
            act.setProgressBarVisibility(show);
            if (show) {
                act.setProgress(0);
            }
        }
    }
}
