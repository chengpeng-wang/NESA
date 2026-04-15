package flexjson.factories;

import flexjson.JSONException;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateObjectFactory implements ObjectFactory {
    List<DateFormat> dateFormats;

    public DateObjectFactory() {
        this.dateFormats = new ArrayList();
        this.dateFormats.add(DateFormat.getDateTimeInstance());
        this.dateFormats.add(DateFormat.getDateTimeInstance(1, 1));
        this.dateFormats.add(DateFormat.getDateTimeInstance(2, 2));
        this.dateFormats.add(DateFormat.getDateTimeInstance(3, 3));
        this.dateFormats.add(new SimpleDateFormat("EEE MMM d hh:mm:ss a z yyyy"));
        this.dateFormats.add(new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy"));
        this.dateFormats.add(new SimpleDateFormat("MM/dd/yy hh:mm:ss a"));
        this.dateFormats.add(new SimpleDateFormat("MM/dd/yy"));
    }

    public DateObjectFactory(List<DateFormat> dateFormats) {
        this.dateFormats = dateFormats;
    }

    public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
        try {
            if (value instanceof Double) {
                return instantiateDate((Class) targetType, Long.valueOf(((Double) value).longValue()), context);
            }
            if (value instanceof Long) {
                return instantiateDate((Class) targetType, (Long) value, context);
            }
            for (DateFormat format : this.dateFormats) {
                try {
                    return format.parse(value.toString());
                } catch (ParseException e) {
                }
            }
            throw new JSONException(String.format("%s:  Parsing date %s was not recognized as a date format", new Object[]{context.getCurrentPath(), value}));
        } catch (IllegalAccessException e2) {
            throw new JSONException(String.format("%s:  Error encountered trying to instantiate %s", new Object[]{context.getCurrentPath(), ((Class) targetType).getName()}), e2);
        } catch (InstantiationException e3) {
            throw new JSONException(String.format("%s:  Error encountered trying to instantiate %s.  Make sure there is a public constructor that accepts a single Long.", new Object[]{context.getCurrentPath(), ((Class) targetType).getName()}), e3);
        } catch (InvocationTargetException e4) {
            throw new JSONException(String.format("%s:  Error encountered trying to instantiate %s.  Make sure there is a public constructor that accepts a single Long.", new Object[]{context.getCurrentPath(), ((Class) targetType).getName()}), e4);
        }
    }

    private Date instantiateDate(Class targetType, Long value, ObjectBinder context) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        try {
            return (Date) targetType.getConstructor(new Class[]{Long.TYPE}).newInstance(new Object[]{value});
        } catch (NoSuchMethodException e) {
            Date d = (Date) targetType.newInstance();
            d.setTime(value.longValue());
            return d;
        }
    }
}
