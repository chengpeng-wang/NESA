package com.baidu.inf.iis.bcs.policy;

import java.util.ArrayList;
import java.util.List;

public class Statement {
    private List<PolicyAction> action = new ArrayList();
    private PolicyEffect effect;
    private PolicyIP ip = null;
    private List<String> resource = new ArrayList();
    private PolicyTime time = null;
    private List<String> user = new ArrayList();

    public Statement addAction(PolicyAction policyAction) {
        this.action.add(policyAction);
        return this;
    }

    public Statement addResource(String str) {
        this.resource.add(str);
        return this;
    }

    public Statement addUser(String str) {
        this.user.add(str);
        return this;
    }

    public List<PolicyAction> getAction() {
        return this.action;
    }

    public PolicyEffect getEffect() {
        return this.effect;
    }

    public PolicyIP getIp() {
        return this.ip;
    }

    public List<String> getResource() {
        return this.resource;
    }

    public PolicyTime getTime() {
        return this.time;
    }

    public List<String> getUser() {
        return this.user;
    }

    public void setAction(List<PolicyAction> list) {
        this.action = list;
    }

    public void setEffect(PolicyEffect policyEffect) {
        this.effect = policyEffect;
    }

    public void setIp(PolicyIP policyIP) {
        this.ip = policyIP;
    }

    public void setResource(List<String> list) {
        this.resource = list;
    }

    public void setTime(PolicyTime policyTime) {
        this.time = policyTime;
    }

    public void setUser(List<String> list) {
        this.user = list;
    }
}
