package org.apache.james.mime4j.message;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.james.mime4j.util.ByteSequence;
import org.apache.james.mime4j.util.ContentUtil;

public class Multipart implements Body {
    private List<BodyPart> bodyParts;
    private ByteSequence epilogue;
    private transient String epilogueStrCache;
    private Entity parent;
    private ByteSequence preamble;
    private transient String preambleStrCache;
    private String subType;

    public Multipart(String subType) {
        this.bodyParts = new LinkedList();
        this.parent = null;
        this.preamble = ByteSequence.EMPTY;
        this.preambleStrCache = "";
        this.epilogue = ByteSequence.EMPTY;
        this.epilogueStrCache = "";
        this.subType = subType;
    }

    public Multipart(Multipart other) {
        this.bodyParts = new LinkedList();
        this.parent = null;
        this.preamble = other.preamble;
        this.preambleStrCache = other.preambleStrCache;
        this.epilogue = other.epilogue;
        this.epilogueStrCache = other.epilogueStrCache;
        for (BodyPart otherBodyPart : other.bodyParts) {
            addBodyPart(new BodyPart(otherBodyPart));
        }
        this.subType = other.subType;
    }

    public String getSubType() {
        return this.subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public Entity getParent() {
        return this.parent;
    }

    public void setParent(Entity parent) {
        this.parent = parent;
        for (BodyPart bodyPart : this.bodyParts) {
            bodyPart.setParent(parent);
        }
    }

    public int getCount() {
        return this.bodyParts.size();
    }

    public List<BodyPart> getBodyParts() {
        return Collections.unmodifiableList(this.bodyParts);
    }

    public void setBodyParts(List<BodyPart> bodyParts) {
        this.bodyParts = bodyParts;
        for (BodyPart bodyPart : bodyParts) {
            bodyPart.setParent(this.parent);
        }
    }

    public void addBodyPart(BodyPart bodyPart) {
        if (bodyPart == null) {
            throw new IllegalArgumentException();
        }
        this.bodyParts.add(bodyPart);
        bodyPart.setParent(this.parent);
    }

    public void addBodyPart(BodyPart bodyPart, int index) {
        if (bodyPart == null) {
            throw new IllegalArgumentException();
        }
        this.bodyParts.add(index, bodyPart);
        bodyPart.setParent(this.parent);
    }

    public BodyPart removeBodyPart(int index) {
        BodyPart bodyPart = (BodyPart) this.bodyParts.remove(index);
        bodyPart.setParent(null);
        return bodyPart;
    }

    public BodyPart replaceBodyPart(BodyPart bodyPart, int index) {
        if (bodyPart == null) {
            throw new IllegalArgumentException();
        }
        BodyPart replacedBodyPart = (BodyPart) this.bodyParts.set(index, bodyPart);
        if (bodyPart == replacedBodyPart) {
            throw new IllegalArgumentException("Cannot replace body part with itself");
        }
        bodyPart.setParent(this.parent);
        replacedBodyPart.setParent(null);
        return replacedBodyPart;
    }

    /* access modifiers changed from: 0000 */
    public ByteSequence getPreambleRaw() {
        return this.preamble;
    }

    /* access modifiers changed from: 0000 */
    public void setPreambleRaw(ByteSequence preamble) {
        this.preamble = preamble;
        this.preambleStrCache = null;
    }

    public String getPreamble() {
        if (this.preambleStrCache == null) {
            this.preambleStrCache = ContentUtil.decode(this.preamble);
        }
        return this.preambleStrCache;
    }

    public void setPreamble(String preamble) {
        this.preamble = ContentUtil.encode(preamble);
        this.preambleStrCache = preamble;
    }

    /* access modifiers changed from: 0000 */
    public ByteSequence getEpilogueRaw() {
        return this.epilogue;
    }

    /* access modifiers changed from: 0000 */
    public void setEpilogueRaw(ByteSequence epilogue) {
        this.epilogue = epilogue;
        this.epilogueStrCache = null;
    }

    public String getEpilogue() {
        if (this.epilogueStrCache == null) {
            this.epilogueStrCache = ContentUtil.decode(this.epilogue);
        }
        return this.epilogueStrCache;
    }

    public void setEpilogue(String epilogue) {
        this.epilogue = ContentUtil.encode(epilogue);
        this.epilogueStrCache = epilogue;
    }

    public void dispose() {
        for (BodyPart bodyPart : this.bodyParts) {
            bodyPart.dispose();
        }
    }
}
