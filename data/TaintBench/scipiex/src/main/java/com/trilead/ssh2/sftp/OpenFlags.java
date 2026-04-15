package com.trilead.ssh2.sftp;

public class OpenFlags {
    public static final int SSH_FXF_ACCESS_APPEND_DATA = 8;
    public static final int SSH_FXF_ACCESS_APPEND_DATA_ATOMIC = 16;
    public static final int SSH_FXF_ACCESS_BLOCK_ADVISORY = 512;
    public static final int SSH_FXF_ACCESS_BLOCK_DELETE = 256;
    public static final int SSH_FXF_ACCESS_BLOCK_READ = 64;
    public static final int SSH_FXF_ACCESS_BLOCK_WRITE = 128;
    public static final int SSH_FXF_ACCESS_DELETE_ON_CLOSE = 2048;
    public static final int SSH_FXF_ACCESS_DISPOSITION = 7;
    public static final int SSH_FXF_ACCESS_NOFOLLOW = 1024;
    public static final int SSH_FXF_ACCESS_TEXT_MODE = 32;
    public static final int SSH_FXF_CREATE_NEW = 0;
    public static final int SSH_FXF_CREATE_TRUNCATE = 1;
    public static final int SSH_FXF_OPEN_EXISTING = 2;
    public static final int SSH_FXF_OPEN_OR_CREATE = 3;
    public static final int SSH_FXF_TRUNCATE_EXISTING = 4;
}
