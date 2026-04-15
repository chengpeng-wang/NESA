package com.address.core;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class Script {
    private String _name = "";
    private Scriptable _scope = null;

    public Script(String name) {
        this._name = name;
        this._scope = new ImporterTopLevel(RunService.getService().getScriptLoader().getContext());
    }

    public void setVariable(String name, Object value) {
        ScriptableObject.putProperty(this._scope, name, Context.javaToJS(value, this._scope));
    }

    public void importPackage(String pkgName) {
        eval("importPackage(Packages." + pkgName + ");");
    }

    public void setClass(Class clazz) {
        try {
            ScriptableObject.defineClass(this._scope, clazz);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Script.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex2) {
            Logger.getLogger(Script.class.getName()).log(Level.SEVERE, null, ex2);
        } catch (InvocationTargetException ex3) {
            Logger.getLogger(Script.class.getName()).log(Level.SEVERE, null, ex3);
        }
    }

    public void destroy() {
    }

    public Object eval(String code) {
        try {
            return RunService.getService().getScriptLoader().getContext().evaluateString(this._scope, code, this._name, 1, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getName() {
        return this._name;
    }

    public Object call(String funcName, Object[] args) {
        Function fObj = this._scope.get(funcName, this._scope);
        if (fObj instanceof Function) {
            Object result = fObj.call(RunService.getService().getScriptLoader().getContext(), this._scope, this._scope, args);
            Log.write(Context.toString(result));
            return result;
        }
        Log.write("[Script:call]: " + funcName + " is undefined or not a function.");
        return null;
    }
}
