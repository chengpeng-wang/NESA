package com.vr.installer.scanner;

import java.util.Comparator;

public class PackageComparator implements Comparator<PackageDescription> {
    public int compare(PackageDescription lhs, PackageDescription rhs) {
        if (lhs.getOrder() == rhs.getOrder()) {
            return 0;
        }
        return lhs.getOrder() > rhs.getOrder() ? 1 : -1;
    }
}
