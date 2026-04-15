package com.baidu.inf.iis.bcs.handler;

import com.baidu.inf.iis.bcs.http.BCSHttpResponse;
import com.baidu.inf.iis.bcs.model.BucketSummary;
import com.baidu.inf.iis.bcs.response.BaiduBCSResponse;
import flexjson.JSONDeserializer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BucketListResponseHandler extends HttpResponseHandler<List<BucketSummary>> {
    public BaiduBCSResponse<List<BucketSummary>> handle(BCSHttpResponse bCSHttpResponse) {
        List<HashMap> list = (List) new JSONDeserializer().deserialize(getResponseContentByStr(bCSHttpResponse));
        ArrayList arrayList = new ArrayList();
        for (HashMap hashMap : list) {
            BucketSummary bucketSummary = new BucketSummary((String) hashMap.get("bucket_name"));
            bucketSummary.setCdatatime(Long.valueOf((String) hashMap.get("cdatetime")));
            bucketSummary.setTotalCapacity(Long.valueOf((String) hashMap.get("total_capacity")));
            bucketSummary.setUsedCapacity(Long.valueOf((String) hashMap.get("used_capacity")));
            arrayList.add(bucketSummary);
        }
        BaiduBCSResponse parseResponseMetadata = parseResponseMetadata(bCSHttpResponse);
        parseResponseMetadata.setResult(arrayList);
        return parseResponseMetadata;
    }
}
