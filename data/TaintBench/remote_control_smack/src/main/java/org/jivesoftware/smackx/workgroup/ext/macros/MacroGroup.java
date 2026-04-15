package org.jivesoftware.smackx.workgroup.ext.macros;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MacroGroup {
    private List macroGroups = new ArrayList();
    private List macros = new ArrayList();
    private String title;

    public void addMacro(Macro macro) {
        this.macros.add(macro);
    }

    public void removeMacro(Macro macro) {
        this.macros.remove(macro);
    }

    public Macro getMacroByTitle(String title) {
        for (Macro macro : Collections.unmodifiableList(this.macros)) {
            if (macro.getTitle().equalsIgnoreCase(title)) {
                return macro;
            }
        }
        return null;
    }

    public void addMacroGroup(MacroGroup group) {
        this.macroGroups.add(group);
    }

    public void removeMacroGroup(MacroGroup group) {
        this.macroGroups.remove(group);
    }

    public Macro getMacro(int location) {
        return (Macro) this.macros.get(location);
    }

    public MacroGroup getMacroGroupByTitle(String title) {
        for (MacroGroup group : Collections.unmodifiableList(this.macroGroups)) {
            if (group.getTitle().equalsIgnoreCase(title)) {
                return group;
            }
        }
        return null;
    }

    public MacroGroup getMacroGroup(int location) {
        return (MacroGroup) this.macroGroups.get(location);
    }

    public List getMacros() {
        return this.macros;
    }

    public void setMacros(List macros) {
        this.macros = macros;
    }

    public List getMacroGroups() {
        return this.macroGroups;
    }

    public void setMacroGroups(List macroGroups) {
        this.macroGroups = macroGroups;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
