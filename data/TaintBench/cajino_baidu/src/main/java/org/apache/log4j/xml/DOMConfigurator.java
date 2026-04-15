package org.apache.log4j.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.config.PropertySetter;
import org.apache.log4j.helpers.FileWatchdog;
import org.apache.log4j.helpers.Loader;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.or.RendererMap;
import org.apache.log4j.spi.AppenderAttachable;
import org.apache.log4j.spi.Configurator;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RendererSupport;
import org.apache.log4j.spi.ThrowableRenderer;
import org.apache.log4j.spi.ThrowableRendererSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DOMConfigurator implements Configurator {
    static final String ADDITIVITY_ATTR = "additivity";
    static final String APPENDER_REF_TAG = "appender-ref";
    static final String APPENDER_TAG = "appender";
    static final String CATEGORY = "category";
    static final String CATEGORY_FACTORY_TAG = "categoryFactory";
    static final String CLASS_ATTR = "class";
    static final String CONFIGURATION_TAG = "log4j:configuration";
    static final String CONFIG_DEBUG_ATTR = "configDebug";
    static final String EMPTY_STR = "";
    static final String ERROR_HANDLER_TAG = "errorHandler";
    static final String FILTER_TAG = "filter";
    static final String INTERNAL_DEBUG_ATTR = "debug";
    static final String LAYOUT_TAG = "layout";
    static final String LEVEL_TAG = "level";
    static final String LOGGER = "logger";
    static final String LOGGER_FACTORY_TAG = "loggerFactory";
    static final String LOGGER_REF = "logger-ref";
    static final String NAME_ATTR = "name";
    static final String OLD_CONFIGURATION_TAG = "configuration";
    static final Class[] ONE_STRING_PARAM;
    static final String PARAM_TAG = "param";
    static final String PRIORITY_TAG = "priority";
    static final String REF_ATTR = "ref";
    static final String RENDERED_CLASS_ATTR = "renderedClass";
    static final String RENDERER_TAG = "renderer";
    static final String RENDERING_CLASS_ATTR = "renderingClass";
    private static final String RESET_ATTR = "reset";
    static final String ROOT_REF = "root-ref";
    static final String ROOT_TAG = "root";
    static final String THRESHOLD_ATTR = "threshold";
    private static final String THROWABLE_RENDERER_TAG = "throwableRenderer";
    static final String VALUE_ATTR = "value";
    static Class class$java$lang$String = null;
    static Class class$org$apache$log4j$spi$ErrorHandler = null;
    static Class class$org$apache$log4j$spi$Filter = null;
    static Class class$org$apache$log4j$spi$LoggerFactory = null;
    static final String dbfKey = "javax.xml.parsers.DocumentBuilderFactory";
    Hashtable appenderBag = new Hashtable();
    protected LoggerFactory catFactory = null;
    Properties props;
    LoggerRepository repository;

    private interface ParseAction {
        Document parse(DocumentBuilder documentBuilder) throws SAXException, IOException;
    }

    /* renamed from: org.apache.log4j.xml.DOMConfigurator$1 */
    class AnonymousClass1 implements ParseAction {
        private final DOMConfigurator this$0;
        private final String val$filename;

        AnonymousClass1(DOMConfigurator dOMConfigurator, String str) {
            this.this$0 = dOMConfigurator;
            this.val$filename = str;
        }

        public Document parse(DocumentBuilder parser) throws SAXException, IOException {
            return parser.parse(new File(this.val$filename));
        }

        public String toString() {
            return new StringBuffer().append("file [").append(this.val$filename).append("]").toString();
        }
    }

    /* renamed from: org.apache.log4j.xml.DOMConfigurator$2 */
    class AnonymousClass2 implements ParseAction {
        private final DOMConfigurator this$0;
        private final URL val$url;

        AnonymousClass2(DOMConfigurator dOMConfigurator, URL url) {
            this.this$0 = dOMConfigurator;
            this.val$url = url;
        }

        public Document parse(DocumentBuilder parser) throws SAXException, IOException {
            URLConnection uConn = this.val$url.openConnection();
            uConn.setUseCaches(false);
            InputSource src = new InputSource(uConn.getInputStream());
            src.setSystemId(this.val$url.toString());
            return parser.parse(src);
        }

        public String toString() {
            return new StringBuffer().append("url [").append(this.val$url.toString()).append("]").toString();
        }
    }

    /* renamed from: org.apache.log4j.xml.DOMConfigurator$3 */
    class AnonymousClass3 implements ParseAction {
        private final DOMConfigurator this$0;
        private final InputStream val$inputStream;

        AnonymousClass3(DOMConfigurator dOMConfigurator, InputStream inputStream) {
            this.this$0 = dOMConfigurator;
            this.val$inputStream = inputStream;
        }

        public Document parse(DocumentBuilder parser) throws SAXException, IOException {
            InputSource inputSource = new InputSource(this.val$inputStream);
            inputSource.setSystemId("dummy://log4j.dtd");
            return parser.parse(inputSource);
        }

        public String toString() {
            return new StringBuffer().append("input stream [").append(this.val$inputStream.toString()).append("]").toString();
        }
    }

    /* renamed from: org.apache.log4j.xml.DOMConfigurator$4 */
    class AnonymousClass4 implements ParseAction {
        private final DOMConfigurator this$0;
        private final Reader val$reader;

        AnonymousClass4(DOMConfigurator dOMConfigurator, Reader reader) {
            this.this$0 = dOMConfigurator;
            this.val$reader = reader;
        }

        public Document parse(DocumentBuilder parser) throws SAXException, IOException {
            InputSource inputSource = new InputSource(this.val$reader);
            inputSource.setSystemId("dummy://log4j.dtd");
            return parser.parse(inputSource);
        }

        public String toString() {
            return new StringBuffer().append("reader [").append(this.val$reader.toString()).append("]").toString();
        }
    }

    /* renamed from: org.apache.log4j.xml.DOMConfigurator$5 */
    class AnonymousClass5 implements ParseAction {
        private final DOMConfigurator this$0;
        private final InputSource val$inputSource;

        AnonymousClass5(DOMConfigurator dOMConfigurator, InputSource inputSource) {
            this.this$0 = dOMConfigurator;
            this.val$inputSource = inputSource;
        }

        public Document parse(DocumentBuilder parser) throws SAXException, IOException {
            return parser.parse(this.val$inputSource);
        }

        public String toString() {
            return new StringBuffer().append("input source [").append(this.val$inputSource.toString()).append("]").toString();
        }
    }

    static {
        Class class$;
        Class[] clsArr = new Class[1];
        if (class$java$lang$String == null) {
            class$ = class$("java.lang.String");
            class$java$lang$String = class$;
        } else {
            class$ = class$java$lang$String;
        }
        clsArr[0] = class$;
        ONE_STRING_PARAM = clsArr;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    /* access modifiers changed from: protected */
    public Appender findAppenderByName(Document doc, String appenderName) {
        Appender appender = (Appender) this.appenderBag.get(appenderName);
        if (appender != null) {
            return appender;
        }
        Element element = null;
        NodeList list = doc.getElementsByTagName(APPENDER_TAG);
        for (int t = 0; t < list.getLength(); t++) {
            Node node = list.item(t);
            if (appenderName.equals(node.getAttributes().getNamedItem(NAME_ATTR).getNodeValue())) {
                element = (Element) node;
                break;
            }
        }
        if (element == null) {
            LogLog.error(new StringBuffer().append("No appender named [").append(appenderName).append("] could be found.").toString());
            return null;
        }
        appender = parseAppender(element);
        if (appender != null) {
            this.appenderBag.put(appenderName, appender);
        }
        return appender;
    }

    /* access modifiers changed from: protected */
    public Appender findAppenderByReference(Element appenderRef) {
        return findAppenderByName(appenderRef.getOwnerDocument(), subst(appenderRef.getAttribute(REF_ATTR)));
    }

    private static void parseUnrecognizedElement(Object instance, Element element, Properties props) throws Exception {
        boolean recognized = false;
        if (instance instanceof UnrecognizedElementHandler) {
            recognized = ((UnrecognizedElementHandler) instance).parseUnrecognizedElement(element, props);
        }
        if (!recognized) {
            LogLog.warn(new StringBuffer().append("Unrecognized element ").append(element.getNodeName()).toString());
        }
    }

    private static void quietParseUnrecognizedElement(Object instance, Element element, Properties props) {
        try {
            parseUnrecognizedElement(instance, element, props);
        } catch (Exception ex) {
            if ((ex instanceof InterruptedException) || (ex instanceof InterruptedIOException)) {
                Thread.currentThread().interrupt();
            }
            LogLog.error("Error in extension content: ", ex);
        }
    }

    /* access modifiers changed from: protected */
    public Appender parseAppender(Element appenderElement) {
        String className = subst(appenderElement.getAttribute(CLASS_ATTR));
        LogLog.debug(new StringBuffer().append("Class name: [").append(className).append(']').toString());
        try {
            Object instance = Loader.loadClass(className).newInstance();
            Appender appender = (Appender) instance;
            PropertySetter propSetter = new PropertySetter(appender);
            appender.setName(subst(appenderElement.getAttribute(NAME_ATTR)));
            NodeList children = appenderElement.getChildNodes();
            int length = children.getLength();
            for (int loop = 0; loop < length; loop++) {
                Node currentNode = children.item(loop);
                if (currentNode.getNodeType() == (short) 1) {
                    Element currentElement = (Element) currentNode;
                    if (currentElement.getTagName().equals(PARAM_TAG)) {
                        setParameter(currentElement, propSetter);
                    } else if (currentElement.getTagName().equals(LAYOUT_TAG)) {
                        appender.setLayout(parseLayout(currentElement));
                    } else if (currentElement.getTagName().equals(FILTER_TAG)) {
                        parseFilters(currentElement, appender);
                    } else if (currentElement.getTagName().equals(ERROR_HANDLER_TAG)) {
                        parseErrorHandler(currentElement, appender);
                    } else if (currentElement.getTagName().equals(APPENDER_REF_TAG)) {
                        String refName = subst(currentElement.getAttribute(REF_ATTR));
                        if (appender instanceof AppenderAttachable) {
                            AppenderAttachable aa = (AppenderAttachable) appender;
                            LogLog.debug(new StringBuffer().append("Attaching appender named [").append(refName).append("] to appender named [").append(appender.getName()).append("].").toString());
                            aa.addAppender(findAppenderByReference(currentElement));
                        } else {
                            LogLog.error(new StringBuffer().append("Requesting attachment of appender named [").append(refName).append("] to appender named [").append(appender.getName()).append("] which does not implement org.apache.log4j.spi.AppenderAttachable.").toString());
                        }
                    } else {
                        parseUnrecognizedElement(instance, currentElement, this.props);
                    }
                }
            }
            propSetter.activate();
            return appender;
        } catch (Exception oops) {
            if ((oops instanceof InterruptedException) || (oops instanceof InterruptedIOException)) {
                Thread.currentThread().interrupt();
            }
            LogLog.error("Could not create an Appender. Reported error follows.", oops);
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void parseErrorHandler(Element element, Appender appender) {
        Class class$;
        String subst = subst(element.getAttribute(CLASS_ATTR));
        if (class$org$apache$log4j$spi$ErrorHandler == null) {
            class$ = class$("org.apache.log4j.spi.ErrorHandler");
            class$org$apache$log4j$spi$ErrorHandler = class$;
        } else {
            class$ = class$org$apache$log4j$spi$ErrorHandler;
        }
        ErrorHandler eh = (ErrorHandler) OptionConverter.instantiateByClassName(subst, class$, null);
        if (eh != null) {
            eh.setAppender(appender);
            PropertySetter propSetter = new PropertySetter(eh);
            NodeList children = element.getChildNodes();
            int length = children.getLength();
            for (int loop = 0; loop < length; loop++) {
                Node currentNode = children.item(loop);
                if (currentNode.getNodeType() == (short) 1) {
                    Element currentElement = (Element) currentNode;
                    String tagName = currentElement.getTagName();
                    if (tagName.equals(PARAM_TAG)) {
                        setParameter(currentElement, propSetter);
                    } else if (tagName.equals(APPENDER_REF_TAG)) {
                        eh.setBackupAppender(findAppenderByReference(currentElement));
                    } else if (tagName.equals(LOGGER_REF)) {
                        String loggerName = currentElement.getAttribute(REF_ATTR);
                        eh.setLogger(this.catFactory == null ? this.repository.getLogger(loggerName) : this.repository.getLogger(loggerName, this.catFactory));
                    } else if (tagName.equals(ROOT_REF)) {
                        eh.setLogger(this.repository.getRootLogger());
                    } else {
                        quietParseUnrecognizedElement(eh, currentElement, this.props);
                    }
                }
            }
            propSetter.activate();
            appender.setErrorHandler(eh);
        }
    }

    /* access modifiers changed from: protected */
    public void parseFilters(Element element, Appender appender) {
        Class class$;
        String clazz = subst(element.getAttribute(CLASS_ATTR));
        if (class$org$apache$log4j$spi$Filter == null) {
            class$ = class$("org.apache.log4j.spi.Filter");
            class$org$apache$log4j$spi$Filter = class$;
        } else {
            class$ = class$org$apache$log4j$spi$Filter;
        }
        Filter filter = (Filter) OptionConverter.instantiateByClassName(clazz, class$, null);
        if (filter != null) {
            PropertySetter propSetter = new PropertySetter(filter);
            NodeList children = element.getChildNodes();
            int length = children.getLength();
            for (int loop = 0; loop < length; loop++) {
                Node currentNode = children.item(loop);
                if (currentNode.getNodeType() == (short) 1) {
                    Element currentElement = (Element) currentNode;
                    if (currentElement.getTagName().equals(PARAM_TAG)) {
                        setParameter(currentElement, propSetter);
                    } else {
                        quietParseUnrecognizedElement(filter, currentElement, this.props);
                    }
                }
            }
            propSetter.activate();
            LogLog.debug(new StringBuffer().append("Adding filter of type [").append(filter.getClass()).append("] to appender named [").append(appender.getName()).append("].").toString());
            appender.addFilter(filter);
        }
    }

    /* access modifiers changed from: protected */
    public void parseCategory(Element loggerElement) {
        Logger cat;
        String catName = subst(loggerElement.getAttribute(NAME_ATTR));
        String className = subst(loggerElement.getAttribute(CLASS_ATTR));
        if (EMPTY_STR.equals(className)) {
            LogLog.debug("Retreiving an instance of org.apache.log4j.Logger.");
            if (this.catFactory == null) {
                cat = this.repository.getLogger(catName);
            } else {
                cat = this.repository.getLogger(catName, this.catFactory);
            }
        } else {
            LogLog.debug(new StringBuffer().append("Desired logger sub-class: [").append(className).append(']').toString());
            try {
                cat = (Logger) Loader.loadClass(className).getMethod("getLogger", ONE_STRING_PARAM).invoke(null, new Object[]{catName});
            } catch (InvocationTargetException oops) {
                if ((oops.getTargetException() instanceof InterruptedException) || (oops.getTargetException() instanceof InterruptedIOException)) {
                    Thread.currentThread().interrupt();
                }
                LogLog.error(new StringBuffer().append("Could not retrieve category [").append(catName).append("]. Reported error follows.").toString(), oops);
                return;
            } catch (Exception oops2) {
                LogLog.error(new StringBuffer().append("Could not retrieve category [").append(catName).append("]. Reported error follows.").toString(), oops2);
                return;
            }
        }
        synchronized (cat) {
            boolean additivity = OptionConverter.toBoolean(subst(loggerElement.getAttribute(ADDITIVITY_ATTR)), true);
            LogLog.debug(new StringBuffer().append("Setting [").append(cat.getName()).append("] additivity to [").append(additivity).append("].").toString());
            cat.setAdditivity(additivity);
            parseChildrenOfLoggerElement(loggerElement, cat, false);
        }
    }

    /* access modifiers changed from: protected */
    public void parseCategoryFactory(Element factoryElement) {
        String className = subst(factoryElement.getAttribute(CLASS_ATTR));
        if (EMPTY_STR.equals(className)) {
            LogLog.error("Category Factory tag class attribute not found.");
            LogLog.debug("No Category Factory configured.");
            return;
        }
        Class class$;
        LogLog.debug(new StringBuffer().append("Desired category factory: [").append(className).append(']').toString());
        if (class$org$apache$log4j$spi$LoggerFactory == null) {
            class$ = class$("org.apache.log4j.spi.LoggerFactory");
            class$org$apache$log4j$spi$LoggerFactory = class$;
        } else {
            class$ = class$org$apache$log4j$spi$LoggerFactory;
        }
        Object factory = OptionConverter.instantiateByClassName(className, class$, null);
        if (factory instanceof LoggerFactory) {
            this.catFactory = (LoggerFactory) factory;
        } else {
            LogLog.error(new StringBuffer().append("Category Factory class ").append(className).append(" does not implement org.apache.log4j.LoggerFactory").toString());
        }
        PropertySetter propSetter = new PropertySetter(factory);
        NodeList children = factoryElement.getChildNodes();
        int length = children.getLength();
        for (int loop = 0; loop < length; loop++) {
            Node currentNode = children.item(loop);
            if (currentNode.getNodeType() == (short) 1) {
                Element currentElement = (Element) currentNode;
                if (currentElement.getTagName().equals(PARAM_TAG)) {
                    setParameter(currentElement, propSetter);
                } else {
                    quietParseUnrecognizedElement(factory, currentElement, this.props);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void parseRoot(Element rootElement) {
        Logger root = this.repository.getRootLogger();
        synchronized (root) {
            parseChildrenOfLoggerElement(rootElement, root, true);
        }
    }

    /* access modifiers changed from: protected */
    public void parseChildrenOfLoggerElement(Element catElement, Logger cat, boolean isRoot) {
        PropertySetter propSetter = new PropertySetter(cat);
        cat.removeAllAppenders();
        NodeList children = catElement.getChildNodes();
        int length = children.getLength();
        for (int loop = 0; loop < length; loop++) {
            Node currentNode = children.item(loop);
            if (currentNode.getNodeType() == (short) 1) {
                Element currentElement = (Element) currentNode;
                String tagName = currentElement.getTagName();
                if (tagName.equals(APPENDER_REF_TAG)) {
                    Element appenderRef = (Element) currentNode;
                    Appender appender = findAppenderByReference(appenderRef);
                    String refName = subst(appenderRef.getAttribute(REF_ATTR));
                    if (appender != null) {
                        LogLog.debug(new StringBuffer().append("Adding appender named [").append(refName).append("] to category [").append(cat.getName()).append("].").toString());
                    } else {
                        LogLog.debug(new StringBuffer().append("Appender named [").append(refName).append("] not found.").toString());
                    }
                    cat.addAppender(appender);
                } else if (tagName.equals(LEVEL_TAG)) {
                    parseLevel(currentElement, cat, isRoot);
                } else if (tagName.equals("priority")) {
                    parseLevel(currentElement, cat, isRoot);
                } else if (tagName.equals(PARAM_TAG)) {
                    setParameter(currentElement, propSetter);
                } else {
                    quietParseUnrecognizedElement(cat, currentElement, this.props);
                }
            }
        }
        propSetter.activate();
    }

    /* access modifiers changed from: protected */
    public Layout parseLayout(Element layout_element) {
        String className = subst(layout_element.getAttribute(CLASS_ATTR));
        LogLog.debug(new StringBuffer().append("Parsing layout of class: \"").append(className).append("\"").toString());
        try {
            Object instance = Loader.loadClass(className).newInstance();
            Layout layout = (Layout) instance;
            PropertySetter propSetter = new PropertySetter(layout);
            NodeList params = layout_element.getChildNodes();
            int length = params.getLength();
            for (int loop = 0; loop < length; loop++) {
                Node currentNode = params.item(loop);
                if (currentNode.getNodeType() == (short) 1) {
                    Element currentElement = (Element) currentNode;
                    if (currentElement.getTagName().equals(PARAM_TAG)) {
                        setParameter(currentElement, propSetter);
                    } else {
                        parseUnrecognizedElement(instance, currentElement, this.props);
                    }
                }
            }
            propSetter.activate();
            return layout;
        } catch (Exception oops) {
            if ((oops instanceof InterruptedException) || (oops instanceof InterruptedIOException)) {
                Thread.currentThread().interrupt();
            }
            LogLog.error("Could not create the Layout. Reported error follows.", oops);
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void parseRenderer(Element element) {
        String renderingClass = subst(element.getAttribute(RENDERING_CLASS_ATTR));
        String renderedClass = subst(element.getAttribute(RENDERED_CLASS_ATTR));
        if (this.repository instanceof RendererSupport) {
            RendererMap.addRenderer((RendererSupport) this.repository, renderedClass, renderingClass);
        }
    }

    /* access modifiers changed from: protected */
    public ThrowableRenderer parseThrowableRenderer(Element element) {
        String className = subst(element.getAttribute(CLASS_ATTR));
        LogLog.debug(new StringBuffer().append("Parsing throwableRenderer of class: \"").append(className).append("\"").toString());
        try {
            Object instance = Loader.loadClass(className).newInstance();
            ThrowableRenderer throwableRenderer = (ThrowableRenderer) instance;
            PropertySetter propSetter = new PropertySetter(throwableRenderer);
            NodeList params = element.getChildNodes();
            int length = params.getLength();
            for (int loop = 0; loop < length; loop++) {
                Node currentNode = params.item(loop);
                if (currentNode.getNodeType() == (short) 1) {
                    Element currentElement = (Element) currentNode;
                    if (currentElement.getTagName().equals(PARAM_TAG)) {
                        setParameter(currentElement, propSetter);
                    } else {
                        parseUnrecognizedElement(instance, currentElement, this.props);
                    }
                }
            }
            propSetter.activate();
            return throwableRenderer;
        } catch (Exception oops) {
            if ((oops instanceof InterruptedException) || (oops instanceof InterruptedIOException)) {
                Thread.currentThread().interrupt();
            }
            LogLog.error("Could not create the ThrowableRenderer. Reported error follows.", oops);
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void parseLevel(Element element, Logger logger, boolean isRoot) {
        String catName = logger.getName();
        if (isRoot) {
            catName = ROOT_TAG;
        }
        String priStr = subst(element.getAttribute(VALUE_ATTR));
        LogLog.debug(new StringBuffer().append("Level value for ").append(catName).append(" is  [").append(priStr).append("].").toString());
        if (!Configurator.INHERITED.equalsIgnoreCase(priStr) && !"null".equalsIgnoreCase(priStr)) {
            String className = subst(element.getAttribute(CLASS_ATTR));
            if (EMPTY_STR.equals(className)) {
                logger.setLevel(OptionConverter.toLevel(priStr, Level.DEBUG));
            } else {
                LogLog.debug(new StringBuffer().append("Desired Level sub-class: [").append(className).append(']').toString());
                try {
                    logger.setLevel((Level) Loader.loadClass(className).getMethod("toLevel", ONE_STRING_PARAM).invoke(null, new Object[]{priStr}));
                } catch (Exception oops) {
                    if ((oops instanceof InterruptedException) || (oops instanceof InterruptedIOException)) {
                        Thread.currentThread().interrupt();
                    }
                    LogLog.error(new StringBuffer().append("Could not create level [").append(priStr).append("]. Reported error follows.").toString(), oops);
                    return;
                }
            }
        } else if (isRoot) {
            LogLog.error("Root level cannot be inherited. Ignoring directive.");
        } else {
            logger.setLevel(null);
        }
        LogLog.debug(new StringBuffer().append(catName).append(" level set to ").append(logger.getLevel()).toString());
    }

    /* access modifiers changed from: protected */
    public void setParameter(Element elem, PropertySetter propSetter) {
        propSetter.setProperty(subst(elem.getAttribute(NAME_ATTR)), subst(OptionConverter.convertSpecialChars(elem.getAttribute(VALUE_ATTR))));
    }

    public static void configure(Element element) {
        new DOMConfigurator().doConfigure(element, LogManager.getLoggerRepository());
    }

    public static void configureAndWatch(String configFilename) {
        configureAndWatch(configFilename, FileWatchdog.DEFAULT_DELAY);
    }

    public static void configureAndWatch(String configFilename, long delay) {
        XMLWatchdog xdog = new XMLWatchdog(configFilename);
        xdog.setDelay(delay);
        xdog.start();
    }

    public void doConfigure(String filename, LoggerRepository repository) {
        doConfigure(new AnonymousClass1(this, filename), repository);
    }

    public void doConfigure(URL url, LoggerRepository repository) {
        doConfigure(new AnonymousClass2(this, url), repository);
    }

    public void doConfigure(InputStream inputStream, LoggerRepository repository) throws FactoryConfigurationError {
        doConfigure(new AnonymousClass3(this, inputStream), repository);
    }

    public void doConfigure(Reader reader, LoggerRepository repository) throws FactoryConfigurationError {
        doConfigure(new AnonymousClass4(this, reader), repository);
    }

    /* access modifiers changed from: protected */
    public void doConfigure(InputSource inputSource, LoggerRepository repository) throws FactoryConfigurationError {
        if (inputSource.getSystemId() == null) {
            inputSource.setSystemId("dummy://log4j.dtd");
        }
        doConfigure(new AnonymousClass5(this, inputSource), repository);
    }

    private final void doConfigure(ParseAction action, LoggerRepository repository) throws FactoryConfigurationError {
        this.repository = repository;
        try {
            LogLog.debug(new StringBuffer().append("System property is :").append(OptionConverter.getSystemProperty(dbfKey, null)).toString());
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            LogLog.debug("Standard DocumentBuilderFactory search succeded.");
            LogLog.debug(new StringBuffer().append("DocumentBuilderFactory is: ").append(dbf.getClass().getName()).toString());
            try {
                dbf.setValidating(true);
                DocumentBuilder docBuilder = dbf.newDocumentBuilder();
                docBuilder.setErrorHandler(new SAXErrorHandler());
                docBuilder.setEntityResolver(new Log4jEntityResolver());
                parse(action.parse(docBuilder).getDocumentElement());
            } catch (Exception e) {
                if ((e instanceof InterruptedException) || (e instanceof InterruptedIOException)) {
                    Thread.currentThread().interrupt();
                }
                LogLog.error(new StringBuffer().append("Could not parse ").append(action.toString()).append(".").toString(), e);
            }
        } catch (FactoryConfigurationError fce) {
            LogLog.debug("Could not instantiate a DocumentBuilderFactory.", fce.getException());
            throw fce;
        }
    }

    public void doConfigure(Element element, LoggerRepository repository) {
        this.repository = repository;
        parse(element);
    }

    public static void configure(String filename) throws FactoryConfigurationError {
        new DOMConfigurator().doConfigure(filename, LogManager.getLoggerRepository());
    }

    public static void configure(URL url) throws FactoryConfigurationError {
        new DOMConfigurator().doConfigure(url, LogManager.getLoggerRepository());
    }

    /* access modifiers changed from: protected */
    public void parse(Element element) {
        int loop;
        Node currentNode;
        Element currentElement;
        String tagName;
        String rootElementName = element.getTagName();
        if (!rootElementName.equals(CONFIGURATION_TAG)) {
            if (rootElementName.equals(OLD_CONFIGURATION_TAG)) {
                LogLog.warn("The <configuration> element has been deprecated.");
                LogLog.warn("Use the <log4j:configuration> element instead.");
            } else {
                LogLog.error("DOM element is - not a <log4j:configuration> element.");
                return;
            }
        }
        String debugAttrib = subst(element.getAttribute(INTERNAL_DEBUG_ATTR));
        LogLog.debug(new StringBuffer().append("debug attribute= \"").append(debugAttrib).append("\".").toString());
        if (debugAttrib.equals(EMPTY_STR) || debugAttrib.equals("null")) {
            LogLog.debug("Ignoring debug attribute.");
        } else {
            LogLog.setInternalDebugging(OptionConverter.toBoolean(debugAttrib, true));
        }
        String resetAttrib = subst(element.getAttribute(RESET_ATTR));
        LogLog.debug(new StringBuffer().append("reset attribute= \"").append(resetAttrib).append("\".").toString());
        if (!EMPTY_STR.equals(resetAttrib) && OptionConverter.toBoolean(resetAttrib, false)) {
            this.repository.resetConfiguration();
        }
        String confDebug = subst(element.getAttribute(CONFIG_DEBUG_ATTR));
        if (!(confDebug.equals(EMPTY_STR) || confDebug.equals("null"))) {
            LogLog.warn("The \"configDebug\" attribute is deprecated.");
            LogLog.warn("Use the \"debug\" attribute instead.");
            LogLog.setInternalDebugging(OptionConverter.toBoolean(confDebug, true));
        }
        String thresholdStr = subst(element.getAttribute(THRESHOLD_ATTR));
        LogLog.debug(new StringBuffer().append("Threshold =\"").append(thresholdStr).append("\".").toString());
        if (!(EMPTY_STR.equals(thresholdStr) || "null".equals(thresholdStr))) {
            this.repository.setThreshold(thresholdStr);
        }
        NodeList children = element.getChildNodes();
        int length = children.getLength();
        for (loop = 0; loop < length; loop++) {
            currentNode = children.item(loop);
            if (currentNode.getNodeType() == (short) 1) {
                currentElement = (Element) currentNode;
                tagName = currentElement.getTagName();
                if (tagName.equals(CATEGORY_FACTORY_TAG) || tagName.equals(LOGGER_FACTORY_TAG)) {
                    parseCategoryFactory(currentElement);
                }
            }
        }
        for (loop = 0; loop < length; loop++) {
            currentNode = children.item(loop);
            if (currentNode.getNodeType() == (short) 1) {
                currentElement = (Element) currentNode;
                tagName = currentElement.getTagName();
                if (tagName.equals(CATEGORY) || tagName.equals(LOGGER)) {
                    parseCategory(currentElement);
                } else if (tagName.equals(ROOT_TAG)) {
                    parseRoot(currentElement);
                } else if (tagName.equals(RENDERER_TAG)) {
                    parseRenderer(currentElement);
                } else if (tagName.equals(THROWABLE_RENDERER_TAG)) {
                    if (this.repository instanceof ThrowableRendererSupport) {
                        ThrowableRenderer tr = parseThrowableRenderer(currentElement);
                        if (tr != null) {
                            ((ThrowableRendererSupport) this.repository).setThrowableRenderer(tr);
                        }
                    }
                } else if (!(tagName.equals(APPENDER_TAG) || tagName.equals(CATEGORY_FACTORY_TAG) || tagName.equals(LOGGER_FACTORY_TAG))) {
                    quietParseUnrecognizedElement(this.repository, currentElement, this.props);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public String subst(String value) {
        return subst(value, this.props);
    }

    public static String subst(String value, Properties props) {
        try {
            return OptionConverter.substVars(value, props);
        } catch (IllegalArgumentException e) {
            LogLog.warn("Could not perform variable substitution.", e);
            return value;
        }
    }

    public static void setParameter(Element elem, PropertySetter propSetter, Properties props) {
        propSetter.setProperty(subst(elem.getAttribute(NAME_ATTR), props), subst(OptionConverter.convertSpecialChars(elem.getAttribute(VALUE_ATTR)), props));
    }

    public static Object parseElement(Element element, Properties props, Class expectedClass) throws Exception {
        Object instance = OptionConverter.instantiateByClassName(subst(element.getAttribute(CLASS_ATTR), props), expectedClass, null);
        if (instance == null) {
            return null;
        }
        PropertySetter propSetter = new PropertySetter(instance);
        NodeList children = element.getChildNodes();
        int length = children.getLength();
        for (int loop = 0; loop < length; loop++) {
            Node currentNode = children.item(loop);
            if (currentNode.getNodeType() == (short) 1) {
                Element currentElement = (Element) currentNode;
                if (currentElement.getTagName().equals(PARAM_TAG)) {
                    setParameter(currentElement, propSetter, props);
                } else {
                    parseUnrecognizedElement(instance, currentElement, props);
                }
            }
        }
        return instance;
    }
}
