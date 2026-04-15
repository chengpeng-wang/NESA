package org.json.simple.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONParser {
    public static final int S_END = 6;
    public static final int S_INIT = 0;
    public static final int S_IN_ARRAY = 3;
    public static final int S_IN_ERROR = -1;
    public static final int S_IN_FINISHED_VALUE = 1;
    public static final int S_IN_OBJECT = 2;
    public static final int S_IN_PAIR_VALUE = 5;
    public static final int S_PASSED_PAIR_KEY = 4;
    private LinkedList handlerStatusStack;
    private Yylex lexer = new Yylex((Reader) null);
    private int status = 0;
    private Yytoken token = null;

    private List createArrayContainer(ContainerFactory containerFactory) {
        if (containerFactory == null) {
            return new JSONArray();
        }
        List creatArrayContainer = containerFactory.creatArrayContainer();
        return creatArrayContainer == null ? new JSONArray() : creatArrayContainer;
    }

    private Map createObjectContainer(ContainerFactory containerFactory) {
        if (containerFactory == null) {
            return new JSONObject();
        }
        Map createObjectContainer = containerFactory.createObjectContainer();
        return createObjectContainer == null ? new JSONObject() : createObjectContainer;
    }

    private void nextToken() throws ParseException, IOException {
        this.token = this.lexer.yylex();
        if (this.token == null) {
            this.token = new Yytoken(-1, null);
        }
    }

    private int peekStatus(LinkedList linkedList) {
        return linkedList.size() == 0 ? -1 : ((Integer) linkedList.getFirst()).intValue();
    }

    public int getPosition() {
        return this.lexer.getPosition();
    }

    public Object parse(Reader reader) throws IOException, ParseException {
        return parse(reader, (ContainerFactory) null);
    }

    public Object parse(Reader reader, ContainerFactory containerFactory) throws IOException, ParseException {
        reset(reader);
        LinkedList linkedList = new LinkedList();
        LinkedList linkedList2 = new LinkedList();
        do {
            try {
                nextToken();
                Map createObjectContainer;
                switch (this.status) {
                    case -1:
                        throw new ParseException(getPosition(), 1, this.token);
                    case 0:
                        switch (this.token.type) {
                            case 0:
                                this.status = 1;
                                linkedList.addFirst(new Integer(this.status));
                                linkedList2.addFirst(this.token.value);
                                break;
                            case 1:
                                this.status = 2;
                                linkedList.addFirst(new Integer(this.status));
                                linkedList2.addFirst(createObjectContainer(containerFactory));
                                break;
                            case 3:
                                this.status = 3;
                                linkedList.addFirst(new Integer(this.status));
                                linkedList2.addFirst(createArrayContainer(containerFactory));
                                break;
                            default:
                                this.status = -1;
                                break;
                        }
                    case 1:
                        if (this.token.type == -1) {
                            return linkedList2.removeFirst();
                        }
                        throw new ParseException(getPosition(), 1, this.token);
                    case 2:
                        switch (this.token.type) {
                            case 0:
                                if (!(this.token.value instanceof String)) {
                                    this.status = -1;
                                    break;
                                }
                                linkedList2.addFirst((String) this.token.value);
                                this.status = 4;
                                linkedList.addFirst(new Integer(this.status));
                                break;
                            case 2:
                                if (linkedList2.size() <= 1) {
                                    this.status = 1;
                                    break;
                                }
                                linkedList.removeFirst();
                                linkedList2.removeFirst();
                                this.status = peekStatus(linkedList);
                                break;
                            case 5:
                                break;
                            default:
                                this.status = -1;
                                break;
                        }
                    case 3:
                        List list;
                        switch (this.token.type) {
                            case 0:
                                ((List) linkedList2.getFirst()).add(this.token.value);
                                break;
                            case 1:
                                list = (List) linkedList2.getFirst();
                                createObjectContainer = createObjectContainer(containerFactory);
                                list.add(createObjectContainer);
                                this.status = 2;
                                linkedList.addFirst(new Integer(this.status));
                                linkedList2.addFirst(createObjectContainer);
                                break;
                            case 3:
                                list = (List) linkedList2.getFirst();
                                List createArrayContainer = createArrayContainer(containerFactory);
                                list.add(createArrayContainer);
                                this.status = 3;
                                linkedList.addFirst(new Integer(this.status));
                                linkedList2.addFirst(createArrayContainer);
                                break;
                            case 4:
                                if (linkedList2.size() <= 1) {
                                    this.status = 1;
                                    break;
                                }
                                linkedList.removeFirst();
                                linkedList2.removeFirst();
                                this.status = peekStatus(linkedList);
                                break;
                            case 5:
                                break;
                            default:
                                this.status = -1;
                                break;
                        }
                    case 4:
                        String str;
                        switch (this.token.type) {
                            case 0:
                                linkedList.removeFirst();
                                ((Map) linkedList2.getFirst()).put((String) linkedList2.removeFirst(), this.token.value);
                                this.status = peekStatus(linkedList);
                                break;
                            case 1:
                                linkedList.removeFirst();
                                str = (String) linkedList2.removeFirst();
                                createObjectContainer = (Map) linkedList2.getFirst();
                                Map createObjectContainer2 = createObjectContainer(containerFactory);
                                createObjectContainer.put(str, createObjectContainer2);
                                this.status = 2;
                                linkedList.addFirst(new Integer(this.status));
                                linkedList2.addFirst(createObjectContainer2);
                                break;
                            case 3:
                                linkedList.removeFirst();
                                str = (String) linkedList2.removeFirst();
                                createObjectContainer = (Map) linkedList2.getFirst();
                                List createArrayContainer2 = createArrayContainer(containerFactory);
                                createObjectContainer.put(str, createArrayContainer2);
                                this.status = 3;
                                linkedList.addFirst(new Integer(this.status));
                                linkedList2.addFirst(createArrayContainer2);
                                break;
                            case 6:
                                break;
                            default:
                                this.status = -1;
                                break;
                        }
                }
                if (this.status == -1) {
                    throw new ParseException(getPosition(), 1, this.token);
                }
            } catch (IOException e) {
                throw e;
            }
        } while (this.token.type != -1);
        throw new ParseException(getPosition(), 1, this.token);
    }

    public Object parse(String str) throws ParseException {
        return parse(str, (ContainerFactory) null);
    }

    public Object parse(String str, ContainerFactory containerFactory) throws ParseException {
        try {
            return parse(new StringReader(str), containerFactory);
        } catch (IOException e) {
            throw new ParseException(-1, 2, e);
        }
    }

    public void parse(Reader reader, ContentHandler contentHandler) throws IOException, ParseException {
        parse(reader, contentHandler, false);
    }

    public void parse(Reader reader, ContentHandler contentHandler, boolean z) throws IOException, ParseException {
        if (!z) {
            reset(reader);
            this.handlerStatusStack = new LinkedList();
        } else if (this.handlerStatusStack == null) {
            reset(reader);
            this.handlerStatusStack = new LinkedList();
        }
        LinkedList linkedList = this.handlerStatusStack;
        do {
            try {
                switch (this.status) {
                    case -1:
                        throw new ParseException(getPosition(), 1, this.token);
                    case 0:
                        contentHandler.startJSON();
                        nextToken();
                        switch (this.token.type) {
                            case 0:
                                this.status = 1;
                                linkedList.addFirst(new Integer(this.status));
                                if (!contentHandler.primitive(this.token.value)) {
                                    return;
                                }
                                break;
                            case 1:
                                this.status = 2;
                                linkedList.addFirst(new Integer(this.status));
                                if (!contentHandler.startObject()) {
                                    return;
                                }
                                break;
                            case 3:
                                this.status = 3;
                                linkedList.addFirst(new Integer(this.status));
                                if (!contentHandler.startArray()) {
                                    return;
                                }
                                break;
                            default:
                                this.status = -1;
                                break;
                        }
                    case 1:
                        nextToken();
                        if (this.token.type == -1) {
                            contentHandler.endJSON();
                            this.status = 6;
                            return;
                        }
                        this.status = -1;
                        throw new ParseException(getPosition(), 1, this.token);
                    case 2:
                        nextToken();
                        switch (this.token.type) {
                            case 0:
                                if (!(this.token.value instanceof String)) {
                                    this.status = -1;
                                    break;
                                }
                                String str = (String) this.token.value;
                                this.status = 4;
                                linkedList.addFirst(new Integer(this.status));
                                if (!contentHandler.startObjectEntry(str)) {
                                    return;
                                }
                                break;
                            case 2:
                                if (linkedList.size() > 1) {
                                    linkedList.removeFirst();
                                    this.status = peekStatus(linkedList);
                                } else {
                                    this.status = 1;
                                }
                                if (!contentHandler.endObject()) {
                                    return;
                                }
                                break;
                            case 5:
                                break;
                            default:
                                this.status = -1;
                                break;
                        }
                    case 3:
                        nextToken();
                        switch (this.token.type) {
                            case 0:
                                if (!contentHandler.primitive(this.token.value)) {
                                    return;
                                }
                                break;
                            case 1:
                                this.status = 2;
                                linkedList.addFirst(new Integer(this.status));
                                if (!contentHandler.startObject()) {
                                    return;
                                }
                                break;
                            case 3:
                                this.status = 3;
                                linkedList.addFirst(new Integer(this.status));
                                if (!contentHandler.startArray()) {
                                    return;
                                }
                                break;
                            case 4:
                                if (linkedList.size() > 1) {
                                    linkedList.removeFirst();
                                    this.status = peekStatus(linkedList);
                                } else {
                                    this.status = 1;
                                }
                                if (!contentHandler.endArray()) {
                                    return;
                                }
                                break;
                            case 5:
                                break;
                            default:
                                this.status = -1;
                                break;
                        }
                    case 4:
                        nextToken();
                        switch (this.token.type) {
                            case 0:
                                linkedList.removeFirst();
                                this.status = peekStatus(linkedList);
                                if (!contentHandler.primitive(this.token.value)) {
                                    return;
                                }
                                if (!contentHandler.endObjectEntry()) {
                                    return;
                                }
                                break;
                            case 1:
                                linkedList.removeFirst();
                                linkedList.addFirst(new Integer(5));
                                this.status = 2;
                                linkedList.addFirst(new Integer(this.status));
                                if (!contentHandler.startObject()) {
                                    return;
                                }
                                break;
                            case 3:
                                linkedList.removeFirst();
                                linkedList.addFirst(new Integer(5));
                                this.status = 3;
                                linkedList.addFirst(new Integer(this.status));
                                if (!contentHandler.startArray()) {
                                    return;
                                }
                                break;
                            case 6:
                                break;
                            default:
                                this.status = -1;
                                break;
                        }
                    case 5:
                        linkedList.removeFirst();
                        this.status = peekStatus(linkedList);
                        if (!contentHandler.endObjectEntry()) {
                            return;
                        }
                        break;
                    case 6:
                        return;
                }
                if (this.status == -1) {
                    throw new ParseException(getPosition(), 1, this.token);
                }
            } catch (IOException e) {
                this.status = -1;
                throw e;
            } catch (ParseException e2) {
                this.status = -1;
                throw e2;
            } catch (RuntimeException e3) {
                this.status = -1;
                throw e3;
            } catch (Error e4) {
                this.status = -1;
                throw e4;
            }
        } while (this.token.type != -1);
        this.status = -1;
        throw new ParseException(getPosition(), 1, this.token);
    }

    public void parse(String str, ContentHandler contentHandler) throws ParseException {
        parse(str, contentHandler, false);
    }

    public void parse(String str, ContentHandler contentHandler, boolean z) throws ParseException {
        try {
            parse(new StringReader(str), contentHandler, z);
        } catch (IOException e) {
            throw new ParseException(-1, 2, e);
        }
    }

    public void reset() {
        this.token = null;
        this.status = 0;
        this.handlerStatusStack = null;
    }

    public void reset(Reader reader) {
        this.lexer.yyreset(reader);
        reset();
    }
}
