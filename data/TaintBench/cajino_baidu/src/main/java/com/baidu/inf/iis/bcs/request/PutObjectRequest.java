package com.baidu.inf.iis.bcs.request;

import com.baidu.inf.iis.bcs.http.HttpMethodName;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;
import com.baidu.inf.iis.bcs.model.X_BS_ACL;
import java.io.File;
import java.io.InputStream;

public class PutObjectRequest extends BaiduBCSRequest {
    private X_BS_ACL acl = null;
    private File file = null;
    private ObjectMetadata metadata = null;
    private InputStream objectContent = null;

    public PutObjectRequest(String str, String str2, File file) {
        super(str, str2, HttpMethodName.PUT);
        this.file = file;
    }

    public PutObjectRequest(String str, String str2, InputStream inputStream, ObjectMetadata objectMetadata) {
        super(str, str2, HttpMethodName.PUT);
        this.objectContent = inputStream;
        this.metadata = objectMetadata;
    }

    public X_BS_ACL getAcl() {
        return this.acl;
    }

    public File getFile() {
        return this.file;
    }

    public ObjectMetadata getMetadata() {
        return this.metadata;
    }

    public InputStream getObjectContent() {
        return this.objectContent;
    }

    public void setAcl(X_BS_ACL x_bs_acl) {
        this.acl = x_bs_acl;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setMetadata(ObjectMetadata objectMetadata) {
        this.metadata = objectMetadata;
    }

    public void setObjectContent(InputStream inputStream) {
        this.objectContent = inputStream;
    }
}
