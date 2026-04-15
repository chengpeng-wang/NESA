package flexjson.locators;

import flexjson.ClassLocator;
import flexjson.ObjectBinder;
import flexjson.Path;

public class StaticClassLocator implements ClassLocator {
    private Class target;

    public StaticClassLocator(Class clazz) {
        this.target = clazz;
    }

    public Class locate(ObjectBinder context, Path currentPath) {
        return this.target;
    }
}
