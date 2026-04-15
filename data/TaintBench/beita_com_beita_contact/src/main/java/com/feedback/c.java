package com.feedback;

import android.os.Handler;
import android.os.Message;

class c extends Handler {
    c() {
    }

    public void handleMessage(Message message) {
        super.handleMessage(message);
        UMFeedbackService.b(message.getData().getString("newReplyContent"));
    }
}
