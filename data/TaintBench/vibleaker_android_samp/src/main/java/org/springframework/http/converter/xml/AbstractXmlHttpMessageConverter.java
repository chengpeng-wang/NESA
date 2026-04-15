package org.springframework.http.converter.xml;

import java.io.IOException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;

public abstract class AbstractXmlHttpMessageConverter<T> extends AbstractHttpMessageConverter<T> {
    private final TransformerFactory transformerFactory = TransformerFactory.newInstance();

    public abstract T readFromSource(Class<? extends T> cls, HttpHeaders httpHeaders, Source source) throws IOException;

    public abstract void writeToResult(T t, HttpHeaders httpHeaders, Result result) throws IOException;

    protected AbstractXmlHttpMessageConverter() {
        super(MediaType.APPLICATION_XML, MediaType.TEXT_XML, new MediaType("application", "*+xml"));
    }

    public final T readInternal(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException {
        return readFromSource(clazz, inputMessage.getHeaders(), new StreamSource(inputMessage.getBody()));
    }

    /* access modifiers changed from: protected|final */
    public final void writeInternal(T t, HttpOutputMessage outputMessage) throws IOException {
        writeToResult(t, outputMessage.getHeaders(), new StreamResult(outputMessage.getBody()));
    }

    /* access modifiers changed from: protected */
    public void transform(Source source, Result result) throws TransformerException {
        this.transformerFactory.newTransformer().transform(source, result);
    }
}
