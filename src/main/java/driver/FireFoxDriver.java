package driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.File;

public class FireFoxDriver {
    public WebDriver driver = null;

    public FireFoxDriver(String proPath, String driverPath) {
        // 设置 Firefox驱动的路径
        System.setProperty("webdriver.gecko.driver", driverPath);
        // 设置Firefox的安装目录
        if (proPath != null && proPath.length() > 0) {
            System.setProperty("webdriver.firefox.bin", proPath);
        }
        //firefox属性，启动浏览器时，加载用户配置文件。
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        FirefoxProfile profile = new FirefoxProfile(new File("C:\\Users\\w\\AppData\\Roaming\\Mozilla\\Firefox\\Profiles\\ftijl6hu.default-release"));
        firefoxOptions.setProfile(profile);
        // 创建一个 Firefox的浏览器实例
        try {
            driver = new FirefoxDriver(firefoxOptions);
            AutoLogger.log.info("启动火狐浏览器");
        } catch (Exception e) {
            AutoLogger.log.info("创建driver失败，请检查配置");
            AutoLogger.log.error(e, e.fillInStackTrace());
        }

    }

    /**
     * 获取driver
     *
     * @return
     */
    public WebDriver getDriver() {
        return  this.driver;
    }
}

