import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class IpTest {
    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = "https://dev-xd.hfmoney.com/gzhf/login.ht";
//        Map<String,Object> map = new HashMap<>();
//        map.put("Content-Type","application/x-www-form-urlencoded");
//        map.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36");
//        map.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        HttpPost ipPost = new HttpPost(url);
        ipPost.setHeader("Content-Type","application/x-www-form-urlencoded");
        ipPost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36");
        ipPost.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        StringEntity entity = new StringEntity("username=admin&password=1&validCode=");
        ipPost.setEntity(entity);
        CloseableHttpResponse ipResponse = httpClient.execute(ipPost);
        String resEntity = EntityUtils.toString(ipResponse.getEntity());
        System.out.println("响应状态码为"+ipResponse.getStatusLine());
        System.out.println("------"+resEntity);
        System.out.println("-----------------------------------------------------------");
        String mainUrl = "https://dev-xd.hfmoney.com/gzhf/platform/console/main.ht";
        HttpGet mainGet = new HttpGet(mainUrl);
        mainGet.setHeader("Content-Type","application/x-www-form-urlencoded");
        mainGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36");
        mainGet.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        CloseableHttpResponse mainResponse = httpClient.execute(mainGet);
        System.out.println("============="+mainResponse);
        String mianEntity = EntityUtils.toString(mainResponse.getEntity());
        System.out.println(">>>>>>>>"+mianEntity);


        System.out.println("-------------------------------------------------------------------------------------");



        }
    }

