package exts.whats.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class Lists {
    public static <T> ArrayList<T> newArrayList() {
        return new ArrayList();
    }

    public static <T> ArrayList<T> newArrayList(int paramInt) {
        return new ArrayList(paramInt);
    }

    public static <T> ArrayList<T> newArrayList(Collection<T> paramCollection) {
        if (paramCollection != null) {
        }
        ArrayList localArrayList = newArrayList(paramCollection.size());
        localArrayList.addAll(paramCollection);
        return localArrayList;
    }

    public static <T> ArrayList<T> newArrayList(T[] paramArrayOfT) {
        ArrayList localArrayList = new ArrayList(paramArrayOfT.length);
        for (Object add : paramArrayOfT) {
            localArrayList.add(add);
        }
        return localArrayList;
    }

    public static <T> LinkedList<T> newLinkedList() {
        return new LinkedList();
    }
}
