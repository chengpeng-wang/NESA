package com.google.tagmanager;

import android.content.Context;
import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.containertag.common.Key;
import com.google.analytics.midtier.proto.containertag.TypeSystem.Value;
import java.util.Map;

class AdwordsClickReferrerMacro extends FunctionCallImplementation {
    private static final String COMPONENT = Key.COMPONENT.toString();
    private static final String CONVERSION_ID = Key.CONVERSION_ID.toString();
    private static final String ID = FunctionType.ADWORDS_CLICK_REFERRER.toString();
    private final Context context;

    public static String getFunctionId() {
        return ID;
    }

    public AdwordsClickReferrerMacro(Context context) {
        super(ID, CONVERSION_ID);
        this.context = context;
    }

    public boolean isCacheable() {
        return true;
    }

    public Value evaluate(Map<String, Value> parameters) {
        Value conversionIdValue = (Value) parameters.get(CONVERSION_ID);
        if (conversionIdValue == null) {
            return Types.getDefaultValue();
        }
        Value componentValue = (Value) parameters.get(COMPONENT);
        String referrer = InstallReferrerUtil.getClickReferrer(this.context, Types.valueToString(conversionIdValue), componentValue != null ? Types.valueToString(componentValue) : null);
        return referrer != null ? Types.objectToValue(referrer) : Types.getDefaultValue();
    }
}
