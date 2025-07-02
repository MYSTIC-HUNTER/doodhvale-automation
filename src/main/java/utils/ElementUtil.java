package utils;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ElementUtil {

    public static WebElement findButtonWithPartialDescription(AndroidDriver driver, String keyword) {
        List<WebElement> buttons = driver.findElements(AppiumBy.className("android.widget.Button"));
        for (WebElement btn : buttons) {
            String desc = btn.getAttribute("contentDescription");
            if (desc != null && desc.contains(keyword)) {
                return btn;
            }
        }
        return null;
    }

    public static void handlePermissionPopup(AndroidDriver driver, String buttonText) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement button = shortWait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.androidUIAutomator("new UiSelector().text(\"" + buttonText + "\")")
            ));
            button.click();
        } catch (Exception ignored) {}
    }

}