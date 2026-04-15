package com.baidu.inf.iis.bcs.policy;

import com.baidu.inf.iis.bcs.model.BCSClientException;
import com.baidu.inf.iis.bcs.model.Pair;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Policy {
    private static final Log log = LogFactory.getLog(Policy.class);
    private String originalJsonStr;
    List<Statement> statements = new ArrayList();

    public static void main(String[] strArr) {
        Policy policy = new Policy();
        Statement statement = new Statement();
        statement.addAction(PolicyAction.get_bucket_policy).addAction(PolicyAction.put_bucket_policy).addAction(PolicyAction.all);
        statement.addResource("/1").addResource("/2").addResource("/3");
        statement.addUser("user1").addUser("user2");
        statement.setEffect(PolicyEffect.allow);
        PolicyTime policyTime = new PolicyTime();
        policyTime.addSingleTime("2012-01-04D12:12:1").addSingleTime("2012-01-04D12:12:2").addTimeRange(new Pair("2012-01-04D12:12:3", "2012-01-04D12:12:4"));
        statement.setTime(policyTime);
        PolicyIP policyIP = new PolicyIP();
        policyIP.addSingleIp("1.1.1.1").addCidrIp("2.2.2.2/16").addIpRange(new Pair("3.3.3.3", "4.4.4.4"));
        statement.setIp(policyIP);
        Statement statement2 = new Statement();
        statement2.addAction(PolicyAction.get_bucket_policy).addAction(PolicyAction.put_bucket_policy);
        statement2.addResource("/1").addResource("/2").addResource("/3");
        statement2.addUser("user1").addUser("user2");
        statement2.setEffect(PolicyEffect.deny);
        policy.addStatements(statement);
        policy.addStatements(statement2);
        String toJson = policy.toJson();
        log.info("Policy object to json str:\n" + toJson);
        String toJson2 = new Policy().buildJsonStr(toJson).toJson();
        log.info("Json str 2 policy object 2 json str:\n" + toJson2);
        if (toJson.equals(toJson2)) {
            log.info("Correct");
        } else {
            log.info("Invalid");
        }
    }

    public Policy addStatements(Statement statement) {
        this.statements.add(statement);
        return this;
    }

    public Policy buildJsonStr(String str) {
        this.originalJsonStr = str;
        Iterator it = ((ArrayList) ((HashMap) new JSONDeserializer().deserialize(str)).get("statements")).iterator();
        while (it.hasNext()) {
            Object next;
            HashMap hashMap = (HashMap) it.next();
            Statement statement = new Statement();
            Iterator it2 = ((ArrayList) hashMap.get("action")).iterator();
            while (it2.hasNext()) {
                statement.addAction(PolicyAction.toPolicyAction((String) it2.next()));
            }
            it2 = ((ArrayList) hashMap.get("user")).iterator();
            while (it2.hasNext()) {
                statement.addUser((String) it2.next());
            }
            it2 = ((ArrayList) hashMap.get("resource")).iterator();
            while (it2.hasNext()) {
                statement.addResource((String) it2.next());
            }
            statement.setEffect(PolicyEffect.valueOf((String) hashMap.get("effect")));
            if (hashMap.get("time") != null) {
                PolicyTime policyTime = new PolicyTime();
                Iterator it3 = ((ArrayList) hashMap.get("time")).iterator();
                while (it3.hasNext()) {
                    next = it3.next();
                    if (next instanceof String) {
                        policyTime.addSingleTime((String) next);
                    } else if (next instanceof List) {
                        policyTime.addTimeRange(new Pair((String) ((List) next).get(0), (String) ((List) next).get(1)));
                    } else {
                        throw new BCSClientException("Analyze policy time failed.");
                    }
                }
                statement.setTime(policyTime);
            }
            if (hashMap.get("ip") != null) {
                PolicyIP policyIP = new PolicyIP();
                Iterator it4 = ((ArrayList) hashMap.get("ip")).iterator();
                while (it4.hasNext()) {
                    next = it4.next();
                    if (next instanceof String) {
                        if (((String) next).indexOf("/") != -1) {
                            policyIP.addCidrIp((String) next);
                        } else {
                            policyIP.addSingleIp((String) next);
                        }
                    } else if (next instanceof List) {
                        policyIP.addIpRange(new Pair((String) ((List) next).get(0), (String) ((List) next).get(1)));
                    } else {
                        throw new BCSClientException("Analyze policy time failed.");
                    }
                }
                statement.setIp(policyIP);
            }
            addStatements(statement);
        }
        return this;
    }

    public String getOriginalJsonStr() {
        return this.originalJsonStr;
    }

    public List<Statement> getStatements() {
        return this.statements;
    }

    public void setStatements(List<Statement> list) {
        this.statements = list;
    }

    public String toJson() {
        if (this.statements.size() == 0) {
            return "";
        }
        JSONSerializer jSONSerializer = new JSONSerializer();
        HashMap hashMap = new HashMap();
        hashMap.put("statements", new ArrayList());
        for (Statement statement : this.statements) {
            HashMap hashMap2 = new HashMap();
            hashMap2.put("user", statement.getUser());
            hashMap2.put("resource", statement.getResource());
            ArrayList arrayList = new ArrayList();
            for (PolicyAction policyAction : statement.getAction()) {
                arrayList.add(policyAction.toString());
            }
            hashMap2.put("action", arrayList);
            hashMap2.put("effect", statement.getEffect().toString());
            if (!(statement.getTime() == null || statement.getTime().isEmpty())) {
                arrayList = new ArrayList();
                arrayList.addAll(statement.getTime().getSingleTimeList());
                for (Pair toArrayList : statement.getTime().getTimeRangeList()) {
                    arrayList.add(toArrayList.toArrayList());
                }
                hashMap2.put("time", arrayList);
            }
            if (!(statement.getIp() == null || statement.getIp().isEmpty())) {
                ArrayList arrayList2 = new ArrayList();
                arrayList2.addAll(statement.getIp().getSingleIpList());
                arrayList2.addAll(statement.getIp().getCidrIpList());
                for (Pair toArrayList2 : statement.getIp().getIpRangeList()) {
                    arrayList2.add(toArrayList2.toArrayList());
                }
                hashMap2.put("ip", arrayList2);
            }
            ((ArrayList) hashMap.get("statements")).add(hashMap2);
        }
        return jSONSerializer.deepSerialize(hashMap);
    }
}
