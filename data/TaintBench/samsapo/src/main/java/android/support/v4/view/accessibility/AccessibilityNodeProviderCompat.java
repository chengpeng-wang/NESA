package android.support.v4.view.accessibility;

import android.os.Build.VERSION;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class AccessibilityNodeProviderCompat {
    private static final AccessibilityNodeProviderImpl IMPL;
    private final Object mProvider;

    interface AccessibilityNodeProviderImpl {
        Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat accessibilityNodeProviderCompat);
    }

    static class AccessibilityNodeProviderStubImpl implements AccessibilityNodeProviderImpl {
        AccessibilityNodeProviderStubImpl() {
        }

        public Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat accessibilityNodeProviderCompat) {
            AccessibilityNodeProviderCompat accessibilityNodeProviderCompat2 = accessibilityNodeProviderCompat;
            return null;
        }
    }

    static class AccessibilityNodeProviderJellyBeanImpl extends AccessibilityNodeProviderStubImpl {
        AccessibilityNodeProviderJellyBeanImpl() {
        }

        public Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat accessibilityNodeProviderCompat) {
            AnonymousClass1 anonymousClass1 = r6;
            final AccessibilityNodeProviderCompat accessibilityNodeProviderCompat2 = accessibilityNodeProviderCompat;
            AnonymousClass1 anonymousClass12 = new AccessibilityNodeInfoBridge(this) {
                final /* synthetic */ AccessibilityNodeProviderJellyBeanImpl this$0;

                public boolean performAction(int i, int i2, Bundle bundle) {
                    return accessibilityNodeProviderCompat2.performAction(i, i2, bundle);
                }

                public List<Object> findAccessibilityNodeInfosByText(String str, int i) {
                    List findAccessibilityNodeInfosByText = accessibilityNodeProviderCompat2.findAccessibilityNodeInfosByText(str, i);
                    ArrayList arrayList = r11;
                    ArrayList arrayList2 = new ArrayList();
                    ArrayList arrayList3 = arrayList;
                    int size = findAccessibilityNodeInfosByText.size();
                    for (int i2 = 0; i2 < size; i2++) {
                        boolean add = arrayList3.add(((AccessibilityNodeInfoCompat) findAccessibilityNodeInfosByText.get(i2)).getInfo());
                    }
                    return arrayList3;
                }

                public Object createAccessibilityNodeInfo(int i) {
                    AccessibilityNodeInfoCompat createAccessibilityNodeInfo = accessibilityNodeProviderCompat2.createAccessibilityNodeInfo(i);
                    if (createAccessibilityNodeInfo == null) {
                        return null;
                    }
                    return createAccessibilityNodeInfo.getInfo();
                }
            };
            return AccessibilityNodeProviderCompatJellyBean.newAccessibilityNodeProviderBridge(anonymousClass1);
        }
    }

    static class AccessibilityNodeProviderKitKatImpl extends AccessibilityNodeProviderStubImpl {
        AccessibilityNodeProviderKitKatImpl() {
        }

        public Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat accessibilityNodeProviderCompat) {
            AnonymousClass1 anonymousClass1 = r6;
            final AccessibilityNodeProviderCompat accessibilityNodeProviderCompat2 = accessibilityNodeProviderCompat;
            AnonymousClass1 anonymousClass12 = new AccessibilityNodeInfoBridge(this) {
                final /* synthetic */ AccessibilityNodeProviderKitKatImpl this$0;

                public boolean performAction(int i, int i2, Bundle bundle) {
                    return accessibilityNodeProviderCompat2.performAction(i, i2, bundle);
                }

                public List<Object> findAccessibilityNodeInfosByText(String str, int i) {
                    List findAccessibilityNodeInfosByText = accessibilityNodeProviderCompat2.findAccessibilityNodeInfosByText(str, i);
                    ArrayList arrayList = r11;
                    ArrayList arrayList2 = new ArrayList();
                    ArrayList arrayList3 = arrayList;
                    int size = findAccessibilityNodeInfosByText.size();
                    for (int i2 = 0; i2 < size; i2++) {
                        boolean add = arrayList3.add(((AccessibilityNodeInfoCompat) findAccessibilityNodeInfosByText.get(i2)).getInfo());
                    }
                    return arrayList3;
                }

                public Object createAccessibilityNodeInfo(int i) {
                    AccessibilityNodeInfoCompat createAccessibilityNodeInfo = accessibilityNodeProviderCompat2.createAccessibilityNodeInfo(i);
                    if (createAccessibilityNodeInfo == null) {
                        return null;
                    }
                    return createAccessibilityNodeInfo.getInfo();
                }

                public Object findFocus(int i) {
                    AccessibilityNodeInfoCompat findFocus = accessibilityNodeProviderCompat2.findFocus(i);
                    if (findFocus == null) {
                        return null;
                    }
                    return findFocus.getInfo();
                }
            };
            return AccessibilityNodeProviderCompatKitKat.newAccessibilityNodeProviderBridge(anonymousClass1);
        }
    }

    static {
        if (VERSION.SDK_INT >= 19) {
            AccessibilityNodeProviderKitKatImpl accessibilityNodeProviderKitKatImpl = r2;
            AccessibilityNodeProviderKitKatImpl accessibilityNodeProviderKitKatImpl2 = new AccessibilityNodeProviderKitKatImpl();
            IMPL = accessibilityNodeProviderKitKatImpl;
        } else if (VERSION.SDK_INT >= 16) {
            AccessibilityNodeProviderJellyBeanImpl accessibilityNodeProviderJellyBeanImpl = r2;
            AccessibilityNodeProviderJellyBeanImpl accessibilityNodeProviderJellyBeanImpl2 = new AccessibilityNodeProviderJellyBeanImpl();
            IMPL = accessibilityNodeProviderJellyBeanImpl;
        } else {
            AccessibilityNodeProviderStubImpl accessibilityNodeProviderStubImpl = r2;
            AccessibilityNodeProviderStubImpl accessibilityNodeProviderStubImpl2 = new AccessibilityNodeProviderStubImpl();
            IMPL = accessibilityNodeProviderStubImpl;
        }
    }

    public AccessibilityNodeProviderCompat() {
        this.mProvider = IMPL.newAccessibilityNodeProviderBridge(this);
    }

    public AccessibilityNodeProviderCompat(Object obj) {
        this.mProvider = obj;
    }

    public Object getProvider() {
        return this.mProvider;
    }

    public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int i) {
        int i2 = i;
        return null;
    }

    public boolean performAction(int i, int i2, Bundle bundle) {
        int i3 = i;
        int i4 = i2;
        Bundle bundle2 = bundle;
        return false;
    }

    public List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText(String str, int i) {
        String str2 = str;
        int i2 = i;
        return null;
    }

    public AccessibilityNodeInfoCompat findFocus(int i) {
        int i2 = i;
        return null;
    }
}
