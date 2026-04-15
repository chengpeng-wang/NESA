package org.xbill.DNS;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.xbill.DNS.Tokenizer.Token;

public class Master {
    private int currentDClass;
    private long currentTTL;
    private int currentType;
    private long defaultTTL;
    private File file;
    private Generator generator;
    private List generators;
    private Master included;
    private Record last;
    private boolean needSOATTL;
    private boolean noExpandGenerate;
    private Name origin;
    private Tokenizer st;

    Master(File file, Name origin, long initialTTL) throws IOException {
        this.last = null;
        this.included = null;
        if (origin == null || origin.isAbsolute()) {
            this.file = file;
            this.st = new Tokenizer(file);
            this.origin = origin;
            this.defaultTTL = initialTTL;
            return;
        }
        throw new RelativeNameException(origin);
    }

    public Master(String filename, Name origin, long ttl) throws IOException {
        this(new File(filename), origin, ttl);
    }

    public Master(String filename, Name origin) throws IOException {
        this(new File(filename), origin, -1);
    }

    public Master(String filename) throws IOException {
        this(new File(filename), null, -1);
    }

    public Master(InputStream in, Name origin, long ttl) {
        this.last = null;
        this.included = null;
        if (origin == null || origin.isAbsolute()) {
            this.st = new Tokenizer(in);
            this.origin = origin;
            this.defaultTTL = ttl;
            return;
        }
        throw new RelativeNameException(origin);
    }

    public Master(InputStream in, Name origin) {
        this(in, origin, -1);
    }

    public Master(InputStream in) {
        this(in, null, -1);
    }

    private Name parseName(String s, Name origin) throws TextParseException {
        try {
            return Name.fromString(s, origin);
        } catch (TextParseException e) {
            throw this.st.exception(e.getMessage());
        }
    }

    private void parseTTLClassAndType() throws IOException {
        boolean seen_class = false;
        String s = this.st.getString();
        int value = DClass.value(s);
        this.currentDClass = value;
        if (value >= 0) {
            s = this.st.getString();
            seen_class = true;
        }
        this.currentTTL = -1;
        try {
            this.currentTTL = TTL.parseTTL(s);
            s = this.st.getString();
        } catch (NumberFormatException e) {
            if (this.defaultTTL >= 0) {
                this.currentTTL = this.defaultTTL;
            } else if (this.last != null) {
                this.currentTTL = this.last.getTTL();
            }
        }
        if (!seen_class) {
            value = DClass.value(s);
            this.currentDClass = value;
            if (value >= 0) {
                s = this.st.getString();
            } else {
                this.currentDClass = 1;
            }
        }
        value = Type.value(s);
        this.currentType = value;
        if (value < 0) {
            throw this.st.exception(new StringBuffer().append("Invalid type '").append(s).append("'").toString());
        } else if (this.currentTTL >= 0) {
        } else {
            if (this.currentType != 6) {
                throw this.st.exception("missing TTL");
            }
            this.needSOATTL = true;
            this.currentTTL = 0;
        }
    }

    private long parseUInt32(String s) {
        if (!Character.isDigit(s.charAt(0))) {
            return -1;
        }
        try {
            long l = Long.parseLong(s);
            if (l < 0 || l > 4294967295L) {
                return -1;
            }
            return l;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void startGenerate() throws IOException {
        String s = this.st.getIdentifier();
        int n = s.indexOf("-");
        if (n < 0) {
            throw this.st.exception(new StringBuffer().append("Invalid $GENERATE range specifier: ").append(s).toString());
        }
        long step;
        String startstr = s.substring(0, n);
        String endstr = s.substring(n + 1);
        String stepstr = null;
        n = endstr.indexOf("/");
        if (n >= 0) {
            stepstr = endstr.substring(n + 1);
            endstr = endstr.substring(0, n);
        }
        long start = parseUInt32(startstr);
        long end = parseUInt32(endstr);
        if (stepstr != null) {
            step = parseUInt32(stepstr);
        } else {
            step = 1;
        }
        if (start < 0 || end < 0 || start > end || step <= 0) {
            throw this.st.exception(new StringBuffer().append("Invalid $GENERATE range specifier: ").append(s).toString());
        }
        String nameSpec = this.st.getIdentifier();
        parseTTLClassAndType();
        if (Generator.supportedType(this.currentType)) {
            String rdataSpec = this.st.getIdentifier();
            this.st.getEOL();
            this.st.unget();
            this.generator = new Generator(start, end, step, nameSpec, this.currentType, this.currentDClass, this.currentTTL, rdataSpec, this.origin);
            if (this.generators == null) {
                this.generators = new ArrayList(1);
            }
            this.generators.add(this.generator);
            return;
        }
        throw this.st.exception(new StringBuffer().append("$GENERATE does not support ").append(Type.string(this.currentType)).append(" records").toString());
    }

    private void endGenerate() throws IOException {
        this.st.getEOL();
        this.generator = null;
    }

    private Record nextGenerated() throws IOException {
        try {
            return this.generator.nextRecord();
        } catch (TokenizerException e) {
            throw this.st.exception(new StringBuffer().append("Parsing $GENERATE: ").append(e.getBaseMessage()).toString());
        } catch (TextParseException e2) {
            throw this.st.exception(new StringBuffer().append("Parsing $GENERATE: ").append(e2.getMessage()).toString());
        }
    }

    public Record _nextRecord() throws IOException {
        Record rec;
        Name name;
        if (this.included != null) {
            rec = this.included.nextRecord();
            if (rec != null) {
                return rec;
            }
            this.included = null;
        }
        if (this.generator != null) {
            rec = nextGenerated();
            if (rec != null) {
                return rec;
            }
            endGenerate();
        }
        while (true) {
            Token token = this.st.get(true, false);
            if (token.type == 2) {
                Token next = this.st.get();
                if (next.type != 1) {
                    if (next.type == 0) {
                        return null;
                    }
                    this.st.unget();
                    if (this.last == null) {
                        throw this.st.exception("no owner");
                    }
                    name = this.last.getName();
                }
            } else if (token.type == 1) {
                continue;
            } else if (token.type == 0) {
                return null;
            } else {
                if (token.value.charAt(0) == '$') {
                    String s = token.value;
                    if (s.equalsIgnoreCase("$ORIGIN")) {
                        this.origin = this.st.getName(Name.root);
                        this.st.getEOL();
                    } else {
                        if (s.equalsIgnoreCase("$TTL")) {
                            this.defaultTTL = this.st.getTTL();
                            this.st.getEOL();
                        } else {
                            if (s.equalsIgnoreCase("$INCLUDE")) {
                                File newfile;
                                String filename = this.st.getString();
                                if (this.file != null) {
                                    newfile = new File(this.file.getParent(), filename);
                                } else {
                                    newfile = new File(filename);
                                }
                                Name incorigin = this.origin;
                                token = this.st.get();
                                if (token.isString()) {
                                    incorigin = parseName(token.value, Name.root);
                                    this.st.getEOL();
                                }
                                this.included = new Master(newfile, incorigin, this.defaultTTL);
                                return nextRecord();
                            }
                            if (!s.equalsIgnoreCase("$GENERATE")) {
                                throw this.st.exception(new StringBuffer().append("Invalid directive: ").append(s).toString());
                            } else if (this.generator != null) {
                                throw new IllegalStateException("cannot nest $GENERATE");
                            } else {
                                startGenerate();
                                if (!this.noExpandGenerate) {
                                    return nextGenerated();
                                }
                                endGenerate();
                            }
                        }
                    }
                } else {
                    name = parseName(token.value, this.origin);
                    if (this.last != null && name.equals(this.last.getName())) {
                        name = this.last.getName();
                    }
                }
            }
        }
        parseTTLClassAndType();
        this.last = Record.fromString(name, this.currentType, this.currentDClass, this.currentTTL, this.st, this.origin);
        if (this.needSOATTL) {
            long ttl = ((SOARecord) this.last).getMinimum();
            this.last.setTTL(ttl);
            this.defaultTTL = ttl;
            this.needSOATTL = false;
        }
        return this.last;
    }

    public Record nextRecord() throws IOException {
        Record rec = null;
        try {
            rec = _nextRecord();
            return rec;
        } finally {
            if (rec == null) {
                this.st.close();
            }
        }
    }

    public void expandGenerate(boolean wantExpand) {
        this.noExpandGenerate = !wantExpand;
    }

    public Iterator generators() {
        if (this.generators != null) {
            return Collections.unmodifiableList(this.generators).iterator();
        }
        return Collections.EMPTY_LIST.iterator();
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        if (this.st != null) {
            this.st.close();
        }
    }
}
