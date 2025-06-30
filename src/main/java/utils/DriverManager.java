package utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options; // Changed import
import java.net.MalformedURLException;
import java.net.URL;

public class DriverManager {

    private static AppiumDriver driver;

    public static AppiumDriver getDriver() {
        return driver;
    }

    public static void initializeDriver() {
        try {
            // Use UiAutomator2Options instead of DesiredCapabilities
            UiAutomator2Options options = new UiAutomator2Options();

            // Set capabilities using new methods
            options.setPlatformName("Android");
            options.setDeviceName("Infinix Hot 40i");
            options.setAutomationName("UiAutomator2");
            options.setAppPackage("com.technologies.subtlelabs.doodhvale");
            options.setAppActivity(".MainActivity");
            options.setNoReset(true);

            // Connect to Appium server
            URL appiumServerURL = new URL("http://localhost:4723/wd/hub");
            driver = new AndroidDriver(appiumServerURL, options);

        } catch (MalformedURLException e) {
            throw new RuntimeException("Appium server URL is malformed", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize the Appium driver", e);
        }
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}