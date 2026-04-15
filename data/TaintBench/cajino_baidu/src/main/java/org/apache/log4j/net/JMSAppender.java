package org.apache.log4j.net;

import java.util.Properties;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

public class JMSAppender extends AppenderSkeleton {
    String initialContextFactoryName;
    boolean locationInfo;
    String password;
    String providerURL;
    String securityCredentials;
    String securityPrincipalName;
    String tcfBindingName;
    String topicBindingName;
    TopicConnection topicConnection;
    TopicPublisher topicPublisher;
    TopicSession topicSession;
    String urlPkgPrefixes;
    String userName;

    public void setTopicConnectionFactoryBindingName(String tcfBindingName) {
        this.tcfBindingName = tcfBindingName;
    }

    public String getTopicConnectionFactoryBindingName() {
        return this.tcfBindingName;
    }

    public void setTopicBindingName(String topicBindingName) {
        this.topicBindingName = topicBindingName;
    }

    public String getTopicBindingName() {
        return this.topicBindingName;
    }

    public boolean getLocationInfo() {
        return this.locationInfo;
    }

    public void activateOptions() {
        try {
            Context jndi;
            LogLog.debug("Getting initial context.");
            if (this.initialContextFactoryName != null) {
                Properties env = new Properties();
                env.put("java.naming.factory.initial", this.initialContextFactoryName);
                if (this.providerURL != null) {
                    env.put("java.naming.provider.url", this.providerURL);
                } else {
                    LogLog.warn("You have set InitialContextFactoryName option but not the ProviderURL. This is likely to cause problems.");
                }
                if (this.urlPkgPrefixes != null) {
                    env.put("java.naming.factory.url.pkgs", this.urlPkgPrefixes);
                }
                if (this.securityPrincipalName != null) {
                    env.put("java.naming.security.principal", this.securityPrincipalName);
                    if (this.securityCredentials != null) {
                        env.put("java.naming.security.credentials", this.securityCredentials);
                    } else {
                        LogLog.warn("You have set SecurityPrincipalName option but not the SecurityCredentials. This is likely to cause problems.");
                    }
                }
                jndi = new InitialContext(env);
            } else {
                jndi = new InitialContext();
            }
            LogLog.debug(new StringBuffer().append("Looking up [").append(this.tcfBindingName).append("]").toString());
            TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) lookup(jndi, this.tcfBindingName);
            LogLog.debug("About to create TopicConnection.");
            if (this.userName != null) {
                this.topicConnection = topicConnectionFactory.createTopicConnection(this.userName, this.password);
            } else {
                this.topicConnection = topicConnectionFactory.createTopicConnection();
            }
            LogLog.debug("Creating TopicSession, non-transactional, in AUTO_ACKNOWLEDGE mode.");
            this.topicSession = this.topicConnection.createTopicSession(false, 1);
            LogLog.debug(new StringBuffer().append("Looking up topic name [").append(this.topicBindingName).append("].").toString());
            Topic topic = (Topic) lookup(jndi, this.topicBindingName);
            LogLog.debug("Creating TopicPublisher.");
            this.topicPublisher = this.topicSession.createPublisher(topic);
            LogLog.debug("Starting TopicConnection.");
            this.topicConnection.start();
            jndi.close();
        } catch (JMSException e) {
            this.errorHandler.error(new StringBuffer().append("Error while activating options for appender named [").append(this.name).append("].").toString(), e, 0);
        } catch (NamingException e2) {
            this.errorHandler.error(new StringBuffer().append("Error while activating options for appender named [").append(this.name).append("].").toString(), e2, 0);
        } catch (RuntimeException e3) {
            this.errorHandler.error(new StringBuffer().append("Error while activating options for appender named [").append(this.name).append("].").toString(), e3, 0);
        }
    }

    /* access modifiers changed from: protected */
    public Object lookup(Context ctx, String name) throws NamingException {
        try {
            return ctx.lookup(name);
        } catch (NameNotFoundException e) {
            LogLog.error(new StringBuffer().append("Could not find name [").append(name).append("].").toString());
            throw e;
        }
    }

    /* access modifiers changed from: protected */
    public boolean checkEntryConditions() {
        String fail = null;
        if (this.topicConnection == null) {
            fail = "No TopicConnection";
        } else if (this.topicSession == null) {
            fail = "No TopicSession";
        } else if (this.topicPublisher == null) {
            fail = "No TopicPublisher";
        }
        if (fail == null) {
            return true;
        }
        this.errorHandler.error(new StringBuffer().append(fail).append(" for JMSAppender named [").append(this.name).append("].").toString());
        return false;
    }

    public synchronized void close() {
        if (!this.closed) {
            LogLog.debug(new StringBuffer().append("Closing appender [").append(this.name).append("].").toString());
            this.closed = true;
            try {
                if (this.topicSession != null) {
                    this.topicSession.close();
                }
                if (this.topicConnection != null) {
                    this.topicConnection.close();
                }
            } catch (JMSException e) {
                LogLog.error(new StringBuffer().append("Error while closing JMSAppender [").append(this.name).append("].").toString(), e);
            } catch (RuntimeException e2) {
                LogLog.error(new StringBuffer().append("Error while closing JMSAppender [").append(this.name).append("].").toString(), e2);
            }
            this.topicPublisher = null;
            this.topicSession = null;
            this.topicConnection = null;
        }
    }

    public void append(LoggingEvent event) {
        if (checkEntryConditions()) {
            try {
                ObjectMessage msg = this.topicSession.createObjectMessage();
                if (this.locationInfo) {
                    event.getLocationInformation();
                }
                msg.setObject(event);
                this.topicPublisher.publish(msg);
            } catch (JMSException e) {
                this.errorHandler.error(new StringBuffer().append("Could not publish message in JMSAppender [").append(this.name).append("].").toString(), e, 0);
            } catch (RuntimeException e2) {
                this.errorHandler.error(new StringBuffer().append("Could not publish message in JMSAppender [").append(this.name).append("].").toString(), e2, 0);
            }
        }
    }

    public String getInitialContextFactoryName() {
        return this.initialContextFactoryName;
    }

    public void setInitialContextFactoryName(String initialContextFactoryName) {
        this.initialContextFactoryName = initialContextFactoryName;
    }

    public String getProviderURL() {
        return this.providerURL;
    }

    public void setProviderURL(String providerURL) {
        this.providerURL = providerURL;
    }

    /* access modifiers changed from: 0000 */
    public String getURLPkgPrefixes() {
        return this.urlPkgPrefixes;
    }

    public void setURLPkgPrefixes(String urlPkgPrefixes) {
        this.urlPkgPrefixes = urlPkgPrefixes;
    }

    public String getSecurityCredentials() {
        return this.securityCredentials;
    }

    public void setSecurityCredentials(String securityCredentials) {
        this.securityCredentials = securityCredentials;
    }

    public String getSecurityPrincipalName() {
        return this.securityPrincipalName;
    }

    public void setSecurityPrincipalName(String securityPrincipalName) {
        this.securityPrincipalName = securityPrincipalName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLocationInfo(boolean locationInfo) {
        this.locationInfo = locationInfo;
    }

    /* access modifiers changed from: protected */
    public TopicConnection getTopicConnection() {
        return this.topicConnection;
    }

    /* access modifiers changed from: protected */
    public TopicSession getTopicSession() {
        return this.topicSession;
    }

    /* access modifiers changed from: protected */
    public TopicPublisher getTopicPublisher() {
        return this.topicPublisher;
    }

    public boolean requiresLayout() {
        return false;
    }
}
