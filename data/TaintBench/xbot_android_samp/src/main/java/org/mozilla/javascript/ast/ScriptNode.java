package org.mozilla.javascript.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.mozilla.javascript.Node;

public class ScriptNode extends Scope {
    private List<FunctionNode> EMPTY_LIST;
    private Object compilerData;
    private String encodedSource;
    private int encodedSourceEnd;
    private int encodedSourceStart;
    private int endLineno;
    private List<FunctionNode> functions;
    private boolean[] isConsts;
    private int paramCount;
    private List<RegExpLiteral> regexps;
    private String sourceName;
    private List<Symbol> symbols;
    private int tempNumber;
    private String[] variableNames;

    public ScriptNode() {
        this.encodedSourceStart = -1;
        this.encodedSourceEnd = -1;
        this.endLineno = -1;
        this.EMPTY_LIST = Collections.emptyList();
        this.symbols = new ArrayList(4);
        this.paramCount = 0;
        this.tempNumber = 0;
        this.top = this;
        this.type = 136;
    }

    public ScriptNode(int pos) {
        super(pos);
        this.encodedSourceStart = -1;
        this.encodedSourceEnd = -1;
        this.endLineno = -1;
        this.EMPTY_LIST = Collections.emptyList();
        this.symbols = new ArrayList(4);
        this.paramCount = 0;
        this.tempNumber = 0;
        this.top = this;
        this.type = 136;
    }

    public String getSourceName() {
        return this.sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public int getEncodedSourceStart() {
        return this.encodedSourceStart;
    }

    public void setEncodedSourceStart(int start) {
        this.encodedSourceStart = start;
    }

    public int getEncodedSourceEnd() {
        return this.encodedSourceEnd;
    }

    public void setEncodedSourceEnd(int end) {
        this.encodedSourceEnd = end;
    }

    public void setEncodedSourceBounds(int start, int end) {
        this.encodedSourceStart = start;
        this.encodedSourceEnd = end;
    }

    public void setEncodedSource(String encodedSource) {
        this.encodedSource = encodedSource;
    }

    public String getEncodedSource() {
        return this.encodedSource;
    }

    public int getBaseLineno() {
        return this.lineno;
    }

    public void setBaseLineno(int lineno) {
        if (lineno < 0 || this.lineno >= 0) {
            AstNode.codeBug();
        }
        this.lineno = lineno;
    }

    public int getEndLineno() {
        return this.endLineno;
    }

    public void setEndLineno(int lineno) {
        if (lineno < 0 || this.endLineno >= 0) {
            AstNode.codeBug();
        }
        this.endLineno = lineno;
    }

    public int getFunctionCount() {
        return this.functions == null ? 0 : this.functions.size();
    }

    public FunctionNode getFunctionNode(int i) {
        return (FunctionNode) this.functions.get(i);
    }

    public List<FunctionNode> getFunctions() {
        return this.functions == null ? this.EMPTY_LIST : this.functions;
    }

    public int addFunction(FunctionNode fnNode) {
        if (fnNode == null) {
            AstNode.codeBug();
        }
        if (this.functions == null) {
            this.functions = new ArrayList();
        }
        this.functions.add(fnNode);
        return this.functions.size() - 1;
    }

    public int getRegexpCount() {
        return this.regexps == null ? 0 : this.regexps.size();
    }

    public String getRegexpString(int index) {
        return ((RegExpLiteral) this.regexps.get(index)).getValue();
    }

    public String getRegexpFlags(int index) {
        return ((RegExpLiteral) this.regexps.get(index)).getFlags();
    }

    public void addRegExp(RegExpLiteral re) {
        if (re == null) {
            AstNode.codeBug();
        }
        if (this.regexps == null) {
            this.regexps = new ArrayList();
        }
        this.regexps.add(re);
        re.putIntProp(4, this.regexps.size() - 1);
    }

    public int getIndexForNameNode(Node nameNode) {
        Symbol symbol;
        if (this.variableNames == null) {
            AstNode.codeBug();
        }
        Scope node = nameNode.getScope();
        if (node == null) {
            symbol = null;
        } else {
            symbol = node.getSymbol(((Name) nameNode).getIdentifier());
        }
        return symbol == null ? -1 : symbol.getIndex();
    }

    public String getParamOrVarName(int index) {
        if (this.variableNames == null) {
            AstNode.codeBug();
        }
        return this.variableNames[index];
    }

    public int getParamCount() {
        return this.paramCount;
    }

    public int getParamAndVarCount() {
        if (this.variableNames == null) {
            AstNode.codeBug();
        }
        return this.symbols.size();
    }

    public String[] getParamAndVarNames() {
        if (this.variableNames == null) {
            AstNode.codeBug();
        }
        return this.variableNames;
    }

    public boolean[] getParamAndVarConst() {
        if (this.variableNames == null) {
            AstNode.codeBug();
        }
        return this.isConsts;
    }

    /* access modifiers changed from: 0000 */
    public void addSymbol(Symbol symbol) {
        if (this.variableNames != null) {
            AstNode.codeBug();
        }
        if (symbol.getDeclType() == 87) {
            this.paramCount++;
        }
        this.symbols.add(symbol);
    }

    public List<Symbol> getSymbols() {
        return this.symbols;
    }

    public void setSymbols(List<Symbol> symbols) {
        this.symbols = symbols;
    }

    public void flattenSymbolTable(boolean flattenAllTables) {
        int i;
        Symbol symbol;
        if (!flattenAllTables) {
            List<Symbol> newSymbols = new ArrayList();
            if (this.symbolTable != null) {
                for (i = 0; i < this.symbols.size(); i++) {
                    symbol = (Symbol) this.symbols.get(i);
                    if (symbol.getContainingTable() == this) {
                        newSymbols.add(symbol);
                    }
                }
            }
            this.symbols = newSymbols;
        }
        this.variableNames = new String[this.symbols.size()];
        this.isConsts = new boolean[this.symbols.size()];
        for (i = 0; i < this.symbols.size(); i++) {
            symbol = (Symbol) this.symbols.get(i);
            this.variableNames[i] = symbol.getName();
            this.isConsts[i] = symbol.getDeclType() == 154;
            symbol.setIndex(i);
        }
    }

    public Object getCompilerData() {
        return this.compilerData;
    }

    public void setCompilerData(Object data) {
        assertNotNull(data);
        if (this.compilerData != null) {
            throw new IllegalStateException();
        }
        this.compilerData = data;
    }

    public String getNextTempName() {
        StringBuilder append = new StringBuilder().append("$");
        int i = this.tempNumber;
        this.tempNumber = i + 1;
        return append.append(i).toString();
    }

    public void visit(NodeVisitor v) {
        if (v.visit(this)) {
            Iterator it = iterator();
            while (it.hasNext()) {
                ((AstNode) ((Node) it.next())).visit(v);
            }
        }
    }
}
