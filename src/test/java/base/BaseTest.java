package base;

import io.appium.java_client.AppiumDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import utils.DriverManager;

public class BaseTest {

    protected AppiumDriver driver;

    @BeforeClass
    public void setUp() {
        DriverManager.initializeDriver();
        driver = DriverManager.getDriver();
    }

    @AfterClass
    public void tearDown() {
        DriverManager.quitDriver();
    }
}