package com.speedtest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.openqa.selenium.OutputType.BYTES;

public class ResultFetcher {

    public static void main(String[] args){
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\andyc\\Documents\\chromedriver.exe");
        WebDriver driver = new ChromeDriver(new ChromeOptions().setHeadless(true));
        driver.manage().window().maximize();
        try {
            driver.navigate().to("https://www.google.co.uk/search?hl=en&q=speed+test&meta=");
            WebElement testButton = driver.findElement(By.id("knowledge-verticals-internetspeedtest__test_button"));
            //WebElement testButton = driver.findElementById("knowledge-verticals-internetspeedtest__test_button");
            testButton.click();
            Thread.sleep(30000);
            WebElement results = driver.findElement(By.className("AU64fe"));

            String downloadSpeed = driver.findElement(By.id("knowledge-verticals-internetspeedtest__download")).getText().split("\\n")[0];
            String uploadSpeed = driver.findElement(By.id("knowledge-verticals-internetspeedtest__upload")).getText().split("\\n")[0];
            saveSpeeds(downloadSpeed, uploadSpeed);

            Calendar cal = new GregorianCalendar();
            String today = cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.YEAR);
            String fileName = "C:\\Users\\andyc\\Desktop\\Screenshots\\" + today + ".png";
            saveImage(results.getScreenshotAs(BYTES), fileName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.close();
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
