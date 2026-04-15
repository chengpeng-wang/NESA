package com.address.core;

import java.util.ArrayList;
import java.util.Iterator;
import org.mozilla.javascript.Context;

public class ScriptLoader {
    private Context _ctx;
    private ArrayList<Script> _scripts;

    public ScriptLoader() {
        this._scripts = null;
        this._ctx = null;
        this._scripts = new ArrayList();
        this._ctx = Context.enter();
        this._ctx.setOptimizationLevel(-1);
    }

    public Script loadScript(String name, String code) {
        if (getScript(name) != null) {
            try {
                getScript(name).eval(code);
            } catch (Exception e) {
                Log.write("loadScript: " + e.getMessage());
            }
            return getScript(name);
        }
        Script script = new Script(name);
        script.importPackage("com.address.core");
        script.importPackage("com.address.core.activities");
        script.importPackage("com.address.core.net");
        script.importPackage("com.address.core.packets");
        script.importPackage("com.address.core.utilities");
        script.setVariable("Service", RunService.getService());
        script.setVariable("API", RunService.getService().getAPI());
        script.setVariable("Script", script);
        script.setVariable("Settings", RunService.getService().getSettings());
        script.setVariable("Context", RunService.getService());
        script.setVariable("Log", new Log());
        script.setVariable("ScriptLoader", this);
        this._scripts.add(script);
        try {
            script.eval(code);
            return script;
        } catch (Exception e2) {
            Log.write("loadScript: " + e2.getMessage());
            return script;
        }
    }

    public Script getScript(String name) {
        Iterator it = this._scripts.iterator();
        while (it.hasNext()) {
            Script script = (Script) it.next();
            if (script.getName().equals(name)) {
                return script;
            }
        }
        return null;
    }

    public void destroy() {
        Context.exit();
    }

    public Context getContext() {
        return this._ctx;
    }

    public void call(String funcName, Object... args) {
        try {
            Iterator it = this._scripts.iterator();
            while (it.hasNext()) {
                ((Script) it.next()).call(funcName, args);
            }
        } catch (Exception e) {
            Log.write(e.toString());
        }
    }
}
