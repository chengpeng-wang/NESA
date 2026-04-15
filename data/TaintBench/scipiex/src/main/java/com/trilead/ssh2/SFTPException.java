package com.trilead.ssh2;

import com.trilead.ssh2.sftp.ErrorCodes;
import java.io.IOException;

public class SFTPException extends IOException {
    private static final long serialVersionUID = 578654644222421811L;
    private final int sftpErrorCode;
    private final String sftpErrorMessage;

    private static String constructMessage(String s, int errorCode) {
        String[] detail = ErrorCodes.getDescription(errorCode);
        if (detail == null) {
            return new StringBuilder(String.valueOf(s)).append(" (UNKNOW SFTP ERROR CODE)").toString();
        }
        return new StringBuilder(String.valueOf(s)).append(" (").append(detail[0]).append(": ").append(detail[1]).append(")").toString();
    }

    SFTPException(String msg, int errorCode) {
        super(constructMessage(msg, errorCode));
        this.sftpErrorMessage = msg;
        this.sftpErrorCode = errorCode;
    }

    public String getServerErrorMessage() {
        return this.sftpErrorMessage;
    }

    public int getServerErrorCode() {
        return this.sftpErrorCode;
    }

    public String getServerErrorCodeSymbol() {
        String[] detail = ErrorCodes.getDescription(this.sftpErrorCode);
        if (detail == null) {
            return "UNKNOW SFTP ERROR CODE " + this.sftpErrorCode;
        }
        return detail[0];
    }

    public String getServerErrorCodeVerbose() {
        String[] detail = ErrorCodes.getDescription(this.sftpErrorCode);
        if (detail == null) {
            return "The error code " + this.sftpErrorCode + " is unknown.";
        }
        return detail[1];
    }
}
