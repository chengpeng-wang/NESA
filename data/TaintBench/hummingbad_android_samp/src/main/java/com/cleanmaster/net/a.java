package com.cleanmaster.net;

import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import com.picksinit.ErrorInfo;
import org.apache.http.HttpResponse;

/* compiled from: Response */
public class a {
    private ResponseCode a = ResponseCode.DEFAULT;

    public void a(ResponseCode responseCode) {
        this.a = responseCode;
    }

    public void a(HttpResponse httpResponse) {
        switch (httpResponse.getStatusLine().getStatusCode()) {
            case CtaButton.WIDTH_DIPS /*200*/:
                a(ResponseCode.Succeed);
                return;
            case ErrorInfo.ERROR_CODE_NO_FILL /*400*/:
                a(ResponseCode.BadRequest);
                return;
            case ErrorInfo.ERROR_CODE_AD_DATA_IS_NULL /*401*/:
                a(ResponseCode.UnAuthorized);
                return;
            case 403:
                a(ResponseCode.Forbidden);
                return;
            case 404:
                a(ResponseCode.NotFound);
                return;
            case 409:
                a(ResponseCode.Conflict);
                return;
            case ErrorInfo.ERROR_CODE_PARAMS /*500*/:
                a(ResponseCode.InternalError);
                return;
            default:
                a(ResponseCode.Failed);
                return;
        }
    }
}
