package flexjson;

public interface ClassLocator {
    Class locate(ObjectBinder objectBinder, Path path) throws ClassNotFoundException;
}
