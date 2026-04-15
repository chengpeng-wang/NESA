package flexjson;

import flexjson.transformer.Transformer;
import flexjson.transformer.TypeTransformerMap;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class JSONContext {
    private static ThreadLocal<JSONContext> context = new ThreadLocal<JSONContext>() {
        /* access modifiers changed from: protected */
        public JSONContext initialValue() {
            return new JSONContext();
        }
    };
    private int indent = 0;
    private LinkedList<Object> objectStack = new LinkedList();
    private OutputHandler out;
    private Path path = new Path();
    private List<PathExpression> pathExpressions;
    private Map<Path, Transformer> pathTransformerMap;
    private boolean prettyPrint = false;
    private String rootName;
    private SerializationType serializationType = SerializationType.SHALLOW;
    private Stack<TypeContext> typeContextStack = new Stack();
    private TypeTransformerMap typeTransformerMap;
    private ChainedSet visits = new ChainedSet(Collections.EMPTY_SET);

    public void serializationType(SerializationType serializationType) {
        this.serializationType = serializationType;
    }

    public void transform(Object object) {
        Transformer transformer = getPathTransformer(object);
        if (transformer == null) {
            transformer = getTypeTransformer(object);
        }
        transformer.transform(object);
    }

    public Transformer getTransformer(Object object) {
        Transformer transformer = getPathTransformer(object);
        if (transformer == null) {
            return getTypeTransformer(object);
        }
        return transformer;
    }

    private Transformer getPathTransformer(Object object) {
        if (object == null) {
            return getTypeTransformer(object);
        }
        return (Transformer) this.pathTransformerMap.get(this.path);
    }

    private Transformer getTypeTransformer(Object object) {
        return this.typeTransformerMap.getTransformer(object);
    }

    public void setTypeTransformers(TypeTransformerMap typeTransformerMap) {
        this.typeTransformerMap = typeTransformerMap;
    }

    public void setPathTransformers(Map<Path, Transformer> pathTransformerMap) {
        this.pathTransformerMap = pathTransformerMap;
    }

    public void setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    public void pushTypeContext(TypeContext contextEnum) {
        this.typeContextStack.push(contextEnum);
    }

    public void popTypeContext() {
        this.typeContextStack.pop();
    }

    public TypeContext peekTypeContext() {
        if (this.typeContextStack.isEmpty()) {
            return null;
        }
        return (TypeContext) this.typeContextStack.peek();
    }

    public void setOut(OutputHandler out) {
        this.out = out;
    }

    public OutputHandler getOut() {
        return this.out;
    }

    public void write(String value) {
        TypeContext currentTypeContext = peekTypeContext();
        if (currentTypeContext != null && currentTypeContext.getBasicType() == BasicType.ARRAY) {
            writeIndent();
        }
        this.out.write(value);
    }

    public TypeContext writeOpenObject() {
        if (this.prettyPrint) {
            TypeContext currentTypeContext = peekTypeContext();
            if (currentTypeContext != null && currentTypeContext.getBasicType() == BasicType.ARRAY) {
                writeIndent();
            }
        }
        TypeContext typeContext = new TypeContext(BasicType.OBJECT);
        pushTypeContext(typeContext);
        this.out.write("{");
        if (this.prettyPrint) {
            this.indent += 4;
            this.out.write("\n");
        }
        return typeContext;
    }

    public void writeCloseObject() {
        if (this.prettyPrint) {
            this.out.write("\n");
            this.indent -= 4;
            writeIndent();
        }
        this.out.write("}");
        popTypeContext();
    }

    public void writeName(String name) {
        if (this.prettyPrint) {
            writeIndent();
        }
        if (name != null) {
            writeQuoted(name);
        } else {
            write("null");
        }
        this.out.write(":");
        if (this.prettyPrint) {
            this.out.write(" ");
        }
    }

    public void writeComma() {
        this.out.write(",");
        if (this.prettyPrint) {
            this.out.write("\n");
        }
    }

    public TypeContext writeOpenArray() {
        if (this.prettyPrint) {
            TypeContext currentTypeContext = peekTypeContext();
            if (currentTypeContext != null && currentTypeContext.getBasicType() == BasicType.ARRAY) {
                writeIndent();
            }
        }
        TypeContext typeContext = new TypeContext(BasicType.ARRAY);
        pushTypeContext(typeContext);
        this.out.write("[");
        if (this.prettyPrint) {
            this.indent += 4;
            this.out.write("\n");
        }
        return typeContext;
    }

    public void writeCloseArray() {
        if (this.prettyPrint) {
            this.out.write("\n");
            this.indent -= 4;
            writeIndent();
        }
        this.out.write("]");
        popTypeContext();
    }

    public void writeIndent() {
        for (int i = 0; i < this.indent; i++) {
            this.out.write(" ");
        }
    }

    public void writeQuoted(String value) {
        if (this.prettyPrint) {
            TypeContext currentTypeContext = peekTypeContext();
            if (currentTypeContext != null && currentTypeContext.getBasicType() == BasicType.ARRAY) {
                writeIndent();
            }
        }
        this.out.write("\"");
        int last = 0;
        int len = value.length();
        for (int i = 0; i < len; i++) {
            char c = value.charAt(i);
            if (c == '\"') {
                last = this.out.write(value, last, i, "\\\"");
            } else if (c == '\\') {
                last = this.out.write(value, last, i, "\\\\");
            } else if (c == 8) {
                last = this.out.write(value, last, i, "\\b");
            } else if (c == 12) {
                last = this.out.write(value, last, i, "\\f");
            } else if (c == 10) {
                last = this.out.write(value, last, i, "\\n");
            } else if (c == 13) {
                last = this.out.write(value, last, i, "\\r");
            } else if (c == 9) {
                last = this.out.write(value, last, i, "\\t");
            } else if (Character.isISOControl(c)) {
                last = this.out.write(value, last, i) + 1;
                unicode(c);
            }
        }
        if (last < value.length()) {
            this.out.write(value, last, value.length());
        }
        this.out.write("\"");
    }

    private void unicode(char c) {
        this.out.write("\\u");
        int n = c;
        for (int i = 0; i < 4; i++) {
            this.out.write(String.valueOf(JSONSerializer.HEX[(61440 & n) >> 12]));
            n <<= 4;
        }
    }

    public static JSONContext get() {
        return (JSONContext) context.get();
    }

    public static void cleanup() {
        context.remove();
    }

    public ChainedSet getVisits() {
        return this.visits;
    }

    public void setVisits(ChainedSet visits) {
        this.visits = visits;
    }

    public LinkedList<Object> getObjectStack() {
        return this.objectStack;
    }

    public String getRootName() {
        return this.rootName;
    }

    public void setRootName(String rootName) {
        this.rootName = rootName;
    }

    public Path getPath() {
        return this.path;
    }

    public void setPathExpressions(List<PathExpression> pathExpressions) {
        this.pathExpressions = pathExpressions;
    }

    public boolean isIncluded(BeanProperty prop) {
        PathExpression expression = matches(this.pathExpressions);
        if (expression != null) {
            return expression.isIncluded();
        }
        Boolean annotation = prop.isAnnotated();
        if (annotation != null) {
            return annotation.booleanValue();
        }
        if (this.serializationType != SerializationType.SHALLOW) {
            return true;
        }
        Class propType = prop.getPropertyType();
        if (propType.isArray() || Iterable.class.isAssignableFrom(propType)) {
            return false;
        }
        return true;
    }

    public boolean isIncluded(String key, Object value) {
        PathExpression expression = matches(this.pathExpressions);
        if (expression != null) {
            return expression.isIncluded();
        }
        String rootName = ((JSONContext) context.get()).getRootName();
        if (value == null || ((this.serializationType != SerializationType.SHALLOW || rootName == null || this.path.length() <= 1) && (this.serializationType != SerializationType.SHALLOW || rootName != null))) {
            return true;
        }
        Class type = value.getClass();
        return (type.isArray() || Iterable.class.isAssignableFrom(type)) ? false : true;
    }

    public boolean isIncluded(Field field) {
        PathExpression expression = matches(this.pathExpressions);
        if (expression != null) {
            return expression.isIncluded();
        }
        if (field.isAnnotationPresent(JSON.class)) {
            return ((JSON) field.getAnnotation(JSON.class)).include();
        }
        if (this.serializationType != SerializationType.SHALLOW) {
            return true;
        }
        Class type = field.getType();
        if (type.isArray() || Iterable.class.isAssignableFrom(type)) {
            return false;
        }
        return true;
    }

    public boolean isValidField(Field field) {
        return (Modifier.isStatic(field.getModifiers()) || !Modifier.isPublic(field.getModifiers()) || Modifier.isTransient(field.getModifiers())) ? false : true;
    }

    /* access modifiers changed from: protected */
    public PathExpression matches(List<PathExpression> expressions) {
        for (PathExpression expr : expressions) {
            if (expr.matches(this.path)) {
                return expr;
            }
        }
        return null;
    }
}
