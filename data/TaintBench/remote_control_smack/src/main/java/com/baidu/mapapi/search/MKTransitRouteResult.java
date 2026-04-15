package com.baidu.mapapi.search;

import java.util.ArrayList;

public class MKTransitRouteResult {
    private MKPlanNode a;
    private MKPlanNode b;
    private ArrayList<MKTransitRoutePlan> c;
    private MKRouteAddrResult d;
    private int e;

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
    public void a(ArrayList<MKTransitRoutePlan> arrayList) {
        this.c = arrayList;
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

    public MKTransitRoutePlan getPlan(int i) {
        return (this.c == null || i < 0 || i > this.c.size() - 1) ? null : (MKTransitRoutePlan) this.c.get(i);
    }

    public MKPlanNode getStart() {
        return this.a;
    }

    public int getTaxiPrice() {
        return this.e;
    }
}
