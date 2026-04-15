package org.jsoup.parser;

import java.util.Iterator;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document.QuirksMode;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;

enum HtmlTreeBuilderState {
    Initial {
        /* access modifiers changed from: 0000 */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (HtmlTreeBuilderState.isWhitespace(t)) {
                return true;
            }
            if (t.isComment()) {
                tb.insert(t.asComment());
                return true;
            } else if (t.isDoctype()) {
                Doctype d = t.asDoctype();
                tb.getDocument().appendChild(new DocumentType(d.getName(), d.getPublicIdentifier(), d.getSystemIdentifier(), tb.getBaseUri()));
                if (d.isForceQuirks()) {
                    tb.getDocument().quirksMode(QuirksMode.quirks);
                }
                tb.transition(BeforeHtml);
                return true;
            } else {
                tb.transition(BeforeHtml);
                return tb.process(t);
            }
        }
    },
    BeforeHtml {
        /* access modifiers changed from: 0000 */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (t.isDoctype()) {
                tb.error(this);
                return false;
            }
            if (t.isComment()) {
                tb.insert(t.asComment());
            } else if (HtmlTreeBuilderState.isWhitespace(t)) {
                return true;
            } else {
                if (t.isStartTag() && t.asStartTag().name().equals("html")) {
                    tb.insert(t.asStartTag());
                    tb.transition(BeforeHead);
                } else {
                    if (t.isEndTag()) {
                        if (StringUtil.in(t.asEndTag().name(), "head", "body", "html", "br")) {
                            return anythingElse(t, tb);
                        }
                    }
                    if (!t.isEndTag()) {
                        return anythingElse(t, tb);
                    }
                    tb.error(this);
                    return false;
                }
            }
            return true;
        }

        private boolean anythingElse(Token t, HtmlTreeBuilder tb) {
            tb.insert("html");
            tb.transition(BeforeHead);
            return tb.process(t);
        }
    },
    BeforeHead {
        /* access modifiers changed from: 0000 */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (HtmlTreeBuilderState.isWhitespace(t)) {
                return true;
            }
            if (t.isComment()) {
                tb.insert(t.asComment());
                return true;
            } else if (t.isDoctype()) {
                tb.error(this);
                return false;
            } else if (t.isStartTag() && t.asStartTag().name().equals("html")) {
                return InBody.process(t, tb);
            } else {
                if (t.isStartTag() && t.asStartTag().name().equals("head")) {
                    tb.setHeadElement(tb.insert(t.asStartTag()));
                    tb.transition(InHead);
                    return true;
                }
                if (t.isEndTag()) {
                    if (StringUtil.in(t.asEndTag().name(), "head", "body", "html", "br")) {
                        tb.process(new StartTag("head"));
                        return tb.process(t);
                    }
                }
                if (t.isEndTag()) {
                    tb.error(this);
                    return false;
                }
                tb.process(new StartTag("head"));
                return tb.process(t);
            }
        }
    },
    InHead {
        /* access modifiers changed from: 0000 */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (HtmlTreeBuilderState.isWhitespace(t)) {
                tb.insert(t.asCharacter());
                return true;
            }
            String name;
            switch (t.type) {
                case Comment:
                    tb.insert(t.asComment());
                    return true;
                case Doctype:
                    tb.error(this);
                    return false;
                case StartTag:
                    StartTag start = t.asStartTag();
                    name = start.name();
                    if (name.equals("html")) {
                        return InBody.process(t, tb);
                    }
                    if (StringUtil.in(name, "base", "basefont", "bgsound", "command", "link")) {
                        Element el = tb.insertEmpty(start);
                        if (!name.equals("base") || !el.hasAttr("href")) {
                            return true;
                        }
                        tb.maybeSetBaseUri(el);
                        return true;
                    } else if (name.equals("meta")) {
                        tb.insertEmpty(start);
                        return true;
                    } else if (name.equals("title")) {
                        HtmlTreeBuilderState.handleRcData(start, tb);
                        return true;
                    } else {
                        if (StringUtil.in(name, "noframes", "style")) {
                            HtmlTreeBuilderState.handleRawtext(start, tb);
                            return true;
                        } else if (name.equals("noscript")) {
                            tb.insert(start);
                            tb.transition(InHeadNoscript);
                            return true;
                        } else if (name.equals("script")) {
                            tb.insert(start);
                            tb.tokeniser.transition(TokeniserState.ScriptData);
                            tb.markInsertionMode();
                            tb.transition(Text);
                            return true;
                        } else if (!name.equals("head")) {
                            return anythingElse(t, tb);
                        } else {
                            tb.error(this);
                            return false;
                        }
                    }
                case EndTag:
                    name = t.asEndTag().name();
                    if (name.equals("head")) {
                        tb.pop();
                        tb.transition(AfterHead);
                        return true;
                    }
                    if (StringUtil.in(name, "body", "html", "br")) {
                        return anythingElse(t, tb);
                    }
                    tb.error(this);
                    return false;
                default:
                    return anythingElse(t, tb);
            }
        }

        private boolean anythingElse(Token t, TreeBuilder tb) {
            tb.process(new EndTag("head"));
            return tb.process(t);
        }
    },
    InHeadNoscript {
        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Missing block: B:21:0x0086, code skipped:
            if (org.jsoup.helper.StringUtil.in(r8.asStartTag().name(), "basefont", "bgsound", "link", "meta", "noframes", "style") != false) goto L_0x0088;
     */
        /* JADX WARNING: Missing block: B:31:0x00c8, code skipped:
            if (org.jsoup.helper.StringUtil.in(r8.asStartTag().name(), "head", "noscript") == false) goto L_0x00ca;
     */
        public boolean process(org.jsoup.parser.Token r8, org.jsoup.parser.HtmlTreeBuilder r9) {
            /*
            r7 = this;
            r6 = 2;
            r1 = 1;
            r0 = 0;
            r2 = r8.isDoctype();
            if (r2 == 0) goto L_0x000e;
        L_0x0009:
            r9.error(r7);
        L_0x000c:
            r0 = r1;
        L_0x000d:
            return r0;
        L_0x000e:
            r2 = r8.isStartTag();
            if (r2 == 0) goto L_0x002b;
        L_0x0014:
            r2 = r8.asStartTag();
            r2 = r2.name();
            r3 = "html";
            r2 = r2.equals(r3);
            if (r2 == 0) goto L_0x002b;
        L_0x0024:
            r0 = InBody;
            r0 = r9.process(r8, r0);
            goto L_0x000d;
        L_0x002b:
            r2 = r8.isEndTag();
            if (r2 == 0) goto L_0x004a;
        L_0x0031:
            r2 = r8.asEndTag();
            r2 = r2.name();
            r3 = "noscript";
            r2 = r2.equals(r3);
            if (r2 == 0) goto L_0x004a;
        L_0x0041:
            r9.pop();
            r0 = InHead;
            r9.transition(r0);
            goto L_0x000c;
        L_0x004a:
            r2 = org.jsoup.parser.HtmlTreeBuilderState.isWhitespace(r8);
            if (r2 != 0) goto L_0x0088;
        L_0x0050:
            r2 = r8.isComment();
            if (r2 != 0) goto L_0x0088;
        L_0x0056:
            r2 = r8.isStartTag();
            if (r2 == 0) goto L_0x0090;
        L_0x005c:
            r2 = r8.asStartTag();
            r2 = r2.name();
            r3 = 6;
            r3 = new java.lang.String[r3];
            r4 = "basefont";
            r3[r0] = r4;
            r4 = "bgsound";
            r3[r1] = r4;
            r4 = "link";
            r3[r6] = r4;
            r4 = 3;
            r5 = "meta";
            r3[r4] = r5;
            r4 = 4;
            r5 = "noframes";
            r3[r4] = r5;
            r4 = 5;
            r5 = "style";
            r3[r4] = r5;
            r2 = org.jsoup.helper.StringUtil.in(r2, r3);
            if (r2 == 0) goto L_0x0090;
        L_0x0088:
            r0 = InHead;
            r0 = r9.process(r8, r0);
            goto L_0x000d;
        L_0x0090:
            r2 = r8.isEndTag();
            if (r2 == 0) goto L_0x00ac;
        L_0x0096:
            r2 = r8.asEndTag();
            r2 = r2.name();
            r3 = "br";
            r2 = r2.equals(r3);
            if (r2 == 0) goto L_0x00ac;
        L_0x00a6:
            r0 = r7.anythingElse(r8, r9);
            goto L_0x000d;
        L_0x00ac:
            r2 = r8.isStartTag();
            if (r2 == 0) goto L_0x00ca;
        L_0x00b2:
            r2 = r8.asStartTag();
            r2 = r2.name();
            r3 = new java.lang.String[r6];
            r4 = "head";
            r3[r0] = r4;
            r4 = "noscript";
            r3[r1] = r4;
            r1 = org.jsoup.helper.StringUtil.in(r2, r3);
            if (r1 != 0) goto L_0x00d0;
        L_0x00ca:
            r1 = r8.isEndTag();
            if (r1 == 0) goto L_0x00d5;
        L_0x00d0:
            r9.error(r7);
            goto L_0x000d;
        L_0x00d5:
            r0 = r7.anythingElse(r8, r9);
            goto L_0x000d;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.jsoup.parser.HtmlTreeBuilderState$AnonymousClass5.process(org.jsoup.parser.Token, org.jsoup.parser.HtmlTreeBuilder):boolean");
        }

        private boolean anythingElse(Token t, HtmlTreeBuilder tb) {
            tb.error(this);
            tb.process(new EndTag("noscript"));
            return tb.process(t);
        }
    },
    AfterHead {
        /* access modifiers changed from: 0000 */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (HtmlTreeBuilderState.isWhitespace(t)) {
                tb.insert(t.asCharacter());
            } else if (t.isComment()) {
                tb.insert(t.asComment());
            } else if (t.isDoctype()) {
                tb.error(this);
            } else if (t.isStartTag()) {
                StartTag startTag = t.asStartTag();
                String name = startTag.name();
                if (name.equals("html")) {
                    return tb.process(t, InBody);
                }
                if (name.equals("body")) {
                    tb.insert(startTag);
                    tb.framesetOk(false);
                    tb.transition(InBody);
                } else if (name.equals("frameset")) {
                    tb.insert(startTag);
                    tb.transition(InFrameset);
                } else {
                    if (StringUtil.in(name, "base", "basefont", "bgsound", "link", "meta", "noframes", "script", "style", "title")) {
                        tb.error(this);
                        Element head = tb.getHeadElement();
                        tb.push(head);
                        tb.process(t, InHead);
                        tb.removeFromStack(head);
                    } else if (name.equals("head")) {
                        tb.error(this);
                        return false;
                    } else {
                        anythingElse(t, tb);
                    }
                }
            } else if (t.isEndTag()) {
                if (StringUtil.in(t.asEndTag().name(), "body", "html")) {
                    anythingElse(t, tb);
                } else {
                    tb.error(this);
                    return false;
                }
            } else {
                anythingElse(t, tb);
            }
            return true;
        }

        private boolean anythingElse(Token t, HtmlTreeBuilder tb) {
            tb.process(new StartTag("body"));
            tb.framesetOk(true);
            return tb.process(t);
        }
    },
    InBody {
        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Removed duplicated region for block: B:96:0x0463  */
        /* JADX WARNING: Removed duplicated region for block: B:112:0x0520  */
        /* JADX WARNING: Removed duplicated region for block: B:381:0x1182  */
        /* JADX WARNING: Removed duplicated region for block: B:367:0x10f4  */
        /* JADX WARNING: Removed duplicated region for block: B:374:0x1135 A:{LOOP_END, LOOP:9: B:372:0x112f->B:374:0x1135} */
        public boolean process(org.jsoup.parser.Token r43, org.jsoup.parser.HtmlTreeBuilder r44) {
            /*
            r42 = this;
            r38 = org.jsoup.parser.HtmlTreeBuilderState.AnonymousClass24.$SwitchMap$org$jsoup$parser$Token$TokenType;
            r0 = r43;
            r0 = r0.type;
            r39 = r0;
            r39 = r39.ordinal();
            r38 = r38[r39];
            switch(r38) {
                case 1: goto L_0x0051;
                case 2: goto L_0x005d;
                case 3: goto L_0x0067;
                case 4: goto L_0x0bf8;
                case 5: goto L_0x0014;
                default: goto L_0x0011;
            };
        L_0x0011:
            r38 = 1;
        L_0x0013:
            return r38;
        L_0x0014:
            r9 = r43.asCharacter();
            r38 = r9.getData();
            r39 = org.jsoup.parser.HtmlTreeBuilderState.nullString;
            r38 = r38.equals(r39);
            if (r38 == 0) goto L_0x0030;
        L_0x0026:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
            r38 = 0;
            goto L_0x0013;
        L_0x0030:
            r38 = org.jsoup.parser.HtmlTreeBuilderState.isWhitespace(r9);
            if (r38 == 0) goto L_0x003f;
        L_0x0036:
            r44.reconstructFormattingElements();
            r0 = r44;
            r0.insert(r9);
            goto L_0x0011;
        L_0x003f:
            r44.reconstructFormattingElements();
            r0 = r44;
            r0.insert(r9);
            r38 = 0;
            r0 = r44;
            r1 = r38;
            r0.framesetOk(r1);
            goto L_0x0011;
        L_0x0051:
            r38 = r43.asComment();
            r0 = r44;
            r1 = r38;
            r0.insert(r1);
            goto L_0x0011;
        L_0x005d:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
            r38 = 0;
            goto L_0x0013;
        L_0x0067:
            r36 = r43.asStartTag();
            r26 = r36.name();
            r38 = "html";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x00b8;
        L_0x007b:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
            r38 = r44.getStack();
            r19 = r38.getFirst();
            r19 = (org.jsoup.nodes.Element) r19;
            r38 = r36.getAttributes();
            r21 = r38.iterator();
        L_0x0094:
            r38 = r21.hasNext();
            if (r38 == 0) goto L_0x0011;
        L_0x009a:
            r7 = r21.next();
            r7 = (org.jsoup.nodes.Attribute) r7;
            r38 = r7.getKey();
            r0 = r19;
            r1 = r38;
            r38 = r0.hasAttr(r1);
            if (r38 != 0) goto L_0x0094;
        L_0x00ae:
            r38 = r19.attributes();
            r0 = r38;
            r0.put(r7);
            goto L_0x0094;
        L_0x00b8:
            r38 = 10;
            r0 = r38;
            r0 = new java.lang.String[r0];
            r38 = r0;
            r39 = 0;
            r40 = "base";
            r38[r39] = r40;
            r39 = 1;
            r40 = "basefont";
            r38[r39] = r40;
            r39 = 2;
            r40 = "bgsound";
            r38[r39] = r40;
            r39 = 3;
            r40 = "command";
            r38[r39] = r40;
            r39 = 4;
            r40 = "link";
            r38[r39] = r40;
            r39 = 5;
            r40 = "meta";
            r38[r39] = r40;
            r39 = 6;
            r40 = "noframes";
            r38[r39] = r40;
            r39 = 7;
            r40 = "script";
            r38[r39] = r40;
            r39 = 8;
            r40 = "style";
            r38[r39] = r40;
            r39 = 9;
            r40 = "title";
            r38[r39] = r40;
            r0 = r26;
            r1 = r38;
            r38 = org.jsoup.helper.StringUtil.in(r0, r1);
            if (r38 == 0) goto L_0x0114;
        L_0x0106:
            r38 = InHead;
            r0 = r44;
            r1 = r43;
            r2 = r38;
            r38 = r0.process(r1, r2);
            goto L_0x0013;
        L_0x0114:
            r38 = "body";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x019e;
        L_0x0120:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
            r35 = r44.getStack();
            r38 = r35.size();
            r39 = 1;
            r0 = r38;
            r1 = r39;
            if (r0 == r1) goto L_0x015b;
        L_0x0137:
            r38 = r35.size();
            r39 = 2;
            r0 = r38;
            r1 = r39;
            if (r0 <= r1) goto L_0x015f;
        L_0x0143:
            r38 = 1;
            r0 = r35;
            r1 = r38;
            r38 = r0.get(r1);
            r38 = (org.jsoup.nodes.Element) r38;
            r38 = r38.nodeName();
            r39 = "body";
            r38 = r38.equals(r39);
            if (r38 != 0) goto L_0x015f;
        L_0x015b:
            r38 = 0;
            goto L_0x0013;
        L_0x015f:
            r38 = 0;
            r0 = r44;
            r1 = r38;
            r0.framesetOk(r1);
            r38 = 1;
            r0 = r35;
            r1 = r38;
            r8 = r0.get(r1);
            r8 = (org.jsoup.nodes.Element) r8;
            r38 = r36.getAttributes();
            r21 = r38.iterator();
        L_0x017c:
            r38 = r21.hasNext();
            if (r38 == 0) goto L_0x0011;
        L_0x0182:
            r7 = r21.next();
            r7 = (org.jsoup.nodes.Attribute) r7;
            r38 = r7.getKey();
            r0 = r38;
            r38 = r8.hasAttr(r0);
            if (r38 != 0) goto L_0x017c;
        L_0x0194:
            r38 = r8.attributes();
            r0 = r38;
            r0.put(r7);
            goto L_0x017c;
        L_0x019e:
            r38 = "frameset";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x022a;
        L_0x01aa:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
            r35 = r44.getStack();
            r38 = r35.size();
            r39 = 1;
            r0 = r38;
            r1 = r39;
            if (r0 == r1) goto L_0x01e5;
        L_0x01c1:
            r38 = r35.size();
            r39 = 2;
            r0 = r38;
            r1 = r39;
            if (r0 <= r1) goto L_0x01e9;
        L_0x01cd:
            r38 = 1;
            r0 = r35;
            r1 = r38;
            r38 = r0.get(r1);
            r38 = (org.jsoup.nodes.Element) r38;
            r38 = r38.nodeName();
            r39 = "body";
            r38 = r38.equals(r39);
            if (r38 != 0) goto L_0x01e9;
        L_0x01e5:
            r38 = 0;
            goto L_0x0013;
        L_0x01e9:
            r38 = r44.framesetOk();
            if (r38 != 0) goto L_0x01f3;
        L_0x01ef:
            r38 = 0;
            goto L_0x0013;
        L_0x01f3:
            r38 = 1;
            r0 = r35;
            r1 = r38;
            r32 = r0.get(r1);
            r32 = (org.jsoup.nodes.Element) r32;
            r38 = r32.parent();
            if (r38 == 0) goto L_0x0208;
        L_0x0205:
            r32.remove();
        L_0x0208:
            r38 = r35.size();
            r39 = 1;
            r0 = r38;
            r1 = r39;
            if (r0 <= r1) goto L_0x0218;
        L_0x0214:
            r35.removeLast();
            goto L_0x0208;
        L_0x0218:
            r0 = r44;
            r1 = r36;
            r0.insert(r1);
            r38 = InFrameset;
            r0 = r44;
            r1 = r38;
            r0.transition(r1);
            goto L_0x0011;
        L_0x022a:
            r38 = 22;
            r0 = r38;
            r0 = new java.lang.String[r0];
            r38 = r0;
            r39 = 0;
            r40 = "address";
            r38[r39] = r40;
            r39 = 1;
            r40 = "article";
            r38[r39] = r40;
            r39 = 2;
            r40 = "aside";
            r38[r39] = r40;
            r39 = 3;
            r40 = "blockquote";
            r38[r39] = r40;
            r39 = 4;
            r40 = "center";
            r38[r39] = r40;
            r39 = 5;
            r40 = "details";
            r38[r39] = r40;
            r39 = 6;
            r40 = "dir";
            r38[r39] = r40;
            r39 = 7;
            r40 = "div";
            r38[r39] = r40;
            r39 = 8;
            r40 = "dl";
            r38[r39] = r40;
            r39 = 9;
            r40 = "fieldset";
            r38[r39] = r40;
            r39 = 10;
            r40 = "figcaption";
            r38[r39] = r40;
            r39 = 11;
            r40 = "figure";
            r38[r39] = r40;
            r39 = 12;
            r40 = "footer";
            r38[r39] = r40;
            r39 = 13;
            r40 = "header";
            r38[r39] = r40;
            r39 = 14;
            r40 = "hgroup";
            r38[r39] = r40;
            r39 = 15;
            r40 = "menu";
            r38[r39] = r40;
            r39 = 16;
            r40 = "nav";
            r38[r39] = r40;
            r39 = 17;
            r40 = "ol";
            r38[r39] = r40;
            r39 = 18;
            r40 = "p";
            r38[r39] = r40;
            r39 = 19;
            r40 = "section";
            r38[r39] = r40;
            r39 = 20;
            r40 = "summary";
            r38[r39] = r40;
            r39 = 21;
            r40 = "ul";
            r38[r39] = r40;
            r0 = r26;
            r1 = r38;
            r38 = org.jsoup.helper.StringUtil.in(r0, r1);
            if (r38 == 0) goto L_0x02e3;
        L_0x02c0:
            r38 = "p";
            r0 = r44;
            r1 = r38;
            r38 = r0.inButtonScope(r1);
            if (r38 == 0) goto L_0x02da;
        L_0x02cc:
            r38 = new org.jsoup.parser.Token$EndTag;
            r39 = "p";
            r38.m250init(r39);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
        L_0x02da:
            r0 = r44;
            r1 = r36;
            r0.insert(r1);
            goto L_0x0011;
        L_0x02e3:
            r38 = 6;
            r0 = r38;
            r0 = new java.lang.String[r0];
            r38 = r0;
            r39 = 0;
            r40 = "h1";
            r38[r39] = r40;
            r39 = 1;
            r40 = "h2";
            r38[r39] = r40;
            r39 = 2;
            r40 = "h3";
            r38[r39] = r40;
            r39 = 3;
            r40 = "h4";
            r38[r39] = r40;
            r39 = 4;
            r40 = "h5";
            r38[r39] = r40;
            r39 = 5;
            r40 = "h6";
            r38[r39] = r40;
            r0 = r26;
            r1 = r38;
            r38 = org.jsoup.helper.StringUtil.in(r0, r1);
            if (r38 == 0) goto L_0x0380;
        L_0x0319:
            r38 = "p";
            r0 = r44;
            r1 = r38;
            r38 = r0.inButtonScope(r1);
            if (r38 == 0) goto L_0x0333;
        L_0x0325:
            r38 = new org.jsoup.parser.Token$EndTag;
            r39 = "p";
            r38.m250init(r39);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
        L_0x0333:
            r38 = r44.currentElement();
            r38 = r38.nodeName();
            r39 = 6;
            r0 = r39;
            r0 = new java.lang.String[r0];
            r39 = r0;
            r40 = 0;
            r41 = "h1";
            r39[r40] = r41;
            r40 = 1;
            r41 = "h2";
            r39[r40] = r41;
            r40 = 2;
            r41 = "h3";
            r39[r40] = r41;
            r40 = 3;
            r41 = "h4";
            r39[r40] = r41;
            r40 = 4;
            r41 = "h5";
            r39[r40] = r41;
            r40 = 5;
            r41 = "h6";
            r39[r40] = r41;
            r38 = org.jsoup.helper.StringUtil.in(r38, r39);
            if (r38 == 0) goto L_0x0377;
        L_0x036d:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
            r44.pop();
        L_0x0377:
            r0 = r44;
            r1 = r36;
            r0.insert(r1);
            goto L_0x0011;
        L_0x0380:
            r38 = 2;
            r0 = r38;
            r0 = new java.lang.String[r0];
            r38 = r0;
            r39 = 0;
            r40 = "pre";
            r38[r39] = r40;
            r39 = 1;
            r40 = "listing";
            r38[r39] = r40;
            r0 = r26;
            r1 = r38;
            r38 = org.jsoup.helper.StringUtil.in(r0, r1);
            if (r38 == 0) goto L_0x03ca;
        L_0x039e:
            r38 = "p";
            r0 = r44;
            r1 = r38;
            r38 = r0.inButtonScope(r1);
            if (r38 == 0) goto L_0x03b8;
        L_0x03aa:
            r38 = new org.jsoup.parser.Token$EndTag;
            r39 = "p";
            r38.m250init(r39);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
        L_0x03b8:
            r0 = r44;
            r1 = r36;
            r0.insert(r1);
            r38 = 0;
            r0 = r44;
            r1 = r38;
            r0.framesetOk(r1);
            goto L_0x0011;
        L_0x03ca:
            r38 = "form";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x0412;
        L_0x03d6:
            r38 = r44.getFormElement();
            if (r38 == 0) goto L_0x03e7;
        L_0x03dc:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
            r38 = 0;
            goto L_0x0013;
        L_0x03e7:
            r38 = "p";
            r0 = r44;
            r1 = r38;
            r38 = r0.inButtonScope(r1);
            if (r38 == 0) goto L_0x0401;
        L_0x03f3:
            r38 = new org.jsoup.parser.Token$EndTag;
            r39 = "p";
            r38.m250init(r39);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
        L_0x0401:
            r0 = r44;
            r1 = r36;
            r16 = r0.insert(r1);
            r0 = r44;
            r1 = r16;
            r0.setFormElement(r1);
            goto L_0x0011;
        L_0x0412:
            r38 = "li";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x04a9;
        L_0x041e:
            r38 = 0;
            r0 = r44;
            r1 = r38;
            r0.framesetOk(r1);
            r35 = r44.getStack();
            r38 = r35.size();
            r20 = r38 + -1;
        L_0x0431:
            if (r20 <= 0) goto L_0x0457;
        L_0x0433:
            r0 = r35;
            r1 = r20;
            r14 = r0.get(r1);
            r14 = (org.jsoup.nodes.Element) r14;
            r38 = r14.nodeName();
            r39 = "li";
            r38 = r38.equals(r39);
            if (r38 == 0) goto L_0x047a;
        L_0x0449:
            r38 = new org.jsoup.parser.Token$EndTag;
            r39 = "li";
            r38.m250init(r39);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
        L_0x0457:
            r38 = "p";
            r0 = r44;
            r1 = r38;
            r38 = r0.inButtonScope(r1);
            if (r38 == 0) goto L_0x0471;
        L_0x0463:
            r38 = new org.jsoup.parser.Token$EndTag;
            r39 = "p";
            r38.m250init(r39);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
        L_0x0471:
            r0 = r44;
            r1 = r36;
            r0.insert(r1);
            goto L_0x0011;
        L_0x047a:
            r0 = r44;
            r38 = r0.isSpecial(r14);
            if (r38 == 0) goto L_0x04a6;
        L_0x0482:
            r38 = r14.nodeName();
            r39 = 3;
            r0 = r39;
            r0 = new java.lang.String[r0];
            r39 = r0;
            r40 = 0;
            r41 = "address";
            r39[r40] = r41;
            r40 = 1;
            r41 = "div";
            r39[r40] = r41;
            r40 = 2;
            r41 = "p";
            r39[r40] = r41;
            r38 = org.jsoup.helper.StringUtil.in(r38, r39);
            if (r38 == 0) goto L_0x0457;
        L_0x04a6:
            r20 = r20 + -1;
            goto L_0x0431;
        L_0x04a9:
            r38 = 2;
            r0 = r38;
            r0 = new java.lang.String[r0];
            r38 = r0;
            r39 = 0;
            r40 = "dd";
            r38[r39] = r40;
            r39 = 1;
            r40 = "dt";
            r38[r39] = r40;
            r0 = r26;
            r1 = r38;
            r38 = org.jsoup.helper.StringUtil.in(r0, r1);
            if (r38 == 0) goto L_0x0567;
        L_0x04c7:
            r38 = 0;
            r0 = r44;
            r1 = r38;
            r0.framesetOk(r1);
            r35 = r44.getStack();
            r38 = r35.size();
            r20 = r38 + -1;
        L_0x04da:
            if (r20 <= 0) goto L_0x0514;
        L_0x04dc:
            r0 = r35;
            r1 = r20;
            r14 = r0.get(r1);
            r14 = (org.jsoup.nodes.Element) r14;
            r38 = r14.nodeName();
            r39 = 2;
            r0 = r39;
            r0 = new java.lang.String[r0];
            r39 = r0;
            r40 = 0;
            r41 = "dd";
            r39[r40] = r41;
            r40 = 1;
            r41 = "dt";
            r39[r40] = r41;
            r38 = org.jsoup.helper.StringUtil.in(r38, r39);
            if (r38 == 0) goto L_0x0537;
        L_0x0504:
            r38 = new org.jsoup.parser.Token$EndTag;
            r39 = r14.nodeName();
            r38.m250init(r39);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
        L_0x0514:
            r38 = "p";
            r0 = r44;
            r1 = r38;
            r38 = r0.inButtonScope(r1);
            if (r38 == 0) goto L_0x052e;
        L_0x0520:
            r38 = new org.jsoup.parser.Token$EndTag;
            r39 = "p";
            r38.m250init(r39);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
        L_0x052e:
            r0 = r44;
            r1 = r36;
            r0.insert(r1);
            goto L_0x0011;
        L_0x0537:
            r0 = r44;
            r38 = r0.isSpecial(r14);
            if (r38 == 0) goto L_0x0563;
        L_0x053f:
            r38 = r14.nodeName();
            r39 = 3;
            r0 = r39;
            r0 = new java.lang.String[r0];
            r39 = r0;
            r40 = 0;
            r41 = "address";
            r39[r40] = r41;
            r40 = 1;
            r41 = "div";
            r39[r40] = r41;
            r40 = 2;
            r41 = "p";
            r39[r40] = r41;
            r38 = org.jsoup.helper.StringUtil.in(r38, r39);
            if (r38 == 0) goto L_0x0514;
        L_0x0563:
            r20 = r20 + -1;
            goto L_0x04da;
        L_0x0567:
            r38 = "plaintext";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x05a1;
        L_0x0573:
            r38 = "p";
            r0 = r44;
            r1 = r38;
            r38 = r0.inButtonScope(r1);
            if (r38 == 0) goto L_0x058d;
        L_0x057f:
            r38 = new org.jsoup.parser.Token$EndTag;
            r39 = "p";
            r38.m250init(r39);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
        L_0x058d:
            r0 = r44;
            r1 = r36;
            r0.insert(r1);
            r0 = r44;
            r0 = r0.tokeniser;
            r38 = r0;
            r39 = org.jsoup.parser.TokeniserState.PLAINTEXT;
            r38.transition(r39);
            goto L_0x0011;
        L_0x05a1:
            r38 = "button";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x05ec;
        L_0x05ad:
            r38 = "button";
            r0 = r44;
            r1 = r38;
            r38 = r0.inButtonScope(r1);
            if (r38 == 0) goto L_0x05d7;
        L_0x05b9:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
            r38 = new org.jsoup.parser.Token$EndTag;
            r39 = "button";
            r38.m250init(r39);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
            r0 = r44;
            r1 = r36;
            r0.process(r1);
            goto L_0x0011;
        L_0x05d7:
            r44.reconstructFormattingElements();
            r0 = r44;
            r1 = r36;
            r0.insert(r1);
            r38 = 0;
            r0 = r44;
            r1 = r38;
            r0.framesetOk(r1);
            goto L_0x0011;
        L_0x05ec:
            r38 = "a";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x0645;
        L_0x05f8:
            r38 = "a";
            r0 = r44;
            r1 = r38;
            r38 = r0.getActiveFormattingElement(r1);
            if (r38 == 0) goto L_0x0633;
        L_0x0604:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
            r38 = new org.jsoup.parser.Token$EndTag;
            r39 = "a";
            r38.m250init(r39);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
            r38 = "a";
            r0 = r44;
            r1 = r38;
            r30 = r0.getFromStack(r1);
            if (r30 == 0) goto L_0x0633;
        L_0x0625:
            r0 = r44;
            r1 = r30;
            r0.removeFromActiveFormattingElements(r1);
            r0 = r44;
            r1 = r30;
            r0.removeFromStack(r1);
        L_0x0633:
            r44.reconstructFormattingElements();
            r0 = r44;
            r1 = r36;
            r3 = r0.insert(r1);
            r0 = r44;
            r0.pushActiveFormattingElements(r3);
            goto L_0x0011;
        L_0x0645:
            r38 = 12;
            r0 = r38;
            r0 = new java.lang.String[r0];
            r38 = r0;
            r39 = 0;
            r40 = "b";
            r38[r39] = r40;
            r39 = 1;
            r40 = "big";
            r38[r39] = r40;
            r39 = 2;
            r40 = "code";
            r38[r39] = r40;
            r39 = 3;
            r40 = "em";
            r38[r39] = r40;
            r39 = 4;
            r40 = "font";
            r38[r39] = r40;
            r39 = 5;
            r40 = "i";
            r38[r39] = r40;
            r39 = 6;
            r40 = "s";
            r38[r39] = r40;
            r39 = 7;
            r40 = "small";
            r38[r39] = r40;
            r39 = 8;
            r40 = "strike";
            r38[r39] = r40;
            r39 = 9;
            r40 = "strong";
            r38[r39] = r40;
            r39 = 10;
            r40 = "tt";
            r38[r39] = r40;
            r39 = 11;
            r40 = "u";
            r38[r39] = r40;
            r0 = r26;
            r1 = r38;
            r38 = org.jsoup.helper.StringUtil.in(r0, r1);
            if (r38 == 0) goto L_0x06b1;
        L_0x069f:
            r44.reconstructFormattingElements();
            r0 = r44;
            r1 = r36;
            r14 = r0.insert(r1);
            r0 = r44;
            r0.pushActiveFormattingElements(r14);
            goto L_0x0011;
        L_0x06b1:
            r38 = "nobr";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x06f3;
        L_0x06bd:
            r44.reconstructFormattingElements();
            r38 = "nobr";
            r0 = r44;
            r1 = r38;
            r38 = r0.inScope(r1);
            if (r38 == 0) goto L_0x06e4;
        L_0x06cc:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
            r38 = new org.jsoup.parser.Token$EndTag;
            r39 = "nobr";
            r38.m250init(r39);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
            r44.reconstructFormattingElements();
        L_0x06e4:
            r0 = r44;
            r1 = r36;
            r14 = r0.insert(r1);
            r0 = r44;
            r0.pushActiveFormattingElements(r14);
            goto L_0x0011;
        L_0x06f3:
            r38 = 3;
            r0 = r38;
            r0 = new java.lang.String[r0];
            r38 = r0;
            r39 = 0;
            r40 = "applet";
            r38[r39] = r40;
            r39 = 1;
            r40 = "marquee";
            r38[r39] = r40;
            r39 = 2;
            r40 = "object";
            r38[r39] = r40;
            r0 = r26;
            r1 = r38;
            r38 = org.jsoup.helper.StringUtil.in(r0, r1);
            if (r38 == 0) goto L_0x072f;
        L_0x0717:
            r44.reconstructFormattingElements();
            r0 = r44;
            r1 = r36;
            r0.insert(r1);
            r44.insertMarkerToFormattingElements();
            r38 = 0;
            r0 = r44;
            r1 = r38;
            r0.framesetOk(r1);
            goto L_0x0011;
        L_0x072f:
            r38 = "table";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x0780;
        L_0x073b:
            r38 = r44.getDocument();
            r38 = r38.quirksMode();
            r39 = org.jsoup.nodes.Document.QuirksMode.quirks;
            r0 = r38;
            r1 = r39;
            if (r0 == r1) goto L_0x0765;
        L_0x074b:
            r38 = "p";
            r0 = r44;
            r1 = r38;
            r38 = r0.inButtonScope(r1);
            if (r38 == 0) goto L_0x0765;
        L_0x0757:
            r38 = new org.jsoup.parser.Token$EndTag;
            r39 = "p";
            r38.m250init(r39);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
        L_0x0765:
            r0 = r44;
            r1 = r36;
            r0.insert(r1);
            r38 = 0;
            r0 = r44;
            r1 = r38;
            r0.framesetOk(r1);
            r38 = InTable;
            r0 = r44;
            r1 = r38;
            r0.transition(r1);
            goto L_0x0011;
        L_0x0780:
            r38 = 6;
            r0 = r38;
            r0 = new java.lang.String[r0];
            r38 = r0;
            r39 = 0;
            r40 = "area";
            r38[r39] = r40;
            r39 = 1;
            r40 = "br";
            r38[r39] = r40;
            r39 = 2;
            r40 = "embed";
            r38[r39] = r40;
            r39 = 3;
            r40 = "img";
            r38[r39] = r40;
            r39 = 4;
            r40 = "keygen";
            r38[r39] = r40;
            r39 = 5;
            r40 = "wbr";
            r38[r39] = r40;
            r0 = r26;
            r1 = r38;
            r38 = org.jsoup.helper.StringUtil.in(r0, r1);
            if (r38 == 0) goto L_0x07cb;
        L_0x07b6:
            r44.reconstructFormattingElements();
            r0 = r44;
            r1 = r36;
            r0.insertEmpty(r1);
            r38 = 0;
            r0 = r44;
            r1 = r38;
            r0.framesetOk(r1);
            goto L_0x0011;
        L_0x07cb:
            r38 = "input";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x07fd;
        L_0x07d7:
            r44.reconstructFormattingElements();
            r0 = r44;
            r1 = r36;
            r14 = r0.insertEmpty(r1);
            r38 = "type";
            r0 = r38;
            r38 = r14.attr(r0);
            r39 = "hidden";
            r38 = r38.equalsIgnoreCase(r39);
            if (r38 != 0) goto L_0x0011;
        L_0x07f2:
            r38 = 0;
            r0 = r44;
            r1 = r38;
            r0.framesetOk(r1);
            goto L_0x0011;
        L_0x07fd:
            r38 = 3;
            r0 = r38;
            r0 = new java.lang.String[r0];
            r38 = r0;
            r39 = 0;
            r40 = "param";
            r38[r39] = r40;
            r39 = 1;
            r40 = "source";
            r38[r39] = r40;
            r39 = 2;
            r40 = "track";
            r38[r39] = r40;
            r0 = r26;
            r1 = r38;
            r38 = org.jsoup.helper.StringUtil.in(r0, r1);
            if (r38 == 0) goto L_0x082a;
        L_0x0821:
            r0 = r44;
            r1 = r36;
            r0.insertEmpty(r1);
            goto L_0x0011;
        L_0x082a:
            r38 = "hr";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x0862;
        L_0x0836:
            r38 = "p";
            r0 = r44;
            r1 = r38;
            r38 = r0.inButtonScope(r1);
            if (r38 == 0) goto L_0x0850;
        L_0x0842:
            r38 = new org.jsoup.parser.Token$EndTag;
            r39 = "p";
            r38.m250init(r39);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
        L_0x0850:
            r0 = r44;
            r1 = r36;
            r0.insertEmpty(r1);
            r38 = 0;
            r0 = r44;
            r1 = r38;
            r0.framesetOk(r1);
            goto L_0x0011;
        L_0x0862:
            r38 = "image";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x0881;
        L_0x086e:
            r38 = "img";
            r0 = r36;
            r1 = r38;
            r0.name(r1);
            r0 = r44;
            r1 = r36;
            r38 = r0.process(r1);
            goto L_0x0013;
        L_0x0881:
            r38 = "isindex";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x09b9;
        L_0x088d:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
            r38 = r44.getFormElement();
            if (r38 == 0) goto L_0x089e;
        L_0x089a:
            r38 = 0;
            goto L_0x0013;
        L_0x089e:
            r0 = r44;
            r0 = r0.tokeniser;
            r38 = r0;
            r38.acknowledgeSelfClosingFlag();
            r38 = new org.jsoup.parser.Token$StartTag;
            r39 = "form";
            r38.m252init(r39);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
            r0 = r36;
            r0 = r0.attributes;
            r38 = r0;
            r39 = "action";
            r38 = r38.hasKey(r39);
            if (r38 == 0) goto L_0x08de;
        L_0x08c3:
            r16 = r44.getFormElement();
            r38 = "action";
            r0 = r36;
            r0 = r0.attributes;
            r39 = r0;
            r40 = "action";
            r39 = r39.get(r40);
            r0 = r16;
            r1 = r38;
            r2 = r39;
            r0.attr(r1, r2);
        L_0x08de:
            r38 = new org.jsoup.parser.Token$StartTag;
            r39 = "hr";
            r38.m252init(r39);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
            r38 = new org.jsoup.parser.Token$StartTag;
            r39 = "label";
            r38.m252init(r39);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
            r0 = r36;
            r0 = r0.attributes;
            r38 = r0;
            r39 = "prompt";
            r38 = r38.hasKey(r39);
            if (r38 == 0) goto L_0x0969;
        L_0x0908:
            r0 = r36;
            r0 = r0.attributes;
            r38 = r0;
            r39 = "prompt";
            r29 = r38.get(r39);
        L_0x0914:
            r38 = new org.jsoup.parser.Token$Character;
            r0 = r38;
            r1 = r29;
            r0.m139init(r1);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
            r22 = new org.jsoup.nodes.Attributes;
            r22.m48init();
            r0 = r36;
            r0 = r0.attributes;
            r38 = r0;
            r21 = r38.iterator();
        L_0x0933:
            r38 = r21.hasNext();
            if (r38 == 0) goto L_0x096c;
        L_0x0939:
            r6 = r21.next();
            r6 = (org.jsoup.nodes.Attribute) r6;
            r38 = r6.getKey();
            r39 = 3;
            r0 = r39;
            r0 = new java.lang.String[r0];
            r39 = r0;
            r40 = 0;
            r41 = "name";
            r39[r40] = r41;
            r40 = 1;
            r41 = "action";
            r39[r40] = r41;
            r40 = 2;
            r41 = "prompt";
            r39[r40] = r41;
            r38 = org.jsoup.helper.StringUtil.in(r38, r39);
            if (r38 != 0) goto L_0x0933;
        L_0x0963:
            r0 = r22;
            r0.put(r6);
            goto L_0x0933;
        L_0x0969:
            r29 = "This is a searchable index. Enter search keywords: ";
            goto L_0x0914;
        L_0x096c:
            r38 = "name";
            r39 = "isindex";
            r0 = r22;
            r1 = r38;
            r2 = r39;
            r0.put(r1, r2);
            r38 = new org.jsoup.parser.Token$StartTag;
            r39 = "input";
            r0 = r38;
            r1 = r39;
            r2 = r22;
            r0.m253init(r1, r2);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
            r38 = new org.jsoup.parser.Token$EndTag;
            r39 = "label";
            r38.m250init(r39);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
            r38 = new org.jsoup.parser.Token$StartTag;
            r39 = "hr";
            r38.m252init(r39);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
            r38 = new org.jsoup.parser.Token$EndTag;
            r39 = "form";
            r38.m250init(r39);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
            goto L_0x0011;
        L_0x09b9:
            r38 = "textarea";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x09ee;
        L_0x09c5:
            r0 = r44;
            r1 = r36;
            r0.insert(r1);
            r0 = r44;
            r0 = r0.tokeniser;
            r38 = r0;
            r39 = org.jsoup.parser.TokeniserState.Rcdata;
            r38.transition(r39);
            r44.markInsertionMode();
            r38 = 0;
            r0 = r44;
            r1 = r38;
            r0.framesetOk(r1);
            r38 = Text;
            r0 = r44;
            r1 = r38;
            r0.transition(r1);
            goto L_0x0011;
        L_0x09ee:
            r38 = "xmp";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x0a29;
        L_0x09fa:
            r38 = "p";
            r0 = r44;
            r1 = r38;
            r38 = r0.inButtonScope(r1);
            if (r38 == 0) goto L_0x0a14;
        L_0x0a06:
            r38 = new org.jsoup.parser.Token$EndTag;
            r39 = "p";
            r38.m250init(r39);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
        L_0x0a14:
            r44.reconstructFormattingElements();
            r38 = 0;
            r0 = r44;
            r1 = r38;
            r0.framesetOk(r1);
            r0 = r36;
            r1 = r44;
            org.jsoup.parser.HtmlTreeBuilderState.handleRawtext(r0, r1);
            goto L_0x0011;
        L_0x0a29:
            r38 = "iframe";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x0a47;
        L_0x0a35:
            r38 = 0;
            r0 = r44;
            r1 = r38;
            r0.framesetOk(r1);
            r0 = r36;
            r1 = r44;
            org.jsoup.parser.HtmlTreeBuilderState.handleRawtext(r0, r1);
            goto L_0x0011;
        L_0x0a47:
            r38 = "noembed";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x0a5c;
        L_0x0a53:
            r0 = r36;
            r1 = r44;
            org.jsoup.parser.HtmlTreeBuilderState.handleRawtext(r0, r1);
            goto L_0x0011;
        L_0x0a5c:
            r38 = "select";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x0abd;
        L_0x0a68:
            r44.reconstructFormattingElements();
            r0 = r44;
            r1 = r36;
            r0.insert(r1);
            r38 = 0;
            r0 = r44;
            r1 = r38;
            r0.framesetOk(r1);
            r37 = r44.state();
            r38 = InTable;
            r38 = r37.equals(r38);
            if (r38 != 0) goto L_0x0aa7;
        L_0x0a87:
            r38 = InCaption;
            r38 = r37.equals(r38);
            if (r38 != 0) goto L_0x0aa7;
        L_0x0a8f:
            r38 = InTableBody;
            r38 = r37.equals(r38);
            if (r38 != 0) goto L_0x0aa7;
        L_0x0a97:
            r38 = InRow;
            r38 = r37.equals(r38);
            if (r38 != 0) goto L_0x0aa7;
        L_0x0a9f:
            r38 = InCell;
            r38 = r37.equals(r38);
            if (r38 == 0) goto L_0x0ab2;
        L_0x0aa7:
            r38 = InSelectInTable;
            r0 = r44;
            r1 = r38;
            r0.transition(r1);
            goto L_0x0011;
        L_0x0ab2:
            r38 = InSelect;
            r0 = r44;
            r1 = r38;
            r0.transition(r1);
            goto L_0x0011;
        L_0x0abd:
            r38 = "optgroup";
            r39 = 1;
            r0 = r39;
            r0 = new java.lang.String[r0];
            r39 = r0;
            r40 = 0;
            r41 = "option";
            r39[r40] = r41;
            r38 = org.jsoup.helper.StringUtil.in(r38, r39);
            if (r38 == 0) goto L_0x0afd;
        L_0x0ad3:
            r38 = r44.currentElement();
            r38 = r38.nodeName();
            r39 = "option";
            r38 = r38.equals(r39);
            if (r38 == 0) goto L_0x0af1;
        L_0x0ae3:
            r38 = new org.jsoup.parser.Token$EndTag;
            r39 = "option";
            r38.m250init(r39);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
        L_0x0af1:
            r44.reconstructFormattingElements();
            r0 = r44;
            r1 = r36;
            r0.insert(r1);
            goto L_0x0011;
        L_0x0afd:
            r38 = "rp";
            r39 = 1;
            r0 = r39;
            r0 = new java.lang.String[r0];
            r39 = r0;
            r40 = 0;
            r41 = "rt";
            r39[r40] = r41;
            r38 = org.jsoup.helper.StringUtil.in(r38, r39);
            if (r38 == 0) goto L_0x0b4b;
        L_0x0b13:
            r38 = "ruby";
            r0 = r44;
            r1 = r38;
            r38 = r0.inScope(r1);
            if (r38 == 0) goto L_0x0011;
        L_0x0b1f:
            r44.generateImpliedEndTags();
            r38 = r44.currentElement();
            r38 = r38.nodeName();
            r39 = "ruby";
            r38 = r38.equals(r39);
            if (r38 != 0) goto L_0x0b42;
        L_0x0b32:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
            r38 = "ruby";
            r0 = r44;
            r1 = r38;
            r0.popStackToBefore(r1);
        L_0x0b42:
            r0 = r44;
            r1 = r36;
            r0.insert(r1);
            goto L_0x0011;
        L_0x0b4b:
            r38 = "math";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x0b6c;
        L_0x0b57:
            r44.reconstructFormattingElements();
            r0 = r44;
            r1 = r36;
            r0.insert(r1);
            r0 = r44;
            r0 = r0.tokeniser;
            r38 = r0;
            r38.acknowledgeSelfClosingFlag();
            goto L_0x0011;
        L_0x0b6c:
            r38 = "svg";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x0b8d;
        L_0x0b78:
            r44.reconstructFormattingElements();
            r0 = r44;
            r1 = r36;
            r0.insert(r1);
            r0 = r44;
            r0 = r0.tokeniser;
            r38 = r0;
            r38.acknowledgeSelfClosingFlag();
            goto L_0x0011;
        L_0x0b8d:
            r38 = 11;
            r0 = r38;
            r0 = new java.lang.String[r0];
            r38 = r0;
            r39 = 0;
            r40 = "caption";
            r38[r39] = r40;
            r39 = 1;
            r40 = "col";
            r38[r39] = r40;
            r39 = 2;
            r40 = "colgroup";
            r38[r39] = r40;
            r39 = 3;
            r40 = "frame";
            r38[r39] = r40;
            r39 = 4;
            r40 = "head";
            r38[r39] = r40;
            r39 = 5;
            r40 = "tbody";
            r38[r39] = r40;
            r39 = 6;
            r40 = "td";
            r38[r39] = r40;
            r39 = 7;
            r40 = "tfoot";
            r38[r39] = r40;
            r39 = 8;
            r40 = "th";
            r38[r39] = r40;
            r39 = 9;
            r40 = "thead";
            r38[r39] = r40;
            r39 = 10;
            r40 = "tr";
            r38[r39] = r40;
            r0 = r26;
            r1 = r38;
            r38 = org.jsoup.helper.StringUtil.in(r0, r1);
            if (r38 == 0) goto L_0x0bec;
        L_0x0be1:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
            r38 = 0;
            goto L_0x0013;
        L_0x0bec:
            r44.reconstructFormattingElements();
            r0 = r44;
            r1 = r36;
            r0.insert(r1);
            goto L_0x0011;
        L_0x0bf8:
            r15 = r43.asEndTag();
            r26 = r15.name();
            r38 = "body";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x0c2e;
        L_0x0c0c:
            r38 = "body";
            r0 = r44;
            r1 = r38;
            r38 = r0.inScope(r1);
            if (r38 != 0) goto L_0x0c23;
        L_0x0c18:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
            r38 = 0;
            goto L_0x0013;
        L_0x0c23:
            r38 = AfterBody;
            r0 = r44;
            r1 = r38;
            r0.transition(r1);
            goto L_0x0011;
        L_0x0c2e:
            r38 = "html";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x0c53;
        L_0x0c3a:
            r38 = new org.jsoup.parser.Token$EndTag;
            r39 = "body";
            r38.m250init(r39);
            r0 = r44;
            r1 = r38;
            r28 = r0.process(r1);
            if (r28 == 0) goto L_0x0011;
        L_0x0c4b:
            r0 = r44;
            r38 = r0.process(r15);
            goto L_0x0013;
        L_0x0c53:
            r38 = 24;
            r0 = r38;
            r0 = new java.lang.String[r0];
            r38 = r0;
            r39 = 0;
            r40 = "address";
            r38[r39] = r40;
            r39 = 1;
            r40 = "article";
            r38[r39] = r40;
            r39 = 2;
            r40 = "aside";
            r38[r39] = r40;
            r39 = 3;
            r40 = "blockquote";
            r38[r39] = r40;
            r39 = 4;
            r40 = "button";
            r38[r39] = r40;
            r39 = 5;
            r40 = "center";
            r38[r39] = r40;
            r39 = 6;
            r40 = "details";
            r38[r39] = r40;
            r39 = 7;
            r40 = "dir";
            r38[r39] = r40;
            r39 = 8;
            r40 = "div";
            r38[r39] = r40;
            r39 = 9;
            r40 = "dl";
            r38[r39] = r40;
            r39 = 10;
            r40 = "fieldset";
            r38[r39] = r40;
            r39 = 11;
            r40 = "figcaption";
            r38[r39] = r40;
            r39 = 12;
            r40 = "figure";
            r38[r39] = r40;
            r39 = 13;
            r40 = "footer";
            r38[r39] = r40;
            r39 = 14;
            r40 = "header";
            r38[r39] = r40;
            r39 = 15;
            r40 = "hgroup";
            r38[r39] = r40;
            r39 = 16;
            r40 = "listing";
            r38[r39] = r40;
            r39 = 17;
            r40 = "menu";
            r38[r39] = r40;
            r39 = 18;
            r40 = "nav";
            r38[r39] = r40;
            r39 = 19;
            r40 = "ol";
            r38[r39] = r40;
            r39 = 20;
            r40 = "pre";
            r38[r39] = r40;
            r39 = 21;
            r40 = "section";
            r38[r39] = r40;
            r39 = 22;
            r40 = "summary";
            r38[r39] = r40;
            r39 = 23;
            r40 = "ul";
            r38[r39] = r40;
            r0 = r26;
            r1 = r38;
            r38 = org.jsoup.helper.StringUtil.in(r0, r1);
            if (r38 == 0) goto L_0x0d2f;
        L_0x0cf5:
            r0 = r44;
            r1 = r26;
            r38 = r0.inScope(r1);
            if (r38 != 0) goto L_0x0d0a;
        L_0x0cff:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
            r38 = 0;
            goto L_0x0013;
        L_0x0d0a:
            r44.generateImpliedEndTags();
            r38 = r44.currentElement();
            r38 = r38.nodeName();
            r0 = r38;
            r1 = r26;
            r38 = r0.equals(r1);
            if (r38 != 0) goto L_0x0d26;
        L_0x0d1f:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
        L_0x0d26:
            r0 = r44;
            r1 = r26;
            r0.popStackToClose(r1);
            goto L_0x0011;
        L_0x0d2f:
            r38 = "form";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x0d82;
        L_0x0d3b:
            r13 = r44.getFormElement();
            r38 = 0;
            r0 = r44;
            r1 = r38;
            r0.setFormElement(r1);
            if (r13 == 0) goto L_0x0d54;
        L_0x0d4a:
            r0 = r44;
            r1 = r26;
            r38 = r0.inScope(r1);
            if (r38 != 0) goto L_0x0d5f;
        L_0x0d54:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
            r38 = 0;
            goto L_0x0013;
        L_0x0d5f:
            r44.generateImpliedEndTags();
            r38 = r44.currentElement();
            r38 = r38.nodeName();
            r0 = r38;
            r1 = r26;
            r38 = r0.equals(r1);
            if (r38 != 0) goto L_0x0d7b;
        L_0x0d74:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
        L_0x0d7b:
            r0 = r44;
            r0.removeFromStack(r13);
            goto L_0x0011;
        L_0x0d82:
            r38 = "p";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x0de0;
        L_0x0d8e:
            r0 = r44;
            r1 = r26;
            r38 = r0.inButtonScope(r1);
            if (r38 != 0) goto L_0x0db7;
        L_0x0d98:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
            r38 = new org.jsoup.parser.Token$StartTag;
            r0 = r38;
            r1 = r26;
            r0.m252init(r1);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
            r0 = r44;
            r38 = r0.process(r15);
            goto L_0x0013;
        L_0x0db7:
            r0 = r44;
            r1 = r26;
            r0.generateImpliedEndTags(r1);
            r38 = r44.currentElement();
            r38 = r38.nodeName();
            r0 = r38;
            r1 = r26;
            r38 = r0.equals(r1);
            if (r38 != 0) goto L_0x0dd7;
        L_0x0dd0:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
        L_0x0dd7:
            r0 = r44;
            r1 = r26;
            r0.popStackToClose(r1);
            goto L_0x0011;
        L_0x0de0:
            r38 = "li";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x0e2a;
        L_0x0dec:
            r0 = r44;
            r1 = r26;
            r38 = r0.inListItemScope(r1);
            if (r38 != 0) goto L_0x0e01;
        L_0x0df6:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
            r38 = 0;
            goto L_0x0013;
        L_0x0e01:
            r0 = r44;
            r1 = r26;
            r0.generateImpliedEndTags(r1);
            r38 = r44.currentElement();
            r38 = r38.nodeName();
            r0 = r38;
            r1 = r26;
            r38 = r0.equals(r1);
            if (r38 != 0) goto L_0x0e21;
        L_0x0e1a:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
        L_0x0e21:
            r0 = r44;
            r1 = r26;
            r0.popStackToClose(r1);
            goto L_0x0011;
        L_0x0e2a:
            r38 = 2;
            r0 = r38;
            r0 = new java.lang.String[r0];
            r38 = r0;
            r39 = 0;
            r40 = "dd";
            r38[r39] = r40;
            r39 = 1;
            r40 = "dt";
            r38[r39] = r40;
            r0 = r26;
            r1 = r38;
            r38 = org.jsoup.helper.StringUtil.in(r0, r1);
            if (r38 == 0) goto L_0x0e86;
        L_0x0e48:
            r0 = r44;
            r1 = r26;
            r38 = r0.inScope(r1);
            if (r38 != 0) goto L_0x0e5d;
        L_0x0e52:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
            r38 = 0;
            goto L_0x0013;
        L_0x0e5d:
            r0 = r44;
            r1 = r26;
            r0.generateImpliedEndTags(r1);
            r38 = r44.currentElement();
            r38 = r38.nodeName();
            r0 = r38;
            r1 = r26;
            r38 = r0.equals(r1);
            if (r38 != 0) goto L_0x0e7d;
        L_0x0e76:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
        L_0x0e7d:
            r0 = r44;
            r1 = r26;
            r0.popStackToClose(r1);
            goto L_0x0011;
        L_0x0e86:
            r38 = 6;
            r0 = r38;
            r0 = new java.lang.String[r0];
            r38 = r0;
            r39 = 0;
            r40 = "h1";
            r38[r39] = r40;
            r39 = 1;
            r40 = "h2";
            r38[r39] = r40;
            r39 = 2;
            r40 = "h3";
            r38[r39] = r40;
            r39 = 3;
            r40 = "h4";
            r38[r39] = r40;
            r39 = 4;
            r40 = "h5";
            r38[r39] = r40;
            r39 = 5;
            r40 = "h6";
            r38[r39] = r40;
            r0 = r26;
            r1 = r38;
            r38 = org.jsoup.helper.StringUtil.in(r0, r1);
            if (r38 == 0) goto L_0x0f52;
        L_0x0ebc:
            r38 = 6;
            r0 = r38;
            r0 = new java.lang.String[r0];
            r38 = r0;
            r39 = 0;
            r40 = "h1";
            r38[r39] = r40;
            r39 = 1;
            r40 = "h2";
            r38[r39] = r40;
            r39 = 2;
            r40 = "h3";
            r38[r39] = r40;
            r39 = 3;
            r40 = "h4";
            r38[r39] = r40;
            r39 = 4;
            r40 = "h5";
            r38[r39] = r40;
            r39 = 5;
            r40 = "h6";
            r38[r39] = r40;
            r0 = r44;
            r1 = r38;
            r38 = r0.inScope(r1);
            if (r38 != 0) goto L_0x0efd;
        L_0x0ef2:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
            r38 = 0;
            goto L_0x0013;
        L_0x0efd:
            r0 = r44;
            r1 = r26;
            r0.generateImpliedEndTags(r1);
            r38 = r44.currentElement();
            r38 = r38.nodeName();
            r0 = r38;
            r1 = r26;
            r38 = r0.equals(r1);
            if (r38 != 0) goto L_0x0f1d;
        L_0x0f16:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
        L_0x0f1d:
            r38 = 6;
            r0 = r38;
            r0 = new java.lang.String[r0];
            r38 = r0;
            r39 = 0;
            r40 = "h1";
            r38[r39] = r40;
            r39 = 1;
            r40 = "h2";
            r38[r39] = r40;
            r39 = 2;
            r40 = "h3";
            r38[r39] = r40;
            r39 = 3;
            r40 = "h4";
            r38[r39] = r40;
            r39 = 4;
            r40 = "h5";
            r38[r39] = r40;
            r39 = 5;
            r40 = "h6";
            r38[r39] = r40;
            r0 = r44;
            r1 = r38;
            r0.popStackToClose(r1);
            goto L_0x0011;
        L_0x0f52:
            r38 = "sarcasm";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x0f64;
        L_0x0f5e:
            r38 = r42.anyOtherEndTag(r43, r44);
            goto L_0x0013;
        L_0x0f64:
            r38 = 14;
            r0 = r38;
            r0 = new java.lang.String[r0];
            r38 = r0;
            r39 = 0;
            r40 = "a";
            r38[r39] = r40;
            r39 = 1;
            r40 = "b";
            r38[r39] = r40;
            r39 = 2;
            r40 = "big";
            r38[r39] = r40;
            r39 = 3;
            r40 = "code";
            r38[r39] = r40;
            r39 = 4;
            r40 = "em";
            r38[r39] = r40;
            r39 = 5;
            r40 = "font";
            r38[r39] = r40;
            r39 = 6;
            r40 = "i";
            r38[r39] = r40;
            r39 = 7;
            r40 = "nobr";
            r38[r39] = r40;
            r39 = 8;
            r40 = "s";
            r38[r39] = r40;
            r39 = 9;
            r40 = "small";
            r38[r39] = r40;
            r39 = 10;
            r40 = "strike";
            r38[r39] = r40;
            r39 = 11;
            r40 = "strong";
            r38[r39] = r40;
            r39 = 12;
            r40 = "tt";
            r38[r39] = r40;
            r39 = 13;
            r40 = "u";
            r38[r39] = r40;
            r0 = r26;
            r1 = r38;
            r38 = org.jsoup.helper.StringUtil.in(r0, r1);
            if (r38 == 0) goto L_0x11b0;
        L_0x0fca:
            r20 = 0;
        L_0x0fcc:
            r38 = 8;
            r0 = r20;
            r1 = r38;
            if (r0 >= r1) goto L_0x0011;
        L_0x0fd4:
            r0 = r44;
            r1 = r26;
            r17 = r0.getActiveFormattingElement(r1);
            if (r17 != 0) goto L_0x0fe4;
        L_0x0fde:
            r38 = r42.anyOtherEndTag(r43, r44);
            goto L_0x0013;
        L_0x0fe4:
            r0 = r44;
            r1 = r17;
            r38 = r0.onStack(r1);
            if (r38 != 0) goto L_0x1000;
        L_0x0fee:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
            r0 = r44;
            r1 = r17;
            r0.removeFromActiveFormattingElements(r1);
            r38 = 1;
            goto L_0x0013;
        L_0x1000:
            r38 = r17.nodeName();
            r0 = r44;
            r1 = r38;
            r38 = r0.inScope(r1);
            if (r38 != 0) goto L_0x1019;
        L_0x100e:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
            r38 = 0;
            goto L_0x0013;
        L_0x1019:
            r38 = r44.currentElement();
            r0 = r38;
            r1 = r17;
            if (r0 == r1) goto L_0x102a;
        L_0x1023:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
        L_0x102a:
            r18 = 0;
            r12 = 0;
            r33 = 0;
            r35 = r44.getStack();
            r34 = 0;
        L_0x1035:
            r38 = r35.size();
            r0 = r34;
            r1 = r38;
            if (r0 >= r1) goto L_0x1072;
        L_0x103f:
            r38 = 64;
            r0 = r34;
            r1 = r38;
            if (r0 >= r1) goto L_0x1072;
        L_0x1047:
            r0 = r35;
            r1 = r34;
            r14 = r0.get(r1);
            r14 = (org.jsoup.nodes.Element) r14;
            r0 = r17;
            if (r14 != r0) goto L_0x1066;
        L_0x1055:
            r38 = r34 + -1;
            r0 = r35;
            r1 = r38;
            r12 = r0.get(r1);
            r12 = (org.jsoup.nodes.Element) r12;
            r33 = 1;
        L_0x1063:
            r34 = r34 + 1;
            goto L_0x1035;
        L_0x1066:
            if (r33 == 0) goto L_0x1063;
        L_0x1068:
            r0 = r44;
            r38 = r0.isSpecial(r14);
            if (r38 == 0) goto L_0x1063;
        L_0x1070:
            r18 = r14;
        L_0x1072:
            if (r18 != 0) goto L_0x108a;
        L_0x1074:
            r38 = r17.nodeName();
            r0 = r44;
            r1 = r38;
            r0.popStackToClose(r1);
            r0 = r44;
            r1 = r17;
            r0.removeFromActiveFormattingElements(r1);
            r38 = 1;
            goto L_0x0013;
        L_0x108a:
            r27 = r18;
            r24 = r18;
            r23 = 0;
        L_0x1090:
            r38 = 3;
            r0 = r23;
            r1 = r38;
            if (r0 >= r1) goto L_0x10c4;
        L_0x1098:
            r0 = r44;
            r1 = r27;
            r38 = r0.onStack(r1);
            if (r38 == 0) goto L_0x10aa;
        L_0x10a2:
            r0 = r44;
            r1 = r27;
            r27 = r0.aboveOnStack(r1);
        L_0x10aa:
            r0 = r44;
            r1 = r27;
            r38 = r0.isInActiveFormattingElements(r1);
            if (r38 != 0) goto L_0x10be;
        L_0x10b4:
            r0 = r44;
            r1 = r27;
            r0.removeFromStack(r1);
        L_0x10bb:
            r23 = r23 + 1;
            goto L_0x1090;
        L_0x10be:
            r0 = r27;
            r1 = r17;
            if (r0 != r1) goto L_0x113d;
        L_0x10c4:
            r38 = r12.nodeName();
            r39 = 5;
            r0 = r39;
            r0 = new java.lang.String[r0];
            r39 = r0;
            r40 = 0;
            r41 = "table";
            r39[r40] = r41;
            r40 = 1;
            r41 = "tbody";
            r39[r40] = r41;
            r40 = 2;
            r41 = "tfoot";
            r39[r40] = r41;
            r40 = 3;
            r41 = "thead";
            r39[r40] = r41;
            r40 = 4;
            r41 = "tr";
            r39[r40] = r41;
            r38 = org.jsoup.helper.StringUtil.in(r38, r39);
            if (r38 == 0) goto L_0x1182;
        L_0x10f4:
            r38 = r24.parent();
            if (r38 == 0) goto L_0x10fd;
        L_0x10fa:
            r24.remove();
        L_0x10fd:
            r0 = r44;
            r1 = r24;
            r0.insertInFosterParent(r1);
        L_0x1104:
            r4 = new org.jsoup.nodes.Element;
            r38 = org.jsoup.parser.Tag.valueOf(r26);
            r39 = r44.getBaseUri();
            r0 = r38;
            r1 = r39;
            r4.m108init(r0, r1);
            r38 = r18.childNodes();
            r39 = r18.childNodeSize();
            r0 = r39;
            r0 = new org.jsoup.nodes.Node[r0];
            r39 = r0;
            r11 = r38.toArray(r39);
            r11 = (org.jsoup.nodes.Node[]) r11;
            r5 = r11;
            r0 = r5.length;
            r25 = r0;
            r21 = 0;
        L_0x112f:
            r0 = r21;
            r1 = r25;
            if (r0 >= r1) goto L_0x1192;
        L_0x1135:
            r10 = r5[r21];
            r4.appendChild(r10);
            r21 = r21 + 1;
            goto L_0x112f;
        L_0x113d:
            r31 = new org.jsoup.nodes.Element;
            r38 = r27.nodeName();
            r38 = org.jsoup.parser.Tag.valueOf(r38);
            r39 = r44.getBaseUri();
            r0 = r31;
            r1 = r38;
            r2 = r39;
            r0.m108init(r1, r2);
            r0 = r44;
            r1 = r27;
            r2 = r31;
            r0.replaceActiveFormattingElement(r1, r2);
            r0 = r44;
            r1 = r27;
            r2 = r31;
            r0.replaceOnStack(r1, r2);
            r27 = r31;
            r0 = r24;
            r1 = r18;
            if (r0 != r1) goto L_0x116e;
        L_0x116e:
            r38 = r24.parent();
            if (r38 == 0) goto L_0x1177;
        L_0x1174:
            r24.remove();
        L_0x1177:
            r0 = r27;
            r1 = r24;
            r0.appendChild(r1);
            r24 = r27;
            goto L_0x10bb;
        L_0x1182:
            r38 = r24.parent();
            if (r38 == 0) goto L_0x118b;
        L_0x1188:
            r24.remove();
        L_0x118b:
            r0 = r24;
            r12.appendChild(r0);
            goto L_0x1104;
        L_0x1192:
            r0 = r18;
            r0.appendChild(r4);
            r0 = r44;
            r1 = r17;
            r0.removeFromActiveFormattingElements(r1);
            r0 = r44;
            r1 = r17;
            r0.removeFromStack(r1);
            r0 = r44;
            r1 = r18;
            r0.insertOnStackAfter(r1, r4);
            r20 = r20 + 1;
            goto L_0x0fcc;
        L_0x11b0:
            r38 = 3;
            r0 = r38;
            r0 = new java.lang.String[r0];
            r38 = r0;
            r39 = 0;
            r40 = "applet";
            r38[r39] = r40;
            r39 = 1;
            r40 = "marquee";
            r38[r39] = r40;
            r39 = 2;
            r40 = "object";
            r38[r39] = r40;
            r0 = r26;
            r1 = r38;
            r38 = org.jsoup.helper.StringUtil.in(r0, r1);
            if (r38 == 0) goto L_0x121d;
        L_0x11d4:
            r38 = "name";
            r0 = r44;
            r1 = r38;
            r38 = r0.inScope(r1);
            if (r38 != 0) goto L_0x0011;
        L_0x11e0:
            r0 = r44;
            r1 = r26;
            r38 = r0.inScope(r1);
            if (r38 != 0) goto L_0x11f5;
        L_0x11ea:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
            r38 = 0;
            goto L_0x0013;
        L_0x11f5:
            r44.generateImpliedEndTags();
            r38 = r44.currentElement();
            r38 = r38.nodeName();
            r0 = r38;
            r1 = r26;
            r38 = r0.equals(r1);
            if (r38 != 0) goto L_0x1211;
        L_0x120a:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
        L_0x1211:
            r0 = r44;
            r1 = r26;
            r0.popStackToClose(r1);
            r44.clearFormattingElementsToLastMarker();
            goto L_0x0011;
        L_0x121d:
            r38 = "br";
            r0 = r26;
            r1 = r38;
            r38 = r0.equals(r1);
            if (r38 == 0) goto L_0x1242;
        L_0x1229:
            r0 = r44;
            r1 = r42;
            r0.error(r1);
            r38 = new org.jsoup.parser.Token$StartTag;
            r39 = "br";
            r38.m252init(r39);
            r0 = r44;
            r1 = r38;
            r0.process(r1);
            r38 = 0;
            goto L_0x0013;
        L_0x1242:
            r38 = r42.anyOtherEndTag(r43, r44);
            goto L_0x0013;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.jsoup.parser.HtmlTreeBuilderState$AnonymousClass7.process(org.jsoup.parser.Token, org.jsoup.parser.HtmlTreeBuilder):boolean");
        }

        /* access modifiers changed from: 0000 */
        public boolean anyOtherEndTag(Token t, HtmlTreeBuilder tb) {
            String name = t.asEndTag().name();
            Iterator<Element> it = tb.getStack().descendingIterator();
            while (it.hasNext()) {
                Element node = (Element) it.next();
                if (node.nodeName().equals(name)) {
                    tb.generateImpliedEndTags(name);
                    if (!name.equals(tb.currentElement().nodeName())) {
                        tb.error(this);
                    }
                    tb.popStackToClose(name);
                    return true;
                } else if (tb.isSpecial(node)) {
                    tb.error(this);
                    return false;
                }
            }
            return true;
        }
    },
    Text {
        /* access modifiers changed from: 0000 */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (t.isCharacter()) {
                tb.insert(t.asCharacter());
            } else if (t.isEOF()) {
                tb.error(this);
                tb.pop();
                tb.transition(tb.originalState());
                return tb.process(t);
            } else if (t.isEndTag()) {
                tb.pop();
                tb.transition(tb.originalState());
            }
            return true;
        }
    },
    InTable {
        /* access modifiers changed from: 0000 */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (t.isCharacter()) {
                tb.newPendingTableCharacters();
                tb.markInsertionMode();
                tb.transition(InTableText);
                return tb.process(t);
            } else if (t.isComment()) {
                tb.insert(t.asComment());
                return true;
            } else if (t.isDoctype()) {
                tb.error(this);
                return false;
            } else {
                String name;
                if (t.isStartTag()) {
                    StartTag startTag = t.asStartTag();
                    name = startTag.name();
                    if (name.equals("caption")) {
                        tb.clearStackToTableContext();
                        tb.insertMarkerToFormattingElements();
                        tb.insert(startTag);
                        tb.transition(InCaption);
                    } else if (name.equals("colgroup")) {
                        tb.clearStackToTableContext();
                        tb.insert(startTag);
                        tb.transition(InColumnGroup);
                    } else if (name.equals("col")) {
                        tb.process(new StartTag("colgroup"));
                        return tb.process(t);
                    } else {
                        if (StringUtil.in(name, "tbody", "tfoot", "thead")) {
                            tb.clearStackToTableContext();
                            tb.insert(startTag);
                            tb.transition(InTableBody);
                        } else {
                            if (StringUtil.in(name, "td", "th", "tr")) {
                                tb.process(new StartTag("tbody"));
                                return tb.process(t);
                            } else if (name.equals("table")) {
                                tb.error(this);
                                if (tb.process(new EndTag("table"))) {
                                    return tb.process(t);
                                }
                            } else {
                                if (StringUtil.in(name, "style", "script")) {
                                    return tb.process(t, InHead);
                                }
                                if (name.equals("input")) {
                                    if (!startTag.attributes.get("type").equalsIgnoreCase("hidden")) {
                                        return anythingElse(t, tb);
                                    }
                                    tb.insertEmpty(startTag);
                                } else if (!name.equals("form")) {
                                    return anythingElse(t, tb);
                                } else {
                                    tb.error(this);
                                    if (tb.getFormElement() != null) {
                                        return false;
                                    }
                                    tb.setFormElement(tb.insertEmpty(startTag));
                                }
                            }
                        }
                    }
                } else if (t.isEndTag()) {
                    name = t.asEndTag().name();
                    if (!name.equals("table")) {
                        if (!StringUtil.in(name, "body", "caption", "col", "colgroup", "html", "tbody", "td", "tfoot", "th", "thead", "tr")) {
                            return anythingElse(t, tb);
                        }
                        tb.error(this);
                        return false;
                    } else if (tb.inTableScope(name)) {
                        tb.popStackToClose("table");
                        tb.resetInsertionMode();
                    } else {
                        tb.error(this);
                        return false;
                    }
                } else if (t.isEOF()) {
                    if (!tb.currentElement().nodeName().equals("html")) {
                        return true;
                    }
                    tb.error(this);
                    return true;
                }
                return anythingElse(t, tb);
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean anythingElse(Token t, HtmlTreeBuilder tb) {
            tb.error(this);
            if (!StringUtil.in(tb.currentElement().nodeName(), "table", "tbody", "tfoot", "thead", "tr")) {
                return tb.process(t, InBody);
            }
            tb.setFosterInserts(true);
            boolean processed = tb.process(t, InBody);
            tb.setFosterInserts(false);
            return processed;
        }
    },
    InTableText {
        /* access modifiers changed from: 0000 */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            switch (t.type) {
                case Character:
                    Character c = t.asCharacter();
                    if (c.getData().equals(HtmlTreeBuilderState.nullString)) {
                        tb.error(this);
                        return false;
                    }
                    tb.getPendingTableCharacters().add(c);
                    return true;
                default:
                    if (tb.getPendingTableCharacters().size() > 0) {
                        for (Character character : tb.getPendingTableCharacters()) {
                            if (HtmlTreeBuilderState.isWhitespace(character)) {
                                tb.insert(character);
                            } else {
                                tb.error(this);
                                if (StringUtil.in(tb.currentElement().nodeName(), "table", "tbody", "tfoot", "thead", "tr")) {
                                    tb.setFosterInserts(true);
                                    tb.process(character, InBody);
                                    tb.setFosterInserts(false);
                                } else {
                                    tb.process(character, InBody);
                                }
                            }
                        }
                        tb.newPendingTableCharacters();
                    }
                    tb.transition(tb.originalState());
                    return tb.process(t);
            }
        }
    },
    InCaption {
        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Missing block: B:15:0x0091, code skipped:
            if (org.jsoup.helper.StringUtil.in(r13.asStartTag().name(), "caption", "col", "colgroup", "tbody", "td", "tfoot", "th", "thead", "tr") == false) goto L_0x0093;
     */
        public boolean process(org.jsoup.parser.Token r13, org.jsoup.parser.HtmlTreeBuilder r14) {
            /*
            r12 = this;
            r11 = 4;
            r10 = 3;
            r9 = 2;
            r4 = 1;
            r3 = 0;
            r5 = r13.isEndTag();
            if (r5 == 0) goto L_0x0052;
        L_0x000b:
            r5 = r13.asEndTag();
            r5 = r5.name();
            r6 = "caption";
            r5 = r5.equals(r6);
            if (r5 == 0) goto L_0x0052;
        L_0x001b:
            r0 = r13.asEndTag();
            r1 = r0.name();
            r5 = r14.inTableScope(r1);
            if (r5 != 0) goto L_0x002d;
        L_0x0029:
            r14.error(r12);
        L_0x002c:
            return r3;
        L_0x002d:
            r14.generateImpliedEndTags();
            r3 = r14.currentElement();
            r3 = r3.nodeName();
            r5 = "caption";
            r3 = r3.equals(r5);
            if (r3 != 0) goto L_0x0043;
        L_0x0040:
            r14.error(r12);
        L_0x0043:
            r3 = "caption";
            r14.popStackToClose(r3);
            r14.clearFormattingElementsToLastMarker();
            r3 = InTable;
            r14.transition(r3);
        L_0x0050:
            r3 = r4;
            goto L_0x002c;
        L_0x0052:
            r5 = r13.isStartTag();
            if (r5 == 0) goto L_0x0093;
        L_0x0058:
            r5 = r13.asStartTag();
            r5 = r5.name();
            r6 = 9;
            r6 = new java.lang.String[r6];
            r7 = "caption";
            r6[r3] = r7;
            r7 = "col";
            r6[r4] = r7;
            r7 = "colgroup";
            r6[r9] = r7;
            r7 = "tbody";
            r6[r10] = r7;
            r7 = "td";
            r6[r11] = r7;
            r7 = 5;
            r8 = "tfoot";
            r6[r7] = r8;
            r7 = 6;
            r8 = "th";
            r6[r7] = r8;
            r7 = 7;
            r8 = "thead";
            r6[r7] = r8;
            r7 = 8;
            r8 = "tr";
            r6[r7] = r8;
            r5 = org.jsoup.helper.StringUtil.in(r5, r6);
            if (r5 != 0) goto L_0x00a9;
        L_0x0093:
            r5 = r13.isEndTag();
            if (r5 == 0) goto L_0x00bf;
        L_0x0099:
            r5 = r13.asEndTag();
            r5 = r5.name();
            r6 = "table";
            r5 = r5.equals(r6);
            if (r5 == 0) goto L_0x00bf;
        L_0x00a9:
            r14.error(r12);
            r3 = new org.jsoup.parser.Token$EndTag;
            r5 = "caption";
            r3.m250init(r5);
            r2 = r14.process(r3);
            if (r2 == 0) goto L_0x0050;
        L_0x00b9:
            r3 = r14.process(r13);
            goto L_0x002c;
        L_0x00bf:
            r5 = r13.isEndTag();
            if (r5 == 0) goto L_0x010b;
        L_0x00c5:
            r5 = r13.asEndTag();
            r5 = r5.name();
            r6 = 10;
            r6 = new java.lang.String[r6];
            r7 = "body";
            r6[r3] = r7;
            r7 = "col";
            r6[r4] = r7;
            r4 = "colgroup";
            r6[r9] = r4;
            r4 = "html";
            r6[r10] = r4;
            r4 = "tbody";
            r6[r11] = r4;
            r4 = 5;
            r7 = "td";
            r6[r4] = r7;
            r4 = 6;
            r7 = "tfoot";
            r6[r4] = r7;
            r4 = 7;
            r7 = "th";
            r6[r4] = r7;
            r4 = 8;
            r7 = "thead";
            r6[r4] = r7;
            r4 = 9;
            r7 = "tr";
            r6[r4] = r7;
            r4 = org.jsoup.helper.StringUtil.in(r5, r6);
            if (r4 == 0) goto L_0x010b;
        L_0x0106:
            r14.error(r12);
            goto L_0x002c;
        L_0x010b:
            r3 = InBody;
            r3 = r14.process(r13, r3);
            goto L_0x002c;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.jsoup.parser.HtmlTreeBuilderState$AnonymousClass11.process(org.jsoup.parser.Token, org.jsoup.parser.HtmlTreeBuilder):boolean");
        }
    },
    InColumnGroup {
        /* access modifiers changed from: 0000 */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (HtmlTreeBuilderState.isWhitespace(t)) {
                tb.insert(t.asCharacter());
                return true;
            }
            switch (t.type) {
                case Comment:
                    tb.insert(t.asComment());
                    return true;
                case Doctype:
                    tb.error(this);
                    return true;
                case StartTag:
                    StartTag startTag = t.asStartTag();
                    String name = startTag.name();
                    if (name.equals("html")) {
                        return tb.process(t, InBody);
                    }
                    if (!name.equals("col")) {
                        return anythingElse(t, tb);
                    }
                    tb.insertEmpty(startTag);
                    return true;
                case EndTag:
                    if (!t.asEndTag().name().equals("colgroup")) {
                        return anythingElse(t, tb);
                    }
                    if (tb.currentElement().nodeName().equals("html")) {
                        tb.error(this);
                        return false;
                    }
                    tb.pop();
                    tb.transition(InTable);
                    return true;
                case EOF:
                    if (tb.currentElement().nodeName().equals("html")) {
                        return true;
                    }
                    return anythingElse(t, tb);
                default:
                    return anythingElse(t, tb);
            }
        }

        private boolean anythingElse(Token t, TreeBuilder tb) {
            if (tb.process(new EndTag("colgroup"))) {
                return tb.process(t);
            }
            return true;
        }
    },
    InTableBody {
        /* access modifiers changed from: 0000 */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            String name;
            switch (t.type) {
                case StartTag:
                    StartTag startTag = t.asStartTag();
                    name = startTag.name();
                    if (name.equals("tr")) {
                        tb.clearStackToTableBodyContext();
                        tb.insert(startTag);
                        tb.transition(InRow);
                        break;
                    }
                    if (StringUtil.in(name, "th", "td")) {
                        tb.error(this);
                        tb.process(new StartTag("tr"));
                        return tb.process(startTag);
                    }
                    if (StringUtil.in(name, "caption", "col", "colgroup", "tbody", "tfoot", "thead")) {
                        return exitTableBody(t, tb);
                    }
                    return anythingElse(t, tb);
                case EndTag:
                    name = t.asEndTag().name();
                    if (StringUtil.in(name, "tbody", "tfoot", "thead")) {
                        if (tb.inTableScope(name)) {
                            tb.clearStackToTableBodyContext();
                            tb.pop();
                            tb.transition(InTable);
                            break;
                        }
                        tb.error(this);
                        return false;
                    } else if (name.equals("table")) {
                        return exitTableBody(t, tb);
                    } else {
                        if (!StringUtil.in(name, "body", "caption", "col", "colgroup", "html", "td", "th", "tr")) {
                            return anythingElse(t, tb);
                        }
                        tb.error(this);
                        return false;
                    }
                default:
                    return anythingElse(t, tb);
            }
            return true;
        }

        private boolean exitTableBody(Token t, HtmlTreeBuilder tb) {
            if (tb.inTableScope("tbody") || tb.inTableScope("thead") || tb.inScope("tfoot")) {
                tb.clearStackToTableBodyContext();
                tb.process(new EndTag(tb.currentElement().nodeName()));
                return tb.process(t);
            }
            tb.error(this);
            return false;
        }

        private boolean anythingElse(Token t, HtmlTreeBuilder tb) {
            return tb.process(t, InTable);
        }
    },
    InRow {
        /* access modifiers changed from: 0000 */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            String name;
            if (t.isStartTag()) {
                StartTag startTag = t.asStartTag();
                name = startTag.name();
                if (StringUtil.in(name, "th", "td")) {
                    tb.clearStackToTableRowContext();
                    tb.insert(startTag);
                    tb.transition(InCell);
                    tb.insertMarkerToFormattingElements();
                } else {
                    if (StringUtil.in(name, "caption", "col", "colgroup", "tbody", "tfoot", "thead", "tr")) {
                        return handleMissingTr(t, tb);
                    }
                    return anythingElse(t, tb);
                }
            } else if (!t.isEndTag()) {
                return anythingElse(t, tb);
            } else {
                name = t.asEndTag().name();
                if (name.equals("tr")) {
                    if (tb.inTableScope(name)) {
                        tb.clearStackToTableRowContext();
                        tb.pop();
                        tb.transition(InTableBody);
                    } else {
                        tb.error(this);
                        return false;
                    }
                } else if (name.equals("table")) {
                    return handleMissingTr(t, tb);
                } else {
                    if (!StringUtil.in(name, "tbody", "tfoot", "thead")) {
                        if (!StringUtil.in(name, "body", "caption", "col", "colgroup", "html", "td", "th")) {
                            return anythingElse(t, tb);
                        }
                        tb.error(this);
                        return false;
                    } else if (tb.inTableScope(name)) {
                        tb.process(new EndTag("tr"));
                        return tb.process(t);
                    } else {
                        tb.error(this);
                        return false;
                    }
                }
            }
            return true;
        }

        private boolean anythingElse(Token t, HtmlTreeBuilder tb) {
            return tb.process(t, InTable);
        }

        private boolean handleMissingTr(Token t, TreeBuilder tb) {
            if (tb.process(new EndTag("tr"))) {
                return tb.process(t);
            }
            return false;
        }
    },
    InCell {
        /* access modifiers changed from: 0000 */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (t.isEndTag()) {
                String name = t.asEndTag().name();
                if (!StringUtil.in(name, "td", "th")) {
                    if (StringUtil.in(name, "body", "caption", "col", "colgroup", "html")) {
                        tb.error(this);
                        return false;
                    }
                    if (!StringUtil.in(name, "table", "tbody", "tfoot", "thead", "tr")) {
                        return anythingElse(t, tb);
                    }
                    if (tb.inTableScope(name)) {
                        closeCell(tb);
                        return tb.process(t);
                    }
                    tb.error(this);
                    return false;
                } else if (tb.inTableScope(name)) {
                    tb.generateImpliedEndTags();
                    if (!tb.currentElement().nodeName().equals(name)) {
                        tb.error(this);
                    }
                    tb.popStackToClose(name);
                    tb.clearFormattingElementsToLastMarker();
                    tb.transition(InRow);
                    return true;
                } else {
                    tb.error(this);
                    tb.transition(InRow);
                    return false;
                }
            }
            if (t.isStartTag()) {
                if (StringUtil.in(t.asStartTag().name(), "caption", "col", "colgroup", "tbody", "td", "tfoot", "th", "thead", "tr")) {
                    if (tb.inTableScope("td") || tb.inTableScope("th")) {
                        closeCell(tb);
                        return tb.process(t);
                    }
                    tb.error(this);
                    return false;
                }
            }
            return anythingElse(t, tb);
        }

        private boolean anythingElse(Token t, HtmlTreeBuilder tb) {
            return tb.process(t, InBody);
        }

        private void closeCell(HtmlTreeBuilder tb) {
            if (tb.inTableScope("td")) {
                tb.process(new EndTag("td"));
            } else {
                tb.process(new EndTag("th"));
            }
        }
    },
    InSelect {
        /* access modifiers changed from: 0000 */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            String name;
            switch (t.type) {
                case Comment:
                    tb.insert(t.asComment());
                    break;
                case Doctype:
                    tb.error(this);
                    return false;
                case StartTag:
                    StartTag start = t.asStartTag();
                    name = start.name();
                    if (name.equals("html")) {
                        return tb.process(start, InBody);
                    }
                    if (name.equals("option")) {
                        tb.process(new EndTag("option"));
                        tb.insert(start);
                        break;
                    } else if (name.equals("optgroup")) {
                        if (tb.currentElement().nodeName().equals("option")) {
                            tb.process(new EndTag("option"));
                        } else if (tb.currentElement().nodeName().equals("optgroup")) {
                            tb.process(new EndTag("optgroup"));
                        }
                        tb.insert(start);
                        break;
                    } else if (name.equals("select")) {
                        tb.error(this);
                        return tb.process(new EndTag("select"));
                    } else {
                        if (StringUtil.in(name, "input", "keygen", "textarea")) {
                            tb.error(this);
                            if (!tb.inSelectScope("select")) {
                                return false;
                            }
                            tb.process(new EndTag("select"));
                            return tb.process(start);
                        } else if (name.equals("script")) {
                            return tb.process(t, InHead);
                        } else {
                            return anythingElse(t, tb);
                        }
                    }
                case EndTag:
                    name = t.asEndTag().name();
                    if (name.equals("optgroup")) {
                        if (tb.currentElement().nodeName().equals("option") && tb.aboveOnStack(tb.currentElement()) != null && tb.aboveOnStack(tb.currentElement()).nodeName().equals("optgroup")) {
                            tb.process(new EndTag("option"));
                        }
                        if (!tb.currentElement().nodeName().equals("optgroup")) {
                            tb.error(this);
                            break;
                        }
                        tb.pop();
                        break;
                    } else if (name.equals("option")) {
                        if (!tb.currentElement().nodeName().equals("option")) {
                            tb.error(this);
                            break;
                        }
                        tb.pop();
                        break;
                    } else if (name.equals("select")) {
                        if (tb.inSelectScope(name)) {
                            tb.popStackToClose(name);
                            tb.resetInsertionMode();
                            break;
                        }
                        tb.error(this);
                        return false;
                    } else {
                        return anythingElse(t, tb);
                    }
                case Character:
                    Character c = t.asCharacter();
                    if (!c.getData().equals(HtmlTreeBuilderState.nullString)) {
                        tb.insert(c);
                        break;
                    }
                    tb.error(this);
                    return false;
                case EOF:
                    if (!tb.currentElement().nodeName().equals("html")) {
                        tb.error(this);
                        break;
                    }
                    break;
                default:
                    return anythingElse(t, tb);
            }
            return true;
        }

        private boolean anythingElse(Token t, HtmlTreeBuilder tb) {
            tb.error(this);
            return false;
        }
    },
    InSelectInTable {
        /* access modifiers changed from: 0000 */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (t.isStartTag()) {
                if (StringUtil.in(t.asStartTag().name(), "caption", "table", "tbody", "tfoot", "thead", "tr", "td", "th")) {
                    tb.error(this);
                    tb.process(new EndTag("select"));
                    return tb.process(t);
                }
            }
            if (t.isEndTag()) {
                if (StringUtil.in(t.asEndTag().name(), "caption", "table", "tbody", "tfoot", "thead", "tr", "td", "th")) {
                    tb.error(this);
                    if (!tb.inTableScope(t.asEndTag().name())) {
                        return false;
                    }
                    tb.process(new EndTag("select"));
                    return tb.process(t);
                }
            }
            return tb.process(t, InSelect);
        }
    },
    AfterBody {
        /* access modifiers changed from: 0000 */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (HtmlTreeBuilderState.isWhitespace(t)) {
                return tb.process(t, InBody);
            }
            if (t.isComment()) {
                tb.insert(t.asComment());
            } else if (t.isDoctype()) {
                tb.error(this);
                return false;
            } else if (t.isStartTag() && t.asStartTag().name().equals("html")) {
                return tb.process(t, InBody);
            } else {
                if (t.isEndTag() && t.asEndTag().name().equals("html")) {
                    if (tb.isFragmentParsing()) {
                        tb.error(this);
                        return false;
                    }
                    tb.transition(AfterAfterBody);
                } else if (!t.isEOF()) {
                    tb.error(this);
                    tb.transition(InBody);
                    return tb.process(t);
                }
            }
            return true;
        }
    },
    InFrameset {
        /* access modifiers changed from: 0000 */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (HtmlTreeBuilderState.isWhitespace(t)) {
                tb.insert(t.asCharacter());
            } else if (t.isComment()) {
                tb.insert(t.asComment());
            } else if (t.isDoctype()) {
                tb.error(this);
                return false;
            } else if (t.isStartTag()) {
                StartTag start = t.asStartTag();
                String name = start.name();
                if (name.equals("html")) {
                    return tb.process(start, InBody);
                }
                if (name.equals("frameset")) {
                    tb.insert(start);
                } else if (name.equals("frame")) {
                    tb.insertEmpty(start);
                } else if (name.equals("noframes")) {
                    return tb.process(start, InHead);
                } else {
                    tb.error(this);
                    return false;
                }
            } else if (t.isEndTag() && t.asEndTag().name().equals("frameset")) {
                if (tb.currentElement().nodeName().equals("html")) {
                    tb.error(this);
                    return false;
                }
                tb.pop();
                if (!(tb.isFragmentParsing() || tb.currentElement().nodeName().equals("frameset"))) {
                    tb.transition(AfterFrameset);
                }
            } else if (!t.isEOF()) {
                tb.error(this);
                return false;
            } else if (!tb.currentElement().nodeName().equals("html")) {
                tb.error(this);
                return true;
            }
            return true;
        }
    },
    AfterFrameset {
        /* access modifiers changed from: 0000 */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (HtmlTreeBuilderState.isWhitespace(t)) {
                tb.insert(t.asCharacter());
            } else if (t.isComment()) {
                tb.insert(t.asComment());
            } else if (t.isDoctype()) {
                tb.error(this);
                return false;
            } else if (t.isStartTag() && t.asStartTag().name().equals("html")) {
                return tb.process(t, InBody);
            } else {
                if (t.isEndTag() && t.asEndTag().name().equals("html")) {
                    tb.transition(AfterAfterFrameset);
                } else if (t.isStartTag() && t.asStartTag().name().equals("noframes")) {
                    return tb.process(t, InHead);
                } else {
                    if (!t.isEOF()) {
                        tb.error(this);
                        return false;
                    }
                }
            }
            return true;
        }
    },
    AfterAfterBody {
        /* access modifiers changed from: 0000 */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (t.isComment()) {
                tb.insert(t.asComment());
            } else if (t.isDoctype() || HtmlTreeBuilderState.isWhitespace(t) || (t.isStartTag() && t.asStartTag().name().equals("html"))) {
                return tb.process(t, InBody);
            } else {
                if (!t.isEOF()) {
                    tb.error(this);
                    tb.transition(InBody);
                    return tb.process(t);
                }
            }
            return true;
        }
    },
    AfterAfterFrameset {
        /* access modifiers changed from: 0000 */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (t.isComment()) {
                tb.insert(t.asComment());
            } else if (t.isDoctype() || HtmlTreeBuilderState.isWhitespace(t) || (t.isStartTag() && t.asStartTag().name().equals("html"))) {
                return tb.process(t, InBody);
            } else {
                if (!t.isEOF()) {
                    if (t.isStartTag() && t.asStartTag().name().equals("noframes")) {
                        return tb.process(t, InHead);
                    }
                    tb.error(this);
                    return false;
                }
            }
            return true;
        }
    },
    ForeignContent {
        /* access modifiers changed from: 0000 */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            return true;
        }
    };
    
    /* access modifiers changed from: private|static */
    public static String nullString;

    public abstract boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder);

    static {
        nullString = String.valueOf(0);
    }

    /* access modifiers changed from: private|static */
    public static boolean isWhitespace(Token t) {
        if (!t.isCharacter()) {
            return false;
        }
        String data = t.asCharacter().getData();
        for (int i = 0; i < data.length(); i++) {
            if (!StringUtil.isWhitespace(data.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: private|static */
    public static void handleRcData(StartTag startTag, HtmlTreeBuilder tb) {
        tb.insert(startTag);
        tb.tokeniser.transition(TokeniserState.Rcdata);
        tb.markInsertionMode();
        tb.transition(Text);
    }

    /* access modifiers changed from: private|static */
    public static void handleRawtext(StartTag startTag, HtmlTreeBuilder tb) {
        tb.insert(startTag);
        tb.tokeniser.transition(TokeniserState.Rawtext);
        tb.markInsertionMode();
        tb.transition(Text);
    }
}
