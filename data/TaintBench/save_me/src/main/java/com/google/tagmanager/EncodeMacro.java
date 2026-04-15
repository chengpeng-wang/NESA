package com.google.tagmanager;

import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.containertag.common.Key;
import com.google.analytics.midtier.proto.containertag.TypeSystem.Value;
import java.util.Map;

class EncodeMacro extends FunctionCallImplementation {
    private static final String ARG0 = Key.ARG0.toString();
    private static final String DEFAULT_INPUT_FORMAT = "text";
    private static final String DEFAULT_OUTPUT_FORMAT = "base16";
    private static final String ID = FunctionType.ENCODE.toString();
    private static final String INPUT_FORMAT = Key.INPUT_FORMAT.toString();
    private static final String NO_PADDING = Key.NO_PADDING.toString();
    private static final String OUTPUT_FORMAT = Key.OUTPUT_FORMAT.toString();

    public static String getFunctionId() {
        return ID;
    }

    public EncodeMacro() {
        super(ID, ARG0);
    }

    public boolean isCacheable() {
        return true;
    }

    public Value evaluate(Map<String, Value> parameters) {
        Value argumentParameter = (Value) parameters.get(ARG0);
        if (argumentParameter == null || argumentParameter == Types.getDefaultValue()) {
            return Types.getDefaultValue();
        }
        String argument = Types.valueToString(argumentParameter);
        Value inputFormatParameter = (Value) parameters.get(INPUT_FORMAT);
        String inputFormat = inputFormatParameter == null ? DEFAULT_INPUT_FORMAT : Types.valueToString(inputFormatParameter);
        Value outputFormatParameter = (Value) parameters.get(OUTPUT_FORMAT);
        String outputFormat = outputFormatParameter == null ? DEFAULT_OUTPUT_FORMAT : Types.valueToString(outputFormatParameter);
        Value inputFormatValue = (Value) parameters.get(INPUT_FORMAT);
        int flags = 0;
        Value noPaddingParameter = (Value) parameters.get(NO_PADDING);
        if (noPaddingParameter != null && Types.valueToBoolean(noPaddingParameter).booleanValue()) {
            flags = 0 | 1;
        }
        try {
            byte[] inputBytes;
            String encoded;
            if (DEFAULT_INPUT_FORMAT.equals(inputFormat)) {
                inputBytes = argument.getBytes();
            } else if (DEFAULT_OUTPUT_FORMAT.equals(inputFormat)) {
                inputBytes = Base16.decode(argument);
            } else if ("base64".equals(inputFormat)) {
                inputBytes = Base64Encoder.decode(argument, flags);
            } else if ("base64url".equals(inputFormat)) {
                inputBytes = Base64Encoder.decode(argument, flags | 2);
            } else {
                Log.e("Encode: unknown input format: " + inputFormat);
                return Types.getDefaultValue();
            }
            if (DEFAULT_OUTPUT_FORMAT.equals(outputFormat)) {
                encoded = Base16.encode(inputBytes);
            } else if ("base64".equals(outputFormat)) {
                encoded = Base64Encoder.encodeToString(inputBytes, flags);
            } else if ("base64url".equals(outputFormat)) {
                encoded = Base64Encoder.encodeToString(inputBytes, flags | 2);
            } else {
                Log.e("Encode: unknown output format: " + outputFormat);
                return Types.getDefaultValue();
            }
            return Types.objectToValue(encoded);
        } catch (IllegalArgumentException e) {
            Log.e("Encode: invalid input:");
            return Types.getDefaultValue();
        }
    }
}
