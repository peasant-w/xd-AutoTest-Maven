import driver.AutoLogger;
import inter.InterOfApp;
import io.appium.java_client.android.AndroidDriver;

import java.util.concurrent.TimeUnit;

public class KeyWordTest implements InterOfApp {
    private AndroidDriver driver = null;

    @Override
    public void threadWait(String waitTime) {
        int time;
        time = Integer.parseInt(waitTime);
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            AutoLogger.log.info(e, e.fillInStackTrace());
        }

    }

    @Override
    public void implicityWait(String xpath) {
        try {
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            AutoLogger.log.info(e, e.fillInStackTrace());
        }
    }
}
