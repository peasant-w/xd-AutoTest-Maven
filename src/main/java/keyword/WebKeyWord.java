package keyword;

import driver.*;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author wong
 * @date 2020-04-30
 */
public class WebKeyWord {
    /**
     * 公共driver，用于后续操作浏览器
     */
    private WebDriver driver = null;
    private String text = null;
    //写入操作对象
    public ExcelWriter results;
    //记录当前操作行
    private int writerLine;
    //操作的列
    public static final int RES_COL = 10;
    //结果
    public static final String PASS = "pass";
    public static final String FAIL = "fail";
    //通用时间格式
    public static final String DATE_FORMAT = "yyyyMMddHHmmss";

    public WebKeyWord(ExcelWriter result) {
        results = result;
    }

    /**
     * 设置当前操作行
     *
     * @param line 指定行
     */
    public void setWriterLine(int line) {
        writerLine = line;
    }

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
                    results.writePassCell(writerLine, RES_COL, PASS);
                    break;
                case "firefox":
                    FireFoxDriver fox = new FireFoxDriver("C:\\Program Files\\Mozilla Firefox\\firefox.exe", "Tools/geckodriver.exe");
                    driver = fox.getDriver();
                    implicitlyWait("3");
                    results.writePassCell(writerLine, RES_COL, PASS);
                    break;
                case "ie":
                    IeDriver ie = new IeDriver("Tools/IEDriver.exe");
                    driver = ie.getdriver();
                    implicitlyWait("3");
                    results.writePassCell(writerLine, RES_COL, PASS);
                    break;
                default:
                    results.writePassCell(writerLine, RES_COL, PASS);
                    AutoLogger.log.info("未能找到可用浏览器");
                    break;
            }
        } catch (Exception e) {
            results.writeFailCell(writerLine, RES_COL, FAIL);
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
            results.writePassCell(writerLine, RES_COL, PASS);
            AutoLogger.log.info("浏览器关闭成功");
        } catch (Exception e) {
            results.writeFailCell(writerLine, RES_COL, FAIL);
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
            results.writePassCell(writerLine, RES_COL, PASS);
            AutoLogger.log.info("访问-" + url);
        } catch (Exception e) {
            results.writeFailCell(writerLine, RES_COL, FAIL);
            AutoLogger.log.info("访问失败-" + url);
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
            results.writePassCell(writerLine, RES_COL, PASS);
            AutoLogger.log.info("浏览器窗口大小调整成功");
        } catch (Exception e) {
            results.writeFailCell(writerLine, RES_COL, FAIL);
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
            results.writePassCell(writerLine, RES_COL, PASS);
        } catch (Exception e) {
            results.writeFailCell(writerLine, RES_COL, FAIL);
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
            results.writePassCell(writerLine, RES_COL, PASS);
        } catch (NumberFormatException e) {
            results.writeFailCell(writerLine, RES_COL, FAIL);
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
            results.writePassCell(writerLine, RES_COL, PASS);
            AutoLogger.log.info("等待-" + waitTime + "秒");
        } catch (InterruptedException e) {
            results.writeFailCell(writerLine, RES_COL, FAIL);
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
    }

    /**
     * 文本框输入
     *
     * @param xpath   元素表达式
     * @param content 需要输入的内容
     */
    public void inputByXpath(String xpath, String content) {
        try {
//            explicitlyWait(xpath);
            WebElement element = driver.findElement(By.xpath(xpath));
            element.clear();
            element.sendKeys(content);
            results.writePassCell(writerLine, RES_COL, PASS);
            AutoLogger.log.info(xpath + "输入值成功-" + content);
        } catch (Exception e) {
            results.writeFailCell(writerLine, RES_COL, FAIL);
            saveScrShort(xpath);
            AutoLogger.log.info(xpath + "输入值失败-" + content);
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
    }

    /**
     * 通过css选择器定位元素，并输入值
     *
     * @param css     css元素表达式
     * @param content 需要输入的内容
     */
    public void inputByCss(String css, String content) {
        try {
            WebElement element = driver.findElement(By.cssSelector(css));
            element.clear();
            element.sendKeys(content);
            results.writePassCell(writerLine, RES_COL, PASS);
            AutoLogger.log.info(css + "输入值" + content);
        } catch (Exception e) {
            results.writeFailCell(writerLine, RES_COL, FAIL);
            AutoLogger.log.error(css + "输入值失败-" + content);
            e.printStackTrace();
        }
    }

    /**
     * 鼠标点击
     *
     * @param xpath 元素表达式
     */
    public void clickByxpath(String xpath) {
        try {
//            explicitlyWait(xpath);
            text = this.getText(xpath);
            WebElement element = driver.findElement(By.xpath(xpath));
            element.click();
            results.writePassCell(writerLine, RES_COL, PASS);
            AutoLogger.log.info("点击-" + text);
        } catch (Exception e) {
            results.writeFailCell(writerLine, RES_COL, FAIL);
            AutoLogger.log.error("点击失败-" + text);
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
    }

    /**
     * 通过css选择器定位
     *
     * @param css css表达式
     */
    public void clickByCss(String css) {
        try {
            WebElement element = driver.findElement(By.cssSelector(css));
            element.click();
            results.writePassCell(writerLine, RES_COL, PASS);
            AutoLogger.log.info("点击" + css);
        } catch (Exception e) {
            results.writeFailCell(writerLine, RES_COL, FAIL);
            AutoLogger.log.error("点击失败" + css);
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
//            implicitlyWait("10");
            WebElement frameElement = driver.findElement(By.xpath(xpath));
            driver.switchTo().frame(frameElement);
            results.writePassCell(writerLine, RES_COL, PASS);
            AutoLogger.log.info("切换iframe页面成功");
        } catch (Exception e) {
            results.writeFailCell(writerLine, RES_COL, FAIL);
            AutoLogger.log.error("切换iframe页面失败");
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
    }

    /**
     * 退出至默认iframe页面
     */
    public void outOfDefaultIframe() {
        try {
            driver.switchTo().defaultContent();
            results.writePassCell(writerLine, RES_COL, PASS);
            AutoLogger.log.info("退出默认iframe页面");
        } catch (Exception e) {
            results.writeFailCell(writerLine, RES_COL, FAIL);
            AutoLogger.log.error("退出默认iframe页面失败");
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
    }

    /**
     * 退出至上一个iframe页面
     */
    public void outOfParentIframe() {
        try {
            driver.switchTo().parentFrame();
            results.writePassCell(writerLine, RES_COL, PASS);
            AutoLogger.log.info("退出至上一个iframe页面");
        } catch (Exception e) {
            results.writeFailCell(writerLine, RES_COL, FAIL);
            AutoLogger.log.error("退出至上一个iframe页面失败");
            AutoLogger.log.info(e, e.fillInStackTrace());
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
            results.writePassCell(writerLine, RES_COL, PASS);
            AutoLogger.log.info("窗口切换成功" + target);
        } catch (Exception e) {
            results.writeFailCell(writerLine, RES_COL, FAIL);
            AutoLogger.log.error("窗口切换失败" + target);
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
    }

    /**
     * 处理浏览器弹窗，默认点击确认
     */
    public void alertAccept() {
        try {
            driver.switchTo().alert().accept();
            results.writePassCell(writerLine, RES_COL, PASS);
            AutoLogger.log.info("默认同意alert弹窗");
        } catch (Exception e) {
            results.writeFailCell(writerLine, RES_COL, FAIL);
            AutoLogger.log.error("处理异常，请检查");
            AutoLogger.log.error(e, e.fillInStackTrace());
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
            results.writePassCell(writerLine, RES_COL, PASS);
            AutoLogger.log.info("鼠标悬停到-" + text);
        } catch (Exception e) {
            results.writeFailCell(writerLine, RES_COL, PASS);
            AutoLogger.log.error("处理异常，请检查");
            AutoLogger.log.error(e, e.fillInStackTrace());
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
    public void selectByValue(String xpath, String value) {
        try {
            WebElement element = driver.findElement(By.xpath(xpath));
            Select select = new Select(element);
            select.selectByValue(value);
            results.writePassCell(writerLine, RES_COL, PASS);
            AutoLogger.log.info("选值完成" + value);
        } catch (Exception e) {
            results.writeFailCell(writerLine, RES_COL, FAIL);
            AutoLogger.log.error("选择异常，请检查" + value);
            AutoLogger.log.error(e, e.fillInStackTrace());
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
            results.writePassCell(writerLine, RES_COL, PASS);
            AutoLogger.log.info("js语句执行完成-" + jsCommond);
        } catch (Exception e) {
            results.writeFailCell(writerLine, RES_COL, FAIL);
            AutoLogger.log.error("js语句执行失败，请检查");
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
    }

    /**
     * 截图并保存
     *
     * @param scrName 保存截图的名字
     */
    public void saveScrShort(String scrName) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String creatDate = sdf.format(date);
        //设置文件的保存位置和名称
        String fileName = "product/screenshort/" + scrName + creatDate + ".png";
        //保存新文件
        File scrShort = new File(fileName);
        //截图并保存到临时变量tmp
        File tmp = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            //复制截图到目标文件
            FileUtils.copyFile(tmp, scrShort);
            AutoLogger.log.info("截图完成");
        } catch (IOException e) {
            AutoLogger.log.error("截图失败，请检查");
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
    }

    /**
     * 判断元素文本是否包含预期
     *
     * @param xpath  元素表达式
     * @param expect 预期值
     * @return true or false
     */
    public boolean assertIsContains(String xpath, String expect) {
        boolean result = false;
        String text = getText(xpath);

        try {
            if (text.contains(expect)) {
                result = true;
            } else {
                result = false;
            }
            results.writePassCell(writerLine, RES_COL, PASS);
            AutoLogger.log.info("断言元素文本" + text + "包含预期" + expect + "为" + result);
        } catch (Exception e) {
            results.writeFailCell(writerLine, RES_COL, FAIL);
            AutoLogger.log.error("断言出错，请检查");
            AutoLogger.log.error(e, e.fillInStackTrace());
        }
        return result;
    }
}
