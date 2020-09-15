package driver;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class AppDriverTest {
    private AndroidDriver driver;

    public AppDriverTest() {
        Properties prop = new Properties();
        try {
            //读取配置文件
            prop.load(this.getClass().getResourceAsStream("/app.properties"));
            //设备类型
            String platformName = prop.getProperty("platformName");
            //设备名称
            String deviceName = prop.getProperty("deviceName");
            //设备版本
            String platformVersion = prop.getProperty("platformVersion");
            //app包名，com.xxx.xxx
            String appPackage = prop.getProperty("appPackage");
            //app启动入口
            String appActivity = prop.getProperty("appActivity");
            //appium服务器地址
            String url = prop.getProperty("url");
            //设置APP启动参数
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("platformName", platformName);
            caps.setCapability("deviceName", deviceName);
            caps.setCapability("platformVersion", platformVersion);
            caps.setCapability("appPackage", appPackage);
            caps.setCapability("appActivity", appActivity);
            driver = new AndroidDriver(new URL(url), caps);
            Thread.sleep(5000);
            AutoLogger.log.info("App启动");
        } catch (Exception e) {
            AutoLogger.log.info("App启动失败，请检查");
            AutoLogger.log.info(e, e.fillInStackTrace());
        }
    }

    /**
     * 获取操作APP的driver
     *
     * @return driver
     */
    public AndroidDriver getDriver() {
        return this.driver;
    }
}
