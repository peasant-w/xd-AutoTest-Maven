package driver;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;


import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpClientDriver {
    //cookie连接池
    private CookieStore cookieStore;
    //标记是否使用cookies
    private boolean useCookie = true;
    //用于保存请求头
    private Map<String, String> headerMap;
    private boolean useHeader = true;
    //用于匹配Unicode编码
    private static final Pattern UNIPATTERN = Pattern.compile("\\\\u([0-9a-zA-Z]{4})");

    public HttpClientDriver() {
        cookieStore = new BasicCookieStore();
        headerMap = new HashMap<>();
    }

    /**
     * 绕过ssl验证，使能够对HTTPS发包请求
     *
     * @return
     */
    public static SSLContext createSSl() {
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSLv3");
            sc.init(null, new TrustManager[]{trustManager}, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sc;
    }

    /**
     * 创建httpclient
     *
     * @return 返回httpclient对象，用于发送请求
     */
    public CloseableHttpClient createHttpClient() {
        SSLContext sslContext = createSSl();
        Registry<ConnectionSocketFactory> socketRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslContext)).build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketRegistry);
        //如果要使用cookiestore，就在创建时带上setdefaultcookieStore方法。
        CloseableHttpClient client;
        // 创建自定义的httpclient对象，使用cookie or 不使用cookie
        if (useCookie) {
            client = HttpClients.custom().setConnectionManager(connManager).setDefaultCookieStore(cookieStore).build();
        } else {
            client = HttpClients.custom().setConnectionManager(connManager).build();
        }
        //启用代理，需要抓包的时候使用，不不使用时记得注释掉
//        HttpHost proxy = new HttpHost("localhost",8888,"http");
//        client = HttpClients.custom().setProxy(proxy).setConnectionManager(connManager).build();
        return client;
    }

    /**
     * 查找字符串中的Unicode编码，并转换成中文
     *
     * @param unicode 需要转码的内容
     * @return 返回转码后的内容
     */
    public static String transCode(String unicode) {
        try {
            Matcher matcher = UNIPATTERN.matcher(unicode);
            StringBuffer sb = new StringBuffer(unicode.length());
            while (matcher.find()) {
                matcher.appendReplacement(sb, Character.toString((char) Integer.parseInt(matcher.group(1), 16)));
            }
            matcher.appendTail(sb);
            return sb.toString();
        } catch (NumberFormatException e) {
            AutoLogger.log.error("转换失败");
            AutoLogger.log.error(e, e.fillInStackTrace());
            return unicode;
        }
    }

    /**
     * get请求发包
     *
     * @param url   请求地址
     * @param param 请求参数
     * @return 返回请求结果response
     */
    public String doGet(String url, String param) {
        CloseableHttpClient client = createHttpClient();
        String result = null;
        try {
            String getUrl = null;
            if (param.length() > 0) {
                getUrl = url + "?" + param;
            } else {
                getUrl = url;
            }
            HttpGet get = new HttpGet(getUrl);
            //设置超时时间，setsocketTImeout指定收发包过程中的超时上线是15秒，connectTime指定和服务器建立连接，还没有发包时的超时上限为10秒
            RequestConfig config = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(10000).build();
            get.setConfig(config);
            AutoLogger.log.info("开始请求get：" + getUrl);
            CloseableHttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
            }
            //处理返回内容中可能出现的Unicode编码
            result = transCode(result);
            //关闭资源
            EntityUtils.consume(entity);
            response.close();
            client.close();
        } catch (Exception e) {
            result = e.fillInStackTrace().toString();
            AutoLogger.log.error("get请求失败，请检查");
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
        return result;
    }

    /**
     * 指定请求体格式post请求发包
     *
     * @param url         请求地址
     * @param param       参数
     * @param contentType 请求体格式，如（application/x-www-form-urlencoded、application/json、text/xml等），以下使用简写xml/url/json
     * @return 返回请求结果result
     */
    public String doPost(String url, String param, String contentType) {
        CloseableHttpClient client = createHttpClient();
        //保存请求最终返回结果
        String result = null;
        try {
            HttpPost post = new HttpPost(url);
            //设置超时时间
            RequestConfig config = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(10000).build();
            post.setConfig(config);
            //使用头域，从headerMap中遍历，添加到post请求
            if (useHeader) {
                for (String headerKey : headerMap.keySet()) {
                    post.setHeader(headerKey, headerMap.get(headerKey));
                }
            }
            switch (contentType) {
                case "url":
                    // 创建urlencoded格式的请求实体，设置编码为utf8
                    StringEntity urlParams = new StringEntity(param);
                    urlParams.setContentType("application/x-www-form-urlencoded");
                    urlParams.setContentEncoding("UTF-8");
                    // 添加请求体到post请求中
                    post.setEntity(urlParams);
                    break;
                case "json":
                    // 创建json格式的请求实体，设置编码为utf8
                    StringEntity jsonParams = new StringEntity(param);
                    jsonParams.setContentType("application/json");
                    jsonParams.setContentEncoding("UTF-8");
                    // 添加请求体到post请求中
                    post.setEntity(jsonParams);
                    break;
                case "xml":
                    // 创建xml格式的请求实体，设置编码为utf8
                    StringEntity xmlParams = new StringEntity(param);
                    xmlParams.setContentType("text/xml");
                    xmlParams.setContentEncoding("UTF-8");
                    // 添加请求体到post请求中
                    post.setEntity(xmlParams);
                    break;
            }
            //执行请求操作，获取返回包
            AutoLogger.log.info("开始请求：" + url);
            CloseableHttpResponse response = client.execute(post);
            //获取返回体内容
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
            }
            result = transCode(result);
            //释放返回实体
            EntityUtils.consume(entity);
            //关闭返回包
            response.close();
        } catch (Exception e) {
            AutoLogger.log.error("请求失败，请检查");
            AutoLogger.log.error(e, e.fillInStackTrace());
            result = e.fillInStackTrace().toString();
        } finally {
            try {
                client.close();
                AutoLogger.log.info("httpclient客户端关闭");
            } catch (IOException e) {
                AutoLogger.log.error("httpclient关闭失败，请检查");
                AutoLogger.log.error(e, e.fillInStackTrace());
            }
        }
        return result;
    }

    /**
     * 设置使用cookie
     */
    public void useCookie() {
        useCookie = true;
    }

    /**
     * 设置不适用cookie
     */
    public void notUseCookie() {
        useCookie = false;
    }

    /**
     * 设置使用头域
     */
    public void useHeader() {
        useHeader = true;
    }

    /**
     * 设置不适用头域
     */
    public void notUseHeader() {
        useHeader = false;
    }

    /**
     * 添加头域到headerMap中进行管理
     *
     * @param key   头域名
     * @param value 头域值
     */
    public void addHeader(String key, String value) {
        headerMap.put(key, value);
    }
}
