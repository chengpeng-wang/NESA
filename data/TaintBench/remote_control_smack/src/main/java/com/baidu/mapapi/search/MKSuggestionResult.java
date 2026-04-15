package com.baidu.mapapi.search;

import java.util.ArrayList;

public class MKSuggestionResult {
    private int a = 0;
    private ArrayList<MKSuggestionInfo> b;

    /* access modifiers changed from: 0000 */
    public void a(ArrayList<MKSuggestionInfo> arrayList) {
        this.b = arrayList;
    }

    public ArrayList<MKSuggestionInfo> getAllSuggestions() {
        return this.b;
    }

    public MKSuggestionInfo getSuggestion(int i) {
        return (this.b == null || i < 0 || i > this.b.size() - 1) ? null : (MKSuggestionInfo) this.b.get(i);
    }

    public int getSuggestionNum() {
        if (this.b != null) {
            this.a = this.b.size();
        } else {
            this.a = 0;
        }
        return this.a;
    }
}
