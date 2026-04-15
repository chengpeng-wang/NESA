package com.baidu.inf.iis.bcs.handler;

import com.baidu.inf.iis.bcs.http.BCSHttpResponse;
import com.baidu.inf.iis.bcs.model.BCSClientException;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;
import com.baidu.inf.iis.bcs.response.BaiduBCSResponse;
import com.baidu.inf.iis.bcs.utils.ServiceUtils;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHeaders;

public abstract class HttpResponseHandler<T> {
    private static final Set<String> ignoredHeaders = new HashSet();
    private static final Log log = LogFactory.getLog(HttpResponseHandler.class);

    public abstract BaiduBCSResponse<T> handle(BCSHttpResponse bCSHttpResponse);

    static {
        ignoredHeaders.add("Date");
        ignoredHeaders.add("Server");
        ignoredHeaders.add("x-bs-request-id");
    }

    /* access modifiers changed from: protected */
    public String getResponseContentByStr(BCSHttpResponse bCSHttpResponse) {
        if (bCSHttpResponse.getContent() == null) {
            return "";
        }
        byte[] bArr = new byte[1024];
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            try {
                int read = bCSHttpResponse.getContent().read(bArr);
                if (read <= 0) {
                    return stringBuilder.toString();
                }
                stringBuilder.append(new String(bArr, 0, read));
            } catch (IOException e) {
                throw new BCSClientException("Read http response body error.", e);
            }
        }
    }

    /* access modifiers changed from: protected */
    public BaiduBCSResponse<T> parseResponseMetadata(BCSHttpResponse bCSHttpResponse) {
        BaiduBCSResponse baiduBCSResponse = new BaiduBCSResponse();
        String str = (String) bCSHttpResponse.getHeaders().get("x-bs-request-id");
        baiduBCSResponse.setRequestId(str);
        log.info("Bcs requestId:" + str);
        return baiduBCSResponse;
    }

    /* access modifiers changed from: protected */
    public void populateObjectMetadata(BCSHttpResponse bCSHttpResponse, ObjectMetadata objectMetadata) {
        for (Entry entry : bCSHttpResponse.getHeaders().entrySet()) {
            String str = (String) entry.getKey();
            if (str.startsWith("x-bs-meta-")) {
                objectMetadata.addUserMetadata(str.substring("x-bs-meta-".length()), (String) entry.getValue());
            } else if (str.equals(HttpHeaders.LAST_MODIFIED)) {
                try {
                    objectMetadata.setHeader(str, ServiceUtils.parseRfc822Date((String) entry.getValue()));
                } catch (ParseException e) {
                    log.warn("Unable to parse last modified date: " + ((String) entry.getValue()), e);
                }
            } else if (!ignoredHeaders.contains(str)) {
                objectMetadata.setHeader(str, entry.getValue());
            }
        }
    }

    public boolean isNeedsConnectionLeftOpen() {
        return false;
    }
}
