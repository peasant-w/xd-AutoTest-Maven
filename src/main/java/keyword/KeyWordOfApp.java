package keyword;

import driver.AppDriver;
import driver.AutoLogger;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * keyword类，用于操作APP的关键字
 *
 * @author wangwei
 * @date 2019-10-13
 */
public class KeyWordOfApp {
    /**
     * dateFormat 统一时间格式
     */
    private String dateFormat = "yyyyMMddHHmmss";
    /**
     * driver 公共driver，后续操作APP使用
     */
    public AndroidDriver driver = null;

    public KeyWordOfApp() {
    }

    /**
     * 强制等待，传递等待秒数
     *
     * @param waitTime 等待时间/秒
     */
    public void threadWait(String waitTime) {
        int time;
        time = Integer.parseInt(waitTime);
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            AutoLogger.log.info(e, e.fillInStackTrace());
        }
    }

    /**
     * 隐式等待，默认等待时长10s
     *
     * @param xpath 元素表达式
     */
    private void implicityWait(String xpath) {
        try {
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            AutoLogger.log.info(e, e.fillInStackTrace());
        }
    }

    /**
     * 显式等待
     *
     * @param xpath 元素表达式
     */
    private void explicityWait(final String xpath) {
        WebDriverWait eWait = new WebDriverWait(driver, 10);
        eWait.until(new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver d) {
                return d.findElement(By.xpath(xpath));
            }
        });
    }

    /**
     * 执行cmd命令
     *
     * @param command cmd命令
     */
    private void runCmd(String command) {
        Runtime runtime = Runtime.getRuntime();
        String cmd = "cmd /c start " + command;
        try {
            runtime.exec(cmd);
            AutoLogger.log.info("cmd命令执行完成");
        } catch (IOException e) {
            AutoLogger.log.info("cmd命令执行异常，请检查");
            AutoLogger.log.info(e, e.fillInStackTrace());
        }
    }

    /**
     * 启动appium服务
     *
     * @param serviceIp appium服务地址
     * @param port      端口
     * @param time      等待时间
     */
    public void startAppium(String serviceIp, String port, String time) {
        AutoLogger.log.info("appium服务启动中···");
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            String createDate = sdf.format(date);
            String appiumlogFile = "product/logs/" + createDate + "appiumlog.txt";
            String startappiumCmd = "appium -a " + serviceIp + " -p " + port + " --log " + appiumlogFile + " --local-timezone";
            runCmd(startappiumCmd);
            threadWait(time);
            AutoLogger.log.info("appium服务启动完成");
        } catch (Exception e) {
            AutoLogger.log.info("appium服务启动异常，请检查");
            AutoLogger.log.info(e, e.fillInStackTrace());
        }
    }

    /**
     * 关闭appium服务，使用cmd命令结束node进程的方法
     */
    public void closeAppium() {
        try {
            runCmd("taskkill /F /IM node.exe");
            this.threadWait("5");
            AutoLogger.log.info("Appium服务已关闭");
        } catch (Exception e) {
            AutoLogger.log.info("Appium服务关闭异常，请检查");
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
    }

    /**
     * 启动APP，传递设备标识、设备名称、设备版本、APP包名、APP启动入口、appium服务地址
     *
     * @param deviceName      设备标识
     * @param platfromName    设备名称 如：安卓、iOS
     * @param platfromVersion 设备系统版本
     * @param appPackage      APP的包名，如：com.wanjia.lend
     * @param appActivity     APP的启动入口，Activity
     * @param appiumServiceIp appium服务地址
     */
    public void startApp(String deviceName, String platfromName, String platfromVersion, String appPackage, String appActivity, String appiumServiceIp) {
        try {
            AutoLogger.log.info("启动APP");
            AppDriver app = new AppDriver(deviceName, platfromName, platfromVersion, appPackage, appActivity, appiumServiceIp);
            driver = app.getDriver();
        } catch (Exception e) {
            AutoLogger.log.info(e, e.fillInStackTrace());
        }
    }

    /**
     * 退出APP
     */
    public void closeApp() {
        try {
            driver.close();
            this.threadWait("1");
            AutoLogger.log.info("APP已关闭");
        } catch (Exception e) {
            AutoLogger.log.info("APP关闭异常，请检查");
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
    }

    /**
     * 点击元素
     *
     * @param xpath 元素表达式
     */
    public void click(String xpath) {
        try {
            explicityWait(xpath);
            driver.findElement(By.xpath(xpath)).click();
            AutoLogger.log.info("点击元素完成");
        } catch (Exception e) {
            AutoLogger.log.info("点击异常，请检查");
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
    }

    /**
     * 文本框输入
     *
     * @param xpath 元素表达式
     * @param text  文本内容
     */
    public void input(String xpath, String text) {
        try {
            explicityWait(xpath);
            driver.findElement(By.xpath(xpath)).clear();
            driver.findElement(By.xpath(xpath)).sendKeys(text);
            AutoLogger.log.info("输入完成");
        } catch (Exception e) {
            AutoLogger.log.info("输入异常，请检查");
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
    }

    /**
     * 切换context
     *
     * @param contextName context名称
     */
    public void switchContext(String contextName) {
        try {
            driver.context(contextName);
            AutoLogger.log.info("context已切换");
        } catch (Exception e) {
            AutoLogger.log.info("context切换异常");
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
    }

    /**
     * 截图并保存
     *
     * @param name 截图定义名称
     */
    public void saveScrShot(String name) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String createDate = sdf.format(date);
        String scrName = "product/screenshot/" + name + createDate + ".png";
        File scrShot = new File(scrName);
        File tmp = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(tmp, scrShot);
            AutoLogger.log.info("截图完成");
        } catch (IOException e) {
            AutoLogger.log.info("截图异常，请检查");
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
    }

    /**
     * 使用adb方式滑动屏幕，设定起点、终点坐标
     *
     * @param startX 起点x轴坐标
     * @param startY 起点y轴坐标
     * @param endX   终点x轴坐标
     * @param endY   终点y轴坐标
     */
    public void adbSwiep(String startX, String startY, String endX, String endY) {
        int x = Integer.parseInt(startX);
        int y = Integer.parseInt(startY);
        int x1 = Integer.parseInt(endX);
        int y1 = Integer.parseInt(endY);
        try {
            this.runCmd("adb shell input swipe " + x + " " + y + " " + x1 + " " + y1);
            this.threadWait("2");
            AutoLogger.log.info("滑动屏幕完成");
        } catch (Exception e) {
            AutoLogger.log.info("滑动屏幕异常，请检查");
            AutoLogger.log.info(e, e.fillInStackTrace());
        }
    }

    /**
     * adb方式点击坐标
     *
     * @param xAxis x轴坐标
     * @param yAxis y轴坐标
     */
    public void adbTap(String xAxis, String yAxis) {
        int x = Integer.parseInt(xAxis);
        int y = Integer.parseInt(yAxis);
        try {
            this.runCmd("adb shell input tap " + x + "" + y);
            this.threadWait("1.5");
            AutoLogger.log.info("坐标点击成功");
        } catch (Exception e) {
            AutoLogger.log.info("坐标点击异常，请检查");
            AutoLogger.log.info(e, e.fillInStackTrace());
        }
    }

    /**
     * adb模拟键盘按键事件
     *
     * @param keycode 键盘编码，百度可获得https://www.cnblogs.com/lxwphp/p/9548823.html
     */
    public void adbkeycode(String keycode) {
        int key = Integer.parseInt(keycode);
        try {
            this.runCmd("adb shell input keyevent " + key);
            this.threadWait("1.5");
            AutoLogger.log.info("模拟按键时间完成");
        } catch (Exception e) {
            AutoLogger.log.info("模拟按键时间异常，请检查");
            AutoLogger.log.info(e, e.fillInStackTrace());
        }
    }

    /**
     * 判断元素是否存在
     *
     * @param xpath 元素表达式
     * @param text  断言内容
     */
    public void assertSame(String xpath, String text) {
        try {
            String result = driver.findElement(By.xpath(xpath)).getText();
            if (result.equals(text)) {
                AutoLogger.log.info("测试成功");
            } else {
                AutoLogger.log.info("测试失败");
            }
        } catch (Exception e) {
            AutoLogger.log.info("执行断言异常，请检查");
            AutoLogger.log.info(e, e.fillInStackTrace());
        }
    }
}
