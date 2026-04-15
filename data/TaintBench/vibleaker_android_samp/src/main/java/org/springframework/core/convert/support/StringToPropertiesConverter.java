package org.springframework.core.convert.support;

import java.io.ByteArrayInputStream;
import java.util.Properties;
import org.springframework.core.convert.converter.Converter;

final class StringToPropertiesConverter implements Converter<String, Properties> {
    StringToPropertiesConverter() {
    }

    public Properties convert(String source) {
        try {
            Properties props = new Properties();
            props.load(new ByteArrayInputStream(source.getBytes("ISO-8859-1")));
            return props;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to parse [" + source + "] into Properties", ex);
        }
    }
}
