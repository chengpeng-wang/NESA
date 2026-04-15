package com.sun.mail.handlers;

import javax.activation.ActivationDataFlavor;

public class text_xml extends text_plain {
    private static ActivationDataFlavor myDF = new ActivationDataFlavor(String.class, "text/xml", "XML String");

    /* access modifiers changed from: protected */
    public ActivationDataFlavor getDF() {
        return myDF;
    }
}
