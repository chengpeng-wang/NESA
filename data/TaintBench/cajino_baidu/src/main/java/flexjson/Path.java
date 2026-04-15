package flexjson;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Path {
    LinkedList<String> path = new LinkedList();

    public Path(String... fields) {
        for (String field : fields) {
            this.path.add(field);
        }
    }

    public Path enqueue(String field) {
        this.path.add(field);
        return this;
    }

    public String pop() {
        return (String) this.path.removeLast();
    }

    public List<String> getPath() {
        return this.path;
    }

    public int length() {
        return this.path.size();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("[ ");
        boolean afterFirst = false;
        Iterator i$ = this.path.iterator();
        while (i$.hasNext()) {
            String current = (String) i$.next();
            if (afterFirst) {
                builder.append(".");
            }
            builder.append(current);
            afterFirst = true;
        }
        builder.append(" ]");
        return builder.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (this.path.equals(((Path) o).path)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.path.hashCode();
    }

    public static Path parse(String path) {
        return path != null ? new Path(path.split("\\.")) : new Path();
    }
}
