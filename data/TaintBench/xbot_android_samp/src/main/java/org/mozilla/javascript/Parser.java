package org.mozilla.javascript;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.mozilla.javascript.Token.CommentType;
import org.mozilla.javascript.ast.ArrayComprehension;
import org.mozilla.javascript.ast.ArrayComprehensionLoop;
import org.mozilla.javascript.ast.ArrayLiteral;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.Block;
import org.mozilla.javascript.ast.BreakStatement;
import org.mozilla.javascript.ast.CatchClause;
import org.mozilla.javascript.ast.Comment;
import org.mozilla.javascript.ast.ConditionalExpression;
import org.mozilla.javascript.ast.ContinueStatement;
import org.mozilla.javascript.ast.DestructuringForm;
import org.mozilla.javascript.ast.DoLoop;
import org.mozilla.javascript.ast.ElementGet;
import org.mozilla.javascript.ast.EmptyExpression;
import org.mozilla.javascript.ast.EmptyStatement;
import org.mozilla.javascript.ast.ErrorNode;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.GeneratorExpression;
import org.mozilla.javascript.ast.GeneratorExpressionLoop;
import org.mozilla.javascript.ast.IdeErrorReporter;
import org.mozilla.javascript.ast.IfStatement;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.Jump;
import org.mozilla.javascript.ast.KeywordLiteral;
import org.mozilla.javascript.ast.Label;
import org.mozilla.javascript.ast.LabeledStatement;
import org.mozilla.javascript.ast.LetNode;
import org.mozilla.javascript.ast.Loop;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NewExpression;
import org.mozilla.javascript.ast.NumberLiteral;
import org.mozilla.javascript.ast.ObjectLiteral;
import org.mozilla.javascript.ast.ObjectProperty;
import org.mozilla.javascript.ast.ParenthesizedExpression;
import org.mozilla.javascript.ast.PropertyGet;
import org.mozilla.javascript.ast.RegExpLiteral;
import org.mozilla.javascript.ast.ReturnStatement;
import org.mozilla.javascript.ast.Scope;
import org.mozilla.javascript.ast.ScriptNode;
import org.mozilla.javascript.ast.StringLiteral;
import org.mozilla.javascript.ast.SwitchStatement;
import org.mozilla.javascript.ast.Symbol;
import org.mozilla.javascript.ast.ThrowStatement;
import org.mozilla.javascript.ast.TryStatement;
import org.mozilla.javascript.ast.UnaryExpression;
import org.mozilla.javascript.ast.VariableDeclaration;
import org.mozilla.javascript.ast.VariableInitializer;
import org.mozilla.javascript.ast.WhileLoop;
import org.mozilla.javascript.ast.WithStatement;
import org.mozilla.javascript.ast.XmlDotQuery;
import org.mozilla.javascript.ast.XmlElemRef;
import org.mozilla.javascript.ast.XmlExpression;
import org.mozilla.javascript.ast.XmlLiteral;
import org.mozilla.javascript.ast.XmlMemberGet;
import org.mozilla.javascript.ast.XmlPropRef;
import org.mozilla.javascript.ast.XmlRef;
import org.mozilla.javascript.ast.XmlString;
import org.mozilla.javascript.ast.Yield;
import org.objectweb.asm.Opcodes;

public class Parser {
    public static final int ARGC_LIMIT = 65536;
    static final int CLEAR_TI_MASK = 65535;
    private static final int GET_ENTRY = 2;
    private static final int PROP_ENTRY = 1;
    private static final int SET_ENTRY = 4;
    static final int TI_AFTER_EOL = 65536;
    static final int TI_CHECK_LABEL = 131072;
    boolean calledByCompileFunction;
    CompilerEnvirons compilerEnv;
    private int currentFlaggedToken;
    private Comment currentJsDocComment;
    private LabeledStatement currentLabel;
    Scope currentScope;
    ScriptNode currentScriptOrFn;
    private int currentToken;
    /* access modifiers changed from: private */
    public int endFlags;
    private IdeErrorReporter errorCollector;
    private ErrorReporter errorReporter;
    private boolean inDestructuringAssignment;
    /* access modifiers changed from: private */
    public boolean inForInit;
    protected boolean inUseStrictDirective;
    /* access modifiers changed from: private */
    public Map<String, LabeledStatement> labelSet;
    /* access modifiers changed from: private */
    public List<Jump> loopAndSwitchSet;
    /* access modifiers changed from: private */
    public List<Loop> loopSet;
    protected int nestingOfFunction;
    private boolean parseFinished;
    private int prevNameTokenLineno;
    private int prevNameTokenStart;
    private String prevNameTokenString;
    private List<Comment> scannedComments;
    private char[] sourceChars;
    private String sourceURI;
    private int syntaxErrorCount;
    private TokenStream ts;

    private static class ConditionData {
        AstNode condition;
        int lp;
        int rp;

        private ConditionData() {
            this.lp = -1;
            this.rp = -1;
        }
    }

    private static class ParserException extends RuntimeException {
        static final long serialVersionUID = 5882582646773765630L;

        private ParserException() {
        }
    }

    protected class PerFunctionVariables {
        private Scope savedCurrentScope;
        private ScriptNode savedCurrentScriptOrFn;
        private int savedEndFlags;
        private boolean savedInForInit;
        private Map<String, LabeledStatement> savedLabelSet;
        private List<Jump> savedLoopAndSwitchSet;
        private List<Loop> savedLoopSet;

        PerFunctionVariables(FunctionNode fnNode) {
            this.savedCurrentScriptOrFn = Parser.this.currentScriptOrFn;
            Parser.this.currentScriptOrFn = fnNode;
            this.savedCurrentScope = Parser.this.currentScope;
            Parser.this.currentScope = fnNode;
            this.savedLabelSet = Parser.this.labelSet;
            Parser.this.labelSet = null;
            this.savedLoopSet = Parser.this.loopSet;
            Parser.this.loopSet = null;
            this.savedLoopAndSwitchSet = Parser.this.loopAndSwitchSet;
            Parser.this.loopAndSwitchSet = null;
            this.savedEndFlags = Parser.this.endFlags;
            Parser.this.endFlags = 0;
            this.savedInForInit = Parser.this.inForInit;
            Parser.this.inForInit = false;
        }

        /* access modifiers changed from: 0000 */
        public void restore() {
            Parser.this.currentScriptOrFn = this.savedCurrentScriptOrFn;
            Parser.this.currentScope = this.savedCurrentScope;
            Parser.this.labelSet = this.savedLabelSet;
            Parser.this.loopSet = this.savedLoopSet;
            Parser.this.loopAndSwitchSet = this.savedLoopAndSwitchSet;
            Parser.this.endFlags = this.savedEndFlags;
            Parser.this.inForInit = this.savedInForInit;
        }
    }

    public Parser() {
        this(new CompilerEnvirons());
    }

    public Parser(CompilerEnvirons compilerEnv) {
        this(compilerEnv, compilerEnv.getErrorReporter());
    }

    public Parser(CompilerEnvirons compilerEnv, ErrorReporter errorReporter) {
        this.currentFlaggedToken = 0;
        this.prevNameTokenString = "";
        this.compilerEnv = compilerEnv;
        this.errorReporter = errorReporter;
        if (errorReporter instanceof IdeErrorReporter) {
            this.errorCollector = (IdeErrorReporter) errorReporter;
        }
    }

    /* access modifiers changed from: 0000 */
    public void addStrictWarning(String messageId, String messageArg) {
        int beg = -1;
        int end = -1;
        if (this.ts != null) {
            beg = this.ts.tokenBeg;
            end = this.ts.tokenEnd - this.ts.tokenBeg;
        }
        addStrictWarning(messageId, messageArg, beg, end);
    }

    /* access modifiers changed from: 0000 */
    public void addStrictWarning(String messageId, String messageArg, int position, int length) {
        if (this.compilerEnv.isStrictMode()) {
            addWarning(messageId, messageArg, position, length);
        }
    }

    /* access modifiers changed from: 0000 */
    public void addWarning(String messageId, String messageArg) {
        int beg = -1;
        int end = -1;
        if (this.ts != null) {
            beg = this.ts.tokenBeg;
            end = this.ts.tokenEnd - this.ts.tokenBeg;
        }
        addWarning(messageId, messageArg, beg, end);
    }

    /* access modifiers changed from: 0000 */
    public void addWarning(String messageId, int position, int length) {
        addWarning(messageId, null, position, length);
    }

    /* access modifiers changed from: 0000 */
    public void addWarning(String messageId, String messageArg, int position, int length) {
        String message = lookupMessage(messageId, messageArg);
        if (this.compilerEnv.reportWarningAsError()) {
            addError(messageId, messageArg, position, length);
        } else if (this.errorCollector != null) {
            this.errorCollector.warning(message, this.sourceURI, position, length);
        } else {
            this.errorReporter.warning(message, this.sourceURI, this.ts.getLineno(), this.ts.getLine(), this.ts.getOffset());
        }
    }

    /* access modifiers changed from: 0000 */
    public void addError(String messageId) {
        addError(messageId, this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
    }

    /* access modifiers changed from: 0000 */
    public void addError(String messageId, int position, int length) {
        addError(messageId, null, position, length);
    }

    /* access modifiers changed from: 0000 */
    public void addError(String messageId, String messageArg) {
        addError(messageId, messageArg, this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
    }

    /* access modifiers changed from: 0000 */
    public void addError(String messageId, String messageArg, int position, int length) {
        this.syntaxErrorCount++;
        String message = lookupMessage(messageId, messageArg);
        if (this.errorCollector != null) {
            this.errorCollector.error(message, this.sourceURI, position, length);
            return;
        }
        int lineno = 1;
        int offset = 1;
        String line = "";
        if (this.ts != null) {
            lineno = this.ts.getLineno();
            line = this.ts.getLine();
            offset = this.ts.getOffset();
        }
        this.errorReporter.error(message, this.sourceURI, lineno, line, offset);
    }

    private void addStrictWarning(String messageId, String messageArg, int position, int length, int line, String lineSource, int lineOffset) {
        if (this.compilerEnv.isStrictMode()) {
            addWarning(messageId, messageArg, position, length, line, lineSource, lineOffset);
        }
    }

    private void addWarning(String messageId, String messageArg, int position, int length, int line, String lineSource, int lineOffset) {
        String message = lookupMessage(messageId, messageArg);
        if (this.compilerEnv.reportWarningAsError()) {
            addError(messageId, messageArg, position, length, line, lineSource, lineOffset);
        } else if (this.errorCollector != null) {
            this.errorCollector.warning(message, this.sourceURI, position, length);
        } else {
            this.errorReporter.warning(message, this.sourceURI, line, lineSource, lineOffset);
        }
    }

    private void addError(String messageId, String messageArg, int position, int length, int line, String lineSource, int lineOffset) {
        this.syntaxErrorCount++;
        String message = lookupMessage(messageId, messageArg);
        if (this.errorCollector != null) {
            this.errorCollector.error(message, this.sourceURI, position, length);
        } else {
            this.errorReporter.error(message, this.sourceURI, line, lineSource, lineOffset);
        }
    }

    /* access modifiers changed from: 0000 */
    public String lookupMessage(String messageId) {
        return lookupMessage(messageId, null);
    }

    /* access modifiers changed from: 0000 */
    public String lookupMessage(String messageId, String messageArg) {
        if (messageArg == null) {
            return ScriptRuntime.getMessage0(messageId);
        }
        return ScriptRuntime.getMessage1(messageId, messageArg);
    }

    /* access modifiers changed from: 0000 */
    public void reportError(String messageId) {
        reportError(messageId, null);
    }

    /* access modifiers changed from: 0000 */
    public void reportError(String messageId, String messageArg) {
        if (this.ts == null) {
            reportError(messageId, messageArg, 1, 1);
        } else {
            reportError(messageId, messageArg, this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
        }
    }

    /* access modifiers changed from: 0000 */
    public void reportError(String messageId, int position, int length) {
        reportError(messageId, null, position, length);
    }

    /* access modifiers changed from: 0000 */
    public void reportError(String messageId, String messageArg, int position, int length) {
        addError(messageId, position, length);
        if (!this.compilerEnv.recoverFromErrors()) {
            throw new ParserException();
        }
    }

    private int getNodeEnd(AstNode n) {
        return n.getPosition() + n.getLength();
    }

    private void recordComment(int lineno, String comment) {
        if (this.scannedComments == null) {
            this.scannedComments = new ArrayList();
        }
        Comment commentNode = new Comment(this.ts.tokenBeg, this.ts.getTokenLength(), this.ts.commentType, comment);
        if (this.ts.commentType == CommentType.JSDOC && this.compilerEnv.isRecordingLocalJsDocComments()) {
            this.currentJsDocComment = commentNode;
        }
        commentNode.setLineno(lineno);
        this.scannedComments.add(commentNode);
    }

    private Comment getAndResetJsDoc() {
        Comment saved = this.currentJsDocComment;
        this.currentJsDocComment = null;
        return saved;
    }

    private int getNumberOfEols(String comment) {
        int lines = 0;
        for (int i = comment.length() - 1; i >= 0; i--) {
            if (comment.charAt(i) == 10) {
                lines++;
            }
        }
        return lines;
    }

    private int peekToken() throws IOException {
        if (this.currentFlaggedToken != 0) {
            return this.currentToken;
        }
        int lineno = this.ts.getLineno();
        int tt = this.ts.getToken();
        boolean sawEOL = false;
        while (true) {
            if (tt != 1 && tt != 161) {
                break;
            }
            if (tt == 1) {
                lineno++;
                sawEOL = true;
            } else if (this.compilerEnv.isRecordingComments()) {
                String comment = this.ts.getAndResetCurrentComment();
                recordComment(lineno, comment);
                lineno += getNumberOfEols(comment);
            }
            tt = this.ts.getToken();
        }
        this.currentToken = tt;
        this.currentFlaggedToken = (sawEOL ? 65536 : 0) | tt;
        return this.currentToken;
    }

    private int peekFlaggedToken() throws IOException {
        peekToken();
        return this.currentFlaggedToken;
    }

    private void consumeToken() {
        this.currentFlaggedToken = 0;
    }

    private int nextToken() throws IOException {
        int tt = peekToken();
        consumeToken();
        return tt;
    }

    private int nextFlaggedToken() throws IOException {
        peekToken();
        int ttFlagged = this.currentFlaggedToken;
        consumeToken();
        return ttFlagged;
    }

    private boolean matchToken(int toMatch) throws IOException {
        if (peekToken() != toMatch) {
            return false;
        }
        consumeToken();
        return true;
    }

    private int peekTokenOrEOL() throws IOException {
        int tt = peekToken();
        if ((this.currentFlaggedToken & 65536) != 0) {
            return 1;
        }
        return tt;
    }

    private boolean mustMatchToken(int toMatch, String messageId) throws IOException {
        return mustMatchToken(toMatch, messageId, this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
    }

    private boolean mustMatchToken(int toMatch, String msgId, int pos, int len) throws IOException {
        if (matchToken(toMatch)) {
            return true;
        }
        reportError(msgId, pos, len);
        return false;
    }

    private void mustHaveXML() {
        if (!this.compilerEnv.isXmlAvailable()) {
            reportError("msg.XML.not.available");
        }
    }

    public boolean eof() {
        return this.ts.eof();
    }

    /* access modifiers changed from: 0000 */
    public boolean insideFunction() {
        return this.nestingOfFunction != 0;
    }

    /* access modifiers changed from: 0000 */
    public void pushScope(Scope scope) {
        Scope parent = scope.getParentScope();
        if (parent == null) {
            this.currentScope.addChildScope(scope);
        } else if (parent != this.currentScope) {
            codeBug();
        }
        this.currentScope = scope;
    }

    /* access modifiers changed from: 0000 */
    public void popScope() {
        this.currentScope = this.currentScope.getParentScope();
    }

    private void enterLoop(Loop loop) {
        if (this.loopSet == null) {
            this.loopSet = new ArrayList();
        }
        this.loopSet.add(loop);
        if (this.loopAndSwitchSet == null) {
            this.loopAndSwitchSet = new ArrayList();
        }
        this.loopAndSwitchSet.add(loop);
        pushScope(loop);
        if (this.currentLabel != null) {
            this.currentLabel.setStatement(loop);
            this.currentLabel.getFirstLabel().setLoop(loop);
            loop.setRelative(-this.currentLabel.getPosition());
        }
    }

    private void exitLoop() {
        Loop loop = (Loop) this.loopSet.remove(this.loopSet.size() - 1);
        this.loopAndSwitchSet.remove(this.loopAndSwitchSet.size() - 1);
        if (loop.getParent() != null) {
            loop.setRelative(loop.getParent().getPosition());
        }
        popScope();
    }

    private void enterSwitch(SwitchStatement node) {
        if (this.loopAndSwitchSet == null) {
            this.loopAndSwitchSet = new ArrayList();
        }
        this.loopAndSwitchSet.add(node);
    }

    private void exitSwitch() {
        this.loopAndSwitchSet.remove(this.loopAndSwitchSet.size() - 1);
    }

    public AstRoot parse(String sourceString, String sourceURI, int lineno) {
        if (this.parseFinished) {
            throw new IllegalStateException("parser reused");
        }
        this.sourceURI = sourceURI;
        if (this.compilerEnv.isIdeMode()) {
            this.sourceChars = sourceString.toCharArray();
        }
        this.ts = new TokenStream(this, null, sourceString, lineno);
        try {
            AstRoot parse = parse();
            this.parseFinished = true;
            return parse;
        } catch (IOException e) {
            throw new IllegalStateException();
        } catch (Throwable th) {
            this.parseFinished = true;
        }
    }

    public AstRoot parse(Reader sourceReader, String sourceURI, int lineno) throws IOException {
        if (this.parseFinished) {
            throw new IllegalStateException("parser reused");
        } else if (this.compilerEnv.isIdeMode()) {
            return parse(readFully(sourceReader), sourceURI, lineno);
        } else {
            try {
                this.sourceURI = sourceURI;
                this.ts = new TokenStream(this, sourceReader, null, lineno);
                AstRoot parse = parse();
                return parse;
            } finally {
                this.parseFinished = true;
            }
        }
    }

    private AstRoot parse() throws IOException {
        String msg;
        AstNode astRoot = new AstRoot(0);
        this.currentScriptOrFn = astRoot;
        this.currentScope = astRoot;
        int baseLineno = this.ts.lineno;
        int end = 0;
        boolean inDirectivePrologue = true;
        boolean savedStrictMode = this.inUseStrictDirective;
        this.inUseStrictDirective = false;
        while (true) {
            try {
                int tt = peekToken();
                if (tt <= 0) {
                    break;
                }
                AstNode n;
                if (tt == 109) {
                    consumeToken();
                    try {
                        int i;
                        if (this.calledByCompileFunction) {
                            i = 2;
                        } else {
                            i = 1;
                        }
                        n = function(i);
                    } catch (ParserException e) {
                    }
                } else {
                    n = statement();
                    if (inDirectivePrologue) {
                        String directive = getDirective(n);
                        if (directive == null) {
                            inDirectivePrologue = false;
                        } else if (directive.equals("use strict")) {
                            this.inUseStrictDirective = true;
                            astRoot.setInStrictMode(true);
                        }
                    }
                }
                end = getNodeEnd(n);
                astRoot.addChildToBack(n);
                n.setParent(astRoot);
            } catch (StackOverflowError e2) {
                msg = lookupMessage("msg.too.deep.parser.recursion");
                if (this.compilerEnv.isIdeMode()) {
                    this.inUseStrictDirective = savedStrictMode;
                } else {
                    throw Context.reportRuntimeError(msg, this.sourceURI, this.ts.lineno, null, 0);
                }
            } catch (Throwable th) {
                this.inUseStrictDirective = savedStrictMode;
                throw th;
            }
        }
        this.inUseStrictDirective = savedStrictMode;
        if (this.syntaxErrorCount != 0) {
            msg = lookupMessage("msg.got.syntax.errors", String.valueOf(this.syntaxErrorCount));
            if (!this.compilerEnv.isIdeMode()) {
                throw this.errorReporter.runtimeError(msg, this.sourceURI, baseLineno, null, 0);
            }
        }
        if (this.scannedComments != null) {
            end = Math.max(end, getNodeEnd((AstNode) this.scannedComments.get(this.scannedComments.size() - 1)));
            for (Comment c : this.scannedComments) {
                astRoot.addComment(c);
            }
        }
        astRoot.setLength(end - 0);
        astRoot.setSourceName(this.sourceURI);
        astRoot.setBaseLineno(baseLineno);
        astRoot.setEndLineno(this.ts.lineno);
        return astRoot;
    }

    private AstNode parseFunctionBody() throws IOException {
        boolean isExpressionClosure = false;
        if (!matchToken(85)) {
            if (this.compilerEnv.getLanguageVersion() < 180) {
                reportError("msg.no.brace.body");
            } else {
                isExpressionClosure = true;
            }
        }
        this.nestingOfFunction++;
        int pos = this.ts.tokenBeg;
        Block pn = new Block(pos);
        boolean inDirectivePrologue = true;
        boolean savedStrictMode = this.inUseStrictDirective;
        pn.setLineno(this.ts.lineno);
        if (isExpressionClosure) {
            try {
                ReturnStatement n = new ReturnStatement(this.ts.lineno);
                n.setReturnValue(assignExpr());
                n.putProp(25, Boolean.TRUE);
                pn.putProp(25, Boolean.TRUE);
                pn.addStatement(n);
            } catch (ParserException e) {
                this.nestingOfFunction--;
                this.inUseStrictDirective = savedStrictMode;
            } catch (Throwable th) {
                this.nestingOfFunction--;
                this.inUseStrictDirective = savedStrictMode;
            }
        } else {
            while (true) {
                AstNode n2;
                switch (peekToken()) {
                    case -1:
                    case 0:
                    case 86:
                        break;
                    case 109:
                        consumeToken();
                        n2 = function(1);
                        continue;
                    default:
                        n2 = statement();
                        if (inDirectivePrologue) {
                            String directive = getDirective(n2);
                            if (directive != null) {
                                if (!directive.equals("use strict")) {
                                    break;
                                }
                                this.inUseStrictDirective = true;
                                break;
                            }
                            inDirectivePrologue = false;
                            break;
                        }
                        continue;
                }
                pn.addStatement(n2);
            }
        }
        this.nestingOfFunction--;
        this.inUseStrictDirective = savedStrictMode;
        int end = this.ts.tokenEnd;
        getAndResetJsDoc();
        if (!isExpressionClosure && mustMatchToken(86, "msg.no.brace.after.body")) {
            end = this.ts.tokenEnd;
        }
        pn.setLength(end - pos);
        return pn;
    }

    private String getDirective(AstNode n) {
        if (n instanceof ExpressionStatement) {
            AstNode e = ((ExpressionStatement) n).getExpression();
            if (e instanceof StringLiteral) {
                return ((StringLiteral) e).getValue();
            }
        }
        return null;
    }

    private void parseFunctionParams(FunctionNode fnNode) throws IOException {
        if (matchToken(88)) {
            fnNode.setRp(this.ts.tokenBeg - fnNode.getPosition());
            return;
        }
        Map<String, Node> destructuring = null;
        Set<String> paramNames = new HashSet();
        do {
            int tt = peekToken();
            if (tt == 83 || tt == 85) {
                AstNode expr = destructuringPrimaryExpr();
                markDestructuring(expr);
                fnNode.addParam(expr);
                if (destructuring == null) {
                    destructuring = new HashMap();
                }
                String pname = this.currentScriptOrFn.getNextTempName();
                defineSymbol(87, pname, false);
                destructuring.put(pname, expr);
            } else if (mustMatchToken(39, "msg.no.parm")) {
                fnNode.addParam(createNameNode());
                String paramName = this.ts.getString();
                defineSymbol(87, paramName);
                if (this.inUseStrictDirective) {
                    if ("eval".equals(paramName) || "arguments".equals(paramName)) {
                        reportError("msg.bad.id.strict", paramName);
                    }
                    if (paramNames.contains(paramName)) {
                        addError("msg.dup.param.strict", paramName);
                    }
                    paramNames.add(paramName);
                }
            } else {
                fnNode.addParam(makeErrorNode());
            }
        } while (matchToken(89));
        if (destructuring != null) {
            Node destructuringNode = new Node(89);
            for (Entry<String, Node> param : destructuring.entrySet()) {
                destructuringNode.addChildToBack(createDestructuringAssignment(122, (Node) param.getValue(), createName((String) param.getKey())));
            }
            fnNode.putProp(23, destructuringNode);
        }
        if (mustMatchToken(88, "msg.no.paren.after.parms")) {
            fnNode.setRp(this.ts.tokenBeg - fnNode.getPosition());
        }
    }

    private FunctionNode function(int type) throws IOException {
        int syntheticType = type;
        int baseLineno = this.ts.lineno;
        int functionSourceStart = this.ts.tokenBeg;
        Name name = null;
        AstNode memberExprNode = null;
        if (matchToken(39)) {
            name = createNameNode(true, 39);
            if (this.inUseStrictDirective) {
                String id = name.getIdentifier();
                if ("eval".equals(id) || "arguments".equals(id)) {
                    reportError("msg.bad.id.strict", id);
                }
            }
            if (!matchToken(87)) {
                if (this.compilerEnv.isAllowMemberExprAsFunctionName()) {
                    AstNode memberExprHead = name;
                    name = null;
                    memberExprNode = memberExprTail(false, memberExprHead);
                }
                mustMatchToken(87, "msg.no.paren.parms");
            }
        } else if (!matchToken(87)) {
            if (this.compilerEnv.isAllowMemberExprAsFunctionName()) {
                memberExprNode = memberExpr(false);
            }
            mustMatchToken(87, "msg.no.paren.parms");
        }
        int lpPos = this.currentToken == 87 ? this.ts.tokenBeg : -1;
        if (memberExprNode != null) {
            syntheticType = 2;
        }
        if (!(syntheticType == 2 || name == null || name.length() <= 0)) {
            defineSymbol(109, name.getIdentifier());
        }
        FunctionNode fnNode = new FunctionNode(functionSourceStart, name);
        fnNode.setFunctionType(type);
        if (lpPos != -1) {
            fnNode.setLp(lpPos - functionSourceStart);
        }
        fnNode.setJsDocNode(getAndResetJsDoc());
        PerFunctionVariables savedVars = new PerFunctionVariables(fnNode);
        try {
            parseFunctionParams(fnNode);
            fnNode.setBody(parseFunctionBody());
            fnNode.setEncodedSourceBounds(functionSourceStart, this.ts.tokenEnd);
            fnNode.setLength(this.ts.tokenEnd - functionSourceStart);
            if (this.compilerEnv.isStrictMode() && !fnNode.getBody().hasConsistentReturnUsage()) {
                String msg = (name == null || name.length() <= 0) ? "msg.anon.no.return.value" : "msg.no.return.value";
                addStrictWarning(msg, name == null ? "" : name.getIdentifier());
            }
            savedVars.restore();
            if (memberExprNode != null) {
                Kit.codeBug();
                fnNode.setMemberExprNode(memberExprNode);
            }
            fnNode.setSourceName(this.sourceURI);
            fnNode.setBaseLineno(baseLineno);
            fnNode.setEndLineno(this.ts.lineno);
            if (this.compilerEnv.isIdeMode()) {
                fnNode.setParentScope(this.currentScope);
            }
            return fnNode;
        } catch (Throwable th) {
            savedVars.restore();
        }
    }

    private AstNode statements(AstNode parent) throws IOException {
        if (!(this.currentToken == 85 || this.compilerEnv.isIdeMode())) {
            codeBug();
        }
        int pos = this.ts.tokenBeg;
        AstNode block = parent != null ? parent : new Block(pos);
        block.setLineno(this.ts.lineno);
        while (true) {
            int tt = peekToken();
            if (tt <= 0 || tt == 86) {
                block.setLength(this.ts.tokenBeg - pos);
            } else {
                block.addChild(statement());
            }
        }
        block.setLength(this.ts.tokenBeg - pos);
        return block;
    }

    private AstNode statements() throws IOException {
        return statements(null);
    }

    private ConditionData condition() throws IOException {
        ConditionData data = new ConditionData();
        if (mustMatchToken(87, "msg.no.paren.cond")) {
            data.lp = this.ts.tokenBeg;
        }
        data.condition = expr();
        if (mustMatchToken(88, "msg.no.paren.after.cond")) {
            data.rp = this.ts.tokenBeg;
        }
        if (data.condition instanceof Assignment) {
            addStrictWarning("msg.equal.as.assign", "", data.condition.getPosition(), data.condition.getLength());
        }
        return data;
    }

    private AstNode statement() throws IOException {
        int pos = this.ts.tokenBeg;
        try {
            AstNode pn = statementHelper();
            if (pn != null) {
                if (!this.compilerEnv.isStrictMode() || pn.hasSideEffects()) {
                    return pn;
                }
                int beg = pn.getPosition();
                beg = Math.max(beg, lineBeginningFor(beg));
                addStrictWarning(pn instanceof EmptyStatement ? "msg.extra.trailing.semi" : "msg.no.side.effects", "", beg, nodeEnd(pn) - beg);
                return pn;
            }
        } catch (ParserException e) {
        }
        while (true) {
            int tt = peekTokenOrEOL();
            consumeToken();
            switch (tt) {
                case -1:
                case 0:
                case 1:
                case 82:
                    return new EmptyStatement(pos, this.ts.tokenBeg - pos);
                default:
            }
        }
    }

    private AstNode statementHelper() throws IOException {
        AstNode pn;
        boolean z = true;
        if (!(this.currentLabel == null || this.currentLabel.getStatement() == null)) {
            this.currentLabel = null;
        }
        int tt = peekToken();
        int pos = this.ts.tokenBeg;
        int lineno;
        switch (tt) {
            case -1:
                consumeToken();
                return makeErrorNode();
            case 4:
            case 72:
                pn = returnOrYield(tt, false);
                break;
            case 39:
                pn = nameOrLabel();
                if (!(pn instanceof ExpressionStatement)) {
                    return pn;
                }
                break;
            case 50:
                pn = throwStatement();
                break;
            case 81:
                return tryStatement();
            case 82:
                consumeToken();
                pos = this.ts.tokenBeg;
                pn = new EmptyStatement(pos, this.ts.tokenEnd - pos);
                pn.setLineno(this.ts.lineno);
                return pn;
            case 85:
                return block();
            case 109:
                consumeToken();
                return function(3);
            case 112:
                return ifStatement();
            case 114:
                return switchStatement();
            case 116:
                pn = defaultXmlNamespace();
                break;
            case 117:
                return whileLoop();
            case 118:
                return doLoop();
            case 119:
                return forLoop();
            case 120:
                pn = breakStatement();
                break;
            case 121:
                pn = continueStatement();
                break;
            case 122:
            case 154:
                consumeToken();
                lineno = this.ts.lineno;
                pn = variables(this.currentToken, this.ts.tokenBeg, true);
                pn.setLineno(lineno);
                break;
            case 123:
                if (this.inUseStrictDirective) {
                    reportError("msg.no.with.strict");
                }
                return withStatement();
            case 153:
                pn = letStatement();
                if (!((pn instanceof VariableDeclaration) && peekToken() == 82)) {
                    return pn;
                }
            case 160:
                consumeToken();
                pn = new KeywordLiteral(this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg, tt);
                pn.setLineno(this.ts.lineno);
                break;
            default:
                lineno = this.ts.lineno;
                AstNode expr = expr();
                if (insideFunction()) {
                    z = false;
                }
                pn = new ExpressionStatement(expr, z);
                pn.setLineno(lineno);
                break;
        }
        autoInsertSemicolon(pn);
        return pn;
    }

    private void autoInsertSemicolon(AstNode pn) throws IOException {
        int ttFlagged = peekFlaggedToken();
        int pos = pn.getPosition();
        switch (CLEAR_TI_MASK & ttFlagged) {
            case -1:
            case 0:
            case 86:
                warnMissingSemi(pos, nodeEnd(pn));
                return;
            case 82:
                consumeToken();
                pn.setLength(this.ts.tokenEnd - pos);
                return;
            default:
                if ((65536 & ttFlagged) == 0) {
                    reportError("msg.no.semi.stmt");
                    return;
                } else {
                    warnMissingSemi(pos, nodeEnd(pn));
                    return;
                }
        }
    }

    private IfStatement ifStatement() throws IOException {
        AstNode astNode;
        if (this.currentToken != 112) {
            codeBug();
        }
        consumeToken();
        int pos = this.ts.tokenBeg;
        int lineno = this.ts.lineno;
        int elsePos = -1;
        ConditionData data = condition();
        AstNode ifTrue = statement();
        AstNode ifFalse = null;
        if (matchToken(113)) {
            elsePos = this.ts.tokenBeg - pos;
            ifFalse = statement();
        }
        if (ifFalse != null) {
            astNode = ifFalse;
        } else {
            astNode = ifTrue;
        }
        IfStatement pn = new IfStatement(pos, getNodeEnd(astNode) - pos);
        pn.setCondition(data.condition);
        pn.setParens(data.lp - pos, data.rp - pos);
        pn.setThenPart(ifTrue);
        pn.setElsePart(ifFalse);
        pn.setElsePosition(elsePos);
        pn.setLineno(lineno);
        return pn;
    }

    /* JADX WARNING: Missing block: B:24:0x0084, code skipped:
            r2 = new org.mozilla.javascript.ast.SwitchCase(r3);
            r2.setExpression(r0);
            r2.setLength(r11.ts.tokenEnd - r7);
            r2.setLineno(r1);
     */
    /* JADX WARNING: Missing block: B:25:0x0097, code skipped:
            r8 = peekToken();
     */
    /* JADX WARNING: Missing block: B:26:0x009d, code skipped:
            if (r8 == 86) goto L_0x00c2;
     */
    /* JADX WARNING: Missing block: B:28:0x00a1, code skipped:
            if (r8 == 115) goto L_0x00c2;
     */
    /* JADX WARNING: Missing block: B:30:0x00a5, code skipped:
            if (r8 == 116) goto L_0x00c2;
     */
    /* JADX WARNING: Missing block: B:31:0x00a7, code skipped:
            if (r8 == 0) goto L_0x00c2;
     */
    /* JADX WARNING: Missing block: B:32:0x00a9, code skipped:
            r2.addStatement(statement());
     */
    /* JADX WARNING: Missing block: B:36:0x00c2, code skipped:
            r6.addCase(r2);
     */
    private org.mozilla.javascript.ast.SwitchStatement switchStatement() throws java.io.IOException {
        /*
        r11 = this;
        r9 = r11.currentToken;
        r10 = 114; // 0x72 float:1.6E-43 double:5.63E-322;
        if (r9 == r10) goto L_0x0009;
    L_0x0006:
        r11.codeBug();
    L_0x0009:
        r11.consumeToken();
        r9 = r11.ts;
        r7 = r9.tokenBeg;
        r6 = new org.mozilla.javascript.ast.SwitchStatement;
        r6.m1289init(r7);
        r9 = 87;
        r10 = "msg.no.paren.switch";
        r9 = r11.mustMatchToken(r9, r10);
        if (r9 == 0) goto L_0x0027;
    L_0x001f:
        r9 = r11.ts;
        r9 = r9.tokenBeg;
        r9 = r9 - r7;
        r6.setLp(r9);
    L_0x0027:
        r9 = r11.ts;
        r9 = r9.lineno;
        r6.setLineno(r9);
        r4 = r11.expr();
        r6.setExpression(r4);
        r11.enterSwitch(r6);
        r9 = 88;
        r10 = "msg.no.paren.after.switch";
        r9 = r11.mustMatchToken(r9, r10);	 Catch:{ all -> 0x0074 }
        if (r9 == 0) goto L_0x004a;
    L_0x0042:
        r9 = r11.ts;	 Catch:{ all -> 0x0074 }
        r9 = r9.tokenBeg;	 Catch:{ all -> 0x0074 }
        r9 = r9 - r7;
        r6.setRp(r9);	 Catch:{ all -> 0x0074 }
    L_0x004a:
        r9 = 85;
        r10 = "msg.no.brace.switch";
        r11.mustMatchToken(r9, r10);	 Catch:{ all -> 0x0074 }
        r5 = 0;
    L_0x0052:
        r8 = r11.nextToken();	 Catch:{ all -> 0x0074 }
        r9 = r11.ts;	 Catch:{ all -> 0x0074 }
        r3 = r9.tokenBeg;	 Catch:{ all -> 0x0074 }
        r9 = r11.ts;	 Catch:{ all -> 0x0074 }
        r1 = r9.lineno;	 Catch:{ all -> 0x0074 }
        r0 = 0;
        switch(r8) {
            case 86: goto L_0x006b;
            case 115: goto L_0x0079;
            case 116: goto L_0x00b1;
            default: goto L_0x0062;
        };	 Catch:{ all -> 0x0074 }
    L_0x0062:
        r9 = "msg.bad.switch";
        r11.reportError(r9);	 Catch:{ all -> 0x0074 }
    L_0x0067:
        r11.exitSwitch();
        return r6;
    L_0x006b:
        r9 = r11.ts;	 Catch:{ all -> 0x0074 }
        r9 = r9.tokenEnd;	 Catch:{ all -> 0x0074 }
        r9 = r9 - r7;
        r6.setLength(r9);	 Catch:{ all -> 0x0074 }
        goto L_0x0067;
    L_0x0074:
        r9 = move-exception;
        r11.exitSwitch();
        throw r9;
    L_0x0079:
        r0 = r11.expr();	 Catch:{ all -> 0x0074 }
        r9 = 103; // 0x67 float:1.44E-43 double:5.1E-322;
        r10 = "msg.no.colon.case";
        r11.mustMatchToken(r9, r10);	 Catch:{ all -> 0x0074 }
    L_0x0084:
        r2 = new org.mozilla.javascript.ast.SwitchCase;	 Catch:{ all -> 0x0074 }
        r2.m1150init(r3);	 Catch:{ all -> 0x0074 }
        r2.setExpression(r0);	 Catch:{ all -> 0x0074 }
        r9 = r11.ts;	 Catch:{ all -> 0x0074 }
        r9 = r9.tokenEnd;	 Catch:{ all -> 0x0074 }
        r9 = r9 - r7;
        r2.setLength(r9);	 Catch:{ all -> 0x0074 }
        r2.setLineno(r1);	 Catch:{ all -> 0x0074 }
    L_0x0097:
        r8 = r11.peekToken();	 Catch:{ all -> 0x0074 }
        r9 = 86;
        if (r8 == r9) goto L_0x00c2;
    L_0x009f:
        r9 = 115; // 0x73 float:1.61E-43 double:5.7E-322;
        if (r8 == r9) goto L_0x00c2;
    L_0x00a3:
        r9 = 116; // 0x74 float:1.63E-43 double:5.73E-322;
        if (r8 == r9) goto L_0x00c2;
    L_0x00a7:
        if (r8 == 0) goto L_0x00c2;
    L_0x00a9:
        r9 = r11.statement();	 Catch:{ all -> 0x0074 }
        r2.addStatement(r9);	 Catch:{ all -> 0x0074 }
        goto L_0x0097;
    L_0x00b1:
        if (r5 == 0) goto L_0x00b8;
    L_0x00b3:
        r9 = "msg.double.switch.default";
        r11.reportError(r9);	 Catch:{ all -> 0x0074 }
    L_0x00b8:
        r5 = 1;
        r0 = 0;
        r9 = 103; // 0x67 float:1.44E-43 double:5.1E-322;
        r10 = "msg.no.colon.case";
        r11.mustMatchToken(r9, r10);	 Catch:{ all -> 0x0074 }
        goto L_0x0084;
    L_0x00c2:
        r6.addCase(r2);	 Catch:{ all -> 0x0074 }
        goto L_0x0052;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.Parser.switchStatement():org.mozilla.javascript.ast.SwitchStatement");
    }

    private WhileLoop whileLoop() throws IOException {
        if (this.currentToken != 117) {
            codeBug();
        }
        consumeToken();
        int pos = this.ts.tokenBeg;
        WhileLoop pn = new WhileLoop(pos);
        pn.setLineno(this.ts.lineno);
        enterLoop(pn);
        try {
            ConditionData data = condition();
            pn.setCondition(data.condition);
            pn.setParens(data.lp - pos, data.rp - pos);
            AstNode body = statement();
            pn.setLength(getNodeEnd(body) - pos);
            pn.setBody(body);
            return pn;
        } finally {
            exitLoop();
        }
    }

    private DoLoop doLoop() throws IOException {
        if (this.currentToken != 118) {
            codeBug();
        }
        consumeToken();
        int pos = this.ts.tokenBeg;
        DoLoop pn = new DoLoop(pos);
        pn.setLineno(this.ts.lineno);
        enterLoop(pn);
        try {
            AstNode body = statement();
            mustMatchToken(117, "msg.no.while.do");
            pn.setWhilePosition(this.ts.tokenBeg - pos);
            ConditionData data = condition();
            pn.setCondition(data.condition);
            pn.setParens(data.lp - pos, data.rp - pos);
            int end = getNodeEnd(body);
            pn.setBody(body);
            if (matchToken(82)) {
                end = this.ts.tokenEnd;
            }
            pn.setLength(end - pos);
            return pn;
        } finally {
            exitLoop();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x017d  */
    private org.mozilla.javascript.ast.Loop forLoop() throws java.io.IOException {
        /*
        r25 = this;
        r0 = r25;
        r0 = r0.currentToken;
        r23 = r0;
        r24 = 119; // 0x77 float:1.67E-43 double:5.9E-322;
        r0 = r23;
        r1 = r24;
        if (r0 == r1) goto L_0x0011;
    L_0x000e:
        r25.codeBug();
    L_0x0011:
        r25.consumeToken();
        r0 = r25;
        r0 = r0.ts;
        r23 = r0;
        r0 = r23;
        r9 = r0.tokenBeg;
        r0 = r25;
        r0 = r0.ts;
        r23 = r0;
        r0 = r23;
        r0 = r0.lineno;
        r16 = r0;
        r14 = 0;
        r15 = 0;
        r6 = -1;
        r10 = -1;
        r17 = -1;
        r19 = -1;
        r13 = 0;
        r4 = 0;
        r11 = 0;
        r18 = 0;
        r20 = new org.mozilla.javascript.ast.Scope;
        r20.m1284init();
        r0 = r25;
        r1 = r20;
        r0.pushScope(r1);
        r23 = 39;
        r0 = r25;
        r1 = r23;
        r23 = r0.matchToken(r1);	 Catch:{ all -> 0x0170 }
        if (r23 == 0) goto L_0x0070;
    L_0x004f:
        r23 = "each";
        r0 = r25;
        r0 = r0.ts;	 Catch:{ all -> 0x0170 }
        r24 = r0;
        r24 = r24.getString();	 Catch:{ all -> 0x0170 }
        r23 = r23.equals(r24);	 Catch:{ all -> 0x0170 }
        if (r23 == 0) goto L_0x0165;
    L_0x0061:
        r14 = 1;
        r0 = r25;
        r0 = r0.ts;	 Catch:{ all -> 0x0170 }
        r23 = r0;
        r0 = r23;
        r0 = r0.tokenBeg;	 Catch:{ all -> 0x0170 }
        r23 = r0;
        r6 = r23 - r9;
    L_0x0070:
        r23 = 87;
        r24 = "msg.no.paren.for";
        r0 = r25;
        r1 = r23;
        r2 = r24;
        r23 = r0.mustMatchToken(r1, r2);	 Catch:{ all -> 0x0170 }
        if (r23 == 0) goto L_0x008e;
    L_0x0080:
        r0 = r25;
        r0 = r0.ts;	 Catch:{ all -> 0x0170 }
        r23 = r0;
        r0 = r23;
        r0 = r0.tokenBeg;	 Catch:{ all -> 0x0170 }
        r23 = r0;
        r17 = r23 - r9;
    L_0x008e:
        r22 = r25.peekToken();	 Catch:{ all -> 0x0170 }
        r0 = r25;
        r1 = r22;
        r13 = r0.forLoopInit(r1);	 Catch:{ all -> 0x0170 }
        r23 = 52;
        r0 = r25;
        r1 = r23;
        r23 = r0.matchToken(r1);	 Catch:{ all -> 0x0170 }
        if (r23 == 0) goto L_0x0181;
    L_0x00a6:
        r15 = 1;
        r0 = r25;
        r0 = r0.ts;	 Catch:{ all -> 0x0170 }
        r23 = r0;
        r0 = r23;
        r0 = r0.tokenBeg;	 Catch:{ all -> 0x0170 }
        r23 = r0;
        r10 = r23 - r9;
        r4 = r25.expr();	 Catch:{ all -> 0x0170 }
    L_0x00b9:
        r23 = 88;
        r24 = "msg.no.paren.for.ctrl";
        r0 = r25;
        r1 = r23;
        r2 = r24;
        r23 = r0.mustMatchToken(r1, r2);	 Catch:{ all -> 0x0170 }
        if (r23 == 0) goto L_0x00d7;
    L_0x00c9:
        r0 = r25;
        r0 = r0.ts;	 Catch:{ all -> 0x0170 }
        r23 = r0;
        r0 = r23;
        r0 = r0.tokenBeg;	 Catch:{ all -> 0x0170 }
        r23 = r0;
        r19 = r23 - r9;
    L_0x00d7:
        if (r15 == 0) goto L_0x0212;
    L_0x00d9:
        r7 = new org.mozilla.javascript.ast.ForInLoop;	 Catch:{ all -> 0x0170 }
        r7.m1378init(r9);	 Catch:{ all -> 0x0170 }
        r0 = r13 instanceof org.mozilla.javascript.ast.VariableDeclaration;	 Catch:{ all -> 0x0170 }
        r23 = r0;
        if (r23 == 0) goto L_0x0102;
    L_0x00e4:
        r0 = r13;
        r0 = (org.mozilla.javascript.ast.VariableDeclaration) r0;	 Catch:{ all -> 0x0170 }
        r23 = r0;
        r23 = r23.getVariables();	 Catch:{ all -> 0x0170 }
        r23 = r23.size();	 Catch:{ all -> 0x0170 }
        r24 = 1;
        r0 = r23;
        r1 = r24;
        if (r0 <= r1) goto L_0x0102;
    L_0x00f9:
        r23 = "msg.mult.index";
        r0 = r25;
        r1 = r23;
        r0.reportError(r1);	 Catch:{ all -> 0x0170 }
    L_0x0102:
        r7.setIterator(r13);	 Catch:{ all -> 0x0170 }
        r7.setIteratedObject(r4);	 Catch:{ all -> 0x0170 }
        r7.setInPosition(r10);	 Catch:{ all -> 0x0170 }
        r7.setIsForEach(r14);	 Catch:{ all -> 0x0170 }
        r7.setEachPosition(r6);	 Catch:{ all -> 0x0170 }
        r18 = r7;
    L_0x0113:
        r0 = r25;
        r0 = r0.currentScope;	 Catch:{ all -> 0x0170 }
        r23 = r0;
        r0 = r23;
        r1 = r18;
        r0.replaceWith(r1);	 Catch:{ all -> 0x0170 }
        r25.popScope();	 Catch:{ all -> 0x0170 }
        r0 = r25;
        r1 = r18;
        r0.enterLoop(r1);	 Catch:{ all -> 0x0170 }
        r3 = r25.statement();	 Catch:{ all -> 0x0224 }
        r0 = r25;
        r23 = r0.getNodeEnd(r3);	 Catch:{ all -> 0x0224 }
        r23 = r23 - r9;
        r0 = r18;
        r1 = r23;
        r0.setLength(r1);	 Catch:{ all -> 0x0224 }
        r0 = r18;
        r0.setBody(r3);	 Catch:{ all -> 0x0224 }
        r25.exitLoop();	 Catch:{ all -> 0x0170 }
        r0 = r25;
        r0 = r0.currentScope;
        r23 = r0;
        r0 = r23;
        r1 = r20;
        if (r0 != r1) goto L_0x0154;
    L_0x0151:
        r25.popScope();
    L_0x0154:
        r0 = r18;
        r1 = r17;
        r2 = r19;
        r0.setParens(r1, r2);
        r0 = r18;
        r1 = r16;
        r0.setLineno(r1);
        return r18;
    L_0x0165:
        r23 = "msg.no.paren.for";
        r0 = r25;
        r1 = r23;
        r0.reportError(r1);	 Catch:{ all -> 0x0170 }
        goto L_0x0070;
    L_0x0170:
        r23 = move-exception;
    L_0x0171:
        r0 = r25;
        r0 = r0.currentScope;
        r24 = r0;
        r0 = r24;
        r1 = r20;
        if (r0 != r1) goto L_0x0180;
    L_0x017d:
        r25.popScope();
    L_0x0180:
        throw r23;
    L_0x0181:
        r23 = 82;
        r24 = "msg.no.semi.for";
        r0 = r25;
        r1 = r23;
        r2 = r24;
        r0.mustMatchToken(r1, r2);	 Catch:{ all -> 0x0170 }
        r23 = r25.peekToken();	 Catch:{ all -> 0x0170 }
        r24 = 82;
        r0 = r23;
        r1 = r24;
        if (r0 != r1) goto L_0x0207;
    L_0x019a:
        r5 = new org.mozilla.javascript.ast.EmptyExpression;	 Catch:{ all -> 0x0170 }
        r0 = r25;
        r0 = r0.ts;	 Catch:{ all -> 0x0170 }
        r23 = r0;
        r0 = r23;
        r0 = r0.tokenBeg;	 Catch:{ all -> 0x0170 }
        r23 = r0;
        r24 = 1;
        r0 = r23;
        r1 = r24;
        r5.m1081init(r0, r1);	 Catch:{ all -> 0x0170 }
        r0 = r25;
        r0 = r0.ts;	 Catch:{ all -> 0x0229 }
        r23 = r0;
        r0 = r23;
        r0 = r0.lineno;	 Catch:{ all -> 0x0229 }
        r23 = r0;
        r0 = r23;
        r5.setLineno(r0);	 Catch:{ all -> 0x0229 }
        r4 = r5;
    L_0x01c3:
        r23 = 82;
        r24 = "msg.no.semi.for.cond";
        r0 = r25;
        r1 = r23;
        r2 = r24;
        r0.mustMatchToken(r1, r2);	 Catch:{ all -> 0x0170 }
        r0 = r25;
        r0 = r0.ts;	 Catch:{ all -> 0x0170 }
        r23 = r0;
        r0 = r23;
        r0 = r0.tokenEnd;	 Catch:{ all -> 0x0170 }
        r21 = r0;
        r23 = r25.peekToken();	 Catch:{ all -> 0x0170 }
        r24 = 88;
        r0 = r23;
        r1 = r24;
        if (r0 != r1) goto L_0x020c;
    L_0x01e8:
        r12 = new org.mozilla.javascript.ast.EmptyExpression;	 Catch:{ all -> 0x0170 }
        r23 = 1;
        r0 = r21;
        r1 = r23;
        r12.m1081init(r0, r1);	 Catch:{ all -> 0x0170 }
        r0 = r25;
        r0 = r0.ts;	 Catch:{ all -> 0x022d }
        r23 = r0;
        r0 = r23;
        r0 = r0.lineno;	 Catch:{ all -> 0x022d }
        r23 = r0;
        r0 = r23;
        r12.setLineno(r0);	 Catch:{ all -> 0x022d }
        r11 = r12;
        goto L_0x00b9;
    L_0x0207:
        r4 = r25.expr();	 Catch:{ all -> 0x0170 }
        goto L_0x01c3;
    L_0x020c:
        r11 = r25.expr();	 Catch:{ all -> 0x0170 }
        goto L_0x00b9;
    L_0x0212:
        r8 = new org.mozilla.javascript.ast.ForLoop;	 Catch:{ all -> 0x0170 }
        r8.m1381init(r9);	 Catch:{ all -> 0x0170 }
        r8.setInitializer(r13);	 Catch:{ all -> 0x0170 }
        r8.setCondition(r4);	 Catch:{ all -> 0x0170 }
        r8.setIncrement(r11);	 Catch:{ all -> 0x0170 }
        r18 = r8;
        goto L_0x0113;
    L_0x0224:
        r23 = move-exception;
        r25.exitLoop();	 Catch:{ all -> 0x0170 }
        throw r23;	 Catch:{ all -> 0x0170 }
    L_0x0229:
        r23 = move-exception;
        r4 = r5;
        goto L_0x0171;
    L_0x022d:
        r23 = move-exception;
        r11 = r12;
        goto L_0x0171;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.Parser.forLoop():org.mozilla.javascript.ast.Loop");
    }

    private AstNode forLoopInit(int tt) throws IOException {
        try {
            AstNode init;
            this.inForInit = true;
            if (tt == 82) {
                init = new EmptyExpression(this.ts.tokenBeg, 1);
                init.setLineno(this.ts.lineno);
            } else if (tt == 122 || tt == 153) {
                consumeToken();
                init = variables(tt, this.ts.tokenBeg, false);
            } else {
                init = expr();
                markDestructuring(init);
            }
            this.inForInit = false;
            return init;
        } catch (Throwable th) {
            this.inForInit = false;
        }
    }

    private TryStatement tryStatement() throws IOException {
        if (this.currentToken != 81) {
            codeBug();
        }
        consumeToken();
        Comment jsdocNode = getAndResetJsDoc();
        int tryPos = this.ts.tokenBeg;
        int lineno = this.ts.lineno;
        int finallyPos = -1;
        if (peekToken() != 85) {
            reportError("msg.no.brace.try");
        }
        AstNode tryBlock = statement();
        int tryEnd = getNodeEnd(tryBlock);
        List<CatchClause> clauses = null;
        boolean sawDefaultCatch = false;
        int peek = peekToken();
        if (peek == 124) {
            while (matchToken(124)) {
                int catchLineNum = this.ts.lineno;
                if (sawDefaultCatch) {
                    reportError("msg.catch.unreachable");
                }
                int catchPos = this.ts.tokenBeg;
                int lp = -1;
                int rp = -1;
                int guardPos = -1;
                if (mustMatchToken(87, "msg.no.paren.catch")) {
                    lp = this.ts.tokenBeg;
                }
                mustMatchToken(39, "msg.bad.catchcond");
                Name varName = createNameNode();
                String varNameString = varName.getIdentifier();
                if (this.inUseStrictDirective && ("eval".equals(varNameString) || "arguments".equals(varNameString))) {
                    reportError("msg.bad.id.strict", varNameString);
                }
                AstNode catchCond = null;
                if (matchToken(112)) {
                    guardPos = this.ts.tokenBeg;
                    catchCond = expr();
                } else {
                    sawDefaultCatch = true;
                }
                if (mustMatchToken(88, "msg.bad.catchcond")) {
                    rp = this.ts.tokenBeg;
                }
                mustMatchToken(85, "msg.no.brace.catchblock");
                Block catchBlock = (Block) statements();
                tryEnd = getNodeEnd(catchBlock);
                CatchClause catchNode = new CatchClause(catchPos);
                catchNode.setVarName(varName);
                catchNode.setCatchCondition(catchCond);
                catchNode.setBody(catchBlock);
                if (guardPos != -1) {
                    catchNode.setIfPosition(guardPos - catchPos);
                }
                catchNode.setParens(lp, rp);
                catchNode.setLineno(catchLineNum);
                if (mustMatchToken(86, "msg.no.brace.after.body")) {
                    tryEnd = this.ts.tokenEnd;
                }
                catchNode.setLength(tryEnd - catchPos);
                if (clauses == null) {
                    clauses = new ArrayList();
                }
                clauses.add(catchNode);
            }
        } else if (peek != 125) {
            mustMatchToken(125, "msg.try.no.catchfinally");
        }
        AstNode finallyBlock = null;
        if (matchToken(125)) {
            finallyPos = this.ts.tokenBeg;
            finallyBlock = statement();
            tryEnd = getNodeEnd(finallyBlock);
        }
        TryStatement tryStatement = new TryStatement(tryPos, tryEnd - tryPos);
        tryStatement.setTryBlock(tryBlock);
        tryStatement.setCatchClauses(clauses);
        tryStatement.setFinallyBlock(finallyBlock);
        if (finallyPos != -1) {
            tryStatement.setFinallyPosition(finallyPos - tryPos);
        }
        tryStatement.setLineno(lineno);
        if (jsdocNode != null) {
            tryStatement.setJsDocNode(jsdocNode);
        }
        return tryStatement;
    }

    private ThrowStatement throwStatement() throws IOException {
        if (this.currentToken != 50) {
            codeBug();
        }
        consumeToken();
        int pos = this.ts.tokenBeg;
        int lineno = this.ts.lineno;
        if (peekTokenOrEOL() == 1) {
            reportError("msg.bad.throw.eol");
        }
        AstNode expr = expr();
        ThrowStatement pn = new ThrowStatement(pos, getNodeEnd(expr), expr);
        pn.setLineno(lineno);
        return pn;
    }

    private LabeledStatement matchJumpLabelName() throws IOException {
        LabeledStatement label = null;
        if (peekTokenOrEOL() == 39) {
            consumeToken();
            if (this.labelSet != null) {
                label = (LabeledStatement) this.labelSet.get(this.ts.getString());
            }
            if (label == null) {
                reportError("msg.undef.label");
            }
        }
        return label;
    }

    private BreakStatement breakStatement() throws IOException {
        if (this.currentToken != 120) {
            codeBug();
        }
        consumeToken();
        int lineno = this.ts.lineno;
        int pos = this.ts.tokenBeg;
        int end = this.ts.tokenEnd;
        Name breakLabel = null;
        if (peekTokenOrEOL() == 39) {
            breakLabel = createNameNode();
            end = getNodeEnd(breakLabel);
        }
        LabeledStatement labels = matchJumpLabelName();
        Jump breakTarget = labels == null ? null : labels.getFirstLabel();
        if (breakTarget == null && breakLabel == null) {
            if (this.loopAndSwitchSet != null && this.loopAndSwitchSet.size() != 0) {
                breakTarget = (Jump) this.loopAndSwitchSet.get(this.loopAndSwitchSet.size() - 1);
            } else if (breakLabel == null) {
                reportError("msg.bad.break", pos, end - pos);
            }
        }
        BreakStatement pn = new BreakStatement(pos, end - pos);
        pn.setBreakLabel(breakLabel);
        if (breakTarget != null) {
            pn.setBreakTarget(breakTarget);
        }
        pn.setLineno(lineno);
        return pn;
    }

    private ContinueStatement continueStatement() throws IOException {
        if (this.currentToken != 121) {
            codeBug();
        }
        consumeToken();
        int lineno = this.ts.lineno;
        int pos = this.ts.tokenBeg;
        int end = this.ts.tokenEnd;
        Name label = null;
        if (peekTokenOrEOL() == 39) {
            label = createNameNode();
            end = getNodeEnd(label);
        }
        LabeledStatement labels = matchJumpLabelName();
        Loop target = null;
        if (labels != null || label != null) {
            if (labels == null || !(labels.getStatement() instanceof Loop)) {
                reportError("msg.continue.nonloop", pos, end - pos);
            }
            target = labels == null ? null : (Loop) labels.getStatement();
        } else if (this.loopSet == null || this.loopSet.size() == 0) {
            reportError("msg.continue.outside");
        } else {
            target = (Loop) this.loopSet.get(this.loopSet.size() - 1);
        }
        ContinueStatement pn = new ContinueStatement(pos, end - pos);
        if (target != null) {
            pn.setTarget(target);
        }
        pn.setLabel(label);
        pn.setLineno(lineno);
        return pn;
    }

    private WithStatement withStatement() throws IOException {
        if (this.currentToken != 123) {
            codeBug();
        }
        consumeToken();
        Comment withComment = getAndResetJsDoc();
        int lineno = this.ts.lineno;
        int pos = this.ts.tokenBeg;
        int lp = -1;
        int rp = -1;
        if (mustMatchToken(87, "msg.no.paren.with")) {
            lp = this.ts.tokenBeg;
        }
        AstNode obj = expr();
        if (mustMatchToken(88, "msg.no.paren.after.with")) {
            rp = this.ts.tokenBeg;
        }
        AstNode body = statement();
        WithStatement pn = new WithStatement(pos, getNodeEnd(body) - pos);
        pn.setJsDocNode(withComment);
        pn.setExpression(obj);
        pn.setStatement(body);
        pn.setParens(lp, rp);
        pn.setLineno(lineno);
        return pn;
    }

    private AstNode letStatement() throws IOException {
        AstNode pn;
        if (this.currentToken != 153) {
            codeBug();
        }
        consumeToken();
        int lineno = this.ts.lineno;
        int pos = this.ts.tokenBeg;
        if (peekToken() == 87) {
            pn = let(true, pos);
        } else {
            pn = variables(153, pos, true);
        }
        pn.setLineno(lineno);
        return pn;
    }

    private static final boolean nowAllSet(int before, int after, int mask) {
        return (before & mask) != mask && (after & mask) == mask;
    }

    private AstNode returnOrYield(int tt, boolean exprContext) throws IOException {
        AstNode ret;
        if (!insideFunction()) {
            reportError(tt == 4 ? "msg.bad.return" : "msg.bad.yield");
        }
        consumeToken();
        int lineno = this.ts.lineno;
        int pos = this.ts.tokenBeg;
        int end = this.ts.tokenEnd;
        AstNode e = null;
        switch (peekTokenOrEOL()) {
            case -1:
            case 0:
            case 1:
            case 72:
            case 82:
            case 84:
            case 86:
            case 88:
                break;
            default:
                e = expr();
                end = getNodeEnd(e);
                break;
        }
        int before = this.endFlags;
        if (tt == 4) {
            int i;
            int i2 = this.endFlags;
            if (e == null) {
                i = 2;
            } else {
                i = 4;
            }
            this.endFlags = i | i2;
            ret = new ReturnStatement(pos, end - pos, e);
            if (nowAllSet(before, this.endFlags, 6)) {
                addStrictWarning("msg.return.inconsistent", "", pos, end - pos);
            }
        } else {
            if (!insideFunction()) {
                reportError("msg.bad.yield");
            }
            this.endFlags |= 8;
            ret = new Yield(pos, end - pos, e);
            setRequiresActivation();
            setIsGenerator();
            if (!exprContext) {
                ret = new ExpressionStatement(ret);
            }
        }
        if (insideFunction() && nowAllSet(before, this.endFlags, 12)) {
            Name name = ((FunctionNode) this.currentScriptOrFn).getFunctionName();
            if (name == null || name.length() == 0) {
                addError("msg.anon.generator.returns", "");
            } else {
                addError("msg.generator.returns", name.getIdentifier());
            }
        }
        ret.setLineno(lineno);
        return ret;
    }

    private AstNode block() throws IOException {
        if (this.currentToken != 85) {
            codeBug();
        }
        consumeToken();
        int pos = this.ts.tokenBeg;
        Scope block = new Scope(pos);
        block.setLineno(this.ts.lineno);
        pushScope(block);
        try {
            statements(block);
            mustMatchToken(86, "msg.no.brace.block");
            block.setLength(this.ts.tokenEnd - pos);
            return block;
        } finally {
            popScope();
        }
    }

    private AstNode defaultXmlNamespace() throws IOException {
        if (this.currentToken != 116) {
            codeBug();
        }
        consumeToken();
        mustHaveXML();
        setRequiresActivation();
        int lineno = this.ts.lineno;
        int pos = this.ts.tokenBeg;
        if (!(matchToken(39) && "xml".equals(this.ts.getString()))) {
            reportError("msg.bad.namespace");
        }
        if (!(matchToken(39) && "namespace".equals(this.ts.getString()))) {
            reportError("msg.bad.namespace");
        }
        if (!matchToken(90)) {
            reportError("msg.bad.namespace");
        }
        AstNode e = expr();
        AstNode dxmln = new UnaryExpression(pos, getNodeEnd(e) - pos);
        dxmln.setOperator(74);
        dxmln.setOperand(e);
        dxmln.setLineno(lineno);
        return new ExpressionStatement(dxmln, true);
    }

    private void recordLabel(Label label, LabeledStatement bundle) throws IOException {
        if (peekToken() != 103) {
            codeBug();
        }
        consumeToken();
        String name = label.getName();
        if (this.labelSet == null) {
            this.labelSet = new HashMap();
        } else {
            LabeledStatement ls = (LabeledStatement) this.labelSet.get(name);
            if (ls != null) {
                if (this.compilerEnv.isIdeMode()) {
                    Label dup = ls.getLabelByName(name);
                    reportError("msg.dup.label", dup.getAbsolutePosition(), dup.getLength());
                }
                reportError("msg.dup.label", label.getPosition(), label.getLength());
            }
        }
        bundle.addLabel(label);
        this.labelSet.put(name, bundle);
    }

    private AstNode nameOrLabel() throws IOException {
        boolean z = true;
        if (this.currentToken != 39) {
            throw codeBug();
        }
        int pos = this.ts.tokenBeg;
        this.currentFlaggedToken |= 131072;
        AstNode expr = expr();
        if (expr.getType() != 130) {
            AstNode n = new ExpressionStatement(expr, !insideFunction());
            n.lineno = expr.lineno;
            return n;
        }
        AstNode bundle = new LabeledStatement(pos);
        recordLabel((Label) expr, bundle);
        bundle.setLineno(this.ts.lineno);
        AstNode stmt = null;
        while (peekToken() == 39) {
            this.currentFlaggedToken |= 131072;
            expr = expr();
            if (expr.getType() != 130) {
                if (insideFunction()) {
                    z = false;
                }
                stmt = new ExpressionStatement(expr, z);
                autoInsertSemicolon(stmt);
            } else {
                recordLabel((Label) expr, bundle);
            }
        }
        try {
            int nodeEnd;
            this.currentLabel = bundle;
            if (stmt == null) {
                stmt = statementHelper();
            }
            this.currentLabel = null;
            for (Label lb : bundle.getLabels()) {
                this.labelSet.remove(lb.getName());
            }
            if (stmt.getParent() == null) {
                nodeEnd = getNodeEnd(stmt) - pos;
            } else {
                nodeEnd = getNodeEnd(stmt);
            }
            bundle.setLength(nodeEnd);
            bundle.setStatement(stmt);
            return bundle;
        } catch (Throwable th) {
            this.currentLabel = null;
            for (Label lb2 : bundle.getLabels()) {
                this.labelSet.remove(lb2.getName());
            }
        }
    }

    private VariableDeclaration variables(int declType, int pos, boolean isStatement) throws IOException {
        int end;
        VariableDeclaration pn = new VariableDeclaration(pos);
        pn.setType(declType);
        pn.setLineno(this.ts.lineno);
        Comment varjsdocNode = getAndResetJsDoc();
        if (varjsdocNode != null) {
            pn.setJsDocNode(varjsdocNode);
        }
        do {
            AstNode destructuring = null;
            Name name = null;
            int tt = peekToken();
            int kidPos = this.ts.tokenBeg;
            end = this.ts.tokenEnd;
            if (tt == 83 || tt == 85) {
                destructuring = destructuringPrimaryExpr();
                end = getNodeEnd(destructuring);
                if (!(destructuring instanceof DestructuringForm)) {
                    reportError("msg.bad.assign.left", kidPos, end - kidPos);
                }
                markDestructuring(destructuring);
            } else {
                mustMatchToken(39, "msg.bad.var");
                name = createNameNode();
                name.setLineno(this.ts.getLineno());
                if (this.inUseStrictDirective) {
                    String id = this.ts.getString();
                    if ("eval".equals(id) || "arguments".equals(this.ts.getString())) {
                        reportError("msg.bad.id.strict", id);
                    }
                }
                defineSymbol(declType, this.ts.getString(), this.inForInit);
            }
            int lineno = this.ts.lineno;
            Comment jsdocNode = getAndResetJsDoc();
            AstNode init = null;
            if (matchToken(90)) {
                init = assignExpr();
                end = getNodeEnd(init);
            }
            VariableInitializer vi = new VariableInitializer(kidPos, end - kidPos);
            if (destructuring != null) {
                if (init == null && !this.inForInit) {
                    reportError("msg.destruct.assign.no.init");
                }
                vi.setTarget(destructuring);
            } else {
                vi.setTarget(name);
            }
            vi.setInitializer(init);
            vi.setType(declType);
            vi.setJsDocNode(jsdocNode);
            vi.setLineno(lineno);
            pn.addVariable(vi);
        } while (matchToken(89));
        pn.setLength(end - pos);
        pn.setIsStatement(isStatement);
        return pn;
    }

    private AstNode let(boolean isStatement, int pos) throws IOException {
        AstNode pn = new LetNode(pos);
        pn.setLineno(this.ts.lineno);
        if (mustMatchToken(87, "msg.no.paren.after.let")) {
            pn.setLp(this.ts.tokenBeg - pos);
        }
        pushScope(pn);
        try {
            pn.setVariables(variables(153, this.ts.tokenBeg, isStatement));
            if (mustMatchToken(88, "msg.no.paren.let")) {
                pn.setRp(this.ts.tokenBeg - pos);
            }
            if (isStatement && peekToken() == 85) {
                consumeToken();
                int beg = this.ts.tokenBeg;
                AstNode stmt = statements();
                mustMatchToken(86, "msg.no.curly.let");
                stmt.setLength(this.ts.tokenEnd - beg);
                pn.setLength(this.ts.tokenEnd - pos);
                pn.setBody(stmt);
                pn.setType(153);
            } else {
                AstNode expr = expr();
                pn.setLength(getNodeEnd(expr) - pos);
                pn.setBody(expr);
                if (isStatement) {
                    AstNode es = new ExpressionStatement(pn, !insideFunction());
                    es.setLineno(pn.getLineno());
                    popScope();
                    return es;
                }
            }
            popScope();
            return pn;
        } catch (Throwable th) {
            popScope();
        }
    }

    /* access modifiers changed from: 0000 */
    public void defineSymbol(int declType, String name) {
        defineSymbol(declType, name, false);
    }

    /* access modifiers changed from: 0000 */
    public void defineSymbol(int declType, String name, boolean ignoreNotInBlock) {
        if (name == null) {
            if (!this.compilerEnv.isIdeMode()) {
                codeBug();
            } else {
                return;
            }
        }
        Scope definingScope = this.currentScope.getDefiningScope(name);
        Symbol symbol = definingScope != null ? definingScope.getSymbol(name) : null;
        int symDeclType = symbol != null ? symbol.getDeclType() : -1;
        if (symbol == null || !(symDeclType == 154 || declType == 154 || (definingScope == this.currentScope && symDeclType == 153))) {
            switch (declType) {
                case 87:
                    if (symbol != null) {
                        addWarning("msg.dup.parms", name);
                    }
                    this.currentScriptOrFn.putSymbol(new Symbol(declType, name));
                    return;
                case 109:
                case 122:
                case 154:
                    if (symbol == null) {
                        this.currentScriptOrFn.putSymbol(new Symbol(declType, name));
                        return;
                    } else if (symDeclType == 122) {
                        addStrictWarning("msg.var.redecl", name);
                        return;
                    } else if (symDeclType == 87) {
                        addStrictWarning("msg.var.hides.arg", name);
                        return;
                    } else {
                        return;
                    }
                case 153:
                    if (ignoreNotInBlock || !(this.currentScope.getType() == 112 || (this.currentScope instanceof Loop))) {
                        this.currentScope.putSymbol(new Symbol(declType, name));
                        return;
                    } else {
                        addError("msg.let.decl.not.in.block");
                        return;
                    }
                default:
                    throw codeBug();
            }
        }
        String str = symDeclType == 154 ? "msg.const.redecl" : symDeclType == 153 ? "msg.let.redecl" : symDeclType == 122 ? "msg.var.redecl" : symDeclType == 109 ? "msg.fn.redecl" : "msg.parm.redecl";
        addError(str, name);
    }

    private AstNode expr() throws IOException {
        AstNode pn = assignExpr();
        int pos = pn.getPosition();
        while (matchToken(89)) {
            int opPos = this.ts.tokenBeg;
            if (this.compilerEnv.isStrictMode() && !pn.hasSideEffects()) {
                addStrictWarning("msg.no.side.effects", "", pos, nodeEnd(pn) - pos);
            }
            if (peekToken() == 72) {
                reportError("msg.yield.parenthesized");
            }
            pn = new InfixExpression(89, pn, assignExpr(), opPos);
        }
        return pn;
    }

    private AstNode assignExpr() throws IOException {
        int tt = peekToken();
        if (tt == 72) {
            return returnOrYield(tt, true);
        }
        AstNode pn = condExpr();
        tt = peekToken();
        if (90 <= tt && tt <= 101) {
            consumeToken();
            Comment jsdocNode = getAndResetJsDoc();
            markDestructuring(pn);
            AstNode pn2 = new Assignment(tt, pn, assignExpr(), this.ts.tokenBeg);
            if (jsdocNode != null) {
                pn2.setJsDocNode(jsdocNode);
            }
            return pn2;
        } else if (tt != 82 || this.currentJsDocComment == null) {
            return pn;
        } else {
            pn.setJsDocNode(getAndResetJsDoc());
            return pn;
        }
    }

    private AstNode condExpr() throws IOException {
        AstNode pn = orExpr();
        if (!matchToken(102)) {
            return pn;
        }
        int line = this.ts.lineno;
        int qmarkPos = this.ts.tokenBeg;
        int colonPos = -1;
        boolean wasInForInit = this.inForInit;
        this.inForInit = false;
        try {
            AstNode ifTrue = assignExpr();
            if (mustMatchToken(103, "msg.no.colon.cond")) {
                colonPos = this.ts.tokenBeg;
            }
            AstNode ifFalse = assignExpr();
            int beg = pn.getPosition();
            AstNode ce = new ConditionalExpression(beg, getNodeEnd(ifFalse) - beg);
            ce.setLineno(line);
            ce.setTestExpression(pn);
            ce.setTrueExpression(ifTrue);
            ce.setFalseExpression(ifFalse);
            ce.setQuestionMarkPosition(qmarkPos - beg);
            ce.setColonPosition(colonPos - beg);
            return ce;
        } finally {
            this.inForInit = wasInForInit;
        }
    }

    private AstNode orExpr() throws IOException {
        AstNode pn = andExpr();
        if (!matchToken(104)) {
            return pn;
        }
        return new InfixExpression(104, pn, orExpr(), this.ts.tokenBeg);
    }

    private AstNode andExpr() throws IOException {
        AstNode pn = bitOrExpr();
        if (!matchToken(105)) {
            return pn;
        }
        return new InfixExpression(105, pn, andExpr(), this.ts.tokenBeg);
    }

    private AstNode bitOrExpr() throws IOException {
        AstNode pn = bitXorExpr();
        while (matchToken(9)) {
            pn = new InfixExpression(9, pn, bitXorExpr(), this.ts.tokenBeg);
        }
        return pn;
    }

    private AstNode bitXorExpr() throws IOException {
        AstNode pn = bitAndExpr();
        while (matchToken(10)) {
            pn = new InfixExpression(10, pn, bitAndExpr(), this.ts.tokenBeg);
        }
        return pn;
    }

    private AstNode bitAndExpr() throws IOException {
        AstNode pn = eqExpr();
        while (matchToken(11)) {
            pn = new InfixExpression(11, pn, eqExpr(), this.ts.tokenBeg);
        }
        return pn;
    }

    private AstNode eqExpr() throws IOException {
        AstNode pn = relExpr();
        while (true) {
            int tt = peekToken();
            int opPos = this.ts.tokenBeg;
            switch (tt) {
                case 12:
                case 13:
                case 46:
                case 47:
                    consumeToken();
                    int parseToken = tt;
                    if (this.compilerEnv.getLanguageVersion() == 120) {
                        if (tt == 12) {
                            parseToken = 46;
                        } else if (tt == 13) {
                            parseToken = 47;
                        }
                    }
                    pn = new InfixExpression(parseToken, pn, relExpr(), opPos);
                default:
                    return pn;
            }
        }
    }

    private AstNode relExpr() throws IOException {
        AstNode pn = shiftExpr();
        while (true) {
            int tt = peekToken();
            int opPos = this.ts.tokenBeg;
            switch (tt) {
                case 14:
                case 15:
                case 16:
                case 17:
                case 53:
                    break;
                case 52:
                    if (this.inForInit) {
                        break;
                    }
                    continue;
                default:
                    break;
            }
            consumeToken();
            pn = new InfixExpression(tt, pn, shiftExpr(), opPos);
        }
        return pn;
    }

    private AstNode shiftExpr() throws IOException {
        AstNode pn = addExpr();
        while (true) {
            int tt = peekToken();
            int opPos = this.ts.tokenBeg;
            switch (tt) {
                case 18:
                case 19:
                case 20:
                    consumeToken();
                    pn = new InfixExpression(tt, pn, addExpr(), opPos);
                default:
                    return pn;
            }
        }
    }

    private AstNode addExpr() throws IOException {
        AstNode pn = mulExpr();
        while (true) {
            int tt = peekToken();
            int opPos = this.ts.tokenBeg;
            if (tt != 21 && tt != 22) {
                return pn;
            }
            consumeToken();
            pn = new InfixExpression(tt, pn, mulExpr(), opPos);
        }
    }

    private AstNode mulExpr() throws IOException {
        AstNode pn = unaryExpr();
        while (true) {
            int tt = peekToken();
            int opPos = this.ts.tokenBeg;
            switch (tt) {
                case 23:
                case 24:
                case 25:
                    consumeToken();
                    pn = new InfixExpression(tt, pn, unaryExpr(), opPos);
                default:
                    return pn;
            }
        }
    }

    private AstNode unaryExpr() throws IOException {
        int tt = peekToken();
        int line = this.ts.lineno;
        AstNode node;
        switch (tt) {
            case -1:
                consumeToken();
                return makeErrorNode();
            case 14:
                if (this.compilerEnv.isXmlAvailable()) {
                    consumeToken();
                    return memberExprTail(true, xmlInitializer());
                }
                break;
            case 21:
                consumeToken();
                node = new UnaryExpression(28, this.ts.tokenBeg, unaryExpr());
                node.setLineno(line);
                return node;
            case 22:
                consumeToken();
                node = new UnaryExpression(29, this.ts.tokenBeg, unaryExpr());
                node.setLineno(line);
                return node;
            case 26:
            case 27:
            case 32:
            case 126:
                consumeToken();
                node = new UnaryExpression(tt, this.ts.tokenBeg, unaryExpr());
                node.setLineno(line);
                return node;
            case 31:
                consumeToken();
                node = new UnaryExpression(tt, this.ts.tokenBeg, unaryExpr());
                node.setLineno(line);
                return node;
            case 106:
            case 107:
                consumeToken();
                AstNode expr = new UnaryExpression(tt, this.ts.tokenBeg, memberExpr(true));
                expr.setLineno(line);
                checkBadIncDec(expr);
                return expr;
        }
        AstNode pn = memberExpr(true);
        tt = peekTokenOrEOL();
        if (tt != 106 && tt != 107) {
            return pn;
        }
        consumeToken();
        AstNode uexpr = new UnaryExpression(tt, this.ts.tokenBeg, pn, true);
        uexpr.setLineno(line);
        checkBadIncDec(uexpr);
        return uexpr;
    }

    private AstNode xmlInitializer() throws IOException {
        if (this.currentToken != 14) {
            codeBug();
        }
        int pos = this.ts.tokenBeg;
        int tt = this.ts.getFirstXMLToken();
        if (tt == 145 || tt == 148) {
            AstNode pn = new XmlLiteral(pos);
            pn.setLineno(this.ts.lineno);
            while (true) {
                switch (tt) {
                    case 145:
                        AstNode expr;
                        pn.addFragment(new XmlString(this.ts.tokenBeg, this.ts.getString()));
                        mustMatchToken(85, "msg.syntax");
                        int beg = this.ts.tokenBeg;
                        if (peekToken() == 86) {
                            expr = new EmptyExpression(beg, this.ts.tokenEnd - beg);
                        } else {
                            expr = expr();
                        }
                        mustMatchToken(86, "msg.syntax");
                        XmlExpression xexpr = new XmlExpression(beg, expr);
                        xexpr.setIsXmlAttribute(this.ts.isXMLAttribute());
                        xexpr.setLength(this.ts.tokenEnd - beg);
                        pn.addFragment(xexpr);
                        tt = this.ts.getNextXMLToken();
                    case 148:
                        pn.addFragment(new XmlString(this.ts.tokenBeg, this.ts.getString()));
                        return pn;
                    default:
                        reportError("msg.syntax");
                        return makeErrorNode();
                }
            }
        }
        reportError("msg.syntax");
        return makeErrorNode();
    }

    private List<AstNode> argumentList() throws IOException {
        if (matchToken(88)) {
            return null;
        }
        List<AstNode> result = new ArrayList();
        boolean wasInForInit = this.inForInit;
        this.inForInit = false;
        while (true) {
            try {
                if (peekToken() == 72) {
                    reportError("msg.yield.parenthesized");
                }
                AstNode en = assignExpr();
                if (peekToken() == 119) {
                    try {
                        result.add(generatorExpression(en, 0, true));
                    } catch (IOException e) {
                    }
                } else {
                    result.add(en);
                }
                if (!matchToken(89)) {
                    break;
                }
            } finally {
                this.inForInit = wasInForInit;
            }
        }
        mustMatchToken(88, "msg.no.paren.arg");
        return result;
    }

    private AstNode memberExpr(boolean allowCallSyntax) throws IOException {
        AstNode pn;
        int tt = peekToken();
        int lineno = this.ts.lineno;
        if (tt != 30) {
            pn = primaryExpr();
        } else {
            consumeToken();
            int pos = this.ts.tokenBeg;
            AstNode nx = new NewExpression(pos);
            AstNode target = memberExpr(false);
            int end = getNodeEnd(target);
            nx.setTarget(target);
            if (matchToken(87)) {
                int lp = this.ts.tokenBeg;
                List<AstNode> args = argumentList();
                if (args != null && args.size() > 65536) {
                    reportError("msg.too.many.constructor.args");
                }
                int rp = this.ts.tokenBeg;
                end = this.ts.tokenEnd;
                if (args != null) {
                    nx.setArguments(args);
                }
                nx.setParens(lp - pos, rp - pos);
            }
            if (matchToken(85)) {
                ObjectLiteral initializer = objectLiteral();
                end = getNodeEnd(initializer);
                nx.setInitializer(initializer);
            }
            nx.setLength(end - pos);
            pn = nx;
        }
        pn.setLineno(lineno);
        return memberExprTail(allowCallSyntax, pn);
    }

    private AstNode memberExprTail(boolean allowCallSyntax, AstNode pn) throws IOException {
        if (pn == null) {
            codeBug();
        }
        int pos = pn.getPosition();
        while (true) {
            int tt = peekToken();
            int lineno;
            int end;
            switch (tt) {
                case 83:
                    consumeToken();
                    int lb = this.ts.tokenBeg;
                    int rb = -1;
                    lineno = this.ts.lineno;
                    AstNode expr = expr();
                    end = getNodeEnd(expr);
                    if (mustMatchToken(84, "msg.no.bracket.index")) {
                        rb = this.ts.tokenBeg;
                        end = this.ts.tokenEnd;
                    }
                    AstNode g = new ElementGet(pos, end - pos);
                    g.setTarget(pn);
                    g.setElement(expr);
                    g.setParens(lb, rb);
                    g.setLineno(lineno);
                    pn = g;
                    continue;
                case 87:
                    if (!allowCallSyntax) {
                        break;
                    }
                    lineno = this.ts.lineno;
                    consumeToken();
                    checkCallRequiresActivation(pn);
                    AstNode f = new FunctionCall(pos);
                    f.setTarget(pn);
                    f.setLineno(lineno);
                    f.setLp(this.ts.tokenBeg - pos);
                    List<AstNode> args = argumentList();
                    if (args != null && args.size() > 65536) {
                        reportError("msg.too.many.function.args");
                    }
                    f.setArguments(args);
                    f.setRp(this.ts.tokenBeg - pos);
                    f.setLength(this.ts.tokenEnd - pos);
                    pn = f;
                    continue;
                case 108:
                case 143:
                    lineno = this.ts.lineno;
                    pn = propertyAccess(tt, pn);
                    pn.setLineno(lineno);
                    continue;
                case 146:
                    consumeToken();
                    int opPos = this.ts.tokenBeg;
                    int rp = -1;
                    lineno = this.ts.lineno;
                    mustHaveXML();
                    setRequiresActivation();
                    AstNode filter = expr();
                    end = getNodeEnd(filter);
                    if (mustMatchToken(88, "msg.no.paren")) {
                        rp = this.ts.tokenBeg;
                        end = this.ts.tokenEnd;
                    }
                    AstNode q = new XmlDotQuery(pos, end - pos);
                    q.setLeft(pn);
                    q.setRight(filter);
                    q.setOperatorPosition(opPos);
                    q.setRp(rp - pos);
                    q.setLineno(lineno);
                    pn = q;
                    continue;
                default:
                    break;
            }
        }
        return pn;
    }

    private AstNode propertyAccess(int tt, AstNode pn) throws IOException {
        if (pn == null) {
            codeBug();
        }
        int memberTypeFlags = 0;
        int lineno = this.ts.lineno;
        int dotPos = this.ts.tokenBeg;
        consumeToken();
        if (tt == 143) {
            mustHaveXML();
            memberTypeFlags = 4;
        }
        if (this.compilerEnv.isXmlAvailable()) {
            AstNode ref;
            InfixExpression result;
            int token = nextToken();
            switch (token) {
                case 23:
                    saveNameTokenData(this.ts.tokenBeg, "*", this.ts.lineno);
                    ref = propertyName(-1, "*", memberTypeFlags);
                    break;
                case 39:
                    ref = propertyName(-1, this.ts.getString(), memberTypeFlags);
                    break;
                case 50:
                    saveNameTokenData(this.ts.tokenBeg, "throw", this.ts.lineno);
                    ref = propertyName(-1, "throw", memberTypeFlags);
                    break;
                case 147:
                    ref = attributeAccess();
                    break;
                default:
                    if (this.compilerEnv.isReservedKeywordAsIdentifier()) {
                        String name = Token.keywordToName(token);
                        if (name != null) {
                            saveNameTokenData(this.ts.tokenBeg, name, this.ts.lineno);
                            ref = propertyName(-1, name, memberTypeFlags);
                            break;
                        }
                    }
                    reportError("msg.no.name.after.dot");
                    return makeErrorNode();
            }
            boolean xml = ref instanceof XmlRef;
            if (xml) {
                result = new XmlMemberGet();
            } else {
                result = new PropertyGet();
            }
            if (xml && tt == 108) {
                result.setType(108);
            }
            int pos = pn.getPosition();
            result.setPosition(pos);
            result.setLength(getNodeEnd(ref) - pos);
            result.setOperatorPosition(dotPos - pos);
            result.setLineno(pn.getLineno());
            result.setLeft(pn);
            result.setRight(ref);
            return result;
        }
        if (!(nextToken() == 39 || (this.compilerEnv.isReservedKeywordAsIdentifier() && TokenStream.isKeyword(this.ts.getString())))) {
            reportError("msg.no.name.after.dot");
        }
        PropertyGet pg = new PropertyGet(pn, createNameNode(true, 33), dotPos);
        pg.setLineno(lineno);
        return pg;
    }

    private AstNode attributeAccess() throws IOException {
        int tt = nextToken();
        int atPos = this.ts.tokenBeg;
        switch (tt) {
            case 23:
                saveNameTokenData(this.ts.tokenBeg, "*", this.ts.lineno);
                return propertyName(atPos, "*", 0);
            case 39:
                return propertyName(atPos, this.ts.getString(), 0);
            case 83:
                return xmlElemRef(atPos, null, -1);
            default:
                reportError("msg.no.name.after.xmlAttr");
                return makeErrorNode();
        }
    }

    private AstNode propertyName(int atPos, String s, int memberTypeFlags) throws IOException {
        int pos;
        if (atPos != -1) {
            pos = atPos;
        } else {
            pos = this.ts.tokenBeg;
        }
        int lineno = this.ts.lineno;
        int colonPos = -1;
        AstNode name = createNameNode(true, this.currentToken);
        Name ns = null;
        if (matchToken(144)) {
            ns = name;
            colonPos = this.ts.tokenBeg;
            switch (nextToken()) {
                case 23:
                    saveNameTokenData(this.ts.tokenBeg, "*", this.ts.lineno);
                    name = createNameNode(false, -1);
                    break;
                case 39:
                    name = createNameNode();
                    break;
                case 83:
                    return xmlElemRef(atPos, ns, colonPos);
                default:
                    reportError("msg.no.name.after.coloncolon");
                    return makeErrorNode();
            }
        }
        if (ns == null && memberTypeFlags == 0 && atPos == -1) {
            return name;
        }
        AstNode ref = new XmlPropRef(pos, getNodeEnd(name) - pos);
        ref.setAtPos(atPos);
        ref.setNamespace(ns);
        ref.setColonPos(colonPos);
        ref.setPropName(name);
        ref.setLineno(lineno);
        return ref;
    }

    private XmlElemRef xmlElemRef(int atPos, Name namespace, int colonPos) throws IOException {
        int pos;
        int lb = this.ts.tokenBeg;
        int rb = -1;
        if (atPos != -1) {
            pos = atPos;
        } else {
            pos = lb;
        }
        AstNode expr = expr();
        int end = getNodeEnd(expr);
        if (mustMatchToken(84, "msg.no.bracket.index")) {
            rb = this.ts.tokenBeg;
            end = this.ts.tokenEnd;
        }
        XmlElemRef ref = new XmlElemRef(pos, end - pos);
        ref.setNamespace(namespace);
        ref.setColonPos(colonPos);
        ref.setAtPos(atPos);
        ref.setExpression(expr);
        ref.setBrackets(lb, rb);
        return ref;
    }

    private AstNode destructuringPrimaryExpr() throws IOException, ParserException {
        try {
            this.inDestructuringAssignment = true;
            AstNode primaryExpr = primaryExpr();
            return primaryExpr;
        } finally {
            this.inDestructuringAssignment = false;
        }
    }

    private AstNode primaryExpr() throws IOException {
        int ttFlagged = nextFlaggedToken();
        int tt = ttFlagged & CLEAR_TI_MASK;
        int pos;
        switch (tt) {
            case -1:
                break;
            case 0:
                reportError("msg.unexpected.eof");
                break;
            case 24:
            case 100:
                this.ts.readRegExp(tt);
                pos = this.ts.tokenBeg;
                AstNode re = new RegExpLiteral(pos, this.ts.tokenEnd - pos);
                re.setValue(this.ts.getString());
                re.setFlags(this.ts.readAndClearRegExpFlags());
                return re;
            case 39:
                return name(ttFlagged, tt);
            case 40:
                String s = this.ts.getString();
                if (this.inUseStrictDirective && this.ts.isNumberOctal()) {
                    reportError("msg.no.octal.strict");
                }
                if (this.ts.isNumberOctal()) {
                    s = "0" + s;
                }
                if (this.ts.isNumberHex()) {
                    s = "0x" + s;
                }
                return new NumberLiteral(this.ts.tokenBeg, s, this.ts.getNumber());
            case 41:
                return createStringLiteral();
            case 42:
            case 43:
            case 44:
            case 45:
                pos = this.ts.tokenBeg;
                return new KeywordLiteral(pos, this.ts.tokenEnd - pos, tt);
            case 83:
                return arrayLiteral();
            case 85:
                return objectLiteral();
            case 87:
                return parenExpr();
            case 109:
                return function(2);
            case 127:
                reportError("msg.reserved.id");
                break;
            case 147:
                mustHaveXML();
                return attributeAccess();
            case 153:
                return let(false, this.ts.tokenBeg);
            default:
                reportError("msg.syntax");
                break;
        }
        return makeErrorNode();
    }

    private AstNode parenExpr() throws IOException {
        boolean wasInForInit = this.inForInit;
        this.inForInit = false;
        try {
            AstNode pn;
            Comment jsdocNode = getAndResetJsDoc();
            int lineno = this.ts.lineno;
            int begin = this.ts.tokenBeg;
            AstNode e = expr();
            if (peekToken() == 119) {
                pn = generatorExpression(e, begin);
            } else {
                pn = new ParenthesizedExpression(e);
                if (jsdocNode == null) {
                    jsdocNode = getAndResetJsDoc();
                }
                if (jsdocNode != null) {
                    pn.setJsDocNode(jsdocNode);
                }
                mustMatchToken(88, "msg.no.paren");
                pn.setLength(this.ts.tokenEnd - pn.getPosition());
                pn.setLineno(lineno);
                this.inForInit = wasInForInit;
            }
            return pn;
        } finally {
            this.inForInit = wasInForInit;
        }
    }

    private AstNode name(int ttFlagged, int tt) throws IOException {
        String nameString = this.ts.getString();
        int namePos = this.ts.tokenBeg;
        int nameLineno = this.ts.lineno;
        if ((131072 & ttFlagged) == 0 || peekToken() != 103) {
            saveNameTokenData(namePos, nameString, nameLineno);
            if (this.compilerEnv.isXmlAvailable()) {
                return propertyName(-1, nameString, 0);
            }
            return createNameNode(true, 39);
        }
        Label label = new Label(namePos, this.ts.tokenEnd - namePos);
        label.setName(nameString);
        label.setLineno(this.ts.lineno);
        return label;
    }

    private AstNode arrayLiteral() throws IOException {
        int i = 1;
        if (this.currentToken != 83) {
            codeBug();
        }
        int pos = this.ts.tokenBeg;
        int end = this.ts.tokenEnd;
        List<AstNode> elements = new ArrayList();
        ArrayLiteral pn = new ArrayLiteral(pos);
        boolean after_lb_or_comma = true;
        int afterComma = -1;
        int skipCount = 0;
        while (true) {
            int tt = peekToken();
            if (tt == 89) {
                consumeToken();
                afterComma = this.ts.tokenEnd;
                if (after_lb_or_comma) {
                    elements.add(new EmptyExpression(this.ts.tokenBeg, 1));
                    skipCount++;
                } else {
                    after_lb_or_comma = true;
                }
            } else if (tt == 84) {
                consumeToken();
                end = this.ts.tokenEnd;
                int size = elements.size();
                if (!after_lb_or_comma) {
                    i = 0;
                }
                pn.setDestructuringLength(i + size);
                pn.setSkipCount(skipCount);
                if (afterComma != -1) {
                    warnTrailingComma(pos, elements, afterComma);
                }
            } else if (tt == 119 && !after_lb_or_comma && elements.size() == 1) {
                return arrayComprehension((AstNode) elements.get(0), pos);
            } else {
                if (tt == 0) {
                    reportError("msg.no.bracket.arg");
                    break;
                }
                if (!after_lb_or_comma) {
                    reportError("msg.no.bracket.arg");
                }
                elements.add(assignExpr());
                after_lb_or_comma = false;
                afterComma = -1;
            }
        }
        for (AstNode e : elements) {
            pn.addElement(e);
        }
        pn.setLength(end - pos);
        return pn;
    }

    private AstNode arrayComprehension(AstNode result, int pos) throws IOException {
        List<ArrayComprehensionLoop> loops = new ArrayList();
        while (peekToken() == 119) {
            loops.add(arrayComprehensionLoop());
        }
        int ifPos = -1;
        ConditionData data = null;
        if (peekToken() == 112) {
            consumeToken();
            ifPos = this.ts.tokenBeg - pos;
            data = condition();
        }
        mustMatchToken(84, "msg.no.bracket.arg");
        ArrayComprehension pn = new ArrayComprehension(pos, this.ts.tokenEnd - pos);
        pn.setResult(result);
        pn.setLoops(loops);
        if (data != null) {
            pn.setIfPosition(ifPos);
            pn.setFilter(data.condition);
            pn.setFilterLp(data.lp - pos);
            pn.setFilterRp(data.rp - pos);
        }
        return pn;
    }

    private ArrayComprehensionLoop arrayComprehensionLoop() throws IOException {
        boolean z = true;
        if (nextToken() != 119) {
            codeBug();
        }
        int pos = this.ts.tokenBeg;
        int eachPos = -1;
        int lp = -1;
        int rp = -1;
        int inPos = -1;
        ArrayComprehensionLoop pn = new ArrayComprehensionLoop(pos);
        pushScope(pn);
        try {
            if (matchToken(39)) {
                if (this.ts.getString().equals("each")) {
                    eachPos = this.ts.tokenBeg - pos;
                } else {
                    reportError("msg.no.paren.for");
                }
            }
            if (mustMatchToken(87, "msg.no.paren.for")) {
                lp = this.ts.tokenBeg - pos;
            }
            AstNode iter = null;
            switch (peekToken()) {
                case 39:
                    consumeToken();
                    iter = createNameNode();
                    break;
                case 83:
                case 85:
                    iter = destructuringPrimaryExpr();
                    markDestructuring(iter);
                    break;
                default:
                    reportError("msg.bad.var");
                    break;
            }
            if (iter.getType() == 39) {
                defineSymbol(153, this.ts.getString(), true);
            }
            if (mustMatchToken(52, "msg.in.after.for.name")) {
                inPos = this.ts.tokenBeg - pos;
            }
            AstNode obj = expr();
            if (mustMatchToken(88, "msg.no.paren.for.ctrl")) {
                rp = this.ts.tokenBeg - pos;
            }
            pn.setLength(this.ts.tokenEnd - pos);
            pn.setIterator(iter);
            pn.setIteratedObject(obj);
            pn.setInPosition(inPos);
            pn.setEachPosition(eachPos);
            if (eachPos == -1) {
                z = false;
            }
            pn.setIsForEach(z);
            pn.setParens(lp, rp);
            return pn;
        } finally {
            popScope();
        }
    }

    private AstNode generatorExpression(AstNode result, int pos) throws IOException {
        return generatorExpression(result, pos, false);
    }

    private AstNode generatorExpression(AstNode result, int pos, boolean inFunctionParams) throws IOException {
        List<GeneratorExpressionLoop> loops = new ArrayList();
        while (peekToken() == 119) {
            loops.add(generatorExpressionLoop());
        }
        int ifPos = -1;
        ConditionData data = null;
        if (peekToken() == 112) {
            consumeToken();
            ifPos = this.ts.tokenBeg - pos;
            data = condition();
        }
        if (!inFunctionParams) {
            mustMatchToken(88, "msg.no.paren.let");
        }
        GeneratorExpression pn = new GeneratorExpression(pos, this.ts.tokenEnd - pos);
        pn.setResult(result);
        pn.setLoops(loops);
        if (data != null) {
            pn.setIfPosition(ifPos);
            pn.setFilter(data.condition);
            pn.setFilterLp(data.lp - pos);
            pn.setFilterRp(data.rp - pos);
        }
        return pn;
    }

    private GeneratorExpressionLoop generatorExpressionLoop() throws IOException {
        if (nextToken() != 119) {
            codeBug();
        }
        int pos = this.ts.tokenBeg;
        int lp = -1;
        int rp = -1;
        int inPos = -1;
        GeneratorExpressionLoop pn = new GeneratorExpressionLoop(pos);
        pushScope(pn);
        try {
            if (mustMatchToken(87, "msg.no.paren.for")) {
                lp = this.ts.tokenBeg - pos;
            }
            AstNode iter = null;
            switch (peekToken()) {
                case 39:
                    consumeToken();
                    iter = createNameNode();
                    break;
                case 83:
                case 85:
                    iter = destructuringPrimaryExpr();
                    markDestructuring(iter);
                    break;
                default:
                    reportError("msg.bad.var");
                    break;
            }
            if (iter.getType() == 39) {
                defineSymbol(153, this.ts.getString(), true);
            }
            if (mustMatchToken(52, "msg.in.after.for.name")) {
                inPos = this.ts.tokenBeg - pos;
            }
            AstNode obj = expr();
            if (mustMatchToken(88, "msg.no.paren.for.ctrl")) {
                rp = this.ts.tokenBeg - pos;
            }
            pn.setLength(this.ts.tokenEnd - pos);
            pn.setIterator(iter);
            pn.setIteratedObject(obj);
            pn.setInPosition(inPos);
            pn.setParens(lp, rp);
            return pn;
        } finally {
            popScope();
        }
    }

    private ObjectLiteral objectLiteral() throws IOException {
        int pos = this.ts.tokenBeg;
        int lineno = this.ts.lineno;
        int afterComma = -1;
        List<ObjectProperty> elems = new ArrayList();
        Set<String> getterNames = null;
        Set<String> setterNames = null;
        if (this.inUseStrictDirective) {
            getterNames = new HashSet();
            setterNames = new HashSet();
        }
        Comment objJsdocNode = getAndResetJsDoc();
        while (true) {
            String propertyName;
            int entryKind = 1;
            int tt = peekToken();
            Comment jsdocNode = getAndResetJsDoc();
            AstNode pname;
            switch (tt) {
                case 39:
                    Name name = createNameNode();
                    propertyName = this.ts.getString();
                    int ppos = this.ts.tokenBeg;
                    consumeToken();
                    int peeked = peekToken();
                    boolean maybeGetterOrSetter = "get".equals(propertyName) || "set".equals(propertyName);
                    if (maybeGetterOrSetter && peeked != 89 && peeked != 103 && peeked != 86) {
                        boolean isGet = "get".equals(propertyName);
                        entryKind = isGet ? 2 : 4;
                        pname = objliteralProperty();
                        if (pname != null) {
                            propertyName = this.ts.getString();
                            ObjectProperty objectProp = getterSetterProperty(ppos, pname, isGet);
                            pname.setJsDocNode(jsdocNode);
                            elems.add(objectProp);
                            break;
                        }
                        propertyName = null;
                        break;
                    }
                    name.setJsDocNode(jsdocNode);
                    elems.add(plainProperty(name, tt));
                    break;
                    break;
                case 86:
                    if (afterComma != -1) {
                        warnTrailingComma(pos, elems, afterComma);
                        break;
                    }
                    break;
                default:
                    pname = objliteralProperty();
                    if (pname != null) {
                        propertyName = this.ts.getString();
                        pname.setJsDocNode(jsdocNode);
                        elems.add(plainProperty(pname, tt));
                        break;
                    }
                    propertyName = null;
                    break;
            }
            if (this.inUseStrictDirective && propertyName != null) {
                switch (entryKind) {
                    case 1:
                        if (getterNames.contains(propertyName) || setterNames.contains(propertyName)) {
                            addError("msg.dup.obj.lit.prop.strict", propertyName);
                        }
                        getterNames.add(propertyName);
                        setterNames.add(propertyName);
                        break;
                    case 2:
                        if (getterNames.contains(propertyName)) {
                            addError("msg.dup.obj.lit.prop.strict", propertyName);
                        }
                        getterNames.add(propertyName);
                        break;
                    case 4:
                        if (setterNames.contains(propertyName)) {
                            addError("msg.dup.obj.lit.prop.strict", propertyName);
                        }
                        setterNames.add(propertyName);
                        break;
                }
            }
            getAndResetJsDoc();
            if (matchToken(89)) {
                afterComma = this.ts.tokenEnd;
            }
        }
        mustMatchToken(86, "msg.no.brace.prop");
        ObjectLiteral pn = new ObjectLiteral(pos, this.ts.tokenEnd - pos);
        if (objJsdocNode != null) {
            pn.setJsDocNode(objJsdocNode);
        }
        pn.setElements(elems);
        pn.setLineno(lineno);
        return pn;
    }

    private AstNode objliteralProperty() throws IOException {
        AstNode pname;
        switch (peekToken()) {
            case 39:
                pname = createNameNode();
                break;
            case 40:
                pname = new NumberLiteral(this.ts.tokenBeg, this.ts.getString(), this.ts.getNumber());
                break;
            case 41:
                pname = createStringLiteral();
                break;
            default:
                if (this.compilerEnv.isReservedKeywordAsIdentifier() && TokenStream.isKeyword(this.ts.getString())) {
                    pname = createNameNode();
                    break;
                }
                reportError("msg.bad.prop");
                return null;
        }
        consumeToken();
        return pname;
    }

    private ObjectProperty plainProperty(AstNode property, int ptt) throws IOException {
        int tt = peekToken();
        ObjectProperty pn;
        if ((tt == 89 || tt == 86) && ptt == 39 && this.compilerEnv.getLanguageVersion() >= 180) {
            if (!this.inDestructuringAssignment) {
                reportError("msg.bad.object.init");
            }
            AstNode nn = new Name(property.getPosition(), property.getString());
            pn = new ObjectProperty();
            pn.putProp(26, Boolean.TRUE);
            pn.setLeftAndRight(property, nn);
            return pn;
        }
        mustMatchToken(103, "msg.no.colon.prop");
        pn = new ObjectProperty();
        pn.setOperatorPosition(this.ts.tokenBeg);
        pn.setLeftAndRight(property, assignExpr());
        return pn;
    }

    private ObjectProperty getterSetterProperty(int pos, AstNode propName, boolean isGetter) throws IOException {
        FunctionNode fn = function(2);
        Name name = fn.getFunctionName();
        if (!(name == null || name.length() == 0)) {
            reportError("msg.bad.prop");
        }
        ObjectProperty pn = new ObjectProperty(pos);
        if (isGetter) {
            pn.setIsGetter();
            fn.setFunctionIsGetter();
        } else {
            pn.setIsSetter();
            fn.setFunctionIsSetter();
        }
        int end = getNodeEnd(fn);
        pn.setLeft(propName);
        pn.setRight(fn);
        pn.setLength(end - pos);
        return pn;
    }

    private Name createNameNode() {
        return createNameNode(false, 39);
    }

    private Name createNameNode(boolean checkActivation, int token) {
        int beg = this.ts.tokenBeg;
        String s = this.ts.getString();
        int lineno = this.ts.lineno;
        if (!"".equals(this.prevNameTokenString)) {
            beg = this.prevNameTokenStart;
            s = this.prevNameTokenString;
            lineno = this.prevNameTokenLineno;
            this.prevNameTokenStart = 0;
            this.prevNameTokenString = "";
            this.prevNameTokenLineno = 0;
        }
        if (s == null) {
            if (this.compilerEnv.isIdeMode()) {
                s = "";
            } else {
                codeBug();
            }
        }
        Name name = new Name(beg, s);
        name.setLineno(lineno);
        if (checkActivation) {
            checkActivationName(s, token);
        }
        return name;
    }

    private StringLiteral createStringLiteral() {
        int pos = this.ts.tokenBeg;
        StringLiteral s = new StringLiteral(pos, this.ts.tokenEnd - pos);
        s.setLineno(this.ts.lineno);
        s.setValue(this.ts.getString());
        s.setQuoteCharacter(this.ts.getQuoteChar());
        return s;
    }

    /* access modifiers changed from: protected */
    public void checkActivationName(String name, int token) {
        if (insideFunction()) {
            boolean activation = false;
            if ("arguments".equals(name) || (this.compilerEnv.getActivationNames() != null && this.compilerEnv.getActivationNames().contains(name))) {
                activation = true;
            } else if ("length".equals(name) && token == 33 && this.compilerEnv.getLanguageVersion() == 120) {
                activation = true;
            }
            if (activation) {
                setRequiresActivation();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setRequiresActivation() {
        if (insideFunction()) {
            ((FunctionNode) this.currentScriptOrFn).setRequiresActivation();
        }
    }

    private void checkCallRequiresActivation(AstNode pn) {
        if ((pn.getType() == 39 && "eval".equals(((Name) pn).getIdentifier())) || (pn.getType() == 33 && "eval".equals(((PropertyGet) pn).getProperty().getIdentifier()))) {
            setRequiresActivation();
        }
    }

    /* access modifiers changed from: protected */
    public void setIsGenerator() {
        if (insideFunction()) {
            ((FunctionNode) this.currentScriptOrFn).setIsGenerator();
        }
    }

    private void checkBadIncDec(UnaryExpression expr) {
        int tt = removeParens(expr.getOperand()).getType();
        if (tt != 39 && tt != 33 && tt != 36 && tt != 67 && tt != 38) {
            reportError(expr.getType() == 106 ? "msg.bad.incr" : "msg.bad.decr");
        }
    }

    private ErrorNode makeErrorNode() {
        ErrorNode pn = new ErrorNode(this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
        pn.setLineno(this.ts.lineno);
        return pn;
    }

    private int nodeEnd(AstNode node) {
        return node.getPosition() + node.getLength();
    }

    private void saveNameTokenData(int pos, String name, int lineno) {
        this.prevNameTokenStart = pos;
        this.prevNameTokenString = name;
        this.prevNameTokenLineno = lineno;
    }

    private int lineBeginningFor(int pos) {
        if (this.sourceChars == null) {
            return -1;
        }
        if (pos <= 0) {
            return 0;
        }
        char[] buf = this.sourceChars;
        if (pos >= buf.length) {
            pos = buf.length - 1;
        }
        do {
            pos--;
            if (pos < 0) {
                return 0;
            }
        } while (!ScriptRuntime.isJSLineTerminator(buf[pos]));
        return pos + 1;
    }

    private void warnMissingSemi(int pos, int end) {
        if (this.compilerEnv.isStrictMode()) {
            int beg;
            int[] linep = new int[2];
            String line = this.ts.getLine(end, linep);
            if (this.compilerEnv.isIdeMode()) {
                beg = Math.max(pos, end - linep[1]);
            } else {
                beg = pos;
            }
            if (line != null) {
                addStrictWarning("msg.missing.semi", "", beg, end - beg, linep[0], line, linep[1]);
                return;
            }
            addStrictWarning("msg.missing.semi", "", beg, end - beg);
        }
    }

    private void warnTrailingComma(int pos, List<?> elems, int commaPos) {
        if (this.compilerEnv.getWarnTrailingComma()) {
            if (!elems.isEmpty()) {
                pos = ((AstNode) elems.get(0)).getPosition();
            }
            pos = Math.max(pos, lineBeginningFor(commaPos));
            addWarning("msg.extra.trailing.comma", pos, commaPos - pos);
        }
    }

    private String readFully(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        try {
            char[] cbuf = new char[Opcodes.ACC_ABSTRACT];
            StringBuilder sb = new StringBuilder(Opcodes.ACC_ABSTRACT);
            while (true) {
                int bytes_read = in.read(cbuf, 0, Opcodes.ACC_ABSTRACT);
                if (bytes_read == -1) {
                    break;
                }
                sb.append(cbuf, 0, bytes_read);
            }
            String stringBuilder = sb.toString();
            return stringBuilder;
        } finally {
            in.close();
        }
    }

    /* access modifiers changed from: 0000 */
    public Node createDestructuringAssignment(int type, Node left, Node right) {
        String tempName = this.currentScriptOrFn.getNextTempName();
        Node result = destructuringAssignmentHelper(type, left, right, tempName);
        result.getLastChild().addChildToBack(createName(tempName));
        return result;
    }

    /* access modifiers changed from: 0000 */
    public Node destructuringAssignmentHelper(int variableType, Node left, Node right, String tempName) {
        Scope result = createScopeNode(158, left.getLineno());
        result.addChildToFront(new Node(153, createName(39, tempName, right)));
        try {
            pushScope(result);
            defineSymbol(153, tempName, true);
            Node comma = new Node(89);
            result.addChildToBack(comma);
            List<String> destructuringNames = new ArrayList();
            boolean empty = true;
            switch (left.getType()) {
                case 33:
                case 36:
                    switch (variableType) {
                        case 122:
                        case 153:
                        case 154:
                            reportError("msg.bad.assign.left");
                            break;
                    }
                    comma.addChildToBack(simpleAssignment(left, createName(tempName)));
                    break;
                case 65:
                    empty = destructuringArray((ArrayLiteral) left, variableType, tempName, comma, destructuringNames);
                    break;
                case 66:
                    empty = destructuringObject((ObjectLiteral) left, variableType, tempName, comma, destructuringNames);
                    break;
                default:
                    reportError("msg.bad.assign.left");
                    break;
            }
            if (empty) {
                comma.addChildToBack(createNumber(0.0d));
            }
            result.putProp(22, destructuringNames);
            return result;
        } finally {
            popScope();
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean destructuringArray(ArrayLiteral array, int variableType, String tempName, Node parent, List<String> destructuringNames) {
        boolean empty = true;
        int setOp = variableType == 154 ? 155 : 8;
        int index = 0;
        for (AstNode n : array.getElements()) {
            if (n.getType() == 128) {
                index++;
            } else {
                Node rightElem = new Node(36, createName(tempName), createNumber((double) index));
                if (n.getType() == 39) {
                    String name = n.getString();
                    parent.addChildToBack(new Node(setOp, createName(49, name, null), rightElem));
                    if (variableType != -1) {
                        defineSymbol(variableType, name, true);
                        destructuringNames.add(name);
                    }
                } else {
                    parent.addChildToBack(destructuringAssignmentHelper(variableType, n, rightElem, this.currentScriptOrFn.getNextTempName()));
                }
                index++;
                empty = false;
            }
        }
        return empty;
    }

    /* access modifiers changed from: 0000 */
    public boolean destructuringObject(ObjectLiteral node, int variableType, String tempName, Node parent, List<String> destructuringNames) {
        boolean empty = true;
        int setOp = variableType == 154 ? 155 : 8;
        for (ObjectProperty prop : node.getElements()) {
            Node rightElem;
            int lineno = 0;
            if (this.ts != null) {
                lineno = this.ts.lineno;
            }
            AstNode id = prop.getLeft();
            if (id instanceof Name) {
                rightElem = new Node(33, createName(tempName), Node.newString(((Name) id).getIdentifier()));
            } else if (id instanceof StringLiteral) {
                rightElem = new Node(33, createName(tempName), Node.newString(((StringLiteral) id).getValue()));
            } else if (id instanceof NumberLiteral) {
                rightElem = new Node(36, createName(tempName), createNumber((double) ((int) ((NumberLiteral) id).getNumber())));
            } else {
                throw codeBug();
            }
            rightElem.setLineno(lineno);
            AstNode value = prop.getRight();
            if (value.getType() == 39) {
                String name = ((Name) value).getIdentifier();
                parent.addChildToBack(new Node(setOp, createName(49, name, null), rightElem));
                if (variableType != -1) {
                    defineSymbol(variableType, name, true);
                    destructuringNames.add(name);
                }
            } else {
                parent.addChildToBack(destructuringAssignmentHelper(variableType, value, rightElem, this.currentScriptOrFn.getNextTempName()));
            }
            empty = false;
        }
        return empty;
    }

    /* access modifiers changed from: protected */
    public Node createName(String name) {
        checkActivationName(name, 39);
        return Node.newString(39, name);
    }

    /* access modifiers changed from: protected */
    public Node createName(int type, String name, Node child) {
        Node result = createName(name);
        result.setType(type);
        if (child != null) {
            result.addChildToBack(child);
        }
        return result;
    }

    /* access modifiers changed from: protected */
    public Node createNumber(double number) {
        return Node.newNumber(number);
    }

    /* access modifiers changed from: protected */
    public Scope createScopeNode(int token, int lineno) {
        Scope scope = new Scope();
        scope.setType(token);
        scope.setLineno(lineno);
        return scope;
    }

    /* access modifiers changed from: protected */
    public Node simpleAssignment(Node left, Node right) {
        int nodeType = left.getType();
        switch (nodeType) {
            case 33:
            case 36:
                Node obj;
                Node id;
                int type;
                if (left instanceof PropertyGet) {
                    obj = ((PropertyGet) left).getTarget();
                    id = ((PropertyGet) left).getProperty();
                } else if (left instanceof ElementGet) {
                    obj = ((ElementGet) left).getTarget();
                    id = ((ElementGet) left).getElement();
                } else {
                    obj = left.getFirstChild();
                    id = left.getLastChild();
                }
                if (nodeType == 33) {
                    type = 35;
                    id.setType(41);
                } else {
                    type = 37;
                }
                return new Node(type, obj, id, right);
            case 39:
                if (this.inUseStrictDirective && "eval".equals(((Name) left).getIdentifier())) {
                    reportError("msg.bad.id.strict", ((Name) left).getIdentifier());
                }
                left.setType(49);
                return new Node(8, left, right);
            case 67:
                Node ref = left.getFirstChild();
                checkMutableReference(ref);
                return new Node(68, ref, right);
            default:
                throw codeBug();
        }
    }

    /* access modifiers changed from: protected */
    public void checkMutableReference(Node n) {
        if ((n.getIntProp(16, 0) & 4) != 0) {
            reportError("msg.bad.assign.left");
        }
    }

    /* access modifiers changed from: protected */
    public AstNode removeParens(AstNode node) {
        while (node instanceof ParenthesizedExpression) {
            node = ((ParenthesizedExpression) node).getExpression();
        }
        return node;
    }

    /* access modifiers changed from: 0000 */
    public void markDestructuring(AstNode node) {
        if (node instanceof DestructuringForm) {
            ((DestructuringForm) node).setIsDestructuring(true);
        } else if (node instanceof ParenthesizedExpression) {
            markDestructuring(((ParenthesizedExpression) node).getExpression());
        }
    }

    private RuntimeException codeBug() throws RuntimeException {
        throw Kit.codeBug("ts.cursor=" + this.ts.cursor + ", ts.tokenBeg=" + this.ts.tokenBeg + ", currentToken=" + this.currentToken);
    }
}
