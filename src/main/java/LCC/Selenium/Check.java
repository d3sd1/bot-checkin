package LCC.Selenium;

import LCC.Logger.LogService;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Service
public class Check {

    @Autowired
    private LogService logger;
    private WebDriver driver;


    @PostConstruct
    public void configure() {
        this.logger.info("Configure SELENIUM");
        /*
        Check if there is a driver available. If not, just wait.
         */
        String os = System.getProperty("os.name").toLowerCase();
        String bits = System.getProperty("sun.arch.data.model");
        String basePath = "src/main/resources/drivers/v0.24/geckodriver.";
        String osExtension = "";
        if (os.contains("mac")) {
            osExtension = "mac";
        } else if (os.contains("windows")) {
            osExtension = "win." + bits + ".exe";
        } else if (os.contains("linux")) {
            osExtension = "linux." + bits;
        }
        System.setProperty("webdriver.gecko.driver", basePath + osExtension);
        FirefoxOptions opts = new FirefoxOptions();
        /*
        Si se desean programar múltiples certificados, se hará aquí.
        Se debe cambiar el perfil al deseado con el certificado instalado.
        Para ello, se cogerá de la instalación original y dicha carpeta de perfil se pondrá en
        resources/profiles/firefox/{{profile}}
        indicando abajo su URL. Para múltiples concurrentes, se debería añadir el campo
        certificate a operation y aquí donde pone default coger this.op.getCertificateName()
        siendo por defecto alguno.
         */
        FirefoxProfile profile = new FirefoxProfile();
        profile.setAcceptUntrustedCertificates(true);
        opts.setProfile(profile);
        opts.setHeadless(true);
        try {
            this.driver = new FirefoxDriver(opts);
            driver.manage().window().setPosition(new Point(0, 0));
            driver.manage().window().setSize(new Dimension(1024, 768));
            this.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            if (!e.getMessage().contains("java.net.ConnectException")
                    && !e.getMessage().contains("Socket timeout reading Marionette") && !e.getMessage().contains("Socket timeout reading Marionette")) {
                this.logger.error("Firefox not installed. Please install it before using bot: Except: " + e.getMessage());
            }
        }
    }

    @PreDestroy
    private void destroy() {
        if (this.driver != null) {
            this.driver.close();
            this.driver.quit();
        }
    }

    private void waitPageLoad() {
        ExpectedCondition<Boolean> pageLoadCondition = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
                    }
                };
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(pageLoadCondition);
    }

    public void doLogin(String type) {
        driver.get("http://10.1.12.21/WEB_LCC/");

        driver.findElement(By.id("name")).sendKeys("AG00631981@techmahindra.com");
        driver.findElement(By.id("text_password_login")).sendKeys("Scorpions3");
        driver.findElement(By.name("button_login")).click();
        if(type.equalsIgnoreCase("check_in")) {
            this.doCheckIn();
        }
        else {
            this.doCheckOut();
        }
    }

    public void doCheckIn() {
        driver.findElement(By.cssSelector("#botones_fichas > button:nth-child(1)")).click();
        try {
            Thread.sleep(3000);
            driver.findElement(By.cssSelector("button.confirm")).click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(3000);
            driver.findElement(By.cssSelector("button.confirm")).click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void doCheckOut() {
        driver.findElement(By.cssSelector("#botones_fichas > button:nth-child(2)")).click();
        try {
            Thread.sleep(3000);
            driver.findElement(By.cssSelector("button.confirm")).click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(3000);
            driver.findElement(By.cssSelector("button.confirm")).click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}