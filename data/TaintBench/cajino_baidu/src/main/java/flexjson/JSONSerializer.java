package flexjson;

import flexjson.transformer.Transformer;
import flexjson.transformer.TransformerWrapper;
import flexjson.transformer.TypeTransformerMap;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONSerializer {
    public static final char[] HEX = "0123456789ABCDEF".toCharArray();
    private List<PathExpression> pathExpressions = new ArrayList();
    private Map<Path, Transformer> pathTransformerMap = new HashMap();
    private boolean prettyPrint;
    private String rootName;
    private TypeTransformerMap typeTransformerMap = new TypeTransformerMap(TransformerUtil.getDefaultTypeTransformers());

    public JSONSerializer prettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
        return this;
    }

    public JSONSerializer rootName(String rootName) {
        this.rootName = rootName;
        return this;
    }

    public String serialize(Object target) {
        return serialize(target, SerializationType.SHALLOW, new StringBuilderOutputHandler(new StringBuilder()));
    }

    public void serialize(Object target, Writer out) {
        serialize(target, SerializationType.SHALLOW, new WriterOutputHandler(out));
    }

    public String serialize(Object target, StringBuilder out) {
        return serialize(target, SerializationType.SHALLOW, new StringBuilderOutputHandler(out));
    }

    public String serialize(Object target, StringBuffer out) {
        return serialize(target, SerializationType.SHALLOW, new StringBufferOutputHandler(out));
    }

    public String serialize(Object target, OutputHandler out) {
        return serialize(target, SerializationType.SHALLOW, out);
    }

    public String deepSerialize(Object target) {
        return serialize(target, SerializationType.DEEP, new StringBuilderOutputHandler(new StringBuilder()));
    }

    public void deepSerialize(Object target, Writer out) {
        serialize(target, SerializationType.DEEP, new WriterOutputHandler(out));
    }

    public String deepSerialize(Object target, StringBuilder out) {
        return serialize(target, SerializationType.DEEP, new StringBuilderOutputHandler(out));
    }

    public String deepSerialize(Object target, StringBuffer out) {
        return serialize(target, SerializationType.DEEP, new StringBufferOutputHandler(out));
    }

    public String deepSerialize(Object target, OutputHandler out) {
        return serialize(target, SerializationType.DEEP, out);
    }

    /* access modifiers changed from: protected */
    public String serialize(Object target, SerializationType serializationType, OutputHandler out) {
        String output = "";
        JSONContext context = JSONContext.get();
        context.setRootName(this.rootName);
        context.setPrettyPrint(this.prettyPrint);
        context.setOut(out);
        context.serializationType(serializationType);
        context.setTypeTransformers(this.typeTransformerMap);
        context.setPathTransformers(this.pathTransformerMap);
        context.setPathExpressions(this.pathExpressions);
        try {
            String rootName = context.getRootName();
            if (rootName == null || rootName.trim().equals("")) {
                context.transform(target);
            } else {
                context.writeOpenObject();
                context.writeName(rootName);
                context.transform(target);
                context.writeCloseObject();
            }
            output = context.getOut().toString();
            return output;
        } finally {
            JSONContext.cleanup();
        }
    }

    public JSONSerializer transform(Transformer transformer, String... fields) {
        Transformer transformer2 = new TransformerWrapper(transformer);
        for (String field : fields) {
            if (field.length() == 0) {
                this.pathTransformerMap.put(new Path(), transformer2);
            } else {
                this.pathTransformerMap.put(new Path(field.split("\\.")), transformer2);
            }
        }
        return this;
    }

    public JSONSerializer transform(Transformer transformer, Class... types) {
        Transformer transformer2 = new TransformerWrapper(transformer);
        for (Class type : types) {
            this.typeTransformerMap.put(type, transformer2);
        }
        return this;
    }

    /* access modifiers changed from: protected */
    public void addExclude(String field) {
        int index = field.lastIndexOf(46);
        if (index > 0) {
            PathExpression expression = new PathExpression(field.substring(0, index), true);
            if (!expression.isWildcard()) {
                this.pathExpressions.add(expression);
            }
        }
        this.pathExpressions.add(new PathExpression(field, false));
    }

    /* access modifiers changed from: protected */
    public void addInclude(String field) {
        this.pathExpressions.add(new PathExpression(field, true));
    }

    public JSONSerializer exclude(String... fields) {
        for (String field : fields) {
            addExclude(field);
        }
        return this;
    }

    public JSONSerializer include(String... fields) {
        for (String field : fields) {
            addInclude(field);
        }
        return this;
    }

    public List<PathExpression> getIncludes() {
        List<PathExpression> expressions = new ArrayList();
        for (PathExpression expression : this.pathExpressions) {
            if (expression.isIncluded()) {
                expressions.add(expression);
            }
        }
        return expressions;
    }

    public List<PathExpression> getExcludes() {
        List<PathExpression> excludes = new ArrayList();
        for (PathExpression expression : this.pathExpressions) {
            if (!expression.isIncluded()) {
                excludes.add(expression);
            }
        }
        return excludes;
    }

    public void setIncludes(List<String> fields) {
        for (String field : fields) {
            this.pathExpressions.add(new PathExpression(field, true));
        }
    }

    public void setExcludes(List<String> fields) {
        for (String field : fields) {
            addExclude(field);
        }
    }
}
