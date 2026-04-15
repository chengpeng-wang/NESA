package android.support.v4.view.accessibility;

import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccessibilityNodeInfoCompat {
    public static final int ACTION_ACCESSIBILITY_FOCUS = 64;
    public static final String ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN = "ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN";
    public static final String ACTION_ARGUMENT_HTML_ELEMENT_STRING = "ACTION_ARGUMENT_HTML_ELEMENT_STRING";
    public static final String ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT = "ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT";
    public static final String ACTION_ARGUMENT_SELECTION_END_INT = "ACTION_ARGUMENT_SELECTION_END_INT";
    public static final String ACTION_ARGUMENT_SELECTION_START_INT = "ACTION_ARGUMENT_SELECTION_START_INT";
    public static final int ACTION_CLEAR_ACCESSIBILITY_FOCUS = 128;
    public static final int ACTION_CLEAR_FOCUS = 2;
    public static final int ACTION_CLEAR_SELECTION = 8;
    public static final int ACTION_CLICK = 16;
    public static final int ACTION_COPY = 16384;
    public static final int ACTION_CUT = 65536;
    public static final int ACTION_FOCUS = 1;
    public static final int ACTION_LONG_CLICK = 32;
    public static final int ACTION_NEXT_AT_MOVEMENT_GRANULARITY = 256;
    public static final int ACTION_NEXT_HTML_ELEMENT = 1024;
    public static final int ACTION_PASTE = 32768;
    public static final int ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = 512;
    public static final int ACTION_PREVIOUS_HTML_ELEMENT = 2048;
    public static final int ACTION_SCROLL_BACKWARD = 8192;
    public static final int ACTION_SCROLL_FORWARD = 4096;
    public static final int ACTION_SELECT = 4;
    public static final int ACTION_SET_SELECTION = 131072;
    public static final int FOCUS_ACCESSIBILITY = 2;
    public static final int FOCUS_INPUT = 1;
    private static final AccessibilityNodeInfoImpl IMPL;
    public static final int MOVEMENT_GRANULARITY_CHARACTER = 1;
    public static final int MOVEMENT_GRANULARITY_LINE = 4;
    public static final int MOVEMENT_GRANULARITY_PAGE = 16;
    public static final int MOVEMENT_GRANULARITY_PARAGRAPH = 8;
    public static final int MOVEMENT_GRANULARITY_WORD = 2;
    private final Object mInfo;

    static class AccessibilityNodeInfoStubImpl implements AccessibilityNodeInfoImpl {
        AccessibilityNodeInfoStubImpl() {
        }

        public Object obtain() {
            return null;
        }

        public Object obtain(View view) {
            View view2 = view;
            return null;
        }

        public Object obtain(View view, int i) {
            View view2 = view;
            int i2 = i;
            return null;
        }

        public Object obtain(Object obj) {
            Object obj2 = obj;
            return null;
        }

        public void addAction(Object obj, int i) {
        }

        public void addChild(Object obj, View view) {
        }

        public void addChild(Object obj, View view, int i) {
        }

        public List<Object> findAccessibilityNodeInfosByText(Object obj, String str) {
            Object obj2 = obj;
            String str2 = str;
            return Collections.emptyList();
        }

        public int getActions(Object obj) {
            Object obj2 = obj;
            return 0;
        }

        public void getBoundsInParent(Object obj, Rect rect) {
        }

        public void getBoundsInScreen(Object obj, Rect rect) {
        }

        public Object getChild(Object obj, int i) {
            Object obj2 = obj;
            int i2 = i;
            return null;
        }

        public int getChildCount(Object obj) {
            Object obj2 = obj;
            return 0;
        }

        public CharSequence getClassName(Object obj) {
            Object obj2 = obj;
            return null;
        }

        public CharSequence getContentDescription(Object obj) {
            Object obj2 = obj;
            return null;
        }

        public CharSequence getPackageName(Object obj) {
            Object obj2 = obj;
            return null;
        }

        public Object getParent(Object obj) {
            Object obj2 = obj;
            return null;
        }

        public CharSequence getText(Object obj) {
            Object obj2 = obj;
            return null;
        }

        public int getWindowId(Object obj) {
            Object obj2 = obj;
            return 0;
        }

        public boolean isCheckable(Object obj) {
            Object obj2 = obj;
            return false;
        }

        public boolean isChecked(Object obj) {
            Object obj2 = obj;
            return false;
        }

        public boolean isClickable(Object obj) {
            Object obj2 = obj;
            return false;
        }

        public boolean isEnabled(Object obj) {
            Object obj2 = obj;
            return false;
        }

        public boolean isFocusable(Object obj) {
            Object obj2 = obj;
            return false;
        }

        public boolean isFocused(Object obj) {
            Object obj2 = obj;
            return false;
        }

        public boolean isVisibleToUser(Object obj) {
            Object obj2 = obj;
            return false;
        }

        public boolean isAccessibilityFocused(Object obj) {
            Object obj2 = obj;
            return false;
        }

        public boolean isLongClickable(Object obj) {
            Object obj2 = obj;
            return false;
        }

        public boolean isPassword(Object obj) {
            Object obj2 = obj;
            return false;
        }

        public boolean isScrollable(Object obj) {
            Object obj2 = obj;
            return false;
        }

        public boolean isSelected(Object obj) {
            Object obj2 = obj;
            return false;
        }

        public boolean performAction(Object obj, int i) {
            Object obj2 = obj;
            int i2 = i;
            return false;
        }

        public boolean performAction(Object obj, int i, Bundle bundle) {
            Object obj2 = obj;
            int i2 = i;
            Bundle bundle2 = bundle;
            return false;
        }

        public void setMovementGranularities(Object obj, int i) {
        }

        public int getMovementGranularities(Object obj) {
            Object obj2 = obj;
            return 0;
        }

        public void setBoundsInParent(Object obj, Rect rect) {
        }

        public void setBoundsInScreen(Object obj, Rect rect) {
        }

        public void setCheckable(Object obj, boolean z) {
        }

        public void setChecked(Object obj, boolean z) {
        }

        public void setClassName(Object obj, CharSequence charSequence) {
        }

        public void setClickable(Object obj, boolean z) {
        }

        public void setContentDescription(Object obj, CharSequence charSequence) {
        }

        public void setEnabled(Object obj, boolean z) {
        }

        public void setFocusable(Object obj, boolean z) {
        }

        public void setFocused(Object obj, boolean z) {
        }

        public void setVisibleToUser(Object obj, boolean z) {
        }

        public void setAccessibilityFocused(Object obj, boolean z) {
        }

        public void setLongClickable(Object obj, boolean z) {
        }

        public void setPackageName(Object obj, CharSequence charSequence) {
        }

        public void setParent(Object obj, View view) {
        }

        public void setPassword(Object obj, boolean z) {
        }

        public void setScrollable(Object obj, boolean z) {
        }

        public void setSelected(Object obj, boolean z) {
        }

        public void setSource(Object obj, View view) {
        }

        public void setSource(Object obj, View view, int i) {
        }

        public Object findFocus(Object obj, int i) {
            Object obj2 = obj;
            int i2 = i;
            return null;
        }

        public Object focusSearch(Object obj, int i) {
            Object obj2 = obj;
            int i2 = i;
            return null;
        }

        public void setText(Object obj, CharSequence charSequence) {
        }

        public void recycle(Object obj) {
        }

        public void setParent(Object obj, View view, int i) {
        }

        public String getViewIdResourceName(Object obj) {
            Object obj2 = obj;
            return null;
        }

        public void setViewIdResourceName(Object obj, String str) {
        }

        public int getLiveRegion(Object obj) {
            Object obj2 = obj;
            return 0;
        }

        public void setLiveRegion(Object obj, int i) {
        }
    }

    static class AccessibilityNodeInfoIcsImpl extends AccessibilityNodeInfoStubImpl {
        AccessibilityNodeInfoIcsImpl() {
        }

        public Object obtain() {
            return AccessibilityNodeInfoCompatIcs.obtain();
        }

        public Object obtain(View view) {
            return AccessibilityNodeInfoCompatIcs.obtain(view);
        }

        public Object obtain(Object obj) {
            return AccessibilityNodeInfoCompatIcs.obtain(obj);
        }

        public void addAction(Object obj, int i) {
            AccessibilityNodeInfoCompatIcs.addAction(obj, i);
        }

        public void addChild(Object obj, View view) {
            AccessibilityNodeInfoCompatIcs.addChild(obj, view);
        }

        public List<Object> findAccessibilityNodeInfosByText(Object obj, String str) {
            return AccessibilityNodeInfoCompatIcs.findAccessibilityNodeInfosByText(obj, str);
        }

        public int getActions(Object obj) {
            return AccessibilityNodeInfoCompatIcs.getActions(obj);
        }

        public void getBoundsInParent(Object obj, Rect rect) {
            AccessibilityNodeInfoCompatIcs.getBoundsInParent(obj, rect);
        }

        public void getBoundsInScreen(Object obj, Rect rect) {
            AccessibilityNodeInfoCompatIcs.getBoundsInScreen(obj, rect);
        }

        public Object getChild(Object obj, int i) {
            return AccessibilityNodeInfoCompatIcs.getChild(obj, i);
        }

        public int getChildCount(Object obj) {
            return AccessibilityNodeInfoCompatIcs.getChildCount(obj);
        }

        public CharSequence getClassName(Object obj) {
            return AccessibilityNodeInfoCompatIcs.getClassName(obj);
        }

        public CharSequence getContentDescription(Object obj) {
            return AccessibilityNodeInfoCompatIcs.getContentDescription(obj);
        }

        public CharSequence getPackageName(Object obj) {
            return AccessibilityNodeInfoCompatIcs.getPackageName(obj);
        }

        public Object getParent(Object obj) {
            return AccessibilityNodeInfoCompatIcs.getParent(obj);
        }

        public CharSequence getText(Object obj) {
            return AccessibilityNodeInfoCompatIcs.getText(obj);
        }

        public int getWindowId(Object obj) {
            return AccessibilityNodeInfoCompatIcs.getWindowId(obj);
        }

        public boolean isCheckable(Object obj) {
            return AccessibilityNodeInfoCompatIcs.isCheckable(obj);
        }

        public boolean isChecked(Object obj) {
            return AccessibilityNodeInfoCompatIcs.isChecked(obj);
        }

        public boolean isClickable(Object obj) {
            return AccessibilityNodeInfoCompatIcs.isClickable(obj);
        }

        public boolean isEnabled(Object obj) {
            return AccessibilityNodeInfoCompatIcs.isEnabled(obj);
        }

        public boolean isFocusable(Object obj) {
            return AccessibilityNodeInfoCompatIcs.isFocusable(obj);
        }

        public boolean isFocused(Object obj) {
            return AccessibilityNodeInfoCompatIcs.isFocused(obj);
        }

        public boolean isLongClickable(Object obj) {
            return AccessibilityNodeInfoCompatIcs.isLongClickable(obj);
        }

        public boolean isPassword(Object obj) {
            return AccessibilityNodeInfoCompatIcs.isPassword(obj);
        }

        public boolean isScrollable(Object obj) {
            return AccessibilityNodeInfoCompatIcs.isScrollable(obj);
        }

        public boolean isSelected(Object obj) {
            return AccessibilityNodeInfoCompatIcs.isSelected(obj);
        }

        public boolean performAction(Object obj, int i) {
            return AccessibilityNodeInfoCompatIcs.performAction(obj, i);
        }

        public void setBoundsInParent(Object obj, Rect rect) {
            AccessibilityNodeInfoCompatIcs.setBoundsInParent(obj, rect);
        }

        public void setBoundsInScreen(Object obj, Rect rect) {
            AccessibilityNodeInfoCompatIcs.setBoundsInScreen(obj, rect);
        }

        public void setCheckable(Object obj, boolean z) {
            AccessibilityNodeInfoCompatIcs.setCheckable(obj, z);
        }

        public void setChecked(Object obj, boolean z) {
            AccessibilityNodeInfoCompatIcs.setChecked(obj, z);
        }

        public void setClassName(Object obj, CharSequence charSequence) {
            AccessibilityNodeInfoCompatIcs.setClassName(obj, charSequence);
        }

        public void setClickable(Object obj, boolean z) {
            AccessibilityNodeInfoCompatIcs.setClickable(obj, z);
        }

        public void setContentDescription(Object obj, CharSequence charSequence) {
            AccessibilityNodeInfoCompatIcs.setContentDescription(obj, charSequence);
        }

        public void setEnabled(Object obj, boolean z) {
            AccessibilityNodeInfoCompatIcs.setEnabled(obj, z);
        }

        public void setFocusable(Object obj, boolean z) {
            AccessibilityNodeInfoCompatIcs.setFocusable(obj, z);
        }

        public void setFocused(Object obj, boolean z) {
            AccessibilityNodeInfoCompatIcs.setFocused(obj, z);
        }

        public void setLongClickable(Object obj, boolean z) {
            AccessibilityNodeInfoCompatIcs.setLongClickable(obj, z);
        }

        public void setPackageName(Object obj, CharSequence charSequence) {
            AccessibilityNodeInfoCompatIcs.setPackageName(obj, charSequence);
        }

        public void setParent(Object obj, View view) {
            AccessibilityNodeInfoCompatIcs.setParent(obj, view);
        }

        public void setPassword(Object obj, boolean z) {
            AccessibilityNodeInfoCompatIcs.setPassword(obj, z);
        }

        public void setScrollable(Object obj, boolean z) {
            AccessibilityNodeInfoCompatIcs.setScrollable(obj, z);
        }

        public void setSelected(Object obj, boolean z) {
            AccessibilityNodeInfoCompatIcs.setSelected(obj, z);
        }

        public void setSource(Object obj, View view) {
            AccessibilityNodeInfoCompatIcs.setSource(obj, view);
        }

        public void setText(Object obj, CharSequence charSequence) {
            AccessibilityNodeInfoCompatIcs.setText(obj, charSequence);
        }

        public void recycle(Object obj) {
            AccessibilityNodeInfoCompatIcs.recycle(obj);
        }
    }

    interface AccessibilityNodeInfoImpl {
        void addAction(Object obj, int i);

        void addChild(Object obj, View view);

        void addChild(Object obj, View view, int i);

        List<Object> findAccessibilityNodeInfosByText(Object obj, String str);

        Object findFocus(Object obj, int i);

        Object focusSearch(Object obj, int i);

        int getActions(Object obj);

        void getBoundsInParent(Object obj, Rect rect);

        void getBoundsInScreen(Object obj, Rect rect);

        Object getChild(Object obj, int i);

        int getChildCount(Object obj);

        CharSequence getClassName(Object obj);

        CharSequence getContentDescription(Object obj);

        int getLiveRegion(Object obj);

        int getMovementGranularities(Object obj);

        CharSequence getPackageName(Object obj);

        Object getParent(Object obj);

        CharSequence getText(Object obj);

        String getViewIdResourceName(Object obj);

        int getWindowId(Object obj);

        boolean isAccessibilityFocused(Object obj);

        boolean isCheckable(Object obj);

        boolean isChecked(Object obj);

        boolean isClickable(Object obj);

        boolean isEnabled(Object obj);

        boolean isFocusable(Object obj);

        boolean isFocused(Object obj);

        boolean isLongClickable(Object obj);

        boolean isPassword(Object obj);

        boolean isScrollable(Object obj);

        boolean isSelected(Object obj);

        boolean isVisibleToUser(Object obj);

        Object obtain();

        Object obtain(View view);

        Object obtain(View view, int i);

        Object obtain(Object obj);

        boolean performAction(Object obj, int i);

        boolean performAction(Object obj, int i, Bundle bundle);

        void recycle(Object obj);

        void setAccessibilityFocused(Object obj, boolean z);

        void setBoundsInParent(Object obj, Rect rect);

        void setBoundsInScreen(Object obj, Rect rect);

        void setCheckable(Object obj, boolean z);

        void setChecked(Object obj, boolean z);

        void setClassName(Object obj, CharSequence charSequence);

        void setClickable(Object obj, boolean z);

        void setContentDescription(Object obj, CharSequence charSequence);

        void setEnabled(Object obj, boolean z);

        void setFocusable(Object obj, boolean z);

        void setFocused(Object obj, boolean z);

        void setLiveRegion(Object obj, int i);

        void setLongClickable(Object obj, boolean z);

        void setMovementGranularities(Object obj, int i);

        void setPackageName(Object obj, CharSequence charSequence);

        void setParent(Object obj, View view);

        void setParent(Object obj, View view, int i);

        void setPassword(Object obj, boolean z);

        void setScrollable(Object obj, boolean z);

        void setSelected(Object obj, boolean z);

        void setSource(Object obj, View view);

        void setSource(Object obj, View view, int i);

        void setText(Object obj, CharSequence charSequence);

        void setViewIdResourceName(Object obj, String str);

        void setVisibleToUser(Object obj, boolean z);
    }

    static class AccessibilityNodeInfoJellybeanImpl extends AccessibilityNodeInfoIcsImpl {
        AccessibilityNodeInfoJellybeanImpl() {
        }

        public Object obtain(View view, int i) {
            return AccessibilityNodeInfoCompatJellyBean.obtain(view, i);
        }

        public Object findFocus(Object obj, int i) {
            return AccessibilityNodeInfoCompatJellyBean.findFocus(obj, i);
        }

        public Object focusSearch(Object obj, int i) {
            return AccessibilityNodeInfoCompatJellyBean.focusSearch(obj, i);
        }

        public void addChild(Object obj, View view, int i) {
            AccessibilityNodeInfoCompatJellyBean.addChild(obj, view, i);
        }

        public void setSource(Object obj, View view, int i) {
            AccessibilityNodeInfoCompatJellyBean.setSource(obj, view, i);
        }

        public boolean isVisibleToUser(Object obj) {
            return AccessibilityNodeInfoCompatJellyBean.isVisibleToUser(obj);
        }

        public void setVisibleToUser(Object obj, boolean z) {
            AccessibilityNodeInfoCompatJellyBean.setVisibleToUser(obj, z);
        }

        public boolean isAccessibilityFocused(Object obj) {
            return AccessibilityNodeInfoCompatJellyBean.isAccessibilityFocused(obj);
        }

        public void setAccessibilityFocused(Object obj, boolean z) {
            AccessibilityNodeInfoCompatJellyBean.setAccesibilityFocused(obj, z);
        }

        public boolean performAction(Object obj, int i, Bundle bundle) {
            return AccessibilityNodeInfoCompatJellyBean.performAction(obj, i, bundle);
        }

        public void setMovementGranularities(Object obj, int i) {
            AccessibilityNodeInfoCompatJellyBean.setMovementGranularities(obj, i);
        }

        public int getMovementGranularities(Object obj) {
            return AccessibilityNodeInfoCompatJellyBean.getMovementGranularities(obj);
        }

        public void setParent(Object obj, View view, int i) {
            AccessibilityNodeInfoCompatJellyBean.setParent(obj, view, i);
        }
    }

    static class AccessibilityNodeInfoJellybeanMr2Impl extends AccessibilityNodeInfoJellybeanImpl {
        AccessibilityNodeInfoJellybeanMr2Impl() {
        }

        public String getViewIdResourceName(Object obj) {
            return AccessibilityNodeInfoCompatJellybeanMr2.getViewIdResourceName(obj);
        }

        public void setViewIdResourceName(Object obj, String str) {
            AccessibilityNodeInfoCompatJellybeanMr2.setViewIdResourceName(obj, str);
        }
    }

    static class AccessibilityNodeInfoKitKatImpl extends AccessibilityNodeInfoJellybeanMr2Impl {
        AccessibilityNodeInfoKitKatImpl() {
        }

        public int getLiveRegion(Object obj) {
            return AccessibilityNodeInfoCompatKitKat.getLiveRegion(obj);
        }

        public void setLiveRegion(Object obj, int i) {
            AccessibilityNodeInfoCompatKitKat.setLiveRegion(obj, i);
        }
    }

    static {
        if (VERSION.SDK_INT >= 19) {
            AccessibilityNodeInfoKitKatImpl accessibilityNodeInfoKitKatImpl = r2;
            AccessibilityNodeInfoKitKatImpl accessibilityNodeInfoKitKatImpl2 = new AccessibilityNodeInfoKitKatImpl();
            IMPL = accessibilityNodeInfoKitKatImpl;
        } else if (VERSION.SDK_INT >= 18) {
            AccessibilityNodeInfoJellybeanMr2Impl accessibilityNodeInfoJellybeanMr2Impl = r2;
            AccessibilityNodeInfoJellybeanMr2Impl accessibilityNodeInfoJellybeanMr2Impl2 = new AccessibilityNodeInfoJellybeanMr2Impl();
            IMPL = accessibilityNodeInfoJellybeanMr2Impl;
        } else if (VERSION.SDK_INT >= 16) {
            AccessibilityNodeInfoJellybeanImpl accessibilityNodeInfoJellybeanImpl = r2;
            AccessibilityNodeInfoJellybeanImpl accessibilityNodeInfoJellybeanImpl2 = new AccessibilityNodeInfoJellybeanImpl();
            IMPL = accessibilityNodeInfoJellybeanImpl;
        } else if (VERSION.SDK_INT >= 14) {
            AccessibilityNodeInfoIcsImpl accessibilityNodeInfoIcsImpl = r2;
            AccessibilityNodeInfoIcsImpl accessibilityNodeInfoIcsImpl2 = new AccessibilityNodeInfoIcsImpl();
            IMPL = accessibilityNodeInfoIcsImpl;
        } else {
            AccessibilityNodeInfoStubImpl accessibilityNodeInfoStubImpl = r2;
            AccessibilityNodeInfoStubImpl accessibilityNodeInfoStubImpl2 = new AccessibilityNodeInfoStubImpl();
            IMPL = accessibilityNodeInfoStubImpl;
        }
    }

    static AccessibilityNodeInfoCompat wrapNonNullInstance(Object obj) {
        Object obj2 = obj;
        if (obj2 == null) {
            return null;
        }
        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = r4;
        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2 = new AccessibilityNodeInfoCompat(obj2);
        return accessibilityNodeInfoCompat;
    }

    public AccessibilityNodeInfoCompat(Object obj) {
        this.mInfo = obj;
    }

    public Object getInfo() {
        return this.mInfo;
    }

    public static AccessibilityNodeInfoCompat obtain(View view) {
        return wrapNonNullInstance(IMPL.obtain(view));
    }

    public static AccessibilityNodeInfoCompat obtain(View view, int i) {
        return wrapNonNullInstance(IMPL.obtain(view, i));
    }

    public static AccessibilityNodeInfoCompat obtain() {
        return wrapNonNullInstance(IMPL.obtain());
    }

    public static AccessibilityNodeInfoCompat obtain(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        return wrapNonNullInstance(IMPL.obtain(accessibilityNodeInfoCompat.mInfo));
    }

    public void setSource(View view) {
        View view2 = view;
        IMPL.setSource(this.mInfo, view2);
    }

    public void setSource(View view, int i) {
        View view2 = view;
        int i2 = i;
        IMPL.setSource(this.mInfo, view2, i2);
    }

    public AccessibilityNodeInfoCompat findFocus(int i) {
        return wrapNonNullInstance(IMPL.findFocus(this.mInfo, i));
    }

    public AccessibilityNodeInfoCompat focusSearch(int i) {
        return wrapNonNullInstance(IMPL.focusSearch(this.mInfo, i));
    }

    public int getWindowId() {
        return IMPL.getWindowId(this.mInfo);
    }

    public int getChildCount() {
        return IMPL.getChildCount(this.mInfo);
    }

    public AccessibilityNodeInfoCompat getChild(int i) {
        return wrapNonNullInstance(IMPL.getChild(this.mInfo, i));
    }

    public void addChild(View view) {
        View view2 = view;
        IMPL.addChild(this.mInfo, view2);
    }

    public void addChild(View view, int i) {
        View view2 = view;
        int i2 = i;
        IMPL.addChild(this.mInfo, view2, i2);
    }

    public int getActions() {
        return IMPL.getActions(this.mInfo);
    }

    public void addAction(int i) {
        int i2 = i;
        IMPL.addAction(this.mInfo, i2);
    }

    public boolean performAction(int i) {
        return IMPL.performAction(this.mInfo, i);
    }

    public boolean performAction(int i, Bundle bundle) {
        return IMPL.performAction(this.mInfo, i, bundle);
    }

    public void setMovementGranularities(int i) {
        int i2 = i;
        IMPL.setMovementGranularities(this.mInfo, i2);
    }

    public int getMovementGranularities() {
        return IMPL.getMovementGranularities(this.mInfo);
    }

    public List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText(String str) {
        String str2 = str;
        ArrayList arrayList = r11;
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = arrayList;
        List findAccessibilityNodeInfosByText = IMPL.findAccessibilityNodeInfosByText(this.mInfo, str2);
        int size = findAccessibilityNodeInfosByText.size();
        for (int i = 0; i < size; i++) {
            arrayList = arrayList3;
            AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = r11;
            AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2 = new AccessibilityNodeInfoCompat(findAccessibilityNodeInfosByText.get(i));
            boolean add = arrayList.add(accessibilityNodeInfoCompat);
        }
        return arrayList3;
    }

    public AccessibilityNodeInfoCompat getParent() {
        return wrapNonNullInstance(IMPL.getParent(this.mInfo));
    }

    public void setParent(View view) {
        View view2 = view;
        IMPL.setParent(this.mInfo, view2);
    }

    public void setParent(View view, int i) {
        View view2 = view;
        int i2 = i;
        IMPL.setParent(this.mInfo, view2, i2);
    }

    public void getBoundsInParent(Rect rect) {
        Rect rect2 = rect;
        IMPL.getBoundsInParent(this.mInfo, rect2);
    }

    public void setBoundsInParent(Rect rect) {
        Rect rect2 = rect;
        IMPL.setBoundsInParent(this.mInfo, rect2);
    }

    public void getBoundsInScreen(Rect rect) {
        Rect rect2 = rect;
        IMPL.getBoundsInScreen(this.mInfo, rect2);
    }

    public void setBoundsInScreen(Rect rect) {
        Rect rect2 = rect;
        IMPL.setBoundsInScreen(this.mInfo, rect2);
    }

    public boolean isCheckable() {
        return IMPL.isCheckable(this.mInfo);
    }

    public void setCheckable(boolean z) {
        boolean z2 = z;
        IMPL.setCheckable(this.mInfo, z2);
    }

    public boolean isChecked() {
        return IMPL.isChecked(this.mInfo);
    }

    public void setChecked(boolean z) {
        boolean z2 = z;
        IMPL.setChecked(this.mInfo, z2);
    }

    public boolean isFocusable() {
        return IMPL.isFocusable(this.mInfo);
    }

    public void setFocusable(boolean z) {
        boolean z2 = z;
        IMPL.setFocusable(this.mInfo, z2);
    }

    public boolean isFocused() {
        return IMPL.isFocused(this.mInfo);
    }

    public void setFocused(boolean z) {
        boolean z2 = z;
        IMPL.setFocused(this.mInfo, z2);
    }

    public boolean isVisibleToUser() {
        return IMPL.isVisibleToUser(this.mInfo);
    }

    public void setVisibleToUser(boolean z) {
        boolean z2 = z;
        IMPL.setVisibleToUser(this.mInfo, z2);
    }

    public boolean isAccessibilityFocused() {
        return IMPL.isAccessibilityFocused(this.mInfo);
    }

    public void setAccessibilityFocused(boolean z) {
        boolean z2 = z;
        IMPL.setAccessibilityFocused(this.mInfo, z2);
    }

    public boolean isSelected() {
        return IMPL.isSelected(this.mInfo);
    }

    public void setSelected(boolean z) {
        boolean z2 = z;
        IMPL.setSelected(this.mInfo, z2);
    }

    public boolean isClickable() {
        return IMPL.isClickable(this.mInfo);
    }

    public void setClickable(boolean z) {
        boolean z2 = z;
        IMPL.setClickable(this.mInfo, z2);
    }

    public boolean isLongClickable() {
        return IMPL.isLongClickable(this.mInfo);
    }

    public void setLongClickable(boolean z) {
        boolean z2 = z;
        IMPL.setLongClickable(this.mInfo, z2);
    }

    public boolean isEnabled() {
        return IMPL.isEnabled(this.mInfo);
    }

    public void setEnabled(boolean z) {
        boolean z2 = z;
        IMPL.setEnabled(this.mInfo, z2);
    }

    public boolean isPassword() {
        return IMPL.isPassword(this.mInfo);
    }

    public void setPassword(boolean z) {
        boolean z2 = z;
        IMPL.setPassword(this.mInfo, z2);
    }

    public boolean isScrollable() {
        return IMPL.isScrollable(this.mInfo);
    }

    public void setScrollable(boolean z) {
        boolean z2 = z;
        IMPL.setScrollable(this.mInfo, z2);
    }

    public CharSequence getPackageName() {
        return IMPL.getPackageName(this.mInfo);
    }

    public void setPackageName(CharSequence charSequence) {
        CharSequence charSequence2 = charSequence;
        IMPL.setPackageName(this.mInfo, charSequence2);
    }

    public CharSequence getClassName() {
        return IMPL.getClassName(this.mInfo);
    }

    public void setClassName(CharSequence charSequence) {
        CharSequence charSequence2 = charSequence;
        IMPL.setClassName(this.mInfo, charSequence2);
    }

    public CharSequence getText() {
        return IMPL.getText(this.mInfo);
    }

    public void setText(CharSequence charSequence) {
        CharSequence charSequence2 = charSequence;
        IMPL.setText(this.mInfo, charSequence2);
    }

    public CharSequence getContentDescription() {
        return IMPL.getContentDescription(this.mInfo);
    }

    public void setContentDescription(CharSequence charSequence) {
        CharSequence charSequence2 = charSequence;
        IMPL.setContentDescription(this.mInfo, charSequence2);
    }

    public void recycle() {
        IMPL.recycle(this.mInfo);
    }

    public void setViewIdResourceName(String str) {
        String str2 = str;
        IMPL.setViewIdResourceName(this.mInfo, str2);
    }

    public String getViewIdResourceName() {
        return IMPL.getViewIdResourceName(this.mInfo);
    }

    public int getLiveRegion() {
        return IMPL.getLiveRegion(this.mInfo);
    }

    public void setLiveRegion(int i) {
        int i2 = i;
        IMPL.setLiveRegion(this.mInfo, i2);
    }

    public int hashCode() {
        return this.mInfo == null ? 0 : this.mInfo.hashCode();
    }

    public boolean equals(Object obj) {
        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = obj;
        if (this == accessibilityNodeInfoCompat) {
            return true;
        }
        if (accessibilityNodeInfoCompat == null) {
            return false;
        }
        if (getClass() != accessibilityNodeInfoCompat.getClass()) {
            return false;
        }
        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2 = accessibilityNodeInfoCompat;
        if (this.mInfo == null) {
            if (accessibilityNodeInfoCompat2.mInfo != null) {
                return false;
            }
        } else if (!this.mInfo.equals(accessibilityNodeInfoCompat2.mInfo)) {
            return false;
        }
        return true;
    }

    public String toString() {
        StringBuilder stringBuilder = r8;
        StringBuilder stringBuilder2 = new StringBuilder();
        StringBuilder stringBuilder3 = stringBuilder;
        stringBuilder = stringBuilder3.append(super.toString());
        Rect rect = r8;
        Rect rect2 = new Rect();
        Rect rect3 = rect;
        getBoundsInParent(rect3);
        stringBuilder = stringBuilder3;
        stringBuilder2 = r8;
        StringBuilder stringBuilder4 = new StringBuilder();
        stringBuilder = stringBuilder.append(stringBuilder2.append("; boundsInParent: ").append(rect3).toString());
        getBoundsInScreen(rect3);
        stringBuilder = stringBuilder3;
        stringBuilder2 = r8;
        stringBuilder4 = new StringBuilder();
        stringBuilder = stringBuilder.append(stringBuilder2.append("; boundsInScreen: ").append(rect3).toString());
        stringBuilder = stringBuilder3.append("; packageName: ").append(getPackageName());
        stringBuilder = stringBuilder3.append("; className: ").append(getClassName());
        stringBuilder = stringBuilder3.append("; text: ").append(getText());
        stringBuilder = stringBuilder3.append("; contentDescription: ").append(getContentDescription());
        stringBuilder = stringBuilder3.append("; viewId: ").append(getViewIdResourceName());
        stringBuilder = stringBuilder3.append("; checkable: ").append(isCheckable());
        stringBuilder = stringBuilder3.append("; checked: ").append(isChecked());
        stringBuilder = stringBuilder3.append("; focusable: ").append(isFocusable());
        stringBuilder = stringBuilder3.append("; focused: ").append(isFocused());
        stringBuilder = stringBuilder3.append("; selected: ").append(isSelected());
        stringBuilder = stringBuilder3.append("; clickable: ").append(isClickable());
        stringBuilder = stringBuilder3.append("; longClickable: ").append(isLongClickable());
        stringBuilder = stringBuilder3.append("; enabled: ").append(isEnabled());
        stringBuilder = stringBuilder3.append("; password: ").append(isPassword());
        stringBuilder = stringBuilder3;
        stringBuilder2 = r8;
        stringBuilder4 = new StringBuilder();
        stringBuilder = stringBuilder.append(stringBuilder2.append("; scrollable: ").append(isScrollable()).toString());
        stringBuilder = stringBuilder3.append("; [");
        int actions = getActions();
        while (actions != 0) {
            int numberOfTrailingZeros = 1 << Integer.numberOfTrailingZeros(actions);
            actions &= numberOfTrailingZeros ^ -1;
            stringBuilder = stringBuilder3.append(getActionSymbolicName(numberOfTrailingZeros));
            if (actions != 0) {
                stringBuilder = stringBuilder3.append(", ");
            }
        }
        stringBuilder = stringBuilder3.append("]");
        return stringBuilder3.toString();
    }

    private static String getActionSymbolicName(int i) {
        switch (i) {
            case 1:
                return "ACTION_FOCUS";
            case 2:
                return "ACTION_CLEAR_FOCUS";
            case 4:
                return "ACTION_SELECT";
            case 8:
                return "ACTION_CLEAR_SELECTION";
            case 16:
                return "ACTION_CLICK";
            case 32:
                return "ACTION_LONG_CLICK";
            case 64:
                return "ACTION_ACCESSIBILITY_FOCUS";
            case 128:
                return "ACTION_CLEAR_ACCESSIBILITY_FOCUS";
            case 256:
                return "ACTION_NEXT_AT_MOVEMENT_GRANULARITY";
            case 512:
                return "ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY";
            case 1024:
                return "ACTION_NEXT_HTML_ELEMENT";
            case 2048:
                return "ACTION_PREVIOUS_HTML_ELEMENT";
            case 4096:
                return "ACTION_SCROLL_FORWARD";
            case 8192:
                return "ACTION_SCROLL_BACKWARD";
            case 16384:
                return "ACTION_COPY";
            case 32768:
                return "ACTION_PASTE";
            case 65536:
                return "ACTION_CUT";
            case 131072:
                return "ACTION_SET_SELECTION";
            default:
                return "ACTION_UNKNOWN";
        }
    }
}
