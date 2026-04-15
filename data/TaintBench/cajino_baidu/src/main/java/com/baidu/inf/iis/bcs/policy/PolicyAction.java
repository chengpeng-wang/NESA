package com.baidu.inf.iis.bcs.policy;

public enum PolicyAction {
    all("*"),
    put_bucket_policy("put_bucket_policy"),
    get_bucket_policy("get_bucket_policy"),
    list_object("list_object"),
    delete_bucket("delete_bucket"),
    get_object("get_object"),
    put_object("put_object"),
    delete_object("delete_object"),
    put_object_policy("put_object_policy"),
    get_object_policy("get_object_policy");
    
    private final String value;

    public static PolicyAction toPolicyAction(String str) {
        if (str.equals("*")) {
            return all;
        }
        return valueOf(str);
    }

    private PolicyAction(String str) {
        this.value = str;
    }

    public String toString() {
        return this.value;
    }
}
