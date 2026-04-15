package org.springframework.http.converter.xml;

import org.springframework.http.converter.FormHttpMessageConverter;

@Deprecated
public class XmlAwareFormHttpMessageConverter extends FormHttpMessageConverter {
    public XmlAwareFormHttpMessageConverter() {
        addPartConverter(new SourceHttpMessageConverter());
    }
}
