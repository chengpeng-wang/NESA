package com.google.tagmanager;

import android.content.Context;
import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.containertag.common.Key;
import com.google.analytics.midtier.proto.containertag.TypeSystem.Value;
import java.util.Map;

class InstallReferrerMacro extends FunctionCallImplementation {
    private static final String COMPONENT = Key.COMPONENT.toString();
    private static final String ID = FunctionType.INSTALL_REFERRER.toString();
    private final Context context;

    public static String getFunctionId() {
        return ID;
    }

    public InstallReferrerMacro(Context context) {
        super(ID, new String[0]);
        this.context = context;
    }

    public boolean isCacheable() {
        return true;
    }

    public Value evaluate(Map<String, Value> parameters) {
        String referrer = InstallReferrerUtil.getInstallReferrer(this.context, ((Value) parameters.get(COMPONENT)) != null ? Types.valueToString((Value) parameters.get(COMPONENT)) : null);
        return referrer != null ? Types.objectToValue(referrer) : Types.getDefaultValue();
    }
}
