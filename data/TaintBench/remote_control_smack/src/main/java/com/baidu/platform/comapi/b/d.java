package com.baidu.platform.comapi.b;

import android.os.Message;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.search.MKSearch;

class d {
    private c a = null;
    private a b = null;
    private e c = null;

    d() {
    }

    public void a() {
        this.c = null;
    }

    public void a(Message message) {
        if (message.what == 2000) {
            String str;
            if (message.arg1 == 4) {
                if (this.b == null) {
                    return;
                }
                if (message.arg2 != 0) {
                    this.b.a(null, message.arg2);
                    return;
                }
            } else if (this.a == null) {
                return;
            } else {
                if (message.arg2 != 0) {
                    this.a.a(message.arg2);
                    return;
                }
            }
            switch (message.arg1) {
                case 2:
                    if (message.arg2 == 0) {
                        str = "";
                        str = this.c.b(2);
                        if (str == null || str.equals("")) {
                            this.a.e(null);
                            return;
                        } else {
                            this.a.e(str);
                            return;
                        }
                    }
                    return;
                case 4:
                    if (message.arg2 == 0) {
                        str = this.c.b(message.arg1);
                        if (str == null || str.equals("")) {
                            this.b.a(null, message.arg2);
                            return;
                        } else {
                            this.b.a(str, message.arg2);
                            return;
                        }
                    }
                    return;
                case 6:
                    if (message.arg2 == 0) {
                        str = "";
                        str = this.c.b(6);
                        if (str == null || str.equals("")) {
                            this.a.f(null);
                            return;
                        } else {
                            this.a.f(str);
                            return;
                        }
                    }
                    return;
                case 7:
                    if (message.arg2 == 0) {
                        str = "";
                        str = this.c.b(7);
                        if (str == null || str.equals("")) {
                            this.a.b(null);
                            return;
                        } else {
                            this.a.b(str);
                            return;
                        }
                    }
                    return;
                case MKSearch.TYPE_POI_LIST /*11*/:
                case MKSearch.TYPE_AREA_POI_LIST /*21*/:
                    if (message.arg2 == 0) {
                        str = "";
                        str = this.c.b(11);
                        if (str == null || str.equals("")) {
                            this.a.a(null);
                            return;
                        } else {
                            this.a.a(str);
                            return;
                        }
                    }
                    return;
                case MKEvent.MKEVENT_MAP_MOVE_FINISH /*14*/:
                    if (message.arg2 == 0) {
                        str = this.c.b(message.arg1);
                        if (str == null || str.trim().length() <= 0) {
                            this.a.j(null);
                            return;
                        } else {
                            this.a.j(str);
                            return;
                        }
                    }
                    return;
                case MKEvent.MKEVENT_POIDETAILSHAREURL /*18*/:
                    if (message.arg2 == 0) {
                        str = "";
                        str = this.c.b(18);
                        if (str == null || str.equals("")) {
                            this.a.l(null);
                            return;
                        } else {
                            this.a.l(str);
                            return;
                        }
                    }
                    return;
                case 20:
                    if (message.arg2 == 0) {
                        str = "";
                        str = this.c.b(message.arg1);
                        if (str == null || str.equals("")) {
                            this.a.h(null);
                            return;
                        } else {
                            this.a.h(str);
                            return;
                        }
                    }
                    return;
                case 23:
                    if (message.arg2 == 0) {
                        str = "";
                        str = this.c.b(23);
                        if (str == null || str.equals("")) {
                            this.a.c(null);
                            return;
                        } else {
                            this.a.c(str);
                            return;
                        }
                    }
                    return;
                case 26:
                case 28:
                    if (message.arg2 == 0) {
                        str = "";
                        str = this.c.b(message.arg1);
                        if (str == null || str.equals("")) {
                            this.a.d(null);
                            return;
                        } else {
                            this.a.d(str);
                            return;
                        }
                    }
                    return;
                case 31:
                    if (message.arg2 == 0) {
                        str = "";
                        str = this.c.b(message.arg1);
                        if (str == null || str.equals("")) {
                            this.a.i(null);
                            return;
                        } else {
                            this.a.i(str);
                            return;
                        }
                    }
                    return;
                case 33:
                    if (message.arg2 == 0) {
                        str = this.c.b(message.arg1);
                        if (str == null || str.trim().length() <= 0) {
                            this.a.n(null);
                            return;
                        } else {
                            this.a.n(str);
                            return;
                        }
                    }
                    return;
                case 35:
                    if (message.arg2 == 0) {
                        str = "";
                        str = this.c.b(35);
                        if (str.equals("")) {
                            this.a.k(null);
                            return;
                        } else {
                            this.a.k(str);
                            return;
                        }
                    }
                    return;
                case 44:
                    break;
                case MKSearch.TYPE_AREA_MULTI_POI_LIST /*45*/:
                    if (message.arg2 == 0) {
                        this.a.a(this.c.b(45));
                        return;
                    }
                    return;
                case 46:
                    if (message.arg2 == 0) {
                        this.a.f(this.c.b(message.arg1));
                        return;
                    }
                    return;
                case 51:
                    if (message.arg2 == 0) {
                        str = "";
                        this.c.b(35);
                        break;
                    }
                    break;
                case 500:
                case 508:
                    if (message.arg2 == 0) {
                        str = this.c.b(message.arg1);
                        if (str == null || str.trim().length() <= 0) {
                            this.a.g(null);
                            return;
                        } else {
                            this.a.g(str);
                            return;
                        }
                    }
                    return;
                case 506:
                    if (message.arg2 == 0) {
                        str = "";
                        str = this.c.b(506);
                        if (str == null || str.equals("")) {
                            this.a.m(null);
                            return;
                        } else {
                            this.a.m(str);
                            return;
                        }
                    }
                    return;
                case 510:
                    if (message.arg2 == 0) {
                        str = this.c.b(message.arg1);
                        if (str == null || str.trim().length() <= 0) {
                            this.a.o(null);
                            return;
                        } else {
                            this.a.o(str);
                            return;
                        }
                    }
                    return;
                default:
                    return;
            }
            if (message.arg2 == 0) {
                str = "";
                str = this.c.b(44);
                if (str == null || str.equals("")) {
                    this.a.k(null);
                } else {
                    this.a.k(str);
                }
            }
        }
    }

    public void a(c cVar) {
        this.a = cVar;
    }

    public void a(e eVar) {
        this.c = eVar;
    }
}
