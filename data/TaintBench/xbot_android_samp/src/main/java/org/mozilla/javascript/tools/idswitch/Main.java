package org.mozilla.javascript.tools.idswitch;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.java_websocket.framing.CloseFrame;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.tools.ToolErrorReporter;
import org.objectweb.asm.signature.SignatureVisitor;

public class Main {
    private static final int GENERATED_TAG = 2;
    private static final String GENERATED_TAG_STR = "generated";
    private static final int NORMAL_LINE = 0;
    private static final int STRING_TAG = 3;
    private static final String STRING_TAG_STR = "string";
    private static final int SWITCH_TAG = 1;
    private static final String SWITCH_TAG_STR = "string_id_map";
    private CodePrinter P;
    private ToolErrorReporter R;
    private final List<IdValuePair> all_pairs = new ArrayList();
    private FileBody body;
    private String source_file;
    private int tag_definition_end;
    private int tag_value_end;
    private int tag_value_start;

    private static boolean is_value_type(int id) {
        if (id == 3) {
            return true;
        }
        return false;
    }

    private static String tag_name(int id) {
        switch (id) {
            case CloseFrame.BUGGYCLOSE /*-2*/:
                return "/generated";
            case -1:
                return "/string_id_map";
            case 1:
                return SWITCH_TAG_STR;
            case 2:
                return GENERATED_TAG_STR;
            default:
                return "";
        }
    }

    /* access modifiers changed from: 0000 */
    public void process_file(String file_path) throws IOException {
        InputStream is;
        this.source_file = file_path;
        this.body = new FileBody();
        if (file_path.equals("-")) {
            is = System.in;
        } else {
            is = new FileInputStream(file_path);
        }
        try {
            this.body.readData(new InputStreamReader(is, "ASCII"));
            process_file();
            if (this.body.wasModified()) {
                OutputStream os;
                if (file_path.equals("-")) {
                    os = System.out;
                } else {
                    os = new FileOutputStream(file_path);
                }
                try {
                    Writer w = new OutputStreamWriter(os);
                    this.body.writeData(w);
                    w.flush();
                } finally {
                    os.close();
                }
            }
        } finally {
            is.close();
        }
    }

    private void process_file() {
        int cur_state = 0;
        char[] buffer = this.body.getBuffer();
        int generated_begin = -1;
        int generated_end = -1;
        int time_stamp_begin = -1;
        int time_stamp_end = -1;
        this.body.startLineLoop();
        while (this.body.nextLine()) {
            int begin = this.body.getLineBegin();
            int end = this.body.getLineEnd();
            int tag_id = extract_line_tag_id(buffer, begin, end);
            boolean bad_tag = false;
            switch (cur_state) {
                case 0:
                    if (tag_id == 1) {
                        cur_state = 1;
                        this.all_pairs.clear();
                        generated_begin = -1;
                        continue;
                    } else if (tag_id == -1) {
                        bad_tag = true;
                        continue;
                    } else {
                        continue;
                    }
                case 1:
                    if (tag_id == 0) {
                        look_for_id_definitions(buffer, begin, end, false);
                        continue;
                    } else if (tag_id == 3) {
                        look_for_id_definitions(buffer, begin, end, true);
                        continue;
                    } else if (tag_id == 2) {
                        if (generated_begin >= 0) {
                            bad_tag = true;
                            continue;
                        } else {
                            cur_state = 2;
                            time_stamp_begin = this.tag_definition_end;
                            time_stamp_end = end;
                            continue;
                        }
                    } else if (tag_id == -1) {
                        cur_state = 0;
                        if (generated_begin >= 0 && !this.all_pairs.isEmpty()) {
                            generate_java_code();
                            if (this.body.setReplacement(generated_begin, generated_end, this.P.toString())) {
                                this.body.setReplacement(time_stamp_begin, time_stamp_end, get_time_stamp());
                                continue;
                            } else {
                                continue;
                            }
                        }
                    } else {
                        bad_tag = true;
                        continue;
                    }
                case 2:
                    if (tag_id == 0) {
                        if (generated_begin < 0) {
                            generated_begin = begin;
                            continue;
                        } else {
                            continue;
                        }
                    } else if (tag_id == -2) {
                        if (generated_begin < 0) {
                            generated_begin = begin;
                        }
                        cur_state = 1;
                        generated_end = begin;
                        continue;
                    } else {
                        bad_tag = true;
                        continue;
                    }
                default:
                    break;
            }
            if (bad_tag) {
                throw this.R.runtimeError(ToolErrorReporter.getMessage("msg.idswitch.bad_tag_order", tag_name(tag_id)), this.source_file, this.body.getLineNumber(), null, 0);
            }
        }
        if (cur_state != 0) {
            throw this.R.runtimeError(ToolErrorReporter.getMessage("msg.idswitch.file_end_in_switch", tag_name(cur_state)), this.source_file, this.body.getLineNumber(), null, 0);
        }
    }

    private String get_time_stamp() {
        return new SimpleDateFormat(" 'Last update:' yyyy-MM-dd HH:mm:ss z").format(new Date());
    }

    private void generate_java_code() {
        this.P.clear();
        IdValuePair[] pairs = new IdValuePair[this.all_pairs.size()];
        this.all_pairs.toArray(pairs);
        SwitchGenerator g = new SwitchGenerator();
        g.char_tail_test_threshold = 2;
        g.setReporter(this.R);
        g.setCodePrinter(this.P);
        g.generateSwitch(pairs, "0");
    }

    private int extract_line_tag_id(char[] array, int cursor, int end) {
        int id = 0;
        cursor = skip_white_space(array, cursor, end);
        int after_leading_white_space = cursor;
        cursor = look_for_slash_slash(array, cursor, end);
        if (cursor != end) {
            boolean at_line_start = after_leading_white_space + 2 == cursor;
            cursor = skip_white_space(array, cursor, end);
            if (cursor != end && array[cursor] == '#') {
                int c;
                cursor++;
                boolean end_tag = false;
                if (cursor != end && array[cursor] == '/') {
                    cursor++;
                    end_tag = true;
                }
                int tag_start = cursor;
                while (cursor != end) {
                    c = array[cursor];
                    if (c == 35 || c == 61 || is_white_space(c)) {
                        break;
                    }
                    cursor++;
                }
                if (cursor != end) {
                    int tag_end = cursor;
                    cursor = skip_white_space(array, cursor, end);
                    if (cursor != end) {
                        c = array[cursor];
                        if (c == 61 || c == 35) {
                            id = get_tag_id(array, tag_start, tag_end, at_line_start);
                            if (id != 0) {
                                String bad = null;
                                if (c == 35) {
                                    if (end_tag) {
                                        id = -id;
                                        if (is_value_type(id)) {
                                            bad = "msg.idswitch.no_end_usage";
                                        }
                                    }
                                    this.tag_definition_end = cursor + 1;
                                } else {
                                    if (end_tag) {
                                        bad = "msg.idswitch.no_end_with_value";
                                    } else if (!is_value_type(id)) {
                                        bad = "msg.idswitch.no_value_allowed";
                                    }
                                    id = extract_tag_value(array, cursor + 1, end, id);
                                }
                                if (bad != null) {
                                    throw this.R.runtimeError(ToolErrorReporter.getMessage(bad, tag_name(id)), this.source_file, this.body.getLineNumber(), null, 0);
                                }
                            }
                        }
                    }
                }
            }
        }
        return id;
    }

    private int look_for_slash_slash(char[] array, int cursor, int end) {
        while (true) {
            int cursor2 = cursor;
            if (cursor2 + 2 <= end) {
                cursor = cursor2 + 1;
                if (array[cursor2] == 47) {
                    cursor2 = cursor + 1;
                    if (array[cursor] == 47) {
                        cursor = cursor2;
                        return cursor2;
                    }
                    cursor = cursor2;
                }
            } else {
                cursor = cursor2;
                return end;
            }
        }
    }

    private int extract_tag_value(char[] array, int cursor, int end, int id) {
        boolean found = false;
        cursor = skip_white_space(array, cursor, end);
        if (cursor != end) {
            int value_start = cursor;
            int value_end = cursor;
            while (cursor != end) {
                int c = array[cursor];
                if (is_white_space(c)) {
                    int after_space = skip_white_space(array, cursor + 1, end);
                    if (after_space != end && array[after_space] == '#') {
                        value_end = cursor;
                        cursor = after_space;
                        break;
                    }
                    cursor = after_space + 1;
                } else if (c == 35) {
                    value_end = cursor;
                    break;
                } else {
                    cursor++;
                }
            }
            if (cursor != end) {
                found = true;
                this.tag_value_start = value_start;
                this.tag_value_end = value_end;
                this.tag_definition_end = cursor + 1;
            }
        }
        return found ? id : 0;
    }

    private int get_tag_id(char[] array, int begin, int end, boolean at_line_start) {
        if (at_line_start) {
            if (equals(SWITCH_TAG_STR, array, begin, end)) {
                return 1;
            }
            if (equals(GENERATED_TAG_STR, array, begin, end)) {
                return 2;
            }
        }
        if (equals(STRING_TAG_STR, array, begin, end)) {
            return 3;
        }
        return 0;
    }

    private void look_for_id_definitions(char[] array, int begin, int end, boolean use_tag_value_as_string) {
        int cursor = skip_white_space(array, begin, end);
        int id_start = cursor;
        int name_start = skip_matched_prefix("Id_", array, cursor, end);
        if (name_start >= 0) {
            cursor = skip_name_char(array, name_start, end);
            int name_end = cursor;
            if (name_start != name_end) {
                cursor = skip_white_space(array, cursor, end);
                if (cursor != end && array[cursor] == SignatureVisitor.INSTANCEOF) {
                    int id_end = name_end;
                    if (use_tag_value_as_string) {
                        name_start = this.tag_value_start;
                        name_end = this.tag_value_end;
                    }
                    add_id(array, id_start, id_end, name_start, name_end);
                }
            }
        }
    }

    private void add_id(char[] array, int id_start, int id_end, int name_start, int name_end) {
        IdValuePair pair = new IdValuePair(new String(array, name_start, name_end - name_start), new String(array, id_start, id_end - id_start));
        pair.setLineNumber(this.body.getLineNumber());
        this.all_pairs.add(pair);
    }

    private static boolean is_white_space(int c) {
        return c == 32 || c == 9;
    }

    private static int skip_white_space(char[] array, int begin, int end) {
        int cursor = begin;
        while (cursor != end && is_white_space(array[cursor])) {
            cursor++;
        }
        return cursor;
    }

    private static int skip_matched_prefix(String prefix, char[] array, int begin, int end) {
        int prefix_length = prefix.length();
        if (prefix_length > end - begin) {
            return -1;
        }
        int cursor = begin;
        int i = 0;
        while (i != prefix_length) {
            if (prefix.charAt(i) != array[cursor]) {
                return -1;
            }
            i++;
            cursor++;
        }
        return cursor;
    }

    private static boolean equals(String str, char[] array, int begin, int end) {
        if (str.length() != end - begin) {
            return false;
        }
        int i = begin;
        int j = 0;
        while (i != end) {
            if (array[i] != str.charAt(j)) {
                return false;
            }
            i++;
            j++;
        }
        return true;
    }

    private static int skip_name_char(char[] array, int begin, int end) {
        int cursor = begin;
        while (cursor != end) {
            int c = array[cursor];
            if ((97 > c || c > 122) && ((65 > c || c > 90) && ((48 > c || c > 57) && c != 95))) {
                break;
            }
            cursor++;
        }
        return cursor;
    }

    public static void main(String[] args) {
        System.exit(new Main().exec(args));
    }

    private int exec(String[] args) {
        this.R = new ToolErrorReporter(true, System.err);
        int arg_count = process_options(args);
        if (arg_count == 0) {
            option_error(ToolErrorReporter.getMessage("msg.idswitch.no_file_argument"));
            return -1;
        } else if (arg_count > 1) {
            option_error(ToolErrorReporter.getMessage("msg.idswitch.too_many_arguments"));
            return -1;
        } else {
            this.P = new CodePrinter();
            this.P.setIndentStep(4);
            this.P.setIndentTabSize(0);
            try {
                process_file(args[0]);
                return 0;
            } catch (IOException ex) {
                print_error(ToolErrorReporter.getMessage("msg.idswitch.io_error", ex.toString()));
                return -1;
            } catch (EvaluatorException e) {
                return -1;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0027  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0035  */
    private int process_options(java.lang.String[] r15) {
        /*
        r14 = this;
        r13 = 0;
        r12 = 45;
        r10 = 2;
        r11 = 1;
        r8 = 1;
        r6 = 0;
        r7 = 0;
        r0 = r15.length;
        r4 = 0;
    L_0x000a:
        if (r4 == r0) goto L_0x0025;
    L_0x000c:
        r1 = r15[r4];
        r2 = r1.length();
        if (r2 < r10) goto L_0x0048;
    L_0x0014:
        r9 = 0;
        r9 = r1.charAt(r9);
        if (r9 != r12) goto L_0x0048;
    L_0x001b:
        r9 = r1.charAt(r11);
        if (r9 != r12) goto L_0x0060;
    L_0x0021:
        if (r2 != r10) goto L_0x003d;
    L_0x0023:
        r15[r4] = r13;
    L_0x0025:
        if (r8 != r11) goto L_0x0033;
    L_0x0027:
        if (r6 == 0) goto L_0x002d;
    L_0x0029:
        r14.show_usage();
        r8 = 0;
    L_0x002d:
        if (r7 == 0) goto L_0x0033;
    L_0x002f:
        r14.show_version();
        r8 = 0;
    L_0x0033:
        if (r8 == r11) goto L_0x0038;
    L_0x0035:
        java.lang.System.exit(r8);
    L_0x0038:
        r9 = r14.remove_nulls(r15);
        return r9;
    L_0x003d:
        r9 = "--help";
        r9 = r1.equals(r9);
        if (r9 == 0) goto L_0x004b;
    L_0x0045:
        r6 = 1;
    L_0x0046:
        r15[r4] = r13;
    L_0x0048:
        r4 = r4 + 1;
        goto L_0x000a;
    L_0x004b:
        r9 = "--version";
        r9 = r1.equals(r9);
        if (r9 == 0) goto L_0x0055;
    L_0x0053:
        r7 = 1;
        goto L_0x0046;
    L_0x0055:
        r9 = "msg.idswitch.bad_option";
        r9 = org.mozilla.javascript.tools.ToolErrorReporter.getMessage(r9, r1);
        r14.option_error(r9);
        r8 = -1;
        goto L_0x0025;
    L_0x0060:
        r5 = 1;
    L_0x0061:
        if (r5 == r2) goto L_0x0046;
    L_0x0063:
        r3 = r1.charAt(r5);
        switch(r3) {
            case 104: goto L_0x0079;
            default: goto L_0x006a;
        };
    L_0x006a:
        r9 = "msg.idswitch.bad_option_char";
        r10 = java.lang.String.valueOf(r3);
        r9 = org.mozilla.javascript.tools.ToolErrorReporter.getMessage(r9, r10);
        r14.option_error(r9);
        r8 = -1;
        goto L_0x0025;
    L_0x0079:
        r6 = 1;
        r5 = r5 + 1;
        goto L_0x0061;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.tools.idswitch.Main.process_options(java.lang.String[]):int");
    }

    private void show_usage() {
        System.out.println(ToolErrorReporter.getMessage("msg.idswitch.usage"));
        System.out.println();
    }

    private void show_version() {
        System.out.println(ToolErrorReporter.getMessage("msg.idswitch.version"));
    }

    private void option_error(String str) {
        print_error(ToolErrorReporter.getMessage("msg.idswitch.bad_invocation", str));
    }

    private void print_error(String text) {
        System.err.println(text);
    }

    private int remove_nulls(String[] array) {
        int N = array.length;
        int cursor = 0;
        while (cursor != N && array[cursor] != null) {
            cursor++;
        }
        int destination = cursor;
        if (cursor != N) {
            for (cursor++; cursor != N; cursor++) {
                String elem = array[cursor];
                if (elem != null) {
                    array[destination] = elem;
                    destination++;
                }
            }
        }
        return destination;
    }
}
