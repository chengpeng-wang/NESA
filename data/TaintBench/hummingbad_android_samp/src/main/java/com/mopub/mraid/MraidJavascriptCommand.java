package com.mopub.mraid;

import android.support.annotation.NonNull;

public enum MraidJavascriptCommand {
    CLOSE("close"),
    EXPAND("expand") {
        /* access modifiers changed from: final */
        public final boolean requiresClick(@NonNull PlacementType placementType) {
            return placementType == PlacementType.INLINE;
        }
    },
    USE_CUSTOM_CLOSE("usecustomclose"),
    OPEN("open") {
        /* access modifiers changed from: final */
        public final boolean requiresClick(@NonNull PlacementType placementType) {
            return true;
        }
    },
    RESIZE("resize") {
        /* access modifiers changed from: final */
        public final boolean requiresClick(@NonNull PlacementType placementType) {
            return true;
        }
    },
    SET_ORIENTATION_PROPERTIES("setOrientationProperties"),
    PLAY_VIDEO("playVideo") {
        /* access modifiers changed from: final */
        public final boolean requiresClick(@NonNull PlacementType placementType) {
            return placementType == PlacementType.INLINE;
        }
    },
    STORE_PICTURE("storePicture") {
        /* access modifiers changed from: final */
        public final boolean requiresClick(@NonNull PlacementType placementType) {
            return true;
        }
    },
    CREATE_CALENDAR_EVENT("createCalendarEvent") {
        /* access modifiers changed from: final */
        public final boolean requiresClick(@NonNull PlacementType placementType) {
            return true;
        }
    },
    UNSPECIFIED("");
    
    @NonNull
    private final String mJavascriptString;

    private MraidJavascriptCommand(String javascriptString) {
        this.mJavascriptString = javascriptString;
    }

    static MraidJavascriptCommand fromJavascriptString(@NonNull String string) {
        for (MraidJavascriptCommand mraidJavascriptCommand : values()) {
            if (mraidJavascriptCommand.mJavascriptString.equals(string)) {
                return mraidJavascriptCommand;
            }
        }
        return UNSPECIFIED;
    }

    /* access modifiers changed from: 0000 */
    public String toJavascriptString() {
        return this.mJavascriptString;
    }

    /* access modifiers changed from: 0000 */
    public boolean requiresClick(@NonNull PlacementType placementType) {
        return false;
    }
}
