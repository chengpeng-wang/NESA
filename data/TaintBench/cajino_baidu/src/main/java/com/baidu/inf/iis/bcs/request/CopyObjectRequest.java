package com.baidu.inf.iis.bcs.request;

import com.baidu.inf.iis.bcs.http.HttpMethodName;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;
import com.baidu.inf.iis.bcs.model.Resource;

public class CopyObjectRequest extends BaiduBCSRequest {
    private Resource dest;
    private ObjectMetadata destMetadata;
    private Resource source;
    private String sourceDirective;
    private String sourceEtag;

    public CopyObjectRequest(Resource resource, Resource resource2) {
        super(resource2.getBucket(), resource2.getObject(), HttpMethodName.PUT);
        this.source = resource;
        this.dest = resource2;
    }

    public CopyObjectRequest(Resource resource, Resource resource2, ObjectMetadata objectMetadata) {
        super(resource2.getBucket(), resource2.getObject(), HttpMethodName.PUT);
        this.destMetadata = objectMetadata;
        this.source = resource;
        this.dest = resource2;
    }

    public Resource getDest() {
        return this.dest;
    }

    public ObjectMetadata getDestMetadata() {
        return this.destMetadata;
    }

    public Resource getSource() {
        return this.source;
    }

    public void setDest(Resource resource) {
        this.dest = resource;
    }

    public void setDestMetadata(ObjectMetadata objectMetadata) {
        this.destMetadata = objectMetadata;
    }

    public void setSource(Resource resource) {
        this.source = resource;
    }

    public String getSourceEtag() {
        return this.sourceEtag;
    }

    public void setSourceEtag(String str) {
        this.sourceEtag = str;
    }

    public String getSourceDirective() {
        return this.sourceDirective;
    }

    public void setSourceDirective(String str) {
        this.sourceDirective = str;
    }
}
