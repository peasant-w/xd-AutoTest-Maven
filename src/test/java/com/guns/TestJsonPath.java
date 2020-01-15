package com.guns;

import com.jayway.jsonpath.JsonPath;
import driver.AutoLogger;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TestJsonPath {
    public static Logger log = Logger.getLogger(TestJsonPath.class);
    public static void main(String[] args){
        String json = "{\"batNo\":\"10000126020272\",\"brNo\":\"0008\",\"dataCnt\":\"1000\",\"list\":[{\"appArea\":\"440100\",\"appUse\":\"01\",\"birth\":\"19810101\",\"cardAmt\":20000,\"children\":\"2\",\"custName\":\"肖国钱\",\"custType\":\"02\",\"degree\":\"4\",\"edu\":\"20\",\"homeArea\":\"422800\",\"homeSts\":\"1\",\"idNo\":\"420101198101014935\",\"idType\":\"0\",\"ifApp\":\"1\",\"ifCar\":\"1\",\"ifCarCred\":\"1\",\"ifCard\":\"1\",\"ifId\":\"1\",\"ifMort\":\"1\",\"ifPact\":\"1\",\"ifRoom\":\"1\",\"income\":\"50000\",\"listAc\":[{\"acAmt\":60000,\"acName\":\"肖鹏\",\"acType\":\"11\",\"acUse\":\"2\",\"acno\":\"6212263602020339834\",\"bankCity\":\"\",\"bankCode\":\"002\",\"bankProv\":\"\",\"bankSite\":\"\",\"idNo\":\"44150219950212303X\",\"idType\":\"0\",\"phoneNo\":\"13602849443\"},{\"acAmt\":0,\"acName\":\"肖国钱\",\"acType\":\"11\",\"acUse\":\"1\",\"acno\":\"6222021664101798846\",\"bankCity\":\"\",\"bankCode\":\"002\",\"bankProv\":\"\",\"bankSite\":\"\",\"idNo\":\"420101198101014935\",\"idType\":\"0\",\"phoneNo\":\"18664671793\"}],\"listCom\":[],\"listGage\":[{\"gBegBal\":177891,\"gLicType\":\"01\",\"gLicno\":\"粤房地权证珠字第0300040953号\",\"gName\":\"普通住宅\",\"gSmType\":\"01\",\"gType\":\"213\",\"gValue\":720000,\"gWorkType\":\"0\",\"gageAddress\":\"珠海市斗门区乾务镇盛兴三路8号2栋3单元604房\",\"gageCity\":\"珠海市\",\"gageSeq\":\"02\",\"gcustAge\":38,\"gcustIdno\":\"420101198101014935\",\"gcustIdtype\":\"0\",\"gcustName\":\"肖国钱\",\"houseAge\":5}],\"listRel\":[],\"lnRate\":\"1.400\",\"marriage\":\"21\",\"pactAmt\":\"60000.0\",\"pactNo\":\"1944\",\"payDay\":\"\",\"payType\":\"01\",\"phoneNo\":\"18664671793\",\"prdtNo\":\"8800\",\"prePactNo\":\"10000126020022\",\"projNo\":\"00080008\",\"sex\":\"1\",\"telNo\":\"18664671793\",\"termDay\":\"0\",\"termMon\":\"12\",\"vouAmt\":\"0\",\"vouType\":\"2\"}]}";
        JSONObject jsonString = new JSONObject(json);
//        String a = jsonString.get("list").toString();
//        AutoLogger.log.info(jsonString.get("list").toString());
//        System.out.println(a);
        log.info("解析后："+jsonString);
        String list = JsonPath.read(jsonString,"$.batNo");
        System.out.print(list);
//        Map<String,String> jsonMap = new HashMap<>();
//        Map<String,String> testMap = new HashMap<>();
//        Iterator<String> jsonIt = jsonString.keys();
//        while(jsonIt.hasNext()){
//            String jsonKey = jsonIt.next();
//            jsonMap.put(jsonKey,jsonString.get(jsonKey).toString());
//        }
//        log.info("jsonMap:"+jsonMap);
////        String i = JsonPath.read(jsonString,"$.list");
//        log.info("在字典里取值："+jsonMap.get("list"));
//        testMap.put("test","123456");
//        testMap.put("name","liao");
//        log.info("测试："+testMap);

    }
}
