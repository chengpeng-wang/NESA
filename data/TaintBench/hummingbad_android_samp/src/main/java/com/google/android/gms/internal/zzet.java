package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationBannerListener;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationInterstitialListener;
import com.google.ads.mediation.MediationServerParameters;
import com.google.ads.mediation.NetworkExtras;
import com.google.android.gms.ads.internal.client.zzk;
import com.google.android.gms.ads.internal.util.client.zza;
import com.google.android.gms.ads.internal.util.client.zzb;

@zzgk
public final class zzet<NETWORK_EXTRAS extends NetworkExtras, SERVER_PARAMETERS extends MediationServerParameters> implements MediationBannerListener, MediationInterstitialListener {
    /* access modifiers changed from: private|final */
    public final zzej zzyY;

    public zzet(zzej zzej) {
        this.zzyY = zzej;
    }

    public void onClick(MediationBannerAdapter<?, ?> mediationBannerAdapter) {
        zzb.zzaC("Adapter called onClick.");
        if (zzk.zzcE().zzgI()) {
            try {
                this.zzyY.onAdClicked();
                return;
            } catch (RemoteException e) {
                zzb.zzd("Could not call onAdClicked.", e);
                return;
            }
        }
        zzb.zzaE("onClick must be called on the main UI thread.");
        zza.zzIy.post(new Runnable() {
            public void run() {
                try {
                    zzet.this.zzyY.onAdClicked();
                } catch (RemoteException e) {
                    zzb.zzd("Could not call onAdClicked.", e);
                }
            }
        });
    }

    public void onDismissScreen(MediationBannerAdapter<?, ?> mediationBannerAdapter) {
        zzb.zzaC("Adapter called onDismissScreen.");
        if (zzk.zzcE().zzgI()) {
            try {
                this.zzyY.onAdClosed();
                return;
            } catch (RemoteException e) {
                zzb.zzd("Could not call onAdClosed.", e);
                return;
            }
        }
        zzb.zzaE("onDismissScreen must be called on the main UI thread.");
        zza.zzIy.post(new Runnable() {
            public void run() {
                try {
                    zzet.this.zzyY.onAdClosed();
                } catch (RemoteException e) {
                    zzb.zzd("Could not call onAdClosed.", e);
                }
            }
        });
    }

    public void onDismissScreen(MediationInterstitialAdapter<?, ?> mediationInterstitialAdapter) {
        zzb.zzaC("Adapter called onDismissScreen.");
        if (zzk.zzcE().zzgI()) {
            try {
                this.zzyY.onAdClosed();
                return;
            } catch (RemoteException e) {
                zzb.zzd("Could not call onAdClosed.", e);
                return;
            }
        }
        zzb.zzaE("onDismissScreen must be called on the main UI thread.");
        zza.zzIy.post(new Runnable() {
            public void run() {
                try {
                    zzet.this.zzyY.onAdClosed();
                } catch (RemoteException e) {
                    zzb.zzd("Could not call onAdClosed.", e);
                }
            }
        });
    }

    public void onFailedToReceiveAd(MediationBannerAdapter<?, ?> mediationBannerAdapter, final ErrorCode errorCode) {
        zzb.zzaC("Adapter called onFailedToReceiveAd with error. " + errorCode);
        if (zzk.zzcE().zzgI()) {
            try {
                this.zzyY.onAdFailedToLoad(zzeu.zza(errorCode));
                return;
            } catch (RemoteException e) {
                zzb.zzd("Could not call onAdFailedToLoad.", e);
                return;
            }
        }
        zzb.zzaE("onFailedToReceiveAd must be called on the main UI thread.");
        zza.zzIy.post(new Runnable() {
            public void run() {
                try {
                    zzet.this.zzyY.onAdFailedToLoad(zzeu.zza(errorCode));
                } catch (RemoteException e) {
                    zzb.zzd("Could not call onAdFailedToLoad.", e);
                }
            }
        });
    }

    public void onFailedToReceiveAd(MediationInterstitialAdapter<?, ?> mediationInterstitialAdapter, final ErrorCode errorCode) {
        zzb.zzaC("Adapter called onFailedToReceiveAd with error " + errorCode + ".");
        if (zzk.zzcE().zzgI()) {
            try {
                this.zzyY.onAdFailedToLoad(zzeu.zza(errorCode));
                return;
            } catch (RemoteException e) {
                zzb.zzd("Could not call onAdFailedToLoad.", e);
                return;
            }
        }
        zzb.zzaE("onFailedToReceiveAd must be called on the main UI thread.");
        zza.zzIy.post(new Runnable() {
            public void run() {
                try {
                    zzet.this.zzyY.onAdFailedToLoad(zzeu.zza(errorCode));
                } catch (RemoteException e) {
                    zzb.zzd("Could not call onAdFailedToLoad.", e);
                }
            }
        });
    }

    public void onLeaveApplication(MediationBannerAdapter<?, ?> mediationBannerAdapter) {
        zzb.zzaC("Adapter called onLeaveApplication.");
        if (zzk.zzcE().zzgI()) {
            try {
                this.zzyY.onAdLeftApplication();
                return;
            } catch (RemoteException e) {
                zzb.zzd("Could not call onAdLeftApplication.", e);
                return;
            }
        }
        zzb.zzaE("onLeaveApplication must be called on the main UI thread.");
        zza.zzIy.post(new Runnable() {
            public void run() {
                try {
                    zzet.this.zzyY.onAdLeftApplication();
                } catch (RemoteException e) {
                    zzb.zzd("Could not call onAdLeftApplication.", e);
                }
            }
        });
    }

    public void onLeaveApplication(MediationInterstitialAdapter<?, ?> mediationInterstitialAdapter) {
        zzb.zzaC("Adapter called onLeaveApplication.");
        if (zzk.zzcE().zzgI()) {
            try {
                this.zzyY.onAdLeftApplication();
                return;
            } catch (RemoteException e) {
                zzb.zzd("Could not call onAdLeftApplication.", e);
                return;
            }
        }
        zzb.zzaE("onLeaveApplication must be called on the main UI thread.");
        zza.zzIy.post(new Runnable() {
            public void run() {
                try {
                    zzet.this.zzyY.onAdLeftApplication();
                } catch (RemoteException e) {
                    zzb.zzd("Could not call onAdLeftApplication.", e);
                }
            }
        });
    }

    public void onPresentScreen(MediationBannerAdapter<?, ?> mediationBannerAdapter) {
        zzb.zzaC("Adapter called onPresentScreen.");
        if (zzk.zzcE().zzgI()) {
            try {
                this.zzyY.onAdOpened();
                return;
            } catch (RemoteException e) {
                zzb.zzd("Could not call onAdOpened.", e);
                return;
            }
        }
        zzb.zzaE("onPresentScreen must be called on the main UI thread.");
        zza.zzIy.post(new Runnable() {
            public void run() {
                try {
                    zzet.this.zzyY.onAdOpened();
                } catch (RemoteException e) {
                    zzb.zzd("Could not call onAdOpened.", e);
                }
            }
        });
    }

    public void onPresentScreen(MediationInterstitialAdapter<?, ?> mediationInterstitialAdapter) {
        zzb.zzaC("Adapter called onPresentScreen.");
        if (zzk.zzcE().zzgI()) {
            try {
                this.zzyY.onAdOpened();
                return;
            } catch (RemoteException e) {
                zzb.zzd("Could not call onAdOpened.", e);
                return;
            }
        }
        zzb.zzaE("onPresentScreen must be called on the main UI thread.");
        zza.zzIy.post(new Runnable() {
            public void run() {
                try {
                    zzet.this.zzyY.onAdOpened();
                } catch (RemoteException e) {
                    zzb.zzd("Could not call onAdOpened.", e);
                }
            }
        });
    }

    public void onReceivedAd(MediationBannerAdapter<?, ?> mediationBannerAdapter) {
        zzb.zzaC("Adapter called onReceivedAd.");
        if (zzk.zzcE().zzgI()) {
            try {
                this.zzyY.onAdLoaded();
                return;
            } catch (RemoteException e) {
                zzb.zzd("Could not call onAdLoaded.", e);
                return;
            }
        }
        zzb.zzaE("onReceivedAd must be called on the main UI thread.");
        zza.zzIy.post(new Runnable() {
            public void run() {
                try {
                    zzet.this.zzyY.onAdLoaded();
                } catch (RemoteException e) {
                    zzb.zzd("Could not call onAdLoaded.", e);
                }
            }
        });
    }

    public void onReceivedAd(MediationInterstitialAdapter<?, ?> mediationInterstitialAdapter) {
        zzb.zzaC("Adapter called onReceivedAd.");
        if (zzk.zzcE().zzgI()) {
            try {
                this.zzyY.onAdLoaded();
                return;
            } catch (RemoteException e) {
                zzb.zzd("Could not call onAdLoaded.", e);
                return;
            }
        }
        zzb.zzaE("onReceivedAd must be called on the main UI thread.");
        zza.zzIy.post(new Runnable() {
            public void run() {
                try {
                    zzet.this.zzyY.onAdLoaded();
                } catch (RemoteException e) {
                    zzb.zzd("Could not call onAdLoaded.", e);
                }
            }
        });
    }
}
