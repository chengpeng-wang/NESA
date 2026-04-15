package org.mozilla.javascript;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.objectweb.asm.signature.SignatureVisitor;

final class NativeDate extends IdScriptableObject {
    static final /* synthetic */ boolean $assertionsDisabled = (!NativeDate.class.desiredAssertionStatus());
    private static final int ConstructorId_UTC = -1;
    private static final int ConstructorId_now = -3;
    private static final int ConstructorId_parse = -2;
    private static final Object DATE_TAG = "Date";
    private static final double HalfTimeDomain = 8.64E15d;
    private static final double HoursPerDay = 24.0d;
    private static final int Id_constructor = 1;
    private static final int Id_getDate = 17;
    private static final int Id_getDay = 19;
    private static final int Id_getFullYear = 13;
    private static final int Id_getHours = 21;
    private static final int Id_getMilliseconds = 27;
    private static final int Id_getMinutes = 23;
    private static final int Id_getMonth = 15;
    private static final int Id_getSeconds = 25;
    private static final int Id_getTime = 11;
    private static final int Id_getTimezoneOffset = 29;
    private static final int Id_getUTCDate = 18;
    private static final int Id_getUTCDay = 20;
    private static final int Id_getUTCFullYear = 14;
    private static final int Id_getUTCHours = 22;
    private static final int Id_getUTCMilliseconds = 28;
    private static final int Id_getUTCMinutes = 24;
    private static final int Id_getUTCMonth = 16;
    private static final int Id_getUTCSeconds = 26;
    private static final int Id_getYear = 12;
    private static final int Id_setDate = 39;
    private static final int Id_setFullYear = 43;
    private static final int Id_setHours = 37;
    private static final int Id_setMilliseconds = 31;
    private static final int Id_setMinutes = 35;
    private static final int Id_setMonth = 41;
    private static final int Id_setSeconds = 33;
    private static final int Id_setTime = 30;
    private static final int Id_setUTCDate = 40;
    private static final int Id_setUTCFullYear = 44;
    private static final int Id_setUTCHours = 38;
    private static final int Id_setUTCMilliseconds = 32;
    private static final int Id_setUTCMinutes = 36;
    private static final int Id_setUTCMonth = 42;
    private static final int Id_setUTCSeconds = 34;
    private static final int Id_setYear = 45;
    private static final int Id_toDateString = 4;
    private static final int Id_toGMTString = 8;
    private static final int Id_toISOString = 46;
    private static final int Id_toJSON = 47;
    private static final int Id_toLocaleDateString = 7;
    private static final int Id_toLocaleString = 5;
    private static final int Id_toLocaleTimeString = 6;
    private static final int Id_toSource = 9;
    private static final int Id_toString = 2;
    private static final int Id_toTimeString = 3;
    private static final int Id_toUTCString = 8;
    private static final int Id_valueOf = 10;
    private static double LocalTZA = 0.0d;
    private static final int MAXARGS = 7;
    private static final int MAX_PROTOTYPE_ID = 47;
    private static final double MinutesPerDay = 1440.0d;
    private static final double MinutesPerHour = 60.0d;
    private static final double SecondsPerDay = 86400.0d;
    private static final double SecondsPerHour = 3600.0d;
    private static final double SecondsPerMinute = 60.0d;
    private static final String js_NaN_date_str = "Invalid Date";
    private static DateFormat localeDateFormatter = null;
    private static DateFormat localeDateTimeFormatter = null;
    private static DateFormat localeTimeFormatter = null;
    private static final double msPerDay = 8.64E7d;
    private static final double msPerHour = 3600000.0d;
    private static final double msPerMinute = 60000.0d;
    private static final double msPerSecond = 1000.0d;
    static final long serialVersionUID = -8307438915861678966L;
    private static TimeZone thisTimeZone;
    private static DateFormat timeZoneFormatter;
    private double date;

    static void init(Scriptable scope, boolean sealed) {
        NativeDate obj = new NativeDate();
        obj.date = ScriptRuntime.NaN;
        obj.exportAsJSClass(47, scope, sealed);
    }

    private NativeDate() {
        if (thisTimeZone == null) {
            thisTimeZone = TimeZone.getDefault();
            LocalTZA = (double) thisTimeZone.getRawOffset();
        }
    }

    public String getClassName() {
        return "Date";
    }

    public Object getDefaultValue(Class<?> typeHint) {
        if (typeHint == null) {
            typeHint = ScriptRuntime.StringClass;
        }
        return super.getDefaultValue(typeHint);
    }

    /* access modifiers changed from: 0000 */
    public double getJSTimeValue() {
        return this.date;
    }

    /* access modifiers changed from: protected */
    public void fillConstructorProperties(IdFunctionObject ctor) {
        addIdFunctionProperty(ctor, DATE_TAG, -3, "now", 0);
        addIdFunctionProperty(ctor, DATE_TAG, -2, "parse", 1);
        addIdFunctionProperty(ctor, DATE_TAG, -1, "UTC", 7);
        super.fillConstructorProperties(ctor);
    }

    /* access modifiers changed from: protected */
    public void initPrototypeId(int id) {
        int arity;
        String s;
        switch (id) {
            case 1:
                arity = 7;
                s = "constructor";
                break;
            case 2:
                arity = 0;
                s = "toString";
                break;
            case 3:
                arity = 0;
                s = "toTimeString";
                break;
            case 4:
                arity = 0;
                s = "toDateString";
                break;
            case 5:
                arity = 0;
                s = "toLocaleString";
                break;
            case 6:
                arity = 0;
                s = "toLocaleTimeString";
                break;
            case 7:
                arity = 0;
                s = "toLocaleDateString";
                break;
            case 8:
                arity = 0;
                s = "toUTCString";
                break;
            case 9:
                arity = 0;
                s = "toSource";
                break;
            case 10:
                arity = 0;
                s = "valueOf";
                break;
            case 11:
                arity = 0;
                s = "getTime";
                break;
            case 12:
                arity = 0;
                s = "getYear";
                break;
            case 13:
                arity = 0;
                s = "getFullYear";
                break;
            case 14:
                arity = 0;
                s = "getUTCFullYear";
                break;
            case 15:
                arity = 0;
                s = "getMonth";
                break;
            case 16:
                arity = 0;
                s = "getUTCMonth";
                break;
            case 17:
                arity = 0;
                s = "getDate";
                break;
            case 18:
                arity = 0;
                s = "getUTCDate";
                break;
            case 19:
                arity = 0;
                s = "getDay";
                break;
            case 20:
                arity = 0;
                s = "getUTCDay";
                break;
            case 21:
                arity = 0;
                s = "getHours";
                break;
            case 22:
                arity = 0;
                s = "getUTCHours";
                break;
            case 23:
                arity = 0;
                s = "getMinutes";
                break;
            case 24:
                arity = 0;
                s = "getUTCMinutes";
                break;
            case 25:
                arity = 0;
                s = "getSeconds";
                break;
            case 26:
                arity = 0;
                s = "getUTCSeconds";
                break;
            case 27:
                arity = 0;
                s = "getMilliseconds";
                break;
            case 28:
                arity = 0;
                s = "getUTCMilliseconds";
                break;
            case 29:
                arity = 0;
                s = "getTimezoneOffset";
                break;
            case 30:
                arity = 1;
                s = "setTime";
                break;
            case 31:
                arity = 1;
                s = "setMilliseconds";
                break;
            case 32:
                arity = 1;
                s = "setUTCMilliseconds";
                break;
            case 33:
                arity = 2;
                s = "setSeconds";
                break;
            case 34:
                arity = 2;
                s = "setUTCSeconds";
                break;
            case 35:
                arity = 3;
                s = "setMinutes";
                break;
            case 36:
                arity = 3;
                s = "setUTCMinutes";
                break;
            case 37:
                arity = 4;
                s = "setHours";
                break;
            case 38:
                arity = 4;
                s = "setUTCHours";
                break;
            case 39:
                arity = 1;
                s = "setDate";
                break;
            case 40:
                arity = 1;
                s = "setUTCDate";
                break;
            case 41:
                arity = 2;
                s = "setMonth";
                break;
            case 42:
                arity = 2;
                s = "setUTCMonth";
                break;
            case 43:
                arity = 3;
                s = "setFullYear";
                break;
            case 44:
                arity = 3;
                s = "setUTCFullYear";
                break;
            case 45:
                arity = 1;
                s = "setYear";
                break;
            case 46:
                arity = 0;
                s = "toISOString";
                break;
            case 47:
                arity = 1;
                s = "toJSON";
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
        initPrototypeMethod(DATE_TAG, id, s, arity);
    }

    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(DATE_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        }
        int id = f.methodId();
        switch (id) {
            case -3:
                return ScriptRuntime.wrapNumber(now());
            case -2:
                return ScriptRuntime.wrapNumber(date_parseString(ScriptRuntime.toString(args, 0)));
            case -1:
                return ScriptRuntime.wrapNumber(jsStaticFunction_UTC(args));
            case 1:
                if (thisObj != null) {
                    return date_format(now(), 2);
                }
                return jsConstructor(args);
            case 47:
                String toISOString = "toISOString";
                Scriptable o = ScriptRuntime.toObject(cx, scope, (Object) thisObj);
                Object tv = ScriptRuntime.toPrimitive(o, ScriptRuntime.NumberClass);
                if (tv instanceof Number) {
                    double d = ((Number) tv).doubleValue();
                    if (d != d || Double.isInfinite(d)) {
                        return null;
                    }
                }
                Object toISO = ScriptableObject.getProperty(o, "toISOString");
                if (toISO == NOT_FOUND) {
                    throw ScriptRuntime.typeError2("msg.function.not.found.in", "toISOString", ScriptRuntime.toString((Object) o));
                } else if (toISO instanceof Callable) {
                    Object result = ((Callable) toISO).call(cx, scope, o, ScriptRuntime.emptyArgs);
                    if (ScriptRuntime.isPrimitive(result)) {
                        return result;
                    }
                    throw ScriptRuntime.typeError1("msg.toisostring.must.return.primitive", ScriptRuntime.toString(result));
                } else {
                    throw ScriptRuntime.typeError3("msg.isnt.function.in", "toISOString", ScriptRuntime.toString((Object) o), ScriptRuntime.toString(toISO));
                }
            default:
                if (thisObj instanceof NativeDate) {
                    NativeDate realThis = (NativeDate) thisObj;
                    double t = realThis.date;
                    switch (id) {
                        case 2:
                        case 3:
                        case 4:
                            if (t == t) {
                                return date_format(t, id);
                            }
                            return js_NaN_date_str;
                        case 5:
                        case 6:
                        case 7:
                            if (t == t) {
                                return toLocale_helper(t, id);
                            }
                            return js_NaN_date_str;
                        case 8:
                            if (t == t) {
                                return js_toUTCString(t);
                            }
                            return js_NaN_date_str;
                        case 9:
                            return "(new Date(" + ScriptRuntime.toString(t) + "))";
                        case 10:
                        case 11:
                            return ScriptRuntime.wrapNumber(t);
                        case 12:
                        case 13:
                        case 14:
                            if (t == t) {
                                if (id != 14) {
                                    t = LocalTime(t);
                                }
                                t = (double) YearFromTime(t);
                                if (id == 12) {
                                    if (!cx.hasFeature(1)) {
                                        t -= 1900.0d;
                                    } else if (1900.0d <= t && t < 2000.0d) {
                                        t -= 1900.0d;
                                    }
                                }
                            }
                            return ScriptRuntime.wrapNumber(t);
                        case 15:
                        case 16:
                            if (t == t) {
                                if (id == 15) {
                                    t = LocalTime(t);
                                }
                                t = (double) MonthFromTime(t);
                            }
                            return ScriptRuntime.wrapNumber(t);
                        case 17:
                        case 18:
                            if (t == t) {
                                if (id == 17) {
                                    t = LocalTime(t);
                                }
                                t = (double) DateFromTime(t);
                            }
                            return ScriptRuntime.wrapNumber(t);
                        case 19:
                        case 20:
                            if (t == t) {
                                if (id == 19) {
                                    t = LocalTime(t);
                                }
                                t = (double) WeekDay(t);
                            }
                            return ScriptRuntime.wrapNumber(t);
                        case 21:
                        case 22:
                            if (t == t) {
                                if (id == 21) {
                                    t = LocalTime(t);
                                }
                                t = (double) HourFromTime(t);
                            }
                            return ScriptRuntime.wrapNumber(t);
                        case 23:
                        case 24:
                            if (t == t) {
                                if (id == 23) {
                                    t = LocalTime(t);
                                }
                                t = (double) MinFromTime(t);
                            }
                            return ScriptRuntime.wrapNumber(t);
                        case 25:
                        case 26:
                            if (t == t) {
                                if (id == 25) {
                                    t = LocalTime(t);
                                }
                                t = (double) SecFromTime(t);
                            }
                            return ScriptRuntime.wrapNumber(t);
                        case 27:
                        case 28:
                            if (t == t) {
                                if (id == 27) {
                                    t = LocalTime(t);
                                }
                                t = (double) msFromTime(t);
                            }
                            return ScriptRuntime.wrapNumber(t);
                        case 29:
                            if (t == t) {
                                t = (t - LocalTime(t)) / msPerMinute;
                            }
                            return ScriptRuntime.wrapNumber(t);
                        case 30:
                            t = TimeClip(ScriptRuntime.toNumber(args, 0));
                            realThis.date = t;
                            return ScriptRuntime.wrapNumber(t);
                        case 31:
                        case 32:
                        case 33:
                        case 34:
                        case 35:
                        case 36:
                        case 37:
                        case 38:
                            t = makeTime(t, args, id);
                            realThis.date = t;
                            return ScriptRuntime.wrapNumber(t);
                        case 39:
                        case 40:
                        case 41:
                        case 42:
                        case 43:
                        case 44:
                            t = makeDate(t, args, id);
                            realThis.date = t;
                            return ScriptRuntime.wrapNumber(t);
                        case 45:
                            double year = ScriptRuntime.toNumber(args, 0);
                            if (year != year || Double.isInfinite(year)) {
                                t = ScriptRuntime.NaN;
                            } else {
                                if (t != t) {
                                    t = 0.0d;
                                } else {
                                    t = LocalTime(t);
                                }
                                if (year >= 0.0d && year <= 99.0d) {
                                    year += 1900.0d;
                                }
                                t = TimeClip(internalUTC(MakeDate(MakeDay(year, (double) MonthFromTime(t), (double) DateFromTime(t)), TimeWithinDay(t))));
                            }
                            realThis.date = t;
                            return ScriptRuntime.wrapNumber(t);
                        case 46:
                            if (t == t) {
                                return js_toISOString(t);
                            }
                            throw ScriptRuntime.constructError("RangeError", ScriptRuntime.getMessage0("msg.invalid.date"));
                        default:
                            throw new IllegalArgumentException(String.valueOf(id));
                    }
                }
                throw IdScriptableObject.incompatibleCallError(f);
        }
    }

    private static double Day(double t) {
        return Math.floor(t / msPerDay);
    }

    private static double TimeWithinDay(double t) {
        double result = t % msPerDay;
        if (result < 0.0d) {
            return result + msPerDay;
        }
        return result;
    }

    private static boolean IsLeapYear(int year) {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }

    private static double DayFromYear(double y) {
        return (((365.0d * (y - 1970.0d)) + Math.floor((y - 1969.0d) / 4.0d)) - Math.floor((y - 1901.0d) / 100.0d)) + Math.floor((y - 1601.0d) / 400.0d);
    }

    private static double TimeFromYear(double y) {
        return DayFromYear(y) * msPerDay;
    }

    private static int YearFromTime(double t) {
        int lo = ((int) Math.floor((t / msPerDay) / 366.0d)) + 1970;
        int hi = ((int) Math.floor((t / msPerDay) / 365.0d)) + 1970;
        if (hi < lo) {
            int temp = lo;
            lo = hi;
            hi = temp;
        }
        while (hi > lo) {
            int mid = (hi + lo) / 2;
            if (TimeFromYear((double) mid) > t) {
                hi = mid - 1;
            } else {
                lo = mid + 1;
                if (TimeFromYear((double) lo) > t) {
                    return mid;
                }
            }
        }
        return lo;
    }

    private static double DayFromMonth(int m, int year) {
        int day = m * 30;
        if (m >= 7) {
            day += (m / 2) - 1;
        } else if (m >= 2) {
            day += ((m - 1) / 2) - 1;
        } else {
            day += m;
        }
        if (m >= 2 && IsLeapYear(year)) {
            day++;
        }
        return (double) day;
    }

    private static int DaysInMonth(int year, int month) {
        return month == 2 ? IsLeapYear(year) ? 29 : 28 : month >= 8 ? 31 - (month & 1) : (month & 1) + 30;
    }

    private static int MonthFromTime(double t) {
        int year = YearFromTime(t);
        int d = ((int) (Day(t) - DayFromYear((double) year))) - 59;
        if (d >= 0) {
            int mstart;
            if (IsLeapYear(year)) {
                if (d == 0) {
                    return 1;
                }
                d--;
            }
            int estimate = d / 30;
            switch (estimate) {
                case 0:
                    return 2;
                case 1:
                    mstart = 31;
                    break;
                case 2:
                    mstart = 61;
                    break;
                case 3:
                    mstart = 92;
                    break;
                case 4:
                    mstart = 122;
                    break;
                case 5:
                    mstart = 153;
                    break;
                case 6:
                    mstart = 184;
                    break;
                case 7:
                    mstart = 214;
                    break;
                case 8:
                    mstart = 245;
                    break;
                case 9:
                    mstart = 275;
                    break;
                case 10:
                    return 11;
                default:
                    throw Kit.codeBug();
            }
            if (d >= mstart) {
                return estimate + 2;
            }
            return estimate + 1;
        } else if (d < -28) {
            return 0;
        } else {
            return 1;
        }
    }

    private static int DateFromTime(double t) {
        int year = YearFromTime(t);
        int d = ((int) (Day(t) - DayFromYear((double) year))) - 59;
        if (d >= 0) {
            int mdays;
            int mstart;
            if (IsLeapYear(year)) {
                if (d == 0) {
                    return 29;
                }
                d--;
            }
            switch (d / 30) {
                case 0:
                    return d + 1;
                case 1:
                    mdays = 31;
                    mstart = 31;
                    break;
                case 2:
                    mdays = 30;
                    mstart = 61;
                    break;
                case 3:
                    mdays = 31;
                    mstart = 92;
                    break;
                case 4:
                    mdays = 30;
                    mstart = 122;
                    break;
                case 5:
                    mdays = 31;
                    mstart = 153;
                    break;
                case 6:
                    mdays = 31;
                    mstart = 184;
                    break;
                case 7:
                    mdays = 30;
                    mstart = 214;
                    break;
                case 8:
                    mdays = 31;
                    mstart = 245;
                    break;
                case 9:
                    mdays = 30;
                    mstart = 275;
                    break;
                case 10:
                    return (d - 275) + 1;
                default:
                    throw Kit.codeBug();
            }
            d -= mstart;
            if (d < 0) {
                d += mdays;
            }
            return d + 1;
        } else if (d < -28) {
            return ((d + 31) + 28) + 1;
        } else {
            return (d + 28) + 1;
        }
    }

    private static int WeekDay(double t) {
        double result = (Day(t) + 4.0d) % 7.0d;
        if (result < 0.0d) {
            result += 7.0d;
        }
        return (int) result;
    }

    private static double now() {
        return (double) System.currentTimeMillis();
    }

    private static double DaylightSavingTA(double t) {
        if (t < 0.0d) {
            t = MakeDate(MakeDay((double) EquivalentYear(YearFromTime(t)), (double) MonthFromTime(t), (double) DateFromTime(t)), TimeWithinDay(t));
        }
        if (thisTimeZone.inDaylightTime(new Date((long) t))) {
            return msPerHour;
        }
        return 0.0d;
    }

    private static int EquivalentYear(int year) {
        int day = (((int) DayFromYear((double) year)) + 4) % 7;
        if (day < 0) {
            day += 7;
        }
        if (IsLeapYear(year)) {
            switch (day) {
                case 0:
                    return 1984;
                case 1:
                    return 1996;
                case 2:
                    return 1980;
                case 3:
                    return 1992;
                case 4:
                    return 1976;
                case 5:
                    return 1988;
                case 6:
                    return 1972;
            }
        }
        switch (day) {
            case 0:
                return 1978;
            case 1:
                return 1973;
            case 2:
                return 1985;
            case 3:
                return 1986;
            case 4:
                return 1981;
            case 5:
                return 1971;
            case 6:
                return 1977;
        }
        throw Kit.codeBug();
    }

    private static double LocalTime(double t) {
        return (LocalTZA + t) + DaylightSavingTA(t);
    }

    private static double internalUTC(double t) {
        return (t - LocalTZA) - DaylightSavingTA(t - LocalTZA);
    }

    private static int HourFromTime(double t) {
        double result = Math.floor(t / msPerHour) % HoursPerDay;
        if (result < 0.0d) {
            result += HoursPerDay;
        }
        return (int) result;
    }

    private static int MinFromTime(double t) {
        double result = Math.floor(t / msPerMinute) % 60.0d;
        if (result < 0.0d) {
            result += 60.0d;
        }
        return (int) result;
    }

    private static int SecFromTime(double t) {
        double result = Math.floor(t / msPerSecond) % 60.0d;
        if (result < 0.0d) {
            result += 60.0d;
        }
        return (int) result;
    }

    private static int msFromTime(double t) {
        double result = t % msPerSecond;
        if (result < 0.0d) {
            result += msPerSecond;
        }
        return (int) result;
    }

    private static double MakeTime(double hour, double min, double sec, double ms) {
        return (((((hour * 60.0d) + min) * 60.0d) + sec) * msPerSecond) + ms;
    }

    private static double MakeDay(double year, double month, double date) {
        year += Math.floor(month / 12.0d);
        month %= 12.0d;
        if (month < 0.0d) {
            month += 12.0d;
        }
        return ((Math.floor(TimeFromYear(year) / msPerDay) + DayFromMonth((int) month, (int) year)) + date) - 1.0d;
    }

    private static double MakeDate(double day, double time) {
        return (msPerDay * day) + time;
    }

    private static double TimeClip(double d) {
        if (d != d || d == Double.POSITIVE_INFINITY || d == Double.NEGATIVE_INFINITY || Math.abs(d) > HalfTimeDomain) {
            return ScriptRuntime.NaN;
        }
        if (d > 0.0d) {
            return Math.floor(d + 0.0d);
        }
        return Math.ceil(d + 0.0d);
    }

    private static double date_msecFromDate(double year, double mon, double mday, double hour, double min, double sec, double msec) {
        return MakeDate(MakeDay(year, mon, mday), MakeTime(hour, min, sec, msec));
    }

    private static double date_msecFromArgs(Object[] args) {
        double[] array = new double[7];
        for (int loop = 0; loop < 7; loop++) {
            if (loop < args.length) {
                double d = ScriptRuntime.toNumber(args[loop]);
                if (d != d || Double.isInfinite(d)) {
                    return ScriptRuntime.NaN;
                }
                array[loop] = ScriptRuntime.toInteger(args[loop]);
            } else if (loop == 2) {
                array[loop] = 1.0d;
            } else {
                array[loop] = 0.0d;
            }
        }
        if (array[0] >= 0.0d && array[0] <= 99.0d) {
            array[0] = array[0] + 1900.0d;
        }
        return date_msecFromDate(array[0], array[1], array[2], array[3], array[4], array[5], array[6]);
    }

    private static double jsStaticFunction_UTC(Object[] args) {
        return TimeClip(date_msecFromArgs(args));
    }

    /* JADX WARNING: Removed duplicated region for block: B:131:0x0219  */
    private static double parseISOString(java.lang.String r48) {
        /*
        r17 = -1;
        r25 = 0;
        r20 = 1;
        r16 = 2;
        r18 = 3;
        r19 = 4;
        r22 = 5;
        r21 = 6;
        r23 = 7;
        r24 = 8;
        r39 = 0;
        r2 = 9;
        r0 = new int[r2];
        r44 = r0;
        r44 = {1970, 1, 1, 0, 0, 0, 0, -1, -1};
        r46 = 4;
        r47 = 1;
        r42 = 1;
        r31 = 0;
        r33 = r48.length();
        if (r33 == 0) goto L_0x004c;
    L_0x002d:
        r2 = 0;
        r0 = r48;
        r26 = r0.charAt(r2);
        r2 = 43;
        r0 = r26;
        if (r0 == r2) goto L_0x0040;
    L_0x003a:
        r2 = 45;
        r0 = r26;
        if (r0 != r2) goto L_0x0070;
    L_0x0040:
        r31 = r31 + 1;
        r46 = 6;
        r2 = 45;
        r0 = r26;
        if (r0 != r2) goto L_0x006d;
    L_0x004a:
        r47 = -1;
    L_0x004c:
        r2 = -1;
        r0 = r39;
        if (r0 == r2) goto L_0x005f;
    L_0x0051:
        if (r39 != 0) goto L_0x007b;
    L_0x0053:
        r2 = r46;
    L_0x0055:
        r34 = r31 + r2;
        r0 = r34;
        r1 = r33;
        if (r0 <= r1) goto L_0x0084;
    L_0x005d:
        r39 = -1;
    L_0x005f:
        r2 = -1;
        r0 = r39;
        if (r0 == r2) goto L_0x006a;
    L_0x0064:
        r0 = r31;
        r1 = r33;
        if (r0 == r1) goto L_0x017e;
    L_0x006a:
        r27 = org.mozilla.javascript.ScriptRuntime.NaN;
    L_0x006c:
        return r27;
    L_0x006d:
        r47 = 1;
        goto L_0x004c;
    L_0x0070:
        r2 = 84;
        r0 = r26;
        if (r0 != r2) goto L_0x004c;
    L_0x0076:
        r31 = r31 + 1;
        r39 = 3;
        goto L_0x004c;
    L_0x007b:
        r2 = 6;
        r0 = r39;
        if (r0 != r2) goto L_0x0082;
    L_0x0080:
        r2 = 3;
        goto L_0x0055;
    L_0x0082:
        r2 = 2;
        goto L_0x0055;
    L_0x0084:
        r43 = 0;
        r32 = r31;
    L_0x0088:
        r0 = r32;
        r1 = r34;
        if (r0 >= r1) goto L_0x00b2;
    L_0x008e:
        r0 = r48;
        r1 = r32;
        r26 = r0.charAt(r1);
        r2 = 48;
        r0 = r26;
        if (r0 < r2) goto L_0x00a2;
    L_0x009c:
        r2 = 57;
        r0 = r26;
        if (r0 <= r2) goto L_0x00a7;
    L_0x00a2:
        r39 = -1;
        r31 = r32;
        goto L_0x005f;
    L_0x00a7:
        r2 = r43 * 10;
        r3 = r26 + -48;
        r43 = r2 + r3;
        r31 = r32 + 1;
        r32 = r31;
        goto L_0x0088;
    L_0x00b2:
        r44[r39] = r43;
        r0 = r32;
        r1 = r33;
        if (r0 != r1) goto L_0x00c3;
    L_0x00ba:
        switch(r39) {
            case 3: goto L_0x00c0;
            case 7: goto L_0x00c0;
            default: goto L_0x00bd;
        };
    L_0x00bd:
        r31 = r32;
        goto L_0x005f;
    L_0x00c0:
        r39 = -1;
        goto L_0x00bd;
    L_0x00c3:
        r31 = r32 + 1;
        r0 = r48;
        r1 = r32;
        r26 = r0.charAt(r1);
        r2 = 90;
        r0 = r26;
        if (r0 != r2) goto L_0x00e3;
    L_0x00d3:
        r2 = 7;
        r3 = 0;
        r44[r2] = r3;
        r2 = 8;
        r3 = 0;
        r44[r2] = r3;
        switch(r39) {
            case 4: goto L_0x005f;
            case 5: goto L_0x005f;
            case 6: goto L_0x005f;
            default: goto L_0x00df;
        };
    L_0x00df:
        r39 = -1;
        goto L_0x005f;
    L_0x00e3:
        switch(r39) {
            case 0: goto L_0x00f5;
            case 1: goto L_0x00f5;
            case 2: goto L_0x010a;
            case 3: goto L_0x0116;
            case 4: goto L_0x012d;
            case 5: goto L_0x0148;
            case 6: goto L_0x0163;
            case 7: goto L_0x0122;
            case 8: goto L_0x0176;
            default: goto L_0x00e6;
        };
    L_0x00e6:
        r2 = 7;
        r0 = r39;
        if (r0 != r2) goto L_0x004c;
    L_0x00eb:
        r2 = 45;
        r0 = r26;
        if (r0 != r2) goto L_0x017a;
    L_0x00f1:
        r42 = -1;
    L_0x00f3:
        goto L_0x004c;
    L_0x00f5:
        r2 = 45;
        r0 = r26;
        if (r0 != r2) goto L_0x00fe;
    L_0x00fb:
        r39 = r39 + 1;
    L_0x00fd:
        goto L_0x00e6;
    L_0x00fe:
        r2 = 84;
        r0 = r26;
        if (r0 != r2) goto L_0x0107;
    L_0x0104:
        r39 = 3;
        goto L_0x00fd;
    L_0x0107:
        r39 = -1;
        goto L_0x00fd;
    L_0x010a:
        r2 = 84;
        r0 = r26;
        if (r0 != r2) goto L_0x0113;
    L_0x0110:
        r39 = 3;
    L_0x0112:
        goto L_0x00e6;
    L_0x0113:
        r39 = -1;
        goto L_0x0112;
    L_0x0116:
        r2 = 58;
        r0 = r26;
        if (r0 != r2) goto L_0x011f;
    L_0x011c:
        r39 = 4;
    L_0x011e:
        goto L_0x00e6;
    L_0x011f:
        r39 = -1;
        goto L_0x011e;
    L_0x0122:
        r2 = 58;
        r0 = r26;
        if (r0 == r2) goto L_0x012a;
    L_0x0128:
        r31 = r31 + -1;
    L_0x012a:
        r39 = 8;
        goto L_0x00e6;
    L_0x012d:
        r2 = 58;
        r0 = r26;
        if (r0 != r2) goto L_0x0136;
    L_0x0133:
        r39 = 5;
    L_0x0135:
        goto L_0x00e6;
    L_0x0136:
        r2 = 43;
        r0 = r26;
        if (r0 == r2) goto L_0x0142;
    L_0x013c:
        r2 = 45;
        r0 = r26;
        if (r0 != r2) goto L_0x0145;
    L_0x0142:
        r39 = 7;
        goto L_0x0135;
    L_0x0145:
        r39 = -1;
        goto L_0x0135;
    L_0x0148:
        r2 = 46;
        r0 = r26;
        if (r0 != r2) goto L_0x0151;
    L_0x014e:
        r39 = 6;
    L_0x0150:
        goto L_0x00e6;
    L_0x0151:
        r2 = 43;
        r0 = r26;
        if (r0 == r2) goto L_0x015d;
    L_0x0157:
        r2 = 45;
        r0 = r26;
        if (r0 != r2) goto L_0x0160;
    L_0x015d:
        r39 = 7;
        goto L_0x0150;
    L_0x0160:
        r39 = -1;
        goto L_0x0150;
    L_0x0163:
        r2 = 43;
        r0 = r26;
        if (r0 == r2) goto L_0x016f;
    L_0x0169:
        r2 = 45;
        r0 = r26;
        if (r0 != r2) goto L_0x0173;
    L_0x016f:
        r39 = 7;
    L_0x0171:
        goto L_0x00e6;
    L_0x0173:
        r39 = -1;
        goto L_0x0171;
    L_0x0176:
        r39 = -1;
        goto L_0x00e6;
    L_0x017a:
        r42 = 1;
        goto L_0x00f3;
    L_0x017e:
        r2 = 0;
        r45 = r44[r2];
        r2 = 1;
        r36 = r44[r2];
        r2 = 2;
        r29 = r44[r2];
        r2 = 3;
        r30 = r44[r2];
        r2 = 4;
        r35 = r44[r2];
        r2 = 5;
        r38 = r44[r2];
        r2 = 6;
        r37 = r44[r2];
        r2 = 7;
        r40 = r44[r2];
        r2 = 8;
        r41 = r44[r2];
        r2 = 275943; // 0x435e7 float:3.86679E-40 double:1.36334E-318;
        r0 = r45;
        if (r0 > r2) goto L_0x006a;
    L_0x01a1:
        r2 = 1;
        r0 = r36;
        if (r0 < r2) goto L_0x006a;
    L_0x01a6:
        r2 = 12;
        r0 = r36;
        if (r0 > r2) goto L_0x006a;
    L_0x01ac:
        r2 = 1;
        r0 = r29;
        if (r0 < r2) goto L_0x006a;
    L_0x01b1:
        r0 = r45;
        r1 = r36;
        r2 = DaysInMonth(r0, r1);
        r0 = r29;
        if (r0 > r2) goto L_0x006a;
    L_0x01bd:
        r2 = 24;
        r0 = r30;
        if (r0 > r2) goto L_0x006a;
    L_0x01c3:
        r2 = 24;
        r0 = r30;
        if (r0 != r2) goto L_0x01cf;
    L_0x01c9:
        if (r35 > 0) goto L_0x006a;
    L_0x01cb:
        if (r38 > 0) goto L_0x006a;
    L_0x01cd:
        if (r37 > 0) goto L_0x006a;
    L_0x01cf:
        r2 = 59;
        r0 = r35;
        if (r0 > r2) goto L_0x006a;
    L_0x01d5:
        r2 = 59;
        r0 = r38;
        if (r0 > r2) goto L_0x006a;
    L_0x01db:
        r2 = 23;
        r0 = r40;
        if (r0 > r2) goto L_0x006a;
    L_0x01e1:
        r2 = 59;
        r0 = r41;
        if (r0 > r2) goto L_0x006a;
    L_0x01e7:
        r2 = r45 * r47;
        r2 = (double) r2;
        r4 = r36 + -1;
        r4 = (double) r4;
        r0 = r29;
        r6 = (double) r0;
        r0 = r30;
        r8 = (double) r0;
        r0 = r35;
        r10 = (double) r0;
        r0 = r38;
        r12 = (double) r0;
        r0 = r37;
        r14 = (double) r0;
        r27 = date_msecFromDate(r2, r4, r6, r8, r10, r12, r14);
        r2 = -1;
        r0 = r40;
        if (r0 != r2) goto L_0x0219;
    L_0x0205:
        r2 = -4377866037058863104; // 0xc33eb208c2dc0000 float:-110.0 double:-8.64E15;
        r2 = (r27 > r2 ? 1 : (r27 == r2 ? 0 : -1));
        if (r2 < 0) goto L_0x006a;
    L_0x020e:
        r2 = 4845505999795912704; // 0x433eb208c2dc0000 float:-110.0 double:8.64E15;
        r2 = (r27 > r2 ? 1 : (r27 == r2 ? 0 : -1));
        if (r2 <= 0) goto L_0x006c;
    L_0x0217:
        goto L_0x006a;
    L_0x0219:
        r2 = r40 * 60;
        r2 = r2 + r41;
        r2 = (double) r2;
        r4 = 4678479150791524352; // 0x40ed4c0000000000 float:0.0 double:60000.0;
        r2 = r2 * r4;
        r0 = r42;
        r4 = (double) r0;
        r2 = r2 * r4;
        r27 = r27 - r2;
        goto L_0x0205;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.NativeDate.parseISOString(java.lang.String):double");
    }

    /* JADX WARNING: Removed duplicated region for block: B:148:0x021c  */
    /* JADX WARNING: Removed duplicated region for block: B:204:0x0215 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00fc  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00df  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00e7  */
    private static double date_parseString(java.lang.String r44) {
        /*
        r23 = parseISOString(r44);
        r3 = (r23 > r23 ? 1 : (r23 == r23 ? 0 : -1));
        if (r3 != 0) goto L_0x0009;
    L_0x0008:
        return r23;
    L_0x0009:
        r43 = -1;
        r32 = -1;
        r30 = -1;
        r26 = -1;
        r31 = -1;
        r37 = -1;
        r22 = 0;
        r39 = 0;
        r27 = 0;
        r35 = -1;
        r40 = -4616189618054758400; // 0xbff0000000000000 float:0.0 double:-1.0;
        r36 = 0;
        r29 = 0;
        r38 = 0;
        r29 = r44.length();
    L_0x0029:
        r0 = r27;
        r1 = r29;
        if (r0 >= r1) goto L_0x02c3;
    L_0x002f:
        r0 = r44;
        r1 = r27;
        r22 = r0.charAt(r1);
        r27 = r27 + 1;
        r3 = 32;
        r0 = r22;
        if (r0 <= r3) goto L_0x004b;
    L_0x003f:
        r3 = 44;
        r0 = r22;
        if (r0 == r3) goto L_0x004b;
    L_0x0045:
        r3 = 45;
        r0 = r22;
        if (r0 != r3) goto L_0x006e;
    L_0x004b:
        r0 = r27;
        r1 = r29;
        if (r0 >= r1) goto L_0x0029;
    L_0x0051:
        r0 = r44;
        r1 = r27;
        r39 = r0.charAt(r1);
        r3 = 45;
        r0 = r22;
        if (r0 != r3) goto L_0x0029;
    L_0x005f:
        r3 = 48;
        r0 = r39;
        if (r3 > r0) goto L_0x0029;
    L_0x0065:
        r3 = 57;
        r0 = r39;
        if (r0 > r3) goto L_0x0029;
    L_0x006b:
        r36 = r22;
        goto L_0x0029;
    L_0x006e:
        r3 = 40;
        r0 = r22;
        if (r0 != r3) goto L_0x009a;
    L_0x0074:
        r25 = 1;
    L_0x0076:
        r0 = r27;
        r1 = r29;
        if (r0 >= r1) goto L_0x0029;
    L_0x007c:
        r0 = r44;
        r1 = r27;
        r22 = r0.charAt(r1);
        r27 = r27 + 1;
        r3 = 40;
        r0 = r22;
        if (r0 != r3) goto L_0x008f;
    L_0x008c:
        r25 = r25 + 1;
        goto L_0x0076;
    L_0x008f:
        r3 = 41;
        r0 = r22;
        if (r0 != r3) goto L_0x0076;
    L_0x0095:
        r25 = r25 + -1;
        if (r25 > 0) goto L_0x0076;
    L_0x0099:
        goto L_0x0029;
    L_0x009a:
        r3 = 48;
        r0 = r22;
        if (r3 > r0) goto L_0x01cc;
    L_0x00a0:
        r3 = 57;
        r0 = r22;
        if (r0 > r3) goto L_0x01cc;
    L_0x00a6:
        r35 = r22 + -48;
    L_0x00a8:
        r0 = r27;
        r1 = r29;
        if (r0 >= r1) goto L_0x00cb;
    L_0x00ae:
        r3 = 48;
        r0 = r44;
        r1 = r27;
        r22 = r0.charAt(r1);
        r0 = r22;
        if (r3 > r0) goto L_0x00cb;
    L_0x00bc:
        r3 = 57;
        r0 = r22;
        if (r0 > r3) goto L_0x00cb;
    L_0x00c2:
        r3 = r35 * 10;
        r3 = r3 + r22;
        r35 = r3 + -48;
        r27 = r27 + 1;
        goto L_0x00a8;
    L_0x00cb:
        r3 = 43;
        r0 = r36;
        if (r0 == r3) goto L_0x00d7;
    L_0x00d1:
        r3 = 45;
        r0 = r36;
        if (r0 != r3) goto L_0x010e;
    L_0x00d7:
        r38 = 1;
        r3 = 24;
        r0 = r35;
        if (r0 >= r3) goto L_0x00fc;
    L_0x00df:
        r35 = r35 * 60;
    L_0x00e1:
        r3 = 43;
        r0 = r36;
        if (r0 != r3) goto L_0x00ec;
    L_0x00e7:
        r0 = r35;
        r0 = -r0;
        r35 = r0;
    L_0x00ec:
        r8 = 0;
        r3 = (r40 > r8 ? 1 : (r40 == r8 ? 0 : -1));
        if (r3 == 0) goto L_0x0105;
    L_0x00f2:
        r8 = -4616189618054758400; // 0xbff0000000000000 float:0.0 double:-1.0;
        r3 = (r40 > r8 ? 1 : (r40 == r8 ? 0 : -1));
        if (r3 == 0) goto L_0x0105;
    L_0x00f8:
        r23 = org.mozilla.javascript.ScriptRuntime.NaN;
        goto L_0x0008;
    L_0x00fc:
        r3 = r35 % 100;
        r5 = r35 / 100;
        r5 = r5 * 60;
        r35 = r3 + r5;
        goto L_0x00e1;
    L_0x0105:
        r0 = r35;
        r0 = (double) r0;
        r40 = r0;
    L_0x010a:
        r36 = 0;
        goto L_0x0029;
    L_0x010e:
        r3 = 70;
        r0 = r35;
        if (r0 >= r3) goto L_0x0120;
    L_0x0114:
        r3 = 47;
        r0 = r36;
        if (r0 != r3) goto L_0x0152;
    L_0x011a:
        if (r32 < 0) goto L_0x0152;
    L_0x011c:
        if (r30 < 0) goto L_0x0152;
    L_0x011e:
        if (r43 >= 0) goto L_0x0152;
    L_0x0120:
        if (r43 < 0) goto L_0x0126;
    L_0x0122:
        r23 = org.mozilla.javascript.ScriptRuntime.NaN;
        goto L_0x0008;
    L_0x0126:
        r3 = 32;
        r0 = r22;
        if (r0 <= r3) goto L_0x013e;
    L_0x012c:
        r3 = 44;
        r0 = r22;
        if (r0 == r3) goto L_0x013e;
    L_0x0132:
        r3 = 47;
        r0 = r22;
        if (r0 == r3) goto L_0x013e;
    L_0x0138:
        r0 = r27;
        r1 = r29;
        if (r0 < r1) goto L_0x014e;
    L_0x013e:
        r3 = 100;
        r0 = r35;
        if (r0 >= r3) goto L_0x014b;
    L_0x0144:
        r0 = r35;
        r0 = r0 + 1900;
        r43 = r0;
    L_0x014a:
        goto L_0x010a;
    L_0x014b:
        r43 = r35;
        goto L_0x014a;
    L_0x014e:
        r23 = org.mozilla.javascript.ScriptRuntime.NaN;
        goto L_0x0008;
    L_0x0152:
        r3 = 58;
        r0 = r22;
        if (r0 != r3) goto L_0x0166;
    L_0x0158:
        if (r26 >= 0) goto L_0x015d;
    L_0x015a:
        r26 = r35;
        goto L_0x010a;
    L_0x015d:
        if (r31 >= 0) goto L_0x0162;
    L_0x015f:
        r31 = r35;
        goto L_0x010a;
    L_0x0162:
        r23 = org.mozilla.javascript.ScriptRuntime.NaN;
        goto L_0x0008;
    L_0x0166:
        r3 = 47;
        r0 = r22;
        if (r0 != r3) goto L_0x017a;
    L_0x016c:
        if (r32 >= 0) goto L_0x0171;
    L_0x016e:
        r32 = r35 + -1;
        goto L_0x010a;
    L_0x0171:
        if (r30 >= 0) goto L_0x0176;
    L_0x0173:
        r30 = r35;
        goto L_0x010a;
    L_0x0176:
        r23 = org.mozilla.javascript.ScriptRuntime.NaN;
        goto L_0x0008;
    L_0x017a:
        r0 = r27;
        r1 = r29;
        if (r0 >= r1) goto L_0x0196;
    L_0x0180:
        r3 = 44;
        r0 = r22;
        if (r0 == r3) goto L_0x0196;
    L_0x0186:
        r3 = 32;
        r0 = r22;
        if (r0 <= r3) goto L_0x0196;
    L_0x018c:
        r3 = 45;
        r0 = r22;
        if (r0 == r3) goto L_0x0196;
    L_0x0192:
        r23 = org.mozilla.javascript.ScriptRuntime.NaN;
        goto L_0x0008;
    L_0x0196:
        if (r38 == 0) goto L_0x01b2;
    L_0x0198:
        r3 = 60;
        r0 = r35;
        if (r0 >= r3) goto L_0x01b2;
    L_0x019e:
        r8 = 0;
        r3 = (r40 > r8 ? 1 : (r40 == r8 ? 0 : -1));
        if (r3 >= 0) goto L_0x01ab;
    L_0x01a4:
        r0 = r35;
        r8 = (double) r0;
        r40 = r40 - r8;
        goto L_0x010a;
    L_0x01ab:
        r0 = r35;
        r8 = (double) r0;
        r40 = r40 + r8;
        goto L_0x010a;
    L_0x01b2:
        if (r26 < 0) goto L_0x01ba;
    L_0x01b4:
        if (r31 >= 0) goto L_0x01ba;
    L_0x01b6:
        r31 = r35;
        goto L_0x010a;
    L_0x01ba:
        if (r31 < 0) goto L_0x01c2;
    L_0x01bc:
        if (r37 >= 0) goto L_0x01c2;
    L_0x01be:
        r37 = r35;
        goto L_0x010a;
    L_0x01c2:
        if (r30 >= 0) goto L_0x01c8;
    L_0x01c4:
        r30 = r35;
        goto L_0x010a;
    L_0x01c8:
        r23 = org.mozilla.javascript.ScriptRuntime.NaN;
        goto L_0x0008;
    L_0x01cc:
        r3 = 47;
        r0 = r22;
        if (r0 == r3) goto L_0x01e4;
    L_0x01d2:
        r3 = 58;
        r0 = r22;
        if (r0 == r3) goto L_0x01e4;
    L_0x01d8:
        r3 = 43;
        r0 = r22;
        if (r0 == r3) goto L_0x01e4;
    L_0x01de:
        r3 = 45;
        r0 = r22;
        if (r0 != r3) goto L_0x01e8;
    L_0x01e4:
        r36 = r22;
        goto L_0x0029;
    L_0x01e8:
        r6 = r27 + -1;
    L_0x01ea:
        r0 = r27;
        r1 = r29;
        if (r0 >= r1) goto L_0x0210;
    L_0x01f0:
        r0 = r44;
        r1 = r27;
        r22 = r0.charAt(r1);
        r3 = 65;
        r0 = r22;
        if (r3 > r0) goto L_0x0204;
    L_0x01fe:
        r3 = 90;
        r0 = r22;
        if (r0 <= r3) goto L_0x0219;
    L_0x0204:
        r3 = 97;
        r0 = r22;
        if (r3 > r0) goto L_0x0210;
    L_0x020a:
        r3 = 122; // 0x7a float:1.71E-43 double:6.03E-322;
        r0 = r22;
        if (r0 <= r3) goto L_0x0219;
    L_0x0210:
        r7 = r27 - r6;
        r3 = 2;
        if (r7 >= r3) goto L_0x021c;
    L_0x0215:
        r23 = org.mozilla.javascript.ScriptRuntime.NaN;
        goto L_0x0008;
    L_0x0219:
        r27 = r27 + 1;
        goto L_0x01ea;
    L_0x021c:
        r2 = "am;pm;monday;tuesday;wednesday;thursday;friday;saturday;sunday;january;february;march;april;may;june;july;august;september;october;november;december;gmt;ut;utc;est;edt;cst;cdt;mst;mdt;pst;pdt;";
        r28 = 0;
        r4 = 0;
    L_0x0221:
        r3 = 59;
        r42 = r2.indexOf(r3, r4);
        if (r42 >= 0) goto L_0x022d;
    L_0x0229:
        r23 = org.mozilla.javascript.ScriptRuntime.NaN;
        goto L_0x0008;
    L_0x022d:
        r3 = 1;
        r5 = r44;
        r3 = r2.regionMatches(r3, r4, r5, r6, r7);
        if (r3 == 0) goto L_0x0247;
    L_0x0236:
        r3 = 2;
        r0 = r28;
        if (r0 >= r3) goto L_0x0262;
    L_0x023b:
        r3 = 12;
        r0 = r26;
        if (r0 > r3) goto L_0x0243;
    L_0x0241:
        if (r26 >= 0) goto L_0x024c;
    L_0x0243:
        r23 = org.mozilla.javascript.ScriptRuntime.NaN;
        goto L_0x0008;
    L_0x0247:
        r4 = r42 + 1;
        r28 = r28 + 1;
        goto L_0x0221;
    L_0x024c:
        if (r28 != 0) goto L_0x0258;
    L_0x024e:
        r3 = 12;
        r0 = r26;
        if (r0 != r3) goto L_0x0029;
    L_0x0254:
        r26 = 0;
        goto L_0x0029;
    L_0x0258:
        r3 = 12;
        r0 = r26;
        if (r0 == r3) goto L_0x0029;
    L_0x025e:
        r26 = r26 + 12;
        goto L_0x0029;
    L_0x0262:
        r28 = r28 + -2;
        r3 = 7;
        r0 = r28;
        if (r0 < r3) goto L_0x0029;
    L_0x0269:
        r28 = r28 + -7;
        r3 = 12;
        r0 = r28;
        if (r0 >= r3) goto L_0x027b;
    L_0x0271:
        if (r32 >= 0) goto L_0x0277;
    L_0x0273:
        r32 = r28;
        goto L_0x0029;
    L_0x0277:
        r23 = org.mozilla.javascript.ScriptRuntime.NaN;
        goto L_0x0008;
    L_0x027b:
        r28 = r28 + -12;
        switch(r28) {
            case 0: goto L_0x0285;
            case 1: goto L_0x0289;
            case 2: goto L_0x028d;
            case 3: goto L_0x0291;
            case 4: goto L_0x0298;
            case 5: goto L_0x029c;
            case 6: goto L_0x02a3;
            case 7: goto L_0x02aa;
            case 8: goto L_0x02b1;
            case 9: goto L_0x02b8;
            case 10: goto L_0x02bc;
            default: goto L_0x0280;
        };
    L_0x0280:
        org.mozilla.javascript.Kit.codeBug();
        goto L_0x0029;
    L_0x0285:
        r40 = 0;
        goto L_0x0029;
    L_0x0289:
        r40 = 0;
        goto L_0x0029;
    L_0x028d:
        r40 = 0;
        goto L_0x0029;
    L_0x0291:
        r40 = 4643985272004935680; // 0x4072c00000000000 float:0.0 double:300.0;
        goto L_0x0029;
    L_0x0298:
        r40 = 4642648265865560064; // 0x406e000000000000 float:0.0 double:240.0;
        goto L_0x0029;
    L_0x029c:
        r40 = 4645040803167600640; // 0x4076800000000000 float:0.0 double:360.0;
        goto L_0x0029;
    L_0x02a3:
        r40 = 4643985272004935680; // 0x4072c00000000000 float:0.0 double:300.0;
        goto L_0x0029;
    L_0x02aa:
        r40 = 4646096334330265600; // 0x407a400000000000 float:0.0 double:420.0;
        goto L_0x0029;
    L_0x02b1:
        r40 = 4645040803167600640; // 0x4076800000000000 float:0.0 double:360.0;
        goto L_0x0029;
    L_0x02b8:
        r40 = 4647151865492930560; // 0x407e000000000000 float:0.0 double:480.0;
        goto L_0x0029;
    L_0x02bc:
        r40 = 4646096334330265600; // 0x407a400000000000 float:0.0 double:420.0;
        goto L_0x0029;
    L_0x02c3:
        if (r43 < 0) goto L_0x02c9;
    L_0x02c5:
        if (r32 < 0) goto L_0x02c9;
    L_0x02c7:
        if (r30 >= 0) goto L_0x02cd;
    L_0x02c9:
        r23 = org.mozilla.javascript.ScriptRuntime.NaN;
        goto L_0x0008;
    L_0x02cd:
        if (r37 >= 0) goto L_0x02d1;
    L_0x02cf:
        r37 = 0;
    L_0x02d1:
        if (r31 >= 0) goto L_0x02d5;
    L_0x02d3:
        r31 = 0;
    L_0x02d5:
        if (r26 >= 0) goto L_0x02d9;
    L_0x02d7:
        r26 = 0;
    L_0x02d9:
        r0 = r43;
        r8 = (double) r0;
        r0 = r32;
        r10 = (double) r0;
        r0 = r30;
        r12 = (double) r0;
        r0 = r26;
        r14 = (double) r0;
        r0 = r31;
        r0 = (double) r0;
        r16 = r0;
        r0 = r37;
        r0 = (double) r0;
        r18 = r0;
        r20 = 0;
        r33 = date_msecFromDate(r8, r10, r12, r14, r16, r18, r20);
        r8 = -4616189618054758400; // 0xbff0000000000000 float:0.0 double:-1.0;
        r3 = (r40 > r8 ? 1 : (r40 == r8 ? 0 : -1));
        if (r3 != 0) goto L_0x0301;
    L_0x02fb:
        r23 = internalUTC(r33);
        goto L_0x0008;
    L_0x0301:
        r8 = 4678479150791524352; // 0x40ed4c0000000000 float:0.0 double:60000.0;
        r8 = r8 * r40;
        r23 = r33 + r8;
        goto L_0x0008;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.NativeDate.date_parseString(java.lang.String):double");
    }

    private static String date_format(double t, int methodId) {
        StringBuilder stringBuilder = new StringBuilder(60);
        double local = LocalTime(t);
        if (methodId != 3) {
            appendWeekDayName(stringBuilder, WeekDay(local));
            stringBuilder.append(' ');
            appendMonthName(stringBuilder, MonthFromTime(local));
            stringBuilder.append(' ');
            append0PaddedUint(stringBuilder, DateFromTime(local), 2);
            stringBuilder.append(' ');
            int year = YearFromTime(local);
            if (year < 0) {
                stringBuilder.append(SignatureVisitor.SUPER);
                year = -year;
            }
            append0PaddedUint(stringBuilder, year, 4);
            if (methodId != 4) {
                stringBuilder.append(' ');
            }
        }
        if (methodId != 4) {
            append0PaddedUint(stringBuilder, HourFromTime(local), 2);
            stringBuilder.append(':');
            append0PaddedUint(stringBuilder, MinFromTime(local), 2);
            stringBuilder.append(':');
            append0PaddedUint(stringBuilder, SecFromTime(local), 2);
            int minutes = (int) Math.floor((LocalTZA + DaylightSavingTA(t)) / msPerMinute);
            int offset = ((minutes / 60) * 100) + (minutes % 60);
            if (offset > 0) {
                stringBuilder.append(" GMT+");
            } else {
                stringBuilder.append(" GMT-");
                offset = -offset;
            }
            append0PaddedUint(stringBuilder, offset, 4);
            if (timeZoneFormatter == null) {
                timeZoneFormatter = new SimpleDateFormat("zzz");
            }
            if (t < 0.0d) {
                t = MakeDate(MakeDay((double) EquivalentYear(YearFromTime(local)), (double) MonthFromTime(t), (double) DateFromTime(t)), TimeWithinDay(t));
            }
            stringBuilder.append(" (");
            Date date = new Date((long) t);
            synchronized (timeZoneFormatter) {
                stringBuilder.append(timeZoneFormatter.format(date));
            }
            stringBuilder.append(')');
        }
        return stringBuilder.toString();
    }

    private static Object jsConstructor(Object[] args) {
        NativeDate obj = new NativeDate();
        if (args.length == 0) {
            obj.date = now();
        } else if (args.length == 1) {
            double date;
            Object arg0 = args[0];
            if (arg0 instanceof Scriptable) {
                arg0 = ((Scriptable) arg0).getDefaultValue(null);
            }
            if (arg0 instanceof CharSequence) {
                date = date_parseString(arg0.toString());
            } else {
                date = ScriptRuntime.toNumber(arg0);
            }
            obj.date = TimeClip(date);
        } else {
            double time = date_msecFromArgs(args);
            if (!(Double.isNaN(time) || Double.isInfinite(time))) {
                time = TimeClip(internalUTC(time));
            }
            obj.date = time;
        }
        return obj;
    }

    private static String toLocale_helper(double t, int methodId) {
        DateFormat formatter;
        String format;
        switch (methodId) {
            case 5:
                if (localeDateTimeFormatter == null) {
                    localeDateTimeFormatter = DateFormat.getDateTimeInstance(1, 1);
                }
                formatter = localeDateTimeFormatter;
                break;
            case 6:
                if (localeTimeFormatter == null) {
                    localeTimeFormatter = DateFormat.getTimeInstance(1);
                }
                formatter = localeTimeFormatter;
                break;
            case 7:
                if (localeDateFormatter == null) {
                    localeDateFormatter = DateFormat.getDateInstance(1);
                }
                formatter = localeDateFormatter;
                break;
            default:
                throw new AssertionError();
        }
        synchronized (formatter) {
            format = formatter.format(new Date((long) t));
        }
        return format;
    }

    private static String js_toUTCString(double date) {
        StringBuilder result = new StringBuilder(60);
        appendWeekDayName(result, WeekDay(date));
        result.append(", ");
        append0PaddedUint(result, DateFromTime(date), 2);
        result.append(' ');
        appendMonthName(result, MonthFromTime(date));
        result.append(' ');
        int year = YearFromTime(date);
        if (year < 0) {
            result.append(SignatureVisitor.SUPER);
            year = -year;
        }
        append0PaddedUint(result, year, 4);
        result.append(' ');
        append0PaddedUint(result, HourFromTime(date), 2);
        result.append(':');
        append0PaddedUint(result, MinFromTime(date), 2);
        result.append(':');
        append0PaddedUint(result, SecFromTime(date), 2);
        result.append(" GMT");
        return result.toString();
    }

    private static String js_toISOString(double t) {
        StringBuilder result = new StringBuilder(27);
        int year = YearFromTime(t);
        if (year < 0) {
            result.append(SignatureVisitor.SUPER);
            append0PaddedUint(result, -year, 6);
        } else if (year > 9999) {
            append0PaddedUint(result, year, 6);
        } else {
            append0PaddedUint(result, year, 4);
        }
        result.append(SignatureVisitor.SUPER);
        append0PaddedUint(result, MonthFromTime(t) + 1, 2);
        result.append(SignatureVisitor.SUPER);
        append0PaddedUint(result, DateFromTime(t), 2);
        result.append('T');
        append0PaddedUint(result, HourFromTime(t), 2);
        result.append(':');
        append0PaddedUint(result, MinFromTime(t), 2);
        result.append(':');
        append0PaddedUint(result, SecFromTime(t), 2);
        result.append('.');
        append0PaddedUint(result, msFromTime(t), 3);
        result.append('Z');
        return result.toString();
    }

    private static void append0PaddedUint(StringBuilder sb, int i, int minWidth) {
        if (i < 0) {
            Kit.codeBug();
        }
        int scale = 1;
        minWidth--;
        if (i >= 10) {
            if (i < 1000000000) {
                while (true) {
                    int newScale = scale * 10;
                    if (i < newScale) {
                        break;
                    }
                    minWidth--;
                    scale = newScale;
                }
            } else {
                minWidth -= 9;
                scale = 1000000000;
            }
        }
        while (minWidth > 0) {
            sb.append('0');
            minWidth--;
        }
        while (scale != 1) {
            sb.append((char) ((i / scale) + 48));
            i %= scale;
            scale /= 10;
        }
        sb.append((char) (i + 48));
    }

    private static void appendMonthName(StringBuilder sb, int index) {
        String months = "JanFebMarAprMayJunJulAugSepOctNovDec";
        index *= 3;
        for (int i = 0; i != 3; i++) {
            sb.append(months.charAt(index + i));
        }
    }

    private static void appendWeekDayName(StringBuilder sb, int index) {
        String days = "SunMonTueWedThuFriSat";
        index *= 3;
        for (int i = 0; i != 3; i++) {
            sb.append(days.charAt(index + i));
        }
    }

    /* JADX WARNING: Missing block: B:8:0x0016, code skipped:
            r20 = 1;
     */
    /* JADX WARNING: Missing block: B:9:0x0018, code skipped:
            r14 = false;
     */
    /* JADX WARNING: Missing block: B:10:0x0022, code skipped:
            if (r32.length >= r20) goto L_0x004a;
     */
    /* JADX WARNING: Missing block: B:11:0x0024, code skipped:
            r21 = r32.length;
     */
    /* JADX WARNING: Missing block: B:13:0x002b, code skipped:
            if ($assertionsDisabled != false) goto L_0x004d;
     */
    /* JADX WARNING: Missing block: B:15:0x0033, code skipped:
            if (r21 <= 4) goto L_0x004d;
     */
    /* JADX WARNING: Missing block: B:17:0x003a, code skipped:
            throw new java.lang.AssertionError();
     */
    /* JADX WARNING: Missing block: B:19:0x003d, code skipped:
            r20 = 2;
     */
    /* JADX WARNING: Missing block: B:21:0x0042, code skipped:
            r20 = 3;
     */
    /* JADX WARNING: Missing block: B:23:0x0047, code skipped:
            r20 = 4;
     */
    /* JADX WARNING: Missing block: B:24:0x004a, code skipped:
            r21 = r20;
     */
    /* JADX WARNING: Missing block: B:25:0x004d, code skipped:
            r22 = new double[4];
            r15 = 0;
     */
    /* JADX WARNING: Missing block: B:27:0x0058, code skipped:
            if (r15 >= r21) goto L_0x0075;
     */
    /* JADX WARNING: Missing block: B:28:0x005a, code skipped:
            r12 = org.mozilla.javascript.ScriptRuntime.toNumber(r32[r15]);
     */
    /* JADX WARNING: Missing block: B:29:0x0062, code skipped:
            if (r12 != r12) goto L_0x006a;
     */
    /* JADX WARNING: Missing block: B:31:0x0068, code skipped:
            if (java.lang.Double.isInfinite(r12) == false) goto L_0x006e;
     */
    /* JADX WARNING: Missing block: B:32:0x006a, code skipped:
            r14 = true;
     */
    /* JADX WARNING: Missing block: B:33:0x006b, code skipped:
            r15 = r15 + 1;
     */
    /* JADX WARNING: Missing block: B:34:0x006e, code skipped:
            r22[r15] = org.mozilla.javascript.ScriptRuntime.toInteger(r12);
     */
    /* JADX WARNING: Missing block: B:35:0x0075, code skipped:
            if (r14 != false) goto L_0x007b;
     */
    /* JADX WARNING: Missing block: B:37:0x0079, code skipped:
            if (r30 == r30) goto L_0x007e;
     */
    /* JADX WARNING: Missing block: B:39:0x007e, code skipped:
            r25 = r21;
     */
    /* JADX WARNING: Missing block: B:40:0x0081, code skipped:
            if (r17 == false) goto L_0x00ed;
     */
    /* JADX WARNING: Missing block: B:41:0x0083, code skipped:
            r18 = LocalTime(r30);
     */
    /* JADX WARNING: Missing block: B:43:0x008d, code skipped:
            if (r20 < 4) goto L_0x00f0;
     */
    /* JADX WARNING: Missing block: B:45:0x0091, code skipped:
            if (0 >= r25) goto L_0x00f0;
     */
    /* JADX WARNING: Missing block: B:46:0x0093, code skipped:
            r16 = 0 + 1;
            r4 = r22[0];
     */
    /* JADX WARNING: Missing block: B:48:0x009d, code skipped:
            if (r20 < 3) goto L_0x00fa;
     */
    /* JADX WARNING: Missing block: B:50:0x00a3, code skipped:
            if (r16 >= r25) goto L_0x00fa;
     */
    /* JADX WARNING: Missing block: B:51:0x00a5, code skipped:
            r15 = r16 + 1;
            r6 = r22[r16];
            r16 = r15;
     */
    /* JADX WARNING: Missing block: B:53:0x00b1, code skipped:
            if (r20 < 2) goto L_0x0102;
     */
    /* JADX WARNING: Missing block: B:55:0x00b7, code skipped:
            if (r16 >= r25) goto L_0x0102;
     */
    /* JADX WARNING: Missing block: B:56:0x00b9, code skipped:
            r15 = r16 + 1;
            r8 = r22[r16];
            r16 = r15;
     */
    /* JADX WARNING: Missing block: B:58:0x00c5, code skipped:
            if (r20 < 1) goto L_0x010a;
     */
    /* JADX WARNING: Missing block: B:60:0x00cb, code skipped:
            if (r16 >= r25) goto L_0x010a;
     */
    /* JADX WARNING: Missing block: B:61:0x00cd, code skipped:
            r15 = r16 + 1;
            r10 = r22[r16];
     */
    /* JADX WARNING: Missing block: B:62:0x00d1, code skipped:
            r23 = MakeDate(Day(r18), MakeTime(r4, r6, r8, r10));
     */
    /* JADX WARNING: Missing block: B:63:0x00e1, code skipped:
            if (r17 == false) goto L_0x00e7;
     */
    /* JADX WARNING: Missing block: B:64:0x00e3, code skipped:
            r23 = internalUTC(r23);
     */
    /* JADX WARNING: Missing block: B:66:0x00ed, code skipped:
            r18 = r30;
     */
    /* JADX WARNING: Missing block: B:67:0x00f0, code skipped:
            r4 = (double) HourFromTime(r18);
            r16 = 0;
     */
    /* JADX WARNING: Missing block: B:68:0x00fa, code skipped:
            r6 = (double) MinFromTime(r18);
     */
    /* JADX WARNING: Missing block: B:69:0x0102, code skipped:
            r8 = (double) SecFromTime(r18);
     */
    /* JADX WARNING: Missing block: B:70:0x010a, code skipped:
            r10 = (double) msFromTime(r18);
            r15 = r16;
     */
    /* JADX WARNING: Missing block: B:75:?, code skipped:
            return org.mozilla.javascript.ScriptRuntime.NaN;
     */
    /* JADX WARNING: Missing block: B:76:?, code skipped:
            return TimeClip(r23);
     */
    private static double makeTime(double r30, java.lang.Object[] r32, int r33) {
        /*
        r0 = r32;
        r0 = r0.length;
        r28 = r0;
        if (r28 != 0) goto L_0x000a;
    L_0x0007:
        r28 = org.mozilla.javascript.ScriptRuntime.NaN;
    L_0x0009:
        return r28;
    L_0x000a:
        r17 = 1;
        switch(r33) {
            case 31: goto L_0x0016;
            case 32: goto L_0x0014;
            case 33: goto L_0x003d;
            case 34: goto L_0x003b;
            case 35: goto L_0x0042;
            case 36: goto L_0x0040;
            case 37: goto L_0x0047;
            case 38: goto L_0x0045;
            default: goto L_0x000f;
        };
    L_0x000f:
        r28 = org.mozilla.javascript.Kit.codeBug();
        throw r28;
    L_0x0014:
        r17 = 0;
    L_0x0016:
        r20 = 1;
    L_0x0018:
        r14 = 0;
        r0 = r32;
        r0 = r0.length;
        r28 = r0;
        r0 = r28;
        r1 = r20;
        if (r0 >= r1) goto L_0x004a;
    L_0x0024:
        r0 = r32;
        r0 = r0.length;
        r21 = r0;
    L_0x0029:
        r28 = $assertionsDisabled;
        if (r28 != 0) goto L_0x004d;
    L_0x002d:
        r28 = 4;
        r0 = r21;
        r1 = r28;
        if (r0 <= r1) goto L_0x004d;
    L_0x0035:
        r28 = new java.lang.AssertionError;
        r28.<init>();
        throw r28;
    L_0x003b:
        r17 = 0;
    L_0x003d:
        r20 = 2;
        goto L_0x0018;
    L_0x0040:
        r17 = 0;
    L_0x0042:
        r20 = 3;
        goto L_0x0018;
    L_0x0045:
        r17 = 0;
    L_0x0047:
        r20 = 4;
        goto L_0x0018;
    L_0x004a:
        r21 = r20;
        goto L_0x0029;
    L_0x004d:
        r28 = 4;
        r0 = r28;
        r0 = new double[r0];
        r22 = r0;
        r15 = 0;
    L_0x0056:
        r0 = r21;
        if (r15 >= r0) goto L_0x0075;
    L_0x005a:
        r28 = r32[r15];
        r12 = org.mozilla.javascript.ScriptRuntime.toNumber(r28);
        r28 = (r12 > r12 ? 1 : (r12 == r12 ? 0 : -1));
        if (r28 != 0) goto L_0x006a;
    L_0x0064:
        r28 = java.lang.Double.isInfinite(r12);
        if (r28 == 0) goto L_0x006e;
    L_0x006a:
        r14 = 1;
    L_0x006b:
        r15 = r15 + 1;
        goto L_0x0056;
    L_0x006e:
        r28 = org.mozilla.javascript.ScriptRuntime.toInteger(r12);
        r22[r15] = r28;
        goto L_0x006b;
    L_0x0075:
        if (r14 != 0) goto L_0x007b;
    L_0x0077:
        r28 = (r30 > r30 ? 1 : (r30 == r30 ? 0 : -1));
        if (r28 == 0) goto L_0x007e;
    L_0x007b:
        r28 = org.mozilla.javascript.ScriptRuntime.NaN;
        goto L_0x0009;
    L_0x007e:
        r15 = 0;
        r25 = r21;
        if (r17 == 0) goto L_0x00ed;
    L_0x0083:
        r18 = LocalTime(r30);
    L_0x0087:
        r28 = 4;
        r0 = r20;
        r1 = r28;
        if (r0 < r1) goto L_0x00f0;
    L_0x008f:
        r0 = r25;
        if (r15 >= r0) goto L_0x00f0;
    L_0x0093:
        r16 = r15 + 1;
        r4 = r22[r15];
    L_0x0097:
        r28 = 3;
        r0 = r20;
        r1 = r28;
        if (r0 < r1) goto L_0x00fa;
    L_0x009f:
        r0 = r16;
        r1 = r25;
        if (r0 >= r1) goto L_0x00fa;
    L_0x00a5:
        r15 = r16 + 1;
        r6 = r22[r16];
        r16 = r15;
    L_0x00ab:
        r28 = 2;
        r0 = r20;
        r1 = r28;
        if (r0 < r1) goto L_0x0102;
    L_0x00b3:
        r0 = r16;
        r1 = r25;
        if (r0 >= r1) goto L_0x0102;
    L_0x00b9:
        r15 = r16 + 1;
        r8 = r22[r16];
        r16 = r15;
    L_0x00bf:
        r28 = 1;
        r0 = r20;
        r1 = r28;
        if (r0 < r1) goto L_0x010a;
    L_0x00c7:
        r0 = r16;
        r1 = r25;
        if (r0 >= r1) goto L_0x010a;
    L_0x00cd:
        r15 = r16 + 1;
        r10 = r22[r16];
    L_0x00d1:
        r26 = MakeTime(r4, r6, r8, r10);
        r28 = Day(r18);
        r0 = r28;
        r2 = r26;
        r23 = MakeDate(r0, r2);
        if (r17 == 0) goto L_0x00e7;
    L_0x00e3:
        r23 = internalUTC(r23);
    L_0x00e7:
        r28 = TimeClip(r23);
        goto L_0x0009;
    L_0x00ed:
        r18 = r30;
        goto L_0x0087;
    L_0x00f0:
        r28 = HourFromTime(r18);
        r0 = r28;
        r4 = (double) r0;
        r16 = r15;
        goto L_0x0097;
    L_0x00fa:
        r28 = MinFromTime(r18);
        r0 = r28;
        r6 = (double) r0;
        goto L_0x00ab;
    L_0x0102:
        r28 = SecFromTime(r18);
        r0 = r28;
        r8 = (double) r0;
        goto L_0x00bf;
    L_0x010a:
        r28 = msFromTime(r18);
        r0 = r28;
        r10 = (double) r0;
        r15 = r16;
        goto L_0x00d1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.NativeDate.makeTime(double, java.lang.Object[], int):double");
    }

    /* JADX WARNING: Missing block: B:8:0x0014, code skipped:
            r16 = 1;
     */
    /* JADX WARNING: Missing block: B:9:0x0016, code skipped:
            r10 = false;
     */
    /* JADX WARNING: Missing block: B:10:0x0020, code skipped:
            if (r26.length >= r16) goto L_0x0049;
     */
    /* JADX WARNING: Missing block: B:11:0x0022, code skipped:
            r17 = r26.length;
     */
    /* JADX WARNING: Missing block: B:13:0x0029, code skipped:
            if ($assertionsDisabled != false) goto L_0x004c;
     */
    /* JADX WARNING: Missing block: B:15:0x0031, code skipped:
            if (1 > r17) goto L_0x003b;
     */
    /* JADX WARNING: Missing block: B:17:0x0039, code skipped:
            if (r17 <= 3) goto L_0x004c;
     */
    /* JADX WARNING: Missing block: B:19:0x0040, code skipped:
            throw new java.lang.AssertionError();
     */
    /* JADX WARNING: Missing block: B:21:0x0042, code skipped:
            r16 = 2;
     */
    /* JADX WARNING: Missing block: B:23:0x0046, code skipped:
            r16 = 3;
     */
    /* JADX WARNING: Missing block: B:24:0x0049, code skipped:
            r17 = r16;
     */
    /* JADX WARNING: Missing block: B:25:0x004c, code skipped:
            r18 = new double[3];
            r11 = 0;
     */
    /* JADX WARNING: Missing block: B:27:0x0057, code skipped:
            if (r11 >= r17) goto L_0x0074;
     */
    /* JADX WARNING: Missing block: B:28:0x0059, code skipped:
            r8 = org.mozilla.javascript.ScriptRuntime.toNumber(r26[r11]);
     */
    /* JADX WARNING: Missing block: B:29:0x0061, code skipped:
            if (r8 != r8) goto L_0x0069;
     */
    /* JADX WARNING: Missing block: B:31:0x0067, code skipped:
            if (java.lang.Double.isInfinite(r8) == false) goto L_0x006d;
     */
    /* JADX WARNING: Missing block: B:32:0x0069, code skipped:
            r10 = true;
     */
    /* JADX WARNING: Missing block: B:33:0x006a, code skipped:
            r11 = r11 + 1;
     */
    /* JADX WARNING: Missing block: B:34:0x006d, code skipped:
            r18[r11] = org.mozilla.javascript.ScriptRuntime.toInteger(r8);
     */
    /* JADX WARNING: Missing block: B:35:0x0074, code skipped:
            if (r10 == false) goto L_0x0079;
     */
    /* JADX WARNING: Missing block: B:37:0x0079, code skipped:
            r21 = r17;
     */
    /* JADX WARNING: Missing block: B:38:0x007e, code skipped:
            if (r24 == r24) goto L_0x00d9;
     */
    /* JADX WARNING: Missing block: B:40:0x0086, code skipped:
            if (r16 >= 3) goto L_0x008c;
     */
    /* JADX WARNING: Missing block: B:42:0x008c, code skipped:
            r14 = 0.0d;
     */
    /* JADX WARNING: Missing block: B:44:0x0094, code skipped:
            if (r16 < 3) goto L_0x00e3;
     */
    /* JADX WARNING: Missing block: B:46:0x0098, code skipped:
            if (0 >= r21) goto L_0x00e3;
     */
    /* JADX WARNING: Missing block: B:47:0x009a, code skipped:
            r12 = 0 + 1;
            r2 = r18[0];
     */
    /* JADX WARNING: Missing block: B:49:0x00a4, code skipped:
            if (r16 < 2) goto L_0x00ec;
     */
    /* JADX WARNING: Missing block: B:51:0x00a8, code skipped:
            if (r12 >= r21) goto L_0x00ec;
     */
    /* JADX WARNING: Missing block: B:52:0x00aa, code skipped:
            r11 = r12 + 1;
            r4 = r18[r12];
            r12 = r11;
     */
    /* JADX WARNING: Missing block: B:54:0x00b5, code skipped:
            if (r16 < 1) goto L_0x00f4;
     */
    /* JADX WARNING: Missing block: B:56:0x00b9, code skipped:
            if (r12 >= r21) goto L_0x00f4;
     */
    /* JADX WARNING: Missing block: B:57:0x00bb, code skipped:
            r11 = r12 + 1;
            r6 = r18[r12];
     */
    /* JADX WARNING: Missing block: B:58:0x00bf, code skipped:
            r19 = MakeDate(MakeDay(r2, r4, r6), TimeWithinDay(r14));
     */
    /* JADX WARNING: Missing block: B:59:0x00cd, code skipped:
            if (r13 == false) goto L_0x00d3;
     */
    /* JADX WARNING: Missing block: B:60:0x00cf, code skipped:
            r19 = internalUTC(r19);
     */
    /* JADX WARNING: Missing block: B:62:0x00d9, code skipped:
            if (r13 == false) goto L_0x00e0;
     */
    /* JADX WARNING: Missing block: B:63:0x00db, code skipped:
            r14 = LocalTime(r24);
     */
    /* JADX WARNING: Missing block: B:64:0x00e0, code skipped:
            r14 = r24;
     */
    /* JADX WARNING: Missing block: B:65:0x00e3, code skipped:
            r2 = (double) YearFromTime(r14);
            r12 = 0;
     */
    /* JADX WARNING: Missing block: B:66:0x00ec, code skipped:
            r4 = (double) MonthFromTime(r14);
     */
    /* JADX WARNING: Missing block: B:67:0x00f4, code skipped:
            r6 = (double) DateFromTime(r14);
            r11 = r12;
     */
    /* JADX WARNING: Missing block: B:72:?, code skipped:
            return org.mozilla.javascript.ScriptRuntime.NaN;
     */
    /* JADX WARNING: Missing block: B:73:?, code skipped:
            return org.mozilla.javascript.ScriptRuntime.NaN;
     */
    /* JADX WARNING: Missing block: B:74:?, code skipped:
            return TimeClip(r19);
     */
    private static double makeDate(double r24, java.lang.Object[] r26, int r27) {
        /*
        r0 = r26;
        r0 = r0.length;
        r22 = r0;
        if (r22 != 0) goto L_0x000a;
    L_0x0007:
        r22 = org.mozilla.javascript.ScriptRuntime.NaN;
    L_0x0009:
        return r22;
    L_0x000a:
        r13 = 1;
        switch(r27) {
            case 39: goto L_0x0014;
            case 40: goto L_0x0013;
            case 41: goto L_0x0042;
            case 42: goto L_0x0041;
            case 43: goto L_0x0046;
            case 44: goto L_0x0045;
            default: goto L_0x000e;
        };
    L_0x000e:
        r22 = org.mozilla.javascript.Kit.codeBug();
        throw r22;
    L_0x0013:
        r13 = 0;
    L_0x0014:
        r16 = 1;
    L_0x0016:
        r10 = 0;
        r0 = r26;
        r0 = r0.length;
        r22 = r0;
        r0 = r22;
        r1 = r16;
        if (r0 >= r1) goto L_0x0049;
    L_0x0022:
        r0 = r26;
        r0 = r0.length;
        r17 = r0;
    L_0x0027:
        r22 = $assertionsDisabled;
        if (r22 != 0) goto L_0x004c;
    L_0x002b:
        r22 = 1;
        r0 = r22;
        r1 = r17;
        if (r0 > r1) goto L_0x003b;
    L_0x0033:
        r22 = 3;
        r0 = r17;
        r1 = r22;
        if (r0 <= r1) goto L_0x004c;
    L_0x003b:
        r22 = new java.lang.AssertionError;
        r22.<init>();
        throw r22;
    L_0x0041:
        r13 = 0;
    L_0x0042:
        r16 = 2;
        goto L_0x0016;
    L_0x0045:
        r13 = 0;
    L_0x0046:
        r16 = 3;
        goto L_0x0016;
    L_0x0049:
        r17 = r16;
        goto L_0x0027;
    L_0x004c:
        r22 = 3;
        r0 = r22;
        r0 = new double[r0];
        r18 = r0;
        r11 = 0;
    L_0x0055:
        r0 = r17;
        if (r11 >= r0) goto L_0x0074;
    L_0x0059:
        r22 = r26[r11];
        r8 = org.mozilla.javascript.ScriptRuntime.toNumber(r22);
        r22 = (r8 > r8 ? 1 : (r8 == r8 ? 0 : -1));
        if (r22 != 0) goto L_0x0069;
    L_0x0063:
        r22 = java.lang.Double.isInfinite(r8);
        if (r22 == 0) goto L_0x006d;
    L_0x0069:
        r10 = 1;
    L_0x006a:
        r11 = r11 + 1;
        goto L_0x0055;
    L_0x006d:
        r22 = org.mozilla.javascript.ScriptRuntime.toInteger(r8);
        r18[r11] = r22;
        goto L_0x006a;
    L_0x0074:
        if (r10 == 0) goto L_0x0079;
    L_0x0076:
        r22 = org.mozilla.javascript.ScriptRuntime.NaN;
        goto L_0x0009;
    L_0x0079:
        r11 = 0;
        r21 = r17;
        r22 = (r24 > r24 ? 1 : (r24 == r24 ? 0 : -1));
        if (r22 == 0) goto L_0x00d9;
    L_0x0080:
        r22 = 3;
        r0 = r16;
        r1 = r22;
        if (r0 >= r1) goto L_0x008c;
    L_0x0088:
        r22 = org.mozilla.javascript.ScriptRuntime.NaN;
        goto L_0x0009;
    L_0x008c:
        r14 = 0;
    L_0x008e:
        r22 = 3;
        r0 = r16;
        r1 = r22;
        if (r0 < r1) goto L_0x00e3;
    L_0x0096:
        r0 = r21;
        if (r11 >= r0) goto L_0x00e3;
    L_0x009a:
        r12 = r11 + 1;
        r2 = r18[r11];
    L_0x009e:
        r22 = 2;
        r0 = r16;
        r1 = r22;
        if (r0 < r1) goto L_0x00ec;
    L_0x00a6:
        r0 = r21;
        if (r12 >= r0) goto L_0x00ec;
    L_0x00aa:
        r11 = r12 + 1;
        r4 = r18[r12];
        r12 = r11;
    L_0x00af:
        r22 = 1;
        r0 = r16;
        r1 = r22;
        if (r0 < r1) goto L_0x00f4;
    L_0x00b7:
        r0 = r21;
        if (r12 >= r0) goto L_0x00f4;
    L_0x00bb:
        r11 = r12 + 1;
        r6 = r18[r12];
    L_0x00bf:
        r6 = MakeDay(r2, r4, r6);
        r22 = TimeWithinDay(r14);
        r0 = r22;
        r19 = MakeDate(r6, r0);
        if (r13 == 0) goto L_0x00d3;
    L_0x00cf:
        r19 = internalUTC(r19);
    L_0x00d3:
        r22 = TimeClip(r19);
        goto L_0x0009;
    L_0x00d9:
        if (r13 == 0) goto L_0x00e0;
    L_0x00db:
        r14 = LocalTime(r24);
        goto L_0x008e;
    L_0x00e0:
        r14 = r24;
        goto L_0x008e;
    L_0x00e3:
        r22 = YearFromTime(r14);
        r0 = r22;
        r2 = (double) r0;
        r12 = r11;
        goto L_0x009e;
    L_0x00ec:
        r22 = MonthFromTime(r14);
        r0 = r22;
        r4 = (double) r0;
        goto L_0x00af;
    L_0x00f4:
        r22 = DateFromTime(r14);
        r0 = r22;
        r6 = (double) r0;
        r11 = r12;
        goto L_0x00bf;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.NativeDate.makeDate(double, java.lang.Object[], int):double");
    }

    /* access modifiers changed from: protected */
    public int findPrototypeId(String s) {
        int id = 0;
        String X = null;
        int c;
        switch (s.length()) {
            case 6:
                c = s.charAt(0);
                if (c != 103) {
                    if (c == 116) {
                        X = "toJSON";
                        id = 47;
                        break;
                    }
                }
                X = "getDay";
                id = 19;
                break;
                break;
            case 7:
                switch (s.charAt(3)) {
                    case 'D':
                        c = s.charAt(0);
                        if (c != 103) {
                            if (c == 115) {
                                X = "setDate";
                                id = 39;
                                break;
                            }
                        }
                        X = "getDate";
                        id = 17;
                        break;
                        break;
                    case 'T':
                        c = s.charAt(0);
                        if (c != 103) {
                            if (c == 115) {
                                X = "setTime";
                                id = 30;
                                break;
                            }
                        }
                        X = "getTime";
                        id = 11;
                        break;
                        break;
                    case 'Y':
                        c = s.charAt(0);
                        if (c != 103) {
                            if (c == 115) {
                                X = "setYear";
                                id = 45;
                                break;
                            }
                        }
                        X = "getYear";
                        id = 12;
                        break;
                        break;
                    case 'u':
                        X = "valueOf";
                        id = 10;
                        break;
                }
                break;
            case 8:
                switch (s.charAt(3)) {
                    case 'H':
                        c = s.charAt(0);
                        if (c != 103) {
                            if (c == 115) {
                                X = "setHours";
                                id = 37;
                                break;
                            }
                        }
                        X = "getHours";
                        id = 21;
                        break;
                        break;
                    case 'M':
                        c = s.charAt(0);
                        if (c != 103) {
                            if (c == 115) {
                                X = "setMonth";
                                id = 41;
                                break;
                            }
                        }
                        X = "getMonth";
                        id = 15;
                        break;
                        break;
                    case 'o':
                        X = "toSource";
                        id = 9;
                        break;
                    case 't':
                        X = "toString";
                        id = 2;
                        break;
                }
                break;
            case 9:
                X = "getUTCDay";
                id = 20;
                break;
            case 10:
                c = s.charAt(3);
                if (c != 77) {
                    if (c != 83) {
                        if (c == 85) {
                            c = s.charAt(0);
                            if (c != 103) {
                                if (c == 115) {
                                    X = "setUTCDate";
                                    id = 40;
                                    break;
                                }
                            }
                            X = "getUTCDate";
                            id = 18;
                            break;
                        }
                    }
                    c = s.charAt(0);
                    if (c != 103) {
                        if (c == 115) {
                            X = "setSeconds";
                            id = 33;
                            break;
                        }
                    }
                    X = "getSeconds";
                    id = 25;
                    break;
                }
                c = s.charAt(0);
                if (c != 103) {
                    if (c == 115) {
                        X = "setMinutes";
                        id = 35;
                        break;
                    }
                }
                X = "getMinutes";
                id = 23;
                break;
                break;
            case 11:
                switch (s.charAt(3)) {
                    case 'F':
                        c = s.charAt(0);
                        if (c != 103) {
                            if (c == 115) {
                                X = "setFullYear";
                                id = 43;
                                break;
                            }
                        }
                        X = "getFullYear";
                        id = 13;
                        break;
                        break;
                    case 'M':
                        X = "toGMTString";
                        id = 8;
                        break;
                    case 'S':
                        X = "toISOString";
                        id = 46;
                        break;
                    case 'T':
                        X = "toUTCString";
                        id = 8;
                        break;
                    case 'U':
                        c = s.charAt(0);
                        if (c != 103) {
                            if (c == 115) {
                                c = s.charAt(9);
                                if (c != 114) {
                                    if (c == 116) {
                                        X = "setUTCMonth";
                                        id = 42;
                                        break;
                                    }
                                }
                                X = "setUTCHours";
                                id = 38;
                                break;
                            }
                        }
                        c = s.charAt(9);
                        if (c != 114) {
                            if (c == 116) {
                                X = "getUTCMonth";
                                id = 16;
                                break;
                            }
                        }
                        X = "getUTCHours";
                        id = 22;
                        break;
                        break;
                    case 's':
                        X = "constructor";
                        id = 1;
                        break;
                }
                break;
            case 12:
                c = s.charAt(2);
                if (c != 68) {
                    if (c == 84) {
                        X = "toTimeString";
                        id = 3;
                        break;
                    }
                }
                X = "toDateString";
                id = 4;
                break;
                break;
            case 13:
                c = s.charAt(0);
                if (c != 103) {
                    if (c == 115) {
                        c = s.charAt(6);
                        if (c != 77) {
                            if (c == 83) {
                                X = "setUTCSeconds";
                                id = 34;
                                break;
                            }
                        }
                        X = "setUTCMinutes";
                        id = 36;
                        break;
                    }
                }
                c = s.charAt(6);
                if (c != 77) {
                    if (c == 83) {
                        X = "getUTCSeconds";
                        id = 26;
                        break;
                    }
                }
                X = "getUTCMinutes";
                id = 24;
                break;
                break;
            case 14:
                c = s.charAt(0);
                if (c != 103) {
                    if (c != 115) {
                        if (c == 116) {
                            X = "toLocaleString";
                            id = 5;
                            break;
                        }
                    }
                    X = "setUTCFullYear";
                    id = 44;
                    break;
                }
                X = "getUTCFullYear";
                id = 14;
                break;
                break;
            case 15:
                c = s.charAt(0);
                if (c != 103) {
                    if (c == 115) {
                        X = "setMilliseconds";
                        id = 31;
                        break;
                    }
                }
                X = "getMilliseconds";
                id = 27;
                break;
                break;
            case 17:
                X = "getTimezoneOffset";
                id = 29;
                break;
            case 18:
                c = s.charAt(0);
                if (c != 103) {
                    if (c != 115) {
                        if (c == 116) {
                            c = s.charAt(8);
                            if (c != 68) {
                                if (c == 84) {
                                    X = "toLocaleTimeString";
                                    id = 6;
                                    break;
                                }
                            }
                            X = "toLocaleDateString";
                            id = 7;
                            break;
                        }
                    }
                    X = "setUTCMilliseconds";
                    id = 32;
                    break;
                }
                X = "getUTCMilliseconds";
                id = 28;
                break;
                break;
        }
        if (X == null || X == s || X.equals(s)) {
            return id;
        }
        return 0;
    }
}
