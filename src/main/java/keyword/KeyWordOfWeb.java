package keyword;

import driver.AutoLogger;
import driver.FireFoxDriver;
import driver.GoogleDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author wong
 * @date 2020-04-30
 */
public class KeyWordOfWeb {
    /**
     * 公共driver，用于后续操作浏览器
     */
    public WebDriver driver = null;
    private String text = null;

    /**
     * 启动浏览器
     *
     * @param browserType 浏览器类型
     */
    public void openBrowser(String browserType) {
        try {
            switch (browserType) {
                case "chrome":
                    GoogleDriver google = new GoogleDriver("Tools/chromedriver.exe");
                    driver = google.getDriver();
                    implicitlyWait("3");
                    AutoLogger.log.info("谷歌浏览器启动成功");
                    break;
                case "firefox":
                    FireFoxDriver fox = new FireFoxDriver("C:\\Program Files\\Mozilla Firefox\\firefox.exe", "Tools/geckodriver.exe");
                    driver = fox.getDriver();
                    implicitlyWait("3");
                    AutoLogger.log.info("火狐浏览器启动成功");
                    break;
                case "ie":
                    implicitlyWait("3");
                    AutoLogger.log.info("暂不支持该浏览器");
                    break;
                default:
                    AutoLogger.log.info("未能找到可用浏览器");
                    break;
            }
        } catch (Exception e) {
            AutoLogger.log.info("浏览器启动失败，请检查配置");
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
    }

    /**
     * 关闭浏览器
     */
    public void closeBrowser() {
        try {
            driver.quit();
            halt("2");
            AutoLogger.log.info("浏览器关闭成功");
        } catch (Exception e) {
            AutoLogger.log.info("浏览器关闭失败");
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
    }

    /**
     * 访问网页
     *
     * @param url 访问地址
     */
    public void visitUrl(String url) {
        try {
            driver.get(url);
            AutoLogger.log.info("访问-" + url);
        } catch (Exception e) {
            AutoLogger.log.info("访问失败-" + url);
            e.printStackTrace();
        }
    }

    /**
     * 浏览器窗口最大化
     */
    public void maxWindow() {
        try {
            driver.manage().window().maximize();
            AutoLogger.log.info("窗口最大化成功");
        } catch (Exception e) {
            AutoLogger.log.info("窗口最大化失败");
            e.printStackTrace();
        }
    }

    /**
     * 自定义浏览器窗口位置大小
     *
     * @param startOne 起始坐标x轴
     * @param startTwo 起始坐标y轴
     * @param endOne   结束坐标x轴
     * @param endTwo   结束坐标y轴
     */
    public void setWindow(String startOne, String startTwo, String endOne, String endTwo) {
        try {
            int x1 = Integer.parseInt(startOne);
            int y1 = Integer.parseInt(startTwo);
            int x2 = Integer.parseInt(endOne);
            int y2 = Integer.parseInt(endTwo);
            Point p = new Point(x1, y1);
            Dimension d = new Dimension(x2, y2);
            driver.manage().window().setPosition(p);
            driver.manage().window().setSize(d);
            AutoLogger.log.info("浏览器窗口大小调整成功");
        } catch (Exception e) {
            AutoLogger.log.info("浏览器窗口大小调整失败");
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
    }

    /**
     * 显示等待，每次定位元素时，先尝试找元素,默认10s超时时间
     *
     * @param xpath 元素表达式
     */
    private void explicitlyWait(final String xpath) {
        try {
            WebDriverWait eWait = new WebDriverWait(driver, 10);
            eWait.until(new ExpectedCondition<WebElement>() {
                @Override
                public WebElement apply(WebDriver d) {
                    return d.findElement(By.xpath(xpath));
                }
            });
        } catch (Exception e) {
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
    }

    /**
     * 隐式等待，规定时间范围内未找到元素抛出异常
     *
     * @param waitTime 等待时间秒数
     */
    public void implicitlyWait(String waitTime) {
        try {
            int time;
            time = Integer.parseInt(waitTime);
            driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
        } catch (NumberFormatException e) {
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
    }

    /**
     * 强制等待设定时间
     *
     * @param waitTime 等待时间秒数
     */
    public void halt(String waitTime) {
        try {
            int time = Integer.parseInt(waitTime);
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
    }

    /**
     * 文本框输入
     *
     * @param xpath   元素表达式
     * @param content 需要输入的内容
     */
    public void input(String xpath, String content) {
        try {
            explicitlyWait(xpath);
            WebElement element = driver.findElement(By.xpath(xpath));
            element.clear();
            element.sendKeys(content);
            AutoLogger.log.info("输入值成功-" + content);
        } catch (Exception e) {
            AutoLogger.log.info("输入不成功-" + content);
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
    }

    /**
     * 鼠标点击
     *
     * @param xpath 元素表达式
     */
    public void click(String xpath) {
        try {
            explicitlyWait(xpath);
            text = this.getText(xpath);
            WebElement element = driver.findElement(By.xpath(xpath));
            element.click();
            AutoLogger.log.info("点击成功-" + text);
        } catch (Exception e) {
            AutoLogger.log.info("点击失败-" + text);
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
    }

    /**
     * 切换iframe子页面
     *
     * @param xpath 元素表达式
     */
    public void intoIframe(String xpath) {
        try {
//            explicitlyWait("xpath");
            implicitlyWait("10");
            WebElement frameElement = driver.findElement(By.xpath(xpath));
            driver.switchTo().frame(frameElement);
            AutoLogger.log.info("切换iframe页面成功");
        } catch (Exception e) {
            AutoLogger.log.info("切换iframe页面失败");
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
    }

    /**
     * 退出iframe子页面
     */
    public void outIframe() {
        try {
            driver.switchTo().defaultContent();
            AutoLogger.log.info("退出iframe页面成功");
        } catch (Exception e) {
            AutoLogger.log.info("退出iframe页面失败");
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
    }

    /**
     * 通过窗口标题切换浏览器窗口
     *
     * @param target 浏览器窗口标题
     */
    public void switchWindow(String target) {
        String h = null;
        Set<String> handles = driver.getWindowHandles();
        for (String s : handles) {
            if (driver.switchTo().window(s).getTitle().equals(target)) {
                h = s;
            }
        }
        try {
            driver.switchTo().window(h);
            AutoLogger.log.info("窗口切换成功" + target);
        } catch (Exception e) {
            AutoLogger.log.info("窗口切换失败" + target);
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
    }

    /**
     * 处理浏览器弹窗，默认点击确认
     */
    public void alertAccept() {
        try {
            driver.switchTo().alert().accept();
            AutoLogger.log.info("默认同意alert弹窗");
        } catch (Exception e) {
            AutoLogger.log.info("处理异常，请检查");
            AutoLogger.log.info(e, e.fillInStackTrace());
        }

    }

    /**
     * 鼠标悬停动作
     *
     * @param xpath 元素表达式
     */
    public void hover(String xpath) {
        try {
            explicitlyWait(xpath);
            String text = this.getText(xpath);
            WebElement actionElement = driver.findElement(By.xpath(xpath));
            Actions action = new Actions(driver);
            action.moveToElement(actionElement).click().perform();
            AutoLogger.log.info("鼠标悬停到-" + text);
        } catch (Exception e) {
            AutoLogger.log.info("处理异常，请检查");
            AutoLogger.log.info(e, e.fillInStackTrace());
        }

    }

    /**
     * 获取元素文本
     *
     * @param xpath 元素表达式
     * @return 返回文本
     */
    private String getText(String xpath) {
        String elementText;
        WebElement element = driver.findElement(By.xpath(xpath));
        elementText = element.getText();
        if (elementText != null) {
            return elementText;
        }
        return "该元素没有文本";

    }

    /**
     * 通过下拉框value值定位
     *
     * @param xpath 元素表达式
     * @param value 下拉选项值
     */
    public void select(String xpath, String value) {
        try {
            WebElement element = driver.findElement(By.xpath(xpath));
            Select select = new Select(element);
            select.selectByValue(value);
            AutoLogger.log.info("选值完成");
        } catch (Exception e) {
            AutoLogger.log.info("选择异常，请检查");
            AutoLogger.log.info(e, e.fillInStackTrace());
        }

    }

    /**
     * 执行js语句
     *
     * @param jsCommond js语句
     */
    public void runJs(String jsCommond) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(jsCommond);
            AutoLogger.log.info("js语句执行完成-" + jsCommond);
        } catch (Exception e) {
            AutoLogger.log.info("js语句执行失败，请检查");
            AutoLogger.log.info(e, e.fillInStackTrace());
        }

    }
}
