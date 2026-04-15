package com.mopub.common.event;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.common.event.BaseEvent.Category;
import com.mopub.common.event.BaseEvent.Name;
import com.mopub.common.event.BaseEvent.ScribeCategory;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorEvent extends BaseEvent {
    @Nullable
    private final String mErrorClassName;
    @Nullable
    private final String mErrorExceptionClassName;
    @Nullable
    private final String mErrorFileName;
    @Nullable
    private final Integer mErrorLineNumber;
    @Nullable
    private final String mErrorMessage;
    @Nullable
    private final String mErrorMethodName;
    @Nullable
    private final String mErrorStackTrace;

    public static class Builder extends com.mopub.common.event.BaseEvent.Builder {
        /* access modifiers changed from: private */
        @Nullable
        public String mErrorClassName;
        /* access modifiers changed from: private */
        @Nullable
        public String mErrorExceptionClassName;
        /* access modifiers changed from: private */
        @Nullable
        public String mErrorFileName;
        /* access modifiers changed from: private */
        @Nullable
        public Integer mErrorLineNumber;
        /* access modifiers changed from: private */
        @Nullable
        public String mErrorMessage;
        /* access modifiers changed from: private */
        @Nullable
        public String mErrorMethodName;
        /* access modifiers changed from: private */
        @Nullable
        public String mErrorStackTrace;

        public Builder(@NonNull Name name, @NonNull Category category, double samplingRate) {
            super(ScribeCategory.EXCHANGE_CLIENT_ERROR, name, category, samplingRate);
        }

        @NonNull
        public Builder withErrorExceptionClassName(@Nullable String errorExceptionClassName) {
            this.mErrorExceptionClassName = errorExceptionClassName;
            return this;
        }

        @NonNull
        public Builder withErrorMessage(@Nullable String errorMessage) {
            this.mErrorMessage = errorMessage;
            return this;
        }

        @NonNull
        public Builder withErrorStackTrace(@Nullable String errorStackTrace) {
            this.mErrorStackTrace = errorStackTrace;
            return this;
        }

        @NonNull
        public Builder withErrorFileName(@Nullable String errorFileName) {
            this.mErrorFileName = errorFileName;
            return this;
        }

        @NonNull
        public Builder withErrorClassName(@Nullable String errorClassName) {
            this.mErrorClassName = errorClassName;
            return this;
        }

        @NonNull
        public Builder withErrorMethodName(@Nullable String errorMethodName) {
            this.mErrorMethodName = errorMethodName;
            return this;
        }

        @NonNull
        public Builder withErrorLineNumber(@Nullable Integer errorLineNumber) {
            this.mErrorLineNumber = errorLineNumber;
            return this;
        }

        @NonNull
        public Builder withException(@Nullable Exception exception) {
            this.mErrorExceptionClassName = exception.getClass().getName();
            this.mErrorMessage = exception.getMessage();
            StringWriter stringWriter = new StringWriter();
            exception.printStackTrace(new PrintWriter(stringWriter));
            this.mErrorStackTrace = stringWriter.toString();
            if (exception.getStackTrace().length > 0) {
                this.mErrorFileName = exception.getStackTrace()[0].getFileName();
                this.mErrorClassName = exception.getStackTrace()[0].getClassName();
                this.mErrorMethodName = exception.getStackTrace()[0].getMethodName();
                this.mErrorLineNumber = Integer.valueOf(exception.getStackTrace()[0].getLineNumber());
            }
            return this;
        }

        @NonNull
        public ErrorEvent build() {
            return new ErrorEvent(this);
        }
    }

    private ErrorEvent(@NonNull Builder builder) {
        super(builder);
        this.mErrorExceptionClassName = builder.mErrorExceptionClassName;
        this.mErrorMessage = builder.mErrorMessage;
        this.mErrorStackTrace = builder.mErrorStackTrace;
        this.mErrorFileName = builder.mErrorFileName;
        this.mErrorClassName = builder.mErrorClassName;
        this.mErrorMethodName = builder.mErrorMethodName;
        this.mErrorLineNumber = builder.mErrorLineNumber;
    }

    @Nullable
    public String getErrorExceptionClassName() {
        return this.mErrorExceptionClassName;
    }

    @Nullable
    public String getErrorMessage() {
        return this.mErrorMessage;
    }

    @Nullable
    public String getErrorStackTrace() {
        return this.mErrorStackTrace;
    }

    @Nullable
    public String getErrorFileName() {
        return this.mErrorFileName;
    }

    @Nullable
    public String getErrorClassName() {
        return this.mErrorClassName;
    }

    @Nullable
    public String getErrorMethodName() {
        return this.mErrorMethodName;
    }

    @Nullable
    public Integer getErrorLineNumber() {
        return this.mErrorLineNumber;
    }

    public String toString() {
        return super.toString() + "ErrorEvent\nErrorExceptionClassName: " + getErrorExceptionClassName() + "\nErrorMessage: " + getErrorMessage() + "\nErrorStackTrace: " + getErrorStackTrace() + "\nErrorFileName: " + getErrorFileName() + "\nErrorClassName: " + getErrorClassName() + "\nErrorMethodName: " + getErrorMethodName() + "\nErrorLineNumber: " + getErrorLineNumber() + MASTNativeAdConstants.NEWLINE;
    }
}
