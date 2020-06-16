package com.speedtest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

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
            driver.get("https://www.google.co.uk/search?hl=en&q=speed+test&meta=");
            driver.manage().window().maximize();
            WebElement testButton = driver.findElement(By.id("knowledge-verticals-internetspeedtest__test_button"));
            testButton.click();
            Thread.sleep(30000);
            WebElement results = driver.findElement(By.className("AU64fe"));
            String downloadSpeed = driver.findElement(By.id("knowledge-verticals-internetspeedtest__download")).getText().split("\\n")[0];
            String uploadSpeed = driver.findElement(By.id("knowledge-verticals-internetspeedtest__upload")).getText().split("\\n")[0];
            saveSpeeds(downloadSpeed, uploadSpeed);
            Calendar cal = new GregorianCalendar();
            String today = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE);
            String fileName = "C:\\Users\\andyc\\Desktop\\Screenshots\\" + today + ".png";
            saveImage(results.getScreenshotAs(BYTES), fileName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
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
        String today = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.YEAR);
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

}
