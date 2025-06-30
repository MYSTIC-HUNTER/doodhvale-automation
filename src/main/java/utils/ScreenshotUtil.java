package utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtil {

    public static void takeScreenshot(AppiumDriver driver, String filename) {
        // Format timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        // Default folder and final path
        String folderPath = "screenshots";
        File destFile = new File(folderPath + "/" + filename + "_" + timestamp + ".png");

        // Create folder if missing
        destFile.getParentFile().mkdirs();

        // Capture screenshot
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            Files.copy(srcFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("📸 Screenshot saved at: " + destFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("❌ Screenshot save failed: " + e.getMessage());
        }
    }
}