package android.support.v4.widget;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewParentCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityManagerCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class ExploreByTouchHelper extends AccessibilityDelegateCompat {
    private static final String DEFAULT_CLASS_NAME = View.class.getName();
    public static final int INVALID_ID = Integer.MIN_VALUE;
    private int mFocusedVirtualViewId = INVALID_ID;
    private int mHoveredVirtualViewId = INVALID_ID;
    private final AccessibilityManager mManager;
    private ExploreByTouchNodeProvider mNodeProvider;
    private final int[] mTempGlobalRect = new int[2];
    private final Rect mTempParentRect;
    private final Rect mTempScreenRect;
    private final Rect mTempVisibleRect;
    private final View mView;

    private class ExploreByTouchNodeProvider extends AccessibilityNodeProviderCompat {
        final /* synthetic */ ExploreByTouchHelper this$0;

        private ExploreByTouchNodeProvider(ExploreByTouchHelper exploreByTouchHelper) {
            this.this$0 = exploreByTouchHelper;
        }

        /* synthetic */ ExploreByTouchNodeProvider(ExploreByTouchHelper exploreByTouchHelper, AnonymousClass1 anonymousClass1) {
            AnonymousClass1 anonymousClass12 = anonymousClass1;
            this(exploreByTouchHelper);
        }

        public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int i) {
            return this.this$0.createNode(i);
        }

        public boolean performAction(int i, int i2, Bundle bundle) {
            return this.this$0.performAction(i, i2, bundle);
        }
    }

    public abstract int getVirtualViewAt(float f, float f2);

    public abstract void getVisibleVirtualViews(List<Integer> list);

    public abstract boolean onPerformActionForVirtualView(int i, int i2, Bundle bundle);

    public abstract void onPopulateEventForVirtualView(int i, AccessibilityEvent accessibilityEvent);

    public abstract void onPopulateNodeForVirtualView(int i, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat);

    public ExploreByTouchHelper(View view) {
        View view2 = view;
        Rect rect = r6;
        Rect rect2 = new Rect();
        this.mTempScreenRect = rect;
        rect = r6;
        rect2 = new Rect();
        this.mTempParentRect = rect;
        rect = r6;
        rect2 = new Rect();
        this.mTempVisibleRect = rect;
        if (view2 == null) {
            IllegalArgumentException illegalArgumentException = r6;
            IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException("View may not be null");
            throw illegalArgumentException;
        }
        this.mView = view2;
        this.mManager = (AccessibilityManager) view2.getContext().getSystemService("accessibility");
    }

    public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View view) {
        View view2 = view;
        if (this.mNodeProvider == null) {
            ExploreByTouchNodeProvider exploreByTouchNodeProvider = r7;
            ExploreByTouchNodeProvider exploreByTouchNodeProvider2 = new ExploreByTouchNodeProvider(this, null);
            this.mNodeProvider = exploreByTouchNodeProvider;
        }
        return this.mNodeProvider;
    }

    public boolean dispatchHoverEvent(MotionEvent motionEvent) {
        MotionEvent motionEvent2 = motionEvent;
        if (!this.mManager.isEnabled() || !AccessibilityManagerCompat.isTouchExplorationEnabled(this.mManager)) {
            return false;
        }
        switch (motionEvent2.getAction()) {
            case MotionEventCompat.ACTION_HOVER_MOVE /*7*/:
            case 9:
                int virtualViewAt = getVirtualViewAt(motionEvent2.getX(), motionEvent2.getY());
                updateHoveredVirtualView(virtualViewAt);
                return virtualViewAt != Integer.MIN_VALUE;
            case 10:
                if (this.mFocusedVirtualViewId == INVALID_ID) {
                    return false;
                }
                updateHoveredVirtualView(INVALID_ID);
                return true;
            default:
                return false;
        }
    }

    public boolean sendEventForVirtualView(int i, int i2) {
        int i3 = i;
        int i4 = i2;
        if (i3 == Integer.MIN_VALUE || !this.mManager.isEnabled()) {
            return false;
        }
        ViewParent parent = this.mView.getParent();
        if (parent == null) {
            return false;
        }
        return ViewParentCompat.requestSendAccessibilityEvent(parent, this.mView, createEvent(i3, i4));
    }

    public void invalidateRoot() {
        invalidateVirtualView(-1);
    }

    public void invalidateVirtualView(int i) {
        boolean sendEventForVirtualView = sendEventForVirtualView(i, 2048);
    }

    public int getFocusedVirtualView() {
        return this.mFocusedVirtualViewId;
    }

    private void updateHoveredVirtualView(int i) {
        int i2 = i;
        if (this.mHoveredVirtualViewId != i2) {
            int i3 = this.mHoveredVirtualViewId;
            this.mHoveredVirtualViewId = i2;
            boolean sendEventForVirtualView = sendEventForVirtualView(i2, 128);
            sendEventForVirtualView = sendEventForVirtualView(i3, 256);
        }
    }

    private AccessibilityEvent createEvent(int i, int i2) {
        int i3 = i;
        int i4 = i2;
        switch (i3) {
            case -1:
                return createEventForHost(i4);
            default:
                return createEventForChild(i3, i4);
        }
    }

    private AccessibilityEvent createEventForHost(int i) {
        AccessibilityEvent obtain = AccessibilityEvent.obtain(i);
        ViewCompat.onInitializeAccessibilityEvent(this.mView, obtain);
        return obtain;
    }

    private AccessibilityEvent createEventForChild(int i, int i2) {
        int i3 = i;
        AccessibilityEvent obtain = AccessibilityEvent.obtain(i2);
        obtain.setEnabled(true);
        obtain.setClassName(DEFAULT_CLASS_NAME);
        onPopulateEventForVirtualView(i3, obtain);
        if (obtain.getText().isEmpty() && obtain.getContentDescription() == null) {
            RuntimeException runtimeException = r8;
            RuntimeException runtimeException2 = new RuntimeException("Callbacks must add text or a content description in populateEventForVirtualViewId()");
            throw runtimeException;
        }
        obtain.setPackageName(this.mView.getContext().getPackageName());
        AccessibilityEventCompat.asRecord(obtain).setSource(this.mView, i3);
        return obtain;
    }

    /* access modifiers changed from: private */
    public AccessibilityNodeInfoCompat createNode(int i) {
        int i2 = i;
        switch (i2) {
            case -1:
                return createNodeForHost();
            default:
                return createNodeForChild(i2);
        }
    }

    private AccessibilityNodeInfoCompat createNodeForHost() {
        AccessibilityNodeInfoCompat obtain = AccessibilityNodeInfoCompat.obtain(this.mView);
        ViewCompat.onInitializeAccessibilityNodeInfo(this.mView, obtain);
        LinkedList linkedList = r8;
        LinkedList linkedList2 = new LinkedList();
        LinkedList linkedList3 = linkedList;
        getVisibleVirtualViews(linkedList3);
        Iterator it = linkedList3.iterator();
        while (it.hasNext()) {
            obtain.addChild(this.mView, ((Integer) it.next()).intValue());
        }
        return obtain;
    }

    private AccessibilityNodeInfoCompat createNodeForChild(int i) {
        int i2 = i;
        AccessibilityNodeInfoCompat obtain = AccessibilityNodeInfoCompat.obtain();
        obtain.setEnabled(true);
        obtain.setClassName(DEFAULT_CLASS_NAME);
        onPopulateNodeForVirtualView(i2, obtain);
        RuntimeException runtimeException;
        RuntimeException runtimeException2;
        if (obtain.getText() == null && obtain.getContentDescription() == null) {
            runtimeException = r9;
            runtimeException2 = new RuntimeException("Callbacks must add text or a content description in populateNodeForVirtualViewId()");
            throw runtimeException;
        }
        obtain.getBoundsInParent(this.mTempParentRect);
        if (this.mTempParentRect.isEmpty()) {
            runtimeException = r9;
            runtimeException2 = new RuntimeException("Callbacks must set parent bounds in populateNodeForVirtualViewId()");
            throw runtimeException;
        }
        int actions = obtain.getActions();
        if ((actions & 64) != 0) {
            runtimeException = r9;
            runtimeException2 = new RuntimeException("Callbacks must not add ACTION_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
            throw runtimeException;
        } else if ((actions & 128) != 0) {
            runtimeException = r9;
            runtimeException2 = new RuntimeException("Callbacks must not add ACTION_CLEAR_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
            throw runtimeException;
        } else {
            obtain.setPackageName(this.mView.getContext().getPackageName());
            obtain.setSource(this.mView, i2);
            obtain.setParent(this.mView);
            if (this.mFocusedVirtualViewId == i2) {
                obtain.setAccessibilityFocused(true);
                obtain.addAction(128);
            } else {
                obtain.setAccessibilityFocused(false);
                obtain.addAction(64);
            }
            if (intersectVisibleToUser(this.mTempParentRect)) {
                obtain.setVisibleToUser(true);
                obtain.setBoundsInParent(this.mTempParentRect);
            }
            this.mView.getLocationOnScreen(this.mTempGlobalRect);
            int i3 = this.mTempGlobalRect[0];
            int i4 = this.mTempGlobalRect[1];
            this.mTempScreenRect.set(this.mTempParentRect);
            this.mTempScreenRect.offset(i3, i4);
            obtain.setBoundsInScreen(this.mTempScreenRect);
            return obtain;
        }
    }

    /* access modifiers changed from: private */
    public boolean performAction(int i, int i2, Bundle bundle) {
        int i3 = i;
        int i4 = i2;
        Bundle bundle2 = bundle;
        switch (i3) {
            case -1:
                return performActionForHost(i4, bundle2);
            default:
                return performActionForChild(i3, i4, bundle2);
        }
    }

    private boolean performActionForHost(int i, Bundle bundle) {
        return ViewCompat.performAccessibilityAction(this.mView, i, bundle);
    }

    private boolean performActionForChild(int i, int i2, Bundle bundle) {
        int i3 = i;
        int i4 = i2;
        Bundle bundle2 = bundle;
        switch (i4) {
            case 64:
            case 128:
                return manageFocusForChild(i3, i4, bundle2);
            default:
                return onPerformActionForVirtualView(i3, i4, bundle2);
        }
    }

    private boolean manageFocusForChild(int i, int i2, Bundle bundle) {
        int i3 = i;
        Bundle bundle2 = bundle;
        switch (i2) {
            case 64:
                return requestAccessibilityFocus(i3);
            case 128:
                return clearAccessibilityFocus(i3);
            default:
                return false;
        }
    }

    private boolean intersectVisibleToUser(Rect rect) {
        Rect rect2 = rect;
        if (rect2 == null || rect2.isEmpty()) {
            return false;
        }
        if (this.mView.getWindowVisibility() != 0) {
            return false;
        }
        ViewParent parent = this.mView.getParent();
        while (true) {
            ViewParent viewParent = parent;
            if (viewParent instanceof View) {
                View view = (View) viewParent;
                if (ViewCompat.getAlpha(view) > 0.0f && view.getVisibility() == 0) {
                    parent = view.getParent();
                }
            } else if (viewParent == null) {
                return false;
            } else {
                if (this.mView.getLocalVisibleRect(this.mTempVisibleRect)) {
                    return rect2.intersect(this.mTempVisibleRect);
                }
                return false;
            }
        }
        return false;
    }

    private boolean isAccessibilityFocused(int i) {
        return this.mFocusedVirtualViewId == i;
    }

    private boolean requestAccessibilityFocus(int i) {
        int i2 = i;
        if (!this.mManager.isEnabled() || !AccessibilityManagerCompat.isTouchExplorationEnabled(this.mManager)) {
            return false;
        }
        if (isAccessibilityFocused(i2)) {
            return false;
        }
        this.mFocusedVirtualViewId = i2;
        this.mView.invalidate();
        boolean sendEventForVirtualView = sendEventForVirtualView(i2, 32768);
        return true;
    }

    private boolean clearAccessibilityFocus(int i) {
        int i2 = i;
        if (!isAccessibilityFocused(i2)) {
            return false;
        }
        this.mFocusedVirtualViewId = INVALID_ID;
        this.mView.invalidate();
        boolean sendEventForVirtualView = sendEventForVirtualView(i2, 65536);
        return true;
    }
}
