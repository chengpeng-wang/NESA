package com.baidu.inf.iis.bcs.policy;

import com.baidu.inf.iis.bcs.model.Pair;
import java.util.ArrayList;
import java.util.List;

public class PolicyTime {
    private List<String> singleTimeList = new ArrayList();
    private List<Pair<String>> timeRangeList = new ArrayList();

    public PolicyTime addSingleTime(String str) {
        this.singleTimeList.add(str);
        return this;
    }

    public PolicyTime addTimeRange(Pair<String> pair) {
        this.timeRangeList.add(pair);
        return this;
    }

    public List<String> getSingleTimeList() {
        return this.singleTimeList;
    }

    public List<Pair<String>> getTimeRangeList() {
        return this.timeRangeList;
    }

    public boolean isEmpty() {
        if (this.singleTimeList.size() > 0 || this.timeRangeList.size() > 0) {
            return false;
        }
        return true;
    }

    public void setSingleTimeList(List<String> list) {
        this.singleTimeList = list;
    }

    public void setTimeRangeList(List<Pair<String>> list) {
        this.timeRangeList = list;
    }
}
