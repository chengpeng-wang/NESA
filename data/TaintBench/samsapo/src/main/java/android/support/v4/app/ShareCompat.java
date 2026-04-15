package android.support.v4.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Parcelable;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;

public class ShareCompat {
    public static final String EXTRA_CALLING_ACTIVITY = "android.support.v4.app.EXTRA_CALLING_ACTIVITY";
    public static final String EXTRA_CALLING_PACKAGE = "android.support.v4.app.EXTRA_CALLING_PACKAGE";
    /* access modifiers changed from: private|static */
    public static ShareCompatImpl IMPL;

    public static class IntentBuilder {
        private Activity mActivity;
        private ArrayList<String> mBccAddresses;
        private ArrayList<String> mCcAddresses;
        private CharSequence mChooserTitle;
        private Intent mIntent;
        private ArrayList<Uri> mStreams;
        private ArrayList<String> mToAddresses;

        public static IntentBuilder from(Activity activity) {
            IntentBuilder intentBuilder = r4;
            IntentBuilder intentBuilder2 = new IntentBuilder(activity);
            return intentBuilder;
        }

        private IntentBuilder(Activity activity) {
            Activity activity2 = activity;
            this.mActivity = activity2;
            Intent intent = r5;
            Intent intent2 = new Intent();
            this.mIntent = intent.setAction("android.intent.action.SEND");
            Intent putExtra = this.mIntent.putExtra(ShareCompat.EXTRA_CALLING_PACKAGE, activity2.getPackageName());
            putExtra = this.mIntent.putExtra(ShareCompat.EXTRA_CALLING_ACTIVITY, activity2.getComponentName());
            putExtra = this.mIntent.addFlags(AccessibilityEventCompat.TYPE_GESTURE_DETECTION_END);
        }

        public Intent getIntent() {
            Intent action;
            if (this.mToAddresses != null) {
                combineArrayExtra("android.intent.extra.EMAIL", this.mToAddresses);
                this.mToAddresses = null;
            }
            if (this.mCcAddresses != null) {
                combineArrayExtra("android.intent.extra.CC", this.mCcAddresses);
                this.mCcAddresses = null;
            }
            if (this.mBccAddresses != null) {
                combineArrayExtra("android.intent.extra.BCC", this.mBccAddresses);
                this.mBccAddresses = null;
            }
            Object obj = (this.mStreams == null || this.mStreams.size() <= 1) ? null : 1;
            Object obj2 = obj;
            boolean equals = this.mIntent.getAction().equals("android.intent.action.SEND_MULTIPLE");
            if (obj2 == null && equals) {
                action = this.mIntent.setAction("android.intent.action.SEND");
                if (this.mStreams == null || this.mStreams.isEmpty()) {
                    this.mIntent.removeExtra("android.intent.extra.STREAM");
                } else {
                    action = this.mIntent.putExtra("android.intent.extra.STREAM", (Parcelable) this.mStreams.get(0));
                }
                this.mStreams = null;
            }
            if (!(obj2 == null || equals)) {
                action = this.mIntent.setAction("android.intent.action.SEND_MULTIPLE");
                if (this.mStreams == null || this.mStreams.isEmpty()) {
                    this.mIntent.removeExtra("android.intent.extra.STREAM");
                } else {
                    action = this.mIntent.putParcelableArrayListExtra("android.intent.extra.STREAM", this.mStreams);
                }
            }
            return this.mIntent;
        }

        /* access modifiers changed from: 0000 */
        public Activity getActivity() {
            return this.mActivity;
        }

        private void combineArrayExtra(String str, ArrayList<String> arrayList) {
            String str2 = str;
            ArrayList<String> arrayList2 = arrayList;
            Object stringArrayExtra = this.mIntent.getStringArrayExtra(str2);
            int length = stringArrayExtra != null ? stringArrayExtra.length : 0;
            Object obj = new String[(length + arrayList2.size())];
            Object[] toArray = arrayList2.toArray(obj);
            if (stringArrayExtra != null) {
                System.arraycopy(stringArrayExtra, 0, obj, arrayList2.size(), length);
            }
            Intent putExtra = this.mIntent.putExtra(str2, obj);
        }

        private void combineArrayExtra(String str, String[] strArr) {
            String str2 = str;
            Object obj = strArr;
            Intent intent = getIntent();
            Object stringArrayExtra = intent.getStringArrayExtra(str2);
            int length = stringArrayExtra != null ? stringArrayExtra.length : 0;
            Object obj2 = new String[(length + obj.length)];
            if (stringArrayExtra != null) {
                System.arraycopy(stringArrayExtra, 0, obj2, 0, length);
            }
            System.arraycopy(obj, 0, obj2, length, obj.length);
            Intent putExtra = intent.putExtra(str2, obj2);
        }

        public Intent createChooserIntent() {
            return Intent.createChooser(getIntent(), this.mChooserTitle);
        }

        public void startChooser() {
            this.mActivity.startActivity(createChooserIntent());
        }

        public IntentBuilder setChooserTitle(CharSequence charSequence) {
            this.mChooserTitle = charSequence;
            return this;
        }

        public IntentBuilder setChooserTitle(int i) {
            return setChooserTitle(this.mActivity.getText(i));
        }

        public IntentBuilder setType(String str) {
            Intent type = this.mIntent.setType(str);
            return this;
        }

        public IntentBuilder setText(CharSequence charSequence) {
            Intent putExtra = this.mIntent.putExtra("android.intent.extra.TEXT", charSequence);
            return this;
        }

        public IntentBuilder setHtmlText(String str) {
            String str2 = str;
            Intent putExtra = this.mIntent.putExtra(IntentCompat.EXTRA_HTML_TEXT, str2);
            if (!this.mIntent.hasExtra("android.intent.extra.TEXT")) {
                IntentBuilder text = setText(Html.fromHtml(str2));
            }
            return this;
        }

        public IntentBuilder setStream(Uri uri) {
            Intent action;
            Uri uri2 = uri;
            if (!this.mIntent.getAction().equals("android.intent.action.SEND")) {
                action = this.mIntent.setAction("android.intent.action.SEND");
            }
            this.mStreams = null;
            action = this.mIntent.putExtra("android.intent.extra.STREAM", uri2);
            return this;
        }

        public IntentBuilder addStream(Uri uri) {
            Uri uri2 = uri;
            Uri uri3 = (Uri) this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
            if (uri3 == null) {
                return setStream(uri2);
            }
            boolean add;
            if (this.mStreams == null) {
                ArrayList arrayList = r6;
                ArrayList arrayList2 = new ArrayList();
                this.mStreams = arrayList;
            }
            if (uri3 != null) {
                this.mIntent.removeExtra("android.intent.extra.STREAM");
                add = this.mStreams.add(uri3);
            }
            add = this.mStreams.add(uri2);
            return this;
        }

        public IntentBuilder setEmailTo(String[] strArr) {
            String[] strArr2 = strArr;
            if (this.mToAddresses != null) {
                this.mToAddresses = null;
            }
            Intent putExtra = this.mIntent.putExtra("android.intent.extra.EMAIL", strArr2);
            return this;
        }

        public IntentBuilder addEmailTo(String str) {
            String str2 = str;
            if (this.mToAddresses == null) {
                ArrayList arrayList = r5;
                ArrayList arrayList2 = new ArrayList();
                this.mToAddresses = arrayList;
            }
            boolean add = this.mToAddresses.add(str2);
            return this;
        }

        public IntentBuilder addEmailTo(String[] strArr) {
            combineArrayExtra("android.intent.extra.EMAIL", strArr);
            return this;
        }

        public IntentBuilder setEmailCc(String[] strArr) {
            Intent putExtra = this.mIntent.putExtra("android.intent.extra.CC", strArr);
            return this;
        }

        public IntentBuilder addEmailCc(String str) {
            String str2 = str;
            if (this.mCcAddresses == null) {
                ArrayList arrayList = r5;
                ArrayList arrayList2 = new ArrayList();
                this.mCcAddresses = arrayList;
            }
            boolean add = this.mCcAddresses.add(str2);
            return this;
        }

        public IntentBuilder addEmailCc(String[] strArr) {
            combineArrayExtra("android.intent.extra.CC", strArr);
            return this;
        }

        public IntentBuilder setEmailBcc(String[] strArr) {
            Intent putExtra = this.mIntent.putExtra("android.intent.extra.BCC", strArr);
            return this;
        }

        public IntentBuilder addEmailBcc(String str) {
            String str2 = str;
            if (this.mBccAddresses == null) {
                ArrayList arrayList = r5;
                ArrayList arrayList2 = new ArrayList();
                this.mBccAddresses = arrayList;
            }
            boolean add = this.mBccAddresses.add(str2);
            return this;
        }

        public IntentBuilder addEmailBcc(String[] strArr) {
            combineArrayExtra("android.intent.extra.BCC", strArr);
            return this;
        }

        public IntentBuilder setSubject(String str) {
            Intent putExtra = this.mIntent.putExtra("android.intent.extra.SUBJECT", str);
            return this;
        }
    }

    public static class IntentReader {
        private static final String TAG = "IntentReader";
        private Activity mActivity;
        private ComponentName mCallingActivity;
        private String mCallingPackage;
        private Intent mIntent;
        private ArrayList<Uri> mStreams;

        public static IntentReader from(Activity activity) {
            IntentReader intentReader = r4;
            IntentReader intentReader2 = new IntentReader(activity);
            return intentReader;
        }

        private IntentReader(Activity activity) {
            Activity activity2 = activity;
            this.mActivity = activity2;
            this.mIntent = activity2.getIntent();
            this.mCallingPackage = ShareCompat.getCallingPackage(activity2);
            this.mCallingActivity = ShareCompat.getCallingActivity(activity2);
        }

        public boolean isShareIntent() {
            String action = this.mIntent.getAction();
            boolean z = "android.intent.action.SEND".equals(action) || "android.intent.action.SEND_MULTIPLE".equals(action);
            return z;
        }

        public boolean isSingleShare() {
            return "android.intent.action.SEND".equals(this.mIntent.getAction());
        }

        public boolean isMultipleShare() {
            return "android.intent.action.SEND_MULTIPLE".equals(this.mIntent.getAction());
        }

        public String getType() {
            return this.mIntent.getType();
        }

        public CharSequence getText() {
            return this.mIntent.getCharSequenceExtra("android.intent.extra.TEXT");
        }

        public String getHtmlText() {
            String stringExtra = this.mIntent.getStringExtra(IntentCompat.EXTRA_HTML_TEXT);
            if (stringExtra == null) {
                CharSequence text = getText();
                if (text instanceof Spanned) {
                    stringExtra = Html.toHtml((Spanned) text);
                } else if (text != null) {
                    stringExtra = ShareCompat.IMPL.escapeHtml(text);
                }
            }
            return stringExtra;
        }

        public Uri getStream() {
            return (Uri) this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
        }

        public Uri getStream(int i) {
            int i2 = i;
            if (this.mStreams == null && isMultipleShare()) {
                this.mStreams = this.mIntent.getParcelableArrayListExtra("android.intent.extra.STREAM");
            }
            if (this.mStreams != null) {
                return (Uri) this.mStreams.get(i2);
            }
            if (i2 == 0) {
                return (Uri) this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
            }
            IndexOutOfBoundsException indexOutOfBoundsException = r6;
            StringBuilder stringBuilder = r6;
            StringBuilder stringBuilder2 = new StringBuilder();
            IndexOutOfBoundsException indexOutOfBoundsException2 = new IndexOutOfBoundsException(stringBuilder.append("Stream items available: ").append(getStreamCount()).append(" index requested: ").append(i2).toString());
            throw indexOutOfBoundsException;
        }

        public int getStreamCount() {
            if (this.mStreams == null && isMultipleShare()) {
                this.mStreams = this.mIntent.getParcelableArrayListExtra("android.intent.extra.STREAM");
            }
            if (this.mStreams != null) {
                return this.mStreams.size();
            }
            return this.mIntent.hasExtra("android.intent.extra.STREAM") ? 1 : 0;
        }

        public String[] getEmailTo() {
            return this.mIntent.getStringArrayExtra("android.intent.extra.EMAIL");
        }

        public String[] getEmailCc() {
            return this.mIntent.getStringArrayExtra("android.intent.extra.CC");
        }

        public String[] getEmailBcc() {
            return this.mIntent.getStringArrayExtra("android.intent.extra.BCC");
        }

        public String getSubject() {
            return this.mIntent.getStringExtra("android.intent.extra.SUBJECT");
        }

        public String getCallingPackage() {
            return this.mCallingPackage;
        }

        public ComponentName getCallingActivity() {
            return this.mCallingActivity;
        }

        public Drawable getCallingActivityIcon() {
            if (this.mCallingActivity == null) {
                return null;
            }
            try {
                return this.mActivity.getPackageManager().getActivityIcon(this.mCallingActivity);
            } catch (NameNotFoundException e) {
                int e2 = Log.e(TAG, "Could not retrieve icon for calling activity", e);
                return null;
            }
        }

        public Drawable getCallingApplicationIcon() {
            if (this.mCallingPackage == null) {
                return null;
            }
            try {
                return this.mActivity.getPackageManager().getApplicationIcon(this.mCallingPackage);
            } catch (NameNotFoundException e) {
                int e2 = Log.e(TAG, "Could not retrieve icon for calling application", e);
                return null;
            }
        }

        public CharSequence getCallingApplicationLabel() {
            if (this.mCallingPackage == null) {
                return null;
            }
            PackageManager packageManager = this.mActivity.getPackageManager();
            try {
                return packageManager.getApplicationLabel(packageManager.getApplicationInfo(this.mCallingPackage, 0));
            } catch (NameNotFoundException e) {
                int e2 = Log.e(TAG, "Could not retrieve label for calling application", e);
                return null;
            }
        }
    }

    interface ShareCompatImpl {
        void configureMenuItem(MenuItem menuItem, IntentBuilder intentBuilder);

        String escapeHtml(CharSequence charSequence);
    }

    static class ShareCompatImplBase implements ShareCompatImpl {
        ShareCompatImplBase() {
        }

        public void configureMenuItem(MenuItem menuItem, IntentBuilder intentBuilder) {
            MenuItem intent = menuItem.setIntent(intentBuilder.createChooserIntent());
        }

        public String escapeHtml(CharSequence charSequence) {
            CharSequence charSequence2 = charSequence;
            StringBuilder stringBuilder = r7;
            StringBuilder stringBuilder2 = new StringBuilder();
            StringBuilder stringBuilder3 = stringBuilder;
            withinStyle(stringBuilder3, charSequence2, 0, charSequence2.length());
            return stringBuilder3.toString();
        }

        private static void withinStyle(StringBuilder stringBuilder, CharSequence charSequence, int i, int i2) {
            StringBuilder stringBuilder2 = stringBuilder;
            CharSequence charSequence2 = charSequence;
            int i3 = i2;
            int i4 = i;
            while (i4 < i3) {
                char charAt = charSequence2.charAt(i4);
                StringBuilder append;
                if (charAt == '<') {
                    append = stringBuilder2.append("&lt;");
                } else if (charAt == '>') {
                    append = stringBuilder2.append("&gt;");
                } else if (charAt == '&') {
                    append = stringBuilder2.append("&amp;");
                } else if (charAt > '~' || charAt < ' ') {
                    append = stringBuilder2;
                    StringBuilder stringBuilder3 = r9;
                    StringBuilder stringBuilder4 = new StringBuilder();
                    append = append.append(stringBuilder3.append("&#").append(charAt).append(";").toString());
                } else if (charAt == ' ') {
                    while (i4 + 1 < i3 && charSequence2.charAt(i4 + 1) == ' ') {
                        append = stringBuilder2.append("&nbsp;");
                        i4++;
                    }
                    append = stringBuilder2.append(' ');
                } else {
                    append = stringBuilder2.append(charAt);
                }
                i4++;
            }
        }
    }

    static class ShareCompatImplICS extends ShareCompatImplBase {
        ShareCompatImplICS() {
        }

        public void configureMenuItem(MenuItem menuItem, IntentBuilder intentBuilder) {
            MenuItem menuItem2 = menuItem;
            IntentBuilder intentBuilder2 = intentBuilder;
            ShareCompatICS.configureMenuItem(menuItem2, intentBuilder2.getActivity(), intentBuilder2.getIntent());
            if (shouldAddChooserIntent(menuItem2)) {
                MenuItem intent = menuItem2.setIntent(intentBuilder2.createChooserIntent());
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean shouldAddChooserIntent(MenuItem menuItem) {
            return !menuItem.hasSubMenu();
        }
    }

    static class ShareCompatImplJB extends ShareCompatImplICS {
        ShareCompatImplJB() {
        }

        public String escapeHtml(CharSequence charSequence) {
            return ShareCompatJB.escapeHtml(charSequence);
        }

        /* access modifiers changed from: 0000 */
        public boolean shouldAddChooserIntent(MenuItem menuItem) {
            MenuItem menuItem2 = menuItem;
            return false;
        }
    }

    public ShareCompat() {
    }

    static {
        if (VERSION.SDK_INT >= 16) {
            ShareCompatImplJB shareCompatImplJB = r2;
            ShareCompatImplJB shareCompatImplJB2 = new ShareCompatImplJB();
            IMPL = shareCompatImplJB;
        } else if (VERSION.SDK_INT >= 14) {
            ShareCompatImplICS shareCompatImplICS = r2;
            ShareCompatImplICS shareCompatImplICS2 = new ShareCompatImplICS();
            IMPL = shareCompatImplICS;
        } else {
            ShareCompatImplBase shareCompatImplBase = r2;
            ShareCompatImplBase shareCompatImplBase2 = new ShareCompatImplBase();
            IMPL = shareCompatImplBase;
        }
    }

    public static String getCallingPackage(Activity activity) {
        Activity activity2 = activity;
        String callingPackage = activity2.getCallingPackage();
        if (callingPackage == null) {
            callingPackage = activity2.getIntent().getStringExtra(EXTRA_CALLING_PACKAGE);
        }
        return callingPackage;
    }

    public static ComponentName getCallingActivity(Activity activity) {
        Activity activity2 = activity;
        ComponentName callingActivity = activity2.getCallingActivity();
        if (callingActivity == null) {
            callingActivity = (ComponentName) activity2.getIntent().getParcelableExtra(EXTRA_CALLING_ACTIVITY);
        }
        return callingActivity;
    }

    public static void configureMenuItem(MenuItem menuItem, IntentBuilder intentBuilder) {
        IMPL.configureMenuItem(menuItem, intentBuilder);
    }

    public static void configureMenuItem(Menu menu, int i, IntentBuilder intentBuilder) {
        int i2 = i;
        IntentBuilder intentBuilder2 = intentBuilder;
        MenuItem findItem = menu.findItem(i2);
        if (findItem == null) {
            IllegalArgumentException illegalArgumentException = r8;
            StringBuilder stringBuilder = r8;
            StringBuilder stringBuilder2 = new StringBuilder();
            IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException(stringBuilder.append("Could not find menu item with id ").append(i2).append(" in the supplied menu").toString());
            throw illegalArgumentException;
        }
        configureMenuItem(findItem, intentBuilder2);
    }
}
