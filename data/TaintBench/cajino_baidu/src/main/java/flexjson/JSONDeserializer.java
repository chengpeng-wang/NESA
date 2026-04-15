package flexjson;

import flexjson.factories.ClassLocatorObjectFactory;
import flexjson.factories.ExistingObjectFactory;
import flexjson.locators.StaticClassLocator;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class JSONDeserializer<T> {
    private Map<Path, ObjectFactory> pathFactories = new HashMap();
    private Map<Class, ObjectFactory> typeFactories = new HashMap();

    public T deserialize(String input) {
        return createObjectBinder().bind(new JSONTokener(input).nextValue());
    }

    public T deserialize(Reader input) {
        return createObjectBinder().bind(new JSONTokener(input).nextValue());
    }

    public T deserialize(String input, Class root) {
        return createObjectBinder().bind(new JSONTokener(input).nextValue(), (Type) root);
    }

    public T deserialize(Reader input, Class root) {
        return createObjectBinder().bind(new JSONTokener(input).nextValue(), (Type) root);
    }

    public T deserialize(String input, ObjectFactory factory) {
        use((String) null, factory);
        return createObjectBinder().bind(new JSONTokener(input).nextValue());
    }

    public T deserialize(Reader input, ObjectFactory factory) {
        use((String) null, factory);
        return createObjectBinder().bind(new JSONTokener(input).nextValue());
    }

    public T deserializeInto(String input, T target) {
        return deserialize(input, new ExistingObjectFactory(target));
    }

    public T deserializeInto(Reader input, T target) {
        return deserialize(input, new ExistingObjectFactory(target));
    }

    public JSONDeserializer<T> use(String path, ClassLocator locator) {
        this.pathFactories.put(Path.parse(path), new ClassLocatorObjectFactory(locator));
        return this;
    }

    public JSONDeserializer<T> use(String path, Class clazz) {
        return use(path, new StaticClassLocator(clazz));
    }

    public JSONDeserializer<T> use(Class clazz, ObjectFactory factory) {
        this.typeFactories.put(clazz, factory);
        return this;
    }

    public JSONDeserializer<T> use(String path, ObjectFactory factory) {
        this.pathFactories.put(Path.parse(path), factory);
        return this;
    }

    public JSONDeserializer<T> use(ObjectFactory factory, String... paths) {
        for (String p : paths) {
            use(p, factory);
        }
        return this;
    }

    private ObjectBinder createObjectBinder() {
        ObjectBinder binder = new ObjectBinder();
        for (Class clazz : this.typeFactories.keySet()) {
            binder.use(clazz, (ObjectFactory) this.typeFactories.get(clazz));
        }
        for (Path p : this.pathFactories.keySet()) {
            binder.use(p, (ObjectFactory) this.pathFactories.get(p));
        }
        return binder;
    }
}
