package com.baidu.inf.iis.bcs.auth;

import com.baidu.inf.iis.bcs.http.BCSHttpRequest;
import com.baidu.inf.iis.bcs.http.DefaultBCSHttpRequest;
import com.baidu.inf.iis.bcs.http.HttpMethodName;
import com.baidu.inf.iis.bcs.model.BCSClientException;
import com.baidu.inf.iis.bcs.request.BaiduBCSRequest;
import com.baidu.inf.iis.bcs.utils.ServiceUtils;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class BCSSigner {
    public static void main(String[] strArr) throws URISyntaxException {
        AnonymousClass1 anonymousClass1 = new BaiduBCSRequest("bucket", "object", HttpMethodName.GET) {
        };
        BCSSignCondition bCSSignCondition = new BCSSignCondition();
        bCSSignCondition.setIp("192.168.1.1");
        bCSSignCondition.setSize(Long.valueOf(1234));
        bCSSignCondition.setTime(Long.valueOf(4321));
        BCSCredentials bCSCredentials = new BCSCredentials("akakak", "sksksk");
        DefaultBCSHttpRequest defaultBCSHttpRequest = new DefaultBCSHttpRequest();
        defaultBCSHttpRequest.setHttpMethod(anonymousClass1.getHttpMethod());
        defaultBCSHttpRequest.setEndpoint("10.81.2.114:8685");
        sign(anonymousClass1, defaultBCSHttpRequest, bCSCredentials, bCSSignCondition);
        System.out.println(defaultBCSHttpRequest.toString());
    }

    public static void sign(BaiduBCSRequest baiduBCSRequest, BCSHttpRequest bCSHttpRequest, BCSCredentials bCSCredentials) {
        sign(baiduBCSRequest, bCSHttpRequest, bCSCredentials, null);
    }

    public static void sign(BaiduBCSRequest baiduBCSRequest, BCSHttpRequest bCSHttpRequest, BCSCredentials bCSCredentials, BCSSignCondition bCSSignCondition) {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        if (baiduBCSRequest.getHttpMethod() == null) {
            throw new BCSClientException("Sign failed! Param: httpMethod, bucket, object can not be empty!");
        } else if (baiduBCSRequest.getBucket() == null) {
            throw new BCSClientException("Sign failed! Param: httpMethod, bucket, object can not be empty!");
        } else if (baiduBCSRequest.getObject() == null || baiduBCSRequest.getObject().length() == 0) {
            throw new BCSClientException("Sign failed! Param: httpMethod, bucket, object can not be empty!");
        } else {
            stringBuilder.append("MBO");
            stringBuilder2.append("Method=").append(baiduBCSRequest.getHttpMethod().toString()).append("\n");
            stringBuilder2.append("Bucket=").append(baiduBCSRequest.getBucket()).append("\n");
            stringBuilder2.append("Object=").append(baiduBCSRequest.getObject()).append("\n");
            if (bCSSignCondition != null) {
                if (bCSSignCondition.getIp().length() != 0) {
                    stringBuilder.append("I");
                    stringBuilder2.append("Ip=").append(bCSSignCondition.getIp()).append("\n");
                }
                if (bCSSignCondition.getTime().longValue() > 0) {
                    stringBuilder.append("T");
                    stringBuilder2.append("Time=").append(bCSSignCondition.getTime()).append("\n");
                    bCSHttpRequest.addParameter("time", String.valueOf(bCSSignCondition.getTime()));
                }
                if (bCSSignCondition.getSize().longValue() > 0) {
                    stringBuilder.append("S");
                    stringBuilder2.append("Size=").append(bCSSignCondition.getSize()).append("\n");
                    bCSHttpRequest.addParameter("size", String.valueOf(bCSSignCondition.getSize()));
                }
            }
            stringBuilder2.insert(0, "\n");
            stringBuilder2.insert(0, stringBuilder.toString());
            byte[] bArr = new byte[0];
            try {
                SecretKeySpec secretKeySpec = new SecretKeySpec(bCSCredentials.getSecretKey().getBytes(), SigningAlgorithm.HmacSHA1.toString());
                Mac instance = Mac.getInstance(secretKeySpec.getAlgorithm());
                instance.init(secretKeySpec);
                bCSHttpRequest.addParameter("sign", stringBuilder.append(":").append(bCSCredentials.getAccessKey()).append(":").append(ServiceUtils.toBase64(instance.doFinal(ServiceUtils.toByteArray(stringBuilder2.toString())))).toString());
            } catch (NoSuchAlgorithmException e) {
                throw new BCSClientException("NoSuchAlgorithmException. Sign bcs failed!", e);
            } catch (InvalidKeyException e2) {
                throw new BCSClientException("InvalidKeyException. Sign bcs failed!", e2);
            } catch (RuntimeException e3) {
                throw new BCSClientException("Sign bcs failed!", e3);
            }
        }
    }
}
