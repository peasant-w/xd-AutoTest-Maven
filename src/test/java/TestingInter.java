import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;



public class TestingInter {
    public static void main(String[] args) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = "http://www.testingedu.com.cn:8081";
        //授权接口
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>登录接口授权>>>>>>>>>>>>>>>>>>>>>>>>>>");
        String authAdr = "/inter/HTTP/auth";
        HttpPost loginAuth = new HttpPost(url+authAdr);
        loginAuth.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36");
        loginAuth.setHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        CloseableHttpResponse authResponse =  httpClient.execute(loginAuth);
        String authResult =  EntityUtils.toString(authResponse.getEntity());
        System.out.println("授权结果："+authResult);
        JSONObject authResJson = JSONObject.parseObject(authResult);
        String loginToken = authResJson.getString("token");
        //登录接口
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>登录接口>>>>>>>>>>>>>>>>>>>>>>>>>>");
        String loginAdr = "/inter/HTTP/login";
        HttpPost loginPost = new HttpPost(url+loginAdr);
        loginPost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36");
        loginPost.setHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        loginPost.setHeader("token",loginToken);
        StringEntity loginEntity = new StringEntity("username=Will&password=123456");
        loginPost.setEntity(loginEntity);
        CloseableHttpResponse loginResponse = httpClient.execute(loginPost);
        String loginResult = EntityUtils.toString(loginResponse.getEntity());
        System.out.println(loginResult);
        //用户信息查询授权
        //授权接口
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>用户信息接口授权>>>>>>>>>>>>>>>>>>>>>>>>>>");
        String getAdr = "/inter/HTTP/auth";
        JSONObject loginResJson = JSONObject.parseObject(loginResult);
        String userid = loginResJson.getString("userid");
        HttpPost getUserAuth = new HttpPost(url+authAdr);
        getUserAuth.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36");
        getUserAuth.setHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        CloseableHttpResponse getUserAuthResponse =  httpClient.execute(getUserAuth);
        String authGetUserResult =  EntityUtils.toString(getUserAuthResponse.getEntity());
        System.out.println("授权结果："+authGetUserResult);
        //用户信息接口
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>用户信息接口>>>>>>>>>>>>>>>>>>>>>>>>>>");
        JSONObject getUserJson = JSONObject.parseObject(authGetUserResult);
//        String getUserToken = getUserJson.getString("token");
        String getUserInfoAdr = "/inter/HTTP/getUserInfo";
        HttpPost getUserPost = new HttpPost(url+getUserInfoAdr);
        getUserPost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36");
        getUserPost.setHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        getUserPost.setHeader("token",loginToken);
        StringEntity idEntity = new StringEntity("id="+userid);
        getUserPost.setEntity(idEntity);
        CloseableHttpResponse getUserResponse =  httpClient.execute(getUserPost);
        String getUserRes = EntityUtils.toString(getUserResponse.getEntity());
        System.out.println("用户信息："+getUserRes);

    }
}
