package com.google.tagmanager;

import android.content.Context;
import com.google.analytics.containertag.common.Key;
import com.google.analytics.midtier.proto.containertag.TypeSystem.Value;
import com.google.analytics.midtier.proto.containertag.TypeSystem.Value.Builder;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.tagmanager.CacheFactory.CacheSizeManager;
import com.google.tagmanager.CustomFunctionCall.CustomEvaluator;
import com.google.tagmanager.ResourceUtil.ExpandedFunctionCall;
import com.google.tagmanager.ResourceUtil.ExpandedResource;
import com.google.tagmanager.ResourceUtil.ExpandedRule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

class Runtime {
    static final String DEFAULT_RULE_NAME = "Unknown";
    private static final ObjectAndStatic<Value> DEFAULT_VALUE_AND_STATIC = new ObjectAndStatic(Types.getDefaultValue(), true);
    private static final int MAX_CACHE_SIZE = 1048576;
    private final EventInfoDistributor eventInfoDistributor;
    private volatile String mCurrentEventName;
    private final Cache<ExpandedFunctionCall, ObjectAndStatic<Value>> mFunctionCallCache;
    private final Cache<String, ObjectAndStatic<Value>> mMacroEvaluationCache;
    private final Map<String, MacroInfo> mMacroLookup;
    private final Map<String, FunctionCallImplementation> mMacroMap;
    private final Map<String, FunctionCallImplementation> mPredicateMap;
    private final ExpandedResource mResource;
    private final Set<ExpandedRule> mRules;
    private final Map<String, FunctionCallImplementation> mTrackingTagMap;

    interface AddRemoveSetPopulator {
        void rulePassed(ExpandedRule expandedRule, Set<ExpandedFunctionCall> set, Set<ExpandedFunctionCall> set2, ResolvedRuleBuilder resolvedRuleBuilder);
    }

    private static class MacroInfo {
        private final Map<ExpandedRule, List<String>> mAddMacroNames = new HashMap();
        private final Map<ExpandedRule, List<ExpandedFunctionCall>> mAddMacros = new HashMap();
        private ExpandedFunctionCall mDefault;
        private final Map<ExpandedRule, List<String>> mRemoveMacroNames = new HashMap();
        private final Map<ExpandedRule, List<ExpandedFunctionCall>> mRemoveMacros = new HashMap();
        private final Set<ExpandedRule> mRules = new HashSet();

        public Set<ExpandedRule> getRules() {
            return this.mRules;
        }

        public void addRule(ExpandedRule rule) {
            this.mRules.add(rule);
        }

        public Map<ExpandedRule, List<ExpandedFunctionCall>> getAddMacros() {
            return this.mAddMacros;
        }

        public Map<ExpandedRule, List<String>> getAddMacroRuleNames() {
            return this.mAddMacroNames;
        }

        public Map<ExpandedRule, List<String>> getRemoveMacroRuleNames() {
            return this.mRemoveMacroNames;
        }

        public void addAddMacroForRule(ExpandedRule rule, ExpandedFunctionCall function) {
            List<ExpandedFunctionCall> result = (List) this.mAddMacros.get(rule);
            if (result == null) {
                result = new ArrayList();
                this.mAddMacros.put(rule, result);
            }
            result.add(function);
        }

        public void addAddMacroRuleNameForRule(ExpandedRule rule, String ruleName) {
            List<String> result = (List) this.mAddMacroNames.get(rule);
            if (result == null) {
                result = new ArrayList();
                this.mAddMacroNames.put(rule, result);
            }
            result.add(ruleName);
        }

        public Map<ExpandedRule, List<ExpandedFunctionCall>> getRemoveMacros() {
            return this.mRemoveMacros;
        }

        public void addRemoveMacroForRule(ExpandedRule rule, ExpandedFunctionCall function) {
            List<ExpandedFunctionCall> result = (List) this.mRemoveMacros.get(rule);
            if (result == null) {
                result = new ArrayList();
                this.mRemoveMacros.put(rule, result);
            }
            result.add(function);
        }

        public void addRemoveMacroRuleNameForRule(ExpandedRule rule, String ruleName) {
            List<String> result = (List) this.mRemoveMacroNames.get(rule);
            if (result == null) {
                result = new ArrayList();
                this.mRemoveMacroNames.put(rule, result);
            }
            result.add(ruleName);
        }

        public ExpandedFunctionCall getDefault() {
            return this.mDefault;
        }

        public void setDefault(ExpandedFunctionCall def) {
            this.mDefault = def;
        }
    }

    public Runtime(Context context, ExpandedResource resource, DataLayer dataLayer, CustomEvaluator functionCallMacroEvaluator, CustomEvaluator functionCallTagEvaluator, EventInfoDistributor eventInfoDistributor) {
        if (resource == null) {
            throw new NullPointerException("resource cannot be null");
        }
        ExpandedFunctionCall function;
        this.mResource = resource;
        this.mRules = new HashSet(resource.getRules());
        this.eventInfoDistributor = eventInfoDistributor;
        this.mFunctionCallCache = new CacheFactory().createCache(1048576, new CacheSizeManager<ExpandedFunctionCall, ObjectAndStatic<Value>>() {
            public int sizeOf(ExpandedFunctionCall key, ObjectAndStatic<Value> value) {
                return ((Value) value.getObject()).toByteArray().length;
            }
        });
        this.mMacroEvaluationCache = new CacheFactory().createCache(1048576, new CacheSizeManager<String, ObjectAndStatic<Value>>() {
            public int sizeOf(String key, ObjectAndStatic<Value> value) {
                return ((Value) value.getObject()).toByteArray().length + key.length();
            }
        });
        this.mTrackingTagMap = new HashMap();
        addTrackingTag(new ArbitraryPixelTag(context));
        addTrackingTag(new CustomFunctionCall(functionCallTagEvaluator));
        addTrackingTag(new UniversalAnalyticsTag(context, dataLayer));
        this.mPredicateMap = new HashMap();
        addPredicate(new ContainsPredicate());
        addPredicate(new EndsWithPredicate());
        addPredicate(new EqualsPredicate());
        addPredicate(new GreaterEqualsPredicate());
        addPredicate(new GreaterThanPredicate());
        addPredicate(new LessEqualsPredicate());
        addPredicate(new LessThanPredicate());
        addPredicate(new RegexPredicate());
        addPredicate(new StartsWithPredicate());
        this.mMacroMap = new HashMap();
        addMacro(new AdvertiserIdMacro(context));
        addMacro(new AdvertisingTrackingEnabledMacro());
        addMacro(new AdwordsClickReferrerMacro(context));
        addMacro(new AppIdMacro(context));
        addMacro(new AppNameMacro(context));
        addMacro(new AppVersionMacro(context));
        addMacro(new ConstantMacro());
        addMacro(new CustomFunctionCall(functionCallMacroEvaluator));
        addMacro(new DataLayerMacro(dataLayer));
        addMacro(new DeviceIdMacro(context));
        addMacro(new DeviceNameMacro());
        addMacro(new EncodeMacro());
        addMacro(new EventMacro(this));
        addMacro(new GtmVersionMacro());
        addMacro(new HashMacro());
        addMacro(new InstallReferrerMacro(context));
        addMacro(new JoinerMacro());
        addMacro(new LanguageMacro());
        addMacro(new MobileAdwordsUniqueIdMacro(context));
        addMacro(new OsVersionMacro());
        addMacro(new PlatformMacro());
        addMacro(new RandomMacro());
        addMacro(new RegexGroupMacro());
        addMacro(new ResolutionMacro(context));
        addMacro(new RuntimeVersionMacro());
        addMacro(new SdkVersionMacro());
        addMacro(new TimeMacro());
        this.mMacroLookup = new HashMap();
        for (ExpandedRule rule : this.mRules) {
            String ruleName;
            MacroInfo info;
            if (eventInfoDistributor.debugMode()) {
                verifyFunctionAndNameListSizes(rule.getAddMacros(), rule.getAddMacroRuleNames(), "add macro");
                verifyFunctionAndNameListSizes(rule.getRemoveMacros(), rule.getRemoveMacroRuleNames(), "remove macro");
                verifyFunctionAndNameListSizes(rule.getAddTags(), rule.getAddTagRuleNames(), "add tag");
                verifyFunctionAndNameListSizes(rule.getRemoveTags(), rule.getRemoveTagRuleNames(), "remove tag");
            }
            int i = 0;
            while (i < rule.getAddMacros().size()) {
                function = (ExpandedFunctionCall) rule.getAddMacros().get(i);
                ruleName = DEFAULT_RULE_NAME;
                if (eventInfoDistributor.debugMode() && i < rule.getAddMacroRuleNames().size()) {
                    ruleName = (String) rule.getAddMacroRuleNames().get(i);
                }
                info = getOrAddMacroInfo(this.mMacroLookup, getFunctionName(function));
                info.addRule(rule);
                info.addAddMacroForRule(rule, function);
                info.addAddMacroRuleNameForRule(rule, ruleName);
                i++;
            }
            i = 0;
            while (i < rule.getRemoveMacros().size()) {
                function = (ExpandedFunctionCall) rule.getRemoveMacros().get(i);
                ruleName = DEFAULT_RULE_NAME;
                if (eventInfoDistributor.debugMode() && i < rule.getRemoveMacroRuleNames().size()) {
                    ruleName = (String) rule.getRemoveMacroRuleNames().get(i);
                }
                info = getOrAddMacroInfo(this.mMacroLookup, getFunctionName(function));
                info.addRule(rule);
                info.addRemoveMacroForRule(rule, function);
                info.addRemoveMacroRuleNameForRule(rule, ruleName);
                i++;
            }
        }
        for (Entry<String, List<ExpandedFunctionCall>> ent : this.mResource.getAllMacros().entrySet()) {
            for (ExpandedFunctionCall function2 : (List) ent.getValue()) {
                if (!Types.valueToBoolean((Value) function2.getProperties().get(Key.NOT_DEFAULT_MACRO.toString())).booleanValue()) {
                    getOrAddMacroInfo(this.mMacroLookup, (String) ent.getKey()).setDefault(function2);
                }
            }
        }
    }

    public Runtime(Context context, ExpandedResource resource, DataLayer dataLayer, CustomEvaluator functionCallMacroEvaluator, CustomEvaluator functionCallTagEvaluator) {
        this(context, resource, dataLayer, functionCallMacroEvaluator, functionCallTagEvaluator, new NoopEventInfoDistributor());
    }

    public ExpandedResource getResource() {
        return this.mResource;
    }

    public synchronized void evaluateTags(String currentEventName) {
        setCurrentEventName(currentEventName);
        EventInfoBuilder eventInfoBuilder = this.eventInfoDistributor.createDataLayerEventEvaluationEventInfo(currentEventName);
        DataLayerEventEvaluationInfoBuilder debugDataLayerBuilder = eventInfoBuilder.createDataLayerEventEvaluationInfoBuilder();
        for (ExpandedFunctionCall tag : (Set) calculateTagsToRun(this.mRules, debugDataLayerBuilder.createRulesEvaluation()).getObject()) {
            executeFunction(this.mTrackingTagMap, tag, new HashSet(), debugDataLayerBuilder.createAndAddResult());
        }
        eventInfoBuilder.processEventInfo();
        setCurrentEventName(null);
    }

    public ObjectAndStatic<Value> evaluateMacroReference(String macroName) {
        EventInfoBuilder eventInfoBuilder = this.eventInfoDistributor.createMacroEvalutionEventInfo(macroName);
        ObjectAndStatic<Value> result = evaluateMacroReferenceCycleDetection(macroName, new HashSet(), eventInfoBuilder.createMacroEvaluationInfoBuilder());
        eventInfoBuilder.processEventInfo();
        return result;
    }

    /* access modifiers changed from: declared_synchronized */
    @VisibleForTesting
    public synchronized void setCurrentEventName(String currentEventName) {
        this.mCurrentEventName = currentEventName;
    }

    /* access modifiers changed from: declared_synchronized */
    public synchronized String getCurrentEventName() {
        return this.mCurrentEventName;
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public ObjectAndStatic<Set<ExpandedFunctionCall>> calculateMacrosToRun(String macroName, Set<ExpandedRule> rules, Map<ExpandedRule, List<ExpandedFunctionCall>> addMacros, Map<ExpandedRule, List<String>> addMacroRuleNames, Map<ExpandedRule, List<ExpandedFunctionCall>> removeMacros, Map<ExpandedRule, List<String>> removeMacroRuleNames, Set<String> pendingMacroExpansions, RuleEvaluationStepInfoBuilder debugRulesEvaluation) {
        final Map<ExpandedRule, List<ExpandedFunctionCall>> map = addMacros;
        final Map<ExpandedRule, List<String>> map2 = addMacroRuleNames;
        final Map<ExpandedRule, List<ExpandedFunctionCall>> map3 = removeMacros;
        final Map<ExpandedRule, List<String>> map4 = removeMacroRuleNames;
        return calculateGenericToRun(rules, pendingMacroExpansions, new AddRemoveSetPopulator() {
            public void rulePassed(ExpandedRule rule, Set<ExpandedFunctionCall> add, Set<ExpandedFunctionCall> remove, ResolvedRuleBuilder debugRuleBuilder) {
                List<ExpandedFunctionCall> thisAddMacro = (List) map.get(rule);
                List<String> thisMacroEnablingRuleNames = (List) map2.get(rule);
                if (thisAddMacro != null) {
                    add.addAll(thisAddMacro);
                    debugRuleBuilder.getAddedMacroFunctions().translateAndAddAll(thisAddMacro, thisMacroEnablingRuleNames);
                }
                List<ExpandedFunctionCall> thisRemoveMacro = (List) map3.get(rule);
                List<String> thisRemoveMacroRuleNames = (List) map4.get(rule);
                if (thisRemoveMacro != null) {
                    remove.addAll(thisRemoveMacro);
                    debugRuleBuilder.getRemovedMacroFunctions().translateAndAddAll(thisRemoveMacro, thisRemoveMacroRuleNames);
                }
            }
        }, debugRulesEvaluation);
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public ObjectAndStatic<Set<ExpandedFunctionCall>> calculateTagsToRun(Set<ExpandedRule> rules, RuleEvaluationStepInfoBuilder debugRulesEvaluation) {
        return calculateGenericToRun(rules, new HashSet(), new AddRemoveSetPopulator() {
            public void rulePassed(ExpandedRule rule, Set<ExpandedFunctionCall> add, Set<ExpandedFunctionCall> remove, ResolvedRuleBuilder debugRuleBuilder) {
                add.addAll(rule.getAddTags());
                remove.addAll(rule.getRemoveTags());
                debugRuleBuilder.getAddedTagFunctions().translateAndAddAll(rule.getAddTags(), rule.getAddTagRuleNames());
                debugRuleBuilder.getRemovedTagFunctions().translateAndAddAll(rule.getRemoveTags(), rule.getRemoveTagRuleNames());
            }
        }, debugRulesEvaluation);
    }

    private static MacroInfo getOrAddMacroInfo(Map<String, MacroInfo> macroLookup, String key) {
        MacroInfo result = (MacroInfo) macroLookup.get(key);
        if (result != null) {
            return result;
        }
        result = new MacroInfo();
        macroLookup.put(key, result);
        return result;
    }

    private ObjectAndStatic<Set<ExpandedFunctionCall>> calculateGenericToRun(Set<ExpandedRule> rules, Set<String> pendingMacroExpansions, AddRemoveSetPopulator setPopulator, RuleEvaluationStepInfoBuilder debugRulesEvaluation) {
        Set<ExpandedFunctionCall> add = new HashSet();
        Set<ExpandedFunctionCall> remove = new HashSet();
        boolean allStatic = true;
        for (ExpandedRule rule : rules) {
            ResolvedRuleBuilder debugRuleBuilder = debugRulesEvaluation.createResolvedRuleBuilder();
            ObjectAndStatic<Boolean> result = evaluatePredicatesInRule(rule, pendingMacroExpansions, debugRuleBuilder);
            if (((Boolean) result.getObject()).booleanValue()) {
                setPopulator.rulePassed(rule, add, remove, debugRuleBuilder);
            }
            allStatic = allStatic && result.isStatic();
        }
        add.removeAll(remove);
        debugRulesEvaluation.setEnabledFunctions(add);
        return new ObjectAndStatic(add, allStatic);
    }

    private static String getFunctionName(ExpandedFunctionCall functionCall) {
        return Types.valueToString((Value) functionCall.getProperties().get(Key.INSTANCE_NAME.toString()));
    }

    private static void addFunctionImplToMap(Map<String, FunctionCallImplementation> addTo, FunctionCallImplementation impl) {
        if (addTo.containsKey(impl.getInstanceFunctionId())) {
            throw new IllegalArgumentException("Duplicate function type name: " + impl.getInstanceFunctionId());
        }
        addTo.put(impl.getInstanceFunctionId(), impl);
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void addMacro(FunctionCallImplementation macro) {
        addFunctionImplToMap(this.mMacroMap, macro);
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void addTrackingTag(FunctionCallImplementation tag) {
        addFunctionImplToMap(this.mTrackingTagMap, tag);
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void addPredicate(FunctionCallImplementation predicate) {
        addFunctionImplToMap(this.mPredicateMap, predicate);
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public ObjectAndStatic<Boolean> evaluatePredicate(ExpandedFunctionCall predicate, Set<String> pendingMacroExpansions, ResolvedFunctionCallBuilder debugFunctionCall) {
        ObjectAndStatic<Value> result = executeFunction(this.mPredicateMap, predicate, pendingMacroExpansions, debugFunctionCall);
        Boolean predicateResult = Types.valueToBoolean((Value) result.getObject());
        debugFunctionCall.setFunctionResult(Types.objectToValue(predicateResult));
        return new ObjectAndStatic(predicateResult, result.isStatic());
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public ObjectAndStatic<Boolean> evaluatePredicatesInRule(ExpandedRule rule, Set<String> pendingMacroExpansions, ResolvedRuleBuilder debugRuleBuilder) {
        ObjectAndStatic<Boolean> result;
        boolean allStatic = true;
        for (ExpandedFunctionCall f : rule.getNegativePredicates()) {
            result = evaluatePredicate(f, pendingMacroExpansions, debugRuleBuilder.createNegativePredicate());
            if (((Boolean) result.getObject()).booleanValue()) {
                debugRuleBuilder.setValue(Types.objectToValue(Boolean.valueOf(false)));
                return new ObjectAndStatic(Boolean.valueOf(false), result.isStatic());
            } else if (allStatic && result.isStatic()) {
                allStatic = true;
            } else {
                allStatic = false;
            }
        }
        for (ExpandedFunctionCall f2 : rule.getPositivePredicates()) {
            result = evaluatePredicate(f2, pendingMacroExpansions, debugRuleBuilder.createPositivePredicate());
            if (!((Boolean) result.getObject()).booleanValue()) {
                debugRuleBuilder.setValue(Types.objectToValue(Boolean.valueOf(false)));
                return new ObjectAndStatic(Boolean.valueOf(false), result.isStatic());
            } else if (allStatic && result.isStatic()) {
                allStatic = true;
            } else {
                allStatic = false;
            }
        }
        debugRuleBuilder.setValue(Types.objectToValue(Boolean.valueOf(true)));
        return new ObjectAndStatic(Boolean.valueOf(true), allStatic);
    }

    private ObjectAndStatic<Value> evaluateMacroReferenceCycleDetection(String macroName, Set<String> pendingMacroExpansions, MacroEvaluationInfoBuilder debugMacroEvaluation) {
        ObjectAndStatic<Value> cachedResult = (ObjectAndStatic) this.mMacroEvaluationCache.get(macroName);
        if (cachedResult != null && !this.eventInfoDistributor.debugMode()) {
            return cachedResult;
        }
        MacroInfo macroInfo = (MacroInfo) this.mMacroLookup.get(macroName);
        if (macroInfo == null) {
            Log.e("Invalid macro: " + macroName);
            return DEFAULT_VALUE_AND_STATIC;
        }
        ExpandedFunctionCall macro;
        ObjectAndStatic<Set<ExpandedFunctionCall>> macrosToRun = calculateMacrosToRun(macroName, macroInfo.getRules(), macroInfo.getAddMacros(), macroInfo.getAddMacroRuleNames(), macroInfo.getRemoveMacros(), macroInfo.getRemoveMacroRuleNames(), pendingMacroExpansions, debugMacroEvaluation.createRulesEvaluation());
        if (((Set) macrosToRun.getObject()).isEmpty()) {
            macro = macroInfo.getDefault();
        } else {
            if (((Set) macrosToRun.getObject()).size() > 1) {
                Log.w("Multiple macros active for macroName " + macroName);
            }
            macro = (ExpandedFunctionCall) ((Set) macrosToRun.getObject()).iterator().next();
        }
        if (macro == null) {
            return DEFAULT_VALUE_AND_STATIC;
        }
        ObjectAndStatic<Value> toReturn;
        ObjectAndStatic<Value> macroResult = executeFunction(this.mMacroMap, macro, pendingMacroExpansions, debugMacroEvaluation.createResult());
        boolean isStatic = macrosToRun.isStatic() && macroResult.isStatic();
        if (macroResult == DEFAULT_VALUE_AND_STATIC) {
            toReturn = DEFAULT_VALUE_AND_STATIC;
        } else {
            ObjectAndStatic<Value> objectAndStatic = new ObjectAndStatic(macroResult.getObject(), isStatic);
        }
        if (toReturn.isStatic()) {
            this.mMacroEvaluationCache.put(macroName, toReturn);
        }
        return toReturn;
    }

    private ObjectAndStatic<Value> macroExpandValue(Value value, Set<String> pendingMacroExpansions, ValueBuilder debugValueBuilder) {
        if (!value.getContainsReferences()) {
            return new ObjectAndStatic(value, true);
        }
        Builder builder;
        int i;
        ObjectAndStatic<Value> macroExpand;
        switch (value.getType()) {
            case LIST:
                builder = ResourceUtil.newValueBuilderBasedOnValue(value);
                for (i = 0; i < value.getListItemCount(); i++) {
                    macroExpand = macroExpandValue(value.getListItem(i), pendingMacroExpansions, debugValueBuilder.getListItem(i));
                    if (macroExpand == DEFAULT_VALUE_AND_STATIC) {
                        return DEFAULT_VALUE_AND_STATIC;
                    }
                    builder.addListItem((Value) macroExpand.getObject());
                }
                return new ObjectAndStatic(builder.build(), false);
            case MAP:
                builder = ResourceUtil.newValueBuilderBasedOnValue(value);
                if (value.getMapKeyCount() != value.getMapValueCount()) {
                    Log.e("Invalid serving value: " + value.toString());
                    return DEFAULT_VALUE_AND_STATIC;
                }
                for (i = 0; i < value.getMapKeyCount(); i++) {
                    ObjectAndStatic<Value> macroExpandKey = macroExpandValue(value.getMapKey(i), pendingMacroExpansions, debugValueBuilder.getMapKey(i));
                    ObjectAndStatic<Value> macroExpandValue = macroExpandValue(value.getMapValue(i), pendingMacroExpansions, debugValueBuilder.getMapValue(i));
                    if (macroExpandKey == DEFAULT_VALUE_AND_STATIC || macroExpandValue == DEFAULT_VALUE_AND_STATIC) {
                        return DEFAULT_VALUE_AND_STATIC;
                    }
                    builder.addMapKey((Value) macroExpandKey.getObject());
                    builder.addMapValue((Value) macroExpandValue.getObject());
                }
                return new ObjectAndStatic(builder.build(), false);
            case MACRO_REFERENCE:
                if (pendingMacroExpansions.contains(value.getMacroReference())) {
                    Log.e("Macro cycle detected.  Current macro reference: " + value.getMacroReference() + "." + "  Previous macro references: " + pendingMacroExpansions.toString() + ".");
                    return DEFAULT_VALUE_AND_STATIC;
                }
                pendingMacroExpansions.add(value.getMacroReference());
                ObjectAndStatic<Value> result = ValueEscapeUtil.applyEscapings(evaluateMacroReferenceCycleDetection(value.getMacroReference(), pendingMacroExpansions, debugValueBuilder.createValueMacroEvaluationInfoExtension()), value.getEscapingList());
                pendingMacroExpansions.remove(value.getMacroReference());
                return result;
            case TEMPLATE:
                builder = ResourceUtil.newValueBuilderBasedOnValue(value);
                for (i = 0; i < value.getTemplateTokenCount(); i++) {
                    macroExpand = macroExpandValue(value.getTemplateToken(i), pendingMacroExpansions, debugValueBuilder.getTemplateToken(i));
                    if (macroExpand == DEFAULT_VALUE_AND_STATIC) {
                        return DEFAULT_VALUE_AND_STATIC;
                    }
                    builder.addTemplateToken((Value) macroExpand.getObject());
                }
                return new ObjectAndStatic(builder.build(), false);
            default:
                Log.e("Unknown type: " + value.getType());
                return DEFAULT_VALUE_AND_STATIC;
        }
    }

    private ObjectAndStatic<Value> executeFunction(Map<String, FunctionCallImplementation> implMap, ExpandedFunctionCall functionCall, Set<String> pendingMacroExpansions, ResolvedFunctionCallBuilder debugFunctionCall) {
        Value functionIdValue = (Value) functionCall.getProperties().get(Key.FUNCTION.toString());
        if (functionIdValue == null) {
            Log.e("No function id in properties");
            return DEFAULT_VALUE_AND_STATIC;
        }
        String functionId = functionIdValue.getFunctionId();
        FunctionCallImplementation impl = (FunctionCallImplementation) implMap.get(functionId);
        if (impl == null) {
            Log.e(functionId + " has no backing implementation.");
            return DEFAULT_VALUE_AND_STATIC;
        }
        ObjectAndStatic<Value> cachedResult = (ObjectAndStatic) this.mFunctionCallCache.get(functionCall);
        if (cachedResult != null && !this.eventInfoDistributor.debugMode()) {
            return cachedResult;
        }
        ObjectAndStatic<Value> result;
        Map<String, Value> expandedParams = new HashMap();
        boolean allParamsStatic = true;
        for (Entry<String, Value> originalParam : functionCall.getProperties().entrySet()) {
            result = macroExpandValue((Value) originalParam.getValue(), pendingMacroExpansions, debugFunctionCall.createResolvedPropertyBuilder((String) originalParam.getKey()).createPropertyValueBuilder((Value) originalParam.getValue()));
            if (result == DEFAULT_VALUE_AND_STATIC) {
                return DEFAULT_VALUE_AND_STATIC;
            }
            if (result.isStatic()) {
                functionCall.updateCacheableProperty((String) originalParam.getKey(), (Value) result.getObject());
            } else {
                allParamsStatic = false;
            }
            expandedParams.put(originalParam.getKey(), result.getObject());
        }
        if (impl.hasRequiredKeys(expandedParams.keySet())) {
            boolean cacheable = allParamsStatic && impl.isCacheable();
            result = new ObjectAndStatic(impl.evaluate(expandedParams), cacheable);
            if (cacheable) {
                this.mFunctionCallCache.put(functionCall, result);
            }
            debugFunctionCall.setFunctionResult((Value) result.getObject());
            return result;
        }
        Log.e("Incorrect keys for function " + functionId + " required " + impl.getRequiredKeys() + " had " + expandedParams.keySet());
        return DEFAULT_VALUE_AND_STATIC;
    }

    private static void verifyFunctionAndNameListSizes(List<ExpandedFunctionCall> functionList, List<String> ruleNameList, String operation) {
        if (functionList.size() != ruleNameList.size()) {
            Log.i("Invalid resource: imbalance of rule names of functions for " + operation + " operation. Using default rule name instead");
        }
    }
}
