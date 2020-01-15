import driver.AutoLogger;

public class TestLogger {
    public static void main(String[] args) {
        AutoLogger.log.info("----------这是info级别----------");
        AutoLogger.log.error("----------这是error级别----------");
        AutoLogger.log.debug("----------这是debug级别----------");
    }
}
