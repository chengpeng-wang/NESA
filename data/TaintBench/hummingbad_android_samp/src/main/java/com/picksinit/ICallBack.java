package com.picksinit;

import java.util.List;

public interface ICallBack {
    void onLoadError(ErrorInfo errorInfo);

    void onLoadSuccess(List list);

    void onPreExecute();
}
