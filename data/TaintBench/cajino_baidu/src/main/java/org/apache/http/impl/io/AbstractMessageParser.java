package org.apache.http.impl.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.ParseException;
import org.apache.http.ProtocolException;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.CharArrayBuffer;

public abstract class AbstractMessageParser implements HttpMessageParser {
    private static final int HEADERS = 1;
    private static final int HEAD_LINE = 0;
    private final List headerLines;
    protected final LineParser lineParser;
    private final int maxHeaderCount;
    private final int maxLineLen;
    private HttpMessage message;
    private final SessionInputBuffer sessionBuffer;
    private int state;

    public abstract HttpMessage parseHead(SessionInputBuffer sessionInputBuffer) throws IOException, HttpException, ParseException;

    public AbstractMessageParser(SessionInputBuffer buffer, LineParser parser, HttpParams params) {
        if (buffer == null) {
            throw new IllegalArgumentException("Session input buffer may not be null");
        } else if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        } else {
            this.sessionBuffer = buffer;
            this.maxHeaderCount = params.getIntParameter(CoreConnectionPNames.MAX_HEADER_COUNT, -1);
            this.maxLineLen = params.getIntParameter(CoreConnectionPNames.MAX_LINE_LENGTH, -1);
            if (parser == null) {
                parser = BasicLineParser.DEFAULT;
            }
            this.lineParser = parser;
            this.headerLines = new ArrayList();
            this.state = 0;
        }
    }

    public static Header[] parseHeaders(SessionInputBuffer inbuffer, int maxHeaderCount, int maxLineLen, LineParser parser) throws HttpException, IOException {
        if (parser == null) {
            parser = BasicLineParser.DEFAULT;
        }
        return parseHeaders(inbuffer, maxHeaderCount, maxLineLen, parser, new ArrayList());
    }

    public static Header[] parseHeaders(SessionInputBuffer inbuffer, int maxHeaderCount, int maxLineLen, LineParser parser, List headerLines) throws HttpException, IOException {
        if (inbuffer == null) {
            throw new IllegalArgumentException("Session input buffer may not be null");
        } else if (parser == null) {
            throw new IllegalArgumentException("Line parser may not be null");
        } else if (headerLines == null) {
            throw new IllegalArgumentException("Header line list may not be null");
        } else {
            Header[] headers;
            int i;
            CharArrayBuffer current = null;
            CharArrayBuffer previous = null;
            while (true) {
                if (current == null) {
                    current = new CharArrayBuffer(64);
                } else {
                    current.clear();
                }
                if (inbuffer.readLine(current) == -1 || current.length() < 1) {
                    headers = new Header[headerLines.size()];
                    i = 0;
                } else {
                    if ((current.charAt(0) == ' ' || current.charAt(0) == 9) && previous != null) {
                        i = 0;
                        while (i < current.length()) {
                            char ch = current.charAt(i);
                            if (ch != ' ' && ch != 9) {
                                break;
                            }
                            i++;
                        }
                        if (maxLineLen <= 0 || ((previous.length() + 1) + current.length()) - i <= maxLineLen) {
                            previous.append(' ');
                            previous.append(current, i, current.length() - i);
                        } else {
                            throw new IOException("Maximum line length limit exceeded");
                        }
                    }
                    headerLines.add(current);
                    previous = current;
                    current = null;
                    if (maxHeaderCount > 0 && headerLines.size() >= maxHeaderCount) {
                        throw new IOException("Maximum header count exceeded");
                    }
                }
            }
            headers = new Header[headerLines.size()];
            i = 0;
            while (i < headerLines.size()) {
                try {
                    headers[i] = parser.parseHeader((CharArrayBuffer) headerLines.get(i));
                    i++;
                } catch (ParseException ex) {
                    throw new ProtocolException(ex.getMessage());
                }
            }
            return headers;
        }
    }

    public HttpMessage parse() throws IOException, HttpException {
        switch (this.state) {
            case 0:
                try {
                    this.message = parseHead(this.sessionBuffer);
                    this.state = 1;
                    break;
                } catch (ParseException px) {
                    throw new ProtocolException(px.getMessage(), px);
                }
            case 1:
                break;
            default:
                throw new IllegalStateException("Inconsistent parser state");
        }
        this.message.setHeaders(parseHeaders(this.sessionBuffer, this.maxHeaderCount, this.maxLineLen, this.lineParser, this.headerLines));
        HttpMessage result = this.message;
        this.message = null;
        this.headerLines.clear();
        this.state = 0;
        return result;
    }
}
