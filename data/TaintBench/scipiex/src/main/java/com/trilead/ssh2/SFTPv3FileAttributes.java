package com.trilead.ssh2;

import com.trilead.ssh2.sftp.AttribFlags;

public class SFTPv3FileAttributes {
    public Long atime = null;
    public Integer gid = null;
    public Long mtime = null;
    public Integer permissions = null;
    public Long size = null;
    public Integer uid = null;

    public boolean isDirectory() {
        if (this.permissions == null || (this.permissions.intValue() & AttribFlags.SSH_FILEXFER_ATTR_UNTRANSLATED_NAME) == 0) {
            return false;
        }
        return true;
    }

    public boolean isRegularFile() {
        if (this.permissions == null || (this.permissions.intValue() & AttribFlags.SSH_FILEXFER_ATTR_CTIME) == 0) {
            return false;
        }
        return true;
    }

    public boolean isSymlink() {
        if (this.permissions == null || (this.permissions.intValue() & 40960) == 0) {
            return false;
        }
        return true;
    }

    public String getOctalPermissions() {
        if (this.permissions == null) {
            return null;
        }
        String res = Integer.toString(this.permissions.intValue() & 65535, 8);
        StringBuffer sb = new StringBuffer();
        for (int leadingZeros = 7 - res.length(); leadingZeros > 0; leadingZeros--) {
            sb.append('0');
        }
        sb.append(res);
        return sb.toString();
    }
}
