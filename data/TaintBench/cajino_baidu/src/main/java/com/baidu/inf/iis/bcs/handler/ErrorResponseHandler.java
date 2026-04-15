package com.baidu.inf.iis.bcs.handler;

import com.baidu.inf.iis.bcs.http.BCSHttpResponse;
import com.baidu.inf.iis.bcs.model.BCSServiceException;
import com.baidu.inf.iis.bcs.response.BaiduBCSResponse;
import flexjson.JSONDeserializer;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ErrorResponseHandler extends HttpResponseHandler<BCSServiceException> {
    private static final Log log = LogFactory.getLog(ErrorResponseHandler.class);

    public BaiduBCSResponse<BCSServiceException> handle(BCSHttpResponse bCSHttpResponse) {
        int i;
        String str;
        int i2;
        Throwable th;
        BCSServiceException bCSServiceException;
        BaiduBCSResponse baiduBCSResponse = new BaiduBCSResponse();
        String responseContentByStr = getResponseContentByStr(bCSHttpResponse);
        String str2 = "";
        if (responseContentByStr.length() != 0) {
            try {
                HashMap hashMap = (HashMap) ((HashMap) new JSONDeserializer().deserialize(responseContentByStr)).get("Error");
                int intValue = Integer.valueOf((String) hashMap.get("code")).intValue();
                try {
                    i = intValue;
                    str = (String) hashMap.get("Message");
                } catch (Exception e) {
                    Throwable th2 = e;
                    i2 = intValue;
                    th = th2;
                    log.warn("analyze bcs error response json failed.", th);
                    str = str2;
                    i = i2;
                    bCSServiceException = new BCSServiceException("[StatusCode:" + bCSHttpResponse.getStatusCode() + "] [ErrorMsg:" + responseContentByStr + "]");
                    bCSServiceException.setHttpErrorCode(bCSHttpResponse.getStatusCode());
                    bCSServiceException.setRequestId((String) bCSHttpResponse.getHeaders().get("x-bs-request-id"));
                    bCSServiceException.setBcsErrorCode(i);
                    bCSServiceException.setBcsErrorMessage(str);
                    baiduBCSResponse.setResult(bCSServiceException);
                    return baiduBCSResponse;
                }
            } catch (Exception e2) {
                th = e2;
                i2 = -1;
            }
        } else {
            str = str2;
            i = -1;
        }
        bCSServiceException = new BCSServiceException("[StatusCode:" + bCSHttpResponse.getStatusCode() + "] [ErrorMsg:" + responseContentByStr + "]");
        bCSServiceException.setHttpErrorCode(bCSHttpResponse.getStatusCode());
        bCSServiceException.setRequestId((String) bCSHttpResponse.getHeaders().get("x-bs-request-id"));
        bCSServiceException.setBcsErrorCode(i);
        bCSServiceException.setBcsErrorMessage(str);
        baiduBCSResponse.setResult(bCSServiceException);
        return baiduBCSResponse;
    }
}
