package tests;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.DriverManager;
import utils.ScreenshotUtil;

import java.net.MalformedURLException;

public class LoginLogoutFlowTest {

    private AndroidDriver driver;

    @BeforeClass
    public void setup() throws MalformedURLException {
        driver = (AndroidDriver) DriverManager.getDriver();
    }

    @Test
    public void loginLogoutFlowTest() throws InterruptedException {
        Thread.sleep(2000); // Intro animation

        WebElement loginBtn = driver.findElement(AppiumBy.accessibilityId("Login"));
        loginBtn.click();

        WebElement mobileField = driver.findElement(AppiumBy.className("android.widget.EditText"));
        mobileField.sendKeys("7668273696");

        WebElement getOtpBtn = driver.findElement(AppiumBy.accessibilityId("Get OTP"));
        getOtpBtn.click();

        Thread.sleep(2000); // Wait for OTP screen

        WebElement otpField = driver.findElement(AppiumBy.className("android.widget.EditText"));
        otpField.sendKeys("1982");

        Thread.sleep(3000); // Wait for dashboard

        WebElement bannerClose = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiSelector().className(\"android.view.View\").clickable(true).index(0)"));
        bannerClose.click();

        WebElement moreTab = driver.findElement(AppiumBy.accessibilityId("More\nTab 4 of 4"));
        moreTab.click();

        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(" +
                        "new UiSelector().description(\"Log out\"))"));

        WebElement logoutIcon = driver.findElement(AppiumBy.accessibilityId("Log out"));
        logoutIcon.click();

        WebElement logoutConfirm = driver.findElement(AppiumBy.accessibilityId("Log out"));
        logoutConfirm.click();
    }

    @AfterMethod
    public void onFailure(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            ScreenshotUtil.takeScreenshot(driver, result.getName());
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}