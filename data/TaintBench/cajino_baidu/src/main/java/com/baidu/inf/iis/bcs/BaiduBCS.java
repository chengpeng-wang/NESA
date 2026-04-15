package com.baidu.inf.iis.bcs;

import com.baidu.inf.iis.bcs.auth.BCSCredentials;
import com.baidu.inf.iis.bcs.auth.BCSSigner;
import com.baidu.inf.iis.bcs.handler.BucketListResponseHandler;
import com.baidu.inf.iis.bcs.handler.ObjectListResponseHandler;
import com.baidu.inf.iis.bcs.handler.ObjectMetadataResponseHandler;
import com.baidu.inf.iis.bcs.handler.ObjectResponseHandler;
import com.baidu.inf.iis.bcs.handler.PolicyResponseHandler;
import com.baidu.inf.iis.bcs.handler.VoidResponseHandler;
import com.baidu.inf.iis.bcs.http.BCSHttpClient;
import com.baidu.inf.iis.bcs.http.BCSHttpRequest;
import com.baidu.inf.iis.bcs.http.ClientConfiguration;
import com.baidu.inf.iis.bcs.http.DefaultBCSHttpRequest;
import com.baidu.inf.iis.bcs.model.BCSClientException;
import com.baidu.inf.iis.bcs.model.BCSServiceException;
import com.baidu.inf.iis.bcs.model.BucketSummary;
import com.baidu.inf.iis.bcs.model.DownloadObject;
import com.baidu.inf.iis.bcs.model.Empty;
import com.baidu.inf.iis.bcs.model.ObjectListing;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;
import com.baidu.inf.iis.bcs.model.Pair;
import com.baidu.inf.iis.bcs.model.Resource;
import com.baidu.inf.iis.bcs.model.SuperfileSubObject;
import com.baidu.inf.iis.bcs.model.X_BS_ACL;
import com.baidu.inf.iis.bcs.policy.Policy;
import com.baidu.inf.iis.bcs.request.BaiduBCSRequest;
import com.baidu.inf.iis.bcs.request.CopyObjectRequest;
import com.baidu.inf.iis.bcs.request.CreateBucketRequest;
import com.baidu.inf.iis.bcs.request.DeleteBucketRequest;
import com.baidu.inf.iis.bcs.request.DeleteObjectRequest;
import com.baidu.inf.iis.bcs.request.GenerateUrlRequest;
import com.baidu.inf.iis.bcs.request.GetBucketPolicyRequest;
import com.baidu.inf.iis.bcs.request.GetObjectMetadataRequest;
import com.baidu.inf.iis.bcs.request.GetObjectPolicyRequest;
import com.baidu.inf.iis.bcs.request.GetObjectRequest;
import com.baidu.inf.iis.bcs.request.ListBucketRequest;
import com.baidu.inf.iis.bcs.request.ListObjectRequest;
import com.baidu.inf.iis.bcs.request.PutBucketPolicyRequest;
import com.baidu.inf.iis.bcs.request.PutObjectPolicyRequest;
import com.baidu.inf.iis.bcs.request.PutObjectRequest;
import com.baidu.inf.iis.bcs.request.PutSuperfileRequest;
import com.baidu.inf.iis.bcs.request.SetObjectMetadataRequest;
import com.baidu.inf.iis.bcs.response.BaiduBCSResponse;
import com.baidu.inf.iis.bcs.utils.Constants;
import com.baidu.inf.iis.bcs.utils.ServiceUtils;
import flexjson.JSONSerializer;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;

public class BaiduBCS {
    private static final Log log = LogFactory.getLog(BaiduBCS.class);
    private BCSHttpClient bcsHttpClient = null;
    private BCSCredentials credentials = null;
    private String endpoint = null;

    public BaiduBCS(BCSCredentials bCSCredentials, String str) {
        this.credentials = bCSCredentials;
        setEndpoint(str);
        this.bcsHttpClient = new BCSHttpClient(new ClientConfiguration());
    }

    public BaiduBCS(BCSCredentials bCSCredentials, String str, ClientConfiguration clientConfiguration) {
        this.credentials = bCSCredentials;
        setEndpoint(str);
        this.bcsHttpClient = new BCSHttpClient(clientConfiguration);
    }

    public void setCredentials(BCSCredentials bCSCredentials) {
        this.credentials = bCSCredentials;
    }

    public BCSCredentials getCredentials() {
        return this.credentials;
    }

    public void setDefaultEncoding(String str) {
        Constants.DEFAULT_ENCODING = str;
    }

    public String getDefaultEncoding() {
        return Constants.DEFAULT_ENCODING;
    }

    public void setEndpoint(String str) {
        if (str.contains("://")) {
            throw new IllegalArgumentException("Endpoint should not contains '://'.");
        }
        this.endpoint = str;
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    public BaiduBCSResponse<Empty> copyObject(CopyObjectRequest copyObjectRequest) throws BCSClientException, BCSServiceException {
        assertParameterNotNull(copyObjectRequest, "The request parameter can be null.");
        assertParameterNotNull(copyObjectRequest.getHttpMethod(), "The http method parameter in Request must be specified.");
        assertParameterNotNull(copyObjectRequest.getSource().getBucket(), "The bucket parameter of source must be specified when copy an object.");
        assertParameterNotNull(copyObjectRequest.getSource().getObject(), "The object parameter of source must be specified when copy an object.");
        assertParameterNotNull(copyObjectRequest.getDest().getBucket(), "The bucket parameter of dest must be specified when copy an object.");
        assertParameterNotNull(copyObjectRequest.getDest().getObject(), "The object parameter of dest must be specified when copy an object.");
        log.debug("copy object, src[Bucket:" + copyObjectRequest.getSource().getBucket() + "][Object:" + copyObjectRequest.getSource().getObject() + "] to dest[Bucket" + copyObjectRequest.getDest().getBucket() + "][Object" + copyObjectRequest.getDest().getObject() + "]");
        BCSHttpRequest createHttpRequest = createHttpRequest(copyObjectRequest);
        createHttpRequest.addHeader("x-bs-copy-source", "bs://" + copyObjectRequest.getSource().getBucket() + copyObjectRequest.getSource().getObject());
        if (copyObjectRequest.getSourceEtag() != null) {
            createHttpRequest.addHeader("x-bs-copy-source-tag", copyObjectRequest.getSourceEtag());
        }
        if (copyObjectRequest.getSourceDirective() != null) {
            createHttpRequest.addHeader("x-bs-copy-source-directive", copyObjectRequest.getSourceDirective());
        }
        populateRequestMetadata(createHttpRequest, copyObjectRequest.getDestMetadata());
        return this.bcsHttpClient.execute(createHttpRequest, new VoidResponseHandler());
    }

    public BaiduBCSResponse<Empty> copyObject(Resource resource, Resource resource2) throws BCSClientException, BCSServiceException {
        return copyObject(new CopyObjectRequest(resource, resource2));
    }

    public BaiduBCSResponse<Empty> copyObject(Resource resource, Resource resource2, ObjectMetadata objectMetadata) throws BCSClientException, BCSServiceException {
        return copyObject(new CopyObjectRequest(resource, resource2, objectMetadata));
    }

    public BaiduBCSResponse<Empty> createBucket(CreateBucketRequest createBucketRequest) throws BCSClientException, BCSServiceException {
        assertParameterNotNull(createBucketRequest, "The request parameter can be null.");
        assertParameterNotNull(createBucketRequest.getHttpMethod(), "The http method parameter in Request must be specified.");
        assertParameterNotNull(createBucketRequest.getBucket(), "The bucket parameter must be specified when creating a bucket");
        log.debug("create bucket, bucket_name [" + createBucketRequest.getBucket() + "]");
        BCSHttpRequest createHttpRequest = createHttpRequest(createBucketRequest);
        if (createBucketRequest.getAcl() != null) {
            createHttpRequest.addHeader("x-bs-acl", createBucketRequest.getAcl().toString());
        }
        return this.bcsHttpClient.execute(createHttpRequest, new VoidResponseHandler());
    }

    public BaiduBCSResponse<Empty> createBucket(String str) throws BCSClientException, BCSServiceException {
        return createBucket(new CreateBucketRequest(str));
    }

    public BaiduBCSResponse<Empty> deleteBucket(DeleteBucketRequest deleteBucketRequest) throws BCSClientException, BCSServiceException {
        assertParameterNotNull(deleteBucketRequest, "The request parameter can be null.");
        assertParameterNotNull(deleteBucketRequest.getHttpMethod(), "The http method parameter in Request must be specified.");
        assertParameterNotNull(deleteBucketRequest.getBucket(), "The bucket parameter must be specified when deleting a bucket.");
        log.debug("delete bucket begin, bucket[" + deleteBucketRequest.getBucket() + "]");
        return this.bcsHttpClient.execute(createHttpRequest(deleteBucketRequest), new VoidResponseHandler());
    }

    public BaiduBCSResponse<Empty> deleteBucket(String str) throws BCSClientException, BCSServiceException {
        return deleteBucket(new DeleteBucketRequest(str));
    }

    public BaiduBCSResponse<Empty> deleteObject(DeleteObjectRequest deleteObjectRequest) throws BCSClientException, BCSServiceException {
        assertParameterNotNull(deleteObjectRequest, "The request parameter can be null.");
        assertParameterNotNull(deleteObjectRequest.getHttpMethod(), "The http method parameter in Request must be specified.");
        assertParameterNotNull(deleteObjectRequest.getBucket(), "The bucket parameter must be specified when deleting an object.");
        assertParameterNotNull(deleteObjectRequest.getObject(), "The object parameter must be specified when deleting an object.");
        log.debug("delete object, bucket[" + deleteObjectRequest.getBucket() + "], object[" + deleteObjectRequest.getObject() + "]");
        return this.bcsHttpClient.execute(createHttpRequest(deleteObjectRequest), new VoidResponseHandler());
    }

    public BaiduBCSResponse<Empty> deleteObject(String str, String str2) throws BCSClientException, BCSServiceException {
        return deleteObject(new DeleteObjectRequest(str, str2));
    }

    public BaiduBCSResponse<Policy> getBucketPolicy(GetBucketPolicyRequest getBucketPolicyRequest) throws BCSClientException, BCSServiceException {
        assertParameterNotNull(getBucketPolicyRequest, "The request parameter can be null.");
        assertParameterNotNull(getBucketPolicyRequest.getHttpMethod(), "The http method parameter in Request must be specified.");
        assertParameterNotNull(getBucketPolicyRequest.getBucket(), "The bucket parameter must be specified when get policy of bucket.");
        log.debug("get bucket policy begin, bucket[" + getBucketPolicyRequest.getBucket() + "]");
        BCSHttpRequest createHttpRequest = createHttpRequest(getBucketPolicyRequest);
        createHttpRequest.addParameter("acl", String.valueOf(1));
        return this.bcsHttpClient.execute(createHttpRequest, new PolicyResponseHandler());
    }

    public BaiduBCSResponse<Policy> getBucketPolicy(String str) throws BCSClientException, BCSServiceException {
        return getBucketPolicy(new GetBucketPolicyRequest(str));
    }

    public BaiduBCSResponse<DownloadObject> getObject(GetObjectRequest getObjectRequest) throws BCSClientException, BCSServiceException {
        assertParameterNotNull(getObjectRequest, "The request parameter can be null.");
        assertParameterNotNull(getObjectRequest.getHttpMethod(), "The http method parameter in Request must be specified.");
        assertParameterNotNull(getObjectRequest.getBucket(), "The bucket parameter must be specified when getting an object.");
        assertParameterNotNull(getObjectRequest.getObject(), "The object parameter must be specified when getting an object.");
        log.debug("get object begin, bucket[" + getObjectRequest.getBucket() + "], object[" + getObjectRequest.getObject() + "]");
        BCSHttpRequest createHttpRequest = createHttpRequest(getObjectRequest);
        if (getObjectRequest.getRange() != null) {
            Pair range = getObjectRequest.getRange();
            assertParameterNotNull(range.getFirst(), "The range first parameter must be specified when getting an object by range.");
            assertParameterNotNull(range.getSecond(), "The range second parameter must be specified when getting an object by range.");
            createHttpRequest.addHeader(HttpHeaders.RANGE, "bytes=" + Long.toString(((Long) range.getFirst()).longValue()) + "-" + Long.toString(((Long) range.getSecond()).longValue()));
        }
        return this.bcsHttpClient.execute(createHttpRequest, new ObjectResponseHandler());
    }

    public BaiduBCSResponse<DownloadObject> getObject(GetObjectRequest getObjectRequest, File file) throws BCSClientException, BCSServiceException {
        IOException e;
        Throwable th;
        assertParameterNotNull(file, "The destination file parameter must be specified when downloading an object directly to a file.");
        BaiduBCSResponse object = getObject(getObjectRequest);
        DownloadObject downloadObject = (DownloadObject) object.getResult();
        if (downloadObject == null) {
            throw new BCSClientException("Get object response is empty.");
        }
        OutputStream bufferedOutputStream;
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            try {
                byte[] bArr = new byte[10240];
                while (true) {
                    int read = downloadObject.getContent().read(bArr);
                    if (read > -1) {
                        bufferedOutputStream.write(bArr, 0, read);
                    } else {
                        try {
                            break;
                        } catch (Exception e2) {
                        }
                    }
                }
                bufferedOutputStream.close();
                try {
                    downloadObject.getContent().close();
                } catch (Exception e3) {
                }
                if (downloadObject.getObjectMetadata().getContentLength() == file.length()) {
                    return object;
                }
                BCSServiceException bCSServiceException = new BCSServiceException("Maybe download incompletely. http Content-Length=" + downloadObject.getObjectMetadata().getContentLength() + " ,download size=" + file.length());
                bCSServiceException.setBcsErrorCode(0);
                bCSServiceException.setBcsErrorMessage("");
                bCSServiceException.setHttpErrorCode(HttpStatus.SC_OK);
                bCSServiceException.setRequestId(object.getRequestId());
                throw bCSServiceException;
            } catch (IOException e4) {
                e = e4;
                try {
                    throw new BCSClientException("Unable to store object contents to disk: " + e.getMessage(), e);
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        bufferedOutputStream.close();
                    } catch (Exception e5) {
                    }
                    try {
                        downloadObject.getContent().close();
                    } catch (Exception e6) {
                    }
                    throw th;
                }
            }
        } catch (IOException e7) {
            e = e7;
            bufferedOutputStream = null;
            throw new BCSClientException("Unable to store object contents to disk: " + e.getMessage(), e);
        } catch (Throwable th3) {
            th = th3;
            bufferedOutputStream = null;
            bufferedOutputStream.close();
            downloadObject.getContent().close();
            throw th;
        }
    }

    public BaiduBCSResponse<DownloadObject> getObject(String str, String str2) throws BCSClientException, BCSServiceException {
        return getObject(new GetObjectRequest(str, str2));
    }

    public BaiduBCSResponse<ObjectMetadata> getObjectMetadata(GetObjectMetadataRequest getObjectMetadataRequest) throws BCSClientException, BCSServiceException {
        return this.bcsHttpClient.execute(createHttpRequest(getObjectMetadataRequest), new ObjectMetadataResponseHandler());
    }

    public BaiduBCSResponse<ObjectMetadata> getObjectMetadata(String str, String str2) throws BCSClientException, BCSServiceException {
        return getObjectMetadata(new GetObjectMetadataRequest(str, str2));
    }

    public boolean doesObjectExist(String str, String str2) throws BCSClientException, BCSServiceException {
        try {
            getObjectMetadata(str, str2);
            return true;
        } catch (BCSServiceException e) {
            if (HttpStatus.SC_NOT_FOUND == e.getHttpErrorCode()) {
                return false;
            }
            throw e;
        } catch (BCSClientException e2) {
            throw e2;
        }
    }

    public BaiduBCSResponse<Policy> getObjectPolicy(GetObjectPolicyRequest getObjectPolicyRequest) throws BCSClientException, BCSServiceException {
        assertParameterNotNull(getObjectPolicyRequest, "The request parameter can be null.");
        assertParameterNotNull(getObjectPolicyRequest.getHttpMethod(), "The http method parameter in Request must be specified.");
        assertParameterNotNull(getObjectPolicyRequest.getBucket(), "The bucket parameter must be specified when getting policy of an object.");
        assertParameterNotNull(getObjectPolicyRequest.getObject(), "The object parameter must be specified when getting policy of an object.");
        log.debug("get object policy, bucket[" + getObjectPolicyRequest.getBucket() + "]" + ", object[" + getObjectPolicyRequest.getObject() + "]");
        BCSHttpRequest createHttpRequest = createHttpRequest(getObjectPolicyRequest);
        createHttpRequest.addParameter("acl", String.valueOf(1));
        return this.bcsHttpClient.execute(createHttpRequest, new PolicyResponseHandler());
    }

    public BaiduBCSResponse<Policy> getObjectPolicy(String str, String str2) throws BCSClientException, BCSServiceException {
        return getObjectPolicy(new GetObjectPolicyRequest(str, str2));
    }

    public BaiduBCSResponse<List<BucketSummary>> listBucket(ListBucketRequest listBucketRequest) throws BCSClientException, BCSServiceException {
        assertParameterNotNull(listBucketRequest, "The request parameter can be null.");
        assertParameterNotNull(listBucketRequest.getHttpMethod(), "The http method parameter in Request must be specified.");
        return this.bcsHttpClient.execute(createHttpRequest(listBucketRequest), new BucketListResponseHandler());
    }

    public BaiduBCSResponse<ObjectListing> listObject(ListObjectRequest listObjectRequest) throws BCSClientException, BCSServiceException {
        assertParameterNotNull(listObjectRequest, "The request parameter can be null.");
        assertParameterNotNull(listObjectRequest.getHttpMethod(), "The http method parameter in Request must be specified.");
        assertParameterNotNull(listObjectRequest.getBucket(), "The bucket parameter must be specified when listing an bucket.");
        log.debug("list object begin, bucket[" + listObjectRequest.getBucket() + "]");
        BCSHttpRequest createHttpRequest = createHttpRequest(listObjectRequest);
        if (!(listObjectRequest.getPrefix() == null || listObjectRequest.getPrefix().length() == 0)) {
            createHttpRequest.addParameter("prefix", listObjectRequest.getPrefix());
        }
        if (listObjectRequest.getStart() >= 0) {
            createHttpRequest.addParameter("start", String.valueOf(listObjectRequest.getStart()));
        }
        if (listObjectRequest.getLimit() >= 0) {
            createHttpRequest.addParameter("limit", String.valueOf(listObjectRequest.getLimit()));
        }
        if (listObjectRequest.getListModel() != 0) {
            createHttpRequest.addParameter("dir", String.valueOf(listObjectRequest.getListModel()));
        }
        return this.bcsHttpClient.execute(createHttpRequest, new ObjectListResponseHandler());
    }

    public BaiduBCSResponse<Empty> putBucketPolicy(PutBucketPolicyRequest putBucketPolicyRequest) throws BCSClientException, BCSServiceException {
        assertParameterNotNull(putBucketPolicyRequest, "The request parameter can be null.");
        assertParameterNotNull(putBucketPolicyRequest.getHttpMethod(), "The http method parameter in Request must be specified.");
        assertParameterNotNull(putBucketPolicyRequest.getBucket(), "The bucket parameter must be specified when setting policy or acl to a bucket.");
        log.debug("put bucket policy begin, bucket[" + putBucketPolicyRequest.getBucket() + "]");
        if (putBucketPolicyRequest.getPolicy() == null || putBucketPolicyRequest.getAcl() == null) {
            BCSHttpRequest createHttpRequest = createHttpRequest(putBucketPolicyRequest);
            createHttpRequest.addParameter("acl", String.valueOf(1));
            if (putBucketPolicyRequest.getPolicy() != null) {
                byte[] toByteArray = ServiceUtils.toByteArray(putBucketPolicyRequest.getPolicy().toJson());
                createHttpRequest.setContent(new ByteArrayInputStream(toByteArray));
                createHttpRequest.addHeader("Content-Length", String.valueOf(toByteArray.length));
            } else if (putBucketPolicyRequest.getAcl() != null) {
                createHttpRequest.addHeader("x-bs-acl", putBucketPolicyRequest.getAcl().toString());
            }
            return this.bcsHttpClient.execute(createHttpRequest, new VoidResponseHandler());
        }
        throw new BCSClientException("Can set policy or acl to bucket at the same time.");
    }

    public BaiduBCSResponse<Empty> putBucketPolicy(String str, Policy policy) throws BCSClientException, BCSServiceException {
        return putBucketPolicy(new PutBucketPolicyRequest(str, policy));
    }

    public BaiduBCSResponse<Empty> putBucketPolicy(String str, X_BS_ACL x_bs_acl) throws BCSClientException, BCSServiceException {
        return putBucketPolicy(new PutBucketPolicyRequest(str, x_bs_acl));
    }

    /* JADX WARNING: Removed duplicated region for block: B:42:0x0102  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0115  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x018a  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0131  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x0181 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x014a  */
    public com.baidu.inf.iis.bcs.response.BaiduBCSResponse<com.baidu.inf.iis.bcs.model.ObjectMetadata> putObject(com.baidu.inf.iis.bcs.request.PutObjectRequest r10) throws com.baidu.inf.iis.bcs.model.BCSClientException, com.baidu.inf.iis.bcs.model.BCSServiceException {
        /*
        r9 = this;
        r3 = 0;
        r0 = "The request parameter can be null.";
        r9.assertParameterNotNull(r10, r0);
        r0 = r10.getHttpMethod();
        r1 = "The http method parameter in Request must be specified.";
        r9.assertParameterNotNull(r0, r1);
        r0 = r10.getBucket();
        r1 = "The bucket parameter must be specified when uploading an object.";
        r9.assertParameterNotNull(r0, r1);
        r0 = r10.getObject();
        r1 = "The object parameter must be specified when uploading an object.";
        r9.assertParameterNotNull(r0, r1);
        r0 = log;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "put object begin,bucket[";
        r1 = r1.append(r2);
        r2 = r10.getBucket();
        r1 = r1.append(r2);
        r2 = "], object[";
        r1 = r1.append(r2);
        r2 = r10.getObject();
        r1 = r1.append(r2);
        r2 = "]";
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.debug(r1);
        r4 = r9.createHttpRequest(r10);
        r0 = r10.getMetadata();
        if (r0 != 0) goto L_0x0067;
    L_0x005b:
        r0 = new com.baidu.inf.iis.bcs.model.ObjectMetadata;
        r0.m765init();
        r10.setMetadata(r0);
        r0 = r10.getMetadata();
    L_0x0067:
        r1 = r10.getFile();
        if (r1 == 0) goto L_0x00df;
    L_0x006d:
        r2 = r10.getFile();
        r5 = r2.length();
        r0.setContentLength(r5);
        r1 = r0.getContentType();
        if (r1 != 0) goto L_0x0089;
    L_0x007e:
        r1 = com.baidu.inf.iis.bcs.utils.Mimetypes.getInstance();
        r1 = r1.getMimetype(r2);
        r0.setContentType(r1);
    L_0x0089:
        r1 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x00b3 }
        r1.<init>(r2);	 Catch:{ Exception -> 0x00b3 }
        r5 = com.baidu.inf.iis.bcs.utils.ServiceUtils.computeMD5Hash(r1);	 Catch:{ Exception -> 0x0186, all -> 0x0182 }
        r5 = com.baidu.inf.iis.bcs.utils.ServiceUtils.toHex(r5);	 Catch:{ Exception -> 0x0186, all -> 0x0182 }
        r0.setContentMD5(r5);	 Catch:{ Exception -> 0x0186, all -> 0x0182 }
        r1.close();	 Catch:{ Exception -> 0x017b }
    L_0x009c:
        r1 = new com.baidu.inf.iis.bcs.http.RepeatableFileInputStream;	 Catch:{ FileNotFoundException -> 0x00d6 }
        r1.m756init(r2);	 Catch:{ FileNotFoundException -> 0x00d6 }
    L_0x00a1:
        r5 = r0.getContentLength();
        r7 = 0;
        r2 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r2 >= 0) goto L_0x00ed;
    L_0x00ab:
        r0 = new com.baidu.inf.iis.bcs.model.BCSClientException;
        r1 = "Content-Length could not be empty.";
        r0.m759init(r1);
        throw r0;
    L_0x00b3:
        r0 = move-exception;
    L_0x00b4:
        r1 = new com.baidu.inf.iis.bcs.model.BCSClientException;	 Catch:{ all -> 0x00d1 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00d1 }
        r2.<init>();	 Catch:{ all -> 0x00d1 }
        r4 = "Unable to calculate MD5 hash: ";
        r2 = r2.append(r4);	 Catch:{ all -> 0x00d1 }
        r4 = r0.getMessage();	 Catch:{ all -> 0x00d1 }
        r2 = r2.append(r4);	 Catch:{ all -> 0x00d1 }
        r2 = r2.toString();	 Catch:{ all -> 0x00d1 }
        r1.m760init(r2, r0);	 Catch:{ all -> 0x00d1 }
        throw r1;	 Catch:{ all -> 0x00d1 }
    L_0x00d1:
        r0 = move-exception;
    L_0x00d2:
        r3.close();	 Catch:{ Exception -> 0x017e }
    L_0x00d5:
        throw r0;
    L_0x00d6:
        r0 = move-exception;
        r1 = new com.baidu.inf.iis.bcs.model.BCSClientException;
        r2 = "Unable to find file to upload";
        r1.m760init(r2, r0);
        throw r1;
    L_0x00df:
        r1 = r10.getObjectContent();
        if (r0 != 0) goto L_0x00a1;
    L_0x00e5:
        r0 = new com.baidu.inf.iis.bcs.model.BCSClientException;
        r1 = "Put object by Inputstream. Must have Content-Length in objectMetadata.";
        r0.m759init(r1);
        throw r0;
    L_0x00ed:
        r2 = r0.getContentMD5();
        if (r2 != 0) goto L_0x015a;
    L_0x00f3:
        r2 = new com.baidu.inf.iis.bcs.http.MD5DigestCalculatingInputStream;	 Catch:{ NoSuchAlgorithmException -> 0x0152 }
        r2.m752init(r1);	 Catch:{ NoSuchAlgorithmException -> 0x0152 }
        r1 = r2;
    L_0x00f9:
        r4.setContent(r2);
        r3 = r10.getAcl();
        if (r3 == 0) goto L_0x010f;
    L_0x0102:
        r3 = "x-bs-acl";
        r5 = r10.getAcl();
        r5 = r5.toString();
        r4.addHeader(r3, r5);
    L_0x010f:
        r3 = r0.getContentType();
        if (r3 != 0) goto L_0x011a;
    L_0x0115:
        r3 = "application/octet-stream";
        r0.setContentType(r3);
    L_0x011a:
        r9.populateRequestMetadata(r4, r0);
        r3 = r9.bcsHttpClient;
        r5 = new com.baidu.inf.iis.bcs.handler.ObjectMetadataResponseHandler;
        r5.m1497init();
        r3 = r3.execute(r4, r5);
        r2.close();	 Catch:{ Exception -> 0x015d }
    L_0x012b:
        r0 = r0.getContentMD5();
        if (r1 == 0) goto L_0x018a;
    L_0x0131:
        r0 = r1.getMd5Digest();
        r0 = com.baidu.inf.iis.bcs.utils.ServiceUtils.toHex(r0);
        r1 = r0;
    L_0x013a:
        r0 = r3.getResult();
        r0 = (com.baidu.inf.iis.bcs.model.ObjectMetadata) r0;
        r0 = r0.getContentMD5();
        r0 = r1.equalsIgnoreCase(r0);
        if (r0 != 0) goto L_0x0181;
    L_0x014a:
        r0 = new com.baidu.inf.iis.bcs.model.BCSClientException;
        r1 = "Client calculated content md5 didn't match md5 calculated by Baidu BCS. ";
        r0.m759init(r1);
        throw r0;
    L_0x0152:
        r2 = move-exception;
        r5 = log;
        r6 = "No MD5 digest algorithm available.  Unable to calculate checksum and verify data integrity.";
        r5.warn(r6, r2);
    L_0x015a:
        r2 = r1;
        r1 = r3;
        goto L_0x00f9;
    L_0x015d:
        r2 = move-exception;
        r4 = log;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Unable to cleanly close input stream: ";
        r5 = r5.append(r6);
        r6 = r2.getMessage();
        r5 = r5.append(r6);
        r5 = r5.toString();
        r4.warn(r5, r2);
        goto L_0x012b;
    L_0x017b:
        r1 = move-exception;
        goto L_0x009c;
    L_0x017e:
        r1 = move-exception;
        goto L_0x00d5;
    L_0x0181:
        return r3;
    L_0x0182:
        r0 = move-exception;
        r3 = r1;
        goto L_0x00d2;
    L_0x0186:
        r0 = move-exception;
        r3 = r1;
        goto L_0x00b4;
    L_0x018a:
        r1 = r0;
        goto L_0x013a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.inf.iis.bcs.BaiduBCS.putObject(com.baidu.inf.iis.bcs.request.PutObjectRequest):com.baidu.inf.iis.bcs.response.BaiduBCSResponse");
    }

    public BaiduBCSResponse<ObjectMetadata> putObject(String str, String str2, File file) throws BCSClientException, BCSServiceException {
        return putObject(new PutObjectRequest(str, str2, file));
    }

    public BaiduBCSResponse<ObjectMetadata> putObject(String str, String str2, InputStream inputStream, ObjectMetadata objectMetadata) throws BCSClientException, BCSServiceException {
        return putObject(new PutObjectRequest(str, str2, inputStream, objectMetadata));
    }

    public BaiduBCSResponse<Empty> putObjectPolicy(PutObjectPolicyRequest putObjectPolicyRequest) throws BCSClientException, BCSServiceException {
        assertParameterNotNull(putObjectPolicyRequest, "The request parameter can be null.");
        assertParameterNotNull(putObjectPolicyRequest.getHttpMethod(), "The http method parameter in Request must be specified.");
        assertParameterNotNull(putObjectPolicyRequest.getBucket(), "The bucket parameter must be specified when setting policy or acl to an object.");
        assertParameterNotNull(putObjectPolicyRequest.getObject(), "The object parameter must be specified when setting policy or acl to an object.");
        log.debug("put object policy begin, bucket[" + putObjectPolicyRequest.getBucket() + "]");
        if (putObjectPolicyRequest.getPolicy() == null || putObjectPolicyRequest.getAcl() == null) {
            BCSHttpRequest createHttpRequest = createHttpRequest(putObjectPolicyRequest);
            createHttpRequest.addParameter("acl", String.valueOf(1));
            if (putObjectPolicyRequest.getPolicy() != null) {
                byte[] toByteArray = ServiceUtils.toByteArray(putObjectPolicyRequest.getPolicy().toJson());
                createHttpRequest.setContent(new ByteArrayInputStream(toByteArray));
                createHttpRequest.addHeader("Content-Length", String.valueOf(toByteArray.length));
            } else if (putObjectPolicyRequest.getAcl() != null) {
                createHttpRequest.addHeader("x-bs-acl", putObjectPolicyRequest.getAcl().toString());
            }
            return this.bcsHttpClient.execute(createHttpRequest, new VoidResponseHandler());
        }
        throw new BCSClientException("Can set policy or acl to object at the same time.");
    }

    public BaiduBCSResponse<Empty> putObjectPolicy(String str, String str2, Policy policy) throws BCSClientException, BCSServiceException {
        return putObjectPolicy(new PutObjectPolicyRequest(str, str2, policy));
    }

    public BaiduBCSResponse<Empty> putObjectPolicy(String str, String str2, X_BS_ACL x_bs_acl) throws BCSClientException, BCSServiceException {
        return putObjectPolicy(new PutObjectPolicyRequest(str, str2, x_bs_acl));
    }

    public BaiduBCSResponse<ObjectMetadata> putSuperfile(PutSuperfileRequest putSuperfileRequest) throws BCSClientException, BCSServiceException {
        assertParameterNotNull(putSuperfileRequest, "The request parameter can be null.");
        assertParameterNotNull(putSuperfileRequest.getHttpMethod(), "The http method parameter in Request must be specified.");
        assertParameterNotNull(putSuperfileRequest.getSubObjectList(), "The sub-object list parameter in Request must be specified.");
        assertParameterNotNull(putSuperfileRequest.getBucket(), "The bucket parameter must be specified when creating a superfile.");
        assertParameterNotNull(putSuperfileRequest.getObject(), "The object parameter must be specified when creating a superfile.");
        BCSHttpRequest createHttpRequest = createHttpRequest(putSuperfileRequest);
        HashMap hashMap = new HashMap();
        hashMap.put("object_list", new LinkedHashMap());
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= putSuperfileRequest.getSubObjectList().size()) {
                break;
            }
            SuperfileSubObject superfileSubObject = (SuperfileSubObject) putSuperfileRequest.getSubObjectList().get(i2);
            ((Map) hashMap.get("object_list")).put("part_" + i2, new HashMap());
            ((Map) ((Map) hashMap.get("object_list")).get("part_" + i2)).put("url", "bs://" + superfileSubObject.getBucket() + superfileSubObject.getObject());
            ((Map) ((Map) hashMap.get("object_list")).get("part_" + i2)).put("etag", superfileSubObject.getEtag());
            i = i2 + 1;
        }
        byte[] toByteArray = ServiceUtils.toByteArray(new JSONSerializer().deepSerialize(hashMap));
        createHttpRequest.setContent(new ByteArrayInputStream(toByteArray));
        createHttpRequest.addHeader("Content-Length", String.valueOf(toByteArray.length));
        createHttpRequest.addParameter("superfile", String.valueOf(1));
        if (putSuperfileRequest.getObjectMetadata() != null) {
            populateRequestMetadata(createHttpRequest, putSuperfileRequest.getObjectMetadata());
        }
        return this.bcsHttpClient.execute(createHttpRequest, new ObjectMetadataResponseHandler());
    }

    public BaiduBCSResponse<ObjectMetadata> putSuperfile(String str, String str2, List<SuperfileSubObject> list) throws BCSClientException, BCSServiceException {
        return putSuperfile(new PutSuperfileRequest(str, str2, list));
    }

    public BaiduBCSResponse<ObjectMetadata> putSuperfile(String str, String str2, ObjectMetadata objectMetadata, List<SuperfileSubObject> list) throws BCSClientException, BCSServiceException {
        return putSuperfile(new PutSuperfileRequest(str, str2, objectMetadata, list));
    }

    public BaiduBCSResponse<Empty> setObjectMetadata(SetObjectMetadataRequest setObjectMetadataRequest) throws BCSClientException, BCSServiceException {
        assertParameterNotNull(setObjectMetadataRequest, "The request parameter can be null.");
        assertParameterNotNull(setObjectMetadataRequest.getHttpMethod(), "The http method parameter in Request must be specified.");
        assertParameterNotNull(setObjectMetadataRequest.getBucket(), "The bucket parameter must be specified when setting object meta.");
        assertParameterNotNull(setObjectMetadataRequest.getObject(), "The object parameter must be specified when setting object meta.");
        BCSHttpRequest createHttpRequest = createHttpRequest(setObjectMetadataRequest);
        if (setObjectMetadataRequest.getMetadata() != null) {
            setObjectMetadataRequest.getMetadata().setContentMD5("");
            setObjectMetadataRequest.getMetadata().setContentLength(0);
        }
        populateRequestMetadata(createHttpRequest, setObjectMetadataRequest.getMetadata());
        createHttpRequest.addHeader("x-bs-copy-source", "bs://" + setObjectMetadataRequest.getBucket() + setObjectMetadataRequest.getObject());
        return this.bcsHttpClient.execute(createHttpRequest, new VoidResponseHandler());
    }

    public BaiduBCSResponse<Empty> setObjectMetadata(String str, String str2, ObjectMetadata objectMetadata) throws BCSClientException, BCSServiceException {
        return setObjectMetadata(new SetObjectMetadataRequest(str, str2, objectMetadata));
    }

    public String generateUrl(GenerateUrlRequest generateUrlRequest) {
        assertParameterNotNull(generateUrlRequest.getHttpMethod(), "The http method parameter in Request must be specified.");
        assertParameterNotNull(generateUrlRequest.getBucket(), "The bucket parameter must be specified.");
        assertParameterNotNull(generateUrlRequest.getObject(), "The object parameter must be specified.");
        BCSHttpRequest createHttpRequest = createHttpRequest(generateUrlRequest);
        BCSSigner.sign(generateUrlRequest, createHttpRequest, this.credentials, generateUrlRequest.getBcsSignCondition());
        return this.bcsHttpClient.getHttpRequestFactory().buildUri(this.bcsHttpClient.getConfig(), createHttpRequest);
    }

    private BCSHttpRequest createHttpRequest(BaiduBCSRequest baiduBCSRequest) {
        DefaultBCSHttpRequest defaultBCSHttpRequest = new DefaultBCSHttpRequest(baiduBCSRequest);
        defaultBCSHttpRequest.setEndpoint(this.endpoint);
        defaultBCSHttpRequest.setResourcePath(buildResourcePath(baiduBCSRequest.getBucket(), baiduBCSRequest.getObject()));
        defaultBCSHttpRequest.setHttpMethod(baiduBCSRequest.getHttpMethod());
        BCSSigner.sign(baiduBCSRequest, defaultBCSHttpRequest, this.credentials);
        return defaultBCSHttpRequest;
    }

    private void populateRequestMetadata(BCSHttpRequest bCSHttpRequest, ObjectMetadata objectMetadata) {
        if (objectMetadata == null) {
            log.debug("populateRequestMetadata, metadata is null");
            return;
        }
        Map rawMetadata = objectMetadata.getRawMetadata();
        if (rawMetadata != null) {
            for (Entry entry : rawMetadata.entrySet()) {
                bCSHttpRequest.addHeader((String) entry.getKey(), entry.getValue().toString());
            }
        }
        rawMetadata = objectMetadata.getUserMetadata();
        if (rawMetadata != null) {
            for (Entry entry2 : rawMetadata.entrySet()) {
                bCSHttpRequest.addHeader("x-bs-meta-" + ((String) entry2.getKey()), (String) entry2.getValue());
            }
        }
    }

    private void assertParameterNotNull(Object obj, String str) {
        if (obj == null) {
            throw new IllegalArgumentException(str);
        }
    }

    private String buildResourcePath(String str, String str2) {
        if (str2.startsWith("/")) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            if (!str2.equals("/")) {
                stringBuilder.append(ServiceUtils.urlEncode(str2));
            }
            return stringBuilder.toString();
        }
        throw new BCSClientException("BCS object must start with a slash.");
    }
}
