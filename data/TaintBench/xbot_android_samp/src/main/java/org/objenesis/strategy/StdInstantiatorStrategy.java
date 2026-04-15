package org.objenesis.strategy;

import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.instantiator.gcj.GCJInstantiator;
import org.objenesis.instantiator.jrockit.JRockit131Instantiator;
import org.objenesis.instantiator.jrockit.JRockitLegacyInstantiator;
import org.objenesis.instantiator.perc.PercInstantiator;
import org.objenesis.instantiator.sun.Sun13Instantiator;
import org.objenesis.instantiator.sun.SunReflectionFactoryInstantiator;

public class StdInstantiatorStrategy extends BaseInstantiatorStrategy {
    public ObjectInstantiator newInstantiatorOf(Class type) {
        if (JVM_NAME.startsWith("Java HotSpot")) {
            if (VM_VERSION.startsWith("1.3")) {
                return new Sun13Instantiator(type);
            }
        } else if (JVM_NAME.startsWith("BEA")) {
            if (VM_VERSION.startsWith("1.3")) {
                return new JRockit131Instantiator(type);
            }
            if (!(!VM_VERSION.startsWith("1.4") || VENDOR_VERSION.startsWith("R") || (VM_INFO != null && VM_INFO.startsWith("R25.1") && VM_INFO.startsWith("R25.2")))) {
                return new JRockitLegacyInstantiator(type);
            }
        } else if (JVM_NAME.startsWith("GNU libgcj")) {
            return new GCJInstantiator(type);
        } else {
            if (JVM_NAME.startsWith("PERC")) {
                return new PercInstantiator(type);
            }
        }
        return new SunReflectionFactoryInstantiator(type);
    }
}
