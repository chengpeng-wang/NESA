package com.dsifakf.aoakmnq;

import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;

public class Connect1 extends AsyncTask<Void, Void, Void> {
    OnADFComplite compiteResult;
    Connect3 connect3;
    Context context;
    private boolean isError = false;
    JSONObject resultObject = null;

    public interface OnADFComplite {
        void error();

        void finish(JSONObject jSONObject) throws JSONException;
    }

    public Connect1(Context contxt, String ReqAddr) {
        this.connect3 = new Connect3(ReqAddr);
        this.context = contxt;
    }

    public Connect1(String ReqAddr) {
        this.connect3 = new Connect3(ReqAddr);
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Void result) {
        try {
            if (this.isError || this.resultObject == null) {
                if (this.compiteResult != null) {
                    this.compiteResult.error();
                }
                super.onPostExecute(result);
            }
            if (this.compiteResult != null) {
                this.compiteResult.finish(this.resultObject);
            }
            super.onPostExecute(result);
        } catch (Exception e) {
            if (this.compiteResult != null) {
                this.compiteResult.error();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
        super.onPreExecute();
    }

    /* access modifiers changed from: protected|varargs */
    public Void doInBackground(Void... params) {
        try {
            this.resultObject = this.connect3.getJSONObject();
        } catch (Exception e) {
            this.isError = true;
        }
        return null;
    }

    public void setFetcherResult(OnADFComplite compiteResult) {
        this.compiteResult = compiteResult;
    }
}
