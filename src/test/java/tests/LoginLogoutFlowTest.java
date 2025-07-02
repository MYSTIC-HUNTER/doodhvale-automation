package tests;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.DriverManager;
import utils.ElementUtil;
import utils.ScreenshotUtil;

import java.net.MalformedURLException;
import java.time.Duration;
import java.util.List;

public class LoginLogoutFlowTest {

    private AndroidDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setup() {
        DriverManager.initializeDriver();
        driver = (AndroidDriver) DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Test
    public void loginLogoutFlowTest() {
        ElementUtil.handlePermissionPopup(driver, "ALLOW");

        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("Login")));
        driver.findElement(AppiumBy.accessibilityId("Login")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.className("android.widget.EditText")));
        WebElement mobileField = driver.findElement(AppiumBy.className("android.widget.EditText"));
        mobileField.sendKeys("7668273696");
        driver.hideKeyboard();

        try {
            Thread.sleep(1000); // Let UI stabilize after entering mobile number

            WebElement getOtpBtn = ElementUtil.findButtonWithPartialDescription(driver, "Get OTP");
            if (getOtpBtn != null) {
                getOtpBtn.click();
            } else {
                throw new RuntimeException("❌ 'Get OTP' button was not found on the screen.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error while clicking 'Get OTP' button: " + e.getMessage());
            throw new RuntimeException(e);
        }

        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(AppiumBy.className("android.widget.EditText")));
        List<WebElement> otpBoxes = driver.findElements(AppiumBy.className("android.widget.EditText"));

        System.out.println("OTP fields found: " + otpBoxes.size());

        if (otpBoxes.size() >= 4) {
            otpBoxes.get(0).sendKeys("1");
            otpBoxes.get(1).sendKeys("9");
            otpBoxes.get(2).sendKeys("8");
            otpBoxes.get(3).sendKeys("2");
        } else {
            throw new RuntimeException("❌ OTP input fields not found or not enough fields visible. Found: " + otpBoxes.size());
        }

        WebElement bannerClose = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.androidUIAutomator("new UiSelector().className(\"android.view.View\").clickable(true).index(0)")));
        bannerClose.click();

        WebElement moreTab = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.accessibilityId("More\nTab 4 of 4")));
        moreTab.click();

        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(" +
                        "new UiSelector().description(\"Log out\"))"));

        WebElement logoutIcon = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.accessibilityId("Log out")));
        logoutIcon.click();

        WebElement logoutConfirm = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.accessibilityId("Log out")));
        logoutConfirm.click();
    }

    @AfterMethod
    public void onFailure(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            System.out.println("❌ Test failed: " + result.getName());
            System.out.println("❌ Reason: " + result.getThrowable());
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