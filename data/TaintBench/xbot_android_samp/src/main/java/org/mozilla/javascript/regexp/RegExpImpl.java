package org.mozilla.javascript.regexp;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Kit;
import org.mozilla.javascript.RegExpProxy;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

public class RegExpImpl implements RegExpProxy {
    protected String input;
    protected SubString lastMatch;
    protected SubString lastParen;
    protected SubString leftContext;
    protected boolean multiline;
    protected SubString[] parens;
    protected SubString rightContext;

    public boolean isRegExp(Scriptable obj) {
        return obj instanceof NativeRegExp;
    }

    public Object compileRegExp(Context cx, String source, String flags) {
        return NativeRegExp.compileRE(cx, source, flags, false);
    }

    public Scriptable wrapRegExp(Context cx, Scriptable scope, Object compiled) {
        return new NativeRegExp(scope, (RECompiled) compiled);
    }

    public Object action(Context cx, Scriptable scope, Scriptable thisObj, Object[] args, int actionType) {
        GlobData data = new GlobData();
        data.mode = actionType;
        data.str = ScriptRuntime.toString((Object) thisObj);
        switch (actionType) {
            case 1:
                Object rval = matchOrReplace(cx, scope, thisObj, args, this, data, createRegExp(cx, scope, args, 1, false));
                if (data.arrayobj == null) {
                    return rval;
                }
                return data.arrayobj;
            case 2:
                Boolean val;
                boolean useRE = (args.length > 0 && (args[0] instanceof NativeRegExp)) || args.length > 2;
                NativeRegExp re = null;
                String search = null;
                if (useRE) {
                    re = createRegExp(cx, scope, args, 2, true);
                } else {
                    search = ScriptRuntime.toString(args.length < 1 ? Undefined.instance : args[0]);
                }
                Object arg1 = args.length < 2 ? Undefined.instance : args[1];
                String repstr = null;
                Function lambda = null;
                if (arg1 instanceof Function) {
                    lambda = (Function) arg1;
                } else {
                    repstr = ScriptRuntime.toString(arg1);
                }
                data.lambda = lambda;
                data.repstr = repstr;
                data.dollar = repstr == null ? -1 : repstr.indexOf(36);
                data.charBuf = null;
                data.leftIndex = 0;
                if (useRE) {
                    val = matchOrReplace(cx, scope, thisObj, args, this, data, re);
                } else {
                    String str = data.str;
                    int index = str.indexOf(search);
                    if (index >= 0) {
                        int slen = search.length();
                        this.lastParen = null;
                        this.leftContext = new SubString(str, 0, index);
                        this.lastMatch = new SubString(str, index, slen);
                        this.rightContext = new SubString(str, index + slen, (str.length() - index) - slen);
                        val = Boolean.TRUE;
                    } else {
                        val = Boolean.FALSE;
                    }
                }
                if (data.charBuf == null) {
                    if (!(data.global || val == null)) {
                        if (val.equals(Boolean.TRUE)) {
                            SubString lc = this.leftContext;
                            replace_glob(data, cx, scope, this, lc.index, lc.length);
                        }
                    }
                    return data.str;
                }
                SubString rc = this.rightContext;
                data.charBuf.append(rc.str, rc.index, rc.index + rc.length);
                return data.charBuf.toString();
            case 3:
                return matchOrReplace(cx, scope, thisObj, args, this, data, createRegExp(cx, scope, args, 1, false));
            default:
                throw Kit.codeBug();
        }
    }

    private static NativeRegExp createRegExp(Context cx, Scriptable scope, Object[] args, int optarg, boolean forceFlat) {
        Scriptable topScope = ScriptableObject.getTopLevelScope(scope);
        if (args.length == 0 || args[0] == Undefined.instance) {
            return new NativeRegExp(topScope, NativeRegExp.compileRE(cx, "", "", false));
        }
        if (args[0] instanceof NativeRegExp) {
            return (NativeRegExp) args[0];
        }
        String opt;
        String src = ScriptRuntime.toString(args[0]);
        if (optarg < args.length) {
            args[0] = src;
            opt = ScriptRuntime.toString(args[optarg]);
        } else {
            opt = null;
        }
        return new NativeRegExp(topScope, NativeRegExp.compileRE(cx, src, opt, forceFlat));
    }

    private static Object matchOrReplace(Context cx, Scriptable scope, Scriptable thisObj, Object[] args, RegExpImpl reImpl, GlobData data, NativeRegExp re) {
        String str = data.str;
        data.global = (re.getFlags() & 1) != 0;
        int[] indexp = new int[]{0};
        Object result = null;
        if (data.mode == 3) {
            result = re.executeRegExp(cx, scope, reImpl, str, indexp, 0);
            if (result != null) {
                if (result.equals(Boolean.TRUE)) {
                    return Integer.valueOf(reImpl.leftContext.length);
                }
            }
            return Integer.valueOf(-1);
        } else if (data.global) {
            re.lastIndex = Double.valueOf(0.0d);
            int count = 0;
            while (indexp[0] <= str.length()) {
                result = re.executeRegExp(cx, scope, reImpl, str, indexp, 0);
                if (result == null) {
                    return result;
                }
                if (!result.equals(Boolean.TRUE)) {
                    return result;
                }
                if (data.mode == 1) {
                    match_glob(data, cx, scope, count, reImpl);
                } else {
                    if (data.mode != 2) {
                        Kit.codeBug();
                    }
                    SubString lastMatch = reImpl.lastMatch;
                    int leftIndex = data.leftIndex;
                    int leftlen = lastMatch.index - leftIndex;
                    data.leftIndex = lastMatch.index + lastMatch.length;
                    replace_glob(data, cx, scope, reImpl, leftIndex, leftlen);
                }
                if (reImpl.lastMatch.length == 0) {
                    if (indexp[0] == str.length()) {
                        return result;
                    }
                    indexp[0] = indexp[0] + 1;
                }
                count++;
            }
            return result;
        } else {
            return re.executeRegExp(cx, scope, reImpl, str, indexp, data.mode == 2 ? 0 : 1);
        }
    }

    public int find_split(Context cx, Scriptable scope, String target, String separator, Scriptable reObj, int[] ip, int[] matchlen, boolean[] matched, String[][] parensp) {
        int result;
        int i = ip[0];
        int length = target.length();
        int version = cx.getLanguageVersion();
        NativeRegExp re = (NativeRegExp) reObj;
        while (true) {
            int ipsave = ip[0];
            ip[0] = i;
            if (re.executeRegExp(cx, scope, this, target, ip, 0) != Boolean.TRUE) {
                ip[0] = ipsave;
                matchlen[0] = 1;
                matched[0] = false;
                return length;
            }
            i = ip[0];
            ip[0] = ipsave;
            matched[0] = true;
            matchlen[0] = this.lastMatch.length;
            if (matchlen[0] != 0 || i != ip[0]) {
                result = i - matchlen[0];
            } else if (i != length) {
                i++;
            } else if (version == 120) {
                matchlen[0] = 1;
                result = i;
            } else {
                result = -1;
            }
        }
        result = i - matchlen[0];
        int size = this.parens == null ? 0 : this.parens.length;
        parensp[0] = new String[size];
        for (int num = 0; num < size; num++) {
            parensp[0][num] = getParenSubString(num).toString();
        }
        return result;
    }

    /* access modifiers changed from: 0000 */
    public SubString getParenSubString(int i) {
        if (this.parens != null && i < this.parens.length) {
            SubString parsub = this.parens[i];
            if (parsub != null) {
                return parsub;
            }
        }
        return SubString.emptySubString;
    }

    private static void match_glob(GlobData mdata, Context cx, Scriptable scope, int count, RegExpImpl reImpl) {
        if (mdata.arrayobj == null) {
            mdata.arrayobj = cx.newArray(scope, 0);
        }
        mdata.arrayobj.put(count, mdata.arrayobj, reImpl.lastMatch.toString());
    }

    private static void replace_glob(GlobData rdata, Context cx, Scriptable scope, RegExpImpl reImpl, int leftIndex, int leftlen) {
        String lambdaStr;
        int replen;
        SubString sub;
        if (rdata.lambda != null) {
            SubString[] parens = reImpl.parens;
            int parenCount = parens == null ? 0 : parens.length;
            Object[] args = new Object[(parenCount + 3)];
            args[0] = reImpl.lastMatch.toString();
            for (int i = 0; i < parenCount; i++) {
                sub = parens[i];
                if (sub != null) {
                    args[i + 1] = sub.toString();
                } else {
                    args[i + 1] = Undefined.instance;
                }
            }
            args[parenCount + 1] = Integer.valueOf(reImpl.leftContext.length);
            args[parenCount + 2] = rdata.str;
            if (reImpl != ScriptRuntime.getRegExpProxy(cx)) {
                Kit.codeBug();
            }
            RegExpImpl re2 = new RegExpImpl();
            re2.multiline = reImpl.multiline;
            re2.input = reImpl.input;
            ScriptRuntime.setRegExpProxy(cx, re2);
            try {
                Scriptable parent = ScriptableObject.getTopLevelScope(scope);
                lambdaStr = ScriptRuntime.toString(rdata.lambda.call(cx, parent, parent, args));
                replen = lambdaStr.length();
            } finally {
                ScriptRuntime.setRegExpProxy(cx, reImpl);
            }
        } else {
            lambdaStr = null;
            replen = rdata.repstr.length();
            if (rdata.dollar >= 0) {
                int[] skip = new int[1];
                int dp = rdata.dollar;
                while (true) {
                    sub = interpretDollar(cx, reImpl, rdata.repstr, dp, skip);
                    if (sub != null) {
                        replen += sub.length - skip[0];
                        dp += skip[0];
                    } else {
                        dp++;
                    }
                    dp = rdata.repstr.indexOf(36, dp);
                    if (dp < 0) {
                        break;
                    }
                }
            }
        }
        int growth = (leftlen + replen) + reImpl.rightContext.length;
        StringBuilder charBuf = rdata.charBuf;
        if (charBuf == null) {
            charBuf = new StringBuilder(growth);
            rdata.charBuf = charBuf;
        } else {
            charBuf.ensureCapacity(rdata.charBuf.length() + growth);
        }
        charBuf.append(reImpl.leftContext.str, leftIndex, leftIndex + leftlen);
        if (rdata.lambda != null) {
            charBuf.append(lambdaStr);
        } else {
            do_replace(rdata, cx, reImpl);
        }
    }

    private static SubString interpretDollar(Context cx, RegExpImpl res, String da, int dp, int[] skip) {
        if (da.charAt(dp) != '$') {
            Kit.codeBug();
        }
        int version = cx.getLanguageVersion();
        if (version != 0 && version <= 140 && dp > 0 && da.charAt(dp - 1) == '\\') {
            return null;
        }
        int daL = da.length();
        if (dp + 1 >= daL) {
            return null;
        }
        char dc = da.charAt(dp + 1);
        if (NativeRegExp.isDigit(dc)) {
            int num;
            int cp;
            int tmp;
            if (version != 0 && version <= 140) {
                if (dc != '0') {
                    num = 0;
                    cp = dp;
                    while (true) {
                        cp++;
                        if (cp >= daL) {
                            break;
                        }
                        dc = da.charAt(cp);
                        if (!NativeRegExp.isDigit(dc)) {
                            break;
                        }
                        tmp = (num * 10) + (dc - 48);
                        if (tmp < num) {
                            break;
                        }
                        num = tmp;
                    }
                } else {
                    return null;
                }
            }
            int parenCount = res.parens == null ? 0 : res.parens.length;
            num = dc - 48;
            if (num > parenCount) {
                return null;
            }
            cp = dp + 2;
            if (dp + 2 < daL) {
                dc = da.charAt(dp + 2);
                if (NativeRegExp.isDigit(dc)) {
                    tmp = (num * 10) + (dc - 48);
                    if (tmp <= parenCount) {
                        cp++;
                        num = tmp;
                    }
                }
            }
            if (num == 0) {
                return null;
            }
            num--;
            skip[0] = cp - dp;
            return res.getParenSubString(num);
        }
        skip[0] = 2;
        switch (dc) {
            case '$':
                return new SubString("$");
            case '&':
                return res.lastMatch;
            case '\'':
                return res.rightContext;
            case '+':
                return res.lastParen;
            case '`':
                if (version == 120) {
                    res.leftContext.index = 0;
                    res.leftContext.length = res.lastMatch.index;
                }
                return res.leftContext;
            default:
                return null;
        }
    }

    private static void do_replace(GlobData rdata, Context cx, RegExpImpl regExpImpl) {
        StringBuilder charBuf = rdata.charBuf;
        int cp = 0;
        String da = rdata.repstr;
        int dp = rdata.dollar;
        if (dp != -1) {
            int[] skip = new int[1];
            do {
                int len = dp - cp;
                charBuf.append(da.substring(cp, dp));
                cp = dp;
                SubString sub = interpretDollar(cx, regExpImpl, da, dp, skip);
                if (sub != null) {
                    len = sub.length;
                    if (len > 0) {
                        charBuf.append(sub.str, sub.index, sub.index + len);
                    }
                    cp += skip[0];
                    dp += skip[0];
                } else {
                    dp++;
                }
                dp = da.indexOf(36, dp);
            } while (dp >= 0);
        }
        int daL = da.length();
        if (daL > cp) {
            charBuf.append(da.substring(cp, daL));
        }
    }

    public Object js_split(Context cx, Scriptable scope, String target, Object[] args) {
        Scriptable result = cx.newArray(scope, 0);
        boolean limited = args.length > 1 && args[1] != Undefined.instance;
        long limit = 0;
        if (limited) {
            limit = ScriptRuntime.toUint32(args[1]);
            if (limit > ((long) target.length())) {
                limit = (long) (target.length() + 1);
            }
        }
        if (args.length >= 1 && args[0] != Undefined.instance) {
            String separator = null;
            int[] matchlen = new int[1];
            Scriptable re = null;
            RegExpProxy reProxy = null;
            if (args[0] instanceof Scriptable) {
                reProxy = ScriptRuntime.getRegExpProxy(cx);
                if (reProxy != null) {
                    Scriptable test = args[0];
                    if (reProxy.isRegExp(test)) {
                        re = test;
                    }
                }
            }
            if (re == null) {
                separator = ScriptRuntime.toString(args[0]);
                matchlen[0] = separator.length();
            }
            int[] ip = new int[]{0};
            int len = 0;
            boolean[] matched = new boolean[]{false};
            String[][] parens = new String[][]{null};
            int version = cx.getLanguageVersion();
            while (true) {
                int match = find_split(cx, scope, target, separator, version, reProxy, re, ip, matchlen, matched, parens);
                if (match < 0 || ((limited && ((long) len) >= limit) || match > target.length())) {
                    break;
                }
                String substr;
                if (target.length() == 0) {
                    substr = target;
                } else {
                    substr = target.substring(ip[0], match);
                }
                result.put(len, result, (Object) substr);
                len++;
                if (re != null && matched[0]) {
                    int size = parens[0].length;
                    for (int num = 0; num < size && (!limited || ((long) len) < limit); num++) {
                        result.put(len, result, parens[0][num]);
                        len++;
                    }
                    matched[0] = false;
                }
                ip[0] = matchlen[0] + match;
                if (version < 130 && version != 0 && !limited && ip[0] == target.length()) {
                    break;
                }
            }
        } else {
            result.put(0, result, (Object) target);
        }
        return result;
    }

    private static int find_split(Context cx, Scriptable scope, String target, String separator, int version, RegExpProxy reProxy, Scriptable re, int[] ip, int[] matchlen, boolean[] matched, String[][] parensp) {
        int i = ip[0];
        int length = target.length();
        if (version == 120 && re == null && separator.length() == 1 && separator.charAt(0) == ' ') {
            if (i == 0) {
                while (i < length && Character.isWhitespace(target.charAt(i))) {
                    i++;
                }
                ip[0] = i;
            }
            if (i == length) {
                return -1;
            }
            while (i < length && !Character.isWhitespace(target.charAt(i))) {
                i++;
            }
            int j = i;
            while (j < length && Character.isWhitespace(target.charAt(j))) {
                j++;
            }
            matchlen[0] = j - i;
            return i;
        } else if (i > length) {
            return -1;
        } else {
            if (re != null) {
                return reProxy.find_split(cx, scope, target, separator, re, ip, matchlen, matched, parensp);
            }
            if (version != 0 && version < 130 && length == 0) {
                return -1;
            }
            if (separator.length() == 0) {
                if (version != 120) {
                    return i == length ? -1 : i + 1;
                } else if (i != length) {
                    return i + 1;
                } else {
                    matchlen[0] = 1;
                    return i;
                }
            } else if (ip[0] >= length) {
                return length;
            } else {
                i = target.indexOf(separator, ip[0]);
                return i != -1 ? i : length;
            }
        }
    }
}
