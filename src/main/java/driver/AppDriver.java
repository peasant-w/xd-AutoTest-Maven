package driver;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class AppDriver {
    public AndroidDriver driver = null;

    /**
     *
     * @param deviceName 设备名称
     * @param platformName 设备类型
     * @param platformVersion 设备版本
     * @param appPackage 测试APP的报名
     * @param appActivity 测试APP的启动入口
     * @param appiumServiceIP appium服务器地址
     */
    public AppDriver(String deviceName, String platformName, String platformVersion, String appPackage,
                     String appActivity, String appiumServiceIP){
        DesiredCapabilities cap =  new DesiredCapabilities();
        cap.setCapability("deviceName",deviceName);
        cap.setCapability("platformName",platformName);
        cap.setCapability("platformVersion",platformVersion);
        cap.setCapability("appPackage",appPackage);
        cap.setCapability("appActivity", appActivity);
        cap.setCapability("noSign", "true");
        cap.setCapability("noReset", "true");
        cap.setCapability("unicodeKeyboard", "true");
        cap.setCapability("autoAcceptAlerts", "true");
        try {
            driver = new AndroidDriver(new URL(appiumServiceIP),cap);
            Thread.sleep(5000);
            AutoLogger.log.info("启动成功");
        } catch (Exception e) {
            AutoLogger.log.info("启动失败");
            AutoLogger.log.error(e,e.fillInStackTrace());
        }
    }

    /**
     * 获取driver
     * @return
     */
    public AndroidDriver getDriver(){
        return this.driver;
    }

}
