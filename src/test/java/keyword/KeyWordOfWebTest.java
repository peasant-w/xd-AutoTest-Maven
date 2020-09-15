//package keyword;
//
//import driver.AutoLogger;
//import io.qameta.allure.*;
//import org.testng.annotations.*;
//
//import java.io.IOException;
//
//import static org.testng.Assert.*;
//
//public class KeyWordOfWebTest {
//    static KeyWordOfWeb kw;
//
//    @BeforeClass
//    public static void BeforeClass() {
//         kw = new KeyWordOfWeb();
//    }
//
//    @AfterClass
//    public static void tearDownAfterClass() {
//        Runtime runtime = Runtime.getRuntime();
//        String filePath = "cmd /c start C:\\Users\\w\\IdeaProjects\\xd-AutoTest-Maven";
//        String cmd = "cmd /c start allure serve ";
//        try {
//            runtime.exec(filePath);
//            runtime.exec(cmd);
//            AutoLogger.log.info("执行cmd命令"+filePath);
//            AutoLogger.log.info("执行cmd命令"+cmd);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @BeforeMethod
//    public void setUp() {
//        AutoLogger.log.info("开始");
//    }
//
//    @AfterMethod
//    public void tearDown() {
//        AutoLogger.log.info("结束");
//    }
//
//
//    @TmsLink("hf001")
//    @Issue("hf001")
//    @Description("测试打开浏览器方法")
//    @Feature("初始化")
//    @Story("测试打开Chrome浏览器")
//    @Test
//    public void testOpenBrowser() {
//        kw.openBrowser("chrome");
//    }
//    @TmsLink("hf002")
//    @Issue("hf002")
//    @Description("测试访问URL方法")
//    @Story("测试访问www.baidu.com")
//    @Test
//    public void testVisitUrl() {
//        kw.visitUrl("https://www.baidu.com/");
//    }
//}