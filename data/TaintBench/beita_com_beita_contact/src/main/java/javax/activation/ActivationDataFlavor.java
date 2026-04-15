package javax.activation;

import myjava.awt.datatransfer.DataFlavor;

public class ActivationDataFlavor extends DataFlavor {
    private String humanPresentableName;
    private MimeType mimeObject;
    private String mimeType;
    private Class representationClass;

    public ActivationDataFlavor(Class representationClass, String mimeType, String humanPresentableName) {
        super(mimeType, humanPresentableName);
        this.mimeType = null;
        this.mimeObject = null;
        this.humanPresentableName = null;
        this.representationClass = null;
        this.mimeType = mimeType;
        this.humanPresentableName = humanPresentableName;
        this.representationClass = representationClass;
    }

    public ActivationDataFlavor(Class representationClass, String humanPresentableName) {
        super(representationClass, humanPresentableName);
        this.mimeType = null;
        this.mimeObject = null;
        this.humanPresentableName = null;
        this.representationClass = null;
        this.mimeType = super.getMimeType();
        this.representationClass = representationClass;
        this.humanPresentableName = humanPresentableName;
    }

    public ActivationDataFlavor(String mimeType, String humanPresentableName) {
        super(mimeType, humanPresentableName);
        this.mimeType = null;
        this.mimeObject = null;
        this.humanPresentableName = null;
        this.representationClass = null;
        this.mimeType = mimeType;
        try {
            this.representationClass = Class.forName("java.io.InputStream");
        } catch (ClassNotFoundException e) {
        }
        this.humanPresentableName = humanPresentableName;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public Class getRepresentationClass() {
        return this.representationClass;
    }

    public String getHumanPresentableName() {
        return this.humanPresentableName;
    }

    public void setHumanPresentableName(String humanPresentableName) {
        this.humanPresentableName = humanPresentableName;
    }

    public boolean equals(DataFlavor dataFlavor) {
        return isMimeTypeEqual(dataFlavor) && dataFlavor.getRepresentationClass() == this.representationClass;
    }

    public boolean isMimeTypeEqual(String mimeType) {
        try {
            if (this.mimeObject == null) {
                this.mimeObject = new MimeType(this.mimeType);
            }
            MimeType mt = new MimeType(mimeType);
            MimeType mimeType2 = mt;
            return this.mimeObject.match(mt);
        } catch (MimeTypeParseException e) {
            return this.mimeType.equalsIgnoreCase(mimeType);
        }
    }

    /* access modifiers changed from: protected */
    public String normalizeMimeTypeParameter(String parameterName, String parameterValue) {
        return parameterValue;
    }

    /* access modifiers changed from: protected */
    public String normalizeMimeType(String mimeType) {
        return mimeType;
    }
}
