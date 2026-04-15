package org.springframework.http.converter.support;

import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.util.ClassUtils;

public class AllEncompassingFormHttpMessageConverter extends FormHttpMessageConverter {
    private static final boolean gsonPresent = ClassUtils.isPresent("com.google.gson.Gson", AllEncompassingFormHttpMessageConverter.class.getClassLoader());
    private static final boolean jackson2Present;

    static {
        boolean z = ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", AllEncompassingFormHttpMessageConverter.class.getClassLoader()) && ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", AllEncompassingFormHttpMessageConverter.class.getClassLoader());
        jackson2Present = z;
    }

    public AllEncompassingFormHttpMessageConverter() {
        addPartConverter(new SourceHttpMessageConverter());
        if (jackson2Present) {
            addPartConverter(new MappingJackson2HttpMessageConverter());
        } else if (gsonPresent) {
            addPartConverter(new GsonHttpMessageConverter());
        }
    }
}
