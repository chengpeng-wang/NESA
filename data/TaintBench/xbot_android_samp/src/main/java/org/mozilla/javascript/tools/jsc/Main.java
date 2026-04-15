package org.mozilla.javascript.tools.jsc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.optimizer.ClassCompiler;
import org.mozilla.javascript.tools.SourceReader;
import org.mozilla.javascript.tools.ToolErrorReporter;

public class Main {
    private String characterEncoding;
    private ClassCompiler compiler;
    private CompilerEnvirons compilerEnv = new CompilerEnvirons();
    private String destinationDir;
    private boolean printHelp;
    private ToolErrorReporter reporter = new ToolErrorReporter(true);
    private String targetName;
    private String targetPackage;

    public static void main(String[] args) {
        Main main = new Main();
        args = main.processOptions(args);
        if (args == null) {
            if (main.printHelp) {
                System.out.println(ToolErrorReporter.getMessage("msg.jsc.usage", Main.class.getName()));
                System.exit(0);
            }
            System.exit(1);
        }
        if (!main.reporter.hasReportedError()) {
            main.processSource(args);
        }
    }

    public Main() {
        this.compilerEnv.setErrorReporter(this.reporter);
        this.compiler = new ClassCompiler(this.compilerEnv);
    }

    public String[] processOptions(String[] args) {
        this.targetPackage = "";
        this.compilerEnv.setGenerateDebugInfo(false);
        int i = 0;
        while (i < args.length) {
            String arg = args[i];
            int j;
            if (!arg.startsWith("-")) {
                int tail = args.length - i;
                if (this.targetName == null || tail <= 1) {
                    String[] result = new String[tail];
                    for (j = 0; j != tail; j++) {
                        result[j] = args[i + j];
                    }
                    return result;
                }
                addError("msg.multiple.js.to.file", this.targetName);
                return null;
            } else if (arg.equals("-help") || arg.equals("-h") || arg.equals("--help")) {
                this.printHelp = true;
                return null;
            } else {
                try {
                    if (arg.equals("-version")) {
                        i++;
                        if (i < args.length) {
                            this.compilerEnv.setLanguageVersion(Integer.parseInt(args[i]));
                            i++;
                        }
                    }
                    if (arg.equals("-opt") || arg.equals("-O")) {
                        i++;
                        if (i < args.length) {
                            this.compilerEnv.setOptimizationLevel(Integer.parseInt(args[i]));
                            i++;
                        }
                    }
                    if (arg.equals("-nosource")) {
                        this.compilerEnv.setGeneratingSource(false);
                    } else if (arg.equals("-debug") || arg.equals("-g")) {
                        this.compilerEnv.setGenerateDebugInfo(true);
                    } else {
                        int end;
                        char c;
                        if (arg.equals("-main-method-class")) {
                            i++;
                            if (i < args.length) {
                                this.compiler.setMainMethodClass(args[i]);
                            }
                        }
                        if (arg.equals("-encoding")) {
                            i++;
                            if (i < args.length) {
                                this.characterEncoding = args[i];
                            }
                        }
                        if (arg.equals("-o")) {
                            i++;
                            if (i < args.length) {
                                String name = args[i];
                                end = name.length();
                                if (end == 0 || !Character.isJavaIdentifierStart(name.charAt(0))) {
                                    addError("msg.invalid.classfile.name", name);
                                } else {
                                    j = 1;
                                    while (j < end) {
                                        c = name.charAt(j);
                                        if (Character.isJavaIdentifierPart(c)) {
                                            j++;
                                        } else {
                                            if (c == '.' && j == end - 6 && name.endsWith(".class")) {
                                                name = name.substring(0, j);
                                            } else {
                                                addError("msg.invalid.classfile.name", name);
                                            }
                                            this.targetName = name;
                                        }
                                    }
                                    this.targetName = name;
                                }
                            }
                        }
                        if (arg.equals("-observe-instruction-count")) {
                            this.compilerEnv.setGenerateObserverCount(true);
                        }
                        if (arg.equals("-package")) {
                            i++;
                            if (i < args.length) {
                                String pkg = args[i];
                                end = pkg.length();
                                j = 0;
                                while (j != end) {
                                    c = pkg.charAt(j);
                                    if (Character.isJavaIdentifierStart(c)) {
                                        j++;
                                        while (j != end) {
                                            c = pkg.charAt(j);
                                            if (!Character.isJavaIdentifierPart(c)) {
                                                break;
                                            }
                                            j++;
                                        }
                                        if (j == end) {
                                            break;
                                        } else if (c == '.' && j != end - 1) {
                                            j++;
                                        }
                                    }
                                    addError("msg.package.name", this.targetPackage);
                                    return null;
                                }
                                this.targetPackage = pkg;
                            }
                        }
                        if (arg.equals("-extends")) {
                            i++;
                            if (i < args.length) {
                                try {
                                    this.compiler.setTargetExtends(Class.forName(args[i]));
                                } catch (ClassNotFoundException e) {
                                    throw new Error(e.toString());
                                }
                            }
                        }
                        if (arg.equals("-implements")) {
                            i++;
                            if (i < args.length) {
                                StringTokenizer stringTokenizer = new StringTokenizer(args[i], ",");
                                List<Class<?>> list = new ArrayList();
                                while (stringTokenizer.hasMoreTokens()) {
                                    try {
                                        list.add(Class.forName(stringTokenizer.nextToken()));
                                    } catch (ClassNotFoundException e2) {
                                        throw new Error(e2.toString());
                                    }
                                }
                                this.compiler.setTargetImplements((Class[]) list.toArray(new Class[list.size()]));
                            }
                        }
                        if (arg.equals("-d")) {
                            i++;
                            if (i < args.length) {
                                this.destinationDir = args[i];
                            }
                        }
                        badUsage(arg);
                        return null;
                    }
                    i++;
                } catch (NumberFormatException e3) {
                    badUsage(args[i]);
                    return null;
                }
            }
        }
        p(ToolErrorReporter.getMessage("msg.no.file"));
        return null;
    }

    private static void badUsage(String s) {
        System.err.println(ToolErrorReporter.getMessage("msg.jsc.bad.usage", Main.class.getName(), s));
    }

    public void processSource(String[] filenames) {
        int i = 0;
        while (i != filenames.length) {
            String filename = filenames[i];
            if (filename.endsWith(".js")) {
                File f = new File(filename);
                String source = readSource(f);
                if (source != null) {
                    String mainClassName = this.targetName;
                    if (mainClassName == null) {
                        String name = f.getName();
                        mainClassName = getClassName(name.substring(0, name.length() - 3));
                    }
                    if (this.targetPackage.length() != 0) {
                        mainClassName = this.targetPackage + "." + mainClassName;
                    }
                    Object[] compiled = this.compiler.compileToClassFiles(source, filename, 1, mainClassName);
                    if (compiled != null && compiled.length != 0) {
                        File targetTopDir = null;
                        if (this.destinationDir != null) {
                            targetTopDir = new File(this.destinationDir);
                        } else {
                            String parent = f.getParent();
                            if (parent != null) {
                                File file = new File(parent);
                            }
                        }
                        for (int j = 0; j != compiled.length; j += 2) {
                            byte[] bytes = (byte[]) compiled[j + 1];
                            FileOutputStream os;
                            try {
                                os = new FileOutputStream(getOutputFile(targetTopDir, compiled[j]));
                                os.write(bytes);
                                os.close();
                            } catch (IOException ioe) {
                                addFormatedError(ioe.toString());
                            } catch (Throwable th) {
                                os.close();
                            }
                        }
                        i++;
                    } else {
                        return;
                    }
                }
                return;
            }
            addError("msg.extension.not.js", filename);
            return;
        }
    }

    private String readSource(File f) {
        String absPath = f.getAbsolutePath();
        if (f.isFile()) {
            try {
                return (String) SourceReader.readFileOrUrl(absPath, true, this.characterEncoding);
            } catch (FileNotFoundException e) {
                addError("msg.couldnt.open", absPath);
            } catch (IOException ioe) {
                addFormatedError(ioe.toString());
            }
        } else {
            addError("msg.jsfile.not.found", absPath);
            return null;
        }
        return null;
    }

    private File getOutputFile(File parentDir, String className) {
        File f = new File(parentDir, className.replace('.', File.separatorChar).concat(".class"));
        String dirPath = f.getParent();
        if (dirPath != null) {
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        return f;
    }

    /* access modifiers changed from: 0000 */
    public String getClassName(String name) {
        char[] s = new char[(name.length() + 1)];
        int j = 0;
        if (!Character.isJavaIdentifierStart(name.charAt(0))) {
            int j2 = 0 + 1;
            s[0] = '_';
            j = j2;
        }
        int i = 0;
        while (i < name.length()) {
            char c = name.charAt(i);
            if (Character.isJavaIdentifierPart(c)) {
                s[j] = c;
            } else {
                s[j] = '_';
            }
            i++;
            j++;
        }
        return new String(s).trim();
    }

    private static void p(String s) {
        System.out.println(s);
    }

    private void addError(String messageId, String arg) {
        String msg;
        if (arg == null) {
            msg = ToolErrorReporter.getMessage(messageId);
        } else {
            msg = ToolErrorReporter.getMessage(messageId, arg);
        }
        addFormatedError(msg);
    }

    private void addFormatedError(String message) {
        this.reporter.error(message, null, -1, null, -1);
    }
}
