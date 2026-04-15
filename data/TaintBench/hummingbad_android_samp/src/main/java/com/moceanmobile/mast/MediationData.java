package com.moceanmobile.mast;

public class MediationData {
    private String mediationAdFormat;
    private String mediationAdId;
    private String mediationNetworkId;
    private String mediationNetworkName;
    private String mediationSource;

    public MediationData(String mediationNetworkId, String mediationNetworkName, String mediationSource, String mediationAdId, String mediationAdFormat) {
        this.mediationNetworkId = mediationNetworkId;
        this.mediationNetworkName = mediationNetworkName;
        this.mediationAdFormat = mediationAdFormat;
        this.mediationSource = mediationSource;
        this.mediationAdId = mediationAdId;
    }

    public String getMediationAdFormat() {
        return this.mediationAdFormat;
    }

    public void setMediationAdFormat(String mediationAdFormat) {
        this.mediationAdFormat = mediationAdFormat;
    }

    public String getMediationNetworkId() {
        return this.mediationNetworkId;
    }

    public void setMediationNetworkId(String mediationNetworkId) {
        this.mediationNetworkId = mediationNetworkId;
    }

    public String getMediationNetworkName() {
        return this.mediationNetworkName;
    }

    public void setMediationNetworkName(String mediationNetworkName) {
        this.mediationNetworkName = mediationNetworkName;
    }

    public String getMediationSource() {
        return this.mediationSource;
    }

    public void setMediationSource(String mediationSource) {
        this.mediationSource = mediationSource;
    }

    public String getMediationAdId() {
        return this.mediationAdId;
    }

    public void setMediationAdId(String mediationAdId) {
        this.mediationAdId = mediationAdId;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("MediationData:{");
        stringBuffer.append("ID:");
        stringBuffer.append(getMediationNetworkId());
        stringBuffer.append(", Name:");
        stringBuffer.append(getMediationNetworkName());
        stringBuffer.append(", Source:");
        stringBuffer.append(getMediationSource());
        stringBuffer.append(", AdFormat:");
        stringBuffer.append(getMediationAdFormat());
        stringBuffer.append(", AdId:");
        stringBuffer.append(getMediationAdId());
        stringBuffer.append("}");
        return stringBuffer.toString();
    }
}
