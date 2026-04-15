package com.baidu.inf.iis.bcs.handler;

import com.baidu.inf.iis.bcs.http.BCSHttpResponse;
import com.baidu.inf.iis.bcs.model.ObjectListing;
import com.baidu.inf.iis.bcs.model.ObjectSummary;
import com.baidu.inf.iis.bcs.response.BaiduBCSResponse;
import flexjson.JSONDeserializer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ObjectListResponseHandler extends HttpResponseHandler<ObjectListing> {
    public BaiduBCSResponse<ObjectListing> handle(BCSHttpResponse bCSHttpResponse) {
        HashMap hashMap = (HashMap) new JSONDeserializer().deserialize(getResponseContentByStr(bCSHttpResponse));
        ObjectListing objectListing = new ObjectListing();
        objectListing.setObjectTotal(((Integer) hashMap.get("object_total")).intValue());
        if (hashMap.get("start") instanceof String) {
            objectListing.setStart(Integer.valueOf((String) hashMap.get("start")).intValue());
        } else {
            objectListing.setStart(((Integer) hashMap.get("start")).intValue());
        }
        if (hashMap.get("limit") instanceof String) {
            objectListing.setLimit(Integer.valueOf((String) hashMap.get("limit")).intValue());
        } else {
            objectListing.setLimit(((Integer) hashMap.get("limit")).intValue());
        }
        objectListing.setBucket((String) hashMap.get("bucket"));
        if (hashMap.get("prefix") != null) {
            objectListing.setPrefix((String) hashMap.get("prefix"));
        }
        Iterator it = ((ArrayList) hashMap.get("object_list")).iterator();
        while (it.hasNext()) {
            hashMap = (HashMap) it.next();
            ObjectSummary objectSummary = new ObjectSummary();
            objectSummary.setName((String) hashMap.get("object"));
            objectSummary.setVersionKey((String) hashMap.get("version_key"));
            objectSummary.setIsDir(((String) hashMap.get("is_dir")).equals("1"));
            objectSummary.setSize(Long.valueOf((String) hashMap.get("size")));
            objectSummary.setLastModifiedTime(Long.valueOf((String) hashMap.get("mdatetime")));
            objectSummary.setParentDir((String) hashMap.get("parent_dir"));
            objectListing.addObjectSummary(objectSummary);
        }
        BaiduBCSResponse parseResponseMetadata = parseResponseMetadata(bCSHttpResponse);
        parseResponseMetadata.setResult(objectListing);
        return parseResponseMetadata;
    }
}
