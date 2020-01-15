import driver.AutoLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import driver.GoogleDriver;

public class TestGoogleDriver {
    public static void main(String[] args){
        GoogleDriver googledriver = new GoogleDriver("Tools/chromedriver.exe");
        WebDriver driver = googledriver.getDriver();
        AutoLogger.log.info("==========测试开始==========");
        driver.get("http://172.16.8.38:8080/");
        driver.findElement(By.xpath("//input[@name='username']")).clear();
        driver.findElement(By.xpath("//input[@name='username']")).sendKeys("admin");
        driver.findElement(By.xpath("//input[@name='password']")).clear();
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys("111111");
        driver.findElement(By.xpath("/html/body/div/div/form/button")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[2]/a")).click();
        driver.close();
        AutoLogger.log.info("==========测试结束==========");
    }
}
