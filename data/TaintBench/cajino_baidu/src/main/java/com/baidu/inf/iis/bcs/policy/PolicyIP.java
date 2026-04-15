package com.baidu.inf.iis.bcs.policy;

import com.baidu.inf.iis.bcs.model.Pair;
import java.util.ArrayList;
import java.util.List;

public class PolicyIP {
    private List<String> cidrIpList = new ArrayList();
    private List<Pair<String>> ipRangeList = new ArrayList();
    private List<String> singleIpList = new ArrayList();

    public PolicyIP addCidrIp(String str) {
        this.cidrIpList.add(str);
        return this;
    }

    public PolicyIP addIpRange(Pair<String> pair) {
        this.ipRangeList.add(pair);
        return this;
    }

    public PolicyIP addSingleIp(String str) {
        this.singleIpList.add(str);
        return this;
    }

    public List<String> getCidrIpList() {
        return this.cidrIpList;
    }

    public List<Pair<String>> getIpRangeList() {
        return this.ipRangeList;
    }

    public List<String> getSingleIpList() {
        return this.singleIpList;
    }

    public boolean isEmpty() {
        if (this.singleIpList.size() > 0 || this.cidrIpList.size() > 0 || this.ipRangeList.size() > 0) {
            return false;
        }
        return true;
    }

    public void setCidrIpList(List<String> list) {
        this.cidrIpList = list;
    }

    public void setIpRangeList(List<Pair<String>> list) {
        this.ipRangeList = list;
    }

    public void setSingleIpList(List<String> list) {
        this.singleIpList = list;
    }
}
