package flexjson;

import java.util.Iterator;
import java.util.Set;

public class ChainedIterator implements Iterator {
    int current = 0;
    Iterator[] iterators;

    public ChainedIterator(Set... sets) {
        this.iterators = new Iterator[sets.length];
        for (int i = 0; i < sets.length; i++) {
            this.iterators[i] = sets[i].iterator();
        }
    }

    public boolean hasNext() {
        if (this.iterators[this.current].hasNext()) {
            return true;
        }
        this.current++;
        if (this.current >= this.iterators.length || !this.iterators[this.current].hasNext()) {
            return false;
        }
        return true;
    }

    public Object next() {
        return this.iterators[this.current].next();
    }

    public void remove() {
        this.iterators[this.current].remove();
    }
}
