package driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GoogleDriver {
    public WebDriver driver = null;

    public GoogleDriver(String driverPath) {
        // 设置 chrome 的路径
        System.setProperty("webdriver.chrome.driver", driverPath);
        //创建chrome浏览器的属性对象。
        ChromeOptions option = new ChromeOptions();
        //关闭保存账号密码弹窗
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        option.setExperimentalOption("prefs", prefs);
        // 去除Chrome浏览器上的黄色警告,注释的两种方法在新版浏览器上已失效
//        option.addArguments("--disable-infobars");
//        option.addArguments("--test-type");
        //新版本浏览器设置去除浏览器正在受自动化软件控制警告
        option.setExperimentalOption("useAutomationExtension",false);
        option.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        //加载chrome用户文件
        option.addArguments("--user-data-dir=C:\\Users\\w\\AppData\\Local\\Google\\Chrome\\User Data");
        //最大化浏览器窗口
        option.addArguments("--start-maximized");
        try { // 创建一个 Chrome 的浏览器实例
            this.driver = new ChromeDriver(option);
            // 打开空白页
            driver.get("about:blank");
        } catch (Exception e) {
            AutoLogger.log.info("driver创建失败！请检查配置！");
            e.printStackTrace();
        }

    }

    /**
     * 获取创建成功的driver
     * @return 返回操作driver
     */
    public WebDriver getDriver() {
        return this.driver;
    }
}
