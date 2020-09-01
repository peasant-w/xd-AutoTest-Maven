package driver;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class IeDriver { // IE浏览器驱动类
    public WebDriver driver = null;

    //webdriver连接启动浏览器时，启动的服务。
    public InternetExplorerDriverService service = null;

    public IeDriver(String driverPath) {
        // 设置 IE 的路径
        System.setProperty("webdriver.ie.driver", driverPath);
        //创建ie的配置参数对象添加相关设置
        DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
        //设置忽略区域安全级别校验
        ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        //设置忽略缩放大小校验
        ieCapabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
        //通过Options选项加载配置参数，下面两种设置方式与设置capabilities方式等价。
        InternetExplorerOptions ieOptions = new InternetExplorerOptions(ieCapabilities);
//		ieOptions.introduceFlakinessByIgnoringSecurityDomains();
//		ieOptions.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        // 创建一个 IEDriver 的服务，用于连接 IE
        try {
            //使用指定的iedriver文件以及任意空闲端口完成服务的启动
            service = new InternetExplorerDriverService.Builder().usingDriverExecutable(new File(driverPath))
                    .usingAnyFreePort().build();
            service.start();
            AutoLogger.log.info("启动IE服务");
        } catch (IOException e) {
            e.printStackTrace();
            AutoLogger.log.info("启动IE服务失败，检查配置");
        }
        try {
            // 基于options选项与driver服务 创建一个 IE 的浏览器webDriver实例，完成浏览器启动。
            this.driver = new InternetExplorerDriver(service, ieOptions);
            //浏览器窗口最大化
            driver.manage().window().maximize();
            // 默认打开空白页面
            driver.get("about:blank");
            AutoLogger.log.info("启动IE浏览器");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("启动IE浏览器失败，检查配置");
        }
    }

    //将在构造函数中实例化的成员变量driver对象通过该方法返回
    public WebDriver getdriver() {
        return this.driver;
    }

    //由于手动启动了driverServer服务，因此关闭时除了调用quit，也一并将service服务关闭
    public void closeIE() {
        driver.quit();
        service.stop();
    }
}