package com.speedtest;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Driver;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.openqa.selenium.OutputType.BYTES;

public class ResultFetcher {

    public static void main(String[] args){
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\andyc\\Documents\\geckodriver.exe");
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
        WebDriver driver = new FirefoxDriver(options);

        try {
            driver.get("https://www.speedtest.net");
            driver.manage().window().maximize();
            WebElement consentButton = driver.findElement(By.id("_evidon-banner-acceptbutton"));
            consentButton.click();
            WebElement testButton = driver.findElement(By.className("start-text"));
            ClickCentre(testButton, driver);
            Thread.sleep(30000);
            WebElement results = driver.findElement(By.className("speedtest-container"));
            String[] uploadDownload = GetDownAndUpload(driver);
            saveSpeeds(uploadDownload[0], uploadDownload[1]);
            Calendar cal = new GregorianCalendar();
            String today = cal.get(Calendar.YEAR) + "-" + make2Digit(cal.get(Calendar.MONTH) + 1) + "-" + make2Digit(cal.get(Calendar.DATE));
            String fileName = "C:\\Users\\andyc\\Desktop\\Screenshots\\" + today + ".png";
            saveImage(results.getScreenshotAs(BYTES), fileName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private static String[] GetDownAndUpload(WebDriver driver) throws InterruptedException {
        String[] results = new String[2];
        String down = "";
        while (down.isBlank()){
            down = driver.findElement(By.className("download-speed")).getText();
            Thread.sleep(5000);
        }
        results[0] = down;
        String up = "";
        while (up.isBlank()){
            up = driver.findElement(By.className("upload-speed")).getText();
            Thread.sleep(5000);
        }
        results[1] = up;
        return results;
    }

    private static void saveImage(byte[] fileBytes, String filePath){
        FileOutputStream stream = null;
        try {
            File file = new File(filePath);
            stream = new FileOutputStream(file);
            stream.write(fileBytes);
        } catch (IOException var13) {
            throw new WebDriverException(var13);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException var12) {
                    var12.printStackTrace();
                }
            }
        }
    }

    private static void saveSpeeds(String down, String up){
        Calendar cal = new GregorianCalendar();
        String today = make2Digit(cal.get(Calendar.DATE)) + "/" + make2Digit(cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.YEAR);
        String time = cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);
        String textForFile = today + "," + time + "," + down + "," + up + System.lineSeparator();
        try {
            FileWriter fr = new FileWriter(new File("C:\\Users\\andyc\\Desktop\\Speed\\SpeedTests.csv"), true);
            BufferedWriter br = new BufferedWriter(fr);
            br.write(textForFile);
            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String make2Digit(int i){
        if (i < 10){
            return "0" + i;
        }
        return String.valueOf(i);
    }

    private static void ClickCentre(WebElement webElement, WebDriver driver){
        Actions builder = new Actions(driver);
        builder.moveToElement(webElement).click().build().perform();
    }
}
