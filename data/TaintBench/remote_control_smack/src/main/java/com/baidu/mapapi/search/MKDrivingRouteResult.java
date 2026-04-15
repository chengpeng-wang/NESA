package com.baidu.mapapi.search;

import java.util.ArrayList;
import java.util.List;

public class MKDrivingRouteResult {
    private MKPlanNode a;
    private MKPlanNode b;
    private ArrayList<MKRoutePlan> c;
    private MKRouteAddrResult d;
    private int e;
    private boolean f = false;
    private List<MKWpNode> g;

    /* access modifiers changed from: 0000 */
    public void a(int i) {
        this.e = i;
    }

    /* access modifiers changed from: 0000 */
    public void a(MKPlanNode mKPlanNode) {
        this.a = mKPlanNode;
    }

    /* access modifiers changed from: 0000 */
    public void a(MKRouteAddrResult mKRouteAddrResult) {
        this.d = mKRouteAddrResult;
    }

    /* access modifiers changed from: 0000 */
    public void a(ArrayList<MKRoutePlan> arrayList) {
        this.c = arrayList;
    }

    /* access modifiers changed from: 0000 */
    public void a(List<MKWpNode> list) {
        this.g = list;
    }

    /* access modifiers changed from: 0000 */
    public void a(boolean z) {
        this.f = z;
    }

    /* access modifiers changed from: 0000 */
    public void b(MKPlanNode mKPlanNode) {
        this.b = mKPlanNode;
    }

    public MKRouteAddrResult getAddrResult() {
        return this.d;
    }

    public MKPlanNode getEnd() {
        return this.b;
    }

    public int getNumPlan() {
        return this.c != null ? this.c.size() : 0;
    }

    public MKRoutePlan getPlan(int i) {
        return (this.c == null || i < 0 || i > this.c.size() - 1) ? null : (MKRoutePlan) this.c.get(i);
    }

    public MKPlanNode getStart() {
        return this.a;
    }

    public int getTaxiPrice() {
        return this.e;
    }

    public List<MKWpNode> getWpNode() {
        return this.g;
    }

    public boolean isSupportTraffic() {
        return this.f;
    }
}
