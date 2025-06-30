package tests;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.annotations.*;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;

import utils.DriverManager;
import utils.ScreenshotUtil;

import java.net.MalformedURLException;
import java.util.List;

public class LoginFlowTest {

    private AndroidDriver driver;
    private WebDriverWait wait;

    @BeforeClass            //Taken directly from DriverManager.java
    public void setup() throws MalformedURLException {
        driver = (AndroidDriver) DriverManager.getDriver();
    }

    @Test
    public void loginFlowTest() throws InterruptedException {
        // Step 1: Wait for intro animation (2s)
        Thread.sleep(2000);

        // Step 2: Handle notification permission dialog if visible
        try {
            By allowButton = AppiumBy.id("com.android.permissioncontroller:id/permission_allow_button");
            if (driver.findElement(allowButton).isDisplayed()) {
                driver.findElement(allowButton).click();
            }
        } catch (Exception e) {
            System.out.println("🔔 Notification permission not shown.");
        }

        // Step 3: Tap on Login button
        By loginButton = AppiumBy.accessibilityId("Login");
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();

        // Step 4: Enter mobile number
        By phoneField = AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").focused(true)");
        driver.findElement(phoneField).sendKeys("7668273696");

        // Step 5: Tap on Get OTP button
        By getOtpButton = AppiumBy.accessibilityId("Get OTP");
        driver.findElement(getOtpButton).click();

        // Step 6: Fetch OTP from notifications (fallback to master OTP)
        String otp = "1982";
        try {
            driver.openNotifications();
            Thread.sleep(2000); // allow notifications to load
            List<WebElement> allMessages = driver.findElements(AppiumBy.className("android.widget.TextView"));
            for (WebElement el : allMessages) {
                String msg = el.getText();
                if (msg.contains("OTP")) {
                    otp = msg.replaceAll("\\D+", "");
                    System.out.println("✅ Extracted OTP: " + otp);
                    break;
                }
            }
            driver.pressKey(new KeyEvent(AndroidKey.BACK)); // close notification drawer
        } catch (Exception e) {
            System.out.println("⚠️ Could not read OTP, using fallback: 1982");
        }

        // Step 7: Enter OTP
        By otpField = AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\").focused(true)");
        driver.findElement(otpField).sendKeys(otp);

        // Step 8: Wait for successful navigation or retry
        try {
            By dashboardBanner = AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.ImageView\").index(1)");
            wait.until(ExpectedConditions.presenceOfElementLocated(dashboardBanner));
            System.out.println("✅ Logged in successfully.");
            ScreenshotUtil.takeScreenshot(driver, "dashboard_success");
        } catch (Exception e) {
            System.out.println("🔁 Retrying OTP input...");
            driver.findElement(otpField).clear();
            driver.findElement(otpField).sendKeys(otp);
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterMethod
    public void captureFailureScreenshot(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            String testName = result.getName();
            ScreenshotUtil.takeScreenshot(driver, "FAIL_" + testName);
        }
    }
}