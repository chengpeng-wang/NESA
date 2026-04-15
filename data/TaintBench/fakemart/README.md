# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: fakemart.apk
- **packageName**: com.android.blackmarket
- **targetSdk**: undefined
- **minSdk**: undefined
- **maxSdk**: undefined
- **mainActivity**: .BlackMarketAlpha
# Behavior Information:
## Activities:
- The malwere may perform the following actions:
	- Clear the XMBPSP.xml contents in shared preference
	- Configure the XMBPSP.xml file to send SMS to 81211 or 81308
	- Set the device to silent mode
	- Delete SMS received from 81211
	- Open network connections
	- Block incoming SMS, encode the body of the message, and post them to the above URLs
	- Send SMS to 81211 or 81308 if the first ten incoming SMS contain the strings "BD MULTIMEDIA" or "code"
## BroadcastReceivers:
- SmsReceiver monitors incoming SMS messages and sends them to remote server. 
# Detail Information:
## Activities: 1
	.BlackMarketAlpha
## Receivers: 1
	.SmsReceiver
## Permissions: 7
	android.permission.SEND_SMS
	android.permission.RECEIVE_SMS
	android.permission.WRITE_SMS
	android.permission.READ_SMS
	android.permission.INTERNET
	android.permission.ACCESS_NETWORK_STATE
	android.permission.READ_PHONE_STATE
## Sources: 87
	<android.content.ContentResolver: android.database.Cursor query(android.net.Uri,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String)>: 1
	<org.apache.james.mime4j.field.address.MailboxList: org.apache.james.mime4j.field.address.Mailbox get(int)>: 3
	<java.io.ByteArrayOutputStream: byte[] toByteArray()>: 5
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 2
	<java.util.TimeZone: java.util.TimeZone getDefault()>: 1
	<org.apache.james.mime4j.field.datetime.parser.DateTimeParser$Time: int getHour()>: 1
	<java.util.BitSet: boolean get(int)>: 9
	<org.apache.james.mime4j.field.datetime.parser.SimpleCharStream: int getBeginLine()>: 1
	<org.apache.james.mime4j.field.contenttype.parser.SimpleCharStream: int getEndLine()>: 2
	<java.text.DateFormat: java.lang.String format(java.util.Date)>: 2
	<org.apache.james.mime4j.field.datetime.parser.SimpleCharStream: int getEndColumn()>: 2
	<org.apache.james.mime4j.field.datetime.DateTime: java.util.Date getDate()>: 2
	<java.lang.Integer: int parseInt(java.lang.String,int)>: 1
	<org.apache.james.mime4j.field.address.parser.SimpleCharStream: int getEndColumn()>: 2
	<java.nio.CharBuffer: java.lang.String toString()>: 1
	<java.io.FileInputStream: void <init>: 3
	<org.apache.james.mime4j.field.DelegatingFieldParser: org.apache.james.mime4j.field.FieldParser getParser(java.lang.String)>: 1
	<java.lang.System: java.lang.String getProperty(java.lang.String,java.lang.String)>: 28
	<java.io.File: java.lang.String getName()>: 1
	<javax.crypto.KeyGenerator: javax.crypto.KeyGenerator getInstance(java.lang.String)>: 1
	<org.apache.james.mime4j.field.datetime.parser.SimpleCharStream: int getBeginColumn()>: 1
	<android.database.Cursor: java.lang.String getString(int)>: 1
	<org.apache.james.mime4j.field.contenttype.parser.ParseException: java.lang.String getMessage()>: 1
	<org.apache.james.mime4j.field.datetime.parser.DateTimeParser$Date: int getMonth()>: 1
	<org.apache.james.mime4j.field.datetime.parser.DateTimeParser$Time: int getZone()>: 1
	<org.apache.james.mime4j.field.address.Mailbox: java.lang.String getLocalPart()>: 1
	<org.apache.james.mime4j.field.datetime.parser.DateTimeParser$Time: int getSecond()>: 1
	<java.lang.ThreadLocal: java.lang.Object get()>: 1
	<org.apache.james.mime4j.field.address.DomainList: java.lang.String get(int)>: 1
	<org.apache.james.mime4j.field.address.parser.SimpleCharStream: int getBeginLine()>: 1
	<org.apache.james.mime4j.field.contenttype.parser.SimpleCharStream: int getBeginLine()>: 1
	<android.content.Intent: android.os.Bundle getExtras()>: 1
	<org.apache.james.mime4j.field.address.parser.AddressListParserTokenManager: org.apache.james.mime4j.field.address.parser.Token jjFillToken()>: 3
	<org.apache.james.mime4j.field.address.Builder: org.apache.james.mime4j.field.address.Builder getInstance()>: 3
	<java.net.URLConnection: java.io.InputStream getInputStream()>: 1
	<org.apache.james.mime4j.field.datetime.parser.DateTimeParserTokenManager: org.apache.james.mime4j.field.datetime.parser.Token jjFillToken()>: 3
	<org.apache.james.mime4j.field.address.parser.AddressListParser: org.apache.james.mime4j.field.address.parser.Token getToken(int)>: 2
	<java.nio.ByteBuffer: java.nio.ByteBuffer get(byte[])>: 1
	<java.util.Calendar: int get(int)>: 2
	<java.lang.System: java.lang.String getProperty(java.lang.String)>: 1
	<java.io.BufferedReader: java.lang.String readLine()>: 1
	<java.lang.String: byte[] getBytes(java.lang.String)>: 4
	<org.apache.http.util.ByteArrayBuffer: byte[] toByteArray()>: 1
	<org.apache.james.mime4j.field.contenttype.parser.SimpleCharStream: int getBeginColumn()>: 1
	<java.nio.charset.Charset: java.nio.CharBuffer decode(java.nio.ByteBuffer)>: 1
	<java.util.LinkedList: java.lang.Object getLast()>: 1
	<java.nio.charset.Charset: java.lang.String name()>: 9
	<java.util.TimeZone: java.util.TimeZone getTimeZone(java.lang.String)>: 1
	<org.apache.james.mime4j.field.datetime.parser.DateTimeParser$Date: int getDay()>: 1
	<java.lang.Class: java.lang.String getName()>: 7
	<org.apache.james.mime4j.field.datetime.parser.DateTimeParserTokenManager: org.apache.james.mime4j.field.datetime.parser.Token getNextToken()>: 4
	<org.apache.commons.logging.LogFactory: org.apache.commons.logging.Log getLog(java.lang.Class)>: 15
	<org.apache.james.mime4j.field.contenttype.parser.ContentTypeParserTokenManager: org.apache.james.mime4j.field.contenttype.parser.Token jjFillToken()>: 3
	<android.content.Intent: java.lang.String getAction()>: 1
	<java.util.Calendar: java.util.Date getTime()>: 2
	<org.apache.james.mime4j.field.contenttype.parser.SimpleCharStream: int getEndColumn()>: 2
	<org.apache.james.mime4j.field.address.parser.SimpleCharStream: int getBeginColumn()>: 1
	<org.apache.james.mime4j.field.contenttype.parser.TokenMgrError: java.lang.String getMessage()>: 2
	<org.apache.james.mime4j.field.contenttype.parser.ContentTypeParserTokenManager: org.apache.james.mime4j.field.contenttype.parser.Token getNextToken()>: 4
	<android.telephony.gsm.SmsMessage: android.telephony.gsm.SmsMessage createFromPdu(byte[])>: 1
	<android.database.Cursor: long getLong(int)>: 1
	<android.telephony.gsm.SmsManager: android.telephony.gsm.SmsManager getDefault()>: 1
	<java.lang.Throwable: java.lang.String getMessage()>: 7
	<org.apache.james.mime4j.field.address.Mailbox: org.apache.james.mime4j.field.address.DomainList getRoute()>: 1
	<org.apache.james.mime4j.field.contenttype.parser.ContentTypeParser: java.lang.String getSubType()>: 1
	<javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>: 2
	<android.content.Context: java.lang.Object getSystemService(java.lang.String)>: 1
	<java.io.File: boolean delete()>: 1
	<org.apache.james.mime4j.field.datetime.parser.ParseException: java.lang.String getMessage()>: 1
	<org.apache.james.mime4j.field.address.Mailbox: java.lang.String getDomain()>: 1
	<org.apache.james.mime4j.field.address.AddressList: org.apache.james.mime4j.field.address.Address get(int)>: 1
	<org.apache.james.mime4j.field.datetime.parser.DateTimeParser$Date: java.lang.String getYear()>: 1
	<java.io.File: void <init>: 2
	<org.apache.james.mime4j.field.address.parser.ParseException: java.lang.String getMessage()>: 3
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 1
	<org.apache.james.mime4j.field.address.parser.TokenMgrError: java.lang.String getMessage()>: 3
	<java.lang.Long: long parseLong(java.lang.String)>: 4
	<org.apache.james.mime4j.field.datetime.parser.DateTimeParser$Time: int getMinute()>: 1
	<org.apache.james.mime4j.field.datetime.parser.TokenMgrError: java.lang.String getMessage()>: 3
	<org.apache.james.mime4j.field.datetime.parser.SimpleCharStream: int getEndLine()>: 2
	<android.os.Bundle: java.lang.Object get(java.lang.String)>: 1
	<java.lang.Integer: int parseInt(java.lang.String)>: 3
	<org.apache.james.mime4j.field.DateTimeField: java.util.Date getDate()>: 2
	<org.apache.james.mime4j.field.contenttype.parser.ContentTypeParser: java.lang.String getType()>: 3
	<org.apache.james.mime4j.field.address.parser.AddressListParserTokenManager: org.apache.james.mime4j.field.address.parser.Token getNextToken()>: 5
	<android.telephony.gsm.SmsMessage: java.lang.String getMessageBody()>: 1
	<org.apache.james.mime4j.field.address.parser.SimpleCharStream: int getEndLine()>: 2
## Sinks: 36
	<java.util.Calendar: void set(int,int,int,int,int,int)>: 1
	<java.lang.String: boolean startsWith(java.lang.String)>: 7
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 2
	<java.io.FileOutputStream: void write(byte[],int,int)>: 1
	<java.net.URLConnection: void setConnectTimeout(int)>: 1
	<java.lang.String: java.lang.String substring(int,int)>: 34
	<java.io.OutputStreamWriter: void <init>: 1
	<java.net.URLConnection: void setAllowUserInteraction(boolean)>: 1
	<android.util.Log: int d(java.lang.String,java.lang.String)>: 1
	<android.media.AudioManager: void setRingerMode(int)>: 1
	<java.lang.Integer: int parseInt(java.lang.String,int)>: 1
	<android.widget.Toast: android.widget.Toast makeText(android.content.Context,java.lang.CharSequence,int)>: 7
	<java.net.URLConnection: void setDoOutput(boolean)>: 1
	<java.net.HttpURLConnection: void connect()>: 1
	<java.net.URLConnection: void setReadTimeout(int)>: 1
	<java.io.OutputStream: void write(byte[],int,int)>: 9
	<java.io.File: boolean delete()>: 1
	<java.lang.StringBuilder: void setLength(int)>: 4
	<java.io.PrintStream: void print(java.lang.String)>: 1
	<java.util.BitSet: void set(int)>: 3
	<java.net.URL: void <init>: 2
	<java.net.URL: java.net.URLConnection openConnection()>: 2
	<java.io.OutputStream: void write(byte[])>: 12
	<android.telephony.gsm.SmsManager: void sendTextMessage(java.lang.String,java.lang.String,java.lang.String,android.app.PendingIntent,android.app.PendingIntent)>: 1
	<java.io.FileOutputStream: void <init>: 2
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 1
	<java.lang.Long: long parseLong(java.lang.String)>: 4
	<org.apache.http.client.HttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 1
	<java.util.Calendar: void set(int,int)>: 1
	<java.lang.Integer: int parseInt(java.lang.String)>: 3
	<android.app.Activity: void onCreate(android.os.Bundle)>: 1
	<android.content.SharedPreferences$Editor: boolean commit()>: 4
	<java.net.URLConnection: java.io.InputStream getInputStream()>: 1
	<java.text.DateFormat: void setTimeZone(java.util.TimeZone)>: 3
	<java.net.HttpURLConnection: void setRequestMethod(java.lang.String)>: 1
	<java.lang.Class: java.lang.Class forName(java.lang.String)>: 1
