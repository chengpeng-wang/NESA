package org.apache.log4j.pattern;

import java.util.Set;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.MDCKeySetExtractor;
import org.apache.log4j.spi.LoggingEvent;

public final class PropertiesPatternConverter extends LoggingEventPatternConverter {
    private final String option;

    private PropertiesPatternConverter(String[] options) {
        String stringBuffer = (options == null || options.length <= 0) ? "Properties" : new StringBuffer().append("Property{").append(options[0]).append("}").toString();
        super(stringBuffer, "property");
        if (options == null || options.length <= 0) {
            this.option = null;
        } else {
            this.option = options[0];
        }
    }

    public static PropertiesPatternConverter newInstance(String[] options) {
        return new PropertiesPatternConverter(options);
    }

    public void format(LoggingEvent event, StringBuffer toAppendTo) {
        if (this.option == null) {
            toAppendTo.append("{");
            try {
                Set keySet = MDCKeySetExtractor.INSTANCE.getPropertyKeySet(event);
                if (keySet != null) {
                    for (Object item : keySet) {
                        toAppendTo.append("{").append(item).append(",").append(event.getMDC(item.toString())).append("}");
                    }
                }
            } catch (Exception ex) {
                LogLog.error("Unexpected exception while extracting MDC keys", ex);
            }
            toAppendTo.append("}");
            return;
        }
        Object val = event.getMDC(this.option);
        if (val != null) {
            toAppendTo.append(val);
        }
    }
}
